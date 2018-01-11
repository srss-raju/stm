package com.deloitte.smt.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * Created by rkb on 17-12-2017.
 */
@Data
@Entity
@Table(name = "sm_task_template_product_values")
public class TaskTemplateProductValues implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 274553099392305910L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String category;
    private String categoryCode;
    private String categoryDesc;
    private String categoryName;
    private Date createdDate;
    private String createdBy;
    private Date lastModifiedDate;
    
	@JsonIgnore
	private TaskTemplateProducts taskTemplateProducts;
    
}
