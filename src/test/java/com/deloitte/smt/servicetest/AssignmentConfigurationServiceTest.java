package com.deloitte.smt.servicetest;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.mock.MockExpressionManager;
import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.entity.AssessmentAssignmentAssignees;
import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.entity.RiskPlanAssignmentAssignees;
import com.deloitte.smt.entity.SignalValidationAssignmentAssignees;
import com.deloitte.smt.repository.AssessmentAssignmentAssigneesRepository;
import com.deloitte.smt.repository.AssignmentConfigurationRepository;
import com.deloitte.smt.repository.RiskPlanAssignmentAssigneesRepository;
import com.deloitte.smt.repository.SignalValidationAssignmentAssigneesRepository;
import com.deloitte.smt.service.AssignmentConfigurationService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class AssignmentConfigurationServiceTest {
	
	private static final Logger LOG = Logger.getLogger(AssignmentConfigurationServiceTest.class);
	
	@Autowired
	private AssignmentConfigurationService assignmentConfigurationService;
	
	@MockBean
	AssignmentConfigurationRepository assignmentConfigurationRepository;
	
	@MockBean
    SignalValidationAssignmentAssigneesRepository signalValidationAssignmentAssigneesRepository;
    
	@MockBean
    AssessmentAssignmentAssigneesRepository assessmentAssignmentAssigneesRepository;
    
	@MockBean
    RiskPlanAssignmentAssigneesRepository riskPlanAssignmentAssigneesRepository;
	
		
	private static final ProcessEngineConfiguration processEngineConfiguration = new StandaloneInMemProcessEngineConfiguration() {
	    {
	      jobExecutorActivate = false;
	      expressionManager = new MockExpressionManager();
	      databaseSchemaUpdate = DB_SCHEMA_UPDATE_CREATE_DROP;
	    }
	  };
	  
	  private static final ProcessEngine PROCESS_ENGINE_NEEDS_CLOSE = processEngineConfiguration.buildProcessEngine();
	  
	  @Rule
	  public final ProcessEngineRule processEngine = new ProcessEngineRule(PROCESS_ENGINE_NEEDS_CLOSE);

	  @AfterClass
	  public static void shutdown() {
	    PROCESS_ENGINE_NEEDS_CLOSE.close();
	  }

    
	@Test
	public void testInsertNotNull() {
		try{
			AssignmentConfiguration config = new AssignmentConfiguration();
			config.setAssessmentAssignmentOwner("Test User");
			config.setIngredient("test");
			config.setSignalSource("test");
			
			List<SignalValidationAssignmentAssignees> svList = new ArrayList<>();
			SignalValidationAssignmentAssignees svAssignee = new SignalValidationAssignmentAssignees();
			svAssignee.setUserGroupKey(1l);
			svList.add(svAssignee);
			
			List<AssessmentAssignmentAssignees> aList = new ArrayList<>();
			AssessmentAssignmentAssignees aAssignee = new AssessmentAssignmentAssignees();
			svAssignee.setUserGroupKey(1l);
			aList.add(aAssignee);
			
			List<RiskPlanAssignmentAssignees> rpList = new ArrayList<>();
			RiskPlanAssignmentAssignees rAssignee = new RiskPlanAssignmentAssignees();
			svAssignee.setUserGroupKey(1l);
			rpList.add(rAssignee);
			
			config.setSignalValidationAssignmentAssignees(svList);
			config.setAssessmentAssignmentAssignees(aList);
			config.setRiskPlanAssignmentAssignees(rpList);
			
			//given(this.assignmentConfigurationRepository.findByIngredientAndSignalSource("test","test")).willReturn(config);
			given(this.assignmentConfigurationRepository.save(config)).willReturn(config);
			assignmentConfigurationService.insert(config);
		}catch(Exception ex){
			
		}
	}
	
	@Test
	public void testInsertWithException() {
		try{
			AssignmentConfiguration config = new AssignmentConfiguration();
			config.setAssessmentAssignmentOwner("Test User");
			config.setIngredient("test");
			config.setSignalSource("test");
			
			given(this.assignmentConfigurationRepository.findByIngredientAndSignalSource("test","test")).willReturn(config);
			assignmentConfigurationService.insert(config);
		}catch(Exception ex){
			
		}
	}
	
	@Test
	public void testInsert() {
		try{
			AssignmentConfiguration config = new AssignmentConfiguration();
			config.setAssessmentAssignmentOwner("Test User");
			config.setIngredient("test");
			config.setSignalSource("test");
			assignmentConfigurationService.insert(config);
		}catch(Exception ex){
			
		}
	}
	
	@Test
	public void testUpdate() throws Exception{
		try{
			AssignmentConfiguration config = new AssignmentConfiguration();
			config.setAssessmentAssignmentOwner("Test User");
			config.setIngredient("Test Ingredient");
			config.setSignalSource("Test Source");
			config.setId(1l);
			given(this.assignmentConfigurationRepository.findByIngredientAndSignalSource("Test Ingredient","Test Source")).willReturn(config);
			assignmentConfigurationService.update(config);
		}catch(Exception ex){
			
		}
	}
	
	@Test
	public void testUpdateWithNull() {
		try{
			AssignmentConfiguration config = new AssignmentConfiguration();
			config.setAssessmentAssignmentOwner("Test User");
			config.setIngredient("Test Ingredient");
			config.setSignalSource("Test Source");
			assignmentConfigurationService.update(config);
		}catch(Exception ex){
			
		}
	}
	
	@Test
	public void testDelete() throws Exception{
		try{
			AssignmentConfiguration config = new AssignmentConfiguration();
			config.setAssessmentAssignmentOwner("Test User");
			config.setIngredient("Test Ingredient");
			config.setSignalSource("Test Source");
			config.setId(1l);
			given(this.assignmentConfigurationRepository.findOne(1l)).willReturn(config);
			assignmentConfigurationService.delete(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testDeleteWithNull() throws Exception{
		try{
			AssignmentConfiguration config = new AssignmentConfiguration();
			config.setAssessmentAssignmentOwner("Test User");
			config.setIngredient("Test Ingredient");
			config.setSignalSource("Test Source");
			config.setId(1l);
			given(this.assignmentConfigurationRepository.findOne(11l)).willReturn(config);
			assignmentConfigurationService.delete(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindById() throws Exception{
		AssignmentConfiguration config = new AssignmentConfiguration();
		config.setAssessmentAssignmentOwner("Test User");
		config.setIngredient("Test Ingredient");
		config.setSignalSource("Test Source");
		config.setId(1l);
		given(this.assignmentConfigurationRepository.findOne(1l)).willReturn(config);
		assignmentConfigurationService.findById(1l);
	}
	@Test
	public void testFindByIdWithNull() throws Exception{
		try{
			AssignmentConfiguration config = new AssignmentConfiguration();
			config.setAssessmentAssignmentOwner("Test User");
			config.setIngredient("Test Ingredient");
			config.setSignalSource("Test Source");
			config.setId(1l);
			given(this.assignmentConfigurationRepository.findOne(11l)).willReturn(config);
			assignmentConfigurationService.findById(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindAll() throws Exception{
		assignmentConfigurationService.findAll();
	}
	
	@Test
	public void testDeleteSignalValidationAssignmentAssignee() throws Exception{
		try{
			SignalValidationAssignmentAssignees assignee = new SignalValidationAssignmentAssignees();
			assignee.setId(1l);
			given(this.signalValidationAssignmentAssigneesRepository.findOne(1l)).willReturn(assignee);
			assignmentConfigurationService.deleteSignalValidationAssignmentAssignee(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testDeleteSignalValidationAssignmentAssigneeWithNull() throws Exception{
		try{
			SignalValidationAssignmentAssignees assignee = new SignalValidationAssignmentAssignees();
			assignee.setId(1l);
			given(this.signalValidationAssignmentAssigneesRepository.findOne(11l)).willReturn(assignee);
			assignmentConfigurationService.deleteSignalValidationAssignmentAssignee(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testDeleteAssessmentAssignmentAssignee() throws Exception{
		try{
			AssessmentAssignmentAssignees assignee = new AssessmentAssignmentAssignees();
			assignee.setId(1l);
			given(this.assessmentAssignmentAssigneesRepository.findOne(1l)).willReturn(assignee);
			assignmentConfigurationService.deleteSignalValidationAssignmentAssignee(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testDeleteAssessmentAssignmentAssigneeWithNull() throws Exception{
		try{
			AssessmentAssignmentAssignees assignee = new AssessmentAssignmentAssignees();
			assignee.setId(1l);
			given(this.assessmentAssignmentAssigneesRepository.findOne(11l)).willReturn(assignee);
			assignmentConfigurationService.deleteSignalValidationAssignmentAssignee(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testDeleteRiskPlanAssignmentAssignee() throws Exception{
		try{
			RiskPlanAssignmentAssignees assignee = new RiskPlanAssignmentAssignees();
			assignee.setId(1l);
			given(this.riskPlanAssignmentAssigneesRepository.findOne(1l)).willReturn(assignee);
			assignmentConfigurationService.deleteRiskPlanAssignmentAssignee(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testDeleteRiskPlanAssignmentAssigneeWithNull() throws Exception{
		try{
			RiskPlanAssignmentAssignees assignee = new RiskPlanAssignmentAssignees();
			assignee.setId(1l);
			given(this.riskPlanAssignmentAssigneesRepository.findOne(11l)).willReturn(assignee);
			assignmentConfigurationService.deleteRiskPlanAssignmentAssignee(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
}
