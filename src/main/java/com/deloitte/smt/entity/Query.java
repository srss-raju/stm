package com.deloitte.smt.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "sm_detection_query")
public class Query  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private Date createdDate;
	private String createdBy;
	private Date lastModifiedDate;
	private String lastModifiedBy;
	private Long detectionId;
	private Long QUERY_KEY;
	private String QUERY_NAME;
	
	@JsonIgnore
	@ManyToOne
	private SignalDetection signalDetection;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Long getDetectionId() {
		return detectionId;
	}

	public void setDetectionId(Long detectionId) {
		this.detectionId = detectionId;
	}

	public Long getQUERY_KEY() {
		return QUERY_KEY;
	}

	public void setQUERY_KEY(Long QUERY_KEY) {
		this.QUERY_KEY = QUERY_KEY;
	}

	public String getQUERY_NAME() {
		return QUERY_NAME;
	}

	public void setUERY_NAME(String QERY_NAME) {
		this.QUERY_NAME = QERY_NAME;
	}

	public SignalDetection getSignalDetection() {
		return signalDetection;
	}

	public void setSignalDetection(SignalDetection signalDetection) {
		this.signalDetection = signalDetection;
	}
	
	}
