package com.deloitte.smt.controllertest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
	
	@Test
	public void testCreateNewAssignmentConfiguration() throws Exception {
		AssignmentConfiguration assignConfiguration=new AssignmentConfiguration();

		when(assignmentConfigurationServiceMock.insert(Matchers.any(AssignmentConfiguration.class))).thenReturn(assignConfiguration);
		
		this.mockMvc
				.perform(post("/camunda/api/signal/assignmentConfiguration")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(TestUtil.convertObjectToJsonBytes(assignConfiguration)))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

	}
	
}
