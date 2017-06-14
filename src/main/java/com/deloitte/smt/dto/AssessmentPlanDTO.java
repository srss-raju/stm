package com.deloitte.smt.dto;

public class AssessmentPlanDTO {
	private Long planId;
	private String ingredientName;
	private String assessmentName;
	private String assessmentPlanStatus;
	
	public AssessmentPlanDTO(Long planId,String ingredientName, String assessmentName, String assessmentPlanStatus) {
		super();
		this.planId=planId;
		this.ingredientName = ingredientName;
		this.assessmentName = assessmentName;
		this.assessmentPlanStatus = assessmentPlanStatus;
	}
	
	public AssessmentPlanDTO(){
		//Do Nothing
	}
	
	public Long getPlanId() {
		return planId;
	}

	public void setPlanId(Long planId) {
		this.planId = planId;
	}
	
	public String getIngredientName() {
		return ingredientName;
	}
	public void setIngredientName(String ingredientName) {
		this.ingredientName = ingredientName;
	}
	public String getAssessmentName() {
		return assessmentName;
	}
	public void setAssessmentName(String assessmentName) {
		this.assessmentName = assessmentName;
	}
	public String getAssessmentPlanStatus() {
		return assessmentPlanStatus;
	}
	public void setAssessmentPlanStatus(String assessmentPlanStatus) {
		this.assessmentPlanStatus = assessmentPlanStatus;
	}
	
}	