package com.deloitte.smt.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "sm_ingredient")
public class Ingredient  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8213868399732487172L;
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private String ingredientName;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredientId")
	private List<Product> products;
	
	@ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name = "topicId")
	@JsonIgnore
	private Topic topic;
	
	
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
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
}

