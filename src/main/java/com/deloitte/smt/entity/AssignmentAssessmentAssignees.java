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
@Table(name = "sm_assignment_assessment_assignees")
public class AssignmentAssessmentAssignees {
	 
		
		@Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;
		private Date createdDate;
	    private String createdBy;
	    private Date lastModifiedDate;
	    private Long userGroupKey;
	    private Long userKey;
	    private Long assignmentConfigurationId;
	    
}
