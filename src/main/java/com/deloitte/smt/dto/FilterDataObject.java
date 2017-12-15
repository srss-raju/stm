package com.deloitte.smt.dto;

import java.util.Date;

import lombok.Data;

@Data
public class FilterDataObject {
	private long topicId;
	private String signalName;
	private String signalStatus;
	private String description;
	private String signalConfirmation;
	private String sourceName;
	private Date createdDate;
}
