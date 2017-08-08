package com.deloitte.smt.util;

import com.google.gson.Gson;

/**
 * 
 * @author RajeshKumar
 *
 */
public class JsonUtil {

	private JsonUtil(){
	}
	
	public static String converToJson(Object object){
		Gson gson = new Gson();
		return gson.toJson(object);
	}
}
