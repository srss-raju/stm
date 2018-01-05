package com.deloitte.smt.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "sm_detection_run")
public class DetectionRun {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private String detectionName;
	private Long detectionId;
	private Date runDate;
	private Date createdDate;
    private String createdBy;
    private Date lastModifiedDate;
    private String message;
    private String jobId;
    private String jobStatus;
    
}
