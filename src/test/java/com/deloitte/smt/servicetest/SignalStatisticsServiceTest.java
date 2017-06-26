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
import com.deloitte.smt.entity.SignalStatistics;
import com.deloitte.smt.repository.SignalStatisticsRepository;
import com.deloitte.smt.service.SignalStatisticsService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class SignalStatisticsServiceTest {
	
	private static final Logger LOG = Logger.getLogger(SignalStatisticsServiceTest.class);
	
	@Autowired
	private SignalStatisticsService signalStatisticsService;
	
	@MockBean
	SignalStatisticsRepository signalStatisticsRepository;
	
		
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
		LOG.info("testInsert");
		List<SignalStatistics> signalStatistics = new ArrayList<>();
		given(this.signalStatisticsRepository.save(signalStatistics)).willReturn(signalStatistics);
		signalStatisticsService.insert(signalStatistics);
	}
	
	@Test
	public void testFindById() throws Exception{
		given(this.signalStatisticsRepository.findByRunInstanceId(1l)).willReturn(null);
		signalStatisticsService.findSignalStatisticsByRunInstanceId(1l);
	}
	
}
