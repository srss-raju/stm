package com.deloitte.smt.util;

import java.io.IOException;

import com.deloitte.smt.entity.SignalAction;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test Utility class
 *
 * @author RajeshKumarB 
 */
public class TestUtil {
    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }
    
    public static SignalAction buildSignalAction() {
    	SignalAction action = new SignalAction();
    	action.setInDays(5);
    	return action;
    	
    }
}
