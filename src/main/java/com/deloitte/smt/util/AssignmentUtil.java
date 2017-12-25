package com.deloitte.smt.util;

import java.util.Collection;

/**
 * 
 * @author rbongurala
 *
 */
public class AssignmentUtil {
	private AssignmentUtil(){
		
	}
	public static String getRecordKey(String recordKey){
		if(recordKey.contains("@#")){
			int latIndex = recordKey.lastIndexOf("@#");
			return recordKey.substring(0, latIndex);
		}
		return null;
	}
	
	
	public static boolean isNullOrEmpty( final Collection< ? > c ) {
	    return c == null || c.isEmpty();
	}

}
