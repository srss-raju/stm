package com.deloitte.smt.servicetest;

import static org.mockito.BDDMockito.given;

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
import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.entity.AssignmentSignalAssignees;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.entity.TopicAssignees;
import com.deloitte.smt.entity.TopicCondition;
import com.deloitte.smt.entity.TopicConditionValues;
import com.deloitte.smt.entity.TopicProduct;
import com.deloitte.smt.entity.TopicProductValues;
import com.deloitte.smt.repository.AssignmentConditionRepository;
import com.deloitte.smt.repository.AssignmentConfigurationRepository;
import com.deloitte.smt.repository.AssignmentProductRepository;
import com.deloitte.smt.repository.RiskPlanAssigneesRepository;
import com.deloitte.smt.repository.TopicAssigneesRepository;
import com.deloitte.smt.service.AssignmentConfigurationService;
import com.deloitte.smt.service.SignalAssignmentService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SignalManagementApplication.class)
@TestPropertySource(locations = { "classpath:test.properties" })
public class SignalAssignmentServiceTest {
	
	@Autowired
	SignalAssignmentService signalAssignmentService;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@MockBean
    AssignmentConfigurationRepository assignmentConfigurationRepository;
	
	@MockBean
    TopicAssigneesRepository topicSignalValidationAssignmentAssigneesRepository;
    
	@MockBean
    RiskPlanAssigneesRepository topicRiskPlanAssignmentAssigneesRepository;
    
    @Autowired
    AssignmentConfigurationService assignmentConfigurationService;
    
    @MockBean
    AssignmentConditionRepository socAssignmentConfigurationRepository;
    
    @MockBean
    AssignmentProductRepository productAssignmentConfigurationRepository;
    
    
    @Test
	public void testSaveSignalAssignmentAssignees() {
    	try{
    		List<TopicAssignees> list = new ArrayList<>();
        	AssignmentConfiguration assignmentConfiguration = new AssignmentConfiguration(); 
        	Topic topicUpdated = new Topic();
        	List<AssignmentSignalAssignees> signalAssignees = new ArrayList<>();
        	AssignmentSignalAssignees assignee = new AssignmentSignalAssignees();
        	signalAssignees.add(assignee);
        	assignmentConfiguration.setSignalAssignees(signalAssignees);
        	given(this.topicSignalValidationAssignmentAssigneesRepository.save(list)).willReturn(list);
        	signalAssignmentService.saveSignalAssignmentAssignees(assignmentConfiguration, topicUpdated);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    @Test
	public void testConvertToAssignmentConfiguration() {
    	try{
    		List<TopicCondition> conditions = new ArrayList<>();
    		TopicCondition topicCondition = new TopicCondition();
    		List<TopicConditionValues> topicConditionValues = new ArrayList<>();
    		TopicConditionValues topicConditionValue = new TopicConditionValues();
    		topicConditionValues.add(topicConditionValue);
    		topicCondition.setRecordValues(topicConditionValues);
    		conditions.add(topicCondition);
    		List<TopicProduct> products = new ArrayList<>();
    		TopicProduct topicProduct = new TopicProduct();
    		List<TopicProductValues> topicProductValues = new ArrayList<>(); 
    		TopicProductValues topicProductValue = new TopicProductValues();
    		topicProductValues.add(topicProductValue);
    		topicProduct.setRecordValues(topicProductValues);
    		products.add(topicProduct);
    		Topic topicUpdated = new Topic();
    		topicUpdated.setProducts(products);
    		topicUpdated.setConditions(conditions);
    		signalAssignmentService.convertToAssignmentConfiguration(topicUpdated);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }

}
