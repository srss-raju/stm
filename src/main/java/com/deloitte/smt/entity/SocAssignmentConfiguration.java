package com.deloitte.smt.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by rkb on 09-11-2017.
 */
@Entity
@Table(name = "sm_soc_assignment_configuration")
public class SocAssignmentConfiguration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 274553099392305910L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date createdDate;
    private String createdBy;
    private Date lastModifiedDate;
    private String lastModifiedBy;
    private String socName;
    private String hlgtName;
    private String hltName;
    private String ptName;
    private String lltName;
    private Long assignmentConfigurationId;
    
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
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	public String getSocName() {
		return socName;
	}
	public void setSocName(String socName) {
		this.socName = socName;
	}
	public String getHlgtName() {
		return hlgtName;
	}
	public void setHlgtName(String hlgtName) {
		this.hlgtName = hlgtName;
	}
	public String getHltName() {
		return hltName;
	}
	public void setHltName(String hltName) {
		this.hltName = hltName;
	}
	public String getPtName() {
		return ptName;
	}
	public void setPtName(String ptName) {
		this.ptName = ptName;
	}
	public String getLltName() {
		return lltName;
	}
	public void setLltName(String lltName) {
		this.lltName = lltName;
	}
	public Long getAssignmentConfigurationId() {
		return assignmentConfigurationId;
	}
	public void setAssignmentConfigurationId(Long assignmentConfigurationId) {
		this.assignmentConfigurationId = assignmentConfigurationId;
	}
    
}
