package com.deloitte.smt.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sm_assessment_plan")
public class AssessmentPlan  implements Serializable {

	private static final long serialVersionUID = -3652414302035277522L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "assessmentPlan")
	@JsonIgnore
	private Set<Topic> topics;
	private String assessmentName;
	private String name;
	private String priority;
	private int inDays;
	private String source;
    private String caseInstanceId;
	private String assessmentPlanStatus;
	private String assessmentRiskStatus;
	private Date assessmentDueDate;
	@Column(length = 100000)
	private String finalAssessmentSummary;
	@OneToOne
	private RiskPlan riskPlan;
	@Transient
	private List<Long> deletedAttachmentIds;
	@Transient
	private Long cohortPercentage;

	private Date createdDate;
	private String createdBy;
	private String modifiedBy;
	private Date lastModifiedDate;
	@Transient
	private Map<String, Attachment> fileMetadata;
	
	@Transient
	private List<Long> templateIds;

	private String assignTo;
	
	@Transient
    private List<SignalURL> signalUrls;
	
	private String assessmentTaskStatus;
	@Transient
	private List<Comments> comments;
	private String owner;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "assessmentId")
	private List<TopicAssessmentAssignmentAssignees> topicAssessmentAssignmentAssignees;
	

	public AssessmentPlan() {
		this.topics = new HashSet<>();
	}

	public AssessmentPlan(Long id, String assessmentName, String priority, int inDays, 
			String source, String caseInstanceId, String assessmentPlanStatus, String assessmentRiskStatus,
			Date assessmentDueDate, String finalAssessmentSummary, RiskPlan riskPlan, Date createdDate,
			String createdBy, Date lastModifiedDate, String assignTo, String assessmentTaskStatus) {
		this.id = id;
		this.assessmentName = assessmentName;
		this.priority = priority;
		this.inDays=inDays;
		this.source = source;
		this.caseInstanceId = caseInstanceId;
		this.assessmentPlanStatus = assessmentPlanStatus;
		this.assessmentRiskStatus = assessmentRiskStatus;
		this.assessmentDueDate = assessmentDueDate;
		this.finalAssessmentSummary = finalAssessmentSummary;
		this.riskPlan = riskPlan;
		this.createdDate = createdDate;
		this.createdBy = createdBy;
		this.lastModifiedDate = lastModifiedDate;
		this.assignTo = assignTo;
		this.assessmentTaskStatus = assessmentTaskStatus;
	}

}
