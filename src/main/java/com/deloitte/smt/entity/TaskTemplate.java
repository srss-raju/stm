package com.deloitte.smt.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "sm_task_template")
public class TaskTemplate {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private String name;
	private String ingrediantName;
	@Transient
	private List<String> ingrediantNames;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIngrediantName() {
		return ingrediantName;
	}
	public void setIngrediantName(String ingrediantName) {
		this.ingrediantName = ingrediantName;
	}
	public List<String> getIngrediantNames() {
		return ingrediantNames;
	}
	public void setIngrediantNames(List<String> ingrediantNames) {
		this.ingrediantNames = ingrediantNames;
	}
}
