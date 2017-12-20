package com.deloitte.smt.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class FilterDataObject {
	private long id;
	private String name;
	private String status;
	private String description;
	private String signalConfirmation;
	private String sourceName;
	private Date createdDate;
	private String priority;
	private Date dueDate;
	private String runFrequency;
	private List<?> denominatorForPoisson;
	private Date lastRunDate;
	private Long signalDetected;
	private Date nextRunDate;
}
