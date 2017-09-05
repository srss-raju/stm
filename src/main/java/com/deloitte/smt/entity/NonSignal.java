package com.deloitte.smt.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by cavula
 */
@Entity
@Table(name = "sm_non_signal")
public class NonSignal implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	private String name;
    private String description;
    private Long runInstanceId;
	private String cases;
	private String caselistId;
	private Date createdDate;
	private String createdBy;
	private Date lastModifiedDate;
	private Integer casesCount;
	private String productKey;
	private String ptDesc;
	
	public String getPtDesc() {
		return ptDesc;
	}
	public void setPtDesc(String ptDesc) {
		this.ptDesc = ptDesc;
	}
	public String getProductKey() {
		return productKey;
	}
	public void setProductKey(String productKey) {
		this.productKey = productKey;
	}
	
	
    public Integer getCasesCount() {
		return casesCount;
	}
	public void setCasesCount(Integer casesCount) {
		this.casesCount = casesCount;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getRunInstanceId() {
		return runInstanceId;
	}
	public void setRunInstanceId(Long runInstanceId) {
		this.runInstanceId = runInstanceId;
	}
	public String getCases() {
		return cases;
	}
	public void setCases(String cases) {
		this.cases = cases;
	}
	public String getCaselistId() {
		return caselistId;
	}
	public void setCaselistId(String caselistId) {
		this.caselistId = caselistId;
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
	
}
