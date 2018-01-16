package com.deloitte.smt.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.constant.SmtConstant;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.Task;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.exception.ErrorType;
import com.deloitte.smt.exception.ExceptionBuilder;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.TaskRepository;
import com.deloitte.smt.util.SignalUtil;

/**
 * Created by RajeshKumar on 10-04-2017.
 */
@Transactional
@Service
public class TaskService {

	@Autowired
	MessageSource messageSource;
	
	@Autowired
	ExceptionBuilder  exceptionBuilder;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    AssessmentPlanRepository assessmentPlanRepository;
    
    
    /**
     * 
     * @param signalAction
     * @param attachments
     * @return
     * @throws IOException
     * @throws ApplicationException
     */
    public Task createTask(Task signalAction) throws ApplicationException {
        
        Date d = new Date();
        signalAction.setCreatedDate(d);
        signalAction.setLastUpdatedDate(d);
        signalAction.setStatus("New");
        
        Long actionsExist=taskRepository.countByNameIgnoreCaseAndAssessmentPlanId(signalAction.getName(), signalAction.getAssessmentPlanId());
    	if (actionsExist > 0) {
			throw exceptionBuilder.buildException(ErrorType.ASSESSMENT_TASK_NAME_DUPLICATE, null);
		}
    	
        Task signalActionUpdated = taskRepository.save(signalAction);
    	checkAssessmentTaskStatus(signalAction);
        return signalActionUpdated;
    }
    /**
     * 
     * @param signalAction
     * @param attachments
     * @throws ApplicationException
     */
    public Task updateAssessmentAction(Task task) throws ApplicationException {
        if(task.getId() == null) {
            throw new ApplicationException("Failed to update Action. Invalid Id received");
        }
        
        Task actionsExist;
        if(task.getTemplateId() != 0){
        	actionsExist =	taskRepository.findByNameIgnoreCaseAndTemplateId(task.getName(), task.getTemplateId());
        }else{
        	actionsExist =	taskRepository.findByNameIgnoreCaseAndAssessmentPlanId(task.getName(), task.getAssessmentPlanId());
        }
        
    	if(actionsExist != null && (actionsExist.getId().intValue() != task.getId().intValue())){
			throw exceptionBuilder.buildException(ErrorType.ASSESSMENT_TASK_NAME_DUPLICATE, null);
		}
       
    	task.setLastUpdatedDate(new Date());
    	Task taskUpdated = taskRepository.save(task);
        List<Task> actions = findAllByAssessmentId(task.getAssessmentPlanId(), null);
        boolean allTasksCompletedFlag = true;
        if(!CollectionUtils.isEmpty(actions)){
        	for(Task action:actions){
        		if(!SmtConstant.COMPLETED.getDescription().equals(action.getStatus())){
        			allTasksCompletedFlag = false;
        		}
        	}
        }
        if(allTasksCompletedFlag){
        	assessmentPlanRepository.updateAssessmentTaskStatus("Completed", Long.valueOf(task.getAssessmentPlanId()));
        	AssessmentPlan assessmentPlan  = assessmentPlanRepository.findOne(Long.valueOf(task.getAssessmentPlanId()));
        	assessmentPlan.setLastModifiedDate(new Date());
        	assessmentPlan.setModifiedBy(task.getLastUpdatedBy());
        }
        return taskUpdated;
    }
    
    private void checkAssessmentTaskStatus(Task signalAction){
    	 List<Task> actions = findAllByAssessmentId(signalAction.getAssessmentPlanId(), null);
         boolean allTasksCompletedFlag = true;
         if(!CollectionUtils.isEmpty(actions)){
         	for(Task action:actions){
         		if(!"Completed".equals(action.getStatus())){
         			allTasksCompletedFlag = false;
         		}
         	}
         }
         if(signalAction.getAssessmentPlanId() != null){
	         if(allTasksCompletedFlag){
	         	assessmentPlanRepository.updateAssessmentTaskStatus(SmtConstant.COMPLETED.getDescription(), Long.valueOf(signalAction.getAssessmentPlanId()));
	         }else{
	        	 assessmentPlanRepository.updateAssessmentTaskStatus(SmtConstant.NOTCOMPLETED.getDescription(), Long.valueOf(signalAction.getAssessmentPlanId()));
	         }
         }
    }
    
    public Task findById(Long id) {
        Task signalAction = taskRepository.findOne(id);
        if("New".equalsIgnoreCase(signalAction.getStatus())){
            signalAction.setStatus("In Progress");
            signalAction = taskRepository.save(signalAction);
        }
        return signalAction;
    }

    public List<Task> findAllByAssessmentId(Long assessmentId, String actionStatus) {
        if(actionStatus != null) {
            return taskRepository.findAllByAssessmentPlanIdAndStatus(assessmentId, actionStatus);
        }
        return taskRepository.findAllByAssessmentPlanId(assessmentId);
    }

    public void delete(Long assessmentActionId) throws ApplicationException {
        Task signalAction = taskRepository.findOne(assessmentActionId);
        if(signalAction == null) {
            throw new ApplicationException("Failed to delete Action. Invalid Id received");
        }
        taskRepository.delete(signalAction);
        checkAssessmentTaskStatus(signalAction);
    }
    
    public Task createTemplateTask(Task signalAction) throws ApplicationException {
    	
    	Task actionsExist=taskRepository.findByNameIgnoreCaseAndTemplateId(signalAction.getName(), signalAction.getTemplateId());
    	if (actionsExist != null) {
			throw exceptionBuilder.buildException(ErrorType.ASSESSMENT_TASK_NAME_DUPLICATE, null);
		}
    	
    	Date d = new Date();
        signalAction.setCreatedDate(d);
        signalAction.setDueDate(SignalUtil.getDueDate(signalAction.getDaysLeft(), signalAction.getCreatedDate()));
        signalAction.setStatus("New");
    	return taskRepository.save(signalAction);
    }
    
    public List<Task> findAll(String type, Long id) {
    	if("assessmentplan".equalsIgnoreCase(type)){
    		return taskRepository.findAllByAssessmentPlanId(id);
    	}else if("riskplan".equalsIgnoreCase(type)){
    		return taskRepository.findAllByRiskId(id);
    	}else{
    		return taskRepository.findAllByTemplateId(id);
    	}
    }
    
}
