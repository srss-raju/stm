package com.deloitte.smt.dto;

import java.io.Serializable;

public class PtDTO implements Serializable{
	
	private static final long serialVersionUID = 4387219880273434223L;
	private int ptId;
	private String ptName;
	private int smqId;
	
	public int getPtId() {
		return ptId;
	}
	public void setPtId(int ptId) {
		this.ptId = ptId;
	}
	public String getPtName() {
		return ptName;
	}
	public void setPtName(String ptName) {
		this.ptName = ptName;
	}
	public int getSmqId() {
		return smqId;
	}
	public void setSmqId(int smqId) {
		this.smqId = smqId;
	}
}
