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

/**
 * Created by myelleswarapu on 04-05-2017.
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
    private String ingredient;
    private String signalSource;
    private String severity;
    private String signalValidationAssignmentOwner;
    private String assessmentAssignmentOwner;
    private String riskPlanAssignmentOwner;
    @Transient
	private List<SignalValidationAssignmentAssignees> signalValidationAssignmentAssignees;
    @Transient
	private List<AssessmentAssignmentAssignees> assessmentAssignmentAssignees;
    @Transient
	private List<RiskPlanAssignmentAssignees> riskPlanAssignmentAssignees;
    
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

	public String getSignalValidationAssignmentOwner() {
		return signalValidationAssignmentOwner;
	}

	public void setSignalValidationAssignmentOwner(
			String signalValidationAssignmentOwner) {
		this.signalValidationAssignmentOwner = signalValidationAssignmentOwner;
	}

	public String getAssessmentAssignmentOwner() {
		return assessmentAssignmentOwner;
	}

	public void setAssessmentAssignmentOwner(String assessmentAssignmentOwner) {
		this.assessmentAssignmentOwner = assessmentAssignmentOwner;
	}

	public String getRiskPlanAssignmentOwner() {
		return riskPlanAssignmentOwner;
	}

	public void setRiskPlanAssignmentOwner(String riskPlanAssignmentOwner) {
		this.riskPlanAssignmentOwner = riskPlanAssignmentOwner;
	}

	public List<SignalValidationAssignmentAssignees> getSignalValidationAssignmentAssignees() {
		return signalValidationAssignmentAssignees;
	}

	public void setSignalValidationAssignmentAssignees(
			List<SignalValidationAssignmentAssignees> signalValidationAssignmentAssignees) {
		this.signalValidationAssignmentAssignees = signalValidationAssignmentAssignees;
	}

	public List<AssessmentAssignmentAssignees> getAssessmentAssignmentAssignees() {
		return assessmentAssignmentAssignees;
	}

	public void setAssessmentAssignmentAssignees(
			List<AssessmentAssignmentAssignees> assessmentAssignmentAssignees) {
		this.assessmentAssignmentAssignees = assessmentAssignmentAssignees;
	}

	public List<RiskPlanAssignmentAssignees> getRiskPlanAssignmentAssignees() {
		return riskPlanAssignmentAssignees;
	}

	public void setRiskPlanAssignmentAssignees(
			List<RiskPlanAssignmentAssignees> riskPlanAssignmentAssignees) {
		this.riskPlanAssignmentAssignees = riskPlanAssignmentAssignees;
	}

}
