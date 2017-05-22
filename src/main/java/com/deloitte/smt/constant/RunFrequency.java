package com.deloitte.smt.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by myelleswarapu on 06-04-2017.
 */
public enum RunFrequency {
	DAILY("Daily"), WEEKLY("Weekly"), MONTHLY("Monthly"), QUARTERLY("Quarterly"),YEARLY("Yearly");

	private String frequency;

	RunFrequency(String frequency) {
		this.frequency=frequency;
	}

	public String getFrequency() {
		return frequency;
	}

	public static List<String> getAll() {
		List<String> frequencyList = new ArrayList<>();
		for (RunFrequency namedFrequency : RunFrequency.values()) {
			frequencyList.add(namedFrequency.getFrequency());
		}
		return frequencyList;
	}
}
