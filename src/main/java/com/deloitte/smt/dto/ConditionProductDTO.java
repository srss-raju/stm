package com.deloitte.smt.dto;

import java.io.Serializable;
import java.util.List;

import com.deloitte.smt.entity.ProductAssignmentConfiguration;
import com.deloitte.smt.entity.SocAssignmentConfiguration;

public class ConditionProductDTO implements Serializable{

	private static final long serialVersionUID = -7910268136658523849L;
	
	private List<SocAssignmentConfiguration> conditions;
	
	private List<ProductAssignmentConfiguration> products;

	public List<SocAssignmentConfiguration> getConditions() {
		return conditions;
	}

	public void setConditions(List<SocAssignmentConfiguration> conditions) {
		this.conditions = conditions;
	}

	public List<ProductAssignmentConfiguration> getProducts() {
		return products;
	}

	public void setProducts(List<ProductAssignmentConfiguration> products) {
		this.products = products;
	}
}
