package com.deloitte.smt.controllertest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.entity.Task;
import com.deloitte.smt.service.TaskService;
import com.deloitte.smt.util.TestUtil;

/**
 * 
 * @author RajeshKumar
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SignalManagementApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = {"classpath:test.properties"})
public class TaskControllerTest {

	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	TaskService  taskServiceMock;
	
	
	@Test
	public void testCreateTaskWithTemplate() throws Exception{
		Task task = new Task();
		task.setTemplateId(1l);
		when(taskServiceMock.createTemplateTask(task)).thenReturn(task);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/camunda/api/signal/{type}/task","Signal")
				.accept(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonString(task))
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		logger.info(result);
	}
	
	@Test
	public void testCreateTaskNoTemplate() throws Exception{
		Task task = new Task();
		when(taskServiceMock.createTask(task)).thenReturn(task);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/camunda/api/signal/{type}/task","Signal")
				.accept(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonString(task))
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		logger.info(result);
	}
	
	@Test
	public void testUpdate() throws Exception{
		Task task = new Task();
		when(taskServiceMock.updateAssessmentAction(task)).thenReturn(task);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put("/camunda/api/signal/{type}/task","Signal")
				.accept(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonString(task))
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		logger.info(result);
	}
	
	@Test
	public void testFindById()  {
		try{
			Task task = new Task();
			when(taskServiceMock.findById(1l)).thenReturn(task);
			this.mockMvc
					.perform(get("/camunda/api/signal/{type}/task/{taskId}","Signal",1l)
							.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testFindAll()  {
		try{
			List<Task> tasks = new ArrayList<>();
			when(taskServiceMock.findAll("Signal",1l)).thenReturn(tasks);
			this.mockMvc
					.perform(get("/camunda/api/signal/{type}/task/{taskId}","Signal",1l)
							.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
}
