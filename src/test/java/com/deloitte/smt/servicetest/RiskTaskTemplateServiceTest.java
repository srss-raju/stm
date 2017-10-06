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
import com.deloitte.smt.entity.RiskTaskTemplate;
import com.deloitte.smt.entity.TaskTemplateIngrediant;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.repository.RiskTaskRepository;
import com.deloitte.smt.repository.RiskTaskTemplateRepository;
import com.deloitte.smt.repository.TaskTemplateIngrediantRepository;
import com.deloitte.smt.service.RiskTaskTemplateService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class RiskTaskTemplateServiceTest {
	
	private static final Logger LOG = Logger.getLogger(TaskTemplateServiceTest.class);
	
	@Autowired
	private RiskTaskTemplateService riskTaskTemplateService;
	
	@MockBean
	private RiskTaskTemplateRepository riskTaskTemplateRepository;
	
	@MockBean
	private RiskTaskRepository riskTaskRepository;
	
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
		RiskTaskTemplate taskTemplate = new RiskTaskTemplate();
		taskTemplate.setId(1l);
		List<TaskTemplateIngrediant> ingradients = new ArrayList<>();
		TaskTemplateIngrediant taskTemplateIngrediant = new TaskTemplateIngrediant();
		ingradients.add(taskTemplateIngrediant);
		taskTemplate.setTaskTemplateIngrediant(ingradients);
		given(this.riskTaskTemplateRepository.save(taskTemplate)).willReturn(taskTemplate);
		riskTaskTemplateService.createTaskTemplate(taskTemplate);
	}
	
	@Test
	public void testUpdateTaskTemplate() throws Exception{
		LOG.info("testCreateTaskTemplate");
		RiskTaskTemplate taskTemplate = new RiskTaskTemplate();
		taskTemplate.setId(1l);
		List<TaskTemplateIngrediant> ingradients = new ArrayList<>();
		TaskTemplateIngrediant taskTemplateIngrediant = new TaskTemplateIngrediant();
		ingradients.add(taskTemplateIngrediant);
		taskTemplate.setTaskTemplateIngrediant(ingradients);
		given(this.riskTaskTemplateRepository.save(taskTemplate)).willReturn(taskTemplate);
		riskTaskTemplateService.updateTaskTemplate(taskTemplate);
	}
	
	@Test
	public void testUpdateTaskTemplateWithIngredients() throws Exception{
		LOG.info("testCreateTaskTemplate");
		RiskTaskTemplate taskTemplate = new RiskTaskTemplate();
		taskTemplate.setId(1l);
		List<TaskTemplateIngrediant> ingradients = new ArrayList<>();
		List<Long> ids = new ArrayList<>();
		ids.add(1l);
		taskTemplate.setDeletedIngrediantIds(ids);
		TaskTemplateIngrediant taskTemplateIngrediant = new TaskTemplateIngrediant();
		ingradients.add(taskTemplateIngrediant);
		taskTemplate.setTaskTemplateIngrediant(ingradients);
		given(this.riskTaskTemplateRepository.save(taskTemplate)).willReturn(taskTemplate);
		riskTaskTemplateService.updateTaskTemplate(taskTemplate);
	}
	
	@Test
	public void testDelete() throws Exception{
		RiskTaskTemplate taskTemplate = new RiskTaskTemplate();
		given(this.riskTaskTemplateRepository.findOne(1l)).willReturn(taskTemplate);
		riskTaskTemplateService.delete(1l);
	}
	
	@Test
	public void testDeleteWithNull() throws Exception{
		try{
			RiskTaskTemplate taskTemplate = new RiskTaskTemplate();
			given(this.riskTaskTemplateRepository.findOne(11l)).willReturn(taskTemplate);
			riskTaskTemplateService.delete(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindAllByTemplateId() throws Exception{
		riskTaskTemplateService.findAllByTemplateId(1l);
	}
	
	@Test
	public void testFindAll() throws Exception{
		RiskTaskTemplate taskTemplate = new RiskTaskTemplate();
		List<RiskTaskTemplate> taskTemplates = new ArrayList<>();
		taskTemplates.add(taskTemplate);
		given(this.riskTaskTemplateRepository.findAllByOrderByCreatedDateDesc()).willReturn(taskTemplates);
		riskTaskTemplateService.findAll();
	}
	
	@Test
	public void testFindById() throws Exception{
		RiskTaskTemplate taskTemplate = new RiskTaskTemplate();
		taskTemplate.setId(1l);
		given(this.riskTaskTemplateRepository.findOne(1l)).willReturn(taskTemplate);
		riskTaskTemplateService.findById(1l);
	}
	
	@Test
	public void testFindByIdWithNull() throws Exception{
		try{
			RiskTaskTemplate taskTemplate = new RiskTaskTemplate();
			taskTemplate.setId(12l);
			given(this.riskTaskTemplateRepository.findOne(1l)).willReturn(taskTemplate);
			riskTaskTemplateService.findById(11l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	@Test
	public void testUpdateRiskTaskName(){
		try {
		List<RiskTaskTemplate> listTasks=new ArrayList<>();
		RiskTaskTemplate taskTemplate=new RiskTaskTemplate();
		taskTemplate.setId(1l);
		taskTemplate.setName("testTemplate1");
		listTasks.add(taskTemplate);
		given(this.riskTaskTemplateRepository.findAll()).willReturn(listTasks);
	
		riskTaskTemplateService.updateRiskTaskName(1l, "testTemplate2");
		} catch (ApplicationException ex) {
			LOG.info(ex);
		}
	}
	@Test(expected = ApplicationException.class)
	public void testupdateRiskTaskDuplicateName() throws ApplicationException{
		List<RiskTaskTemplate> listTasks=new ArrayList<>();
		RiskTaskTemplate taskTemplate=new RiskTaskTemplate();
		taskTemplate.setId(1l);
		taskTemplate.setName("testTemplate1");
		listTasks.add(taskTemplate);
		given(this.riskTaskTemplateRepository.findAll()).willReturn(listTasks);
		List<String> duplicateNames=new ArrayList<>();
		duplicateNames.add("testTemplate1");
		given(this.riskTaskTemplateRepository.findByName("testTemplate1",1l)).willReturn(duplicateNames);
		riskTaskTemplateService.updateRiskTaskName(1l, "testTemplate1");
	}
	
}
