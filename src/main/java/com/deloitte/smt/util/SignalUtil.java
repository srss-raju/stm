package com.deloitte.smt.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.SignalAction;

public class SignalUtil {
	
	public static String getCounts(Long validateCount, Long assesmentCount, Long riskCount){
		StringBuilder builder = new StringBuilder();
		builder.append("{").append("\"validateCount\":").append(validateCount)
		.append(",\"assesmentCount\":").append(assesmentCount)
		.append(",\"riskCount\":").append(riskCount)
		.append("}");
		return builder.toString();
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
