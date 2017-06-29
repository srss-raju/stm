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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.entity.SMUser;
import com.deloitte.smt.service.SMUserService;
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
public class SMUserControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	SMUserService smUserServiceMock;
	
	SMUserService smUserServiceMock1;

	@Before
	public void setUp(){
		smUserServiceMock1=mock(SMUserService.class);
	}
	
	@Test
	public void testCreateNewUser() throws Exception {
		SMUser smUser = new SMUser();
		when(smUserServiceMock.insert(Matchers.any(SMUser.class))).thenReturn(smUser);
		this.mockMvc
				.perform(post("/camunda/api/signal/user")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(TestUtil.convertObjectToJsonBytes(smUser)))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		
	}
	
	@Test
	public void testUpdateUser() throws Exception {
		SMUser smUser = new SMUser();

		when(smUserServiceMock.update(Matchers.any(SMUser.class))).thenReturn(smUser);

		this.mockMvc
				.perform(put("/camunda/api/signal/user")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(TestUtil.convertObjectToJsonBytes(smUser)))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));	
	}
	
	@Test
	public void testDeleteUser() throws Exception {
		
		doNothing().when(smUserServiceMock).delete(anyLong());
		
		this.mockMvc
				.perform(delete("/camunda/api/signal/user/{userId}",1)
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk());

		verify(smUserServiceMock, times(1)).delete(anyLong());
		verifyNoMoreInteractions(smUserServiceMock);

	}
	
	@Test
	public void testFindAssignmentConfigurationById() throws Exception {
		SMUser smUser = new SMUser();

		when(smUserServiceMock.findById(anyLong())).thenReturn(smUser);

		this.mockMvc
				.perform(get("/camunda/api/signal/user/{userId}", 1)
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"));

		verify(smUserServiceMock, times(1)).findById(anyLong());
		verifyNoMoreInteractions(smUserServiceMock);
	}
	
	@Test
	public void testFindAll() throws Exception {
		List<SMUser> users = new ArrayList<>();

		when(smUserServiceMock.findAll()).thenReturn(users);

		this.mockMvc
				.perform(get("/camunda/api/signal/user", 1)
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"));

		verify(smUserServiceMock, times(1)).findAll();
		verifyNoMoreInteractions(smUserServiceMock);

	}
	
}