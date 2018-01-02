package com.deloitte.smt.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

/**
 * Created by rkb on 09-11-2017.
 */
@Data
@Entity
@Table(name = "sm_topic_soc_assignment_configuration")
public class TopicSocAssignmentConfiguration implements Serializable {
	private static final long serialVersionUID = 274553099392305910L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date createdDate;
    private String createdBy;
    private Date lastModifiedDate;
    private String lastModifiedBy;
    private String recordKey;
    private String conditionName;
    private Long assignmentConfigurationId;
    @ManyToOne
    private Topic topic;
    private Long detectionId;
    @Transient
    private List<TopicAssignmentCondition> recordValues;
   	
}
