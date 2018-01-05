package com.deloitte.smt.entity;

import java.io.Serializable;
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
@Table(name = "sm_assessment_action")
public class SignalAction implements Serializable{
	
	private static final long serialVersionUID = -5637786500061885596L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	private String actionName;
    private String actionType;
	private String actionDescription;
	private String actionNotes;
    private Date dueDate;
    private int daysLeft;
    private Date createdDate;
	private Date lastModifiedDate;
    private String assignTo;
	private String owner;
    private String actionStatus;
    private String assessmentId;
    private String caseInstanceId;
    private String report;
    private String taskId;
	private String recipients;
	private String createdBy;
	private Long templateId;
	private Integer inDays;
	private String modifiedBy;

	@Transient
	private Map<String, Attachment> fileMetadata;
	@Transient
	private List<Long> deletedAttachmentIds;
	@Transient
    private List<SignalURL> signalUrls;

}
