package com.deloitte.smt.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "sm_risk_task_template_products")
public class RiskTaskTemplateProducts {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private String name;
	private String recordKey;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "riskTaskTemplateProductId")
	private List<RiskTaskTemplateProductValues> recordValues;
	
	
	@Transient
	@JsonIgnore
	private RiskTaskTemplate riskTaskTemplate;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getRecordKey() {
		return recordKey;
	}
	public void setRecordKey(String recordKey) {
		this.recordKey = recordKey;
	}
	public List<RiskTaskTemplateProductValues> getRecordValues() {
		return recordValues;
	}
	public void setRecordValues(List<RiskTaskTemplateProductValues> recordValues) {
		this.recordValues = recordValues;
	}
	public RiskTaskTemplate getRiskTaskTemplate() {
		return riskTaskTemplate;
	}
	public void setRiskTaskTemplate(RiskTaskTemplate riskTaskTemplate) {
		this.riskTaskTemplate = riskTaskTemplate;
	}
	
}
