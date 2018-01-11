package com.deloitte.smt.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Entity
@Table(name = "sm_task")
public class Task {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String riskId;
	
	private String name;
	
	private String description;
	
	private Date dueDate;

	private Date createdDate;

	private Date lastUpdatedDate;

	private String lastUpdatedBy;

	private String notes;
	
	private String caseInstanceId;
	
    private String taskId;
    
    private String status;

	private String actionType;
	private String type;
	private String createdBy;
	private int daysLeft;
	private String assessmentPlanId;
	private String report;
	
	private String recipients;
	
	private Map<String, Attachment> fileMetadata;
	
	private List<Long> deletedAttachmentIds;
	
	
    private List<SignalURL> signalUrls;

	private String assignTo;
	private String owner;
	
	private Long templateId;
	
	private int inDays;

}