package com.deloitte.smt.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "sm_hlt")
public class Hlt implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private String hltName;
	private Long topicId;
	private Long socId;
	private Long hlgtId;
	private Long detectionId;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "hltId")
	private List<Pt> pts;

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

	public Long getDetectionId() {
		return detectionId;
	}

	public void setDetectionId(Long detectionId) {
		this.detectionId = detectionId;
	}
	@ManyToOne
	@JoinColumn(name = "hlgtId")
	public Long getHlgtId() {
		return hlgtId;
	}
	public void setHlgtId(Long hlgtId) {
		this.hlgtId = hlgtId;
	}
	public List<Pt> getPts() {
		return pts;
	}
	public void setPts(List<Pt> pts) {
		this.pts = pts;
	}
	public Soc getSoc() {
		return soc;
	}
	public void setSoc(Soc soc) {
		this.soc = soc;
	}
	
}
