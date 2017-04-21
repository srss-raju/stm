package com.deloitte.smt.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sm_include_ae")
public class IncludeAE {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private String aeName;
	private Long detectionId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getDetectionId() {
		return detectionId;
	}
	public void setDetectionId(Long detectionId) {
		this.detectionId = detectionId;
	}
	public String getAeName() {
		return aeName;
	}
	public void setAeName(String aeName) {
		this.aeName = aeName;
	}
}
