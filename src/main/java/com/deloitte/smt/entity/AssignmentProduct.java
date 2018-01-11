package com.deloitte.smt.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

/**
 * Created by rkb on 09-11-2017.
 */
@Data
@Entity
@Table(name = "sm_assignment_product")
public class AssignmentProduct implements Serializable {

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
    private String productName;
    private String recordKey;
    private Date createdDate;
    private String createdBy;
    private Date lastModifiedDate;
	private String lastModifiedBy;
        
    @OneToMany
    private List<AssignmentProductValues> recordValues;
    
}
