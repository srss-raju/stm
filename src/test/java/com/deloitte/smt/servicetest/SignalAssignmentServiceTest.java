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
import com.deloitte.smt.entity.SignalValidationAssignmentAssignees;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.repository.AssessmentAssignmentAssigneesRepository;
import com.deloitte.smt.repository.RiskPlanAssignmentAssigneesRepository;
import com.deloitte.smt.repository.SignalValidationAssignmentAssigneesRepository;
import com.deloitte.smt.repository.TopicAssessmentAssignmentAssigneesRepository;
import com.deloitte.smt.repository.TopicRiskPlanAssignmentAssigneesRepository;
import com.deloitte.smt.repository.TopicSignalValidationAssignmentAssigneesRepository;
import com.deloitte.smt.service.SignalAssignmentService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class SignalAssignmentServiceTest {
	
	private static final Logger LOG = Logger.getLogger(MeetingServiceTest.class);
	
	@Autowired
	private SignalAssignmentService signalAssignmentService;
	
	@MockBean
    SignalValidationAssignmentAssigneesRepository signalValidationAssignmentAssigneesRepository;
    
	@MockBean
    AssessmentAssignmentAssigneesRepository assessmentAssignmentAssigneesRepository;
    
	@MockBean
    RiskPlanAssignmentAssigneesRepository riskPlanAssignmentAssigneesRepository;
    
	@MockBean
    TopicSignalValidationAssignmentAssigneesRepository topicSignalValidationAssignmentAssigneesRepository;
    
	@MockBean
    TopicAssessmentAssignmentAssigneesRepository topicAssessmentAssignmentAssigneesRepository;
    
	@MockBean
    TopicRiskPlanAssignmentAssigneesRepository topicRiskPlanAssignmentAssigneesRepository;
	
		
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
	public void testSaveSignalAssignmentAssignees() {
		try{
			AssignmentConfiguration assignmentConfiguration = new AssignmentConfiguration();
			Topic topicUpdated = new Topic();
			List<SignalValidationAssignmentAssignees> signalValidationAssignmentAssignees = new ArrayList<>();
			assignmentConfiguration.setSignalValidationAssignmentAssignees(signalValidationAssignmentAssignees);
			assignmentConfiguration.setId(1l);
			SignalValidationAssignmentAssignees assignee = new SignalValidationAssignmentAssignees();
			signalValidationAssignmentAssignees.add(assignee);
			given(this.signalValidationAssignmentAssigneesRepository.findByAssignmentConfigurationId(1l)).willReturn(signalValidationAssignmentAssignees);
			signalAssignmentService.saveSignalAssignmentAssignees(assignmentConfiguration, topicUpdated);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindSignalAssignmentAssignees() {
		try{
			AssignmentConfiguration assignmentConfiguration = new AssignmentConfiguration();
			Topic topicUpdated = new Topic();
			List<SignalValidationAssignmentAssignees> signalValidationAssignmentAssignees = new ArrayList<>();
			assignmentConfiguration.setSignalValidationAssignmentAssignees(signalValidationAssignmentAssignees);
			assignmentConfiguration.setId(1l);
			SignalValidationAssignmentAssignees assignee = new SignalValidationAssignmentAssignees();
			signalValidationAssignmentAssignees.add(assignee);
			given(this.signalValidationAssignmentAssigneesRepository.findByAssignmentConfigurationId(1l)).willReturn(signalValidationAssignmentAssignees);
			signalAssignmentService.findSignalAssignmentAssignees(topicUpdated);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
}
