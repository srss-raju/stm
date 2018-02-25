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
import com.deloitte.smt.entity.TaskTemplateProducts;
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
	public void testCreateTaskTemplateNameNull() {
		try{
			logger.info("testCreateTaskTemplate");
			TaskTemplate taskTemplate = new TaskTemplate();
			taskTemplate.setId(1l);
			taskTemplateService.createTaskTemplate(taskTemplate);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCreateTaskTemplateDuplicate() {
		try{
			logger.info("testCreateTaskTemplate");
			TaskTemplate taskTemplate = new TaskTemplate();
			taskTemplate.setId(1l);
			taskTemplate.setName("A");
			given(this.taskTemplateRepository.countTaskTemplateByNameIgnoreCaseAndType("A","A")).willReturn(1l);
			taskTemplateService.createTaskTemplate(taskTemplate);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCreateTaskTemplateDuplicateRecord() {
		try{
			logger.info("testCreateTaskTemplate");
			TaskTemplate taskTemplate = new TaskTemplate();
			taskTemplate.setId(1l);
			taskTemplate.setName("A");
			List<TaskTemplateProducts> products = new ArrayList<>();
			TaskTemplateProducts product = new TaskTemplateProducts();
			product.setId(1l);
			products.add(product);
			taskTemplate.setProducts(products);
			given(this.taskTemplateRepository.countTaskTemplateByNameIgnoreCaseAndType("B","A")).willReturn(0l);
			taskTemplateService.createTaskTemplate(taskTemplate);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCreateTaskTemplate() {
		try{
			logger.info("testCreateTaskTemplate");
			TaskTemplate taskTemplate = new TaskTemplate();
			taskTemplate.setId(1l);
			taskTemplate.setName("A");
			List<TaskTemplateProducts> products = new ArrayList<>();
			TaskTemplateProducts taskTemplateProduct = new TaskTemplateProducts();
			taskTemplateProduct.setRecordKey("1@#2");
			products.add(taskTemplateProduct);
			given(this.taskTemplateRepository.countTaskTemplateByNameIgnoreCaseAndType("A","A")).willReturn(0l);
			taskTemplateService.createTaskTemplate(taskTemplate);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	@Test
	public void testUpdateTaskTemplate() {
		try{
			logger.info("testCreateTaskTemplate");
			TaskTemplate taskTemplate = new TaskTemplate();
			taskTemplate.setId(1l);
			given(this.taskTemplateRepository.save(taskTemplate)).willReturn(taskTemplate);
			taskTemplateService.updateTaskTemplate(taskTemplate);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testUpdateTaskTemplateDuplicate() {
		try{
			logger.info("testCreateTaskTemplate");
			TaskTemplate taskTemplate = new TaskTemplate();
			taskTemplate.setId(1l);
			given(this.taskTemplateRepository.findByNameIgnoreCase("A")).willReturn(taskTemplate);
			taskTemplateService.updateTaskTemplate(taskTemplate);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testDelete() {
		try{
			TaskTemplate taskTemplate = new TaskTemplate();
			given(this.taskTemplateRepository.findOne(1l)).willReturn(taskTemplate);
			taskTemplateService.delete(1l);
		}catch(Exception e){
			e.printStackTrace();
		}
		
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
	public void testFindAll() {
		try{
			TaskTemplate taskTemplate = new TaskTemplate();
			List<TaskTemplate> taskTemplates = new ArrayList<>();
			taskTemplates.add(taskTemplate);
			given(this.taskTemplateRepository.findAllByOrderByCreatedDateDesc()).willReturn(taskTemplates);
			taskTemplateService.findAll();
		}catch(Exception ex){
			logger.info(ex);
		}
		
	}
	
	@Test
	public void testFindById() {
		try{
			TaskTemplate taskTemplate = new TaskTemplate();
			taskTemplate.setId(1l);
			given(this.taskTemplateRepository.findOne(1l)).willReturn(taskTemplate);
			taskTemplateService.findById(1l);
		}catch(Exception ex){
			logger.info(ex);
		}
		
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
	
	@Test
	public void testUpdateTaskTemplateName() {
		try{
			TaskTemplate taskTemplate = new TaskTemplate();
			taskTemplate.setId(2l);
			List<TaskTemplate> listTasks = new ArrayList<>();
			List<String> taskTemplateNames=new ArrayList<>();
			taskTemplateNames.add("B");
			listTasks.add(taskTemplate);
			given(this.taskTemplateRepository.findAll()).willReturn(listTasks);
			given(this.taskTemplateRepository.findByName("A",1l)).willReturn(taskTemplateNames);
			taskTemplateService.updateTaskTemplate(taskTemplate);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testUpdateTaskTemplateNameDuplicate() {
		try{
			TaskTemplate taskTemplate = new TaskTemplate();
			taskTemplate.setId(1l);
			List<TaskTemplate> listTasks = new ArrayList<>();
			List<String> taskTemplateNames=new ArrayList<>();
			taskTemplateNames.add("A");
			listTasks.add(taskTemplate);
			given(this.taskTemplateRepository.findAll()).willReturn(listTasks);
			given(this.taskTemplateRepository.findByName("A",1l)).willReturn(taskTemplateNames);
			taskTemplateService.updateTaskTemplate(taskTemplate);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testUpdateTaskTemplateNameDuplicateCheck() {
		try{
			TaskTemplate taskTemplate = new TaskTemplate();
			taskTemplate.setId(1l);
			List<TaskTemplate> listTasks = new ArrayList<>();
			List<String> taskTemplateNames=new ArrayList<>();
			taskTemplateNames.add("A");
			listTasks.add(taskTemplate);
			given(this.taskTemplateRepository.findAll()).willReturn(listTasks);
			given(this.taskTemplateRepository.findByName("A",1l)).willReturn(taskTemplateNames);
			taskTemplateService.updateTaskTemplateName(1l,"A");
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testUpdateTaskTemplateNameNoDuplicateCheck() {
		try{
			TaskTemplate taskTemplate = new TaskTemplate();
			taskTemplate.setId(1l);
			List<TaskTemplate> listTasks = new ArrayList<>();
			List<String> taskTemplateNames=new ArrayList<>();
			taskTemplateNames.add("A");
			listTasks.add(taskTemplate);
			given(this.taskTemplateRepository.findAll()).willReturn(listTasks);
			given(this.taskTemplateRepository.findByName("A",2l)).willReturn(taskTemplateNames);
			taskTemplateService.updateTaskTemplateName(1l,"B");
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testFindAllByTemplateId() {
		try{
			taskTemplateService.findAllByTemplateId(1l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testUpdateRiskTaskName() {
		try{
			TaskTemplate taskTemplate = new TaskTemplate();
			taskTemplate.setId(2l);
			List<TaskTemplate> listTasks = new ArrayList<>();
			List<String> taskTemplateNames=new ArrayList<>();
			taskTemplateNames.add("B");
			listTasks.add(taskTemplate);
			given(this.taskTemplateRepository.findAll()).willReturn(listTasks);
			given(this.taskTemplateRepository.findByName("A",4l)).willReturn(taskTemplateNames);
			taskTemplateService.updateRiskTaskName(1l,"C");
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testUpdateRiskTaskNameDuplicate() {
		try{
			TaskTemplate taskTemplate = new TaskTemplate();
			taskTemplate.setId(1l);
			List<TaskTemplate> listTasks = new ArrayList<>();
			List<String> taskTemplateNames=new ArrayList<>();
			taskTemplateNames.add("A");
			listTasks.add(taskTemplate);
			given(this.taskTemplateRepository.findAll()).willReturn(listTasks);
			given(this.taskTemplateRepository.findByName("A",1l)).willReturn(taskTemplateNames);
			taskTemplateService.updateRiskTaskName(1l,"A");
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
}
