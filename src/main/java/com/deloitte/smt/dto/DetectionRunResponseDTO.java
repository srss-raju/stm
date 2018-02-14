package com.deloitte.smt.dto;

import java.io.Serializable;
import java.util.Date;

public class DetectionRunResponseDTO  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long jobId;
	
	private Long runInstanceId;
	
	private Date createdDate;
	
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
}
