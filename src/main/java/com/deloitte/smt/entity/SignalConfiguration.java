package com.deloitte.smt.entity;


import java.io.Serializable;

import javax.persistence.Column;
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
@Table(name = "sm_signal_configuration")
public class SignalConfiguration implements Serializable {
  
	/**
	 * 
	 */
	private static final long serialVersionUID = -4862256768995870660L;
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private Integer cohortPercentage;
	private Integer confidenceIndex;
	
	@Column(unique=true)
	private String configName;
	
  }
