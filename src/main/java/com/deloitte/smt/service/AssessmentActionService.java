package com.deloitte.smt.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.deloitte.smt.constant.AttachmentType;
import com.deloitte.smt.constant.SignalStatus;
import com.deloitte.smt.entity.SignalAction;
import com.deloitte.smt.entity.SignalURL;
import com.deloitte.smt.entity.TaskInst;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.exception.ErrorType;
import com.deloitte.smt.exception.ExceptionBuilder;
import com.deloitte.smt.repository.AssessmentActionRepository;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.SignalURLRepository;
import com.deloitte.smt.repository.TaskInstRepository;
import com.deloitte.smt.util.SignalUtil;

/**
 * Created by RajeshKumar on 10-04-2017.
 */
@Transactional
@Service
public class AssessmentActionService {

	@Autowired
	MessageSource messageSource;
	
	@Autowired
	ExceptionBuilder  exceptionBuilder;
	
    @Autowired
    private TaskService taskService;

    @Autowired
    TaskInstRepository taskInstRepository;

    @Autowired
    AssessmentActionRepository assessmentActionRepository;

    @Autowired
    AttachmentService attachmentService;
    
    @Autowired
    AssessmentPlanRepository assessmentPlanRepository;
    
    @Autowired
    SignalURLRepository signalURLRepository;

    public SignalAction createAssessmentAction(SignalAction signalAction, MultipartFile[] attachments) throws IOException, ApplicationException {
        if(signalAction.getCaseInstanceId() != null && SignalStatus.COMPLETED.name().equalsIgnoreCase(signalAction.getActionStatus())){
            Task task = taskService.createTaskQuery().caseInstanceId(signalAction.getCaseInstanceId()).singleResult();
            taskService.complete(task.getId());
        }
        Task task = taskService.newTask();
        task.setCaseInstanceId(signalAction.getCaseInstanceId());
        task.setName(signalAction.getActionName());
        taskService.saveTask(task);
        List<Task> list = taskService.createTaskQuery().caseInstanceId(signalAction.getCaseInstanceId()).list();
        TaskInst taskInstance = new TaskInst();
        taskInstance.setId(list.get(list.size()-1).getId());
        taskInstance.setCaseDefKey("assessment");
        taskInstance.setTaskDefKey("assessment");
        taskInstance.setCaseInstId(signalAction.getCaseInstanceId());
        taskInstance.setStartTime(new Date());
        signalAction.setTaskId(taskInstance.getId());
        taskInstRepository.save(taskInstance);
        Date d = new Date();
        signalAction.setCreatedDate(d);
        signalAction.setLastModifiedDate(d);
        signalAction.setActionStatus("New");
        
        Long actionsExist=assessmentActionRepository.countByActionNameIgnoreCaseAndAssessmentId(signalAction.getActionName(), signalAction.getAssessmentId());
    	if (actionsExist > 0) {
			throw exceptionBuilder.buildException(ErrorType.ASSESSMENTACCTION_NAME_DUPLICATE, null);
		}

    	
        SignalAction signalActionUpdated = assessmentActionRepository.save(signalAction);
        attachmentService.addAttachments(signalActionUpdated.getId(), attachments, AttachmentType.ASSESSMENT_ACTION_ATTACHMENT, null, signalActionUpdated.getFileMetadata(), signalActionUpdated.getCreatedBy());
        if(!CollectionUtils.isEmpty(signalActionUpdated.getSignalUrls())){
        	for(SignalURL url:signalActionUpdated.getSignalUrls()){
        		url.setTopicId(signalActionUpdated.getId());
        		url.setCreatedDate(signalAction.getCreatedDate());
				url.setCreatedBy(signalAction.getCreatedBy());
				url.setModifiedBy(signalAction.getModifiedBy());
				url.setModifiedDate(signalAction.getLastModifiedDate());
        	}
        	signalURLRepository.save(signalActionUpdated.getSignalUrls());
        }
        return signalActionUpdated;
    }

    public void updateAssessmentAction(SignalAction signalAction, MultipartFile[] attachments) throws ApplicationException {
        if(signalAction.getId() == null) {
            throw new ApplicationException("Failed to update Action. Invalid Id received");
        }
        if("completed".equalsIgnoreCase(signalAction.getActionStatus())) {
            taskService.complete(signalAction.getTaskId());
        }
        signalAction.setLastModifiedDate(new Date());
        assessmentActionRepository.save(signalAction);
        attachmentService.addAttachments(signalAction.getId(), attachments, AttachmentType.ASSESSMENT_ACTION_ATTACHMENT, signalAction.getDeletedAttachmentIds(), signalAction.getFileMetadata(), signalAction.getCreatedBy());
        List<SignalAction> actions = findAllByAssessmentId(signalAction.getAssessmentId(), null);
        boolean allTasksCompletedFlag = true;
        if(!CollectionUtils.isEmpty(actions)){
        	for(SignalAction action:actions){
        		if(!"Completed".equals(action.getActionStatus())){
        			allTasksCompletedFlag = false;
        		}
        	}
        }
        if(!CollectionUtils.isEmpty(signalAction.getSignalUrls())){
        	for(SignalURL url:signalAction.getSignalUrls()){
        		url.setTopicId(signalAction.getId());
        		url.setCreatedDate(signalAction.getCreatedDate());
				url.setCreatedBy(signalAction.getCreatedBy());
				url.setModifiedBy(signalAction.getModifiedBy());
				url.setModifiedDate(signalAction.getLastModifiedDate());
        	}
        	signalURLRepository.save(signalAction.getSignalUrls());
        }
        if(allTasksCompletedFlag){
        	assessmentPlanRepository.updateAssessmentTaskStatus("Completed", Long.valueOf(signalAction.getAssessmentId()));
        }
    }

    public SignalAction findById(Long id) {
        SignalAction signalAction = assessmentActionRepository.findOne(id);
        if("New".equalsIgnoreCase(signalAction.getActionStatus())){
            signalAction.setActionStatus("In Progress");
            signalAction = assessmentActionRepository.save(signalAction);
        }
        signalAction.setSignalUrls(signalURLRepository.findByTopicId(signalAction.getId()));
        return signalAction;
    }

    public List<SignalAction> findAllByAssessmentId(String assessmentId, String actionStatus) {
        if(actionStatus != null) {
            return assessmentActionRepository.findAllByAssessmentIdAndActionStatus(assessmentId, actionStatus);
        }
        return assessmentActionRepository.findAllByAssessmentId(assessmentId);
    }

    public void delete(Long assessmentActionId, String taskId) throws ApplicationException {
        SignalAction signalAction = assessmentActionRepository.findOne(assessmentActionId);
        if(signalAction == null) {
            throw new ApplicationException("Failed to delete Action. Invalid Id received");
        }
        assessmentActionRepository.delete(signalAction);
        if(taskId != null){
        	taskService.deleteTask(taskId);
        }
    }
    
    public SignalAction createOrphanAssessmentAction(SignalAction signalAction, MultipartFile[] attachments) throws IOException {
    	Date d = new Date();
        signalAction.setCreatedDate(d);
        signalAction.setDueDate(SignalUtil.getDueDate(signalAction.getDaysLeft(), signalAction.getCreatedDate()));
        signalAction.setActionStatus("New");
        SignalAction signalActionUpdated = assessmentActionRepository.save(signalAction);
    	attachmentService.addAttachments(signalActionUpdated.getId(), attachments, AttachmentType.ASSESSMENT_ACTION_ATTACHMENT, null, signalActionUpdated.getFileMetadata(), signalActionUpdated.getCreatedBy());
    	if(!CollectionUtils.isEmpty(signalActionUpdated.getSignalUrls())){
        	for(SignalURL url:signalActionUpdated.getSignalUrls()){
        		url.setTopicId(signalActionUpdated.getId());
        		url.setModifiedDate(new Date());
        	}
        	signalURLRepository.save(signalActionUpdated.getSignalUrls());
        }
    	return signalActionUpdated;
    }
    
}
