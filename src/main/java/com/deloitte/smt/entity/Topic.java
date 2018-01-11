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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

/**
 * Created by RKB on 04-04-2017.
 */
@Data
@Entity
@Table(name = "sm_topic")
public class Topic implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;
	private String description;

	private String sourceName;
	private String sourceUrl;
	private Date startDate;
	private Date endDate;
	private Date createdDate;
	private String createdBy;
	private Date lastModifiedDate;
	private String remarks;
	private String signalValidation;
	private String signalConfirmation;
	private String validationComments;
	private String signalStatus;
	private String signalStrength;  //TODO
	private Date dueDate;
	private String assignTo;
	private Long runInstanceId;
	private String cases;
	private String caselistId;
	private String modifiedBy;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "topicId")
	private List<SignalURL> signalUrls;
	
	private Long confidenceIndex;
	private Long cohortPercentage;
	private String owner;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "topicId")
	private List<TopicAssignees> topicAssignees;

	@OneToMany(cascade = CascadeType.MERGE)
	@JoinColumn(name = "topicId")
	private List<SignalStrength> signalStrengthAtrributes;

	/** Signal created Manual or Automated **/
	private String sourceLabel;

	@OneToMany
	@JoinColumn(name = "topicId")
	private List<Comments> comments;

	private Long casesCount;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "topic", cascade = CascadeType.ALL)
	private Set<SignalStatistics> signalStatistics;

	@ManyToOne(fetch = FetchType.EAGER)
	private AssessmentPlan assessmentPlan;

	
	@OneToMany
	@JoinColumn(name = "topicId")
	private List<Attachment> attachments;
	
	@Transient
	private List<AssessmentPlan> assessmentPlans;

	@OneToMany
	@JoinColumn(name = "topicId")
	private List<TopicCondition> conditions;

	@OneToMany
	@JoinColumn(name = "topicId")
	private List<TopicProduct> products;

}
