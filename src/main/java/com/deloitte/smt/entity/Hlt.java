package com.deloitte.smt.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sm_hlt")
public class Hlt {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private String hltName;
	private Long topicId;
	private Long socId;
	private Long detectionId;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getHltName() {
		return hltName;
	}
	public void setHltName(String hltName) {
		this.hltName = hltName;
	}
	public Long getTopicId() {
		return topicId;
	}
	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}
	public Long getSocId() {
		return socId;
	}
	public void setSocId(Long socId) {
		this.socId = socId;
	}

	public Long getDetectionId() {
		return detectionId;
	}

	public void setDetectionId(Long detectionId) {
		this.detectionId = detectionId;
	}
}
