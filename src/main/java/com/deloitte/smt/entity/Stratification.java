package com.deloitte.smt.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "sm_detection_stratification")
public class Stratification  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@JsonProperty("STRATIFICATION_KEY")
	private Long stratificationKey;
	@JsonProperty("STRATIFICATION_NAME")
	private String stratificationName;
	
	@Transient
	@JsonIgnore
	private SignalDetection signalDetection;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "detectionId")
	public SignalDetection getSignalDetection() {
		return signalDetection;
	}

	public void setSignalDetection(SignalDetection signalDetection) {
		this.signalDetection = signalDetection;
	}

	public Long getStratificationKey() {
		return stratificationKey;
	}

	public void setStratificationKey(Long stratificationKey) {
		this.stratificationKey = stratificationKey;
	}

	public String getStratificationName() {
		return stratificationName;
	}

	public void setStratificationName(String stratificationName) {
		this.stratificationName = stratificationName;
	}

	}
