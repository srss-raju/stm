package com.deloitte.smt.constant;

/**
 * 
 * @author jshaik
 *
 */
public enum FilterTypes {
	STATUS("Status"),
	OWNER("Owner"),
	ASSIGNEDTO("Assigned To"),
	DATERANGE("Date Range"),
	SIGNALCONFIRMATION("Signal Confirmation"),
	SIGNALSOURCE("Signal Source"),
	ASSESSMENTTASKSTATUS("Assessment Task Status"),
	FINALDISPOSITION("Final Disposition"),
	RISKPLANACTIONSTATUS("Risk Plan Action Status"),
	FREQUENCY("Frequency"),
	EMPTY("");
	private String type;
	
	FilterTypes(String type) {
        this.type = type;
    }
    public String type() {
        return type;
    }

   
}
