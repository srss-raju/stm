package com.deloitte.smt.servicetest;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

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
import com.deloitte.smt.entity.SignalConfirmations;
import com.deloitte.smt.repository.SignalConfirmationRespository;
import com.deloitte.smt.service.SignalConfirmationsService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class SignalConfirmationsServiceTest {
	
	private static final Logger LOG = Logger.getLogger(SignalConfirmationsServiceTest.class);
	
	@Autowired
	private SignalConfirmationsService signalConfirmationsService;
	
	@MockBean
	SignalConfirmationRespository signalConfirmationRespository;
	
		
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
	public void testInsert() throws Exception{
		List<SignalConfirmations> signalConfirmations = new ArrayList<>();
		SignalConfirmations signalConfirmation = new SignalConfirmations();
		signalConfirmations.add(signalConfirmation);
		signalConfirmationsService.insert(signalConfirmations);
	}
	
	@Test
	public void testUpdate() throws Exception{
		SignalConfirmations signalConfirmation = new SignalConfirmations();
		signalConfirmation.setId(1l);
		signalConfirmationsService.update(signalConfirmation);
	}
	
	@Test
	public void testUpdateWithNull() throws Exception{
		try{
			SignalConfirmations signalConfirmation = new SignalConfirmations();
			signalConfirmationsService.update(signalConfirmation);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testDelete() throws Exception{
		SignalConfirmations signalConfirmation = new SignalConfirmations();
		given(this.signalConfirmationRespository.findOne(1l)).willReturn(signalConfirmation);
		signalConfirmationsService.delete(1l);
	}
	
	@Test
	public void testDeleteWithNull() throws Exception{
		try{
			signalConfirmationsService.delete(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindById() throws Exception{
		SignalConfirmations signalConfirmation = new SignalConfirmations();
		given(this.signalConfirmationRespository.findOne(1l)).willReturn(signalConfirmation);
		signalConfirmationsService.findById(1l);
	}
	@Test
	public void testFindByIdWithNull() throws Exception{
		try{
			signalConfirmationsService.findById(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindAll() throws Exception{
		signalConfirmationsService.findAll();
	}
	
}
