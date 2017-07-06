package com.deloitte.smt.servicetest;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
import com.deloitte.smt.constant.DateKeyType;
import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.entity.Comments;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.RiskTask;
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

	@MockBean
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
	
	@Test
	public void testFindAllRiskPlansForSearch2DueDate() {
		try{
			SearchDto searchDto = new SearchDto();
			getSearchDto(searchDto);
			searchDto.setDateKey(DateKeyType.DUEDATE.name());
			riskPlanService.findAllRiskPlansForSearch(searchDto);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}

	@Test
	public void testFindAllRiskPlansForSearch2CreatedDate() {
		try{
			SearchDto searchDto = new SearchDto();
			getSearchDto(searchDto);
			searchDto.setDateKey(DateKeyType.CREATED.name());
			riskPlanService.findAllRiskPlansForSearch(searchDto);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindAllRiskPlansForSearchNull() {
		try{
			riskPlanService.findAllRiskPlansForSearch(null);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindAllRiskPlansForSearchAllFalse() {
		try{
			SearchDto searchDto = new SearchDto();
			getSearchDto(searchDto);
			searchDto.setDateKey(DateKeyType.CREATED.name());
			List<Long> riskTopicIds = new ArrayList<>();
			riskTopicIds.add(1l);
			given(this.searchService.getSignalIdsForSearch(searchDto, riskTopicIds, true)).willReturn(true);
			riskPlanService.findAllRiskPlansForSearch(searchDto);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testCreateRiskTask() {
		try{
			RiskPlan riskPlan = new RiskPlan();
			RiskTask riskTask = new RiskTask();
			riskTask.setCaseInstanceId("11");
			riskTask.setStatus("Completed1");
			riskTask.setId(1l);
			riskTask.setRiskId("1");
			given(this. riskPlanRepository.findOne(1l)).willReturn(riskPlan);
			RiskTask riskTaskUpdated = new RiskTask();
			List<SignalURL> urls = new ArrayList<>();
			SignalURL url = new SignalURL();
			urls.add(url);
			riskTaskUpdated.setSignalUrls(urls);
			given(this. riskTaskRepository.save(riskTask)).willReturn(riskTaskUpdated);
			riskPlanService.createRiskTask(riskTask, null);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testCreateRiskTaskCompleted() {
		try{
			RiskPlan riskPlan = new RiskPlan();
			RiskTask riskTask = new RiskTask();
			CaseInstance instance = caseService.createCaseInstanceByKey("riskCaseId");
			riskPlan.setCaseInstanceId(instance.getCaseInstanceId());
			riskTask.setCaseInstanceId(instance.getCaseInstanceId());
			riskTask.setStatus("Completed");
			riskTask.setId(1l);
			riskTask.setRiskId("1");
			given(this. riskPlanRepository.findOne(1l)).willReturn(riskPlan);
			RiskTask riskTaskUpdated = new RiskTask();
			List<SignalURL> urls = new ArrayList<>();
			SignalURL url = new SignalURL();
			urls.add(url);
			riskTaskUpdated.setSignalUrls(urls);
			given(this. riskTaskRepository.save(riskTask)).willReturn(riskTaskUpdated);
			riskPlanService.createRiskTask(riskTask, null);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindById() {
		try{
			RiskTask riskTask = new RiskTask();
			riskTask.setId(1l);
			riskTask.setStatus("New");
			given(this.riskTaskRepository.findOne(1l)).willReturn(riskTask);
			given(this.riskTaskRepository.save(riskTask)).willReturn(riskTask);
			riskPlanService.findById(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindAllByRiskIdStatusNull() {
		try{
			List<RiskTask> list = new ArrayList<>();
			given(this.riskTaskRepository.findAllByRiskIdOrderByCreatedDateDesc("1")).willReturn(list);
			riskPlanService.findAllByRiskId("1", null);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindAllByRiskIdStatus() {
		try{
			List<RiskTask> list = new ArrayList<>();
			given(this.riskTaskRepository.findAllByRiskIdAndStatusOrderByCreatedDateDesc("1", "Completed")).willReturn(list);
			riskPlanService.findAllByRiskId("1", "Completed");
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testDeleteWithNull() {
		try{
			RiskTask riskTask = new RiskTask();
			given(this.riskTaskRepository.findOne(1l)).willReturn(riskTask);
			riskPlanService.delete(1l, "1");
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testDelete() {
		try{
			riskPlanService.delete(1l, "1");
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testUpdateRiskTask() {
		try{
			RiskTask riskTask = new RiskTask();
			riskTask.setCaseInstanceId("11");
			riskTask.setStatus("Completed1");
			riskTask.setId(1l);
			riskTask.setRiskId("1");
			List<SignalURL> urls = new ArrayList<>();
			SignalURL url = new SignalURL();
			urls.add(url);
			riskTask.setSignalUrls(urls);
			riskPlanService.updateRiskTask(riskTask, null);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testUpdateRiskTaskClosed() {
		try{
			RiskTask riskTask = new RiskTask();
			riskTask.setCaseInstanceId("11");
			riskTask.setStatus("Completed1");
			riskTask.setId(1l);
			riskTask.setRiskId("1");
			List<SignalURL> urls = new ArrayList<>();
			SignalURL url = new SignalURL();
			urls.add(url);
			riskTask.setSignalUrls(urls);
			List<RiskTask> list = new ArrayList<>();
			list.add(riskTask);
			given(this.riskTaskRepository.findAllByRiskIdOrderByCreatedDateDesc("1")).willReturn(list);
			riskPlanService.updateRiskTask(riskTask, null);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindByRiskId() {
		try{
			RiskPlan riskPlan = new RiskPlan();
			riskPlan.setId(1l);
			riskPlan.setStatus("New");
			given(this.riskPlanRepository.findOne(1l)).willReturn(riskPlan);
			given(this.riskPlanRepository.save(riskPlan)).willReturn(riskPlan);
			riskPlanService.findByRiskId(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindByRiskIdForNull() {
		try{
			riskPlanService.findByRiskId(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testRiskPlanSummary() {
		try{
			RiskPlan riskPlan = new RiskPlan();
			riskPlan.setId(1l);
			riskPlanService.riskPlanSummary(riskPlan, null);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testRiskPlanSummaryWithNull() {
		try{
			RiskPlan riskPlan = new RiskPlan();
			riskPlanService.riskPlanSummary(riskPlan, null);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testUpdateRiskPlan() {
		try{
			RiskPlan riskPlan = new RiskPlan();
			riskPlan.setId(1l);
			List<Comments> list = new ArrayList<>();
			Comments comments = new Comments();
			comments.setUserComments("Test Comments");
			list.add(comments);
			riskPlan.setComments(list);
			List<SignalURL> urls = new ArrayList<>();
			SignalURL url = new SignalURL();
			urls.add(url);
			riskPlan.setSignalUrls(urls);
			riskPlanService.updateRiskPlan(riskPlan, null);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	
	@Test
	public void testUpdateRiskPlanWithNull() {
		try{
			RiskPlan riskPlan = new RiskPlan();
			riskPlanService.updateRiskPlan(riskPlan, null);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	private void getSearchDto(SearchDto searchDto) {
		List<String> ingredients = new ArrayList<>();
		ingredients.add("Test Ingredient");
		List<String> licenses = new ArrayList<>();
		licenses.add("Test Licenses");
		List<String> products = new ArrayList<>();
		products.add("Test Products");
		List<String> statuses = new ArrayList<>();
		statuses.add("Test Statuses");
		List<String> assignees = new ArrayList<>();
		assignees.add("Test Assignees");
		List<String> riskTaskStatus = new ArrayList<>();
		riskTaskStatus.add("Test RiskTaskStatus");
		List<String> hlgts = new ArrayList<>();
		hlgts.add("Test hlgts");
		List<String> hlts = new ArrayList<>();
		hlts.add("Test hlts");
		List<String> pts = new ArrayList<>();
		pts.add("Test pts");
		List<String> socs = new ArrayList<>();
		socs.add("Test socs");
		Date startDate = new Date();
		Date endDate = new Date();
		
		searchDto.setIngredients(ingredients);
		searchDto.setLicenses(licenses);
		searchDto.setProducts(products);
		searchDto.setStatuses(statuses);
		searchDto.setAssignees(assignees);
		searchDto.setRiskTaskStatus(riskTaskStatus);
		searchDto.setStartDate(startDate);
		searchDto.setEndDate(endDate);
		searchDto.setHlgts(hlgts);
		searchDto.setHlts(hlts);
		searchDto.setPts(pts);
		searchDto.setSocs(socs);
		
	}
}
