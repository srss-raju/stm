package com.deloitte.smt.servicetest;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.camunda.bpm.engine.CaseService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.camunda.bpm.engine.runtime.CaseInstance;
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
import com.deloitte.smt.entity.SignalAction;
import com.deloitte.smt.entity.SignalURL;
import com.deloitte.smt.repository.AssessmentActionRepository;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.SignalURLRepository;
import com.deloitte.smt.repository.TaskInstRepository;
import com.deloitte.smt.service.AssessmentActionService;
import com.deloitte.smt.service.AttachmentService;
import com.deloitte.smt.util.TestUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class AssessmentActionServiceTest {
	
	private static final Logger LOG = Logger.getLogger(AssessmentActionServiceTest.class);
	
	@Autowired
	private AssessmentActionService assessmentActionService;
	
	@Autowired
	CaseService caseService;
	
	@MockBean
    TaskInstRepository taskInstRepository;

	@MockBean
    AssessmentActionRepository assessmentActionRepository;

	@MockBean
    AttachmentService attachmentService;
    
	@MockBean
    AssessmentPlanRepository assessmentPlanRepository;
    
	@MockBean
    SignalURLRepository signalURLRepository;
	
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
	public void testCreateAssessmentAction() {
		try{
			SignalAction signalAction = TestUtil.buildSignalAction();
			CaseInstance instance = caseService.createCaseInstanceByKey("assesmentCaseId");
			signalAction.setCaseInstanceId(instance.getCaseInstanceId());
			signalAction.setActionStatus("Completed");
			List<SignalURL> urls = new ArrayList<>();
			SignalURL url = new SignalURL();
			urls.add(url);
			signalAction.setSignalUrls(urls);
			given(this.assessmentActionRepository.save(signalAction)).willReturn(signalAction);
			assessmentActionService.createAssessmentAction(signalAction, null);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testCreateAssessmentActionNoURL() {
		try{
			SignalAction signalAction = TestUtil.buildSignalAction();
			CaseInstance instance = caseService.createCaseInstanceByKey("assesmentCaseId");
			signalAction.setCaseInstanceId(instance.getCaseInstanceId());
			signalAction.setActionStatus("Completed");
			signalAction.setAssessmentId("1");
			List<SignalAction> signalActions = new ArrayList<>();
			signalActions.add(signalAction);
			
			given(this.assessmentActionRepository.save(signalAction)).willReturn(signalAction);
			given(this.assessmentActionRepository.findAllByAssessmentId("1")).willReturn(signalActions);
			assessmentActionService.createAssessmentAction(signalAction, null);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testCreateAssessmentActionNoURL2() {
		try{
			SignalAction signalAction = TestUtil.buildSignalAction();
			CaseInstance instance = caseService.createCaseInstanceByKey("assesmentCaseId");
			signalAction.setCaseInstanceId(instance.getCaseInstanceId());
			signalAction.setActionStatus("Completed");
			signalAction.setAssessmentId("1");
			List<SignalAction> signalActions = new ArrayList<>();
			signalActions.add(signalAction);
			
			given(this.assessmentActionRepository.save(signalAction)).willReturn(signalAction);
			given(this.assessmentActionRepository.findAllByAssessmentId("1")).willReturn(null);
			assessmentActionService.createAssessmentAction(signalAction, null);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testCreateAssessmentActionexists() {
		try{
			SignalAction signalAction = TestUtil.buildSignalAction();
			CaseInstance instance = caseService.createCaseInstanceByKey("assesmentCaseId");
			signalAction.setCaseInstanceId(instance.getCaseInstanceId());
			signalAction.setActionStatus("Completed");
			signalAction.setActionName("A");
			signalAction.setAssessmentId("1");
			List<SignalURL> urls = new ArrayList<>();
			SignalURL url = new SignalURL();
			urls.add(url);
			signalAction.setSignalUrls(urls);
			given(this.assessmentActionRepository.countByActionNameIgnoreCaseAndAssessmentId("A", "1")).willReturn(1l);
			assessmentActionService.createAssessmentAction(signalAction, null);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testUpdateAssessmentActionWithNull() {
		try{
			SignalAction signalAction = new SignalAction();
			assessmentActionService.updateAssessmentAction(signalAction, null);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testUpdateAssessmentAction() {
		try{
			SignalAction signalAction = TestUtil.buildSignalAction();
			CaseInstance instance = caseService.createCaseInstanceByKey("assesmentCaseId");
			signalAction.setCaseInstanceId(instance.getCaseInstanceId());
			signalAction.setActionStatus("QCompleted");
			List<SignalURL> urls = new ArrayList<>();
			SignalURL url = new SignalURL();
			urls.add(url);
			signalAction.setSignalUrls(urls);
			signalAction.setId(1l);
			List<SignalAction> list = new ArrayList<>();
			signalAction.setAssessmentId("1");
			signalAction.setTaskId("1");
			list.add(signalAction);
			given(this.assessmentActionRepository.findAllByAssessmentId("1")).willReturn(list);
			assessmentActionService.updateAssessmentAction(signalAction, null);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testUpdateAssessmentActionTaskCompleted() {
		try{
			SignalAction signalAction = new SignalAction();
			signalAction.setAssessmentId("1");
			signalAction.setId(1l);
			AssessmentPlan plan = new AssessmentPlan();
			given(this.assessmentPlanRepository.findOne(Long.valueOf("1"))).willReturn(plan);
			assessmentActionService.updateAssessmentAction(signalAction, null);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindById() {
		try{
			SignalAction signalAction = new SignalAction();
			signalAction.setActionStatus("New");
			signalAction.setId(1l);
			List<SignalURL> urls = new ArrayList<>();
			SignalURL url = new SignalURL();
			urls.add(url);
			given(this.assessmentActionRepository.findOne(signalAction.getId())).willReturn(signalAction);
			given(this.assessmentActionRepository.save(signalAction)).willReturn(signalAction);
			given(this.signalURLRepository.findByTopicId(signalAction.getId())).willReturn(urls);
			assessmentActionService.findById(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindAllByAssessmentId() {
		try{
			SignalAction signalAction = new SignalAction();
			signalAction.setActionStatus("New");
			signalAction.setId(1l);
			List<SignalAction> list = new ArrayList<>();
			list.add(signalAction);
			given(this.assessmentActionRepository.findAllByAssessmentId("1")).willReturn(list);
			assessmentActionService.findAllByAssessmentId("1",null);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindAllByAssessmentIdWithStatus() {
		try{
			SignalAction signalAction = new SignalAction();
			signalAction.setActionStatus("New");
			signalAction.setId(1l);
			List<SignalAction> list = new ArrayList<>();
			list.add(signalAction);
			given(this.assessmentActionRepository.findAllByAssessmentIdAndActionStatus("1","completed")).willReturn(list);
			assessmentActionService.findAllByAssessmentId("1","completed");
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testDelete() throws Exception{
		try{
			SignalAction signalAction = new SignalAction();
			given(this.assessmentActionRepository.findOne(1l)).willReturn(signalAction);
			given(this.assessmentActionRepository.save(signalAction)).willReturn(signalAction);
			given(this.assessmentActionRepository.findAllByAssessmentId("1")).willReturn(null);
			assessmentActionService.delete(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testDeleteWithNull() {
		try{
			assessmentActionService.delete(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testCreateOrphanAssessmentAction() {
		try{
			SignalAction signalAction = TestUtil.buildSignalAction();
			signalAction.setDaysLeft(1);
			signalAction.setCreatedDate(new Date());
			List<SignalURL> urls = new ArrayList<>();
			SignalURL url = new SignalURL();
			urls.add(url);
			signalAction.setSignalUrls(urls);
			given(this.assessmentActionRepository.save(signalAction)).willReturn(signalAction);
			assessmentActionService.createOrphanAssessmentAction(signalAction, null);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}

}
