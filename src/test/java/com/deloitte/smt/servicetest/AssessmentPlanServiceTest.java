package com.deloitte.smt.servicetest;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.Comments;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.SignalURL;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.entity.TopicRiskPlanAssignmentAssignees;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.TopicRiskPlanAssignmentAssigneesRepository;
import com.deloitte.smt.service.AssessmentPlanService;
import com.deloitte.smt.util.TestUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class AssessmentPlanServiceTest {
	
	private static final Logger LOG = Logger.getLogger(AssessmentPlanServiceTest.class);
	
	@Autowired
	private AssessmentPlanService assessmentPlanService;
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@MockBean
    AssessmentPlanRepository assessmentPlanRepository;
	
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
	public void testFindOne() throws Exception{
		AssessmentPlan assessmentPlan = new AssessmentPlan();
		assessmentPlan.setAssessmentName("Test Case 1");
		given(this.assessmentPlanRepository.findOne(121l)).willReturn(assessmentPlan);
		assessmentPlanService.findById(121l);
	}
	
	@Test
	public void testFindOneWithNewStatus() {
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
	public void testFindOneWithNull() {
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
	
	@Test
	public void testUpdateAssessment() throws Exception{
		AssessmentPlan assessmentPlan = new AssessmentPlan();
		Set<Topic> topics = new HashSet<>();
		Topic topic = new Topic();
		topic.setId(100l);
		topic.setName("Test Topic 1");
		topics.add(topic);
		assessmentPlan.setTopics(topics);
		assessmentPlan.setAssessmentName("Test Assessment 1");
		List<Comments> list = new ArrayList<>();
		Comments comments = new Comments();
		comments.setId(1l);
		comments.setUserComments("Test Comments");
		list.add(comments);
		List<SignalURL> urls = new ArrayList<>();
		SignalURL url = new SignalURL();
		url.setUrl("www.test.com");
		url.setDescription("test description");
		urls.add(url);
		assessmentPlan.setSignalUrls(urls);
		assessmentPlan.setComments(list);
		assessmentPlan.setId(11l);
		assessmentPlanService.updateAssessment(assessmentPlan, null);
	}
	
	@Test
	public void testUpdateAssessmentWithNull() {
		AssessmentPlan assessmentPlan = new AssessmentPlan();
		try{
			assessmentPlanService.updateAssessment(assessmentPlan, null);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFinalAssessmentWithNull() {
		AssessmentPlan assessmentPlan = new AssessmentPlan();
		try{
			assessmentPlanService.finalAssessment(assessmentPlan, null);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFinalAssessment() throws Exception{
		AssessmentPlan assessmentPlan = new AssessmentPlan();
		assessmentPlan.setId(1l);
		assessmentPlanService.finalAssessment(assessmentPlan, null);
	}
	
	@Test
	public void testFindAllAssessmentPlansNoDueDateAndNoGantt() {
		try{
			assessmentPlanService.findAllAssessmentPlans(TestUtil.buildSearchDto(false, false));
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindAllAssessmentPlansWithDueDateAndNoGantt() {
		try{
			assessmentPlanService.findAllAssessmentPlans(TestUtil.buildSearchDto(true, false));
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindAllAssessmentPlansNoDueDateAndWithGantt() {
		try{
			assessmentPlanService.findAllAssessmentPlans(TestUtil.buildSearchDto(false, true));
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindAllAssessmentPlansNull() {
		try{
			assessmentPlanService.findAllAssessmentPlans(null);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	
	@Test
	public void testFindAllAssessmentPlansWithDueDateAndWithGantt() {
		try{
			assessmentPlanService.findAllAssessmentPlans(TestUtil.buildSearchDto(true, true));
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testAssociateAssignees() {
		try{
			List<AssessmentPlan> assessmentPlanList = new ArrayList<>();
			List<TopicRiskPlanAssignmentAssignees> trList = new ArrayList<>();
			TopicRiskPlanAssignmentAssignees assignee = new TopicRiskPlanAssignmentAssignees();
			trList.add(assignee);
			RiskPlan riskPlan= new RiskPlan();
			AssessmentPlan assessmentPlan = new AssessmentPlan();
			assessmentPlan.setAssessmentName("Test Case 1");
			assessmentPlan.setAssessmentPlanStatus("New");
			assessmentPlan.setId(1l);
			assessmentPlan.setRiskPlan(riskPlan);
			assessmentPlanList.add(assessmentPlan);
			given(this.topicRiskPlanAssignmentAssigneesRepository.findByRiskId(1l)).willReturn(trList);
			assessmentPlanService.associateAssignees(assessmentPlanList);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	@Test
	public void testUpdateAssessmentName(){
		AssessmentPlan assessmentPlan = new AssessmentPlan();
		assessmentPlan.setId(1l);
		assessmentPlan.setAssessmentName("testAssessmentName");
		List<AssessmentPlan> assessmentPlanList=new ArrayList<>();
		assessmentPlanList.add(assessmentPlan);
		try {
			given(this.assessmentPlanRepository.findAll()).willReturn(assessmentPlanList);
			assessmentPlanService.updateAssessmentName(1l, "testAssessmentName1");
		} catch (ApplicationException ex) {
			LOG.info(ex);
		}
	}
	@Test(expected = ApplicationException.class)
	public void testUpdateDuplicateAssessmentName() throws ApplicationException{
		AssessmentPlan assessmentPlan = new AssessmentPlan();
		assessmentPlan.setId(1l);
		assessmentPlan.setAssessmentName("testAssessmentName");
		List<AssessmentPlan> assessmentPlanList=new ArrayList<>();
		assessmentPlanList.add(assessmentPlan);
		List<String> assesmentNames=new ArrayList<>();
		assesmentNames.add("testAssessmentName");
			given(this.assessmentPlanRepository.findAll()).willReturn(assessmentPlanList);
			given(this.assessmentPlanRepository.findByAssessmentName("testAssessmentName", 1l)).willReturn(assesmentNames);
			assessmentPlanService.updateAssessmentName(1l, "testAssessmentName");
	}

}
