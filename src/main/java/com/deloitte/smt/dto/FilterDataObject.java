package com.deloitte.smt.dto;

import java.util.Date;

import lombok.Data;

@Data
public class FilterDataObject {
	private long signalId;
	private String name;
	private String signalStatus;
	private String description;
	private String signalConfirmation;
	private String sourceName;
	private Date createdDate;
}
