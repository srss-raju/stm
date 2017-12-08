package com.deloitte.smt.util;

/**
 * 
 * @author rbongurala
 *
 */
public class AssignmentUtil {
	private AssignmentUtil(){
		
	}
	public static String getRecordKey(String recordKey){
		if(recordKey.contains("@#$")){
			int latIndex = recordKey.lastIndexOf("@#$");
			return recordKey.substring(0, latIndex);
		}
		return null;
	}

}
