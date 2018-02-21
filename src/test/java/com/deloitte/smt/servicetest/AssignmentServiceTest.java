package com.deloitte.smt.servicetest;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.dto.ConditionProductDTO;
import com.deloitte.smt.entity.AssignmentCondition;
import com.deloitte.smt.entity.AssignmentConditionValues;
import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.entity.AssignmentProduct;
import com.deloitte.smt.entity.AssignmentProductValues;
import com.deloitte.smt.repository.AssignmentConditionRepository;
import com.deloitte.smt.repository.AssignmentConfigurationRepository;
import com.deloitte.smt.repository.AssignmentProductRepository;
import com.deloitte.smt.service.AssignmentConfigurationService;
import com.deloitte.smt.service.AssignmentService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SignalManagementApplication.class)
@TestPropertySource(locations = { "classpath:test.properties" })
public class AssignmentServiceTest {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@MockBean
    AssignmentConfigurationRepository assignmentConfigurationRepository;
	
	@Autowired
    AssignmentConfigurationService assignmentConfigurationService;
	
	@Autowired
	AssignmentService assignmentService;
    
	@MockBean
    AssignmentConditionRepository socAssignmentConfigurationRepository;
    
	@MockBean
    AssignmentProductRepository productAssignmentConfigurationRepository;
    
	 @Test
		public void testGetAssignmentConfiguration() {
	    	try{
	    		AssignmentConfiguration assignmentConfiguration = new AssignmentConfiguration();
	    		ConditionProductDTO conditionProductDTO = new ConditionProductDTO();
	    		
	    		List<AssignmentCondition> conditions = new ArrayList<>();
	    		AssignmentCondition assignmentCondition = new AssignmentCondition();
	    		List<AssignmentConditionValues> assignmentConditionValues = new ArrayList<>();
	    		AssignmentConditionValues assignmentConditionValue = new AssignmentConditionValues();
	    		assignmentConditionValues.add(assignmentConditionValue);
	    		assignmentCondition.setRecordValues(assignmentConditionValues);
	    		assignmentCondition.setRecordKey("1@#2");
	    		conditions.add(assignmentCondition);
	    		assignmentConfiguration.setConditions(conditions);
	    		conditionProductDTO.setConditions(conditions);
	    		
	    		List<AssignmentProduct> products = new ArrayList<>();
	    		AssignmentProduct assignmentProduct = new AssignmentProduct();
	    		
	    		List<AssignmentProductValues> assignmentProductValues = new ArrayList<>();
	    		AssignmentProductValues assignmentProductValue = new AssignmentProductValues();
	    		assignmentProductValues.add(assignmentProductValue);
	    		assignmentProduct.setRecordValues(assignmentProductValues);
	    		assignmentProduct.setRecordKey("1@#2");
	    		products.add(assignmentProduct);
	    		assignmentConfiguration.setProducts(products);
	    		
	    		conditionProductDTO.setProducts(products);
	    		
	    		assignmentService.getAssignmentConfiguration(assignmentConfiguration,conditionProductDTO);
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	    }
	    
	    @Test
		public void testGetAssignmentConfigurationWithConditionsOnly() {
	    	try{
	    		AssignmentConfiguration assignmentConfiguration = new AssignmentConfiguration();
	    		ConditionProductDTO conditionProductDTO = new ConditionProductDTO();
	    		
	    		List<AssignmentCondition> conditions = new ArrayList<>();
	    		AssignmentCondition assignmentCondition = new AssignmentCondition();
	    		List<AssignmentConditionValues> assignmentConditionValues = new ArrayList<>();
	    		AssignmentConditionValues assignmentConditionValue = new AssignmentConditionValues();
	    		assignmentConditionValues.add(assignmentConditionValue);
	    		assignmentCondition.setRecordValues(assignmentConditionValues);
	    		assignmentCondition.setRecordKey("1@#2");
	    		conditions.add(assignmentCondition);
	    		assignmentConfiguration.setConditions(conditions);
	    		conditionProductDTO.setConditions(conditions);
	    		
	    		assignmentService.getAssignmentConfiguration(assignmentConfiguration,conditionProductDTO);
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	    }
	    
	    @Test
		public void testGetAssignmentConfigurationWithProductsOnly() {
	    	try{
	    		AssignmentConfiguration assignmentConfiguration = new AssignmentConfiguration();
	    		ConditionProductDTO conditionProductDTO = new ConditionProductDTO();
	    		
	    		List<AssignmentProduct> products = new ArrayList<>();
	    		AssignmentProduct assignmentProduct = new AssignmentProduct();
	    		
	    		List<AssignmentProductValues> assignmentProductValues = new ArrayList<>();
	    		AssignmentProductValues assignmentProductValue = new AssignmentProductValues();
	    		assignmentProductValues.add(assignmentProductValue);
	    		assignmentProduct.setRecordValues(assignmentProductValues);
	    		assignmentProduct.setRecordKey("1@#2");
	    		products.add(assignmentProduct);
	    		assignmentConfiguration.setProducts(products);
	    		conditionProductDTO.setProducts(products);
	    		
	    		assignmentService.getAssignmentConfiguration(assignmentConfiguration,conditionProductDTO);
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	    }

}
