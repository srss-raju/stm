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
import com.deloitte.smt.entity.SignalAction;
import com.deloitte.smt.entity.SignalURL;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.exception.ErrorType;
import com.deloitte.smt.exception.ExceptionBuilder;
import com.deloitte.smt.repository.AssessmentActionRepository;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.SignalURLRepository;
import com.deloitte.smt.util.JsonUtil;
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
    AssessmentActionRepository assessmentActionRepository;

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
    public SignalAction createAssessmentAction(SignalAction signalAction, MultipartFile[] attachments) throws IOException, ApplicationException {
        
        Date d = new Date();
        signalAction.setCreatedDate(d);
        signalAction.setLastModifiedDate(d);
        signalAction.setActionStatus("New");
        
        Long actionsExist=assessmentActionRepository.countByActionNameIgnoreCaseAndAssessmentId(signalAction.getActionName(), signalAction.getAssessmentId());
    	if (actionsExist > 0) {
			throw exceptionBuilder.buildException(ErrorType.ASSESSMENTACCTION_NAME_DUPLICATE, null);
		}
    	checkAssessmentTaskStatus(signalAction);
    	
        SignalAction signalActionUpdated = assessmentActionRepository.save(signalAction);
        List<Attachment> attachmentList = attachmentService.addAttachments(signalActionUpdated.getId(), attachments, AttachmentType.ASSESSMENT_ACTION_ATTACHMENT, null, signalActionUpdated.getFileMetadata(), signalActionUpdated.getCreatedBy());
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
        signalAuditService.saveOrUpdateSignalActionAudit(signalActionUpdated, null, attachmentList, SmtConstant.CREATE.getDescription());
        return signalActionUpdated;
    }
    /**
     * 
     * @param signalAction
     * @param attachments
     * @throws ApplicationException
     */
    public void updateAssessmentAction(SignalAction signalAction, MultipartFile[] attachments) throws ApplicationException {
        if(signalAction.getId() == null) {
            throw new ApplicationException("Failed to update Action. Invalid Id received");
        }
       
        String assessmentActionOriginal = JsonUtil.converToJson(assessmentActionRepository.findOne(signalAction.getId()));
        signalAction.setLastModifiedDate(new Date());
        assessmentActionRepository.save(signalAction);
        List<Attachment> attachmentList = attachmentService.addAttachments(signalAction.getId(), attachments, AttachmentType.ASSESSMENT_ACTION_ATTACHMENT, signalAction.getDeletedAttachmentIds(), signalAction.getFileMetadata(), signalAction.getCreatedBy());
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
        	String assessmentPlanOriginal = JsonUtil.converToJson(assessmentPlanRepository.findOne(Long.valueOf(signalAction.getAssessmentId())));
        	assessmentPlanRepository.updateAssessmentTaskStatus("Completed", Long.valueOf(signalAction.getAssessmentId()));
        	AssessmentPlan assessmentPlan  = assessmentPlanRepository.findOne(Long.valueOf(signalAction.getAssessmentId()));
        	assessmentPlan.setLastModifiedDate(new Date());
        	assessmentPlan.setModifiedBy(signalAction.getModifiedBy());
        	signalAuditService.saveOrUpdateAssessmentPlanAudit(assessmentPlan, assessmentPlanOriginal, null, SmtConstant.UPDATE.getDescription());
        }
        signalAuditService.saveOrUpdateSignalActionAudit(signalAction, assessmentActionOriginal, attachmentList, SmtConstant.UPDATE.getDescription());
    }
    
    private void checkAssessmentTaskStatus(SignalAction signalAction){
    	 List<SignalAction> actions = findAllByAssessmentId(signalAction.getAssessmentId(), null);
         boolean allTasksCompletedFlag = true;
         if(!CollectionUtils.isEmpty(actions)){
         	for(SignalAction action:actions){
         		if(!"Completed".equals(action.getActionStatus())){
         			allTasksCompletedFlag = false;
         		}
         	}
         }
         if(allTasksCompletedFlag){
         	assessmentPlanRepository.updateAssessmentTaskStatus(SmtConstant.COMPLETED.getDescription(), Long.valueOf(signalAction.getAssessmentId()));
         }else{
        	 assessmentPlanRepository.updateAssessmentTaskStatus("Not Completed", Long.valueOf(signalAction.getAssessmentId()));
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
        
        signalAuditService.saveOrUpdateSignalActionAudit(signalAction, null, null, SmtConstant.DELETE.getDescription());
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
