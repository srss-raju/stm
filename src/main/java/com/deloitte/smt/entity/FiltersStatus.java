package com.deloitte.smt.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "sm_filter_status")
public class FiltersStatus implements Serializable {
	private static final long serialVersionUID = 1602032555828014884L;
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private String key;
	private String name;
	private String description;
	private boolean visible;
	@ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name = "FILTER_ID")
    private Filters filters;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public Filters getFilters() {
		return filters;
	}
	public void setFilters(Filters filters) {
		this.filters = filters;
	}
}
