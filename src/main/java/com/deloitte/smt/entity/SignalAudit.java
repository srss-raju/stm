package com.deloitte.smt.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonRawValue;

@Entity
@Table(name = "sm_signal_audit")
public class SignalAudit   implements Serializable {

	private static final long serialVersionUID = -3652414302035277522L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	private String entityType;
	@Column(columnDefinition = "TEXT")
	@JsonRawValue
	private String originalValue;
	@Column(columnDefinition = "TEXT")
	@JsonRawValue
	private String modifiedValue;
	private String createdBy;
	private String modifieddBy;
	private Date createdDate;
	private Date modifiedDate;
	private String operation;
	
	@Transient
	private List<SignalAttachmentAudit> signalAttachmentAudit;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	public String getOriginalValue() {
		return originalValue;
	}
	public void setOriginalValue(String originalValue) {
		this.originalValue = originalValue;
	}
	public String getModifiedValue() {
		return modifiedValue;
	}
	public void setModifiedValue(String modifiedValue) {
		this.modifiedValue = modifiedValue;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getModifieddBy() {
		return modifieddBy;
	}
	public void setModifieddBy(String modifieddBy) {
		this.modifieddBy = modifieddBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public List<SignalAttachmentAudit> getSignalAttachmentAudit() {
		return signalAttachmentAudit;
	}
	public void setSignalAttachmentAudit(
			List<SignalAttachmentAudit> signalAttachmentAudit) {
		this.signalAttachmentAudit = signalAttachmentAudit;
	}
}
