package com.deloitte.smt.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Rajesh on 31-07-2017.
 */
@Entity
@Table(name = "sm_topic_signal_detection_assignees")
public class TopicSignalDetectionAssignmentAssignees {
	
		public TopicSignalDetectionAssignmentAssignees(){
			
		}
		
		
		@Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;
		private Date createdDate;
	    private String createdBy;
	    private Date lastModifiedDate;
	    private String assignTo;
	    private String userGroupKey;
	    private String userKey;
	    
	    private Long detectionId;
	    @Transient
		@JsonIgnore
	    private SignalDetection signalDetection;
	    
	    @ManyToOne
		@JoinColumn(name = "detectionId")
		public SignalDetection getSignalDetection() {
			return signalDetection;
		}
		public void setSignalDetection(SignalDetection signalDetection) {
			this.signalDetection = signalDetection;
		}
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
		public Long getDetectionId() {
			return detectionId;
		}
		public void setDetectionId(Long detectionId) {
			this.detectionId = detectionId;
		}
		public String getUserGroupKey() {
			return userGroupKey;
		}
		public void setUserGroupKey(String userGroupKey) {
			this.userGroupKey = userGroupKey;
		}
		public String getUserKey() {
			return userKey;
		}
		public void setUserKey(String userKey) {
			this.userKey = userKey;
		}
		
		public TopicSignalDetectionAssignmentAssignees( String userGroupKey,  String userKey){
			this.userKey = userKey;
			this.userGroupKey = userGroupKey;
		}
		
}
