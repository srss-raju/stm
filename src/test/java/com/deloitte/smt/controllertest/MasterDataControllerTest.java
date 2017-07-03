package com.deloitte.smt.controllertest;

import org.junit.runner.RunWith;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.service.SearchService;

import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = SignalManagementApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MasterDataControllerTest {


	@Autowired
	private MockMvc mockMvc;

	@MockBean
	 SearchService searchService;

	
	@Test
	public void testGetAllActionTypes()throws Exception{
		this.mockMvc
				.perform(get("/camunda/api/signal/utils/actionTypes")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	}
	
	@Test
	public void testGetAllActionStatuses()throws Exception{
		this.mockMvc
				.perform(get("/camunda/api/signal/utils/actionStatus")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	}
	
	
	
	@Test
	public void testGetFiltersForSignal()throws Exception{
		SearchDto searchDto=new SearchDto();
		when(searchService.getFiltersForSignal()).thenReturn(searchDto);
		this.mockMvc
				.perform(get("/camunda/api/signal/utils/filters/signal")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	}
	
	
	@Test
	public void testGetFiltersForSignalDetection()throws Exception{
		SearchDto searchDto=new SearchDto();
		when(searchService.getFiltersForSignalDetection()).thenReturn(searchDto);
		this.mockMvc
				.perform(get("/camunda/api/signal/utils/filters/signalDetection")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	}
	
	@Test
	public void testGetFiltersForAssessmentPlan()throws Exception{
		SearchDto searchDto=new SearchDto();
		when(searchService.getAllFiltersForAssessmentPlan()).thenReturn(searchDto);
		this.mockMvc
				.perform(get("/camunda/api/signal/utils/filters/assessmentPlan")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	}
	
	@Test
	public void testGetFiltersForRiskPlan()throws Exception{
		SearchDto searchDto=new SearchDto();
		when(searchService.getAllFiltersForRiskPlan()).thenReturn(searchDto);
		this.mockMvc
				.perform(get("/camunda/api/signal/utils/filters/riskPlan")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	}
	
	@Test
	public void testGetIngredientFilters()throws Exception{
		List<String> values=new ArrayList();
		
		when(searchService.getIngredients()).thenReturn(values);
		this.mockMvc
				.perform(get("/camunda/api/signal/utils/filters/ingredients")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	}
	
	@Test
	public void testGetSeverityFilters()throws Exception{
		this.mockMvc
				.perform(get("/camunda/api/signal/utils/filters/severity")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	}
	
}
