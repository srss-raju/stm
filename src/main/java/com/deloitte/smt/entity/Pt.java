package com.deloitte.smt.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "sm_pt")
public class Pt implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private String ptName;
	private Long topicId;
	private Long socId;
	private Long hltId;
	private Long detectionId;
	private int ptId;
	private Long smqId;
	
	@Transient
	@JsonIgnore
	private Soc soc;
	
	@ManyToOne
	@JoinColumn(name = "socId")
	public Long getSocId() {
		return socId;
	}
	public void setSocId(Long socId) {
		this.socId = socId;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getTopicId() {
		return topicId;
	}
	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}
	
	public String getPtName() {
		return ptName;
	}
	public void setPtName(String ptName) {
		this.ptName = ptName;
	}

	public Long getDetectionId() {
		return detectionId;
	}

	public void setDetectionId(Long detectionId) {
		this.detectionId = detectionId;
	}
	
	public int getPtId() {
		return ptId;
	}
	public void setPtId(int ptId) {
		this.ptId = ptId;
	}
	public Long getSmqId() {
		return smqId;
	}
	public void setSmqId(Long smqId) {
		this.smqId = smqId;
	}
	@ManyToOne
	@JoinColumn(name = "hltId")
	public Long getHltId() {
		return hltId;
	}
	public void setHltId(Long hltId) {
		this.hltId = hltId;
	}
}
