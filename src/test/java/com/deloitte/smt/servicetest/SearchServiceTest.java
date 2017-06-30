package com.deloitte.smt.servicetest;

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
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.HlgtRepository;
import com.deloitte.smt.repository.HltRepository;
import com.deloitte.smt.repository.IngredientRepository;
import com.deloitte.smt.repository.LicenseRepository;
import com.deloitte.smt.repository.MeetingRepository;
import com.deloitte.smt.repository.ProductRepository;
import com.deloitte.smt.repository.PtRepository;
import com.deloitte.smt.repository.RiskPlanRepository;
import com.deloitte.smt.repository.SocRepository;
import com.deloitte.smt.repository.TopicRepository;
import com.deloitte.smt.service.SearchService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class SearchServiceTest {
	
	@Autowired
	private SearchService searchService;
	@MockBean
	MeetingRepository meetingRepository;
	@MockBean
    IngredientRepository ingredientRepository;
	@MockBean
    ProductRepository productRepository;
	@MockBean
    LicenseRepository licenseRepository;
	@MockBean
    SocRepository socRepository;
	@MockBean
    HlgtRepository hlgtRepository;
	@MockBean
    HltRepository hltRepository;
	@MockBean
    PtRepository ptRepository;
	@MockBean
    AssessmentPlanRepository assessmentPlanRepository;
	@MockBean
    RiskPlanRepository riskPlanRepository;
	@MockBean
    TopicRepository topicRepository;
	
		
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
	public void testGetFiltersForSignal() {
		searchService.getFiltersForSignal();
	}
	
}
