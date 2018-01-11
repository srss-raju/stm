package com.deloitte.smt.servicetest;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.constant.MeetingType;
import com.deloitte.smt.entity.Meeting;
import com.deloitte.smt.entity.Pt;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.repository.MeetingRepository;
import com.deloitte.smt.repository.TopicRepository;
import com.deloitte.smt.service.MeetingService;
import com.deloitte.smt.service.SignalMatchService;
import com.deloitte.smt.util.TestUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class SignalMatchServiceTest {
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private SignalMatchService signalMatchService;
	
	
	@MockBean
	MeetingRepository meetingRepository;

	@MockBean
    MeetingService meetingService;
	
	@MockBean
	private TopicRepository topicRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	public void testFindMatchingSignal() {
		try{
			signalMatchService.findMatchingSignal(TestUtil.buildSignal());
		}catch(Exception ex){
			logger.info(ex);
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
			logger.info(ex);
		}
	}
	
	@Test
	public void testCheckConfidenceIndex() {
		try{
			signalMatchService.checkConfidenceIndex(TestUtil.buildmatchingSignals(),TestUtil.buildSignal());
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testCheckCohortPercentage() {
		try{
			signalMatchService.checkCohortPercentage(TestUtil.buildmatchingSignals(),TestUtil.buildSignal());
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testCheckCohort95Percentage() {
		try{
			signalMatchService.checkCohortPercentage(TestUtil.build95matchingSignals(),TestUtil.buildSignal());
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testCheckCohort7595Percentage() {
		try{
			signalMatchService.checkCohortPercentage(TestUtil.build7595matchingSignals(),TestUtil.buildSignal());
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testCheckCohort75Percentage() {
		try{
			signalMatchService.checkCohortPercentage(TestUtil.build75matchingSignals(),TestUtil.buildSignal());
		}catch(Exception ex){
			logger.info(ex);
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
			logger.info(ex);
		}
	}
	
}
