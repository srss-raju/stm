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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.entity.SignalSources;
import com.deloitte.smt.service.SignalSourcesService;
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
public class SignalSourcesControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	SignalSourcesService signalSourcesServiceMock;
	
	SignalSourcesService signalSourcesServiceMock1;

	@Before
	public void setUp(){
		signalSourcesServiceMock1=mock(SignalSourcesService.class);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCreateSignalSources() throws Exception {
		when(signalSourcesServiceMock.insert(anyList())).thenReturn(anyList());
		
		List<SignalSources> typeList=new ArrayList<>();
		SignalSources type=new SignalSources();
		typeList.add(type);
		
		this.mockMvc
				.perform(post("/camunda/api/signal/sources")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(TestUtil.convertObjectToJsonBytes(typeList)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

		verify(signalSourcesServiceMock, times(1)).insert(anyList());
		verifyNoMoreInteractions(signalSourcesServiceMock);
	}
	
	@Test
	public void testUpdateSignalSource() throws Exception {
		SignalSources signalSource=new SignalSources();

		when(signalSourcesServiceMock.update(Matchers.any(SignalSources.class))).thenReturn(signalSource);

		this.mockMvc
				.perform(put("/camunda/api/signal/sources")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(TestUtil.convertObjectToJsonBytes(signalSource)))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));	
	}
	
	@Test
	public void testDeleteSignalSource() throws Exception {
		
		doNothing().when(signalSourcesServiceMock).delete(anyLong());
		
		this.mockMvc
				.perform(delete("/camunda/api/signal/sources/{signalSourceId}",1)
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk());

		verify(signalSourcesServiceMock, times(1)).delete(anyLong());
		verifyNoMoreInteractions(signalSourcesServiceMock);
	}
	
	@Test
	public void testFindSignalSourceById() throws Exception {
		SignalSources signalSource=new SignalSources();

		when(signalSourcesServiceMock.findById(anyLong())).thenReturn(signalSource);

		this.mockMvc
				.perform(get("/camunda/api/signal/sources/{signalSourceId}", 1)
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"));

		verify(signalSourcesServiceMock, times(1)).findById(anyLong());
		verifyNoMoreInteractions(signalSourcesServiceMock);
	}
	
	@Test
	public void testFindAll() throws Exception {
		List<SignalSources> signalSources = new ArrayList<>();

		when(signalSourcesServiceMock.findAll()).thenReturn(signalSources);

		this.mockMvc
				.perform(get("/camunda/api/signal/sources", 1)
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"));

		verify(signalSourcesServiceMock, times(1)).findAll();
		verifyNoMoreInteractions(signalSourcesServiceMock);

	}
}