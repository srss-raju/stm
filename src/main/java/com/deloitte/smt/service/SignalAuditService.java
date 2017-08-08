package com.deloitte.smt.service;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.entity.SignalAudit;
import com.deloitte.smt.repository.SignalAttachmentAuditRepository;
import com.deloitte.smt.repository.SignalAuditRepository;

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

	
}
