package com.deloitte.smt.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class FilterDataObject implements Serializable{
	private static final long serialVersionUID = 5249048523110491930L;
	private Object id;
	private String name;
	private String status;
	private String description;
	private String signalConfirmation;
	private String sourceName;
	private Object createdDate;
	private String priority;
	private Object dueDate;
	private String runFrequency;
	private Object denominatorForPoisson;
	private Object lastRunDate;
	private Object signalDetected;
	private Object nextRunDate;
	private String assessmentName;
	private Object assessmentDueDate;
	private String assessmentPlanStatus;
	
}
