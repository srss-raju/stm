package com.deloitte.smt.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "sm_soc")
public class Soc implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private String socName;
	private Long topicId;
	private Long detectionId;
	private String hanaSocId;
	private boolean hlgtSelected;
	private boolean hltSelected;
	private boolean ptSelected;
	
	@OneToMany
	@JoinColumn(name="socId")
    private List<Pt> pts;
	

}
