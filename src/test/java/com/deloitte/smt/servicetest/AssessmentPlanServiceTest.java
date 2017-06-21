package com.deloitte.smt.servicetest;

import static org.mockito.BDDMockito.given;

import java.util.HashSet;
import java.util.Set;

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
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.service.AssessmentPlanService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class AssessmentPlanServiceTest {
	
	private static final Logger LOG = Logger.getLogger(AssessmentPlanServiceTest.class);
	
	@Autowired
	private AssessmentPlanService assessmentPlanService;
	
	@MockBean
    AssessmentPlanRepository assessmentPlanRepository;

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
	public void testFindOne() throws Exception{
		AssessmentPlan assessmentPlan = new AssessmentPlan();
		assessmentPlan.setAssessmentName("Test Case 1");
		given(this.assessmentPlanRepository.findOne(121l)).willReturn(assessmentPlan);
		assessmentPlanService.findById(121l);
	}
	
	@Test
	public void testFindOneWithNewStatus() throws Exception{
		AssessmentPlan assessmentPlan = new AssessmentPlan();
		assessmentPlan.setAssessmentName("Test Case 1");
		assessmentPlan.setAssessmentPlanStatus("New");
		given(this.assessmentPlanRepository.findOne(121l)).willReturn(assessmentPlan);
		try{
			assessmentPlanService.findById(121l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindOneWithNull() throws Exception{
		try{
			assessmentPlanService.findById(121l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testUnlinkSignalToAssessment() throws Exception{
		AssessmentPlan assessmentPlan = new AssessmentPlan();
		Set<Topic> topics = new HashSet<>();
		Topic topic = new Topic();
		topic.setId(100l);
		topic.setName("Test Topic 1");
		topics.add(topic);
		assessmentPlan.setTopics(topics);
		assessmentPlan.setAssessmentName("Test Assessment 1");
		given(this.assessmentPlanRepository.findOne(121l)).willReturn(assessmentPlan);
		assessmentPlanService.unlinkSignalToAssessment(121l,100l);
	}

}
