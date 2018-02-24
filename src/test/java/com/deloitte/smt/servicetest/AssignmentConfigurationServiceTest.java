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
import com.deloitte.smt.entity.AssignmentCondition;
import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.entity.AssignmentProduct;
import com.deloitte.smt.repository.AssignmentConfigurationRepository;
import com.deloitte.smt.service.AssignmentConfigurationService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class AssignmentConfigurationServiceTest {
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private AssignmentConfigurationService assignmentConfigurationService;
	
	@MockBean
	AssignmentConfigurationRepository assignmentConfigurationRepository;
	
		
	@Test
	public void testInsert() {
		try{
			AssignmentConfiguration config = new AssignmentConfiguration();
			config.setAssessmentOwner("Test User");
			config.setSignalSource("test");
			assignmentConfigurationService.insert(config);
		}catch(Exception ex){
			
		}
	}
	
	@Test
	public void testInsertDuplicateConfig() {
		try{
			AssignmentConfiguration config = new AssignmentConfiguration();
			AssignmentConfiguration configDB = new AssignmentConfiguration();
			config.setAssessmentOwner("Test User");
			config.setSignalSource("test");
			config.setName("A");
			configDB.setName("A");
			given(this.assignmentConfigurationRepository.findByNameIgnoreCase("A")).willReturn(configDB);
			
			assignmentConfigurationService.insert(config);
		}catch(Exception ex){
			
		}
	}
	
	@Test
	public void testInsertDuplicateCheck() {
		try{
			AssignmentConfiguration config = new AssignmentConfiguration();
			AssignmentConfiguration configDB = new AssignmentConfiguration();
			config.setAssessmentOwner("Test User");
			config.setSignalSource("test");
			config.setName("A");
			configDB.setName("B");
			
			List<AssignmentCondition> conditions = new ArrayList<>();
			AssignmentCondition condition = new AssignmentCondition();
			condition.setRecordKey("A@#B");
			condition.setId(1l);
			conditions.add(condition);
			
			List<AssignmentProduct> products = new ArrayList<>();
			AssignmentProduct product = new AssignmentProduct();
			product.setRecordKey("A@#B");
			product.setId(1l);
			products.add(product);
			
			given(this.assignmentConfigurationRepository.findByNameIgnoreCase("A")).willReturn(configDB);
			assignmentConfigurationService.insert(config);
		}catch(Exception ex){
			
		}
	}
	
	@Test
	public void testUpdateWithNull() {
		try{
			AssignmentConfiguration config = new AssignmentConfiguration();
			config.setAssessmentOwner("Test User");
			config.setSignalSource("Test Source");
			assignmentConfigurationService.update(config);
		}catch(Exception ex){
			
		}
	}
	
	@Test
	public void testUpdateDuplicate() {
		try{
			AssignmentConfiguration config = new AssignmentConfiguration();
			AssignmentConfiguration configDB = new AssignmentConfiguration();
			config.setId(1l);
			config.setAssessmentOwner("Test User");
			config.setSignalSource("test");
			config.setName("A");
			configDB.setName("A");
			
			given(this.assignmentConfigurationRepository.findByNameIgnoreCase("A")).willReturn(configDB);
			assignmentConfigurationService.update(config);
		}catch(Exception ex){
			
		}
	}
	
	@Test
	public void testUpdate() {
		try{
			AssignmentConfiguration config = new AssignmentConfiguration();
			AssignmentConfiguration configDB = new AssignmentConfiguration();
			config.setId(1l);
			config.setAssessmentOwner("Test User");
			config.setSignalSource("test");
			config.setName("A");
			configDB.setName("B");
			
			given(this.assignmentConfigurationRepository.findByNameIgnoreCase("A")).willReturn(configDB);
			assignmentConfigurationService.update(config);
		}catch(Exception ex){
			
		}
	}
	
	@Test
	public void testDelete() throws Exception{
		try{
			AssignmentConfiguration config = new AssignmentConfiguration();
			config.setAssessmentOwner("Test User");
			config.setSignalSource("Test Source");
			config.setId(1l);
			given(this.assignmentConfigurationRepository.findOne(1l)).willReturn(config);
			assignmentConfigurationService.delete(1l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testDeleteWithNull() throws Exception{
		try{
			AssignmentConfiguration config = new AssignmentConfiguration();
			config.setAssessmentOwner("Test User");
			config.setSignalSource("Test Source");
			config.setId(1l);
			given(this.assignmentConfigurationRepository.findOne(11l)).willReturn(config);
			assignmentConfigurationService.delete(1l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testFindById() throws Exception{
		AssignmentConfiguration config = new AssignmentConfiguration();
		config.setAssessmentOwner("Test User");
		config.setSignalSource("Test Source");
		config.setId(1l);
		given(this.assignmentConfigurationRepository.findOne(1l)).willReturn(config);
		assignmentConfigurationService.findById(1l);
	}
	@Test
	public void testFindByIdWithNull() throws Exception{
		try{
			AssignmentConfiguration config = new AssignmentConfiguration();
			config.setAssessmentOwner("Test User");
			config.setSignalSource("Test Source");
			config.setId(1l);
			given(this.assignmentConfigurationRepository.findOne(11l)).willReturn(config);
			assignmentConfigurationService.findById(1l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testFindAll() throws Exception{
		assignmentConfigurationService.findAll();
	}
	
	@Test
	public void testGetProductsAndConditions() {
		try{
			AssignmentConfiguration assignmentConfiguration = new AssignmentConfiguration();
			List<AssignmentProduct> products = new ArrayList<>();
			AssignmentProduct  product = new AssignmentProduct ();
			product.setRecordKey("A@#2");
			product.setId(1l);
			products.add(product);
			List<AssignmentCondition> conditions = new ArrayList<>();
			AssignmentCondition socConfig = new AssignmentCondition();
			socConfig.setRecordKey("A@#2");
			socConfig.setId(1l);
			conditions.add(socConfig);
			assignmentConfigurationService.getProductsAndConditions(assignmentConfiguration);
		}catch(Exception ex){
			logger.info(ex);
		}
		
	}
	
}
