package com.deloitte.smt.servicetest;

import static org.mockito.BDDMockito.given;

import org.apache.log4j.Logger;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
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
import com.deloitte.smt.entity.DetectionRun;
import com.deloitte.smt.repository.DetectionRunRepository;
import com.deloitte.smt.service.DetectionRunService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class DetectionRunServiceTest {
	
	private static final Logger LOG = Logger.getLogger(DetectionRunServiceTest.class);
	
	@Autowired
	private DetectionRunService detectionRunService;
	
	@MockBean
	DetectionRunRepository detectionRunRepository;
	
		
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
	public void testInsert() throws Exception {
		LOG.info("testInsert");
		DetectionRun detectionRun = new DetectionRun();
		detectionRunService.insert(detectionRun);
	}
	
	@Test
	public void update() throws Exception {
		LOG.info("update");
		DetectionRun detectionRun = new DetectionRun();
		detectionRun.setId(1l);
		detectionRunService.update(detectionRun);
	}
	
	@Test
	public void updateWithNull() {
		try{
			LOG.info("update");
			DetectionRun detectionRun = new DetectionRun();
			detectionRunService.update(detectionRun);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	
	@Test
	public void testFindById() {
		try{
			LOG.info("Test findById");
			DetectionRun detectionRun = new DetectionRun();
			given(this.detectionRunRepository.findOne(1l)).willReturn(detectionRun);
			detectionRunService.findById(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindByIdWithNull() {
		try{
			detectionRunService.findById(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindAll() throws Exception {
		detectionRunService.findAll();
	}
	
	@Test
	public void testFindByDetectionId() throws Exception {
		detectionRunService.findByDetectionId(1l);
	}
	
	@Test
	public void delete()  {
		try{
			LOG.info("update");
			DetectionRun detectionRun = new DetectionRun();
			detectionRun.setId(1l);
			given(this.detectionRunRepository.findOne(1l)).willReturn(detectionRun);
			detectionRunService.delete(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
		
	}
	
	@Test
	public void deleteWithNull() {
		try{
			LOG.info("delete");
			given(this.detectionRunRepository.findOne(1l)).willReturn(null);
			detectionRunService.delete(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}

}
