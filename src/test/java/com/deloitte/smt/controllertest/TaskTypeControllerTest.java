package com.deloitte.smt.controllertest;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import com.deloitte.smt.entity.TaskType;
import com.deloitte.smt.service.TaskTypeService;
import com.deloitte.smt.util.TestUtil;

/**
 * 
 * @author cavula
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SignalManagementApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = {"classpath:test.properties"})
public class TaskTypeControllerTest {

	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	TaskTypeService assessmentActionTypeServiceMock;
	


	@Test
	public void testCreateNewTaskTypeWithTe() throws Exception {
		List<TaskType> list = new ArrayList<>();
		
		when(assessmentActionTypeServiceMock.insert(list)).thenReturn(list);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/camunda/api/signal/{type}/tasktype","Signal")
				.accept(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonString(list))
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		logger.info(result);
		
		
	}
	
	
	@Test
	public void testUpdateTaskType() throws Exception {
		List<TaskType> list = new ArrayList<>();
		
		when(assessmentActionTypeServiceMock.insert(list)).thenReturn(list);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put("/camunda/api/signal/{type}/tasktype","Signal")
				.accept(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonString(list))
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		logger.info(result);
		
	}
	
	
	@Test
	public void testFindAssessmentActionTypeById() throws Exception {
		try{
			TaskType  taskType  = new TaskType ();
			when(assessmentActionTypeServiceMock.findById(1l)).thenReturn(taskType);
			this.mockMvc
					.perform(get("/camunda/api/signal/{type}/tasktype/{assessmentActionTypeId}","Signal",1l)
							.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		}catch(Exception ex){
			ex.printStackTrace();
		}

	}
	
	@Test
	public void testDeleteTaskType() throws Exception{

		doNothing().when(assessmentActionTypeServiceMock).delete(anyLong());

		this.mockMvc
				.perform(delete("/camunda/api/signal/{type}/tasktype/{assessmentActionTypeId}","Signal",1l)
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetAll()  {
		try{
			List<TaskType> taskTypes = new ArrayList<>();
			when(assessmentActionTypeServiceMock.findAll()).thenReturn(taskTypes);
			this.mockMvc
					.perform(get("/camunda/api/signal/{type}/tasktype","Signal")
							.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	
}