package com.deloitte.smt.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by myelleswarapu on 06-04-2017.
 */
public enum AssessmentPlanStatus {
	COMPLETED("Completed"), IN_PREGRESS("In Progress");

	private String status;

	public String getStatus() {
		return status;
	}

	AssessmentPlanStatus(String status) {
		this.status = status;
	}

	public boolean isStatusAvailable(String status) {
		for (AssessmentPlanStatus assessmentPlanStatus : AssessmentPlanStatus.values()) {
			if(assessmentPlanStatus.getStatus().equalsIgnoreCase(status)){
				return true;
			}
		}
		
		return false;
	}
	
	public static List<String> getStatusValues(){
		List<String> values=new ArrayList<>();
		for (AssessmentPlanStatus assessmentPlanStatus : AssessmentPlanStatus.values()) {
			values.add(assessmentPlanStatus.getStatus());
		}
		
		return values;
	}
}
