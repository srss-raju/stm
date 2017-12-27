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

import lombok.Data;

/**
 * Created by Rajesh on 31-07-2017.

 * Updated by 
 * @author jshaik
 * on 27-12-2017
 */
@Data
@Entity
@Table(name = "sm_topic_riskplan_assignment_assignees")
public class TopicRiskPlanAssignmentAssignees implements Serializable{
	private static final long serialVersionUID = 9119772529109562840L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Date createdDate;
	private String createdBy;
	private Date lastModifiedDate;
	private String assignTo;
	private Long userGroupKey;
	private Long userKey;
	@ManyToOne
	@JoinColumn(name = "riskId")
	private RiskPlan riskId;


	public TopicRiskPlanAssignmentAssignees(){
		
	}
	
	public TopicRiskPlanAssignmentAssignees( Long userGroupKey,  Long userKey){
		this.userKey = userKey;
		this.userGroupKey = userGroupKey;
	}
}
