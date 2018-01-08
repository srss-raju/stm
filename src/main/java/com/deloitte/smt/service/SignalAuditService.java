package com.deloitte.smt.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.Attachment;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.SignalAttachmentAudit;
import com.deloitte.smt.entity.SignalAudit;
import com.deloitte.smt.entity.Task;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.repository.SignalAttachmentAuditRepository;
import com.deloitte.smt.repository.SignalAuditRepository;
import com.deloitte.smt.util.JsonUtil;

/**
 * Created by RKB on 08-08-2017.
 */
@Transactional
@Service
public class SignalAuditService {

	private static final Logger LOG = Logger.getLogger(SignalService.class);
	
	@Autowired
	SignalAuditRepository signalAuditRepository;
	
	@Autowired
	SignalAttachmentAuditRepository signalAttachmentAuditRepository;

	public List<SignalAudit> getAuditDetails() {
		LOG.info("In getAuditDetails");
		List<SignalAudit> allSignals = signalAuditRepository.findAll();
		if(!CollectionUtils.isEmpty(allSignals)){
			for(SignalAudit signal : allSignals){
				signal.setSignalAttachmentAudit(signalAttachmentAuditRepository.findAllByAttachmentResourceId(signal.getId()));
			}
		}
		return allSignals;
	}
	
	public void saveSignalAttachmentAudit(List<Attachment> attchmentList, SignalAudit audit) {
		if(!CollectionUtils.isEmpty(attchmentList)){
			List<SignalAttachmentAudit> list = new ArrayList<>();
			for(Attachment attachment:attchmentList){
				SignalAttachmentAudit signalAttachmentAudit = new SignalAttachmentAudit();
				 signalAttachmentAudit.setAttachmentResourceId(audit.getId());
				 signalAttachmentAudit.setAttachmentsURL(attachment.getAttachmentsURL());
				 signalAttachmentAudit.setAttachmentType(attachment.getAttachmentType());
				 signalAttachmentAudit.setContent(attachment.getContent());
				 signalAttachmentAudit.setContentType(attachment.getContentType());
				 signalAttachmentAudit.setCreatedBy(audit.getCreatedBy());
				 signalAttachmentAudit.setLastModifiedDate(audit.getModifiedDate());
				 signalAttachmentAudit.setModifiedBy(attachment.getModifiedBy());
				 signalAttachmentAudit.setCreatedDate(audit.getCreatedDate());
				 signalAttachmentAudit.setDescription(attachment.getDescription());
				 signalAttachmentAudit.setFileName(attachment.getFileName());
				 list.add(signalAttachmentAudit);
			}
			signalAttachmentAuditRepository.save(list);
		}
	}
	
	public void saveOrUpdateSignalAudit(Topic topicUpdated, String topicOriginal, List<Attachment> attchmentList, String operation) {
		SignalAudit signalAudit = new SignalAudit();
		signalAudit.setCreatedBy(topicUpdated.getModifiedBy());
		signalAudit.setCreatedDate(new Date());
		signalAudit.setEntityType("Signal");
		signalAudit.setOperation(operation);
		if(topicOriginal != null){
			signalAudit.setOriginalValue(topicOriginal);
			signalAudit.setModifiedValue(JsonUtil.converToJson(topicUpdated));
		}else{
			signalAudit.setOriginalValue(JsonUtil.converToJson(topicUpdated));
		}
		SignalAudit audit = signalAuditRepository.save(signalAudit);
		saveSignalAttachmentAudit(attchmentList, audit);
	}
	
	public void saveOrUpdateAssessmentPlanAudit(AssessmentPlan assessmentPlan, String assessmentPlanOriginal, List<Attachment> attachmentList, String operation) {
		SignalAudit signalAudit = new SignalAudit();
		signalAudit.setCreatedBy(assessmentPlan.getModifiedBy());
		signalAudit.setCreatedDate(new Date());
		signalAudit.setEntityType("AssessmentPlan");
		signalAudit.setOperation(operation);
		if(assessmentPlanOriginal != null){
			signalAudit.setOriginalValue(assessmentPlanOriginal);
			signalAudit.setModifiedValue(JsonUtil.converToJson(assessmentPlan));
		}else{
			signalAudit.setOriginalValue(JsonUtil.converToJson(assessmentPlan));
		}
		SignalAudit audit = signalAuditRepository.save(signalAudit);
		saveSignalAttachmentAudit(attachmentList, audit);
	}
	
	public void saveOrUpdateSignalActionAudit(Task signalActionUpdated, String topicOriginal, List<Attachment> attchmentList, String operation) {
		SignalAudit signalAudit = new SignalAudit();
		signalAudit.setCreatedBy(signalActionUpdated.getCreatedBy());
		signalAudit.setCreatedDate(new Date());
		signalAudit.setEntityType("SignalAction");
		signalAudit.setOperation(operation);
		if(topicOriginal != null){
			signalAudit.setOriginalValue(topicOriginal);
			signalAudit.setModifiedValue(JsonUtil.converToJson(signalActionUpdated));
		}else{
			signalAudit.setOriginalValue(JsonUtil.converToJson(signalActionUpdated));
		}
		SignalAudit audit = signalAuditRepository.save(signalAudit);
		saveSignalAttachmentAudit(attchmentList, audit);
	}
	
	public void saveOrUpdateRiskPlanAudit(RiskPlan riskPlanUpdated, String riskPlanOriginal, List<Attachment> attchmentList, String operation) {
		SignalAudit signalAudit = new SignalAudit();
		signalAudit.setCreatedBy(riskPlanUpdated.getCreatedBy());
		signalAudit.setCreatedDate(new Date());
		signalAudit.setEntityType("RiskPlan");
		signalAudit.setOperation(operation);
		if(riskPlanOriginal != null){
			signalAudit.setOriginalValue(riskPlanOriginal);
			signalAudit.setModifiedValue(JsonUtil.converToJson(riskPlanUpdated));
		}else{
			signalAudit.setOriginalValue(JsonUtil.converToJson(riskPlanUpdated));
		}
		SignalAudit audit = signalAuditRepository.save(signalAudit);
		saveSignalAttachmentAudit(attchmentList, audit);
	}
	
	public void saveOrUpdateRiskTaskAudit(Task riskTaskUpdated, String riskTaskOriginal, List<Attachment> attchmentList, String operation) {
		SignalAudit signalAudit = new SignalAudit();
		signalAudit.setCreatedBy(riskTaskUpdated.getCreatedBy());
		signalAudit.setCreatedDate(new Date());
		signalAudit.setEntityType("RiskTask");
		signalAudit.setOperation(operation);
		if(riskTaskOriginal != null){
			signalAudit.setOriginalValue(riskTaskOriginal);
			signalAudit.setModifiedValue(JsonUtil.converToJson(riskTaskUpdated));
		}else{
			signalAudit.setOriginalValue(JsonUtil.converToJson(riskTaskUpdated));
		}
		SignalAudit audit = signalAuditRepository.save(signalAudit);
		saveSignalAttachmentAudit(attchmentList, audit);
	}
	
}
