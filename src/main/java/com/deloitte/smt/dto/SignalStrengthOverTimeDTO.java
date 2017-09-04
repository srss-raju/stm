package com.deloitte.smt.dto;

public class SignalStrengthOverTimeDTO {
	

	
	private String signalStatus;
	public String getSignalStatus() {
		return signalStatus;
	}
	public void setSignalStatus(String signalStatus) {
		this.signalStatus = signalStatus;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public long getTopicId() {
		return topicId;
	}
	public void setTopicId(long topicId) {
		this.topicId = topicId;
	}
	
	public String getAlgorithm() {
		return algorithm;
	}
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	
	
	public Double getLb() {
		return lb;
	}
	public void setLb(Double lb) {
		this.lb = lb;
	}
	public Double getUb() {
		return ub;
	}
	public void setUb(Double ub) {
		this.ub = ub;
	}

	
	private String algorithm;
	private long timestamp;
	private long topicId;
	private Double lb=0.0;
	private Double ub=0.0;
	
	

	
}
