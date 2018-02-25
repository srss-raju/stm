package com.deloitte.smt.controllertest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
public class TaskControllerTest {

	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	TaskService  taskServiceMock;
	
	TaskService  taskServiceMock1;
	
	@Before
	public void setUp() {
		taskServiceMock1 = mock(TaskService.class);
	}
	
	@Test
	public void testCreateTask() throws Exception{
		
		Task  task=new Task ();
		
		when(taskServiceMock.createTask(task)).thenReturn(task);

		this.mockMvc
				.perform(post("/camunda/api/signal/{type}/task","risk")
						.param("data",TestUtil.convertObjectToJsonString(task))
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk());

	}
	
	@Test
	public void testCreateTaskTemplate() throws Exception{
		
		Task  task=new Task ();
		task.setTemplateId(1l);
		
		when(taskServiceMock.createTask(task)).thenReturn(task);

		this.mockMvc
				.perform(post("/camunda/api/signal/{type}/task","risk")
						.param("data",TestUtil.convertObjectToJsonString(task))
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk());

	}
	
	@Test
	public void testUpdateTask() throws Exception{
		
		Task  task=new Task ();
		
		this.mockMvc
				.perform(put("/camunda/api/signal/{type}/task")
						.param("data",TestUtil.convertObjectToJsonString(task))
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk());

	}
	

	@Test
	public void testFindAll()  {
		try{
			List<Task> tasks = new ArrayList<>();
			when(taskServiceMock.findAll("risk",1l)).thenReturn(tasks);
			this.mockMvc
					.perform(get("/camunda/api/signal/{type}/task/all/{id}", "risk",1)
							.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testFfindByTemplateId()  {
		try{
			Task  task=new Task ();
			when(taskServiceMock.findById(1l)).thenReturn(task);
			this.mockMvc
					.perform(get("/camunda/api/signal/{type}/task/{id}", "risk",1)
							.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testDeleteById()  {
		try{
			this.mockMvc
					.perform(delete("/camunda/api/signal/{type}/task/{taskId}", "risk",1)
							.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
}
