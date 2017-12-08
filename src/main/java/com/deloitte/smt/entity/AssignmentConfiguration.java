package com.deloitte.smt.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rkb on 04-05-2017.
 */
@Entity
@Table(name = "sm_assignment_configuration")
public class AssignmentConfiguration implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 274553099392305910L;
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date createdDate;
    private String createdBy;
    private Date lastModifiedDate;
    private String name;
    @JsonProperty
    private boolean isDefault;
    private String ingredient;
    private String signalSource;
    private String severity;
    private String signalOwner;
    private String assessmentOwner;
    private String riskOwner;
    @Transient
	private List<SignalValidationAssignmentAssignees> signalAssignees;
    @Transient
	private List<AssessmentAssignmentAssignees> assessmentAssignees;
    @Transient
	private List<RiskPlanAssignmentAssignees> riskAssignees;
    
    @Transient
	private List<SocAssignmentConfiguration> conditions;
    @Transient
	private List<ProductAssignmentConfiguration> products;
    
    @JsonIgnore
    @Transient
    private boolean conditionFlag;
    
    @JsonIgnore
    @Transient
    private boolean productFlag;
    
    @JsonIgnore
    private String totalRecordKey;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getSignalSource() {
        return signalSource;
    }

    public void setSignalSource(String signalSource) {
        this.signalSource = signalSource;
    }

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public List<SocAssignmentConfiguration> getConditions() {
		return conditions;
	}

	public void setConditions(List<SocAssignmentConfiguration> conditions) {
		this.conditions = conditions;
	}

	public String getSignalOwner() {
		return signalOwner;
	}

	public void setSignalOwner(String signalOwner) {
		this.signalOwner = signalOwner;
	}

	public String getAssessmentOwner() {
		return assessmentOwner;
	}

	public void setAssessmentOwner(String assessmentOwner) {
		this.assessmentOwner = assessmentOwner;
	}

	public String getRiskOwner() {
		return riskOwner;
	}

	public void setRiskOwner(String riskOwner) {
		this.riskOwner = riskOwner;
	}

	public List<SignalValidationAssignmentAssignees> getSignalAssignees() {
		return signalAssignees;
	}

	public void setSignalAssignees(
			List<SignalValidationAssignmentAssignees> signalAssignees) {
		this.signalAssignees = signalAssignees;
	}

	public List<AssessmentAssignmentAssignees> getAssessmentAssignees() {
		return assessmentAssignees;
	}

	public void setAssessmentAssignees(
			List<AssessmentAssignmentAssignees> assessmentAssignees) {
		this.assessmentAssignees = assessmentAssignees;
	}

	public List<RiskPlanAssignmentAssignees> getRiskAssignees() {
		return riskAssignees;
	}

	public void setRiskAssignees(List<RiskPlanAssignmentAssignees> riskAssignees) {
		this.riskAssignees = riskAssignees;
	}

	public List<ProductAssignmentConfiguration> getProducts() {
		return products;
	}

	public void setProducts(List<ProductAssignmentConfiguration> products) {
		this.products = products;
	}

	public String getTotalRecordKey() {
		return totalRecordKey;
	}

	public void setTotalRecordKey(String totalRecordKey) {
		this.totalRecordKey = totalRecordKey;
	}

	public boolean isConditionFlag() {
		return conditionFlag;
	}

	public void setConditionFlag(boolean conditionFlag) {
		this.conditionFlag = conditionFlag;
	}

	public boolean isProductFlag() {
		return productFlag;
	}

	public void setProductFlag(boolean productFlag) {
		this.productFlag = productFlag;
	}

}
