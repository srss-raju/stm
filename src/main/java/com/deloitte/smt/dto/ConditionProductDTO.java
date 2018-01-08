package com.deloitte.smt.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

import com.deloitte.smt.entity.AssignmentCondition;
import com.deloitte.smt.entity.AssignmentProduct;

@Data
public class ConditionProductDTO implements Serializable{

	private static final long serialVersionUID = -7910268136658523849L;
	
	private List<AssignmentCondition> conditions;
	
	private List<AssignmentProduct> products;

	
}
