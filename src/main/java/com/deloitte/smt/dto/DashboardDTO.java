package com.deloitte.smt.dto;

import java.io.Serializable;
import java.util.List;

import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.Topic;

public class DashboardDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6678141117765723101L;
	
	List<Topic> topics;
	List<AssessmentPlan> assessmentPlans;
	List<RiskPlan> riskPlans;
	
	public List<RiskPlan> getRiskPlans() {
		return riskPlans;
	}
	public void setRiskPlans(List<RiskPlan> riskPlans) {
		this.riskPlans = riskPlans;
	}
	public List<Topic> getTopics() {
		return topics;
	}
	public void setTopics(List<Topic> topics) {
		this.topics = topics;
	}
	public List<AssessmentPlan> getAssessmentPlans() {
		return assessmentPlans;
	}
	public void setAssessmentPlans(List<AssessmentPlan> assessmentPlans) {
		this.assessmentPlans = assessmentPlans;
	}
	
	

}
