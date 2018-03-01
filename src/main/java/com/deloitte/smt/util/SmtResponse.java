package com.deloitte.smt.util;

import java.util.List;

import lombok.Data;

/**
 * 
 * @author rbongurala
 *
 */
@Data
public class SmtResponse {
	
	private List<?> result;
	private String message;
	private int totalRecords;
	private int fetchSize;
	private int fromRecord;
	
}
