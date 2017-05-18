package com.deloitte.smt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by myelleswarapu on 12-04-2017.
 */
@Entity
@Table(name = "sm_risk_plan")
public class RiskPlan implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private int inDays;
    private Date createdDate;
    private String createdBy;
    private Date lastModifiedDate;
    private String summary;
    private String caseInstanceId;
    private String status;
    private String ingredient;
    private String source;
    private String assignTo;
    private Date riskDueDate;
    private String riskTaskStatus;
    @Transient
	private List<Comments> comments;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, mappedBy = "riskPlan")
    @JsonIgnore
    private AssessmentPlan assessmentPlan;
    
    @Transient
	private List<Long> deletedAttachmentIds;
    @Transient
    private Map<String, Attachment> fileMetadata;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getInDays() {
        return inDays;
    }

    public void setInDays(int inDays) {
        this.inDays = inDays;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCaseInstanceId() {
        return caseInstanceId;
    }

    public void setCaseInstanceId(String caseInstanceId) {
        this.caseInstanceId = caseInstanceId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAssignTo() {
        return assignTo;
    }

    public void setAssignTo(String assignTo) {
        this.assignTo = assignTo;
    }

    public AssessmentPlan getAssessmentPlan() {
        return assessmentPlan;
    }

    public void setAssessmentPlan(AssessmentPlan assessmentPlan) {
        this.assessmentPlan = assessmentPlan;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

	public List<Long> getDeletedAttachmentIds() {
		return deletedAttachmentIds;
	}

	public void setDeletedAttachmentIds(List<Long> deletedAttachmentIds) {
		this.deletedAttachmentIds = deletedAttachmentIds;
	}

    public Map<String, Attachment> getFileMetadata() {
        return fileMetadata;
    }

    public void setFileMetadata(Map<String, Attachment> fileMetadata) {
        this.fileMetadata = fileMetadata;
    }

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getRiskDueDate() {
		return riskDueDate;
	}

	public void setRiskDueDate(Date riskDueDate) {
		this.riskDueDate = riskDueDate;
	}

	public String getRiskTaskStatus() {
		return riskTaskStatus;
	}

	public void setRiskTaskStatus(String riskTaskStatus) {
		this.riskTaskStatus = riskTaskStatus;
	}

	public List<Comments> getComments() {
		return comments;
	}

	public void setComments(List<Comments> comments) {
		this.comments = comments;
	}
}
