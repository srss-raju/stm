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
import com.deloitte.smt.entity.DenominatorForPoisson;
import com.deloitte.smt.repository.DenominatorForPoissonRepository;
import com.deloitte.smt.service.DenominationForPoissionService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class DenominationForPoissionServiceTest {
	
	private static final Logger LOG = Logger.getLogger(DenominationForPoissionServiceTest.class);
	
	@Autowired
	private DenominationForPoissionService denominationForPoissionService;
	
	@MockBean
	DenominatorForPoissonRepository denominationForPoissionRespository;
	
		
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
		List<DenominatorForPoisson> denominatorForPoissons = new ArrayList<>();
		DenominatorForPoisson denominatorForPoisson = new DenominatorForPoisson();
		denominatorForPoissons.add(denominatorForPoisson);
		denominationForPoissionService.insert(denominatorForPoissons);
	}
	
	@Test
	public void testUpdate() throws Exception{
		DenominatorForPoisson denominatorForPoisson = new DenominatorForPoisson();
		denominatorForPoisson.setId(1l);
		denominationForPoissionService.update(denominatorForPoisson);
	}
	
	@Test
	public void testUpdateWithNull() throws Exception{
		try{
			DenominatorForPoisson denominatorForPoisson = new DenominatorForPoisson();
			denominationForPoissionService.update(denominatorForPoisson);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testDelete() throws Exception{
		DenominatorForPoisson denominatorForPoisson = new DenominatorForPoisson();
		given(this.denominationForPoissionRespository.findOne(1l)).willReturn(denominatorForPoisson);
		denominationForPoissionService.delete(1l);
	}
	
	@Test
	public void testDeleteWithNull() throws Exception{
		try{
			denominationForPoissionService.delete(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindById() throws Exception{
		DenominatorForPoisson denominatorForPoisson = new DenominatorForPoisson();
		given(this.denominationForPoissionRespository.findOne(1l)).willReturn(denominatorForPoisson);
		denominationForPoissionService.findById(1l);
	}
	@Test
	public void testFindByIdWithNull() throws Exception{
		try{
			denominationForPoissionService.findById(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindAll() throws Exception{
		denominationForPoissionService.findAll();
	}
	
}
