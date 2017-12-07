package com.deloitte.smt.dto;

import java.util.List;

public class ProductResponse {
	
	//private List<Versions> versions;
	//private List<Levels> levels;
	private List<String> levels;
	private List<String> versions;
	private boolean showCodes;
	public List<String> getLevels() {
		return levels;
	}
	public void setLevels(List<String> levels) {
		this.levels = levels;
	}
	public List<String> getVersions() {
		return versions;
	}
	public void setVersions(List<String> versions) {
		this.versions = versions;
	}
	public boolean isShowCodes() {
		return showCodes;
	}
	public void setShowCodes(boolean showCodes) {
		this.showCodes = showCodes;
	}
	
	
	
}
