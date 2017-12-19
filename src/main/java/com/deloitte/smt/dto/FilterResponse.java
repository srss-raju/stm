package com.deloitte.smt.dto;

import java.util.List;

import lombok.Data;

/**
 * 
 * @author jshaik
 *
 */
@Data
public class FilterResponse {
	private List<FilterDataObject> result;
	private int totalRecords;
	private int fetchSize;
	private int fromRecord;
}
