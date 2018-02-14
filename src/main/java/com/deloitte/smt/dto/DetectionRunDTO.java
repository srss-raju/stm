package com.deloitte.smt.dto;

import java.io.Serializable;
import java.util.List;

import com.deloitte.smt.entity.Query;
import com.deloitte.smt.entity.Stratification;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DetectionRunDTO  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<Query> queries;
	private List<Stratification> stratifications;
	@JsonProperty("run_instance_id")
	private Long runInstanceId;
	private String primaryProductKey;
	private String primaryEventKey;
	private String primaryProductIngredientKey;
	private String secondaryProductKey;
	private String secondaryEventKey;
	private String secondaryProductIngredientKey;
	private String primaryProductActLevel;
	private String secondaryProductActLevel;
	private String primaryEventLevel;
	private String secondaryEventLevel;
	
	
	public List<Query> getQueries() {
		return queries;
	}
	public void setQueries(List<Query> queries) {
		this.queries = queries;
	}
	public List<Stratification> getStratifications() {
		return stratifications;
	}
	public void setStratifications(List<Stratification> stratifications) {
		this.stratifications = stratifications;
	}
	public Long getRunInstanceId() {
		return runInstanceId;
	}
	public void setRunInstanceId(Long runInstanceId) {
		this.runInstanceId = runInstanceId;
	}
	public String getPrimaryProductKey() {
		return primaryProductKey;
	}
	public void setPrimaryProductKey(String primaryProductKey) {
		this.primaryProductKey = primaryProductKey;
	}
	public String getPrimaryEventKey() {
		return primaryEventKey;
	}
	public void setPrimaryEventKey(String primaryEventKey) {
		this.primaryEventKey = primaryEventKey;
	}
	public String getPrimaryProductIngredientKey() {
		return primaryProductIngredientKey;
	}
	public void setPrimaryProductIngredientKey(String primaryProductIngredientKey) {
		this.primaryProductIngredientKey = primaryProductIngredientKey;
	}
	public String getSecondaryProductKey() {
		return secondaryProductKey;
	}
	public void setSecondaryProductKey(String secondaryProductKey) {
		this.secondaryProductKey = secondaryProductKey;
	}
	public String getSecondaryEventKey() {
		return secondaryEventKey;
	}
	public void setSecondaryEventKey(String secondaryEventKey) {
		this.secondaryEventKey = secondaryEventKey;
	}
	public String getSecondaryProductIngredientKey() {
		return secondaryProductIngredientKey;
	}
	public void setSecondaryProductIngredientKey(
			String secondaryProductIngredientKey) {
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
	
}
