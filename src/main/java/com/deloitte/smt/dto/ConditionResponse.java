package com.deloitte.smt.dto;

import java.util.List;

import com.deloitte.smt.util.Levels;

public class ConditionResponse {
	
	private List<Versions> versions;
	
	private List<Levels> levels;

	
	public List<Levels> getLevels() {
		return levels;
	}

	public void setLevels(List<Levels> levels) {
		this.levels = levels;
	}

	public List<Versions> getVersions() {
		return versions;
	}

	public void setVersions(List<Versions> versions) {
		this.versions = versions;
	}


	
}
