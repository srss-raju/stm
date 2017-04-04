package com.deloitte.smt.util;

public class SignalUtil {
	
	public static String getCounts(int validateCount, int assesmentCount, int riskCount){
		StringBuilder builder = new StringBuilder();
		builder.append("{").append("\"validateCount\":").append(validateCount)
		.append(",\"assesmentCount\":").append(assesmentCount)
		.append(",\"riskCount\":").append(riskCount)
		.append("}");
		return builder.toString();
	}

}
