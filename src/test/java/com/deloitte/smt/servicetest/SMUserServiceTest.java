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
import com.deloitte.smt.entity.SMUser;
import com.deloitte.smt.repository.SMUserRepository;
import com.deloitte.smt.service.SMUserService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class SMUserServiceTest {
	
	private static final Logger LOG = Logger.getLogger(SMUserServiceTest.class);
	
	@Autowired
	private SMUserService smUserService;
	
	@MockBean
	SMUserRepository smUserRepository;
	
		
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
		SMUser smUser = new SMUser();
		smUserService.insert(smUser);
	}
	
	@Test
	public void testUpdate() throws Exception{
		SMUser smUser = new SMUser();
		smUser.setId(1l);
		smUserService.update(smUser);
	}
	
	@Test
	public void testUpdateWithNull() throws Exception{
		try{
			SMUser smUser = new SMUser();
			smUserService.update(smUser);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testDelete() throws Exception{
			SMUser smUser = new SMUser();
			given(this.smUserRepository.findOne(1l)).willReturn(smUser);
			smUserService.delete(1l);
	}
	
	@Test
	public void testDeleteWithNull() throws Exception{
		try{
			smUserService.delete(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindById() throws Exception{
		SMUser smUser = new SMUser();
		given(this.smUserRepository.findOne(1l)).willReturn(smUser);
		smUserService.findById(1l);
	}
	@Test
	public void testFindByIdWithNull() throws Exception{
		try{
			smUserService.findById(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindAll() throws Exception{
		smUserService.findAll();
	}
	
}
