package com.deloitte.smt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "sm_assessment_plan")
public class AssessmentPlan {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "assessmentPlan")
	@JsonIgnore
	private Set<Topic> topics;
	private String assessmentName;
	private String priority;
	private int inDays;
    private String comments;
    private String ingrediantName;
	private String source;
    private String caseInstanceId;
	private String assessmentPlanStatus;
	private String assessmentRiskStatus;
	private Date assessmentDueDate;
	private String finalAssessmentSummary;
	@OneToOne
	private RiskPlan riskPlan;
	@Transient
	private List<Long> deletedAttachmentIds;

	private Date createdDate;
	private String createdBy;
	private Date lastModifiedDate;
	@Transient
	private Map<String, String> fileMetadata;
	
	@Transient
	private List<Long> templateIds;

	private String assignTo;
	
	private String assessmentTaskStatus;

	public AssessmentPlan() {
		this.topics = new HashSet<>();
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Set<Topic> getTopics() {
		return topics;
	}

	public void setTopics(Set<Topic> topics) {
		this.topics = topics;
	}

	public String getAssessmentName() {
		return assessmentName;
	}
	public void setAssessmentName(String assessmentName) {
		this.assessmentName = assessmentName;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public int getInDays() {
		return inDays;
	}
	public void setInDays(int inDays) {
		this.inDays = inDays;
	}

	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getCaseInstanceId() {
		return caseInstanceId;
	}
	public void setCaseInstanceId(String caseInstanceId) {
		this.caseInstanceId = caseInstanceId;
	}

	public String getAssessmentPlanStatus() {
		return assessmentPlanStatus;
	}

	public void setAssessmentPlanStatus(String assessmentPlanStatus) {
		this.assessmentPlanStatus = assessmentPlanStatus;
	}

	public String getAssessmentRiskStatus() {
		return assessmentRiskStatus;
	}

	public void setAssessmentRiskStatus(String assessmentRiskStatus) {
		this.assessmentRiskStatus = assessmentRiskStatus;
	}

	public Date getAssessmentDueDate() {
		return assessmentDueDate;
	}

	public void setAssessmentDueDate(Date assessmentDueDate) {
		this.assessmentDueDate = assessmentDueDate;
	}

	public String getFinalAssessmentSummary() {
		return finalAssessmentSummary;
	}

	public void setFinalAssessmentSummary(String finalAssessmentSummary) {
		this.finalAssessmentSummary = finalAssessmentSummary;
	}

	public RiskPlan getRiskPlan() {
		return riskPlan;
	}

	public void setRiskPlan(RiskPlan riskPlan) {
		this.riskPlan = riskPlan;
	}

	public List<Long> getDeletedAttachmentIds() {
		return deletedAttachmentIds;
	}

	public void setDeletedAttachmentIds(List<Long> deletedAttachmentIds) {
		this.deletedAttachmentIds = deletedAttachmentIds;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Map<String, String> getFileMetadata() {
		return fileMetadata;
	}

	public void setFileMetadata(Map<String, String> fileMetadata) {
		this.fileMetadata = fileMetadata;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getIngrediantName() {
		return ingrediantName;
	}

	public void setIngrediantName(String ingrediantName) {
		this.ingrediantName = ingrediantName;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public List<Long> getTemplateIds() {
		return templateIds;
	}

	public void setTemplateIds(List<Long> templateIds) {
		this.templateIds = templateIds;
	}

	public String getAssignTo() {
		return assignTo;
	}

	public void setAssignTo(String assignTo) {
		this.assignTo = assignTo;
	}

	public String getAssessmentTaskStatus() {
		return assessmentTaskStatus;
	}

	public void setAssessmentTaskStatus(String assessmentTaskStatus) {
		this.assessmentTaskStatus = assessmentTaskStatus;
	}
}
