package com.deloitte.smt.servicetest;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
import com.deloitte.smt.entity.Pt;
import com.deloitte.smt.repository.AttachmentRepository;
import com.deloitte.smt.repository.MeetingRepository;
import com.deloitte.smt.repository.SignalURLRepository;
import com.deloitte.smt.repository.TopicRepository;
import com.deloitte.smt.service.AttachmentService;
import com.deloitte.smt.service.MeetingService;
import com.deloitte.smt.service.SignalMatchService;
import com.deloitte.smt.util.TestUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class SignalMatchServiceTest {
	
	private static final Logger LOG = Logger.getLogger(SignalMatchServiceTest.class);
	
	@Autowired
	private SignalMatchService signalMatchService;
	
	@Autowired
	private MeetingService meetingService;
	
	@MockBean
	MeetingRepository meetingRepository;

	@MockBean
	AttachmentService attachmentService;
	
	@MockBean
	private TopicRepository topicRepository;

	@MockBean
	SignalURLRepository signalURLRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@MockBean
	AttachmentRepository attachmentRepository;
	
	

	
		
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
	public void testFindMatchingSignal() {
		try{
			signalMatchService.findMatchingSignal(TestUtil.buildSignal());
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testbuildPts() {
		try{
			StringBuilder ptBuilder = new StringBuilder();
			List<Pt> pts = new ArrayList<>();
			Pt pt = new Pt();
			pt.setPtName("test");
			pts.add(pt);
			signalMatchService.buildPts(ptBuilder, pts);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testCheckConfidenceIndex() {
		try{
			signalMatchService.checkConfidenceIndex(TestUtil.buildmatchingSignals(),TestUtil.buildSignal());
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testCheckCohortPercentage() {
		try{
			signalMatchService.checkCohortPercentage(TestUtil.buildmatchingSignals(),TestUtil.buildSignal());
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
}
