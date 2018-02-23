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
import com.deloitte.smt.entity.ProductLevels;
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
	public void testGetSignalFiltersSignal() {
		try{
			List<FilterDTO> filterList = new ArrayList<>();
			FilterDTO filterDTO = new FilterDTO();
			filterList.add(filterDTO);
			List<ProductLevels> productLevels = new ArrayList<>();
			ProductLevels productLevel = new ProductLevels();
			productLevel.setVersions("2.1");
			productLevels.add(productLevel);
			List<String> prodFillVals = new ArrayList<>();
			prodFillVals.add("A@#B");
			Set<String> recValues = new HashSet<>();
			List<Object[]> list = new ArrayList<>();
			Object[] array = new Object[3];
			array[0] = "A";
			array[1] = "B";
			array[2] = "C";
			list.add(array);
			given(this.productLevelRepository.findAllByOrderByIdAsc()).willReturn(productLevels);
			given(this.assignmentProductValuesRepository.findDistinctByCategoryCodeIn(recValues)).willReturn(list);
			given(this.topicRepository.getProductFilterValues()).willReturn(prodFillVals);
			productFilterServiceImpl.productLevelFilter(filterList, "signal");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testGetSignalFiltersDetection() {
		try{
			List<FilterDTO> filterList = new ArrayList<>();
			FilterDTO filterDTO = new FilterDTO();
			filterList.add(filterDTO);
			List<ProductLevels> productLevels = new ArrayList<>();
			ProductLevels productLevel = new ProductLevels();
			productLevel.setVersions("2.1");
			productLevels.add(productLevel);
			List<String> prodFillVals = new ArrayList<>();
			prodFillVals.add("A@#B");
			Set<String> recValues = new HashSet<>();
			List<Object[]> list = new ArrayList<>();
			Object[] array = new Object[3];
			array[0] = "A";
			array[1] = "B";
			array[2] = "C";
			list.add(array);
			given(this.productLevelRepository.findAllByOrderByIdAsc()).willReturn(productLevels);
			given(this.assignmentProductValuesRepository.findDistinctByCategoryCodeIn(recValues)).willReturn(list);
			given(this.topicRepository.getProductFilterValues()).willReturn(prodFillVals);
			productFilterServiceImpl.productLevelFilter(filterList, "detection");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testGetSignalFiltersRisk() {
		try{
			List<FilterDTO> filterList = new ArrayList<>();
			FilterDTO filterDTO = new FilterDTO();
			filterList.add(filterDTO);
			List<ProductLevels> productLevels = new ArrayList<>();
			ProductLevels productLevel = new ProductLevels();
			productLevel.setVersions("2.1");
			productLevels.add(productLevel);
			List<String> prodFillVals = new ArrayList<>();
			prodFillVals.add("A@#B");
			Set<String> recValues = new HashSet<>();
			List<Object[]> list = new ArrayList<>();
			Object[] array = new Object[3];
			array[0] = "A";
			array[1] = "B";
			array[2] = "C";
			list.add(array);
			given(this.productLevelRepository.findAllByOrderByIdAsc()).willReturn(productLevels);
			given(this.assignmentProductValuesRepository.findDistinctByCategoryCodeIn(recValues)).willReturn(list);
			given(this.topicRepository.getProductFilterValues()).willReturn(prodFillVals);
			productFilterServiceImpl.productLevelFilter(filterList, "risk");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testGetSignalFiltersAssessment() {
		try{
			List<FilterDTO> filterList = new ArrayList<>();
			FilterDTO filterDTO = new FilterDTO();
			filterList.add(filterDTO);
			List<ProductLevels> productLevels = new ArrayList<>();
			ProductLevels productLevel = new ProductLevels();
			productLevel.setVersions("2.1");
			productLevels.add(productLevel);
			List<String> prodFillVals = new ArrayList<>();
			prodFillVals.add("A@#B");
			Set<String> recValues = new HashSet<>();
			List<Object[]> list = new ArrayList<>();
			Object[] array = new Object[3];
			array[0] = "A";
			array[1] = "B";
			array[2] = "C";
			list.add(array);
			given(this.productLevelRepository.findAllByOrderByIdAsc()).willReturn(productLevels);
			given(this.assignmentProductValuesRepository.findDistinctByCategoryCodeIn(recValues)).willReturn(list);
			given(this.topicRepository.getProductFilterValues()).willReturn(prodFillVals);
			productFilterServiceImpl.productLevelFilter(filterList, "assessment");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	@Test
	public void testConstructProductPredicateSignal() {
		try{
			List<String> prodFillVals = new ArrayList<>();
			prodFillVals.add("A@#B");
			Set<String> productSet = new HashSet<>();
			productSet.add("A@#B");
			StringBuilder queryBuilder = new StringBuilder();;
			String type = "signal";
			Map<String, Object> parameterMap = new HashMap<>();
			given(this.topicRepository.getProductFilterValues()).willReturn(prodFillVals);
			productFilterServiceImpl.constructProductPredicate(productSet, queryBuilder, type, parameterMap);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testConstructProductPredicateDetection() {
		try{
			List<String> prodFillVals = new ArrayList<>();
			prodFillVals.add("A@#B");
			Set<String> productSet = new HashSet<>();
			productSet.add("A@#B");
			StringBuilder queryBuilder = new StringBuilder();;
			String type = "detection";
			Map<String, Object> parameterMap = new HashMap<>();
			given(this.topicRepository.getProductFilterValues()).willReturn(prodFillVals);
			productFilterServiceImpl.constructProductPredicate(productSet, queryBuilder, type, parameterMap);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	

	@Test
	public void testConstructProductPredicateRisk() {
		try{
			List<String> prodFillVals = new ArrayList<>();
			prodFillVals.add("A@#B");
			Set<String> productSet = new HashSet<>();
			productSet.add("A@#B");
			StringBuilder queryBuilder = new StringBuilder();;
			String type = "risk";
			Map<String, Object> parameterMap = new HashMap<>();
			given(this.topicRepository.getProductFilterValues()).willReturn(prodFillVals);
			productFilterServiceImpl.constructProductPredicate(productSet, queryBuilder, type, parameterMap);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testConstructProductPredicateAssessment() {
		try{
			List<String> prodFillVals = new ArrayList<>();
			prodFillVals.add("A@#B");
			Set<String> productSet = new HashSet<>();
			productSet.add("A@#B");
			StringBuilder queryBuilder = new StringBuilder();;
			String type = "assessment";
			Map<String, Object> parameterMap = new HashMap<>();
			given(this.topicRepository.getProductFilterValues()).willReturn(prodFillVals);
			productFilterServiceImpl.constructProductPredicate(productSet, queryBuilder, type, parameterMap);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
