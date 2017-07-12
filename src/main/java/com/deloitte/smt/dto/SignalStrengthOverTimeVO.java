package com.deloitte.smt.dto;

import com.deloitte.smt.entity.SignalStatistics;
import com.deloitte.smt.entity.Topic;

public class SignalStrengthOverTimeVO {
	
	private Topic topic;
	private SignalStatistics signalStatistics;
	
	public Topic getTopic() {
		return topic;
	}
	public void setTopic(Topic topic) {
		this.topic = topic;
	}
	public SignalStatistics getSignalStatistics() {
		return signalStatistics;
	}
	public void setSignalStatistics(SignalStatistics signalStatistics) {
		this.signalStatistics = signalStatistics;
	}
	
	
}
