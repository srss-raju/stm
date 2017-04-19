package com.deloitte.smt.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "sm_soc")
public class Soc {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private String socName;
	private Long topicId;
	
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
