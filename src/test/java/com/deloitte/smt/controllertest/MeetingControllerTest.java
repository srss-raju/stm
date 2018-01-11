package com.deloitte.smt.controllertest;


import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.constant.MeetingType;
import com.deloitte.smt.entity.Meeting;
import com.deloitte.smt.service.MeetingService;
import com.deloitte.smt.util.TestUtil;


/**
 * 
 * @author cavula
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SignalManagementApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MeetingControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	MeetingService meetingServiceMock;
	
	MeetingService meetingServiceMock1;

	@Before
	public void setUp(){
		meetingServiceMock1=mock(MeetingService.class);
	}
	
	@Test
	public void testCreateMeeting() throws IOException, Exception{
		
		doNothing().when(meetingServiceMock).insert(Matchers.any(Meeting.class));
		
		Meeting meeting=new Meeting();
		this.mockMvc
				.perform(post("/camunda/api/signal/meeting/create")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.param("meetingType", "Signal Meeting")
						.param("data", TestUtil.convertObjectToJsonString(meeting)))
				.andExpect(status().isOk());
	}
	
	@Test
	public void testUpdateMeeting() throws IOException, Exception{
		
		doNothing().when(meetingServiceMock).update(Matchers.any(Meeting.class));
		
		Meeting meeting=new Meeting();
		this.mockMvc
				.perform(post("/camunda/api/signal/meeting/update")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.param("data", TestUtil.convertObjectToJsonString(meeting)))
				.andExpect(status().isOk());
	}
	
	
	@Test
	public void testGetMeeting() throws IOException, Exception{
		
		Meeting meeting = new Meeting();
		when(meetingServiceMock.findById(anyLong())).thenReturn(meeting);
		
		this.mockMvc
				.perform(get("/camunda/api/signal/meeting/byId/{meetingId}",1)
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk());
	}
	
	
	@Test
	public void testGetAllMeetings() throws IOException, Exception{
		List<Meeting> meetingList=new ArrayList<>();
		Meeting meeting = new Meeting();
		meetingList.add(meeting);
		
		when(meetingServiceMock.findAllyByResourceIdAndMeetingType(anyLong(),Matchers.any(MeetingType.class))).thenReturn(meetingList);
		
		this.mockMvc
				.perform(get("/camunda/api/signal/meeting/{meetingResourceId}",1)
						.param("meetingType","Signal Meeting")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk());
	}
	
	
	@Test
	public void testDeleteMeeting() throws IOException, Exception{
		
		List<Meeting> meetingList=new ArrayList<>();
		Meeting meeting = new Meeting();
		meetingList.add(meeting);
		
		doNothing().when(meetingServiceMock).delete(anyLong());
		
		this.mockMvc
				.perform(delete("/camunda/api/signal/meeting/byId/{meetingId}",1)
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk());
	}
	
}
