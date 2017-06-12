package com.deloitte.smt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ValidationOutComesDTO {
	
	private String label;
	
	@JsonProperty("value")
	private Long count;
	private String color;
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	

}
