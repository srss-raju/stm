package com.deloitte.smt.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by myelleswarapu on 04-05-2017.
 */
@Entity
@Table(name = "sm_assignment_configuration")
public class AssignmentConfiguration implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date createdDate;
    private String createdBy;
    private Date lastModifiedDate;
    private String ingredient;
    private String signalSource;
    @ManyToMany(cascade = CascadeType.MERGE)
    private List<SMUser> signalValidationAssignmentUsers;
    @ManyToMany(cascade = CascadeType.MERGE)
    private List<SMUser> assessmentAssignmentUsers;
    @ManyToMany(cascade = CascadeType.MERGE)
    private List<SMUser> riskPlanAssignmentUsers;

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

    public List<SMUser> getSignalValidationAssignmentUsers() {
        return signalValidationAssignmentUsers;
    }

    public void setSignalValidationAssignmentUsers(List<SMUser> signalValidationAssignmentUsers) {
        this.signalValidationAssignmentUsers = signalValidationAssignmentUsers;
    }

    public List<SMUser> getAssessmentAssignmentUsers() {
        return assessmentAssignmentUsers;
    }

    public void setAssessmentAssignmentUsers(List<SMUser> assessmentAssignmentUsers) {
        this.assessmentAssignmentUsers = assessmentAssignmentUsers;
    }

    public List<SMUser> getRiskPlanAssignmentUsers() {
        return riskPlanAssignmentUsers;
    }

    public void setRiskPlanAssignmentUsers(List<SMUser> riskPlanAssignmentUsers) {
        this.riskPlanAssignmentUsers = riskPlanAssignmentUsers;
    }
}
