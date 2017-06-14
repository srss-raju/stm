package com.deloitte.smt.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by myelleswarapu on 06-04-2017.
 */
public enum SignalStatus {
	NEW("New"),COMPLETED("Completed"), IN_PREGRESS("In Progress");

	private String status;

	public String getStatus() {
		return status;
	}

	SignalStatus(String status) {
		this.status = status;
	}

	public boolean isStatusAvailable(String status) {
		for (SignalStatus assessmentPlanStatus : SignalStatus.values()) {
			if(assessmentPlanStatus.getStatus().equalsIgnoreCase(status)){
				return true;
			}
		}
		
		return false;
	}
	
	public static List<String> getStatusValues(){
		List<String> values=new ArrayList<>();
		for (SignalStatus assessmentPlanStatus : SignalStatus.values()) {
			values.add(assessmentPlanStatus.getStatus());
		}
		
		return values;
	}
}
