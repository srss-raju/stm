package com.deloitte.smt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sm_condition_level")
public class ConditionLevels implements Serializable {

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public String getVersions() {
		return versions;
	}

	public void setVersions(String versions) {
		this.versions = versions;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isShowCodes() {
		return showCodes;
	}

	public void setShowCodes(boolean showCodes) {
		this.showCodes = showCodes;
	}

	
	
	

}
