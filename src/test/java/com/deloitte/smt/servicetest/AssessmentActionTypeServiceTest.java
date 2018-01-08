package com.deloitte.smt.servicetest;

import static org.mockito.BDDMockito.given;

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
import com.deloitte.smt.entity.TaskType;
import com.deloitte.smt.repository.TaskTypeRepository;
import com.deloitte.smt.service.TaskTypeService;
import com.deloitte.smt.util.TestUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class AssessmentActionTypeServiceTest {
	
	private static final Logger LOG = Logger.getLogger(AssessmentActionTypeServiceTest.class);
	
	@Autowired
	private TaskTypeService assessmentActionTypeService;
	
	@MockBean
    TaskTypeRepository assessmentActionTypeRepository;
	
		
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
		assessmentActionTypeService.insert(TestUtil.buildAssessmentActionTypes());
	}
	
	@Test
	public void testUpdateWithNull() {
		try{
			TaskType assessmentActionType = new TaskType();
			assessmentActionTypeService.update(assessmentActionType);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testUpdate() throws Exception{
		assessmentActionTypeService.update(TestUtil.buildAssessmentActionTypes().get(0));
	}
	
	@Test
	public void testDeleteWithNull() {
		try{
			assessmentActionTypeService.delete(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testDelete() throws Exception{
		TaskType assessmentActionType = new TaskType();
		given(this.assessmentActionTypeRepository.findOne(1l)).willReturn(assessmentActionType);
		assessmentActionTypeService.delete(1l);
	}
	
	@Test
	public void testFindByIdWithNull() throws Exception{
		try{
			assessmentActionTypeService.findById(101l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	
	@Test
	public void testFindById() throws Exception{
		try{
			TaskType assessmentActionType = new TaskType();
			given(this.assessmentActionTypeRepository.findOne(1l)).willReturn(assessmentActionType);
			assessmentActionTypeService.findById(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindAll() throws Exception{
		assessmentActionTypeService.findAll();
	}
	
}
