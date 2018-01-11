package com.deloitte.smt.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
	private String notes;
	private String caseInstanceId;
    private String status;
	private String actionType;
	private String type;
	private int daysLeft;
	private String assessmentPlanId;
	private String report;
	private String recipients;
	private String owner;
	private Long templateId;
	private int inDays;
	
	@OneToMany
	@JoinColumn(name = "taskId")
	private List<Attachment> attachments;

	@OneToMany
	@JoinColumn(name = "taskId")
    private List<SignalURL> signalUrls;

	private String createdBy;
	private Date createdDate;
	private Date lastUpdatedDate;
	private String lastUpdatedBy;
}
