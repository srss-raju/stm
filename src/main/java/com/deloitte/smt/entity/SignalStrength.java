package com.deloitte.smt.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "sm_signal_strength")
public class SignalStrength implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1370072173437651674L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	private String strength;
	private Date modifiedDate;
	private String createdBy;
	private String modifiedBy;
	private Date createdDate;
	
	@ManyToOne
	@JoinColumn(name = "topicId")
	@JsonIgnore
	private Topic topic;
	
}
