package com.deloitte.smt.dto;

import java.util.List;

import com.deloitte.smt.entity.TopicProductAssignmentConfiguration;
import com.deloitte.smt.entity.TopicSocAssignmentConfiguration;

public class ProductEventDTO {
	
	private List<TopicSocAssignmentConfiguration> conditions;
	private List<TopicProductAssignmentConfiguration> products;
	
	public List<TopicSocAssignmentConfiguration> getConditions() {
		return conditions;
	}
	public void setConditions(List<TopicSocAssignmentConfiguration> conditions) {
		this.conditions = conditions;
	}
	public List<TopicProductAssignmentConfiguration> getProducts() {
		return products;
	}
	public void setProducts(List<TopicProductAssignmentConfiguration> products) {
		this.products = products;
	}
	
}
