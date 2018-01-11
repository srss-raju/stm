package com.deloitte.smt.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * Created by Rajesh on 31-07-2017.
 */
@Data
@Entity
@Table(name = "sm_assignment_riskplan_assignees")
public class AssignmentRiskPlanAssignees {
	 
		@Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;
		private Long userGroupKey;
	    private Long userKey;
		private Date createdDate;
	    private String createdBy;
	    private Date lastModifiedDate;
	    
}
