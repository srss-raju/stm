package com.deloitte.smt.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by rkb on 09-11-2017.
 */
@Entity
@Table(name = "sm_product_assignment_configuration")
public class ProductAssignmentConfiguration implements Serializable {

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
    private String productName;
    private String recordKey;
    private Long assignmentConfigurationId;
    @Transient
    private List<AssignmentProduct> recordValues;
    
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
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public Long getAssignmentConfigurationId() {
		return assignmentConfigurationId;
	}
	public void setAssignmentConfigurationId(Long assignmentConfigurationId) {
		this.assignmentConfigurationId = assignmentConfigurationId;
	}
	public String getRecordKey() {
		return recordKey;
	}
	public void setRecordKey(String recordKey) {
		this.recordKey = recordKey;
	}
	public List<AssignmentProduct> getRecordValues() {
		return recordValues;
	}
	public void setRecordValues(List<AssignmentProduct> recordValues) {
		this.recordValues = recordValues;
	}
	
}
