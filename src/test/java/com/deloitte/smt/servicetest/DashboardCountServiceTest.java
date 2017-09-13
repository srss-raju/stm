package com.deloitte.smt.servicetest;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.service.DashboardCountService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class DashboardCountServiceTest {
	
	private static final Logger LOG = Logger.getLogger(DashboardCountServiceTest.class);
	
	@Autowired
	private DashboardCountService dashboardCountService;
	
	@PersistenceContext
	private EntityManager entityManager;
	
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
	public void testGetValidateAndPrioritizeCount() throws Exception {
		LOG.info("getValidateAndPrioritizeCount");
		List<Long> userKeys = new ArrayList<>();
		userKeys.add(1L);
		List<Long> userGroupKeys = new ArrayList<>();
		userGroupKeys.add(1L);
		dashboardCountService.getValidateAndPrioritizeCount("1", userKeys, userGroupKeys);
	}
	
	@Test
	public void testGetAssessmentCount() throws Exception {
		LOG.info("getAssessmentCount");
		List<Long> userKeys = new ArrayList<>();
		userKeys.add(1L);
		List<Long> userGroupKeys = new ArrayList<>();
		userGroupKeys.add(1L);
		dashboardCountService.getAssessmentCount("1", userKeys, userGroupKeys);
	}
	
	@Test
	public void testGetRiskCount() throws Exception {
		LOG.info("getRiskCount");
		List<Long> userKeys = new ArrayList<>();
		userKeys.add(1L);
		List<Long> userGroupKeys = new ArrayList<>();
		userGroupKeys.add(1L);
		dashboardCountService.getRiskCount("1", userKeys, userGroupKeys);
	}
	
}
