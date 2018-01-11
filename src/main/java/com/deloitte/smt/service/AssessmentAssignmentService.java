package com.deloitte.smt.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.entity.AssignmentAssessmentAssignees;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.entity.AssessmentAssignees;
import com.deloitte.smt.repository.AssignmentAssessmentAssigneesRepository;
import com.deloitte.smt.repository.AssessmentAssigneesRepository;

/**
 * Created by RKB on 31-07-2017.
 */
@Transactional
@Service
public class AssessmentAssignmentService {

    @Autowired
    AssignmentAssessmentAssigneesRepository assessmentAssignmentAssigneesRepository;
    
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
			List<AssessmentAssignees> list = new ArrayList<>();
			for(AssignmentAssessmentAssignees aaAssignees : assignmentConfiguration.getAssessmentAssignees()){
				saveAssessmentAssignmentAssignees(aaAssignees,assessmentPlan, list);
        	}
			topicAssessmentAssignmentAssigneesRepository.save(list);
		}
		
		return assessmentPlan;
	}

	private void saveAssessmentAssignmentAssignees(AssignmentAssessmentAssignees aaAssignees, AssessmentPlan assessmentPlan, List<AssessmentAssignees> list) {
		AssessmentAssignees assignee = new AssessmentAssignees();
		assignee.setUserGroupKey(aaAssignees.getUserGroupKey());
		assignee.setUserKey(aaAssignees.getUserKey());
		assignee.setCreatedDate(assessmentPlan.getCreatedDate());
		assignee.setCreatedBy(assessmentPlan.getCreatedBy());
		assignee.setAssessmentPlan(assessmentPlan);
		list.add(assignee);
	}
	

}
