package com.deloitte.smt.util;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * 
 * @author RajeshKumar
 *
 */
public class JsonUtil {
	private static final Logger LOG = Logger.getLogger(JsonUtil.class);

	private JsonUtil(){
	}
	
	public static String converToJson(Object object){
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			return ow.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			LOG.info("Exception While Converting "+e);
		}
		return null;
	}
}
