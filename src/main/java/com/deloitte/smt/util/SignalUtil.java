package com.deloitte.smt.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SignalUtil {
	private static final Logger LOGGER = Logger.getLogger(SignalUtil.class);
	
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
	public static Date convertStringToDate(String date) {
		Date frmDate = null;
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (date != null) {
			try {
				frmDate = sdf.parse(date);
			} catch (ParseException e) {
				LOGGER.error(e);
			}
		}
		return frmDate;
	}
	
	public static String convertDateTOString(Date currdate)
	{
		DateFormat sdff = new SimpleDateFormat("dd-MMM-yyyy");
		return sdff.format(currdate);
	}
}
