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
	private Long runInstanceId;
	@JsonProperty("PRODUCT_KEY")
	private String productKey;
	@JsonProperty("PRODUCT_KEY2")
	private String productKey2;
	@JsonProperty("EVENT_KEY")
	private String eventKey;
	@JsonProperty("EVENT_KEY2")
	private String eventKey2;
	
	
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
	public String getProductKey() {
		return productKey;
	}
	public void setProductKey(String productKey) {
		this.productKey = productKey;
	}
	public String getProductKey2() {
		return productKey2;
	}
	public void setProductKey2(String productKey2) {
		this.productKey2 = productKey2;
	}
	public String getEventKey() {
		return eventKey;
	}
	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}
	public String getEventKey2() {
		return eventKey2;
	}
	public void setEventKey2(String eventKey2) {
		this.eventKey2 = eventKey2;
	}
	
}
