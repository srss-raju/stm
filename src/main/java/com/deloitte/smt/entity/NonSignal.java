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
 * Created by cavula
 */
@Data
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
	
}
