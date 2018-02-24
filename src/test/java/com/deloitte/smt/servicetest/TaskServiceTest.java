package com.deloitte.smt.servicetest;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.Task;
import com.deloitte.smt.exception.ExceptionBuilder;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.TaskRepository;
import com.deloitte.smt.service.TaskService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class TaskServiceTest {
	
	@Autowired
	TaskService taskService;

	@MockBean
	MessageSource messageSource;
	
	@MockBean
	ExceptionBuilder  exceptionBuilder;

	@MockBean
    TaskRepository taskRepository;

    @MockBean
    AssessmentPlanRepository assessmentPlanRepository;
	
    
    @Test
	public void testCreateTaskDuplicate() {
		try{
			Task task = new Task();
			given(this.taskRepository.countByNameIgnoreCaseAndAssessmentPlanId("A", 1l)).willReturn(1l);
			taskService.createTask(task);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
    
    
	@Test
	public void testCreateTask() {
		try{
			List<Task> actions = new ArrayList<>();
			Task task = new Task();
			task.setStatus("New");
			task.setAssessmentPlanId(3l);
			actions.add(task);
			given(this.taskRepository.countByNameIgnoreCaseAndAssessmentPlanId("A", 1l)).willReturn(0l);
			given(this.taskRepository.save(task)).willReturn(task);
			given(this.taskRepository.findAllByAssessmentPlanId(3l)).willReturn(actions);
			taskService.createTask(task);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testCreateTaskCompleted() {
		try{
			List<Task> actions = new ArrayList<>();
			Task task = new Task();
			task.setStatus("Completed");
			actions.add(task);
			given(this.taskRepository.countByNameIgnoreCaseAndAssessmentPlanId("A", 1l)).willReturn(0l);
			taskService.createTask(task);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	
	@Test
	public void testUpdateAssessmentActionWithNull() {
		try{
			Task task = new Task();
			task.setStatus("New");
			taskService.updateAssessmentAction(task);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testUpdateAssessmentAction() {
		try{
			List<Task> actions = new ArrayList<>();
			Task task = new Task();
			task.setId(1l);
			task.setStatus("Completed");
			Task actionsExist = new Task();
			actions.add(task);
			given(this.taskRepository.findByNameIgnoreCaseAndAssessmentPlanId("A", 1l)).willReturn(actionsExist);
			given(this.taskRepository.save(task)).willReturn(task);
			taskService.updateAssessmentAction(task);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testUpdateAssessmentActionExistsNoTemplate() {
		try{
			Task task = new Task();
			task.setId(1l);
			task.setStatus("Completed");
			Task actionsExist = new Task();
			given(this.taskRepository.findByNameIgnoreCaseAndAssessmentPlanId("A", 1l)).willReturn(actionsExist);
			given(this.taskRepository.save(task)).willReturn(task);
			taskService.updateAssessmentAction(task);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testUpdateAssessmentActionExistsWithTemplate() {
		try{
			AssessmentPlan assessmentPlan  = new AssessmentPlan();
			List<Task> tasks = new ArrayList<>();
			Task task = new Task();
			task.setId(1l);
			task.setTemplateId(2l);
			task.setStatus("Completed");
			task.setAssessmentPlanId(3l);
			tasks.add(task);
			Task actionsExist = new Task();
			given(this.taskRepository.findByNameIgnoreCaseAndTemplateId("A", 1l)).willReturn(actionsExist);
			given(this.taskRepository.save(task)).willReturn(task);
			given(this.taskRepository.findAllByAssessmentPlanId(3l)).willReturn(tasks);
			given(this.assessmentPlanRepository.findOne(3l)).willReturn(assessmentPlan);
			taskService.updateAssessmentAction(task);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testUpdateAssessmentActionExistsWithTemplateStatusNew() {
		try{
			List<Task> tasks = new ArrayList<>();
			Task task = new Task();
			task.setId(1l);
			task.setTemplateId(2l);
			task.setStatus("New");
			tasks.add(task);
			Task actionsExist = new Task();
			given(this.taskRepository.findByNameIgnoreCaseAndTemplateId("A", 1l)).willReturn(actionsExist);
			given(this.taskRepository.save(task)).willReturn(task);
			given(this.taskRepository.findAllByAssessmentPlanId(3l)).willReturn(tasks);
			taskService.updateAssessmentAction(task);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testFindById() {
		try{
			Task task = new Task();
			task.setStatus("New");
			given(this.taskRepository.findOne(1l)).willReturn(task);
			taskService.updateAssessmentAction(task);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testFindAllByAssessmentIdStatusNull() {
		try{
			taskService.findAllByAssessmentId(1l,null);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testFindAllByAssessmentIdStatus() {
		try{
			taskService.findAllByAssessmentId(1l,"New");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testDelete() {
		try{
			Task task = new Task();
			task.setStatus("New");
			given(this.taskRepository.findOne(1l)).willReturn(task);
			taskService.delete(1l);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testDeleteNull() {
		try{
			given(this.taskRepository.findOne(1l)).willReturn(null);
			taskService.delete(1l);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testcreateTemplateTaskDuplicate() {
		try{
			Task task = new Task();
			task.setStatus("New");
			given(this.taskRepository.findByNameIgnoreCaseAndTemplateId("A",1l)).willReturn(task);
			taskService.createTemplateTask(task);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testcreateTemplateTask() {
		try{
			Task task = new Task();
			task.setStatus("New");
			given(this.taskRepository.findByNameIgnoreCaseAndTemplateId("A",1l)).willReturn(null);
			taskService.createTemplateTask(task);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testFindAllAssessmentplan() {
		try{
			taskService.findAll("assessmentplan",1l);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testFindAllRiskplan() {
		try{
			taskService.findAll("riskplan",1l);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testFindAll() {
		try{
			taskService.findAll("siganl",1l);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
}
