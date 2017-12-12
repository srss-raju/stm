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


@Entity
@Table(name = "sm_filters")
public class Filters implements Serializable {
	private static final long serialVersionUID = 1602032555828014884L;
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long filterId;
	private String type;
	private String key;
	private String name;
	private String description;
	private boolean visible;
	
	@OneToMany(fetch = FetchType.EAGER,mappedBy="filters",cascade = CascadeType.ALL)
    private Set<FiltersStatus> filtersStatus;

	
	public Long getFilterId() {
		return filterId;
	}
	public void setFilterId(Long filterId) {
		this.filterId = filterId;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public Set<FiltersStatus> getFiltersStatus() {
		return filtersStatus;
	}
	public void setFiltersStatus(Set<FiltersStatus> filtersStatus) {
		this.filtersStatus = filtersStatus;
	}
}
