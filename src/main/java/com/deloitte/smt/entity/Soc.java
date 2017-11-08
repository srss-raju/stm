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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
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
	private String hanaSocId;
	private boolean hlgtSelected;
	private boolean hltSelected;
	private boolean ptSelected;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "socId")
    private List<Hlgt> hlgts;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "socId")
    private List<Hlt> hlts;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "socId")
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
	public String getHanaSocId() {
		return hanaSocId;
	}
	public void setHanaSocId(String hanaSocId) {
		this.hanaSocId = hanaSocId;
	}
	public boolean isHlgtSelected() {
		return hlgtSelected;
	}
	public void setHlgtSelected(boolean hlgtSelected) {
		this.hlgtSelected = hlgtSelected;
	}
	public boolean isHltSelected() {
		return hltSelected;
	}
	public void setHltSelected(boolean hltSelected) {
		this.hltSelected = hltSelected;
	}
	public boolean isPtSelected() {
		return ptSelected;
	}
	public void setPtSelected(boolean ptSelected) {
		this.ptSelected = ptSelected;
	}

}
