package com.deloitte.smt.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.AssignmentRiskPlanAssignees;
import com.deloitte.smt.entity.RiskPlanAssignees;
import com.deloitte.smt.repository.AssignmentRiskPlanAssigneesRepository;
import com.deloitte.smt.repository.RiskPlanAssigneesRepository;

/**
 * Created by RKB on 31-07-2017.
 */
@Transactional
@Service
public class RiskPlanAssignmentService {

    @Autowired
    AssignmentRiskPlanAssigneesRepository riskPlanAssignmentAssigneesRepository;
    
    @Autowired
    RiskPlanAssigneesRepository topicRiskPlanAssignmentAssigneesRepository;

	public RiskPlan saveAssignmentAssignees(AssignmentConfiguration assignmentConfiguration, RiskPlan riskPlan) {
        assignmentConfiguration.setRiskAssignees(riskPlanAssignmentAssigneesRepository.findByAssignmentConfigurationId(assignmentConfiguration.getId()));
		return setAssessmentAssignmentConfigurationAssignees(assignmentConfiguration, riskPlan);
	}
    
	/**
	 * @param assignmentConfiguration
	 * @param assignmentConfigurationUpdated
	 */
	private RiskPlan setAssessmentAssignmentConfigurationAssignees(AssignmentConfiguration assignmentConfiguration, RiskPlan riskPlan) {
		if(!CollectionUtils.isEmpty(assignmentConfiguration.getRiskAssignees())){
			List<RiskPlanAssignees> list = new ArrayList<>();
			for(AssignmentRiskPlanAssignees rpaAssignees : assignmentConfiguration.getRiskAssignees()){
				saveTopicRiskPlanAssignmentAssignees(rpaAssignees,riskPlan,list);
        	}
		}
		return riskPlan;
	}

	private void saveTopicRiskPlanAssignmentAssignees(AssignmentRiskPlanAssignees rpaAssignees, RiskPlan riskPlan, List<RiskPlanAssignees> list) {
		RiskPlanAssignees assignee = new RiskPlanAssignees();
		assignee.setUserGroupKey(rpaAssignees.getUserGroupKey());
		assignee.setUserKey(rpaAssignees.getUserKey());
		assignee.setCreatedDate(riskPlan.getCreatedDate());
		assignee.setCreatedBy(riskPlan.getCreatedBy());
		list.add(assignee);
	}

}
