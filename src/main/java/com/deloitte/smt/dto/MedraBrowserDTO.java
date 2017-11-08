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
	
	
	List<Soc> socs;
	List<Hlgt> hlgts;
	List<Hlt> hlts;
	List<Pt> pts;
	List<Llt> llts;
	
	public List<Soc> getSocs() {
		return socs;
	}
	public void setSocs(List<Soc> socs) {
		this.socs = socs;
	}
	public List<Hlgt> getHlgts() {
		return hlgts;
	}
	public void setHlgts(List<Hlgt> hlgts) {
		this.hlgts = hlgts;
	}
	public List<Hlt> getHlts() {
		return hlts;
	}
	public void setHlts(List<Hlt> hlts) {
		this.hlts = hlts;
	}
	public List<Pt> getPts() {
		return pts;
	}
	public void setPts(List<Pt> pts) {
		this.pts = pts;
	}
	public List<Llt> getLlts() {
		return llts;
	}
	public void setLlts(List<Llt> llts) {
		this.llts = llts;
	}
	
	
	

}
