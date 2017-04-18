package com.deloitte.smt.service;

import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.AttachmentType;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.RiskTask;
import com.deloitte.smt.entity.TaskInst;
import com.deloitte.smt.exception.DeleteFailedException;
import com.deloitte.smt.exception.EntityNotFoundException;
import com.deloitte.smt.exception.UpdateFailedException;
import com.deloitte.smt.repository.AssessmentPlanRepository;
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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
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

    public RiskPlan insert(RiskPlan riskPlan, MultipartFile[] attachments, Long assessmentId) throws IOException, EntityNotFoundException {
        CaseInstance instance = caseService.createCaseInstanceByKey("riskCaseId");
        riskPlan.setCaseInstanceId(instance.getCaseInstanceId());
        riskPlan.setStatus("New");
        Date d = new Date();
        riskPlan.setCreatedDate(d);
        riskPlan.setLastModifiedDate(d);
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
        attachmentService.addAttachments(riskPlan.getId(), attachments, AttachmentType.RISK_ASSESSMENT, null);
        return riskPlan;
    }
    
    public List<RiskPlan> findAllRiskPlansForSearch(String statuses, Date createdDate) {
        if(!StringUtils.isEmpty(statuses) && null != createdDate) {
            return riskPlanRepository.findAllByStatusAndCreatedDate(Arrays.asList(statuses.split(",")), createdDate);
        }
        if(!StringUtils.isEmpty(statuses)) {
            return riskPlanRepository.findAllByStatusInOrderByCreatedDateDesc(Arrays.asList(statuses.split(",")));
        }
        if(null != createdDate) {
            return riskPlanRepository.findAllByCreatedDateOrderByCreatedDateDesc(createdDate);
        }
        Sort sort = new Sort(Sort.Direction.DESC, "createdDate");
        return riskPlanRepository.findAll(sort);
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
        taskInstRepository.save(taskInstance);
        riskTask = riskTaskRepository.save(riskTask);
        attachmentService.addAttachments(riskTask.getId(), attachments, AttachmentType.RISK_TASK_ASSESSMENT, null);
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
    
   /* public void updateRiskTask(RiskTask riskTask) throws UpdateFailedException {
        if(riskTask.getId() == null) {
            throw new UpdateFailedException("Failed to update Task. Invalid Id received");
        }
        if("completed".equalsIgnoreCase(riskTask.getStatus())) {
            taskService.complete(riskTask.getTaskId());
        }
        riskTask.setLastUpdatedDate(new Date());
        riskTaskRepository.save(riskTask);
    }*/
    
    public void updateRiskTask(RiskTask riskTask, MultipartFile[] attachments) throws UpdateFailedException, IOException {
        if(riskTask.getId() == null) {
            throw new UpdateFailedException("Failed to update Action. Invalid Id received");
        }
        if("completed".equalsIgnoreCase(riskTask.getStatus())) {
            taskService.complete(riskTask.getTaskId());
        }
        riskTask.setLastUpdatedDate(new Date());
        riskTaskRepository.save(riskTask);
        attachmentService.addAttachments(riskTask.getId(), attachments, AttachmentType.RISK_TASK_ASSESSMENT, null);
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
        attachmentService.addAttachments(riskPlan.getId(), attachments, AttachmentType.FINAL_ASSESSMENT, riskPlan.getDeletedAttachmentIds());
        riskPlanRepository.save(riskPlan);
	}
	
	public void updateRiskPlan(RiskPlan riskPlan, MultipartFile[] attachments)  throws UpdateFailedException, IOException {
		if(riskPlan.getId() == null) {
            throw new UpdateFailedException("Failed to update Risk. Invalid Id received");
        }
		riskPlan.setLastModifiedDate(new Date());
        attachmentService.addAttachments(riskPlan.getId(), attachments, AttachmentType.RISK_ASSESSMENT, riskPlan.getDeletedAttachmentIds());
        riskPlanRepository.save(riskPlan);
	}
}
