package com.deloitte.smt.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name = "sm_signal_statistics")
public class SignalStatistics  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String algorithm;
	private double score;
	private double se;
	private double lb;
	private double ub;
	private String socName;
	private String ptName;
	private Long runInstanceId;
	@JsonIgnore
	@ManyToOne
	private Topic topic;

}
