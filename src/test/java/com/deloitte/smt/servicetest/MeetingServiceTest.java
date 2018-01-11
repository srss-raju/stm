package com.deloitte.smt.servicetest;


import static org.mockito.BDDMockito.given;

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
import com.deloitte.smt.entity.Meeting;
import com.deloitte.smt.repository.MeetingRepository;
import com.deloitte.smt.service.MeetingService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class MeetingServiceTest {
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private MeetingService meetingService;
	
	@MockBean
	MeetingRepository meetingRepository;

	
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
			logger.info(ex);
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
			logger.info(ex);
		}
	}
	
}
