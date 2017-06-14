package com.deloitte.smt.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cavula
 */
public enum ExecutionType {
	LATE,ONTIME;


	public boolean isStatusAvailable(String executionTypeInput) {
		for (ExecutionType executionType : ExecutionType.values()) {
			if(executionType.name().equalsIgnoreCase(executionTypeInput)){
				return true;
			}
		}
		
		return false;
	}
	
	public static List<String> getExecutionTypes(){
		List<String> values=new ArrayList<String>();
		for (ExecutionType executionType : ExecutionType.values()) {
			values.add(executionType.name());
		}
		
		return values;
	}
}
