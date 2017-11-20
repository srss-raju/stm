package com.deloitte.smt.dto;

import java.util.List;

import com.deloitte.smt.util.Levels;

public class ConditionResponse {
	
	private Versions versions;
	
	private List<Levels> levels;

	public Versions getVersions() {
		return versions;
	}

	public void setVersions(Versions versions) {
		this.versions = versions;
	}

	public List<Levels> getLevels() {
		return levels;
	}

	public void setLevels(List<Levels> levels) {
		this.levels = levels;
	}


	
}
