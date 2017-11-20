package com.deloitte.smt.dto;

import java.util.List;

public class ConditionsDto {
	
	private List<String> versionNumber;
	
	private List<String> levels;

	public List<String> getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(List<String> versionNumber) {
		this.versionNumber = versionNumber;
	}

	public List<String> getLevels() {
		return levels;
	}

	public void setLevels(List<String> levels) {
		this.levels = levels;
	}
	

}
