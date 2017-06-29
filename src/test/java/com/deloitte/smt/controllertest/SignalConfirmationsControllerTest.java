package com.deloitte.smt.controllertest;

import static org.mockito.Matchers.anyList;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.entity.SignalConfirmations;
import com.deloitte.smt.service.SignalConfirmationsService;
import com.deloitte.smt.util.TestUtil;

/**
 * 
 * @author RajeshKumar
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SignalManagementApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SignalConfirmationsControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	SignalConfirmationsService signalConfirmationsServiceMock;
	
	SignalConfirmationsService signalConfirmationsServiceMock1;

	@Before
	public void setUp(){
		signalConfirmationsServiceMock1=mock(SignalConfirmationsService.class);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCreateSignalConfirmations() throws Exception {
		when(signalConfirmationsServiceMock.insert(anyList())).thenReturn(anyList());
		
		List<SignalConfirmations> typeList=new ArrayList<>();
		SignalConfirmations type=new SignalConfirmations();
		typeList.add(type);
		
		this.mockMvc
				.perform(post("/camunda/api/signal/confirmations")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(TestUtil.convertObjectToJsonBytes(typeList)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

		verify(signalConfirmationsServiceMock, times(1)).insert(anyList());
		verifyNoMoreInteractions(signalConfirmationsServiceMock);
	}
	
	@Test
	public void testUpdateUser() throws Exception {
		SignalConfirmations signalConfirmations=new SignalConfirmations();

		when(signalConfirmationsServiceMock.update(Matchers.any(SignalConfirmations.class))).thenReturn(signalConfirmations);

		this.mockMvc
				.perform(put("/camunda/api/signal/confirmations")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(TestUtil.convertObjectToJsonBytes(signalConfirmations)))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));	
	}
	
	@Test
	public void testDeleteSignalSource() throws Exception {
		
		doNothing().when(signalConfirmationsServiceMock).delete(anyLong());
		
		this.mockMvc
				.perform(delete("/camunda/api/signal/confirmations/{signalConfirmationId}",1)
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk());

		verify(signalConfirmationsServiceMock, times(1)).delete(anyLong());
		verifyNoMoreInteractions(signalConfirmationsServiceMock);
	}
	
	@Test
	public void testFindSignalSourceById() throws Exception {
		SignalConfirmations signalConfirmations=new SignalConfirmations();

		when(signalConfirmationsServiceMock.findById(anyLong())).thenReturn(signalConfirmations);

		this.mockMvc
				.perform(get("/camunda/api/signal/confirmations/{signalConfirmationId}", 1)
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"));

		verify(signalConfirmationsServiceMock, times(1)).findById(anyLong());
		verifyNoMoreInteractions(signalConfirmationsServiceMock);
	}
	
	@Test
	public void testFindAll() throws Exception {
		List<SignalConfirmations> signalConfirmations = new ArrayList<>();

		when(signalConfirmationsServiceMock.findAll()).thenReturn(signalConfirmations);

		this.mockMvc
				.perform(get("/camunda/api/signal/confirmations", 1)
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"));

		verify(signalConfirmationsServiceMock, times(1)).findAll();
		verifyNoMoreInteractions(signalConfirmationsServiceMock);

	}
}