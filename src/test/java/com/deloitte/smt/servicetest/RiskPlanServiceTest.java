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
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.Task;
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
	
	
	
}
