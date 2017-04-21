package com.deloitte.smt.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sm_denominator_for_poisson")
public class DenominatorForPoisson {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private String denominatorForPoissonName;
	private Long detectionId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDenominatorForPoissonName() {
		return denominatorForPoissonName;
	}
	public void setDenominatorForPoissonName(String denominatorForPoissonName) {
		this.denominatorForPoissonName = denominatorForPoissonName;
	}
	public Long getDetectionId() {
		return detectionId;
	}
	public void setDetectionId(Long detectionId) {
		this.detectionId = detectionId;
	}
}
