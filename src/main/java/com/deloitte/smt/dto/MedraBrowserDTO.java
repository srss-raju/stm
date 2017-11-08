package com.deloitte.smt.dto;

import java.util.List;

import com.deloitte.smt.entity.Hlgt;
import com.deloitte.smt.entity.Hlt;
import com.deloitte.smt.entity.Llt;
import com.deloitte.smt.entity.Pt;
import com.deloitte.smt.entity.Soc;

/**
 * 
 * @author shbondada
 *
 */
public class MedraBrowserDTO {
	
	
	List<String> socs;
	List<String> hlgts;
	List<String> hlts;
	List<String> pts;
	List<String> llts;
	public List<String> getSocs() {
		return socs;
	}
	public void setSocs(List<String> socs) {
		this.socs = socs;
	}
	public List<String> getHlgts() {
		return hlgts;
	}
	public void setHlgts(List<String> hlgts) {
		this.hlgts = hlgts;
	}
	public List<String> getHlts() {
		return hlts;
	}
	public void setHlts(List<String> hlts) {
		this.hlts = hlts;
	}
	public List<String> getPts() {
		return pts;
	}
	public void setPts(List<String> pts) {
		this.pts = pts;
	}
	public List<String> getLlts() {
		return llts;
	}
	public void setLlts(List<String> llts) {
		this.llts = llts;
	}
	
	
	
	

}
