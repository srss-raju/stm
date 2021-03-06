package com.deloitte.smt.util;

import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SignalUtil {
	
	private SignalUtil(){}
	
	public static String getCounts(Long validateCount, Long assesmentCount, Long riskCount){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("validateCount", validateCount);
		node.put("assesmentCount", assesmentCount);
		node.put("riskCount", riskCount);
		return node.toString();
	}
	 
	public static Date getNextRunDate(String runFrequency, Date createdDate){
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(createdDate);
		if("Daily".equalsIgnoreCase(runFrequency)){
			calendar.add(Calendar.DATE, 1);
		}else if("Weekly".equalsIgnoreCase(runFrequency)){
			calendar.add(Calendar.DATE, 7);
		}else if("Monthly".equalsIgnoreCase(runFrequency)){
			calendar.add(Calendar.MONTH, 1);
		}else if("Quarterly".equalsIgnoreCase(runFrequency)){
			calendar.add(Calendar.MONTH, 3);
		}else if("Yearly".equalsIgnoreCase(runFrequency)){
			calendar.add(Calendar.YEAR, 1);
		}
		return calendar.getTime();
	}
	
	public static Date getDueDate(int inDays, Date createdDate){
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(createdDate);
		calendar.add(Calendar.DATE, inDays);
		return calendar.getTime();
	}
	
}
