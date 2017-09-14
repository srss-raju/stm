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
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.entity.TopicAssessmentAssignmentAssignees;
import com.deloitte.smt.repository.AssessmentAssignmentAssigneesRepository;
import com.deloitte.smt.repository.TopicAssessmentAssignmentAssigneesRepository;
import com.deloitte.smt.service.AssessmentAssignmentService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class AssessmentAssignmentServiceTest {
	
	private static final Logger LOG = Logger.getLogger(MeetingServiceTest.class);
	
	@Autowired
	private AssessmentAssignmentService assessmentAssignmentService;
	
	@MockBean
	AssessmentAssignmentAssigneesRepository assessmentAssignmentAssigneesRepository;
	    
	@MockBean
	TopicAssessmentAssignmentAssigneesRepository topicAssessmentAssignmentAssigneesRepository;
	
		
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
			AssessmentPlan assessmentPlan = new AssessmentPlan();
			List<AssessmentAssignmentAssignees> assignees = new ArrayList<>();
			assignmentConfiguration.setId(1l);
			AssessmentAssignmentAssignees assignee = new AssessmentAssignmentAssignees();
			assignees.add(assignee);
			assignmentConfiguration.setAssessmentAssignmentAssignees(assignees);
			given(this.assessmentAssignmentAssigneesRepository.findByAssignmentConfigurationId(1l)).willReturn(assignees);
			assessmentAssignmentService.saveAssignmentAssignees(assignmentConfiguration, assessmentPlan);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindAssignmentAssignees() {
		try{
			AssignmentConfiguration assignmentConfiguration = new AssignmentConfiguration();
			AssessmentPlan assessmentPlan = new AssessmentPlan();
			List<AssessmentAssignmentAssignees> assignees = new ArrayList<>();
			assignmentConfiguration.setId(1l);
			AssessmentAssignmentAssignees assignee = new AssessmentAssignmentAssignees();
			assignees.add(assignee);
			assignmentConfiguration.setAssessmentAssignmentAssignees(assignees);
			List<TopicAssessmentAssignmentAssignees>  topicAssessmentAssignmentAssignees = new ArrayList<>();
			given(this.topicAssessmentAssignmentAssigneesRepository.findByAssessmentId(1l)).willReturn(topicAssessmentAssignmentAssignees);
			assessmentAssignmentService.findAssignmentAssignees(assessmentPlan);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
}
