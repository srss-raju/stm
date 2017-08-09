package com.deloitte.smt.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.entity.Attachment;
import com.deloitte.smt.entity.SignalAttachmentAudit;
import com.deloitte.smt.entity.SignalAudit;
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
	
	public void saveSignalAudit(Topic topicUpdated, List<Attachment> attchmentList) {
		SignalAudit signalAudit = new SignalAudit();
		signalAudit.setCreatedBy(topicUpdated.getCreatedBy());
		signalAudit.setCreatedDate(topicUpdated.getCreatedDate());
		signalAudit.setEntityType("Signal");
		signalAudit.setOperation("Create");
		signalAudit.setOriginalValue(JsonUtil.converToJson(topicUpdated));
		SignalAudit audit = signalAuditRepository.save(signalAudit);
		saveSignalAttachmentAudit(attchmentList, audit);
	}
	
	public void updateSignalAudit(Topic topicUpdated, String topicOriginal, List<Attachment> attchmentList) {
		SignalAudit signalAudit = new SignalAudit();
		signalAudit.setModifieddBy(topicUpdated.getModifiedBy());
		signalAudit.setModifiedDate(topicUpdated.getLastModifiedDate());
		signalAudit.setEntityType("Signal");
		signalAudit.setOperation("Update");
		signalAudit.setOriginalValue(topicOriginal);
		signalAudit.setModifiedValue(JsonUtil.converToJson(topicUpdated));
		SignalAudit audit = signalAuditRepository.save(signalAudit);
		saveSignalAttachmentAudit(attchmentList, audit);
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

	
}
