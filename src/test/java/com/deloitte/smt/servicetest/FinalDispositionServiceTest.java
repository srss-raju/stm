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
import com.deloitte.smt.entity.FinalDispositions;
import com.deloitte.smt.repository.FinalDispositionRepository;
import com.deloitte.smt.service.FinalDispositionService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class FinalDispositionServiceTest {
	
	private static final Logger LOG = Logger.getLogger(FinalDispositionServiceTest.class);
	
	@Autowired
	private FinalDispositionService finalDispositionService;
	
	@MockBean
	FinalDispositionRepository finalDispositionRepository;
	
		
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
		List<FinalDispositions> finalDispositions = new ArrayList<>();
		FinalDispositions finalDisposition = new FinalDispositions();
		finalDispositions.add(finalDisposition);
		finalDispositionService.insert(finalDispositions);
	}
	
	@Test
	public void testUpdate() throws Exception{
		FinalDispositions finalDisposition = new FinalDispositions();
		finalDisposition.setId(1l);
		finalDispositionService.update(finalDisposition);
	}
	
	@Test
	public void testUpdateWithNull() throws Exception{
		try{
			FinalDispositions finalDisposition = new FinalDispositions();
			finalDispositionService.update(finalDisposition);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testDelete() throws Exception{
		FinalDispositions finalDisposition = new FinalDispositions();
		given(this.finalDispositionRepository.findOne(1l)).willReturn(finalDisposition);
		finalDispositionService.delete(1l);
	}
	
	@Test
	public void testDeleteWithNull() throws Exception{
		try{
			finalDispositionService.delete(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindById() throws Exception{
		FinalDispositions finalDisposition = new FinalDispositions();
		given(this.finalDispositionRepository.findOne(1l)).willReturn(finalDisposition);
		finalDispositionService.findById(1l);
	}
	@Test
	public void testFindByIdWithNull() throws Exception{
		try{
			finalDispositionService.findById(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindAll() throws Exception{
		finalDispositionService.findAll();
	}
	
}
