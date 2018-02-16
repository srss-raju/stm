package com.deloitte.smt.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
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

import lombok.Getter;
import lombok.Setter;

/**
 * Created by RKB on 04-04-2017.
 */
@Getter
@Setter
@Entity
@Table(name = "sm_topic")
public class Topic implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;
    
	@Embedded
	private Condition condition;
	private String sourceName;
	private String sourceUrl;
	private Date startDate;
	private Date endDate;
	private Date createdDate;
	private String createdBy;
	private Date lastModifiedDate;
	private String remarks;	
	private String processId;
	private String signalValidation;
	private String signalConfirmation;
	private String validationComments;
	private String signalStatus;
	private String signalStrength;
	private Date dueDate;
	private String assignTo;
	private Long runInstanceId;
	private String cases;
	private String caselistId;
	private String modifiedBy;
	@Transient
	private List<SignalURL> signalUrls;
	private Long confidenceIndex;
	private Long cohortPercentage;
	private String owner;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "topicId")
	private List<TopicSignalValidationAssignmentAssignees> topicSignalValidationAssignmentAssignees;
	
	@OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "topicId")
	private List<SignalStrength> signalStrengthAtrributes;
	
	/**Signal created Manual or Automated **/
	private String sourceLabel;
	
	private String productKey;
	
	@Transient
	private List<Comments> comments;
	
    private Long casesCount;
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy="topic", cascade = CascadeType.ALL)
    private Set<SignalStatistics> signalStatistics;

    @Transient
    private List<Soc> socs;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private AssessmentPlan assessmentPlan;

    @Transient
    private List<Long> deletedAttachmentIds;

    @Transient
    private Map<String, Attachment> fileMetadata;
    
    @Transient
    private List<AssessmentPlan> assessmentPlans;
    
    
    @Transient
	private List<TopicSocAssignmentConfiguration> conditions;
    @Transient
	private List<TopicProductAssignmentConfiguration> products;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "topicId")
	private List<Query> queries;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "topicId")
	private List<Stratification> stratifications;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "topicId")
	private List<Ingredient> ingredients;
	
	private String signalType;
	
    
    public Topic(Long id, String name, String description, String sourceName,
			String signalConfirmation, String signalStatus, Date createdDate) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.sourceName = sourceName;
		this.createdDate = createdDate;
		this.signalConfirmation = signalConfirmation;
		this.signalStatus = signalStatus;
	}
    
	public Topic() {
        this.startDate = new Date();
    }

   
	
}
