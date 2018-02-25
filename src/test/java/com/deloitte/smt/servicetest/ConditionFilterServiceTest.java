package com.deloitte.smt.servicetest;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.dto.FilterDTO;
import com.deloitte.smt.entity.ConditionLevels;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.AssignmentConditionValuesRepository;
import com.deloitte.smt.repository.ConditionLevelRepository;
import com.deloitte.smt.repository.RiskPlanRepository;
import com.deloitte.smt.repository.SignalDetectionRepository;
import com.deloitte.smt.repository.TopicRepository;
import com.deloitte.smt.service.impl.ConditionFilterServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class ConditionFilterServiceTest {

	@Autowired
	ConditionFilterServiceImpl conditionFilterServiceImpl;
	
	@MockBean
	private ConditionLevelRepository conditionLevelRepository;

	@MockBean
	private TopicRepository topicRepository;

	@MockBean
	private AssignmentConditionValuesRepository assignmentConditionValuesRepository;

	@MockBean
	private SignalDetectionRepository signalDetectionRepository;

	@MockBean
	private RiskPlanRepository riskPlanRepository;

	@MockBean
	private AssessmentPlanRepository assessmentPlanRepository;
	
	@Test
	public void testGetSignalFilters() {
		try{
			List<FilterDTO> filterList = new ArrayList<>();
			List<String> conFillVals = new ArrayList<>();
			List<ConditionLevels> conditionLevels = new ArrayList<>();
			ConditionLevels conditionLevel = new ConditionLevels();
			conditionLevels.add(conditionLevel);
			
			conFillVals.add("A@#B");
			given(this.topicRepository.getConditionFilterValues()).willReturn(conFillVals);
			given(this.conditionLevelRepository.findAllByOrderByIdAsc()).willReturn(conditionLevels);
			conditionFilterServiceImpl.conditionLevelFilter(filterList, "signal");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testGetDetectionFilters() {
		try{
			List<FilterDTO> filterList = new ArrayList<>();
			List<String> conFillVals = new ArrayList<>();
			List<ConditionLevels> conditionLevels = new ArrayList<>();
			ConditionLevels conditionLevel = new ConditionLevels();
			conditionLevels.add(conditionLevel);
			
			conFillVals.add("1@#2");
			given(this.signalDetectionRepository.getConditionFilterValues()).willReturn(conFillVals);
			conditionFilterServiceImpl.conditionLevelFilter(filterList, "detection");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testGetRiskFilters() {
		try{
			List<FilterDTO> filterList = new ArrayList<>();
			List<String> conFillVals = new ArrayList<>();
			List<ConditionLevels> conditionLevels = new ArrayList<>();
			ConditionLevels conditionLevel = new ConditionLevels();
			conditionLevels.add(conditionLevel);
			Set<Long> ids = new HashSet<>();
			ids.add(1l);
			conFillVals.add("1@#2");
			given(this.riskPlanRepository.findAllSignalIds()).willReturn(ids);
			given(this.topicRepository.getListOfConditionRecordKeys(ids)).willReturn(conFillVals);
			conditionFilterServiceImpl.conditionLevelFilter(filterList, "risk");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testGetAssessmentFilters() {
		try{
			List<FilterDTO> filterList = new ArrayList<>();
			List<String> conFillVals = new ArrayList<>();
			List<ConditionLevels> conditionLevels = new ArrayList<>();
			ConditionLevels conditionLevel = new ConditionLevels();
			conditionLevels.add(conditionLevel);
			conFillVals.add("1@#2");
			Set<Long> ids = new HashSet<>();
			ids.add(1l);
			given(this.assessmentPlanRepository.findAllSignalIds()).willReturn(ids);
			given(this.topicRepository.getListOfConditionRecordKeys(ids)).willReturn(conFillVals);
			conditionFilterServiceImpl.conditionLevelFilter(filterList, "assessment");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testConstructConditionPredicateSignal() {
		try{
			List<String> conFillVals = new ArrayList<>();
			conFillVals.add("A@#B");
			Set<String> conditionSet = new HashSet<>();
			conditionSet.add("A@#B");
			StringBuilder queryBuilder = new StringBuilder();;
			String type = "assessment";
			Map<String, Object> parameterMap = new HashMap<>();
			given(this.topicRepository.getConditionFilterValues()).willReturn(conFillVals);
			conditionFilterServiceImpl.constructConditionPredicate(conditionSet, queryBuilder, type, parameterMap);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testConstructConditionPredicateDetection() {
		try{
			List<String> conFillVals = new ArrayList<>();
			conFillVals.add("A@#B");
			Set<String> conditionSet = new HashSet<>();
			conditionSet.add("A@#B");
			StringBuilder queryBuilder = new StringBuilder();;
			String type = "detection";
			Map<String, Object> parameterMap = new HashMap<>();
			given(this.topicRepository.getProductFilterValues()).willReturn(conFillVals);
			conditionFilterServiceImpl.constructConditionPredicate(conditionSet, queryBuilder, type, parameterMap);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testConstructConditionPredicateAssessment() {
		try{
			List<String> conFillVals = new ArrayList<>();
			conFillVals.add("A@#B");
			Set<String> conditionSet = new HashSet<>();
			conditionSet.add("A@#B");
			StringBuilder queryBuilder = new StringBuilder();;
			String type = "assessment";
			Map<String, Object> parameterMap = new HashMap<>();
			given(this.topicRepository.getProductFilterValues()).willReturn(conFillVals);
			conditionFilterServiceImpl.constructConditionPredicate(conditionSet, queryBuilder, type, parameterMap);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testConstructConditionPredicateRisk() {
		try{
			List<String> conFillVals = new ArrayList<>();
			conFillVals.add("A@#B");
			Set<String> conditionSet = new HashSet<>();
			conditionSet.add("A@#B");
			StringBuilder queryBuilder = new StringBuilder();;
			String type = "risk";
			Map<String, Object> parameterMap = new HashMap<>();
			given(this.topicRepository.getProductFilterValues()).willReturn(conFillVals);
			conditionFilterServiceImpl.constructConditionPredicate(conditionSet, queryBuilder, type, parameterMap);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
}
