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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

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
    
	private List<AssignmentSignalAssignees> signalAssignees;
    
	private List<AssignmentAssessmentAssignees> assessmentAssignees;
    
	private List<AssignmentRiskPlanAssignees> riskAssignees;
    
    
	private List<AssignmentCondition> conditions;
    
	private List<AssignmentProduct> products;
    
    @JsonIgnore
    
    private boolean conditionFlag;
    
    @JsonIgnore
    
    private boolean productFlag;
    
    @JsonIgnore
    
    private boolean repeatProductFlag;
    
    @JsonIgnore
    
    private boolean repeatSocFlag;
    
    @JsonIgnore
    
    private boolean conditionEmptyFlag;
    
    @JsonIgnore
    
    private boolean productEmptyFlag;
    
    @JsonIgnore
    private String totalRecordKey;
    
    
}
