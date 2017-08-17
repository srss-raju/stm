package com.deloitte.smt.dto;

import java.util.Date;

public class SmqDTO {
	
	private String smqName;
	private int smqId;
	private Date validStartDate;
	private Date validEndDate;
	
	public String getSmqName() {
		return smqName;
	}
	public void setSmqName(String smqName) {
		this.smqName = smqName;
	}
	public int getSmqId() {
		return smqId;
	}
	public void setSmqId(int smqId) {
		this.smqId = smqId;
	}
	public Date getValidStartDate() {
		return validStartDate;
	}
	public void setValidStartDate(Date validStartDate) {
		this.validStartDate = validStartDate;
	}
	public Date getValidEndDate() {
		return validEndDate;
	}
	public void setValidEndDate(Date validEndDate) {
		this.validEndDate = validEndDate;
	}
}
