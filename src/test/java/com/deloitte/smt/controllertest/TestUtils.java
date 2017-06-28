package com.deloitte.smt.controllertest;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtils {
	public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsBytes(object);
    }
	
	public static String convertObjectToJsonString(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
