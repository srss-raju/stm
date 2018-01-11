package com.deloitte.smt.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "sm_strength_of_evidence")
public class StrengthOfEvidence implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1370072173437651674L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private String strengthValue;
	private String strengthKey;
	
}
