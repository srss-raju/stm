package com.deloitte.smt.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.entity.SignalValidationAssignmentAssignees;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.entity.TopicSignalValidationAssignmentAssignees;
import com.deloitte.smt.repository.AssessmentAssignmentAssigneesRepository;
import com.deloitte.smt.repository.RiskPlanAssignmentAssigneesRepository;
import com.deloitte.smt.repository.SignalValidationAssignmentAssigneesRepository;
import com.deloitte.smt.repository.TopicAssessmentAssignmentAssigneesRepository;
import com.deloitte.smt.repository.TopicRiskPlanAssignmentAssigneesRepository;
import com.deloitte.smt.repository.TopicSignalValidationAssignmentAssigneesRepository;

/**
 * Created by RKB on 31-07-2017.
 */
@Transactional
@Service
public class SignalAssignmentService {

	@Autowired
    SignalValidationAssignmentAssigneesRepository signalValidationAssignmentAssigneesRepository;
    
    @Autowired
    AssessmentAssignmentAssigneesRepository assessmentAssignmentAssigneesRepository;
    
    @Autowired
    RiskPlanAssignmentAssigneesRepository riskPlanAssignmentAssigneesRepository;
    
    @Autowired
    TopicSignalValidationAssignmentAssigneesRepository topicSignalValidationAssignmentAssigneesRepository;
    
    @Autowired
    TopicAssessmentAssignmentAssigneesRepository topicAssessmentAssignmentAssigneesRepository;
    
    @Autowired
    TopicRiskPlanAssignmentAssigneesRepository topicRiskPlanAssignmentAssigneesRepository;

	public Topic saveSignalAssignmentAssignees(AssignmentConfiguration assignmentConfiguration, Topic topicUpdated) {
		assignmentConfiguration.setSignalValidationAssignmentAssignees(signalValidationAssignmentAssigneesRepository.findByAssignmentConfigurationId(assignmentConfiguration.getId()));
		return setTopicAssignmentConfigurationAssignees(assignmentConfiguration, topicUpdated);
	}
    
	/**
	 * @param assignmentConfiguration
	 * @param assignmentConfigurationUpdated
	 */
	private Topic setTopicAssignmentConfigurationAssignees(AssignmentConfiguration assignmentConfiguration, Topic topic) {
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getSignalValidationAssignmentAssignees())){
        	List<TopicSignalValidationAssignmentAssignees> list = new ArrayList<>();
			for(SignalValidationAssignmentAssignees svaAssignees : assignmentConfiguration.getSignalValidationAssignmentAssignees()){
        		saveTopicSignalValidationAssignmentAssignees(svaAssignees,topic,list);
        	}
			topic.setTopicSignalValidationAssignmentAssignees(topicSignalValidationAssignmentAssigneesRepository.save(list));
        }
		return topic;
	}
	
	private void saveTopicSignalValidationAssignmentAssignees(SignalValidationAssignmentAssignees svaAssignees, Topic topic, List<TopicSignalValidationAssignmentAssignees> list) {
		TopicSignalValidationAssignmentAssignees assignee = new TopicSignalValidationAssignmentAssignees();
		assignee.setAssignTo(svaAssignees.getAssignTo());
		assignee.setUserGroupKeys(svaAssignees.getUserGroupKey());
		assignee.setUserKeys(svaAssignees.getUserKey());
		assignee.setCreatedDate(topic.getCreatedDate());
		assignee.setCreatedBy(topic.getCreatedBy());
		list.add(assignee);
	}

	public void findSignalAssignmentAssignees(Topic topic) {
		topic.setTopicSignalValidationAssignmentAssignees(topicSignalValidationAssignmentAssigneesRepository.findByTopicId(topic.getId()));
	}

}
