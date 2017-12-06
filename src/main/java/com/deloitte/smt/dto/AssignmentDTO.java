package com.deloitte.smt.dto;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AssignmentDTO  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6678141117765723101L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private Long assignmentId;
	private Long pid;
	private Long cid;
	private String assignmentName;
	private String crecordkey;
	private String precordkey;
	public Long getAssignmentId() {
		return assignmentId;
	}
	public void setAssignmentId(Long assignmentId) {
		this.assignmentId = assignmentId;
	}
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	public Long getCid() {
		return cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
	public String getAssignmentName() {
		return assignmentName;
	}
	public void setAssignmentName(String assignmentName) {
		this.assignmentName = assignmentName;
	}
	public String getCrecordkey() {
		return crecordkey;
	}
	public void setCrecordkey(String crecordkey) {
		this.crecordkey = crecordkey;
	}
	public String getPrecordkey() {
		return precordkey;
	}
	public void setPrecordkey(String precordkey) {
		this.precordkey = precordkey;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

}
