package com.deloitte.smt.dto;

public class RiskPlanDTO {
	private Long riskPlanId;
	private String name;
	private String riskPlanStatus;
	
	
	public RiskPlanDTO(Long riskPlanId,String name, String riskPlanStatus) {
		super();
		this.riskPlanId=riskPlanId;
		this.name = name;
		this.riskPlanStatus = riskPlanStatus;
	}
	
	public RiskPlanDTO(){
		//Do Nothing
	}
	
	public Long getRiskPlanId() {
		return riskPlanId;
	}

	public void setRiskPlanId(Long riskPlanId) {
		this.riskPlanId = riskPlanId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRiskPlanStatus() {
		return riskPlanStatus;
	}
	public void setRiskPlanStatus(String riskPlanStatus) {
		this.riskPlanStatus = riskPlanStatus;
	}
	
	

}
