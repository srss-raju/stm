package com.deloitte.smt.servicetest;

import static org.mockito.BDDMockito.given;

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
import com.deloitte.smt.entity.AssignmentConfiguration;
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
	
}
