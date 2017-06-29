package com.deloitte.smt.controllertest;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.service.AssignmentConfigurationService;
import com.deloitte.smt.util.TestUtil;

/**
 * 
 * @author cavula
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SignalManagementApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AssignmentConfigurationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	AssignmentConfigurationService assignmentConfigurationServiceMock;

	AssignmentConfigurationService assignmentConfigurationServiceMock1;

	@Before
	public void setUp() {
		assignmentConfigurationServiceMock1 = mock(AssignmentConfigurationService.class);
	}

	@Test
	public void testCreateNewAssignmentConfiguration() throws Exception {
		AssignmentConfiguration assignConfiguration = new AssignmentConfiguration();

		when(assignmentConfigurationServiceMock.insert(Matchers.any(AssignmentConfiguration.class)))
				.thenReturn(assignConfiguration);

		this.mockMvc
				.perform(post("/camunda/api/signal/assignmentConfiguration")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(TestUtil.convertObjectToJsonBytes(assignConfiguration)))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

	}

	@Test
	public void testUpdateAssignmentConfiguration() throws Exception {
		AssignmentConfiguration assignConfiguration = new AssignmentConfiguration();

		when(assignmentConfigurationServiceMock.update(Matchers.any(AssignmentConfiguration.class)))
				.thenReturn(assignConfiguration);

		this.mockMvc
				.perform(put("/camunda/api/signal/assignmentConfiguration")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(TestUtil.convertObjectToJsonBytes(assignConfiguration)))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	}

	@Test
	public void testDeleteAssignmentConfiguration() throws  Exception{
		doNothing().when(assignmentConfigurationServiceMock1).delete(anyLong());
		
		this.mockMvc
				.perform(delete("/camunda/api/signal/assignmentConfiguration/{assignmentConfigurationId}",1)
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk());
		
	}

	
}
