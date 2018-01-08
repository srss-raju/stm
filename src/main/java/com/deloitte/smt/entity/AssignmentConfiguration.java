package com.deloitte.smt.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rkb on 04-05-2017.
 */
@Data
@Entity
@Table(name = "sm_assignment_configuration")
public class AssignmentConfiguration implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 274553099392305910L;
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date createdDate;
    private String createdBy;
    private Date lastModifiedDate;
    private String name;
    @JsonProperty
    private boolean isDefault;
    private String signalSource;
    private String severity;
    private String signalOwner;
    private String assessmentOwner;
    private String riskOwner;
    @Transient
	private List<AssignmentSignalAssignees> signalAssignees;
    @Transient
	private List<AssignmentAssessmentAssignees> assessmentAssignees;
    @Transient
	private List<AssignmentRiskPlanAssignees> riskAssignees;
    
    @Transient
	private List<AssignmentCondition> conditions;
    @Transient
	private List<AssignmentProduct> products;
    
    @JsonIgnore
    @Transient
    private boolean conditionFlag;
    
    @JsonIgnore
    @Transient
    private boolean productFlag;
    
    @JsonIgnore
    @Transient
    private boolean repeatProductFlag;
    
    @JsonIgnore
    @Transient
    private boolean repeatSocFlag;
    
    @JsonIgnore
    @Transient
    private boolean conditionEmptyFlag;
    
    @JsonIgnore
    @Transient
    private boolean productEmptyFlag;
    
    @JsonIgnore
    private String totalRecordKey;
    
    
}
