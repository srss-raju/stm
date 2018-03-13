package com.deloitte.smt.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	private DateUtil(){}
	
	public static String convertDateToString(Date date){
		DateFormat sdff = new SimpleDateFormat("MMddYYYY_HHMMSSS");
		return sdff.format(date);
	}

}
