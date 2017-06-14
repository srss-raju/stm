package com.deloitte.smt.constant;

public enum SignalConfirmationStatus {
	CONTINUE_TO_MONITOR("Continue To Monitor"),VALIDATED_SIGNAL("Validated Signal"),NON_SIGNAL("Non-Signal"),NOT_YET_DETERMINED("Not Yet Determined");
	
	private String confirmationStatus;
	
	SignalConfirmationStatus(String confirmationStatus){
		this.confirmationStatus=confirmationStatus;
	}
	
	public String getName() {
		return confirmationStatus;
	}
	

}
