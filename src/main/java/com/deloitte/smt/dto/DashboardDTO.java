package com.deloitte.smt.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.Topic;

public class DashboardDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6678141117765723101L;
	
	List<TopicDTO> topics;
	List<AssessmentPlanDTO> assessmentPlans;
	List<RiskPlanDTO> riskPlans;

	private Map<String, Map<String, Long>> topicMetrics;
	private Map<String, Map<String, Long>> assessmentMetrics;
	private Map<String, Map<String, Long>> riskMetrics;
	
	
	public List<RiskPlanDTO> getRiskPlans() {
		return riskPlans;
	}
	public void setRiskPlans(List<RiskPlanDTO> riskPlans) {
		this.riskPlans = riskPlans;
	}
	public List<TopicDTO> getTopics() {
		return topics;
	}
	public void setTopics(List<TopicDTO> topics) {
		this.topics = topics;
	}
	public List<AssessmentPlanDTO> getAssessmentPlans() {
		return assessmentPlans;
	}
	public void setAssessmentPlans(List<AssessmentPlanDTO> assessmentPlans) {
		this.assessmentPlans = assessmentPlans;
	}
	
	
	public Map<String, Map<String, Long>> getTopicMetrics() {
		return topicMetrics;
	}
	public void setTopicMetrics(Map<String, Map<String, Long>> topicMetrics) {
		this.topicMetrics = topicMetrics;
	}
	public Map<String, Map<String, Long>> getAssessmentMetrics() {
		return assessmentMetrics;
	}
	public void setAssessmentMetrics(Map<String, Map<String, Long>> assessmentMetrics) {
		this.assessmentMetrics = assessmentMetrics;
	}
	public Map<String, Map<String, Long>> getRiskMetrics() {
		return riskMetrics;
	}
	public void setRiskMetrics(Map<String, Map<String, Long>> riskMetrics) {
		this.riskMetrics = riskMetrics;
	}


}