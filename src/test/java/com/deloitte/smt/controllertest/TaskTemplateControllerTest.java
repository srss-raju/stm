package com.deloitte.smt.controllertest;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
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

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.entity.TaskTemplate;
import com.deloitte.smt.service.TaskTemplateService;
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
public class TaskTemplateControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	TaskTemplateService taskTemplateServiceMock;
	
	TaskTemplateService taskTemplateServiceMock1;

	@Before
	public void setUp(){
		taskTemplateServiceMock1=mock(TaskTemplateService.class);
	}
	
	@Test
	public void testCreateTaskTemplate() throws Exception {
		TaskTemplate taskTemplate = new TaskTemplate();
		when(taskTemplateServiceMock.createTaskTemplate(taskTemplate)).thenReturn(taskTemplate);
		this.mockMvc
				.perform(post("/camunda/api/signal/template/createTaskTemplate")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.param("data", TestUtil.convertObjectToJsonString(taskTemplate)))
						
				.andExpect(status().isOk());
	}
	
	@Test
	public void testUpdateTaskTemplate() throws Exception {
		TaskTemplate taskTemplate = new TaskTemplate();
		when(taskTemplateServiceMock.updateTaskTemplate(taskTemplate)).thenReturn(taskTemplate);
		this.mockMvc
				.perform(post("/camunda/api/signal/template/updateTaskTemplate")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.param("data", TestUtil.convertObjectToJsonString(taskTemplate)))
						
				.andExpect(status().isOk());
	}
	
	@Test
	public void testDeleteById() throws Exception {
		
		doNothing().when(taskTemplateServiceMock).delete(anyLong());
		
		this.mockMvc
				.perform(delete("/camunda/api/signal/template/{taskId}",1)
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk());

		verify(taskTemplateServiceMock, times(1)).delete(anyLong());
		verifyNoMoreInteractions(taskTemplateServiceMock);

	}
	
	@Test
	public void testFindAll() throws Exception {
		List<TaskTemplate> taskTemplates = new ArrayList<>();

		when(taskTemplateServiceMock.findAll()).thenReturn(taskTemplates);

		this.mockMvc
				.perform(get("/camunda/api/signal/template/all", 1)
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"));

		verify(taskTemplateServiceMock, times(1)).findAll();
		verifyNoMoreInteractions(taskTemplateServiceMock);

	}
	
	@Test
	public void testFindByTemplateId() throws Exception {
		TaskTemplate taskTemplate = new TaskTemplate();

		when(taskTemplateServiceMock.findById(anyLong())).thenReturn(taskTemplate);

		this.mockMvc
				.perform(get("/camunda/api/signal/template/{templateId}", 1)
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"));

		verify(taskTemplateServiceMock, times(1)).findById(anyLong());
		verifyNoMoreInteractions(taskTemplateServiceMock);
	}

}