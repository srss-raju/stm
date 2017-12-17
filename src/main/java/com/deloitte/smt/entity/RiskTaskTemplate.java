package com.deloitte.smt.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "sm_risk_task_template")
public class RiskTaskTemplate {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private String name;
	private Date createdDate;
	private String createdBy;
	@Transient
	private List<Long> deletedIngrediantIds;
	@Transient
	private List<Long> deletedProductIds;
	@Transient
	private List<TaskTemplateIngrediant> taskTemplateIngrediant;
	private Long inDays;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "riskTaskTemplateId")
	private List<RiskTaskTemplateProducts> products;

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
	public List<TaskTemplateIngrediant> getTaskTemplateIngrediant() {
		return taskTemplateIngrediant;
	}
	public void setTaskTemplateIngrediant(
			List<TaskTemplateIngrediant> taskTemplateIngrediant) {
		this.taskTemplateIngrediant = taskTemplateIngrediant;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public List<Long> getDeletedIngrediantIds() {
		return deletedIngrediantIds;
	}
	public void setDeletedIngrediantIds(List<Long> deletedIngrediantIds) {
		this.deletedIngrediantIds = deletedIngrediantIds;
	}
	public Long getInDays() {
		return inDays;
	}
	public void setInDays(Long inDays) {
		this.inDays = inDays;
	}
	public List<Long> getDeletedProductIds() {
		return deletedProductIds;
	}
	public void setDeletedProductIds(List<Long> deletedProductIds) {
		this.deletedProductIds = deletedProductIds;
	}
	public List<RiskTaskTemplateProducts> getProducts() {
		return products;
	}
	public void setProducts(List<RiskTaskTemplateProducts> products) {
		this.products = products;
	}
	
}
