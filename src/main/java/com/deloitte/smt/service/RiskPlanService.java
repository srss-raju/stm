package com.deloitte.smt.service;

import com.deloitte.smt.entity.AttachmentType;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.RiskTask;
import com.deloitte.smt.entity.TaskInst;
import com.deloitte.smt.exception.DeleteFailedException;
import com.deloitte.smt.exception.UpdateFailedException;
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
import java.util.Date;
import java.util.List;

/**
 * Created by myelleswarapu on 12-04-2017.
 */
@Service
public class RiskPlanService {

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

    public void insert(RiskPlan riskPlan) {
        CaseInstance instance = caseService.createCaseInstanceByKey("riskCaseId");
        riskPlan.setCaseInstanceId(instance.getCaseInstanceId());
        riskPlan.setCreatedDate(new Date());
        riskPlanRepository.save(riskPlan);
    }
    
    public List<RiskPlan> findAllRiskPlansByStatus(String status) {
        if(StringUtils.isEmpty(status)) {
            Sort sort = new Sort(Sort.Direction.DESC, "createdDate");
            return riskPlanRepository.findAll(sort);
        }
        return riskPlanRepository.findAllByStatusOrderByCreatedDateDesc(status);
    }

	public void createRiskTask(RiskTask riskTask) {
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
        taskInstRepository.save(taskInstance);
        riskTaskRepository.save(riskTask);
	}
	
	public RiskTask findById(Long id) {
        return riskTaskRepository.findOne(id);
    }
	
	public List<RiskTask> findAllByRiskId(String riskId, String status) {
        if(status != null){
            return riskTaskRepository.findAllByRiskIdAndStatus(riskId, status);
        }
        return riskTaskRepository.findAllByRiskId(riskId);
    }

    public void delete(Long riskTaskId, String taskId) throws DeleteFailedException {
        RiskTask riskTask = riskTaskRepository.findOne(riskTaskId);
        if(riskTask == null) {
            throw new DeleteFailedException("Failed to delete Action. Invalid Id received");
        }
        riskTaskRepository.delete(riskTask);
        taskService.deleteTask(taskId);
    }
    
    public void updateRiskTask(RiskTask riskTask) throws UpdateFailedException {
        if(riskTask.getId() == null) {
            throw new UpdateFailedException("Failed to update Task. Invalid Id received");
        }
        if("completed".equalsIgnoreCase(riskTask.getStatus())) {
            taskService.complete(riskTask.getTaskId());
        }
        riskTask.setLastUpdatedDate(new Date());
        riskTaskRepository.save(riskTask);
    }
    
    public RiskPlan findByRiskId(Long riskId){
        return riskPlanRepository.findOne(riskId);
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
}
