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
@Table(name = "sm_topic_soc_assignment_product")
public class TopicAssignmentProduct implements Serializable {

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
    private String category;
    private String categoryCode;
    private String categoryDesc;
    private String categoryName;
    private Long assignmentConfigurationId;
    private Long topicProductAssignmentConfigurationId;
    
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
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getCategoryDesc() {
		return categoryDesc;
	}
	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}
	public Long getAssignmentConfigurationId() {
		return assignmentConfigurationId;
	}
	public void setAssignmentConfigurationId(Long assignmentConfigurationId) {
		this.assignmentConfigurationId = assignmentConfigurationId;
	}
	public Long getTopicProductAssignmentConfigurationId() {
		return topicProductAssignmentConfigurationId;
	}
	public void setTopicProductAssignmentConfigurationId(
			Long topicProductAssignmentConfigurationId) {
		this.topicProductAssignmentConfigurationId = topicProductAssignmentConfigurationId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	
}
