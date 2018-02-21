package com.deloitte.smt.servicetest;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import com.deloitte.smt.repository.AssignmentProductValuesRepository;
import com.deloitte.smt.repository.ProductLevelRepository;
import com.deloitte.smt.repository.RiskPlanRepository;
import com.deloitte.smt.repository.SignalDetectionRepository;
import com.deloitte.smt.repository.TopicRepository;
import com.deloitte.smt.service.impl.ProductFilterServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class ProductFilterServiceTest {
	
	@Autowired
	ProductFilterServiceImpl productFilterServiceImpl;
	
	@MockBean
	private ProductLevelRepository productLevelRepository;
	
	@MockBean
	private AssignmentProductValuesRepository assignmentProductValuesRepository;
	
	@MockBean
	private TopicRepository topicRepository;
	
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
			
			conFillVals.add("1@#2");
			given(this.topicRepository.getConditionFilterValues()).willReturn(conFillVals);
			productFilterServiceImpl.productLevelFilter(filterList, "signal");
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
			productFilterServiceImpl.productLevelFilter(filterList, "detection");
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
			productFilterServiceImpl.productLevelFilter(filterList, "risk");
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
			productFilterServiceImpl.productLevelFilter(filterList, "assessment");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	

}
