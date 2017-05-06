package com.deloitte.smt.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "sm_signal_statistics")
public class SignalStatistics {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String algorithm;
	
	private double score;
	
	private double se;
	
	private double lb;
	
	private double ub;
	
	private String socName;
	
	private String ptName;
	
	private Long runInstanceId;
	
	@JsonIgnore
	@ManyToOne
	private Topic topic;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public double getSe() {
		return se;
	}

	public void setSe(double se) {
		this.se = se;
	}

	public double getLb() {
		return lb;
	}

	public void setLb(double lb) {
		this.lb = lb;
	}

	public double getUb() {
		return ub;
	}

	public void setUb(double ub) {
		this.ub = ub;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public String getSocName() {
		return socName;
	}

	public void setSocName(String socName) {
		this.socName = socName;
	}

	public String getPtName() {
		return ptName;
	}

	public void setPtName(String ptName) {
		this.ptName = ptName;
	}

	public Long getRunInstanceId() {
		return runInstanceId;
	}

	public void setRunInstanceId(Long runInstanceId) {
		this.runInstanceId = runInstanceId;
	}
}
