package com.deloitte.smt.servicetest;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

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
import com.deloitte.smt.entity.TaskTemplate;
import com.deloitte.smt.repository.TaskTemplateRepository;
import com.deloitte.smt.service.TaskTemplateService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class TaskTemplateServiceTest {
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private TaskTemplateService taskTemplateService;
	
	@MockBean
	private TaskTemplateRepository taskTemplateRepository;
	
	
	@Test
	public void testCreateTaskTemplate() throws Exception{
		logger.info("testCreateTaskTemplate");
		TaskTemplate taskTemplate = new TaskTemplate();
		taskTemplate.setId(1l);
		given(this.taskTemplateRepository.save(taskTemplate)).willReturn(taskTemplate);
		taskTemplateService.createTaskTemplate(taskTemplate);
	}
	
	@Test
	public void testUpdateTaskTemplate() throws Exception{
		logger.info("testCreateTaskTemplate");
		TaskTemplate taskTemplate = new TaskTemplate();
		taskTemplate.setId(1l);
		given(this.taskTemplateRepository.save(taskTemplate)).willReturn(taskTemplate);
		taskTemplateService.updateTaskTemplate(taskTemplate);
	}
	
	@Test
	public void testDelete() throws Exception{
		TaskTemplate taskTemplate = new TaskTemplate();
		given(this.taskTemplateRepository.findOne(1l)).willReturn(taskTemplate);
		taskTemplateService.delete(1l);
	}
	
	@Test
	public void testDeleteWithNull() throws Exception{
		try{
			TaskTemplate taskTemplate = new TaskTemplate();
			given(this.taskTemplateRepository.findOne(11l)).willReturn(taskTemplate);
			taskTemplateService.delete(1l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testFindAll() throws Exception{
		TaskTemplate taskTemplate = new TaskTemplate();
		List<TaskTemplate> taskTemplates = new ArrayList<>();
		taskTemplates.add(taskTemplate);
		given(this.taskTemplateRepository.findAllByOrderByCreatedDateDesc()).willReturn(taskTemplates);
		taskTemplateService.findAll();
	}
	
	@Test
	public void testFindById() throws Exception{
		TaskTemplate taskTemplate = new TaskTemplate();
		taskTemplate.setId(1l);
		given(this.taskTemplateRepository.findOne(1l)).willReturn(taskTemplate);
		taskTemplateService.findById(1l);
	}
	
	@Test
	public void testFindByIdWithNull() throws Exception{
		try{
			TaskTemplate taskTemplate = new TaskTemplate();
			taskTemplate.setId(12l);
			given(this.taskTemplateRepository.findOne(1l)).willReturn(taskTemplate);
			taskTemplateService.findById(11l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
}
