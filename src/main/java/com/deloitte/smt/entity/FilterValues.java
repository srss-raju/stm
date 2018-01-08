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

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "sm_filter_status")
public class FilterValues implements Serializable {
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
}
