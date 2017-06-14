package com.deloitte.smt.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

/**
 * Created by myelleswarapu on 04-04-2017.
 */
@Embeddable
public class Condition implements Serializable{

	private static final long serialVersionUID = 1L;

    private String soc;
    private String hlgt;
    private String hlt;
    private String pt;

    public String getSoc() {
        return soc;
    }

    public void setSoc(String soc) {
        this.soc = soc;
    }

    public String getHlgt() {
        return hlgt;
    }

    public void setHlgt(String hlgt) {
        this.hlgt = hlgt;
    }

    public String getHlt() {
        return hlt;
    }

    public void setHlt(String hlt) {
        this.hlt = hlt;
    }

    public String getPt() {
        return pt;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }
}
