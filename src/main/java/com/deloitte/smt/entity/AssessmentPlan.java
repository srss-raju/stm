package com.deloitte.smt.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
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

import lombok.Data;

@Data
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
	private String priority;
	private int inDays;
	private String source;
    private String caseInstanceId;
	private String assessmentPlanStatus;
	private String assessmentRiskStatus;
	private Date assessmentDueDate;
	private String finalAssessmentSummary;
	@OneToOne
	private RiskPlan riskPlan;
	
	@OneToMany
	@JoinColumn(name = "assessmentPlanId")
	private List<Attachment> attachments;
	
	private Long cohortPercentage;

	private Date createdDate;
	private String createdBy;
	private String modifiedBy;
	private Date lastModifiedDate;
	
	
	@Transient
	private List<Long> templateIds;

	private String assignTo;
	
	@OneToMany
	@JoinColumn(name = "assessmentPlanId")
	private List<SignalURL> signalUrls;
	
	private String assessmentTaskStatus;
	
	@OneToMany
	@JoinColumn(name = "assessmentPlanId")
	private List<Comments> comments;
	private String owner;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "assessmentId")
	private List<AssessmentAssignees> assessmentAssignees;

}
