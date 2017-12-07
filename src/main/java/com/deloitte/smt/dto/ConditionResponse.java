package com.deloitte.smt.dto;

import java.util.List;

import com.deloitte.smt.util.Levels;

public class ConditionResponse {

	// private List<Versions> versions;
	private List<String> versions;
	private List<Levels> levels;
	private boolean showCodes;
	public List<String> getVersions() {
		return versions;
	}
	public void setVersions(List<String> versions) {
		this.versions = versions;
	}
	public List<Levels> getLevels() {
		return levels;
	}
	public void setLevels(List<Levels> levels) {
		this.levels = levels;
	}
	public boolean isShowCodes() {
		return showCodes;
	}
	public void setShowCodes(boolean showCodes) {
		this.showCodes = showCodes;
	}
	
	

}
