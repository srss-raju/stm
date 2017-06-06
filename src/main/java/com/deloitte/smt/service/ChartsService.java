package com.deloitte.smt.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deloitte.smt.repository.TopicRepository;

@Service
public class ChartsService {
	
	private static final Logger LOG = Logger.getLogger(ChartsService.class);
	
	@Autowired
	private TopicRepository topicRepository;
	
	public String getSmtComplianceDetails(){
		LOG.info("Method Start getSmtComplianceDetails");
		
		return null;
	}

}
