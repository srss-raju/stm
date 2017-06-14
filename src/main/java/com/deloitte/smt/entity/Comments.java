package com.deloitte.smt.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sm_comments")
public class Comments  implements Serializable {

	private static final long serialVersionUID = -3652484302035277522L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private String createdBy;
	private Date modifiedDate;
	private String userComments;
	private Long assessmentId;
	private Long riskPlanId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public Long getAssessmentId() {
		return assessmentId;
	}
	public void setAssessmentId(Long assessmentId) {
		this.assessmentId = assessmentId;
	}
	public Long getRiskPlanId() {
		return riskPlanId;
	}
	public void setRiskPlanId(Long riskPlanId) {
		this.riskPlanId = riskPlanId;
	}
	public String getUserComments() {
		return userComments;
	}
	public void setUserComments(String userComments) {
		this.userComments = userComments;
	}
}
