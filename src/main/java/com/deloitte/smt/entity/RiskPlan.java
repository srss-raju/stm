package com.deloitte.smt.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * Created by RajeshKumarB on 12-04-2017.
 */
@Data
@Entity
@Table(name = "sm_risk_plan")
public class RiskPlan implements Serializable {

	private static final long serialVersionUID = -3652414302035277522L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private String description;
	private int inDays;
	@Column(length=10000)
	private String summary;
	private String caseInstanceId;
	private String status;
	private String source;
	private Date riskDueDate;
	private String riskTaskStatus;
	private String owner;

	@OneToMany
	@JoinColumn(name = "riskId")
	private List<Comments> comments;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "riskId")
	private List<SignalURL> signalUrls;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "riskId")
	private List<Long> riskTemplateIds;


	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "riskId")
	private List<RiskPlanAssignees> riskPlanAssignees;

	@OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, mappedBy = "riskPlan")
	@JsonIgnore
	private AssessmentPlan assessmentPlan;

	@OneToMany
	@JoinColumn(name = "riskId")
	private List<Attachment> attachments;
	
	private Date createdDate;
	private String createdBy;
	private String modifiedBy;
	private Date lastModifiedDate;
	
}
