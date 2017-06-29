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
import com.deloitte.smt.entity.FinalDispositions;
import com.deloitte.smt.service.FinalDispositionService;
import com.deloitte.smt.util.TestUtil;

/**
 * 
 * @author RajeshKumar
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SignalManagementApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class FinalDispositionsControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	FinalDispositionService finalDispositionServiceMock;
	
	FinalDispositionService finalDispositionServiceMock1;

	@Before
	public void setUp(){
		finalDispositionServiceMock1=mock(FinalDispositionService.class);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCreateRiskPlanActionType() throws Exception {
		when(finalDispositionServiceMock.insert(anyList())).thenReturn(anyList());
		
		List<FinalDispositions> typeList=new ArrayList<>();
		FinalDispositions finalDispositions=new FinalDispositions();
		typeList.add(finalDispositions);
		
		this.mockMvc
				.perform(post("/camunda/api/signal/finalDispositions")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(TestUtil.convertObjectToJsonBytes(typeList)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

		verify(finalDispositionServiceMock, times(1)).insert(anyList());
		verifyNoMoreInteractions(finalDispositionServiceMock);
	}
	
	@Test
	public void testUpdateUser() throws Exception {
		FinalDispositions finalDispositions=new FinalDispositions();

		when(finalDispositionServiceMock.update(Matchers.any(FinalDispositions.class))).thenReturn(finalDispositions);

		this.mockMvc
				.perform(put("/camunda/api/signal/finalDispositions")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(TestUtil.convertObjectToJsonBytes(finalDispositions)))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));	
	}
	
	@Test
	public void testDeleteSignalSource() throws Exception {
		
		doNothing().when(finalDispositionServiceMock).delete(anyLong());
		
		this.mockMvc
				.perform(delete("/camunda/api/signal/finalDispositions/{finalDispositionId}",1)
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk());

		verify(finalDispositionServiceMock, times(1)).delete(anyLong());
		verifyNoMoreInteractions(finalDispositionServiceMock);
	}
	
	@Test
	public void testFindSignalSourceById() throws Exception {
		FinalDispositions finalDispositions=new FinalDispositions();

		when(finalDispositionServiceMock.findById(anyLong())).thenReturn(finalDispositions);

		this.mockMvc
				.perform(get("/camunda/api/signal/finalDispositions/{finalDispositionId}", 1)
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"));

		verify(finalDispositionServiceMock, times(1)).findById(anyLong());
		verifyNoMoreInteractions(finalDispositionServiceMock);
	}
	
	@Test
	public void testFindAll() throws Exception {
		List<FinalDispositions> finalDispositions = new ArrayList<>();

		when(finalDispositionServiceMock.findAll()).thenReturn(finalDispositions);

		this.mockMvc
				.perform(get("/camunda/api/signal/finalDispositions", 1)
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"));

		verify(finalDispositionServiceMock, times(1)).findAll();
		verifyNoMoreInteractions(finalDispositionServiceMock);

	}
}