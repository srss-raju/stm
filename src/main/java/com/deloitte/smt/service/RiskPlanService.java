package com.deloitte.smt.service;

import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.entity.AttachmentType;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.RiskTask;
import com.deloitte.smt.entity.TaskInst;
import com.deloitte.smt.exception.DeleteFailedException;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.AssignmentConfigurationRepository;
import com.deloitte.smt.repository.IngredientRepository;
import com.deloitte.smt.repository.LicenseRepository;
import com.deloitte.smt.repository.ProductRepository;
import com.deloitte.smt.repository.RiskPlanRepository;
import com.deloitte.smt.repository.RiskTaskRepository;
import com.deloitte.smt.repository.TaskInstRepository;
import org.camunda.bpm.engine.CaseService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.CaseInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by myelleswarapu on 12-04-2017.
 */
@Service
public class RiskPlanService {

    @Autowired
    AssessmentPlanRepository assessmentPlanRepository;

    @Autowired
    RiskPlanRepository riskPlanRepository;
    
    @Autowired
    RiskTaskRepository riskTaskRepository;
    
    @Autowired
    private TaskService taskService;

    @Autowired
    TaskInstRepository taskInstRepository;

    @Autowired
    CaseService caseService;
    
    @Autowired
    AttachmentService attachmentService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    LicenseRepository licenseRepository;

    @Autowired
    IngredientRepository ingredientRepository;

    @Autowired
    SearchService searchService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AssignmentConfigurationRepository assignmentConfigurationRepository;

    public RiskPlan insert(RiskPlan riskPlan, MultipartFile[] attachments, Long assessmentId) throws IOException, EntityNotFoundException {
        CaseInstance instance = caseService.createCaseInstanceByKey("riskCaseId");
        riskPlan.setCaseInstanceId(instance.getCaseInstanceId());
        riskPlan.setStatus("New");
        Date d = new Date();
        riskPlan.setCreatedDate(d);
        riskPlan.setLastModifiedDate(d);
        AssignmentConfiguration assignmentConfiguration = null;
        
        if(!StringUtils.isEmpty(riskPlan.getSource())) {
            assignmentConfiguration = assignmentConfigurationRepository.findByIngredientAndSignalSource(riskPlan.getIngredient(), riskPlan.getSource());
        } 
        // If Source is not null and combination not available we have to fetch with Ingredient
        if(assignmentConfiguration == null){
        	assignmentConfiguration = assignmentConfigurationRepository.findByIngredientAndSignalSourceIsNull(riskPlan.getIngredient());
        }
        
        
        if(assignmentConfiguration != null) {
            riskPlan.setAssignTo(assignmentConfiguration.getRiskPlanAssignmentUser());
        }
        if(assessmentId != null){
            AssessmentPlan assessmentPlan = assessmentPlanRepository.findOne(assessmentId);
            if(assessmentPlan == null) {
                throw new EntityNotFoundException("Assessment Plan not found with the given Id : "+assessmentId);
            }
            assessmentPlan.setRiskPlan(riskPlan);
            riskPlan.setAssessmentPlan(assessmentPlan);
            riskPlan = riskPlanRepository.save(riskPlan);
            assessmentPlanRepository.save(assessmentPlan);
        } else {
            riskPlan = riskPlanRepository.save(riskPlan);
        }
        attachmentService.addAttachments(riskPlan.getId(), attachments, AttachmentType.RISK_ASSESSMENT, null, riskPlan.getFileMetadata());
        return riskPlan;
    }

    public List<RiskPlan> findAllRiskPlansForSearch(SearchDto searchDto) {
        List<Long> riskTopicIds = new ArrayList<>();
        boolean searchAll = true;
        if(!CollectionUtils.isEmpty(searchDto.getStatuses())) {
            searchAll = false;
        }
        searchAll = searchService.getSignalIdsForSearch(searchDto, riskTopicIds, searchAll);
        if(searchAll) {
            Sort sort = new Sort(Sort.Direction.DESC, "createdDate");
            return riskPlanRepository.findAll(sort);
        }
        List<RiskPlan> riskPlanList = new ArrayList<>();
        StringBuilder queryString = new StringBuilder("SELECT o FROM RiskPlan o ");
        boolean executeQuery = false;
        if(!CollectionUtils.isEmpty(riskTopicIds)) {
            if (!CollectionUtils.isEmpty(searchDto.getProducts()) || !CollectionUtils.isEmpty(searchDto.getLicenses()) || !CollectionUtils.isEmpty(searchDto.getIngredients())) {
                executeQuery = true;
                queryString.append("INNER JOIN o.assessmentPlan a ");
                queryString.append("INNER JOIN a.topics t WHERE t.id IN :topicIds ");
            }
        }
            if(!CollectionUtils.isEmpty(searchDto.getStatuses())){
                executeQuery = true;
                if(queryString.toString().contains(":topicIds")){
                    queryString.append(" AND o.status IN :riskPlanStatus ");
                } else {
                    queryString.append(" WHERE o.status IN :riskPlanStatus ");
                }
            }
            queryString.append(" ORDER BY o.createdDate DESC");
            Query q = entityManager.createQuery(queryString.toString(), RiskPlan.class);

            if(queryString.toString().contains(":topicIds")){
                if(CollectionUtils.isEmpty(riskTopicIds)) {
                    q.setParameter("topicIds", null);
                } else {
                    q.setParameter("topicIds", riskTopicIds);
                }
            }
            if(queryString.toString().contains(":riskPlanStatus")){
                q.setParameter("riskPlanStatus", searchDto.getStatuses());
            }
        if(executeQuery) {
            riskPlanList = q.getResultList();
        }
        return riskPlanList;
    }

	public void createRiskTask(RiskTask riskTask, MultipartFile[] attachments) throws IOException {
		if(riskTask.getCaseInstanceId() != null && "Completed".equalsIgnoreCase(riskTask.getStatus())){
            Task task = taskService.createTaskQuery().caseInstanceId(riskTask.getCaseInstanceId()).singleResult();
            taskService.complete(task.getId());
        }
        Task task = taskService.newTask();
        task.setCaseInstanceId(riskTask.getCaseInstanceId());
        task.setName(riskTask.getName());
        taskService.saveTask(task);
        List<Task> list = taskService.createTaskQuery().caseInstanceId(riskTask.getCaseInstanceId()).list();
        TaskInst taskInstance = new TaskInst();
        taskInstance.setId(list.get(list.size()-1).getId());
        taskInstance.setCaseDefKey("risk");
        taskInstance.setTaskDefKey("risk");
        taskInstance.setCaseInstId(riskTask.getCaseInstanceId());
        taskInstance.setStartTime(new Date());
        riskTask.setTaskId(taskInstance.getId());
        Date d = new Date();
        riskTask.setCreatedDate(d);
        riskTask.setLastUpdatedDate(d);
        riskTask.setStatus("New");
        RiskPlan riskPlan = riskPlanRepository.findOne(Long.valueOf(riskTask.getRiskId()));
        riskTask.setAssignTo(riskPlan.getAssignTo());
        riskTask.setOwner(riskPlan.getAssignTo());

        taskInstRepository.save(taskInstance);
        riskTask = riskTaskRepository.save(riskTask);
        attachmentService.addAttachments(riskTask.getId(), attachments, AttachmentType.RISK_TASK_ASSESSMENT, null, riskTask.getFileMetadata());
	}
	
	public RiskTask findById(Long id) {
        RiskTask riskTask = riskTaskRepository.findOne(id);
        if("New".equalsIgnoreCase(riskTask.getStatus())) {
            riskTask.setStatus("In Progress");
            riskTask = riskTaskRepository.save(riskTask);
        }
        return riskTask;
    }
	
	public List<RiskTask> findAllByRiskId(String riskId, String status) {
        if(status != null){
            return riskTaskRepository.findAllByRiskIdAndStatusOrderByCreatedDateDesc(riskId, status);
        }
        return riskTaskRepository.findAllByRiskIdOrderByCreatedDateDesc(riskId);
    }

    public void delete(Long riskTaskId, String taskId) throws DeleteFailedException {
        RiskTask riskTask = riskTaskRepository.findOne(riskTaskId);
        if(riskTask == null) {
            throw new DeleteFailedException("Failed to delete Action. Invalid Id received");
        }
        riskTaskRepository.delete(riskTask);
        taskService.deleteTask(taskId);
    }

    public void updateRiskTask(RiskTask riskTask, MultipartFile[] attachments) throws UpdateFailedException, IOException {
        if(riskTask.getId() == null) {
            throw new UpdateFailedException("Failed to update Action. Invalid Id received");
        }
        if("completed".equalsIgnoreCase(riskTask.getStatus())) {
            taskService.complete(riskTask.getTaskId());
        }
        riskTask.setLastUpdatedDate(new Date());
        riskTaskRepository.save(riskTask);
        attachmentService.addAttachments(riskTask.getId(), attachments, AttachmentType.RISK_TASK_ASSESSMENT, riskTask.getDeletedAttachmentIds(), riskTask.getFileMetadata());
    }

    public RiskPlan findByRiskId(Long riskId) throws EntityNotFoundException {
        RiskPlan riskPlan = riskPlanRepository.findOne(riskId);
        if(riskPlan == null) {
            throw new EntityNotFoundException("Risk Plan not found with the given Id : "+riskId);
        }
        if("New".equalsIgnoreCase(riskPlan.getStatus())) {
            riskPlan.setStatus("In Progress");
            riskPlan = riskPlanRepository.save(riskPlan);
        }
        return riskPlan;
    }

	public void riskPlanSummary(RiskPlan riskPlan, MultipartFile[] attachments)  throws UpdateFailedException, IOException {
		if(riskPlan.getId() == null) {
            throw new UpdateFailedException("Failed to update Risk. Invalid Id received");
        }
		riskPlan.setLastModifiedDate(new Date());
		riskPlan.setStatus("Completed");
        attachmentService.addAttachments(riskPlan.getId(), attachments, AttachmentType.FINAL_ASSESSMENT, riskPlan.getDeletedAttachmentIds(), riskPlan.getFileMetadata());
        riskPlanRepository.save(riskPlan);
	}
	
	public void updateRiskPlan(RiskPlan riskPlan, MultipartFile[] attachments)  throws UpdateFailedException, IOException {
		if(riskPlan.getId() == null) {
            throw new UpdateFailedException("Failed to update Risk. Invalid Id received");
        }
		riskPlan.setLastModifiedDate(new Date());
        attachmentService.addAttachments(riskPlan.getId(), attachments, AttachmentType.RISK_ASSESSMENT, riskPlan.getDeletedAttachmentIds(), riskPlan.getFileMetadata());
        riskPlanRepository.save(riskPlan);
	}
}
