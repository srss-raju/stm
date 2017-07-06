package com.deloitte.smt.servicetest;

import static org.mockito.BDDMockito.given;

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
import com.deloitte.smt.constant.AttachmentType;
import com.deloitte.smt.constant.MeetingType;
import com.deloitte.smt.entity.Attachment;
import com.deloitte.smt.entity.Meeting;
import com.deloitte.smt.entity.Pt;
import com.deloitte.smt.entity.SignalURL;
import com.deloitte.smt.entity.Topic;
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
	
	
	@MockBean
	MeetingRepository meetingRepository;

	@MockBean
	AttachmentService attachmentService;
	
	@MockBean
    MeetingService meetingService;
	
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
	
	@Test
	public void testCheckCohort95Percentage() {
		try{
			signalMatchService.checkCohortPercentage(TestUtil.build95matchingSignals(),TestUtil.buildSignal());
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testCheckCohort7595Percentage() {
		try{
			signalMatchService.checkCohortPercentage(TestUtil.build7595matchingSignals(),TestUtil.buildSignal());
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testCheckCohort75Percentage() {
		try{
			signalMatchService.checkCohortPercentage(TestUtil.build75matchingSignals(),TestUtil.buildSignal());
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testApplyMatchingSignalUrls() {
		try{
			List<SignalURL> matchingTopicSignalUrls = new ArrayList<>();
			SignalURL signalURL = new SignalURL();
			matchingTopicSignalUrls.add(signalURL);
			Topic t1 = TestUtil.buildSignal();
			t1.setId(1l);
			Topic t2 = TestUtil.buildSignal();
			t2.setId(1l);
			given(this. signalURLRepository.findByTopicId(1l)).willReturn(matchingTopicSignalUrls);
			signalMatchService.applyMatchingSignalUrls(t1,t2);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testApplyMatchingSignalMeetings() {
		try{
			List<Meeting> meetings = new ArrayList<>();
			Meeting meeting = new Meeting();
			meetings.add(meeting);
			Topic t1 = TestUtil.buildSignal();
			t1.setId(1l);
			given(this. meetingService.findAllyByResourceIdAndMeetingType(1l, MeetingType.getByDescription("Signal Meeting"))).willReturn(meetings);
			signalMatchService.applyMatchingSignalMeetings(t1);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testApplyMatchingSignalAttachments() {
		try{
			Topic t1 = TestUtil.buildSignal();
			t1.setId(1l);
			Topic t2 = TestUtil.buildSignal();
			t2.setId(1l);
			List<Attachment> matchingTopicAttachments = new ArrayList<>();
			Attachment attachment = new Attachment();
			attachment.setAttachmentsURL("test");
			matchingTopicAttachments.add(attachment);
			given(this. attachmentService.findByResourceIdAndAttachmentType(1l, AttachmentType.TOPIC_ATTACHMENT)).willReturn(matchingTopicAttachments);
			signalMatchService.applyMatchingSignalAttachments(t1,t2);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
}
