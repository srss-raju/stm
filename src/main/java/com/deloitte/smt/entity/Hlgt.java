package com.deloitte.smt.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sm_hlgt")
public class Hlgt {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private String hlgtName;
	private Long topicId;
	private Long socId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getHlgtName() {
		return hlgtName;
	}
	public void setHlgtName(String hlgtName) {
		this.hlgtName = hlgtName;
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
}