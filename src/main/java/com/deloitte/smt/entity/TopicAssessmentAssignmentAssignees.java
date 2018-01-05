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
import javax.persistence.Transient;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Rajesh on 31-07-2017.
 */
@Data
@Entity
@Table(name = "sm_topic_assessment_assignment_assignees")
public class TopicAssessmentAssignmentAssignees implements Serializable{
	 
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
	    private String assignTo;
	    private Long userGroupKey;
	    private Long userKey;
	    
	    private Long assessmentId;
	    public TopicAssessmentAssignmentAssignees(){
			
		}
		
		public TopicAssessmentAssignmentAssignees( Long userGroupKey,  Long userKey){
			this.userKey = userKey;
			this.userGroupKey = userGroupKey;
		}
	    @Transient
	    @JsonIgnore
	    private AssessmentPlan assessmentPlan;
	    
	    @ManyToOne
		@JoinColumn(name = "assessmentId")
	    public AssessmentPlan getAssessmentPlan() {
			return assessmentPlan;
		}
		public void setAssessmentPlan(AssessmentPlan assessmentPlan) {
			this.assessmentPlan = assessmentPlan;
		}
	    
		
}
