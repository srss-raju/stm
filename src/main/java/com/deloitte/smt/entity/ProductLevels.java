package com.deloitte.smt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "sm_product_level")
public class ProductLevels implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5681759063819490433L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	private String versions;
	
	private String key;
	
	private String value;
	@Column(nullable = false, columnDefinition = "boolean default false")
	private boolean showCodes;

}
