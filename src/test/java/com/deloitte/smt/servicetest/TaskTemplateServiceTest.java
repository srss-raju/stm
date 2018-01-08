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
import com.deloitte.smt.repository.AssessmentActionRepository;
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
		given(this.taskTemplateRepository.save(taskTemplate)).willReturn(taskTemplate);
		taskTemplateService.createTaskTemplate(taskTemplate);
	}
	
	@Test
	public void testUpdateTaskTemplate() throws Exception{
		LOG.info("testCreateTaskTemplate");
		TaskTemplate taskTemplate = new TaskTemplate();
		taskTemplate.setId(1l);
		given(this.taskTemplateRepository.save(taskTemplate)).willReturn(taskTemplate);
		taskTemplateService.updateTaskTemplate(taskTemplate);
	}
	
	@Test
	public void testDelete() throws Exception{
		TaskTemplate taskTemplate = new TaskTemplate();
		given(this.taskTemplateRepository.findOne(1l)).willReturn(taskTemplate);
		taskTemplateService.delete(1l);
	}
	
	@Test
	public void testDeleteWithNull() throws Exception{
		try{
			TaskTemplate taskTemplate = new TaskTemplate();
			given(this.taskTemplateRepository.findOne(11l)).willReturn(taskTemplate);
			taskTemplateService.delete(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindAll() throws Exception{
		TaskTemplate taskTemplate = new TaskTemplate();
		List<TaskTemplate> taskTemplates = new ArrayList<>();
		taskTemplates.add(taskTemplate);
		given(this.taskTemplateRepository.findAllByOrderByCreatedDateDesc()).willReturn(taskTemplates);
		taskTemplateService.findAll();
	}
	
	@Test
	public void testFindById() throws Exception{
		TaskTemplate taskTemplate = new TaskTemplate();
		taskTemplate.setId(1l);
		given(this.taskTemplateRepository.findOne(1l)).willReturn(taskTemplate);
		taskTemplateService.findById(1l);
	}
	
	@Test
	public void testFindByIdWithNull() throws Exception{
		try{
			TaskTemplate taskTemplate = new TaskTemplate();
			taskTemplate.setId(12l);
			given(this.taskTemplateRepository.findOne(1l)).willReturn(taskTemplate);
			taskTemplateService.findById(11l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
}
