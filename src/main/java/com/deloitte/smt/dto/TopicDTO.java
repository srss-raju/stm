package com.deloitte.smt.dto;

public class TopicDTO {
	
	private Long topicId;
	private String signalName;
	private String signalStatus;
	private String ingredientName;
	
	public TopicDTO(){
		//Do Nothing
	}
	
	public TopicDTO(Long topicId,String ingredientName, String signalName, String signalStatus) {
		super();
		this.topicId=topicId;
		this.ingredientName = ingredientName;
		this.signalName = signalName;
		this.signalStatus = signalStatus;
	}
	
	
	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public String getSignalName() {
		return signalName;
	}

	public void setSignalName(String signalName) {
		this.signalName = signalName;
	}

	public String getSignalStatus() {
		return signalStatus;
	}

	public void setSignalStatus(String signalStatus) {
		this.signalStatus = signalStatus;
	}

	
	public String getIngredientName() {
		return ingredientName;
	}

	public void setIngredientName(String ingredientName) {
		this.ingredientName = ingredientName;
	}
	
	

}
