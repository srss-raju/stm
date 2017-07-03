package com.deloitte.smt.controllertest;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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
import com.deloitte.smt.constant.AttachmentType;
import com.deloitte.smt.entity.Attachment;
import com.deloitte.smt.service.AttachmentService;


/**
 * 
 * @author cavula
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SignalManagementApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AttachmentControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	AttachmentService attachmentServiceMock;

	AttachmentService attachmentServiceMock1;

	@Test
	public void testFindAllBySignalId() throws Exception {
		List<Attachment> attachmentList=new ArrayList();
	
		when(attachmentServiceMock.findByResourceIdAndAttachmentType(anyLong(),Matchers.any(AttachmentType.TOPIC_ATTACHMENT.getClass()))).thenReturn(attachmentList);
		
		this.mockMvc
		.perform(get("/camunda/api/signal/attachment/{signalId}",1)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(status().isOk());
		
	}
	
	@Test
	public void testFindAllByAssessmentId() throws Exception {
		List<Attachment> attachmentList=new ArrayList();
	
		when(attachmentServiceMock.findByResourceIdAndAttachmentType(anyLong(),Matchers.any(AttachmentType.ASSESSMENT_ATTACHMENT.getClass()))).thenReturn(attachmentList);
		
		this.mockMvc
		.perform(get("/camunda/api/signal/attachment/assessment/{assessmentId}",1)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(status().isOk());
		
	}
	
	@Test
	public void testFindAllByAssessmentActionId() throws Exception {
		List<Attachment> attachmentList=new ArrayList();
	
		when(attachmentServiceMock.findByResourceIdAndAttachmentType(anyLong(),Matchers.any(AttachmentType.ASSESSMENT_ACTION_ATTACHMENT.getClass()))).thenReturn(attachmentList);
		
		this.mockMvc
		.perform(get("/camunda/api/signal/attachment/assessment/action/{assessmentActionId}",1)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(status().isOk());
		
	}
	
	
	@Test
	public void testDeleteAttachment() throws Exception {
	
		doNothing().when(attachmentServiceMock).delete(anyLong());
		
		this.mockMvc
		.perform(delete("/camunda/api/signal/attachment/{attachmentId}",1)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(status().isOk());
		
	}
	
	@Test
	public void testFindAllByRiskId() throws Exception {
		List<Attachment> attachmentList=new ArrayList();
		when(attachmentServiceMock.findByResourceIdAndAttachmentType(anyLong(),Matchers.any(AttachmentType.RISK_ASSESSMENT.getClass()))).thenReturn(attachmentList);

		
		this.mockMvc
		.perform(get("/camunda/api/signal/attachment/risk/{riskId}",1)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(status().isOk());
		
	}
	
	@Test
	public void testFindAllByRiskTaskId() throws Exception {
		List<Attachment> attachmentList=new ArrayList();
		when(attachmentServiceMock.findByResourceIdAndAttachmentType(anyLong(),Matchers.any(AttachmentType.RISK_TASK_ASSESSMENT.getClass()))).thenReturn(attachmentList);

		
		this.mockMvc
		.perform(get("/camunda/api/signal/attachment/risk/task/{riskTaskId}",1)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(status().isOk());
		
	}
	
	@Test
	public void testFindAllByMeetingId() throws Exception {
		List<Attachment> attachmentList=new ArrayList();
		when(attachmentServiceMock.findByResourceIdAndAttachmentType(anyLong(),Matchers.any(AttachmentType.MEETING_ATTACHMENT.getClass()))).thenReturn(attachmentList);

		
		this.mockMvc
		.perform(get("/camunda/api/signal/attachment/meeting/{meetingId}",1)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(status().isOk());
		
	}
	
	@Test
	public void testDownloadContent() throws Exception {
		List<Attachment> attachmentList=new ArrayList();
		
		Attachment attachment=new Attachment();
		String msg="Test Message";
		attachment.setContent(msg.getBytes());
		when(attachmentServiceMock.findById(anyLong())).thenReturn(attachment);

		
		this.mockMvc
		.perform(get("/camunda/api/signal/attachment/content/{attachmentId}",1)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(status().isOk());
		
	}
	
}
