package com.deloitte.smt.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.entity.AssessmentActionType;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.SignalAction;
import com.deloitte.smt.entity.Topic;
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
	
	public static SearchDto buildSearchDto(boolean isDueDate, boolean gantt) {
		SearchDto searchDto = new SearchDto();
		List<String> assignees = new ArrayList<>();
		List<String> ingredients = new ArrayList<>();
		List<String> products = new ArrayList<>();
		List<String> licenses = new ArrayList<>();
		List<String> socs = new ArrayList<>();
		List<String> hlgts = new ArrayList<>();
		List<String> hlts = new ArrayList<>();
		List<String> pts = new ArrayList<>();
		List<String> signalConfirmations = new ArrayList<>();
		List<String> signalNames = new ArrayList<>();
		List<String> frequency = new ArrayList<>();
		List<String> statuses = new ArrayList<>();
		List<String> assessmentTaskStatus = new ArrayList<>();
		List<String> riskTaskStatus = new ArrayList<>();
		Date createdDate = new Date();
		String description = "Test";
		String dateKey = "DueDate";
		Date startDate = new Date();
		Date endDate = new Date();
		
		List<String> sources = new ArrayList<>();
		sources.add("'Social Media");
		
		List<Long> userKeys = new ArrayList<>();
		List<Long> userGroupKeys = new ArrayList<>();
		userKeys.add(1l);
		userGroupKeys.add(1l);
		List<String> owners = new ArrayList<>();
		owners.add("admin");
		
		assignees.add("SANALYST1");
		ingredients.add("AMOXICILLIN");
		products.add("IBUPROFEN ( Tablet )");
		licenses.add("ANEMIA");
		socs.add("ANEMIA");
		hlgts.add("ANEMIA");
		hlts.add("ANEMIA");
		pts.add("ANEMIA");
		signalConfirmations.add("Validated Signal");
		signalNames.add("Test");
		frequency.add("Test");
		statuses.add("New");
		assessmentTaskStatus.add("Completed");
		riskTaskStatus.add("Not Completed");
		
		searchDto.setIngredients(ingredients);
		searchDto.setProducts(products);
		searchDto.setLicenses(licenses);
		searchDto.setSocs(socs);
		searchDto.setHlgts(hlgts);
		searchDto.setHlts(hlts);
		searchDto.setPts(pts);
		searchDto.setSignalConfirmations(signalConfirmations);
		searchDto.setSignalNames(signalNames);
		searchDto.setFrequency(frequency);
		searchDto.setStatuses(statuses);
		searchDto.setAssessmentTaskStatus(assessmentTaskStatus);
		searchDto.setRiskTaskStatus(riskTaskStatus);
		searchDto.setCreatedDate(createdDate);
		searchDto.setDescription(description);
		searchDto.setStartDate(startDate);
		searchDto.setEndDate(endDate);
		searchDto.setDateKey(dateKey);
		searchDto.setDueDate(isDueDate);
		searchDto.setGantt(gantt);
		searchDto.setSources(sources);
		searchDto.setUserGroupKeys(userGroupKeys);
		searchDto.setUserKeys(userKeys);
		searchDto.setOwners(owners);
		
    	return searchDto;
    }
	
	public static List<Topic> buildmatchingSignals() {
		List<Topic> signals = new ArrayList<>();
		Topic signal1 = new Topic();
		signal1.setCohortPercentage(99l);
		signal1.setCreatedDate(new Date());
		signal1.setConfidenceIndex(40l);
		signals.add(signal1);
		AssessmentPlan assessmentPlan = new AssessmentPlan();
		assessmentPlan.setCohortPercentage(80l);
		Topic signal2 = new Topic();
		signal2.setCohortPercentage(80l);
		signal2.setConfidenceIndex(40l);
		signal2.setAssessmentPlan(assessmentPlan);
		signals.add(signal2);
		Topic signal3 = new Topic();
		signal3.setCohortPercentage(71l);
		signal3.setConfidenceIndex(40l);
		signals.add(signal3);
		Topic signal4 = new Topic();
		signal4.setCohortPercentage(96l);
		signal4.setConfidenceIndex(40l);
		signal4.setCreatedDate(new Date());
		signals.add(signal4);
		return signals;
	}
	
	public static List<Topic> build95matchingSignals() {
		List<Topic> signals = new ArrayList<>();
		Topic signal1 = new Topic();
		signal1.setCohortPercentage(99l);
		signal1.setCreatedDate(new Date());
		signal1.setConfidenceIndex(40l);
		signals.add(signal1);
		AssessmentPlan assessmentPlan = new AssessmentPlan();
		assessmentPlan.setCohortPercentage(80l);
		Topic signal2 = new Topic();
		signal2.setCohortPercentage(98l);
		signal2.setConfidenceIndex(40l);
		signal2.setAssessmentPlan(assessmentPlan);
		signals.add(signal2);
		Topic signal3 = new Topic();
		signal3.setCohortPercentage(97l);
		signal3.setConfidenceIndex(40l);
		signals.add(signal3);
		Topic signal4 = new Topic();
		signal4.setCohortPercentage(96l);
		signal4.setConfidenceIndex(40l);
		signal4.setCreatedDate(new Date());
		signals.add(signal4);
		return signals;
	}
	
	public static List<Topic> build7595matchingSignals() {
		List<Topic> signals = new ArrayList<>();
		Topic signal1 = new Topic();
		signal1.setCohortPercentage(76l);
		signal1.setCreatedDate(new Date());
		signal1.setConfidenceIndex(40l);
		signals.add(signal1);
		AssessmentPlan assessmentPlan = new AssessmentPlan();
		assessmentPlan.setCohortPercentage(80l);
		Topic signal2 = new Topic();
		signal2.setCohortPercentage(77l);
		signal2.setConfidenceIndex(40l);
		signal2.setAssessmentPlan(assessmentPlan);
		signals.add(signal2);
		Topic signal3 = new Topic();
		signal3.setCohortPercentage(78l);
		signal3.setConfidenceIndex(40l);
		signals.add(signal3);
		Topic signal4 = new Topic();
		signal4.setCohortPercentage(79l);
		signal4.setConfidenceIndex(40l);
		signal4.setCreatedDate(new Date());
		signals.add(signal4);
		return signals;
	}
	
	public static List<Topic> build75matchingSignals() {
		List<Topic> signals = new ArrayList<>();
		Topic signal1 = new Topic();
		signal1.setCohortPercentage(66l);
		signal1.setCreatedDate(new Date());
		signal1.setConfidenceIndex(40l);
		signals.add(signal1);
		AssessmentPlan assessmentPlan = new AssessmentPlan();
		assessmentPlan.setCohortPercentage(80l);
		Topic signal2 = new Topic();
		signal2.setCohortPercentage(67l);
		signal2.setConfidenceIndex(40l);
		signal2.setAssessmentPlan(assessmentPlan);
		signals.add(signal2);
		Topic signal3 = new Topic();
		signal3.setCohortPercentage(68l);
		signal3.setConfidenceIndex(40l);
		signals.add(signal3);
		Topic signal4 = new Topic();
		signal4.setCohortPercentage(69l);
		signal4.setConfidenceIndex(40l);
		signal4.setCreatedDate(new Date());
		signals.add(signal4);
		return signals;
	}
	
	public static Topic buildSignal() {
		Topic signal = new Topic();
		signal.setConfidenceIndex(50l);
		return signal;
	}
	
	public static String convertObjectToJsonString(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
	
}
