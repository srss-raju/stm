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
import com.deloitte.smt.entity.TaskTemplate;
import com.deloitte.smt.entity.TaskTemplateIngrediant;
import com.deloitte.smt.repository.AssessmentActionRepository;
import com.deloitte.smt.repository.TaskTemplateIngrediantRepository;
import com.deloitte.smt.repository.TaskTemplateRepository;
import com.deloitte.smt.service.TaskTemplateService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class TaskTemplateServiceTest {
	
	private static final Logger LOG = Logger.getLogger(TaskTemplateServiceTest.class);
	
	@Autowired
	private TaskTemplateService taskTemplateService;
	
	@MockBean
	private TaskTemplateRepository taskTemplateRepository;
	
	@MockBean
	private AssessmentActionRepository assessmentActionRepository;
	
	@MockBean
	private TaskTemplateIngrediantRepository taskTemplateIngrediantRepository;
	
		
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
	public void testCreateTaskTemplate() throws Exception{
		LOG.info("testCreateTaskTemplate");
		TaskTemplate taskTemplate = new TaskTemplate();
		taskTemplate.setId(1l);
		List<TaskTemplateIngrediant> ingradients = new ArrayList<>();
		TaskTemplateIngrediant taskTemplateIngrediant = new TaskTemplateIngrediant();
		ingradients.add(taskTemplateIngrediant);
		taskTemplate.setTaskTemplateIngrediant(ingradients);
		given(this.taskTemplateRepository.save(taskTemplate)).willReturn(taskTemplate);
		taskTemplateService.createTaskTemplate(taskTemplate);
	}
	
	
	
}
