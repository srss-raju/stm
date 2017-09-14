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
import com.deloitte.smt.entity.AssessmentAssignmentAssignees;
import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.RiskPlanAssignmentAssignees;
import com.deloitte.smt.entity.TopicRiskPlanAssignmentAssignees;
import com.deloitte.smt.repository.RiskPlanAssignmentAssigneesRepository;
import com.deloitte.smt.repository.TopicRiskPlanAssignmentAssigneesRepository;
import com.deloitte.smt.service.RiskPlanAssignmentService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class RiskPlanAssignmentServiceTest {
	
	private static final Logger LOG = Logger.getLogger(MeetingServiceTest.class);
	
	@Autowired
	private RiskPlanAssignmentService riskPlanAssignmentService;
	
	@MockBean
	RiskPlanAssignmentAssigneesRepository riskPlanAssignmentAssigneesRepository;
	    
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
	public void testSaveAssignmentAssignees() {
		try{
			AssignmentConfiguration assignmentConfiguration = new AssignmentConfiguration();
			RiskPlan riskPlan = new RiskPlan();
			List<RiskPlanAssignmentAssignees>  assignees = new ArrayList<>();
			assignmentConfiguration.setId(1l);
			RiskPlanAssignmentAssignees assignee = new RiskPlanAssignmentAssignees();
			assignees.add(assignee);
			assignmentConfiguration.setRiskPlanAssignmentAssignees(assignees);
			given(this.riskPlanAssignmentAssigneesRepository.findByAssignmentConfigurationId(1l)).willReturn(assignees);
			riskPlanAssignmentService.saveAssignmentAssignees(assignmentConfiguration, riskPlan);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindRiskAssignmentAssignees() {
		try{
			AssignmentConfiguration assignmentConfiguration = new AssignmentConfiguration();
			RiskPlan riskPlan = new RiskPlan();
			List<AssessmentAssignmentAssignees> assignees = new ArrayList<>();
			assignmentConfiguration.setId(1l);
			AssessmentAssignmentAssignees assignee = new AssessmentAssignmentAssignees();
			assignees.add(assignee);
			assignmentConfiguration.setAssessmentAssignmentAssignees(assignees);
			List<TopicRiskPlanAssignmentAssignees>  topicRiskPlanAssignmentAssignees = new ArrayList<>();
			given(this.topicRiskPlanAssignmentAssigneesRepository.findByRiskId(1l)).willReturn(topicRiskPlanAssignmentAssignees);
			riskPlanAssignmentService.findRiskAssignmentAssignees(riskPlan);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
}
