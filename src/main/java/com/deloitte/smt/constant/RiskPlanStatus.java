package com.deloitte.smt.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cavula
 */
public enum RiskPlanStatus {
	NEW("New"), IN_PREGRESS("In Progress"),COMPLETED("Completed");

	private String status;

	public String getStatus() {
		return status;
	}

	RiskPlanStatus(String status) {
		this.status = status;
	}

	public boolean isStatusAvailable(String status) {
		for (RiskPlanStatus assessmentPlanStatus : RiskPlanStatus.values()) {
			if(assessmentPlanStatus.getStatus().equalsIgnoreCase(status)){
				return true;
			}
		}
		
		return false;
	}
	
	public static List<String> getStatusValues(){
		List<String> values=new ArrayList<String>();
		for (RiskPlanStatus assessmentPlanStatus : RiskPlanStatus.values()) {
			values.add(assessmentPlanStatus.getStatus());
		}
		
		return values;
	}
}
