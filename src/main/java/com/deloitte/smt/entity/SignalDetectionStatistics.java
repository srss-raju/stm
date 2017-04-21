package com.deloitte.smt.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "sm_signal_detection_statistics")
public class SignalDetectionStatistics {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String algorithm;
	
	private double score;
	
	private double se;
	
	private double lb;
	
	private double ub;
	
	@JsonIgnore
	@ManyToOne
	private SignalDetection signalDetection;

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

	public SignalDetection getSignalDetection() {
		return signalDetection;
	}

	public void setSignalDetection(SignalDetection signalDetection) {
		this.signalDetection = signalDetection;
	}

}
