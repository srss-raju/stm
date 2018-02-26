package com.deloitte.smt.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "sm_signal_algorithm")
public class Algorithm  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String thresholdName;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy="algorithm", cascade = CascadeType.ALL)
    private Set<AlgorithmStatistics> thresholdValues;
	
}
