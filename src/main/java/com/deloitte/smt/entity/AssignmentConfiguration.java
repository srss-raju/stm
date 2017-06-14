package com.deloitte.smt.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
    /*@ManyToMany(cascade = CascadeType.MERGE)
    private List<SMUser> signalValidationAssignmentUsers;
    @ManyToMany(cascade = CascadeType.MERGE)
    private List<SMUser> assessmentAssignmentUsers;
    @ManyToMany(cascade = CascadeType.MERGE)
    private List<SMUser> riskPlanAssignmentUsers;*/
    
    private String signalValidationAssignmentUser;
    private String assessmentAssignmentUser;
    private String riskPlanAssignmentUser;
    
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

	public String getSignalValidationAssignmentUser() {
		return signalValidationAssignmentUser;
	}

	public void setSignalValidationAssignmentUser(
			String signalValidationAssignmentUser) {
		this.signalValidationAssignmentUser = signalValidationAssignmentUser;
	}

	public String getAssessmentAssignmentUser() {
		return assessmentAssignmentUser;
	}

	public void setAssessmentAssignmentUser(String assessmentAssignmentUser) {
		this.assessmentAssignmentUser = assessmentAssignmentUser;
	}

	public String getRiskPlanAssignmentUser() {
		return riskPlanAssignmentUser;
	}

	public void setRiskPlanAssignmentUser(String riskPlanAssignmentUser) {
		this.riskPlanAssignmentUser = riskPlanAssignmentUser;
	}
}
