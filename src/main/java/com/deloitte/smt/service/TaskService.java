package com.deloitte.smt.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.deloitte.smt.constant.AttachmentType;
import com.deloitte.smt.constant.SmtConstant;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.Attachment;
import com.deloitte.smt.entity.Task;
import com.deloitte.smt.entity.SignalURL;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.exception.ErrorType;
import com.deloitte.smt.exception.ExceptionBuilder;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.SignalURLRepository;
import com.deloitte.smt.repository.TaskRepository;
import com.deloitte.smt.util.JsonUtil;
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
    AttachmentService attachmentService;
    
    @Autowired
    AssessmentPlanRepository assessmentPlanRepository;
    
    @Autowired
    SignalURLRepository signalURLRepository;
    
    @Autowired
    SignalAuditService signalAuditService;
    /**
     * 
     * @param signalAction
     * @param attachments
     * @return
     * @throws IOException
     * @throws ApplicationException
     */
    public Task createAssessmentAction(Task signalAction, MultipartFile[] attachments) throws ApplicationException {
        
        Date d = new Date();
        signalAction.setCreatedDate(d);
        signalAction.setLastUpdatedDate(d);
        signalAction.setStatus("New");
        
        Long actionsExist=taskRepository.countByNameIgnoreCaseAndAssessmentPlanId(signalAction.getName(), signalAction.getAssessmentPlanId());
    	if (actionsExist > 0) {
			throw exceptionBuilder.buildException(ErrorType.ASSESSMENT_TASK_NAME_DUPLICATE, null);
		}
    	
        Task signalActionUpdated = taskRepository.save(signalAction);
        List<Attachment> attachmentList = attachmentService.addAttachments(signalActionUpdated.getId(), attachments, AttachmentType.ASSESSMENT_ACTION_ATTACHMENT, null, signalActionUpdated.getFileMetadata(), signalActionUpdated.getCreatedBy());
        if(!CollectionUtils.isEmpty(signalActionUpdated.getSignalUrls())){
        	for(SignalURL url:signalActionUpdated.getSignalUrls()){
        		url.setTopicId(signalActionUpdated.getId());
        		url.setCreatedDate(signalAction.getCreatedDate());
				url.setCreatedBy(signalAction.getCreatedBy());
				url.setModifiedBy(signalAction.getLastUpdatedBy());
				url.setModifiedDate(signalAction.getLastUpdatedDate());
        	}
        	signalURLRepository.save(signalActionUpdated.getSignalUrls());
        }
    	checkAssessmentTaskStatus(signalAction);
        signalAuditService.saveOrUpdateSignalActionAudit(signalActionUpdated, null, attachmentList, SmtConstant.CREATE.getDescription());
        return signalActionUpdated;
    }
    /**
     * 
     * @param signalAction
     * @param attachments
     * @throws ApplicationException
     */
    public void updateAssessmentAction(Task signalAction, MultipartFile[] attachments) throws ApplicationException {
        if(signalAction.getId() == null) {
            throw new ApplicationException("Failed to update Action. Invalid Id received");
        }
        
        Task actionsExist = null;
        if(signalAction.getTemplateId() != 0){
        	actionsExist =	taskRepository.findByNameIgnoreCaseAndTemplateId(signalAction.getName(), signalAction.getTemplateId());
        }else{
        	actionsExist =	taskRepository.findByNameIgnoreCaseAndAssessmentPlanId(signalAction.getName(), signalAction.getAssessmentPlanId());
        }
        
    	if(actionsExist != null && (actionsExist.getId().intValue() != signalAction.getId().intValue())){
			throw exceptionBuilder.buildException(ErrorType.ASSESSMENT_TASK_NAME_DUPLICATE, null);
		}
       
        String assessmentActionOriginal = JsonUtil.converToJson(taskRepository.findOne(signalAction.getId()));
        signalAction.setLastUpdatedDate(new Date());
        taskRepository.save(signalAction);
        List<Attachment> attachmentList = attachmentService.addAttachments(signalAction.getId(), attachments, AttachmentType.ASSESSMENT_ACTION_ATTACHMENT, signalAction.getDeletedAttachmentIds(), signalAction.getFileMetadata(), signalAction.getCreatedBy());
        List<Task> actions = findAllByAssessmentId(signalAction.getAssessmentPlanId(), null);
        boolean allTasksCompletedFlag = true;
        if(!CollectionUtils.isEmpty(actions)){
        	for(Task action:actions){
        		if(!SmtConstant.COMPLETED.getDescription().equals(action.getStatus())){
        			allTasksCompletedFlag = false;
        		}
        	}
        }
        if(!CollectionUtils.isEmpty(signalAction.getSignalUrls())){
        	for(SignalURL url:signalAction.getSignalUrls()){
        		url.setTopicId(signalAction.getId());
        		url.setCreatedDate(signalAction.getCreatedDate());
				url.setCreatedBy(signalAction.getCreatedBy());
				url.setModifiedBy(signalAction.getLastUpdatedBy());
				url.setModifiedDate(signalAction.getLastUpdatedDate());
        	}
        	signalURLRepository.save(signalAction.getSignalUrls());
        }
        if(allTasksCompletedFlag){
        	String assessmentPlanOriginal = JsonUtil.converToJson(assessmentPlanRepository.findOne(Long.valueOf(signalAction.getAssessmentPlanId())));
        	assessmentPlanRepository.updateAssessmentTaskStatus("Completed", Long.valueOf(signalAction.getAssessmentPlanId()));
        	AssessmentPlan assessmentPlan  = assessmentPlanRepository.findOne(Long.valueOf(signalAction.getAssessmentPlanId()));
        	assessmentPlan.setLastModifiedDate(new Date());
        	assessmentPlan.setModifiedBy(signalAction.getLastUpdatedBy());
        	signalAuditService.saveOrUpdateAssessmentPlanAudit(assessmentPlan, assessmentPlanOriginal, null, SmtConstant.UPDATE.getDescription());
        }
        signalAuditService.saveOrUpdateSignalActionAudit(signalAction, assessmentActionOriginal, attachmentList, SmtConstant.UPDATE.getDescription());
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
        signalAction.setSignalUrls(signalURLRepository.findByTopicId(signalAction.getId()));
        return signalAction;
    }

    public List<Task> findAllByAssessmentId(String assessmentId, String actionStatus) {
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
        signalAuditService.saveOrUpdateSignalActionAudit(signalAction, null, null, SmtConstant.DELETE.getDescription());
    }
    
    public Task createOrphanAssessmentAction(Task signalAction, MultipartFile[] attachments) throws IOException, ApplicationException {
    	
    	Task actionsExist=taskRepository.findByNameIgnoreCaseAndTemplateId(signalAction.getName(), signalAction.getTemplateId());
    	if (actionsExist != null) {
			throw exceptionBuilder.buildException(ErrorType.ASSESSMENT_TASK_NAME_DUPLICATE, null);
		}
    	
    	Date d = new Date();
        signalAction.setCreatedDate(d);
        signalAction.setDueDate(SignalUtil.getDueDate(signalAction.getDaysLeft(), signalAction.getCreatedDate()));
        signalAction.setStatus("New");
        Task signalActionUpdated = taskRepository.save(signalAction);
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
