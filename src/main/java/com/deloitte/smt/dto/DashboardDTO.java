package com.deloitte.smt.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;


public class DashboardDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6678141117765723101L;

	@JsonProperty("topic")
	private List<Map<String, Map<String, Long>>> topicMetrics=new ArrayList<Map<String, Map<String, Long>>>();
	
	@JsonProperty("assessment")
	private List<Map<String, Map<String, Long>>> assessmentMetrics=new ArrayList<Map<String, Map<String, Long>>>();
	
	@JsonProperty("risk")
	private List<Map<String, Map<String, Long>>> riskMetrics=new ArrayList<Map<String, Map<String, Long>>>();
	
	
	public  List<Map<String, Map<String, Long>>> getTopicMetrics() {
		return topicMetrics;
	}
	public void setTopicMetrics( List<Map<String, Map<String, Long>>> topicMetrics) {
		this.topicMetrics = topicMetrics;
	}
	public List<Map<String, Map<String, Long>>> getAssessmentMetrics() {
		return assessmentMetrics;
	}
	public void setAssessmentMetrics(List<Map<String, Map<String, Long>>> assessmentMetrics) {
		this.assessmentMetrics = assessmentMetrics;
	}
	public  List<Map<String, Map<String, Long>>> getRiskMetrics() {
		return riskMetrics;
	}
	public void setRiskMetrics( List<Map<String, Map<String, Long>>> riskMetrics) {
		this.riskMetrics = riskMetrics;
	}


}
