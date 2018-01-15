package com.deloitte.smt.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

/**
 * Created by rkb on 09-11-2017.
 */
@Data
@Entity
@Table(name = "sm_topic_product")
public class TopicProduct implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 274553099392305910L;
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String productName;
    private String recordKey;
    private Long assignmentConfigurationId;
    private Long topicId;
    private Long detectionId;
    
    @OneToMany
    private List<TopicProductValues> recordValues;

    private Date createdDate;
    private String createdBy;
    private Date lastModifiedDate;
    private String lastModifiedBy;
    
}
