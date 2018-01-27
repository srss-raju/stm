package com.deloitte.smt.dto;

import java.util.List;

import lombok.Data;

@Data
public class SearchCriteriaDTO {
	private List<FilterDTO> filters;
	private int fetchSize;
	private int fromRecord;
	private int totalRecords;
	private String sortBy;
	private String orderBy;
}
