package com.deloitte.smt.controllertest;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.Comments;
import com.deloitte.smt.entity.NonSignal;
import com.deloitte.smt.entity.Task;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.service.SignalService;
import com.deloitte.smt.util.TestUtil;

/**
 * 
 * @author RajeshKumar
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SignalManagementApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SignalControllerTest {

	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	SignalService signalServiceMock;
	
	SignalService signalServiceMock1;
	
	@Before
	public void setUp() {
		signalServiceMock1 = mock(SignalService.class);
	}
	
	@Test
	public void testCreateTopic() throws Exception{
		Topic topic = new Topic();
		when(signalServiceMock.createTopic(topic)).thenReturn(topic);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/camunda/api/signal/createTopic")
				.accept(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonString(topic))
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		logger.info(result);
	}
	
	@Test
	public void testUpdateTopic() throws Exception{
		
		Topic topic = new Topic();
		when(signalServiceMock.createTopic(topic)).thenReturn(topic);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/camunda/api/signal/updateTopic")
				.accept(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonString(topic))
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		logger.info(result);
		
	}
	
	

	@Test
	public void testGetTopicById()  {
		try{
			Topic topic = new Topic();
			when(signalServiceMock.findById(anyLong())).thenReturn(topic);
			this.mockMvc
					.perform(get("/camunda/api/signal/{signalId}", 1)
							.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testFindTopicsByRunInstanceId()  {
		try{
			List<Topic> signals= new ArrayList<>();
			List<NonSignal> nonSignals= new ArrayList<>();
			when(signalServiceMock.findTopicsByRunInstanceId(anyLong())).thenReturn(signals);
			when(signalServiceMock.findNonSignalsByRunInstanceId(anyLong())).thenReturn(nonSignals);
			this.mockMvc
					.perform(get("/camunda/api/signal/run/{runInstanceId}", 1)
							.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testUpdateComments() throws Exception{
		Topic topic = new Topic();
		List<Comments> comments =new ArrayList<>();
		when(signalServiceMock.updateComments(topic)).thenReturn(comments);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/camunda/api/signal/updateComments")
				.accept(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonString(topic))
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		logger.info(result);
	}
	
	
	@Test
	public void testGetTopicComments()  {
		try{
			List<Comments> comments= new ArrayList<>();
			when(signalServiceMock.getTopicComments(anyLong())).thenReturn(comments);
			this.mockMvc
					.perform(get("/camunda/api/signal/getTopicComments/{topicId}", 1)
							.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testValidateAndPrioritizeTopic() throws Exception{
		AssessmentPlan assessmentPlan = new AssessmentPlan();
		when(signalServiceMock.validateAndPrioritize(1l, assessmentPlan)).thenReturn(assessmentPlan);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put("/camunda/api/signal/{topicId}/validateAndPrioritize",1l)
				.accept(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonString(assessmentPlan))
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		logger.info(result);
	}
	
	@Test
	public void testAssociateTemplateTasks() throws Exception{
		AssessmentPlan assessmentPlan = new AssessmentPlan();
		List<Task> tasks = new ArrayList<>();
		when(signalServiceMock.associateTemplateTasks(assessmentPlan)).thenReturn(tasks);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/camunda/api/signal/template/associateTemplateTasks")
				.accept(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonString(assessmentPlan))
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		logger.info(result);
	}
	
	
}
