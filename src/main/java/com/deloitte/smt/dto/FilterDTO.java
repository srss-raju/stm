package com.deloitte.smt.dto;

import java.util.List;

import lombok.Data;

@Data
public class FilterDTO {
	private String filterKey;
	private String filterName;
	private List<?> filterValues;
	private String filterType="";
}