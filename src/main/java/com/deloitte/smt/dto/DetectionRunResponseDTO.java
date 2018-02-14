package com.deloitte.smt.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DetectionRunResponseDTO  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("JOB_ID")
	private Long jobId;
	
	@JsonProperty("STATUS")
	private String jobStatus;

	public Long getJobId() {
		return jobId;
	}
	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}
	public String getJobStatus() {
		return jobStatus;
	}
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
	
}
