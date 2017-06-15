package com.deloitte.smt.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sm_soc")
public class Soc implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private String socName;
	private Long topicId;
	private Long detectionId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSocName() {
		return socName;
	}
	public void setSocName(String socName) {
		this.socName = socName;
	}
	public Long getTopicId() {
		return topicId;
	}
	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public Long getDetectionId() {
		return detectionId;
	}

	public void setDetectionId(Long detectionId) {
		this.detectionId = detectionId;
	}

}
