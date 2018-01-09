package com.deloitte.smt.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private Date createdDate;
    private String createdBy;
    private String modifiedBy;
    private Date lastModifiedDate;
    private String summary;
    private String caseInstanceId;
    private String status;
    private String source;
    private String assignTo;
    private Date riskDueDate;
    private String riskTaskStatus;
    
	private List<Comments> comments;
    
    private Long assessmentId;

 	
    private List<SignalURL> signalUrls;
 	
 	
	private List<Long> riskTemplateIds;
 	
 	private String owner;
 	
 	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "riskId")
	private List<RiskPlanAssignees> topicRiskPlanAssignmentAssignees;
 	
 	 @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, mappedBy = "riskPlan")
     @JsonIgnore
     private AssessmentPlan assessmentPlan;
     
     
 	private List<Long> deletedAttachmentIds;
     
     private Map<String, Attachment> fileMetadata;
     
    public RiskPlan(){
    	//Do Nothing
    }
    
    public RiskPlan(Long id, String name, String description, int inDays, Date createdDate, String createdBy,
			Date lastModifiedDate, String summary, String caseInstanceId, String status,
			String source, String assignTo, Date riskDueDate, String riskTaskStatus) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.inDays = inDays;
		this.createdDate = createdDate;
		this.createdBy = createdBy;
		this.lastModifiedDate = lastModifiedDate;
		this.summary = summary;
		this.caseInstanceId = caseInstanceId;
		this.status = status;
		this.source = source;
		this.assignTo = assignTo;
		this.riskDueDate = riskDueDate;
		this.riskTaskStatus = riskTaskStatus;
	}
   
}
