package com.deloitte.smt.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DetectionRunResponseDTO  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("JOB_ID")
	private Long jobId;
	
	@JsonProperty("STATUS")
	private String jobStatus;
	
	private Date createdDate;
	
	private Long runInstanceId;
	
	private Date runDate;
	
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	

	public Long getRunInstanceId() {
		return runInstanceId;
	}
	public void setRunInstanceId(Long runInstanceId) {
		this.runInstanceId = runInstanceId;
	}
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
	
	public Date getRunDate() {
		return runDate;
	}
	public void setRunDate(Date runDate) {
		this.runDate = runDate;
	}
	@Override
	public String toString() {
		return "DetectionRunResponseDTO [jobId=" + jobId + ", jobStatus="
				+ jobStatus + ", createdDate=" + createdDate
				+ ", runInstanceId=" + runInstanceId + ", runDate=" + runDate
				+ "]";
	}
	
}
