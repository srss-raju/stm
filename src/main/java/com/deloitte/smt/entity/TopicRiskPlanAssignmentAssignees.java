package com.deloitte.smt.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Rajesh on 31-07-2017.
 */
@Entity
@Table(name = "sm_topic_riskplan_assignment_assignees")
public class TopicRiskPlanAssignmentAssignees {
	 
		@Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;
		private Date createdDate;
	    private String createdBy;
	    private Date lastModifiedDate;
	    private String assignTo;
	    private Long userGroupKey;
	    private Long userKey;
	    
	    private Long riskId;
	    
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public Date getCreatedDate() {
			return createdDate;
		}
		public void setCreatedDate(Date createdDate) {
			this.createdDate = createdDate;
		}
		public String getCreatedBy() {
			return createdBy;
		}
		public void setCreatedBy(String createdBy) {
			this.createdBy = createdBy;
		}
		public Date getLastModifiedDate() {
			return lastModifiedDate;
		}
		public void setLastModifiedDate(Date lastModifiedDate) {
			this.lastModifiedDate = lastModifiedDate;
		}
		public String getAssignTo() {
			return assignTo;
		}
		public void setAssignTo(String assignTo) {
			this.assignTo = assignTo;
		}
		public Long getRiskId() {
			return riskId;
		}
		public void setRiskId(Long riskId) {
			this.riskId = riskId;
		}
		public Long getUserGroupKey() {
			return userGroupKey;
		}
		public void setUserGroupKey(Long userGroupKey) {
			this.userGroupKey = userGroupKey;
		}
		public Long getUserKey() {
			return userKey;
		}
		public void setUserKey(Long userKey) {
			this.userKey = userKey;
		}
}
