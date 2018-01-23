package com.deloitte.smt.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date createdDate;
	private String priority;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date dueDate;
	private String runFrequency;
	private Object denominatorForPoisson;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date lastRunDate;
	private Object signalDetected;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date nextRunDate;
	private String assessmentName;
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date assessmentDueDate;
	private String assessmentPlanStatus;
	
}
