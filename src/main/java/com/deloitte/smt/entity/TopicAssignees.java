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

@Data
@Entity
@Table(name = "sm_topic_assignees")
public class TopicAssignees implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String userGroupKey;
	private String userKey;
	private Date createdDate;
	private String createdBy;
	private Date lastModifiedDate;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "topicId")
	private Topic topic;
	
	public TopicAssignees(){}
	
	public TopicAssignees(String userGroupKey, String userKey){
		this.userGroupKey = userGroupKey;
		this.userKey = userKey;
	}
}
