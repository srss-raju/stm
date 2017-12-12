package com.deloitte.smt.dto;

import java.util.List;

public class FilterDTO {
private String filterKey;
private String filterName;
private List<?> filterValues;
public String getFilterKey() {
	return filterKey;
}
public void setFilterKey(String filterKey) {
	this.filterKey = filterKey;
}
public String getFilterName() {
	return filterName;
}
public void setFilterName(String filterName) {
	this.filterName = filterName;
}
public List<?> getFilterValues() {
	return filterValues;
}
public void setFilterValues(List<?> filterValues) {
	this.filterValues = filterValues;
}



}