package com.deloitte.smt.util;

import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.SignalAction;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SignalUtil {
	
	public static String getCounts(Long validateCount, Long assesmentCount, Long riskCount){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("validateCount", validateCount);
		node.put("assesmentCount", assesmentCount);
		node.put("riskCount", riskCount);
		return node.toString();
	}
	
	public static List<SignalAction> createAssessmentActions(AssessmentPlan assessmentPlan){
		List<SignalAction> signalActionList = new ArrayList<>();
		for(int i=0; i<5; i++){
			SignalAction signalAction = new SignalAction();
			Date date = new Date();
			signalAction.setActionName(String.valueOf(i+1));
	        signalAction.setCreatedDate(date);
	        signalAction.setLastModifiedDate(date);
	        signalAction.setActionStatus("New");
	        final Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_YEAR, i);
			signalAction.setDueDate(calendar.getTime());
			signalAction.setAssessmentId(String.valueOf(assessmentPlan.getId()));
	        signalActionList.add(signalAction);
		}
        return signalActionList;
	}

}
