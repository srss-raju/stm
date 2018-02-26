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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "sm_product")
public class Product  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3866550129431257035L;
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private String productName;
	private String productKey;
	@ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name = "ingredientId")
	@JsonIgnore
	private Ingredient ingredient;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public String getProductKey() {
		return productKey;
	}
	public void setProductKey(String productKey) {
		this.productKey = productKey;
	}
	
	@ManyToOne
	@JoinColumn(name = "ingredientId")
	public Ingredient getIngredient() {
		return ingredient;
	}
	public void setIngredient(Ingredient ingredient) {
		this.ingredient = ingredient;
	}
	
}
