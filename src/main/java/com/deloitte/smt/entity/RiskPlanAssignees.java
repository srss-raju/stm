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
@Table(name = "sm_riskplan_assignees")
public class RiskPlanAssignees implements Serializable{
	private static final long serialVersionUID = 9119772529109562840L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String userGroupKey;
	private String userKey;
	@ManyToOne
	@JoinColumn(name = "riskId")
	private RiskPlan riskId;

	private Date createdDate;
	private String createdBy;
	private Date lastModifiedDate;
	
	public RiskPlanAssignees(String userGroupKey, String userKey) {
		this.userGroupKey = userGroupKey;
		this.userKey = userKey;
	}

}
