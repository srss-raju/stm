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
import com.deloitte.smt.entity.ExternalDatasets;
import com.deloitte.smt.repository.ExternalDatasetsRepository;
import com.deloitte.smt.service.ExternalDatasetsService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class ExternalDatasetsServiceTest {
	
	private static final Logger LOG = Logger.getLogger(ExternalDatasetsServiceTest.class);
	
	@Autowired
	private ExternalDatasetsService externalDatasetsService;
	
	@MockBean
	ExternalDatasetsRepository externalDatasetsRepository;
	
		
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
		List<ExternalDatasets> externalDatasets = new ArrayList<>();
		ExternalDatasets externalDataset = new ExternalDatasets();
		externalDatasets.add(externalDataset);
		externalDatasetsService.insert(externalDatasets);
	}
	
	@Test
	public void testUpdate() throws Exception{
		ExternalDatasets externalDataset = new ExternalDatasets();
		externalDataset.setId(1l);
		externalDatasetsService.update(externalDataset);
	}
	
	@Test
	public void testUpdateWithNull() throws Exception{
		try{
			ExternalDatasets externalDataset = new ExternalDatasets();
			externalDatasetsService.update(externalDataset);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testDelete() throws Exception{
		ExternalDatasets externalDataset = new ExternalDatasets();
		given(this.externalDatasetsRepository.findOne(1l)).willReturn(externalDataset);
		externalDatasetsService.delete(1l);
	}
	
	@Test
	public void testDeleteWithNull() throws Exception{
		try{
			externalDatasetsService.delete(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindById() throws Exception{
		ExternalDatasets externalDataset = new ExternalDatasets();
		given(this.externalDatasetsRepository.findOne(1l)).willReturn(externalDataset);
		externalDatasetsService.findById(1l);
	}
	@Test
	public void testFindByIdWithNull() throws Exception{
		try{
			externalDatasetsService.findById(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindAll() throws Exception{
		externalDatasetsService.findAll();
	}
	
}
