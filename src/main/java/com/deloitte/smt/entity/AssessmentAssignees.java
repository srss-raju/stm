package com.deloitte.smt.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * Created by Rajesh on 31-07-2017.
 */
@Data
@Entity
@Table(name = "sm_assessment_assignees")
public class AssessmentAssignees implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = -6586959436931561167L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Date createdDate;
	private String createdBy;
	private Date lastModifiedDate;
	private String userGroupKey;
	private String userKey;
	
	@ManyToOne
	@JoinColumn(name = "assessmentPlanId")
	@JsonIgnore
	private AssessmentPlan assessmentPlan;
}
