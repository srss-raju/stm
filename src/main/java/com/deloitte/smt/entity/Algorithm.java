package com.deloitte.smt.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Entity
@Table(name = "sm_signal_algorithm")
public class Algorithm  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private int id;
	
	@JsonProperty("THRESHOLD_KEY")
	private String thresholdKey;
	
	@JsonProperty("THRESHOLD_VALUE")
	private int thresholdValue;
	
	@JsonProperty("THRESHOLD_NAME")
	private String thresholdName;
	
}
