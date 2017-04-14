package com.deloitte.smt.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "sm_ingredient")
public class Ingredient  implements Serializable{
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private String ingredientName;
	private Long topicId;
	
	@Transient
    private List<Product> products;
	
	@Transient
    private List<License> licenses;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getIngredientName() {
		return ingredientName;
	}
	public void setIngredientName(String ingredientName) {
		this.ingredientName = ingredientName;
	}
	public Long getTopicId() {
		return topicId;
	}
	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	public List<License> getLicenses() {
		return licenses;
	}
	public void setLicenses(List<License> licenses) {
		this.licenses = licenses;
	}
	
}
