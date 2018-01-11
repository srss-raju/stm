package com.deloitte.smt.dto;

import java.util.List;

import com.deloitte.smt.util.LevelsDTO;

public class ConditionResponse {

	// private List<Versions> versions;
	private List<String> versions;
	private List<LevelsDTO> levels;
	private boolean showCodes;
	public List<String> getVersions() {
		return versions;
	}
	public void setVersions(List<String> versions) {
		this.versions = versions;
	}
	public List<LevelsDTO> getLevels() {
		return levels;
	}
	public void setLevels(List<LevelsDTO> levels) {
		this.levels = levels;
	}
	public boolean isShowCodes() {
		return showCodes;
	}
	public void setShowCodes(boolean showCodes) {
		this.showCodes = showCodes;
	}
	
	

}
