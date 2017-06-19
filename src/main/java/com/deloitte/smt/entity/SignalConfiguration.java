package com.deloitte.smt.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by cavula
 */
@Entity
@Table(name = "sm_signal_configuration")
public class SignalConfiguration implements Serializable {
  
	/**
	 * 
	 */
	private static final long serialVersionUID = -4862256768995870660L;
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private Integer cohortPercentage;
	private Integer confidenceIndex;
	
	@Column(unique=true)
	private String configName;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getConfigName() {
		return configName;
	}
	public void setConfigName(String configName) {
		this.configName = configName;
	}
	public Integer getCohortPercentage() {
		return cohortPercentage;
	}
	public void setCohortPercentage(Integer cohortPercentage) {
		this.cohortPercentage = cohortPercentage;
	}
	public Integer getConfidenceIndex() {
		return confidenceIndex;
	}
	public void setConfidenceIndex(Integer confidenceIndex) {
		this.confidenceIndex = confidenceIndex;
	}
	
	

  }
