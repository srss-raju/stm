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
import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.repository.AssignmentConfigurationRepository;
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
	public void testInsert() throws Exception{
		List<AssignmentConfiguration> assignmentConfigurationList = new ArrayList<>();
		AssignmentConfiguration config = new AssignmentConfiguration();
		config.setAssessmentAssignmentUser("Test User");
		assignmentConfigurationList.add(config);
		assignmentConfigurationService.insert(assignmentConfigurationList);
	}
	
	@Test
	public void testInsertExists() throws Exception{
		List<AssignmentConfiguration> assignmentConfigurationList = new ArrayList<>();
		AssignmentConfiguration config = new AssignmentConfiguration();
		config.setAssessmentAssignmentUser("Test User");
		config.setIngredient("Test Ingredient");
		config.setSignalSource("Test Source");
		assignmentConfigurationList.add(config);
		try{
			given(this.assignmentConfigurationRepository.findByIngredientAndSignalSource("Test Ingredient","Test Source")).willReturn(config);
			assignmentConfigurationService.insert(assignmentConfigurationList);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testUpdate() throws Exception{
		List<AssignmentConfiguration> assignmentConfigurationList = new ArrayList<>();
		AssignmentConfiguration config = new AssignmentConfiguration();
		config.setAssessmentAssignmentUser("Test User");
		config.setIngredient("Test Ingredient");
		config.setSignalSource("Test Source");
		config.setId(1l);
		assignmentConfigurationList.add(config);
		given(this.assignmentConfigurationRepository.findByIngredientAndSignalSource("Test Ingredient","Test Source")).willReturn(config);
		assignmentConfigurationService.update(assignmentConfigurationList);
	}
	
	@Test
	public void testUpdateWithNull() throws Exception{
		List<AssignmentConfiguration> assignmentConfigurationList = new ArrayList<>();
		AssignmentConfiguration config = new AssignmentConfiguration();
		config.setAssessmentAssignmentUser("Test User");
		config.setIngredient("Test Ingredient");
		config.setSignalSource("Test Source");
		assignmentConfigurationList.add(config);
		try{
			given(this.assignmentConfigurationRepository.findByIngredientAndSignalSource("Test Ingredient","Test Source")).willReturn(config);
			assignmentConfigurationService.update(assignmentConfigurationList);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testDelete() throws Exception{
		try{
			AssignmentConfiguration config = new AssignmentConfiguration();
			config.setAssessmentAssignmentUser("Test User");
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
			config.setAssessmentAssignmentUser("Test User");
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
		config.setAssessmentAssignmentUser("Test User");
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
			config.setAssessmentAssignmentUser("Test User");
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
	}
	
}
