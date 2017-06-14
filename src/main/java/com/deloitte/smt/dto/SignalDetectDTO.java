package com.deloitte.smt.dto;

import java.sql.Timestamp;

public class SignalDetectDTO {
	
	private Timestamp month;
	private Long casesCount;
	private Long signalCount;
	private Long recurringCount;
	private Long totalSignalCount;
	
	public Timestamp getMonth() {
		return month;
	}
	public void setMonth(Timestamp month) {
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
	public Long getRecurringCount() {
		return recurringCount;
	}
	public void setRecurringCount(Long recurringCount) {
		this.recurringCount = recurringCount;
	}
	
}