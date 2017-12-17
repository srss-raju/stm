package com.deloitte.smt.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sm_task_template_ingrediant")
public class TaskTemplateIngrediant {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private String name;
	private Long taskTemplateId;

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
	public Long getTaskTemplateId() {
		return taskTemplateId;
	}
	public void setTaskTemplateId(Long taskTemplateId) {
		this.taskTemplateId = taskTemplateId;
	}
}
