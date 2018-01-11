package com.deloitte.smt.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * Created by rkb on 09-11-2017.
 */
@Data
@Entity
@Table(name = "sm_topic_condition_values")
public class TopicConditionValues implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 274553099392305910L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String category;
    private String categoryCode;
    private String categoryDesc;
    private String categoryName;
    private Long assignmentConfigurationId;
    private Long topicSocAssignmentConfigurationId;
    private Date createdDate;
    private String createdBy;
    private Date lastModifiedDate;
    
}
