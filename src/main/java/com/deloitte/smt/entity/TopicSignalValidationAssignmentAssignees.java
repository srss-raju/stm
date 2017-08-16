package com.deloitte.smt.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "sm_topic_signal_validation_assignees")
public class TopicSignalValidationAssignmentAssignees {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Date createdDate;
	private String createdBy;
	private Date lastModifiedDate;
	private String assignTo;
	private Long userGroupKeys;
	private Long userKeys;

	@ManyToOne
	@JoinColumn(name = "TOPIC_ID", referencedColumnName = "id")
	private Topic topic;

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getAssignTo() {
		return assignTo;
	}

	public void setAssignTo(String assignTo) {
		this.assignTo = assignTo;
	}

	public Long getUserGroupKeys() {
		return userGroupKeys;
	}

	public void setUserGroupKeys(Long userGroupKeys) {
		this.userGroupKeys = userGroupKeys;
	}

	public Long getUserKeys() {
		return userKeys;
	}

	public void setUserKeys(Long userKeys) {
		this.userKeys = userKeys;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
