package com.deloitte.smt.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.io.Serializable;
import java.util.List;

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
	
	@Transient
    private List<Hlgt> hlgts;
	
	@Transient
    private List<Hlt> hlts;
	
	@Transient
    private List<Pt> pts;
	
	
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

	public List<Hlgt> getHlgts() {
		return hlgts;
	}
	public void setHlgts(List<Hlgt> hlgts) {
		this.hlgts = hlgts;
	}
	public List<Hlt> getHlts() {
		return hlts;
	}
	public void setHlts(List<Hlt> hlts) {
		this.hlts = hlts;
	}
	public List<Pt> getPts() {
		return pts;
	}
	public void setPts(List<Pt> pts) {
		this.pts = pts;
	}
}
