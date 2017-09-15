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
import com.deloitte.smt.entity.Attachment;
import com.deloitte.smt.entity.SignalAudit;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.repository.MeetingRepository;
import com.deloitte.smt.repository.SignalAttachmentAuditRepository;
import com.deloitte.smt.repository.SignalAuditRepository;
import com.deloitte.smt.service.SignalAuditService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class SignalAuditServiceTest {
	
	private static final Logger LOG = Logger.getLogger(MeetingServiceTest.class);
	
	@Autowired
	private SignalAuditService signalAuditService;
	
	@MockBean
	MeetingRepository meetingRepository;

	@MockBean
	SignalAuditRepository signalAuditRepository;
	
	@MockBean
	SignalAttachmentAuditRepository signalAttachmentAuditRepository;
	
		
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
	public void testGetAuditDetails() {
		try{
			List<SignalAudit> allSignals = new ArrayList<>();
			SignalAudit signalAudit = new SignalAudit();
			signalAudit.setId(1l);
			allSignals.add(signalAudit);
			given(this.signalAuditRepository.findAll()).willReturn(allSignals);
			given(this.signalAttachmentAuditRepository.findAllByAttachmentResourceId(1l)).willReturn(null);
			signalAuditService.getAuditDetails();
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testSaveSignalAttachmentAudit() {
		try{
			SignalAudit signalAudit = new SignalAudit();
			signalAudit.setId(1l);
			List<Attachment> attchmentList = new ArrayList<>();
			Attachment attachment = new Attachment();
			attchmentList.add(attachment);
			signalAuditService.saveSignalAttachmentAudit(attchmentList, signalAudit);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testSaveSignalAttachmentAuditOriginalNotNull() throws Exception{
		try{
			SignalAudit signalAudit = new SignalAudit();
			signalAudit.setId(1l);
			List<Attachment> attchmentList = new ArrayList<>();
			Attachment attachment = new Attachment();
			attchmentList.add(attachment);
			
			Topic topicUpdated = new Topic();
			String topicOriginal = "SomeValue";
			String operation = "INSERT";
			
			signalAuditService.saveOrUpdateSignalAudit(topicUpdated, topicOriginal, attchmentList, operation);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
}
