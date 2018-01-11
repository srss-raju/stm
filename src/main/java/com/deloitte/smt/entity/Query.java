package com.deloitte.smt.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "sm_detection_query")
public class Query  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@JsonProperty("QUERY_KEY")
	private Long queryKey;
	@JsonProperty("QUERY_NAME")
	private String queryName;
	
	
	@ManyToOne
	@JoinColumn(name = "detectionId")
	@JsonIgnore
	private SignalDetection signalDetection;
}
