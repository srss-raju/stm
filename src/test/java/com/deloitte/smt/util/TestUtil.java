package com.deloitte.smt.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.deloitte.smt.entity.AssessmentActionType;
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
    	SignalAction signalAction = new SignalAction();
    	signalAction.setInDays(5);
    	signalAction.setCaseInstanceId("1111");
    	return signalAction;
    }
    
	public static List<AssessmentActionType> buildAssessmentActionTypes() {
		List<AssessmentActionType> assessmentActionTypes = new ArrayList<>();
		AssessmentActionType assessmentActionType = new AssessmentActionType();
		assessmentActionType.setId(101l);
		assessmentActionType.setName("Type 1");
		assessmentActionType.setDescription("Type 1 Description");
		assessmentActionTypes.add(assessmentActionType);
		return assessmentActionTypes;
	}
}
