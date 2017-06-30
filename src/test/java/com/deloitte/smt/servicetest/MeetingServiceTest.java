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
import com.deloitte.smt.entity.Meeting;
import com.deloitte.smt.repository.MeetingRepository;
import com.deloitte.smt.service.AttachmentService;
import com.deloitte.smt.service.MeetingService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class MeetingServiceTest {
	
	private static final Logger LOG = Logger.getLogger(MeetingServiceTest.class);
	
	@Autowired
	private MeetingService meetingService;
	
	@MockBean
	MeetingRepository meetingRepository;

	@MockBean
	AttachmentService attachmentService;
	
		
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
	public void testInsert() {
		try{
			Meeting meeting = new Meeting();
			meeting.setId(1l);
			meetingService.insert(meeting, null);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testInsertWithNull() {
		try{
			Meeting meeting = new Meeting();
			meetingService.insert(meeting, null);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testUpdate() {
		try{
			Meeting meeting = new Meeting();
			meeting.setId(1l);
			meetingService.update(meeting, null);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testUpdateWithNull() {
		try{
			Meeting meeting = new Meeting();
			meetingService.update(meeting, null);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testDelete() throws Exception{
		Meeting meeting = new Meeting();
		given(this.meetingRepository.findOne(1l)).willReturn(meeting);
		meetingService.delete(1l);
	}
	
	@Test
	public void testDeleteWithNull() {
		try{
			meetingService.delete(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindById() throws Exception{
		meetingService.findById(1l);
	}
	
	@Test
	public void testFindAllyByResourceIdAndMeetingType() {
		try{
			meetingService.findAllyByResourceIdAndMeetingType(1l, null);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
}
