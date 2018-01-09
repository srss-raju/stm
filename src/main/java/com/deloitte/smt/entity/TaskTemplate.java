package com.deloitte.smt.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Entity
@Table(name = "sm_task_template")
public class TaskTemplate {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private String name;
	private String type;
	private Date createdDate;
	private String createdBy;
	
	
	private List<Long> deletedProductIds;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "taskTemplateId")
	private List<TaskTemplateProducts> products;
/*	Taken from RiskTaskTemplate	
 * @JoinColumn(name = "riskTaskTemplateId")
	private List<RiskTaskTemplateProducts> products;*/
	
}
