package com.deloitte.smt.servicetest;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.camunda.bpm.engine.CaseService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.TaskService;
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
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.SignalURL;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.AssignmentConfigurationRepository;
import com.deloitte.smt.repository.CommentsRepository;
import com.deloitte.smt.repository.IngredientRepository;
import com.deloitte.smt.repository.LicenseRepository;
import com.deloitte.smt.repository.MeetingRepository;
import com.deloitte.smt.repository.ProductRepository;
import com.deloitte.smt.repository.RiskPlanRepository;
import com.deloitte.smt.repository.RiskTaskRepository;
import com.deloitte.smt.repository.SignalURLRepository;
import com.deloitte.smt.repository.TaskInstRepository;
import com.deloitte.smt.service.AttachmentService;
import com.deloitte.smt.service.RiskPlanService;
import com.deloitte.smt.service.SearchService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class RiskPlanServiceTest {
	
	private static final Logger LOG = Logger.getLogger(RiskPlanServiceTest.class);
	
	@Autowired
	private RiskPlanService riskPlanService;
	
	@MockBean
	MeetingRepository meetingRepository;

	@MockBean
	AttachmentService attachmentService;
	
	@MockBean
	AssessmentPlanRepository assessmentPlanRepository;

	@MockBean
	RiskPlanRepository riskPlanRepository;

	@MockBean
	RiskTaskRepository riskTaskRepository;

	@Autowired
	private TaskService taskService;

	@MockBean
	TaskInstRepository taskInstRepository;

	@Autowired
	CaseService caseService;

	@MockBean
	ProductRepository productRepository;

	@MockBean
	LicenseRepository licenseRepository;

	@MockBean
	IngredientRepository ingredientRepository;

	@Autowired
	SearchService searchService;

	@PersistenceContext
	private EntityManager entityManager;

	@MockBean
	CommentsRepository commentsRepository;

	@MockBean
	SignalURLRepository signalURLRepository;

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
	public void testInsertWithAssignmentConfiguration() {
		try{
			RiskPlan riskPlan = new RiskPlan();
			riskPlan.setSource("Test Source");
			
			AssignmentConfiguration assignmentConfiguration = new AssignmentConfiguration();
			given(this.assignmentConfigurationRepository.findByIngredientAndSignalSource(riskPlan.getIngredient(), riskPlan.getSource())).willReturn(assignmentConfiguration);
			Long assessmentId = 1l;
			List<SignalURL> urls = new ArrayList<>();
			SignalURL url = new SignalURL();
			urls.add(url);
			AssessmentPlan assessmentPlan = new AssessmentPlan();
			given(this.assessmentPlanRepository.findOne(assessmentId)).willReturn(assessmentPlan);
			RiskPlan riskPlanUpdated = new RiskPlan();
			riskPlanUpdated.setSignalUrls(urls);
			given(this.riskPlanRepository.save(riskPlan)).willReturn(riskPlanUpdated);
			riskPlanService.insert(riskPlan, null, assessmentId);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testInsertWithAssignmentConfigurationNull() {
		try{
			RiskPlan riskPlan = new RiskPlan();
			riskPlan.setSource("Test Source");
			Long assessmentId = 1l;
			List<SignalURL> urls = new ArrayList<>();
			SignalURL url = new SignalURL();
			urls.add(url);
			AssessmentPlan assessmentPlan = new AssessmentPlan();
			given(this.assessmentPlanRepository.findOne(assessmentId)).willReturn(assessmentPlan);
			RiskPlan riskPlanUpdated = new RiskPlan();
			riskPlanUpdated.setSignalUrls(urls);
			given(this.riskPlanRepository.save(riskPlan)).willReturn(riskPlanUpdated);
			riskPlanService.insert(riskPlan, null, assessmentId);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testInsertWithAssessmentIdNull() {
		try{
			RiskPlan riskPlan = new RiskPlan();
			riskPlan.setSource("Test Source");
			AssignmentConfiguration assignmentConfiguration = new AssignmentConfiguration();
			given(this.assignmentConfigurationRepository.findByIngredientAndSignalSource(riskPlan.getIngredient(), riskPlan.getSource())).willReturn(assignmentConfiguration);
			List<SignalURL> urls = new ArrayList<>();
			SignalURL url = new SignalURL();
			urls.add(url);
			RiskPlan riskPlanUpdated = new RiskPlan();
			riskPlanUpdated.setSignalUrls(urls);
			given(this.riskPlanRepository.save(riskPlan)).willReturn(riskPlanUpdated);
			riskPlanService.insert(riskPlan, null, null);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testInsertWithAssessmentPlanNull() {
		try{
			RiskPlan riskPlan = new RiskPlan();
			riskPlan.setSource("Test Source");
			AssignmentConfiguration assignmentConfiguration = new AssignmentConfiguration();
			given(this.assignmentConfigurationRepository.findByIngredientAndSignalSource(riskPlan.getIngredient(), riskPlan.getSource())).willReturn(assignmentConfiguration);
			Long assessmentId = 1l;
			List<SignalURL> urls = new ArrayList<>();
			SignalURL url = new SignalURL();
			urls.add(url);
			RiskPlan riskPlanUpdated = new RiskPlan();
			riskPlanUpdated.setSignalUrls(urls);
			given(this.riskPlanRepository.save(riskPlan)).willReturn(riskPlanUpdated);
			riskPlanService.insert(riskPlan, null, assessmentId);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
}
