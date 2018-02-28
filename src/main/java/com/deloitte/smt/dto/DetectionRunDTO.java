package com.deloitte.smt.dto;

import java.io.Serializable;
import java.util.List;

import com.deloitte.smt.entity.Algorithm;
import com.deloitte.smt.entity.Query;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DetectionRunDTO  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<Query> queries;
	private List<String> stratifications;
	@JsonProperty("run_instance_id")
	private Long runInstanceId;
	private List<String> primaryProductKey;
	private List<String> primaryEventKey;
	private List<String> primaryProductIngredientKey;
	private List<String> secondaryProductKey;
	private List<String> secondaryEventKey;
	private List<String> secondaryProductIngredientKey;
	private String primaryProductActLevel;
	private String secondaryProductActLevel;
	private String primaryEventLevel;
	private String secondaryEventLevel;
	private Long minCases;
	@JsonProperty("thresholds_table")
	private List<Algorithm> algorithms;
	
	public List<Query> getQueries() {
		return queries;
	}
	public void setQueries(List<Query> queries) {
		this.queries = queries;
	}
	public List<String> getStratifications() {
		return stratifications;
	}
	public void setStratifications(List<String> stratifications) {
		this.stratifications = stratifications;
	}
	public Long getRunInstanceId() {
		return runInstanceId;
	}
	public void setRunInstanceId(Long runInstanceId) {
		this.runInstanceId = runInstanceId;
	}
	public List<String> getPrimaryProductKey() {
		return primaryProductKey;
	}
	public void setPrimaryProductKey(List<String> primaryProductKey) {
		this.primaryProductKey = primaryProductKey;
	}
	public List<String> getPrimaryEventKey() {
		return primaryEventKey;
	}
	public void setPrimaryEventKey(List<String> primaryEventKey) {
		this.primaryEventKey = primaryEventKey;
	}
	public List<String> getPrimaryProductIngredientKey() {
		return primaryProductIngredientKey;
	}
	public void setPrimaryProductIngredientKey(
			List<String> primaryProductIngredientKey) {
		this.primaryProductIngredientKey = primaryProductIngredientKey;
	}
	public List<String> getSecondaryProductKey() {
		return secondaryProductKey;
	}
	public void setSecondaryProductKey(List<String> secondaryProductKey) {
		this.secondaryProductKey = secondaryProductKey;
	}
	public List<String> getSecondaryEventKey() {
		return secondaryEventKey;
	}
	public void setSecondaryEventKey(List<String> secondaryEventKey) {
		this.secondaryEventKey = secondaryEventKey;
	}
	public List<String> getSecondaryProductIngredientKey() {
		return secondaryProductIngredientKey;
	}
	public void setSecondaryProductIngredientKey(
			List<String> secondaryProductIngredientKey) {
		this.secondaryProductIngredientKey = secondaryProductIngredientKey;
	}
	public String getPrimaryProductActLevel() {
		return primaryProductActLevel;
	}
	public void setPrimaryProductActLevel(String primaryProductActLevel) {
		this.primaryProductActLevel = primaryProductActLevel;
	}
	public String getSecondaryProductActLevel() {
		return secondaryProductActLevel;
	}
	public void setSecondaryProductActLevel(String secondaryProductActLevel) {
		this.secondaryProductActLevel = secondaryProductActLevel;
	}
	public String getPrimaryEventLevel() {
		return primaryEventLevel;
	}
	public void setPrimaryEventLevel(String primaryEventLevel) {
		this.primaryEventLevel = primaryEventLevel;
	}
	public String getSecondaryEventLevel() {
		return secondaryEventLevel;
	}
	public void setSecondaryEventLevel(String secondaryEventLevel) {
		this.secondaryEventLevel = secondaryEventLevel;
	}
	public Long getMinCases() {
		return minCases;
	}
	public void setMinCases(Long minCases) {
		this.minCases = minCases;
	}
	public List<Algorithm> getAlgorithms() {
		return algorithms;
	}
	public void setAlgorithms(List<Algorithm> algorithms) {
		this.algorithms = algorithms;
	}
	
}
