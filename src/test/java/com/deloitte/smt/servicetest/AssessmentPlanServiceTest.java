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
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.Task;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.TaskRepository;
import com.deloitte.smt.service.AssessmentPlanService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class AssessmentPlanServiceTest {
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private AssessmentPlanService assessmentPlanService;
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@MockBean
    AssessmentPlanRepository assessmentPlanRepository;
	
	@MockBean
	TaskRepository taskRepository;

	@Test
	public void testFindOne(){
		try{
			AssessmentPlan assessmentPlan = new AssessmentPlan();
			assessmentPlan.setAssessmentName("Test Case 1");
			given(this.assessmentPlanRepository.findOne(121l)).willReturn(assessmentPlan);
			assessmentPlanService.findById(121l);
		}catch(Exception ex){
			logger.info(ex);
		}
		
	}
	
	@Test
	public void testUpdateAssessmentNull() {
		try{
			AssessmentPlan assessmentPlan = new AssessmentPlan();
			given(this.assessmentPlanRepository.findOne(121l)).willReturn(assessmentPlan);
			assessmentPlanService.updateAssessment(assessmentPlan);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testUpdateAssessmentOwner() {
		try{
			AssessmentPlan assessmentPlan = new AssessmentPlan();
			assessmentPlan.setAssessmentName("Test Case 1");
			assessmentPlan.setId(121l);
			AssessmentPlan assessmentPlanDB = new AssessmentPlan();
			assessmentPlanDB.setOwner("R");
			given(this.assessmentPlanRepository.findOne(121l)).willReturn(assessmentPlanDB);
			assessmentPlanService.updateAssessment(assessmentPlan);
		}catch(Exception ex){
			logger.info(ex);
		}
		
	}
	
	@Test
	public void testUpdateAssessment() {
		try{
			AssessmentPlan assessmentPlan = new AssessmentPlan();
			assessmentPlan.setAssessmentName("Test Case 1");
			assessmentPlan.setId(121l);
			 List<Task> signalActionsStatus=new ArrayList<>();
			 Task task = new Task();
			 task.setStatus("new");
			 signalActionsStatus.add(task);
			given(this.assessmentPlanRepository.findOne(121l)).willReturn(null);
			given(this.taskRepository.findAllByAssessmentPlanId(121l)).willReturn(signalActionsStatus);
			assessmentPlanService.updateAssessment(assessmentPlan);
		}catch(Exception ex){
			logger.info(ex);
		}
		
	}
	
	@Test
	public void testFinalAssessmentNull() {
		try{
			AssessmentPlan assessmentPlan = new AssessmentPlan();
			given(this.assessmentPlanRepository.findOne(121l)).willReturn(assessmentPlan);
			assessmentPlanService.finalAssessment(assessmentPlan);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testFinalAssessment() {
		try{
			AssessmentPlan assessmentPlan = new AssessmentPlan();
			assessmentPlan.setId(121l);
			given(this.assessmentPlanRepository.findOne(121l)).willReturn(assessmentPlan);
			assessmentPlanService.finalAssessment(assessmentPlan);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testUpdateAssessmentNameDuplicate() {
		try{
			List<AssessmentPlan> list = new ArrayList<>();
			List<String> assessmentNames = new ArrayList<>();
			assessmentNames.add("A");
			AssessmentPlan assessmentPlan = new AssessmentPlan();
			assessmentPlan.setId(2l);
			list.add(assessmentPlan);
			given(this.assessmentPlanRepository.findAll()).willReturn(list);
			given(this.assessmentPlanRepository.findByAssessmentName("A",2l)).willReturn(assessmentNames);
			assessmentPlanService.updateAssessmentName(1l,"A");
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testUpdateAssessmentName() {
		try{
			List<AssessmentPlan> list = new ArrayList<>();
			List<String> assessmentNames = new ArrayList<>();
			assessmentNames.add("B");
			AssessmentPlan assessmentPlan = new AssessmentPlan();
			assessmentPlan.setId(121l);
			list.add(assessmentPlan);
			given(this.assessmentPlanRepository.findAll()).willReturn(list);
			given(this.assessmentPlanRepository.findByAssessmentName("A",2l)).willReturn(assessmentNames);
			assessmentPlanService.updateAssessmentName(1l,"A");
		}catch(Exception ex){
			logger.info(ex);
		}
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
			logger.info(ex);
		}
	}
	
	@Test
	public void testFindOneWithNull() {
		try{
			assessmentPlanService.findById(121l);
		}catch(Exception ex){
			logger.info(ex);
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
	public void testGetTaskTamplatesOfAssessmentProducts() {
		try{
			AssessmentPlan assessmentPlan = new AssessmentPlan();
			Set<Topic> topics = new HashSet<>();
			Topic topic = new Topic();
			topic.setId(100l);
			topic.setName("Test Topic 1");
			topics.add(topic);
			assessmentPlan.setTopics(topics);
			given(this.assessmentPlanRepository.findOne(121l)).willReturn(assessmentPlan);
			assessmentPlanService.getTaskTamplatesOfAssessmentProducts(121l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	
}
