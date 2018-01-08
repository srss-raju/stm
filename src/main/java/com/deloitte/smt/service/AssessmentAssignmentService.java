package com.deloitte.smt.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.entity.AssessmentAssignmentAssignees;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.entity.TopicAssessmentAssignmentAssignees;
import com.deloitte.smt.repository.AssessmentAssignmentAssigneesRepository;
import com.deloitte.smt.repository.AssessmentAssigneesRepository;

/**
 * Created by RKB on 31-07-2017.
 */
@Transactional
@Service
public class AssessmentAssignmentService {

    @Autowired
    AssessmentAssignmentAssigneesRepository assessmentAssignmentAssigneesRepository;
    
    @Autowired
    AssessmentAssigneesRepository topicAssessmentAssignmentAssigneesRepository;
    

	public AssessmentPlan saveAssignmentAssignees(AssignmentConfiguration assignmentConfiguration, AssessmentPlan assessmentPlan) {
        assignmentConfiguration.setAssessmentAssignees(assessmentAssignmentAssigneesRepository.findByAssignmentConfigurationId(assignmentConfiguration.getId()));
		return setAssessmentAssignmentConfigurationAssignees(assignmentConfiguration, assessmentPlan);
	}
    
	/**
	 * @param assignmentConfiguration
	 * @param assignmentConfigurationUpdated
	 */
	private AssessmentPlan setAssessmentAssignmentConfigurationAssignees(AssignmentConfiguration assignmentConfiguration, AssessmentPlan assessmentPlan) {
        
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getAssessmentAssignees())){
			List<TopicAssessmentAssignmentAssignees> list = new ArrayList<>();
			for(AssessmentAssignmentAssignees aaAssignees : assignmentConfiguration.getAssessmentAssignees()){
				saveAssessmentAssignmentAssignees(aaAssignees,assessmentPlan, list);
        	}
			assessmentPlan.setTopicAssessmentAssignmentAssignees(topicAssessmentAssignmentAssigneesRepository.save(list));
		}
		
		return assessmentPlan;
	}

	private void saveAssessmentAssignmentAssignees(AssessmentAssignmentAssignees aaAssignees, AssessmentPlan assessmentPlan, List<TopicAssessmentAssignmentAssignees> list) {
		TopicAssessmentAssignmentAssignees assignee = new TopicAssessmentAssignmentAssignees();
		assignee.setAssessmentId(assessmentPlan.getId());
		assignee.setAssignTo(aaAssignees.getAssignTo());
		assignee.setUserGroupKey(aaAssignees.getUserGroupKey());
		assignee.setUserKey(aaAssignees.getUserKey());
		assignee.setCreatedDate(assessmentPlan.getCreatedDate());
		assignee.setCreatedBy(assessmentPlan.getCreatedBy());
		assignee.setAssessmentPlan(assessmentPlan);
		list.add(assignee);
	}
	
	public void findAssignmentAssignees(AssessmentPlan assessmentPlan) {
		assessmentPlan.setTopicAssessmentAssignmentAssignees(topicAssessmentAssignmentAssigneesRepository.findByAssessmentId(assessmentPlan.getId()));
	}

}
