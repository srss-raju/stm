package com.deloitte.smt.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.camunda.bpm.engine.CaseService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.CaseInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.deloitte.smt.constant.AttachmentType;
import com.deloitte.smt.constant.SmtConstant;
import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.entity.Comments;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.RiskTask;
import com.deloitte.smt.entity.SignalURL;
import com.deloitte.smt.entity.TaskInst;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.AssignmentConfigurationRepository;
import com.deloitte.smt.repository.CommentsRepository;
import com.deloitte.smt.repository.IngredientRepository;
import com.deloitte.smt.repository.LicenseRepository;
import com.deloitte.smt.repository.ProductRepository;
import com.deloitte.smt.repository.RiskPlanRepository;
import com.deloitte.smt.repository.RiskTaskRepository;
import com.deloitte.smt.repository.SignalURLRepository;
import com.deloitte.smt.repository.TaskInstRepository;

/**
 * Created by myelleswarapu on 12-04-2017.
 */
@Transactional
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
    CommentsRepository commentsRepository;
    
    @Autowired
    SignalURLRepository signalURLRepository;
    
    @Autowired
    private AssignmentConfigurationRepository assignmentConfigurationRepository;

    public RiskPlan insert(RiskPlan riskPlan, MultipartFile[] attachments, Long assessmentId) throws ApplicationException {
        CaseInstance instance = caseService.createCaseInstanceByKey("riskCaseId");
        riskPlan.setCaseInstanceId(instance.getCaseInstanceId());
        riskPlan.setStatus("New");
        Date d = new Date();
        riskPlan.setCreatedDate(d);
        riskPlan.setLastModifiedDate(d);
        riskPlan.setRiskTaskStatus("Not Completed");
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
        
        RiskPlan riskPlanUpdated;
        if(assessmentId != null){
            AssessmentPlan assessmentPlan = assessmentPlanRepository.findOne(assessmentId);
            if(assessmentPlan == null) {
                throw new ApplicationException("Assessment Plan not found with the given Id : "+assessmentId);
            }
            assessmentPlan.setRiskPlan(riskPlan);
            riskPlan.setAssessmentPlan(assessmentPlan);
            riskPlanUpdated = riskPlanRepository.save(riskPlan);
            assessmentPlanRepository.save(assessmentPlan);
        } else {
        	riskPlanUpdated = riskPlanRepository.save(riskPlan);
        }
        attachmentService.addAttachments(riskPlanUpdated.getId(), attachments, AttachmentType.RISK_ASSESSMENT, null, riskPlanUpdated.getFileMetadata());
        if(!CollectionUtils.isEmpty(riskPlanUpdated.getSignalUrls())){
        	for(SignalURL url:riskPlanUpdated.getSignalUrls()){
        		url.setTopicId(riskPlanUpdated.getId());
        	}
        	signalURLRepository.save(riskPlanUpdated.getSignalUrls());
        }
        return riskPlanUpdated;
    }

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<RiskPlan> findAllRiskPlansForSearch(SearchDto searchDto) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();

		Root<Topic> topic = criteriaQuery.from(Topic.class);
		Join<Topic, AssessmentPlan> topicAssignmentJoin = topic.join("assessmentPlan", JoinType.INNER);
		Join<AssessmentPlan, RiskPlan> assementRiskJoin = topicAssignmentJoin.join("riskPlan", JoinType.INNER);

		if (null != searchDto) {
			List<Predicate> predicates = new ArrayList<>(10);

			addStatuses(searchDto, criteriaBuilder, assementRiskJoin, predicates);
			addAssignTo(searchDto, criteriaBuilder, assementRiskJoin, predicates);
			addRiskTaskStatus(searchDto, criteriaBuilder, assementRiskJoin, predicates);
			addCreatedOrDueDate(searchDto, criteriaBuilder, assementRiskJoin, predicates);

			Predicate andPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			criteriaQuery
					.multiselect(criteriaBuilder.construct(RiskPlan.class, assementRiskJoin.get("id"),
							assementRiskJoin.get("name"), assementRiskJoin.get("description"),
							assementRiskJoin.get("inDays"), assementRiskJoin.get(SmtConstant.CREATED_DATE.getDescription()),
							assementRiskJoin.get("createdBy"), assementRiskJoin.get("lastModifiedDate"),
							assementRiskJoin.get("summary"), assementRiskJoin.get("caseInstanceId"),
							assementRiskJoin.get(SmtConstant.STATUS.getDescription()), assementRiskJoin.get("ingredient"),
							assementRiskJoin.get("source"), assementRiskJoin.get(SmtConstant.ASSIGN_TO.getDescription()),
							assementRiskJoin.get(SmtConstant.RISK_DUE_DATE.getDescription()), assementRiskJoin.get(SmtConstant.RISK_TASK_STATUS.getDescription())))
					.where(andPredicate).orderBy(criteriaBuilder.desc(topicAssignmentJoin.get(SmtConstant.CREATED_DATE.getDescription())));

		} else {
			criteriaQuery
					.multiselect(criteriaBuilder.construct(RiskPlan.class, assementRiskJoin.get("id"),
							assementRiskJoin.get("name"), assementRiskJoin.get("description"),
							assementRiskJoin.get("inDays"), assementRiskJoin.get(SmtConstant.CREATED_DATE.getDescription()),
							assementRiskJoin.get("createdBy"), assementRiskJoin.get("lastModifiedDate"),
							assementRiskJoin.get("summary"), assementRiskJoin.get("caseInstanceId"),
							assementRiskJoin.get(SmtConstant.STATUS.getDescription()), assementRiskJoin.get("ingredient"),
							assementRiskJoin.get("source"), assementRiskJoin.get(SmtConstant.ASSIGN_TO.getDescription()),
							assementRiskJoin.get(SmtConstant.RISK_DUE_DATE.getDescription()), assementRiskJoin.get(SmtConstant.RISK_TASK_STATUS.getDescription())))
					.orderBy(criteriaBuilder.desc(topicAssignmentJoin.get(SmtConstant.CREATED_DATE.getDescription())));
		}

		TypedQuery<RiskPlan> q = entityManager.createQuery(criteriaQuery);
		return q.getResultList();
	}

	/**
	 * @param searchDto
	 * @param criteriaBuilder
	 * @param assementRiskJoin
	 * @param predicates
	 */
	private void addCreatedOrDueDate(SearchDto searchDto,
			CriteriaBuilder criteriaBuilder,
			Join<AssessmentPlan, RiskPlan> assementRiskJoin,
			List<Predicate> predicates) {
		if (null != searchDto.getStartDate()) {
			if (searchDto.isDueDate()) {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(assementRiskJoin.get(SmtConstant.RISK_DUE_DATE.getDescription()),
						searchDto.getStartDate()));
				if (null != searchDto.getEndDate()) {
					predicates.add(criteriaBuilder.lessThanOrEqualTo(assementRiskJoin.get(SmtConstant.RISK_DUE_DATE.getDescription()),
							searchDto.getEndDate()));
				}

			} else {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(assementRiskJoin.get(SmtConstant.CREATED_DATE.getDescription()),
						searchDto.getStartDate()));
				if (null != searchDto.getEndDate()) {
					predicates.add(criteriaBuilder.lessThanOrEqualTo(assementRiskJoin.get(SmtConstant.CREATED_DATE.getDescription()),
							searchDto.getEndDate()));
				}
			}

		}
	}

	/**
	 * @param searchDto
	 * @param criteriaBuilder
	 * @param assementRiskJoin
	 * @param predicates
	 */
	private void addRiskTaskStatus(SearchDto searchDto,
			CriteriaBuilder criteriaBuilder,
			Join<AssessmentPlan, RiskPlan> assementRiskJoin,
			List<Predicate> predicates) {
		if (!CollectionUtils.isEmpty(searchDto.getRiskTaskStatus())) {
			predicates.add(criteriaBuilder.isTrue(assementRiskJoin.get(SmtConstant.RISK_TASK_STATUS.getDescription()).in(searchDto.getRiskTaskStatus())));
		}
	}

	/**
	 * @param searchDto
	 * @param criteriaBuilder
	 * @param assementRiskJoin
	 * @param predicates
	 */
	private void addAssignTo(SearchDto searchDto,
			CriteriaBuilder criteriaBuilder,
			Join<AssessmentPlan, RiskPlan> assementRiskJoin,
			List<Predicate> predicates) {
		if (!CollectionUtils.isEmpty(searchDto.getAssignees())) {
			predicates.add(criteriaBuilder.isTrue(assementRiskJoin.get(SmtConstant.ASSIGN_TO.getDescription()).in(searchDto.getAssignees())));
		}
	}

	/**
	 * @param searchDto
	 * @param criteriaBuilder
	 * @param assementRiskJoin
	 * @param predicates
	 */
	private void addStatuses(SearchDto searchDto,
			CriteriaBuilder criteriaBuilder,
			Join<AssessmentPlan, RiskPlan> assementRiskJoin,
			List<Predicate> predicates) {
		if (!CollectionUtils.isEmpty(searchDto.getStatuses())) {
			predicates.add(criteriaBuilder.isTrue(assementRiskJoin.get(SmtConstant.STATUS.getDescription()).in(searchDto.getStatuses())));
		}
	}

	public void createRiskTask(RiskTask riskTask, MultipartFile[] attachments) throws IOException {
		if(riskTask.getCaseInstanceId() != null && SmtConstant.COMPLETED.getDescription().equalsIgnoreCase(riskTask.getStatus())){
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
        RiskTask riskTaskUpdated = riskTaskRepository.save(riskTask);
        attachmentService.addAttachments(riskTaskUpdated.getId(), attachments, AttachmentType.RISK_TASK_ASSESSMENT, null, riskTaskUpdated.getFileMetadata());
        if(!CollectionUtils.isEmpty(riskTaskUpdated.getSignalUrls())){
        	for(SignalURL url:riskTaskUpdated.getSignalUrls()){
        		url.setTopicId(riskTaskUpdated.getId());
        	}
        	signalURLRepository.save(riskTaskUpdated.getSignalUrls());
        }
	}
	
	public RiskTask findById(Long id) {
        RiskTask riskTask = riskTaskRepository.findOne(id);
        if("New".equalsIgnoreCase(riskTask.getStatus())) {
            riskTask.setStatus("In Progress");
            riskTask = riskTaskRepository.save(riskTask);
        }
        riskTask.setSignalUrls(signalURLRepository.findByTopicId(riskTask.getId()));
        return riskTask;
    }
	
	public List<RiskTask> findAllByRiskId(String riskId, String status) {
        if(status != null){
            return riskTaskRepository.findAllByRiskIdAndStatusOrderByCreatedDateDesc(riskId, status);
        }
        return riskTaskRepository.findAllByRiskIdOrderByCreatedDateDesc(riskId);
    }

    public void delete(Long riskTaskId, String taskId) throws ApplicationException {
        RiskTask riskTask = riskTaskRepository.findOne(riskTaskId);
        if(riskTask == null) {
            throw new ApplicationException("Failed to delete Action. Invalid Id received");
        }
        riskTaskRepository.delete(riskTask);
        taskService.deleteTask(taskId);
    }

    public void updateRiskTask(RiskTask riskTask, MultipartFile[] attachments) throws ApplicationException {
        if(riskTask.getId() == null) {
            throw new ApplicationException("Failed to update Action. Invalid Id received");
        }
        if(SmtConstant.COMPLETED.getDescription().equalsIgnoreCase(riskTask.getStatus())) {
            taskService.complete(riskTask.getTaskId());
        }
        riskTask.setLastUpdatedDate(new Date());
        riskTaskRepository.save(riskTask);
        attachmentService.addAttachments(riskTask.getId(), attachments, AttachmentType.RISK_TASK_ASSESSMENT, riskTask.getDeletedAttachmentIds(), riskTask.getFileMetadata());
        List<RiskTask> risks = findAllByRiskId(riskTask.getRiskId(), null);
        boolean allTasksCompletedFlag = true;
        if(!CollectionUtils.isEmpty(risks)){
        	for(RiskTask risk:risks){
        		if(!SmtConstant.COMPLETED.getDescription().equals(risk.getStatus())){
        			allTasksCompletedFlag = false;
        		}
        	}
        }
        if(!CollectionUtils.isEmpty(riskTask.getSignalUrls())){
        	for(SignalURL url:riskTask.getSignalUrls()){
        		url.setTopicId(riskTask.getId());
        	}
        	signalURLRepository.save(riskTask.getSignalUrls());
        }
        if(allTasksCompletedFlag){
        	riskPlanRepository.updateRiskTaskStatus(SmtConstant.COMPLETED.getDescription(), Long.valueOf(riskTask.getRiskId()));
        }
    }

    public RiskPlan findByRiskId(Long riskId) throws ApplicationException {
        RiskPlan riskPlan = riskPlanRepository.findOne(riskId);
        if(riskPlan == null) {
            throw new ApplicationException("Risk Plan not found with the given Id : "+riskId);
        }
        if("New".equalsIgnoreCase(riskPlan.getStatus())) {
            riskPlan.setStatus("In Progress");
            riskPlan = riskPlanRepository.save(riskPlan);
        }
        riskPlan.setComments(commentsRepository.findByRiskPlanId(riskId));
        riskPlan.setSignalUrls(signalURLRepository.findByTopicId(riskPlan.getId()));
        return riskPlan;
    }

	public void riskPlanSummary(RiskPlan riskPlan, MultipartFile[] attachments)  throws ApplicationException {
		if(riskPlan.getId() == null) {
            throw new ApplicationException("Failed to update Risk. Invalid Id received");
        }
		riskPlan.setLastModifiedDate(new Date());
		riskPlan.setStatus(SmtConstant.COMPLETED.getDescription());
        attachmentService.addAttachments(riskPlan.getId(), attachments, AttachmentType.FINAL_ASSESSMENT, riskPlan.getDeletedAttachmentIds(), riskPlan.getFileMetadata());
        riskPlanRepository.save(riskPlan);
	}
	
	public void updateRiskPlan(RiskPlan riskPlan, MultipartFile[] attachments)  throws ApplicationException {
		if(riskPlan.getId() == null) {
            throw new ApplicationException("Failed to update Risk. Invalid Id received");
        }
		riskPlan.setLastModifiedDate(new Date());
        attachmentService.addAttachments(riskPlan.getId(), attachments, AttachmentType.RISK_ASSESSMENT, riskPlan.getDeletedAttachmentIds(), riskPlan.getFileMetadata());
        riskPlanRepository.save(riskPlan);
        List<Comments> list = riskPlan.getComments();
        if(!CollectionUtils.isEmpty(list)){
        	for(Comments comment:list){
        		comment.setRiskPlanId(riskPlan.getId());
        		comment.setModifiedDate(new Date());
        	}
        }
        commentsRepository.save(riskPlan.getComments());
        
        if(!CollectionUtils.isEmpty(riskPlan.getSignalUrls())){
        	for(SignalURL url:riskPlan.getSignalUrls()){
        		url.setTopicId(riskPlan.getId());
        	}
        	signalURLRepository.save(riskPlan.getSignalUrls());
        }
	}
}
