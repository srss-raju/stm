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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getActionDescription() {
		return actionDescription;
	}

	public void setActionDescription(String actionDescription) {
		this.actionDescription = actionDescription;
	}

	public String getActionNotes() {
		return actionNotes;
	}

	public void setActionNotes(String actionNotes) {
		this.actionNotes = actionNotes;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public int getDaysLeft() {
		return daysLeft;
	}

	public void setDaysLeft(int daysLeft) {
		this.daysLeft = daysLeft;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getAssignTo() {
		return assignTo;
	}

	public void setAssignTo(String assignTo) {
		this.assignTo = assignTo;
	}

	public String getActionStatus() {
		return actionStatus;
	}

	public void setActionStatus(String actionStatus) {
		this.actionStatus = actionStatus;
	}

	public String getAssessmentId() {
		return assessmentId;
	}

	public void setAssessmentId(String assessmentId) {
		this.assessmentId = assessmentId;
	}

	public String getCaseInstanceId() {
		return caseInstanceId;
	}

	public void setCaseInstanceId(String caseInstanceId) {
		this.caseInstanceId = caseInstanceId;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getRecipients() {
		return recipients;
	}

	public void setRecipients(String recipients) {
		this.recipients = recipients;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Map<String, Attachment> getFileMetadata() {
		return fileMetadata;
	}

	public void setFileMetadata(Map<String, Attachment> fileMetadata) {
		this.fileMetadata = fileMetadata;
	}

	public List<Long> getDeletedAttachmentIds() {
		return deletedAttachmentIds;
	}

	public void setDeletedAttachmentIds(List<Long> deletedAttachmentIds) {
		this.deletedAttachmentIds = deletedAttachmentIds;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public int getInDays() {
		return inDays;
	}

	public void setInDays(int inDays) {
		this.inDays = inDays;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public List<SignalURL> getSignalUrls() {
		return signalUrls;
	}

	public void setSignalUrls(List<SignalURL> signalUrls) {
		this.signalUrls = signalUrls;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
}
