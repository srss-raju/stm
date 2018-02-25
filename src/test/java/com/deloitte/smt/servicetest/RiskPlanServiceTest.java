package com.deloitte.smt.servicetest;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.dto.ConditionProductDTO;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.entity.Comments;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.Task;
import com.deloitte.smt.entity.TaskTemplate;
import com.deloitte.smt.entity.TaskTemplateProducts;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.entity.TopicCondition;
import com.deloitte.smt.entity.TopicConditionValues;
import com.deloitte.smt.entity.TopicProduct;
import com.deloitte.smt.entity.TopicProductValues;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.AssignmentConfigurationRepository;
import com.deloitte.smt.repository.CommentsRepository;
import com.deloitte.smt.repository.MeetingRepository;
import com.deloitte.smt.repository.RiskPlanRepository;
import com.deloitte.smt.repository.TaskRepository;
import com.deloitte.smt.repository.TaskTemplateProductsRepository;
import com.deloitte.smt.repository.TaskTemplateRepository;
import com.deloitte.smt.service.AssessmentPlanService;
import com.deloitte.smt.service.RiskPlanService;
import com.deloitte.smt.service.SignalAssignmentService;
import com.deloitte.smt.service.SignalService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class RiskPlanServiceTest {
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private RiskPlanService riskPlanService;
	
	@MockBean
	private TaskRepository taskRepository;
	
	@Autowired
	private SignalAssignmentService signalAssignmentService;
	
	@MockBean
	private SignalService signalService;
	
	@MockBean
	private AssessmentPlanService assessmentPlanService;
	
	@MockBean
	MeetingRepository meetingRepository;

	@MockBean
	AssessmentPlanRepository assessmentPlanRepository;

	@MockBean
	RiskPlanRepository riskPlanRepository;


	@PersistenceContext
	private EntityManager entityManager;

	@MockBean
	CommentsRepository commentsRepository;

	@MockBean
	AssignmentConfigurationRepository assignmentConfigurationRepository;
	
	@MockBean
	TaskTemplateProductsRepository riskTaskTemplateProductsRepository;
	
	@MockBean
    TaskTemplateRepository riskTaskTemplateRepository;
	
	@Test
	public void testInsert() {
		try{
			List<TopicCondition> conditions = new ArrayList<>();
			TopicCondition topicCondition = new TopicCondition();
			List<TopicConditionValues> topicConditionValues = new ArrayList<>();
			TopicConditionValues topicConditionValue = new TopicConditionValues();
			topicConditionValues.add(topicConditionValue);
			topicCondition.setRecordValues(topicConditionValues);
			conditions.add(topicCondition);
			List<TopicProduct> products = new ArrayList<>();
			TopicProduct topicProduct = new TopicProduct();
			List<TopicProductValues> topicProductValues = new ArrayList<>(); 
			TopicProductValues topicProductValue = new TopicProductValues();
			topicProductValues.add(topicProductValue);
			topicProduct.setRecordValues(topicProductValues);
			products.add(topicProduct);
			Topic topicUpdated = new Topic();
			topicUpdated.setId(1l);
			topicUpdated.setProducts(products);
			topicUpdated.setConditions(conditions);
			AssessmentPlan assessmentPlan = new AssessmentPlan();
			assessmentPlan.setId(1l);
			Set<Topic> topics = new HashSet<>();
			topics.add(topicUpdated);
			assessmentPlan.setTopics(topics);
			RiskPlan riskPlan = new RiskPlan();
			riskPlan.setName("Test Plan");
			riskPlan.setAssessmentPlan(assessmentPlan);
			
			ConditionProductDTO conditionProductDTO = new ConditionProductDTO();
			AssignmentConfiguration assignmentConfiguration = new AssignmentConfiguration();
			assignmentConfiguration.setRiskOwner("R");
			
			given(this.assessmentPlanService.findById(1l)).willReturn(assessmentPlan);
			given(this.riskPlanRepository.countByNameIgnoreCase(riskPlan.getName())).willReturn(0l);
			given(this.riskPlanRepository.save(riskPlan)).willReturn(riskPlan);
			given(this.signalService.findById(1l)).willReturn(topicUpdated);
			given(this.signalAssignmentService.getAssignmentConfiguration(assignmentConfiguration, conditionProductDTO)).willReturn(assignmentConfiguration);
			
			riskPlanService.insert(riskPlan, null);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testInsertWithAssignmentNull() {
		try{
			List<TopicCondition> conditions = new ArrayList<>();
			TopicCondition topicCondition = new TopicCondition();
			List<TopicConditionValues> topicConditionValues = new ArrayList<>();
			TopicConditionValues topicConditionValue = new TopicConditionValues();
			topicConditionValues.add(topicConditionValue);
			topicCondition.setRecordValues(topicConditionValues);
			conditions.add(topicCondition);
			List<TopicProduct> products = new ArrayList<>();
			TopicProduct topicProduct = new TopicProduct();
			List<TopicProductValues> topicProductValues = new ArrayList<>(); 
			TopicProductValues topicProductValue = new TopicProductValues();
			topicProductValues.add(topicProductValue);
			topicProduct.setRecordValues(topicProductValues);
			products.add(topicProduct);
			Topic topicUpdated = new Topic();
			topicUpdated.setId(1l);
			topicUpdated.setProducts(products);
			topicUpdated.setConditions(conditions);
			AssessmentPlan assessmentPlan = new AssessmentPlan();
			assessmentPlan.setId(1l);
			Set<Topic> topics = new HashSet<>();
			topics.add(topicUpdated);
			assessmentPlan.setTopics(topics);
			RiskPlan riskPlan = new RiskPlan();
			riskPlan.setName("Test Plan");
			riskPlan.setAssessmentPlan(assessmentPlan);
			
			given(this.assessmentPlanService.findById(1l)).willReturn(assessmentPlan);
			given(this.riskPlanRepository.countByNameIgnoreCase(riskPlan.getName())).willReturn(0l);
			given(this.riskPlanRepository.save(riskPlan)).willReturn(riskPlan);
			given(this.signalService.findById(1l)).willReturn(topicUpdated);
			
			riskPlanService.insert(riskPlan, null);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testInsertWithAssessment() {
		try{
			List<TopicCondition> conditions = new ArrayList<>();
			TopicCondition topicCondition = new TopicCondition();
			List<TopicConditionValues> topicConditionValues = new ArrayList<>();
			TopicConditionValues topicConditionValue = new TopicConditionValues();
			topicConditionValues.add(topicConditionValue);
			topicCondition.setRecordValues(topicConditionValues);
			conditions.add(topicCondition);
			List<TopicProduct> products = new ArrayList<>();
			TopicProduct topicProduct = new TopicProduct();
			List<TopicProductValues> topicProductValues = new ArrayList<>(); 
			TopicProductValues topicProductValue = new TopicProductValues();
			topicProductValues.add(topicProductValue);
			topicProduct.setRecordValues(topicProductValues);
			products.add(topicProduct);
			Topic topicUpdated = new Topic();
			topicUpdated.setId(1l);
			topicUpdated.setProducts(products);
			topicUpdated.setConditions(conditions);
			AssessmentPlan assessmentPlan = new AssessmentPlan();
			assessmentPlan.setId(1l);
			Set<Topic> topics = new HashSet<>();
			topics.add(topicUpdated);
			assessmentPlan.setTopics(topics);
			RiskPlan riskPlan = new RiskPlan();
			riskPlan.setName("Test Plan");
			riskPlan.setAssessmentPlan(assessmentPlan);
			
			given(this.assessmentPlanRepository.findOne(1l)).willReturn(assessmentPlan);
			given(this.assessmentPlanService.findById(1l)).willReturn(assessmentPlan);
			given(this.riskPlanRepository.countByNameIgnoreCase(riskPlan.getName())).willReturn(0l);
			given(this.riskPlanRepository.save(riskPlan)).willReturn(riskPlan);
			given(this.signalService.findById(1l)).willReturn(topicUpdated);
			
			riskPlanService.insert(riskPlan, 1l);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCreateRiskTask() {
		try{
			Task riskTask = new Task();
			riskTask.setId(1l);
			riskTask.setRiskId(1l);
			RiskPlan riskPlan = new RiskPlan();
			riskPlan.setName("Test Plan");
			given(this.riskPlanRepository.findOne(1l)).willReturn(riskPlan);
			given(this.taskRepository.countByNameIgnoreCaseAndRiskId("Task1",1l)).willReturn(0l);
			given(this.taskRepository.save(riskTask)).willReturn(riskTask);
			
			riskPlanService.createRiskTask(riskTask);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testCreateRiskTemplateTask() {
		try{
			Task riskTask = new Task();
			riskTask.setId(1l);
			riskTask.setRiskId(1l);
			given(this.taskRepository.findByNameIgnoreCaseAndTemplateId("Task1",1l)).willReturn(null);
			riskPlanService.createRiskTemplateTask(riskTask);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testCreateRiskTemplateTaskDuplicate() {
		try{
			Task riskTask = new Task();
			riskTask.setId(1l);
			riskTask.setRiskId(1l);
			given(this.taskRepository.findByNameIgnoreCaseAndTemplateId("Task1",1l)).willReturn(riskTask);
			riskPlanService.createRiskTemplateTask(riskTask);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testFindById() {
		try{
			Task riskTask = new Task();
			riskTask.setId(1l);
			riskTask.setStatus("New");
			given(this.taskRepository.findOne(1l)).willReturn(riskTask);
			given(this.taskRepository.save(riskTask)).willReturn(riskTask);
			riskPlanService.findById(1l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testFindAllByRiskId() {
		try{
			List<Task> list = new ArrayList<>();
			given(this.taskRepository.findAllByRiskIdAndStatusOrderByCreatedDateDesc(1l,"New")).willReturn(list);
			riskPlanService.findAllByRiskId(1l,"New");
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testFindAllByRiskIdWithNull() {
		try{
			List<Task> list = new ArrayList<>();
			given(this.taskRepository.findAllByRiskIdAndStatusOrderByCreatedDateDesc(1l,null)).willReturn(list);
			riskPlanService.findAllByRiskId(1l,null);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	
	@Test
	public void testUpdateRiskTask() {
		try{
			Task riskTask = new Task();
			riskTask.setId(1l);
			riskTask.setTemplateId(1l);
			Task riskTaskExists = new Task();
			riskTaskExists.setId(11l);
			given(this. taskRepository.findByNameIgnoreCaseAndTemplateId("A",1l)).willReturn(riskTaskExists);
			riskPlanService.updateRiskTask(riskTask);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testUpdateRiskTaskExists() {
		try{
			Task riskTask = new Task();
			riskTask.setId(1l);
			riskTask.setTemplateId(1l);
			Task riskTaskExists = new Task();
			riskTaskExists.setId(1l);
			given(this. taskRepository.findByNameIgnoreCaseAndTemplateId("A",1l)).willReturn(riskTaskExists);
			riskPlanService.updateRiskTask(riskTask);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testUpdateRiskTaskWithNull() {
		try{
			Task riskTask = new Task();
			riskPlanService.updateRiskTask(riskTask);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testDelete() {
		try{
			riskPlanService.delete(1l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testDeleteId() {
		try{
			List<Task> list = new ArrayList<>();
			Task riskTask = new Task();
			riskTask.setStatus("New");
			list.add(riskTask);
			given(this. taskRepository.findOne(1l)).willReturn(riskTask);
			given(this. taskRepository.findAllByRiskIdOrderByCreatedDateDesc(1l)).willReturn(list);
			given(this. taskRepository.findAllByRiskIdOrderByCreatedDateDesc(1l)).willReturn(list);
			riskPlanService.delete(1l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testDeleteIdWithStatus() {
		try{
			List<Task> list = new ArrayList<>();
			Task riskTask = new Task();
			riskTask.setStatus("Completed");
			list.add(riskTask);
			given(this. taskRepository.findOne(1l)).willReturn(riskTask);
			given(this. taskRepository.findAllByRiskIdOrderByCreatedDateDesc(1l)).willReturn(list);
			given(this. taskRepository.findAllByRiskIdOrderByCreatedDateDesc(1l)).willReturn(list);
			riskPlanService.delete(1l);
		}catch(Exception ex){
			logger.info(ex);
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
			logger.info(ex);
		}
	}
	
	@Test
	public void testFindByRiskIdForNull() {
		try{
			riskPlanService.findByRiskId(1l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testRiskPlanSummaryIdNull() {
		try{
			RiskPlan riskPlan = new RiskPlan();
			riskPlan.setStatus("New");
			riskPlanService.riskPlanSummary(riskPlan);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testRiskPlanSummary() {
		try{
			RiskPlan riskPlan = new RiskPlan();
			riskPlan.setId(1l);
			riskPlan.setStatus("New");
			riskPlanService.riskPlanSummary(riskPlan);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testUpdateRiskPlanIdNull() {
		try{
			RiskPlan riskPlan = new RiskPlan();
			riskPlan.setStatus("New");
			riskPlanService.updateRiskPlan(riskPlan);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	
	@Test
	public void testUpdateRiskPlanOwnerSame() {
		try{
			RiskPlan riskPlan = new RiskPlan();
			RiskPlan riskPlanDB = new RiskPlan();
			riskPlan.setId(1l);
			riskPlan.setStatus("New");
			riskPlan.setOwner("A");
			riskPlanDB.setOwner("A");
			given(this.riskPlanRepository.findOne(1l)).willReturn(riskPlanDB);
			riskPlanService.updateRiskPlan(riskPlan);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testUpdateRiskPlanSummary() {
		try{
			RiskPlan riskPlan = new RiskPlan();
			RiskPlan riskPlanDB = new RiskPlan();
			riskPlan.setId(1l);
			riskPlan.setStatus("Completed");
			riskPlan.setOwner("A");
			riskPlanDB.setOwner("B");
			given(this.riskPlanRepository.findOne(1l)).willReturn(riskPlanDB);
			riskPlanService.updateRiskPlan(riskPlan);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testUpdateRiskPlanAll() {
		try{
			RiskPlan riskPlan = new RiskPlan();
			riskPlan.setOwner("A");
			riskPlan.setId(1l);
			List<Task> tasks = new ArrayList<>();
			Task riskTask = new Task();
			riskTask.setStatus("Completed");
			tasks.add(riskTask);
			List<Comments> comments = new ArrayList<>();
			Comments comment = new Comments();
			comment.setUserComments("AAA");
			comments.add(comment);
			riskPlan.setComments(comments);
			given(this.riskPlanRepository.findOne(1l)).willReturn(null);
			given(this.taskRepository.findAllByRiskIdOrderByCreatedDateDesc(1l)).willReturn(tasks);
			given(this.riskPlanRepository.save(riskPlan)).willReturn(riskPlan);
			riskPlanService.updateRiskPlan(riskPlan);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testUpdateRiskPlan() {
		try{
			RiskPlan riskPlan = new RiskPlan();
			RiskPlan riskPlanDB = new RiskPlan();
			riskPlan.setId(1l);
			riskPlan.setStatus("New");
			riskPlan.setOwner("A");
			riskPlanDB.setOwner("B");
			List<Task> riskTaskStatues = new ArrayList<>();
			Task task = new Task();
			riskTaskStatues.add(task);
			task.setStatus("New");
			List<Comments> list = new ArrayList<>();
			Comments comment = new Comments();
			list.add(comment);
			riskPlan.setComments(list);
			given(this.riskPlanRepository.findOne(1l)).willReturn(riskPlanDB);
			given(this.taskRepository.findAllByRiskIdOrderByCreatedDateDesc(1l)).willReturn(riskTaskStatues);
			riskPlanService.updateRiskPlan(riskPlan);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testUpdateRiskNameDuplicate() {
		try{
			List<RiskPlan> list = new ArrayList<>();
			RiskPlan riskPlan = new RiskPlan();
			riskPlan.setName("A");
			riskPlan.setId(1l);
			riskPlan.setStatus("New");
			riskPlan.setOwner("A");
			list.add(riskPlan);
			given(this.riskPlanRepository.findAll()).willReturn(list);
			riskPlanService.updateRiskName(1l,"A");
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testUpdateRiskName() {
		try{
			List<RiskPlan> list = new ArrayList<>();
			RiskPlan riskPlan = new RiskPlan();
			riskPlan.setName("B");
			riskPlan.setId(1l);
			riskPlan.setStatus("New");
			riskPlan.setOwner("A");
			list.add(riskPlan);
			given(this.riskPlanRepository.findAll()).willReturn(list);
			riskPlanService.updateRiskName(1l,"A");
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testGetTaskTamplatesOfRiskProducts() {
		try{
			AssessmentPlan assessmentPlan = new AssessmentPlan();
			assessmentPlan.setId(2l);
			RiskPlan riskPlan = new RiskPlan();
			riskPlan.setId(1l);
			riskPlan.setStatus("New");
			List<Comments> comments = new ArrayList<>();
			Comments comment = new Comments();
			comment.setUserComments("AA");
			comments.add(comment);
			riskPlan.setComments(comments);
			List<Task> tasks = new ArrayList<>();
			Task task = new Task();
			task.setStatus("Completed");
			tasks.add(task);
			Set<Topic> topics = new HashSet<>();
			Topic topic = new Topic();
			List<TopicProduct> products = new ArrayList<>();
			TopicProduct topicProduct = new TopicProduct();
			topicProduct.setRecordKey("A@#B");
			products.add(topicProduct);
			topic.setProducts(products);
			topic.setName("S1");
			topic.setId(1l);
			topics.add(topic);
			assessmentPlan.setTopics(topics);
			riskPlan.setAssessmentPlan(assessmentPlan);
			
			TaskTemplateProducts taskTemplateProduct = new TaskTemplateProducts ();
			taskTemplateProduct.setId(1l);
			TaskTemplate taskTemplate = new TaskTemplate();
			
			
			given(this.riskPlanRepository.findOne(1l)).willReturn(riskPlan);
			given(this.riskPlanRepository.save(riskPlan)).willReturn(riskPlan);
			given(this.commentsRepository.findByRiskPlanId(1l)).willReturn(comments);
			given(this.assessmentPlanService.findById(2l)).willReturn(assessmentPlan);
			given(this.signalService.findById(1l)).willReturn(topic);
			given(this.riskTaskTemplateProductsRepository.findTemplateId(1l)).willReturn(1l);
			given(this.riskTaskTemplateRepository.findOne(1l)).willReturn(taskTemplate);
			given(this.riskTaskTemplateProductsRepository.findByRecordKey("A@#B")).willReturn(taskTemplateProduct);
			given(this.taskRepository.findAllByRiskIdOrderByCreatedDateDesc(1l)).willReturn(tasks);
			
			riskPlanService.getTaskTamplatesOfRiskProducts(1l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testGetTaskTamplatesOfRiskProductsStatusNew() {
		try{
			AssessmentPlan assessmentPlan = new AssessmentPlan();
			assessmentPlan.setId(2l);
			RiskPlan riskPlan = new RiskPlan();
			riskPlan.setId(1l);
			riskPlan.setStatus("New");
			List<Comments> comments = new ArrayList<>();
			Comments comment = new Comments();
			comment.setUserComments("AA");
			comments.add(comment);
			riskPlan.setComments(comments);
			List<Task> tasks = new ArrayList<>();
			Task task = new Task();
			task.setStatus("New");
			tasks.add(task);
			Set<Topic> topics = new HashSet<>();
			Topic topic = new Topic();
			List<TopicProduct> products = new ArrayList<>();
			TopicProduct topicProduct = new TopicProduct();
			topicProduct.setRecordKey("A@#B");
			products.add(topicProduct);
			topic.setProducts(products);
			topic.setName("S1");
			topic.setId(1l);
			topics.add(topic);
			assessmentPlan.setTopics(topics);
			riskPlan.setAssessmentPlan(assessmentPlan);
			
			TaskTemplateProducts taskTemplateProduct = new TaskTemplateProducts ();
			taskTemplateProduct.setId(1l);
			TaskTemplate taskTemplate = new TaskTemplate();
			
			
			given(this.riskPlanRepository.findOne(1l)).willReturn(riskPlan);
			given(this.riskPlanRepository.save(riskPlan)).willReturn(riskPlan);
			given(this.commentsRepository.findByRiskPlanId(1l)).willReturn(comments);
			given(this.assessmentPlanService.findById(2l)).willReturn(assessmentPlan);
			given(this.signalService.findById(1l)).willReturn(topic);
			given(this.riskTaskTemplateProductsRepository.findTemplateId(1l)).willReturn(1l);
			given(this.riskTaskTemplateRepository.findOne(1l)).willReturn(taskTemplate);
			given(this.riskTaskTemplateProductsRepository.findByRecordKey("A@#B")).willReturn(taskTemplateProduct);
			given(this.taskRepository.findAllByRiskIdOrderByCreatedDateDesc(1l)).willReturn(tasks);
			
			riskPlanService.getTaskTamplatesOfRiskProducts(1l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	
}
