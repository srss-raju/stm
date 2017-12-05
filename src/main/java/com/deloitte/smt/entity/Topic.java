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

/**
 * Created by RKB on 04-04-2017.
 */
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
	
	@Transient
	private List<Comments> comments;
	
    private Long casesCount;
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy="topic", cascade = CascadeType.ALL)
    private Set<SignalStatistics> signalStatistics;

    @Transient
    private Ingredient ingredient;
    
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
    
	public Topic() {
        this.startDate = new Date();
    }

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

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getSignalValidation() {
        return signalValidation;
    }

    public void setSignalValidation(String signalValidation) {
        this.signalValidation = signalValidation;
    }

    public String getSignalConfirmation() {
        return signalConfirmation;
    }

    public void setSignalConfirmation(String signalConfirmation) {
        this.signalConfirmation = signalConfirmation;
    }

    public String getValidationComments() {
        return validationComments;
    }

    public void setValidationComments(String validationComments) {
        this.validationComments = validationComments;
    }

    public String getSignalStatus() {
        return signalStatus;
    }

    public void setSignalStatus(String signalStatus) {
        this.signalStatus = signalStatus;
    }

    public String getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(String signalStrength) {
        this.signalStrength = signalStrength;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
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

    public List<Long> getDeletedAttachmentIds() {
        return deletedAttachmentIds;
    }

    public void setDeletedAttachmentIds(List<Long> deletedAttachmentIds) {
        this.deletedAttachmentIds = deletedAttachmentIds;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

	public Ingredient getIngredient() {
		return ingredient;
	}

	public void setIngredient(Ingredient ingredient) {
		this.ingredient = ingredient;
	}

	public Long getCasesCount() {
		return casesCount;
	}

	public void setCasesCount(Long casesCount) {
		this.casesCount = casesCount==null? 0 : casesCount;
	}

	public Set<SignalStatistics> getSignalStatistics() {
		return signalStatistics;
	}

	public void setSignalStatistics(Set<SignalStatistics> signalStatistics) {
		this.signalStatistics = signalStatistics;
	}

    public Map<String, Attachment> getFileMetadata() {
        return fileMetadata;
    }

    public void setFileMetadata(Map<String, Attachment> fileMetadata) {
        this.fileMetadata = fileMetadata;
    }

	public List<Soc> getSocs() {
		return socs;
	}

	public void setSocs(List<Soc> socs) {
		this.socs = socs;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Long getRunInstanceId() {
		return runInstanceId;
	}

	public void setRunInstanceId(Long runInstanceId) {
		this.runInstanceId = runInstanceId;
	}

    public String getCases() {
        return cases;
    }

    public void setCases(String cases) {
        this.cases = cases;
    }

	public String getCaselistId() {
		return caselistId;
	}

	public void setCaselistId(String caselistId) {
		this.caselistId = caselistId;
	}

	public List<SignalURL> getSignalUrls() {
		return signalUrls;
	}

	public void setSignalUrls(List<SignalURL> signalUrls) {
		this.signalUrls = signalUrls;
	}

	public Long getConfidenceIndex() {
		return confidenceIndex;
	}

	public void setConfidenceIndex(Long confidenceIndex) {
		this.confidenceIndex = confidenceIndex;
	}

	public Long getCohortPercentage() {
		return cohortPercentage;
	}

	public void setCohortPercentage(Long cohortPercentage) {
		this.cohortPercentage = cohortPercentage;
	}

	public List<AssessmentPlan> getAssessmentPlans() {
		return assessmentPlans;
	}

	public void setAssessmentPlans(List<AssessmentPlan> assessmentPlans) {
		this.assessmentPlans = assessmentPlans;
	}

	public List<Comments> getComments() {
		return comments;
	}

	public void setComments(List<Comments> comments) {
		this.comments = comments;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public List<TopicSignalValidationAssignmentAssignees> getTopicSignalValidationAssignmentAssignees() {
		return topicSignalValidationAssignmentAssignees;
	}

	public void setTopicSignalValidationAssignmentAssignees(
			List<TopicSignalValidationAssignmentAssignees> topicSignalValidationAssignmentAssignees) {
		this.topicSignalValidationAssignmentAssignees = topicSignalValidationAssignmentAssignees;
	}

	public String getSourceLabel() {
		return sourceLabel;
	}

	public void setSourceLabel(String sourceLabel) {
		this.sourceLabel = sourceLabel;
	}

	public List<SignalStrength> getSignalStrengthAtrributes() {
		return signalStrengthAtrributes;
	}

	public void setSignalStrengthAtrributes(List<SignalStrength> signalStrengthAtrributes) {
		this.signalStrengthAtrributes = signalStrengthAtrributes;
	}

	public List<TopicSocAssignmentConfiguration> getConditions() {
		return conditions;
	}

	public void setConditions(List<TopicSocAssignmentConfiguration> conditions) {
		this.conditions = conditions;
	}

	public List<TopicProductAssignmentConfiguration> getProducts() {
		return products;
	}

	public void setProducts(List<TopicProductAssignmentConfiguration> products) {
		this.products = products;
	}

	
	
	

	
}
