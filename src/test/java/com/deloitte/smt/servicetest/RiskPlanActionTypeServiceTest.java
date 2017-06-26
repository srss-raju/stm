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
import com.deloitte.smt.entity.RiskPlanActionTaskType;
import com.deloitte.smt.repository.RiskPlanActionTaskTypeRepository;
import com.deloitte.smt.service.RiskPlanActionTypeService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class RiskPlanActionTypeServiceTest {
	
	private static final Logger LOG = Logger.getLogger(RiskPlanActionTypeServiceTest.class);
	
	@Autowired
	private RiskPlanActionTypeService riskPlanActionTypeService;
	
	@MockBean
	RiskPlanActionTaskTypeRepository riskPlanActionTaskTypeRepository;
	
		
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
		List<RiskPlanActionTaskType> riskPlanActionTaskTypes = new ArrayList<>();
		RiskPlanActionTaskType riskPlanActionTaskType = new RiskPlanActionTaskType();
		riskPlanActionTaskTypes.add(riskPlanActionTaskType);
		riskPlanActionTypeService.insert(riskPlanActionTaskTypes);
	}
	
	@Test
	public void testUpdate() throws Exception{
		RiskPlanActionTaskType riskPlanActionTaskType = new RiskPlanActionTaskType();
		riskPlanActionTaskType.setId(1l);
		riskPlanActionTypeService.update(riskPlanActionTaskType);
	}
	
	@Test
	public void testUpdateWithNull() throws Exception{
		try{
			RiskPlanActionTaskType riskPlanActionTaskType = new RiskPlanActionTaskType();
			riskPlanActionTypeService.update(riskPlanActionTaskType);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testDelete() throws Exception{
		RiskPlanActionTaskType riskPlanActionTaskType = new RiskPlanActionTaskType();
		given(this.riskPlanActionTaskTypeRepository.findOne(1l)).willReturn(riskPlanActionTaskType);
		riskPlanActionTypeService.delete(1l);
	}
	
	@Test
	public void testDeleteWithNull() throws Exception{
		try{
			riskPlanActionTypeService.delete(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindById() throws Exception{
		RiskPlanActionTaskType riskPlanActionTaskType = new RiskPlanActionTaskType();
		given(this.riskPlanActionTaskTypeRepository.findOne(1l)).willReturn(riskPlanActionTaskType);
		riskPlanActionTypeService.findById(1l);
	}
	@Test
	public void testFindByIdWithNull() throws Exception{
		try{
			riskPlanActionTypeService.findById(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindAll() throws Exception{
		riskPlanActionTypeService.findAll();
	}
	
}
