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
import com.deloitte.smt.entity.ExternalDatasets;
import com.deloitte.smt.service.ExternalDatasetsService;
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
public class ExternalDatasetsControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	ExternalDatasetsService externalDatasetsServiceMock;
	
	ExternalDatasetsService externalDatasetsServiceMock1;

	@Before
	public void setUp(){
		externalDatasetsServiceMock1=mock(ExternalDatasetsService.class);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCreateExternalDatasets() throws Exception {
		when(externalDatasetsServiceMock.insert(anyList())).thenReturn(anyList());
		
		List<ExternalDatasets> typeList=new ArrayList<>();
		ExternalDatasets externalDatasets=new ExternalDatasets();
		typeList.add(externalDatasets);
		
		this.mockMvc
				.perform(post("/camunda/api/signal/ae")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(TestUtil.convertObjectToJsonBytes(typeList)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

		verify(externalDatasetsServiceMock, times(1)).insert(anyList());
		verifyNoMoreInteractions(externalDatasetsServiceMock);
	}
	
	@Test
	public void testUpdateExternalDatasets() throws Exception {
		ExternalDatasets externalDatasets=new ExternalDatasets();

		when(externalDatasetsServiceMock.update(Matchers.any(ExternalDatasets.class))).thenReturn(externalDatasets);

		this.mockMvc
				.perform(put("/camunda/api/signal/ae")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(TestUtil.convertObjectToJsonBytes(externalDatasets)))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));	
	}
	
	@Test
	public void testDeleteExternalDatasets() throws Exception {
		
		doNothing().when(externalDatasetsServiceMock).delete(anyLong());
		
		this.mockMvc
				.perform(delete("/camunda/api/signal/ae/{externalDatasetsId}",1)
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk());

		verify(externalDatasetsServiceMock, times(1)).delete(anyLong());
		verifyNoMoreInteractions(externalDatasetsServiceMock);
	}
	
	@Test
	public void testFindExternalDatasetsById() throws Exception {
		ExternalDatasets externalDatasets=new ExternalDatasets();

		when(externalDatasetsServiceMock.findById(anyLong())).thenReturn(externalDatasets);

		this.mockMvc
				.perform(get("/camunda/api/signal/ae/{includeAEId}", 1)
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"));

		verify(externalDatasetsServiceMock, times(1)).findById(anyLong());
		verifyNoMoreInteractions(externalDatasetsServiceMock);
	}
	
	@Test
	public void testFindAll() throws Exception {
		List<ExternalDatasets> externalDatasets = new ArrayList<>();

		when(externalDatasetsServiceMock.findAll()).thenReturn(externalDatasets);

		this.mockMvc
				.perform(get("/camunda/api/signal/ae", 1)
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"));

		verify(externalDatasetsServiceMock, times(1)).findAll();
		verifyNoMoreInteractions(externalDatasetsServiceMock);

	}
}