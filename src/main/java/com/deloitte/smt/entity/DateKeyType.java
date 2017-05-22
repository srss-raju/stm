package com.deloitte.smt.entity;

/**
 * Created by myelleswarapu on 06-04-2017.
 */
public enum DateKeyType {
	CREATED,LASTRUN,NEXTRUN;
	
	public static boolean searchIn(String dateKey){
		for (DateKeyType dateKeyType : DateKeyType.values()) {
			if(dateKeyType.name().equalsIgnoreCase(dateKey)){
				return true;
			}
		}
		
		return false;
	}
}
