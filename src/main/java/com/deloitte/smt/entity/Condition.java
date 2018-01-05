package com.deloitte.smt.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Data;

/**
 * Created by myelleswarapu on 04-04-2017.
 */
@Data
@Embeddable
public class Condition implements Serializable{

	private static final long serialVersionUID = 1L;

    private String soc;
    private String pt;
    
}
