package com.deloitte.smt.entity;

import java.io.Serializable;
import java.util.List;

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
@Table(name = "sm_filters")
public class Filters implements Serializable {
	private static final long serialVersionUID = 1602032555828014884L;
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long filterId;
	private String key;
	private String name;
	private String description;
	private String type;
	private boolean visible;
	
	@OneToMany(fetch = FetchType.EAGER,mappedBy="filters",cascade = CascadeType.ALL)
    private List<FiltersStatus> filtersStatus;
}
