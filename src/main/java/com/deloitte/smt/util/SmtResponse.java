package com.deloitte.smt.util;

import java.util.List;

/**
 * 
 * @author rbongurala
 *
 */
public class SmtResponse {
	
	private List<?> result;
	private String message;
	private int totalRecords;
	private int fetchSize;
	private int fromRecord;
	
	
	public List<?> getResult() {
		return result;
	}
	public void setResult(List<?> result) {
		this.result = result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}
	public int getFetchSize() {
		return fetchSize;
	}
	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}
	public int getFromRecord() {
		return fromRecord;
	}
	public void setFromRecord(int fromRecord) {
		this.fromRecord = fromRecord;
	}
	
}
