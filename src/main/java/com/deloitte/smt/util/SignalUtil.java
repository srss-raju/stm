package com.deloitte.smt.util;

public class SignalUtil {
	
	public static String getCounts(Long validateCount, Long assesmentCount, Long riskCount){
		StringBuilder builder = new StringBuilder();
		builder.append("{").append("\"validateCount\":").append(validateCount)
		.append(",\"assesmentCount\":").append(assesmentCount)
		.append(",\"riskCount\":").append(riskCount)
		.append("}");
		return builder.toString();
	}

}
