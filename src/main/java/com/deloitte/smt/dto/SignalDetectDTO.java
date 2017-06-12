package com.deloitte.smt.dto;

public class SignalDetectDTO {
	
	private String month;
	private Long casesCount;
	private Long signalCount;
	private Long totalSignalCount;
	private Long totalCasesCount;
	private String status;
	
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public Long getCasesCount() {
		return casesCount;
	}
	public void setCasesCount(Long casesCount) {
		this.casesCount = casesCount;
	}
	public Long getSignalCount() {
		return signalCount;
	}
	public void setSignalCount(Long signalCount) {
		this.signalCount = signalCount;
	}
	public Long getTotalSignalCount() {
		return totalSignalCount;
	}
	public void setTotalSignalCount(Long totalSignalCount) {
		this.totalSignalCount = totalSignalCount;
	}
	public Long getTotalCasesCount() {
		return totalCasesCount;
	}
	public void setTotalCasesCount(Long totalCasesCount) {
		this.totalCasesCount = totalCasesCount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}