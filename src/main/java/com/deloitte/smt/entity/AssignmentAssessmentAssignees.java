package com.deloitte.smt.entity;

import java.io.Serializable;
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
public class AssignmentAssessmentAssignees implements Serializable{
	 
		
		/**
	 * 
	 */
	private static final long serialVersionUID = 3156862727940849823L;
		@Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;
		private Date createdDate;
	    private String createdBy;
	    private Date lastModifiedDate;
	    private String userGroupKey;
	    private String userKey;
	    private Long assignmentConfigurationId;
	    
}
