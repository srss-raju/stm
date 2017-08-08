package com.deloitte.smt.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.RiskPlanAssignmentAssignees;
import com.deloitte.smt.entity.TopicRiskPlanAssignmentAssignees;
import com.deloitte.smt.repository.RiskPlanAssignmentAssigneesRepository;
import com.deloitte.smt.repository.TopicRiskPlanAssignmentAssigneesRepository;

/**
 * Created by RKB on 31-07-2017.
 */
@Transactional
@Service
public class RiskPlanAssignmentService {

    @Autowired
    RiskPlanAssignmentAssigneesRepository riskPlanAssignmentAssigneesRepository;
    
    @Autowired
    TopicRiskPlanAssignmentAssigneesRepository topicRiskPlanAssignmentAssigneesRepository;

	public RiskPlan saveAssignmentAssignees(AssignmentConfiguration assignmentConfiguration, RiskPlan riskPlan) {
        assignmentConfiguration.setRiskPlanAssignmentAssignees(riskPlanAssignmentAssigneesRepository.findByAssignmentConfigurationId(assignmentConfiguration.getId()));
		return setAssessmentAssignmentConfigurationAssignees(assignmentConfiguration, riskPlan);
	}
    
	/**
	 * @param assignmentConfiguration
	 * @param assignmentConfigurationUpdated
	 */
	private RiskPlan setAssessmentAssignmentConfigurationAssignees(AssignmentConfiguration assignmentConfiguration, RiskPlan riskPlan) {
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getRiskPlanAssignmentAssignees())){
			List<TopicRiskPlanAssignmentAssignees> list = new ArrayList<>();
			for(RiskPlanAssignmentAssignees rpaAssignees : assignmentConfiguration.getRiskPlanAssignmentAssignees()){
				saveTopicRiskPlanAssignmentAssignees(rpaAssignees,riskPlan,list);
        	}
			riskPlan.setTopicRiskPlanAssignmentAssignees(topicRiskPlanAssignmentAssigneesRepository.save(list));
		}
		return riskPlan;
	}

	private void saveTopicRiskPlanAssignmentAssignees(RiskPlanAssignmentAssignees rpaAssignees, RiskPlan riskPlan, List<TopicRiskPlanAssignmentAssignees> list) {
		TopicRiskPlanAssignmentAssignees assignee = new TopicRiskPlanAssignmentAssignees();
		assignee.setRiskId(riskPlan.getId());
		assignee.setAssignTo(rpaAssignees.getAssignTo());
		assignee.setUserGroupKey(rpaAssignees.getUserGroupKey());
		assignee.setUserKey(rpaAssignees.getUserKey());
		assignee.setCreatedDate(riskPlan.getCreatedDate());
		assignee.setCreatedBy(riskPlan.getCreatedBy());
		list.add(assignee);
	}

	public void findRiskAssignmentAssignees(RiskPlan riskPlan) {
		riskPlan.setTopicRiskPlanAssignmentAssignees(topicRiskPlanAssignmentAssigneesRepository.findByRiskId(riskPlan.getId()));
	}

}
