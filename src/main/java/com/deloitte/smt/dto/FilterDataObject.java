package com.deloitte.smt.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class FilterDataObject {
	private Object id;
	private String name;
	private String status;
	private String description;
	private String signalConfirmation;
	private String sourceName;
	private Date createdDate;
	private String priority;
	private Date dueDate;
	private String runFrequency;
	private Object denominatorForPoisson;
	private Date lastRunDate;
	private Object signalDetected;
	private Date nextRunDate;
	private String assessmentName;
	private Date assessmentDueDate;
	private String assessmentPlanStatus;
	
}
