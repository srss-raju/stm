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
import com.deloitte.smt.entity.RiskPlanActionTaskType;
import com.deloitte.smt.service.RiskPlanActionTypeService;
import com.deloitte.smt.util.TestUtil;

/**
 * 
 * @author RajeshKumar
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SignalManagementApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RiskPlanActionTypeControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	RiskPlanActionTypeService riskPlanActionTypeServiceMock;
	
	RiskPlanActionTypeService riskPlanActionTypeServiceMock1;

	@Before
	public void setUp(){
		riskPlanActionTypeServiceMock1=mock(RiskPlanActionTypeService.class);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCreateRiskPlanActionType() throws Exception {
		when(riskPlanActionTypeServiceMock.insert(anyList())).thenReturn(anyList());
		
		List<RiskPlanActionTaskType> typeList=new ArrayList<>();
		RiskPlanActionTaskType riskPlanActionTaskType=new RiskPlanActionTaskType();
		typeList.add(riskPlanActionTaskType);
		
		this.mockMvc
				.perform(post("/camunda/api/signal/riskPlanActionType")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(TestUtil.convertObjectToJsonBytes(typeList)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

		verify(riskPlanActionTypeServiceMock, times(1)).insert(anyList());
		verifyNoMoreInteractions(riskPlanActionTypeServiceMock);
	}
	
	@Test
	public void testUpdateUser() throws Exception {
		RiskPlanActionTaskType riskPlanActionTaskType=new RiskPlanActionTaskType();

		when(riskPlanActionTypeServiceMock.update(Matchers.any(RiskPlanActionTaskType.class))).thenReturn(riskPlanActionTaskType);

		this.mockMvc
				.perform(put("/camunda/api/signal/riskPlanActionType")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(TestUtil.convertObjectToJsonBytes(riskPlanActionTaskType)))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));	
	}
	
	@Test
	public void testDeleteSignalSource() throws Exception {
		
		doNothing().when(riskPlanActionTypeServiceMock).delete(anyLong());
		
		this.mockMvc
				.perform(delete("/camunda/api/signal/riskPlanActionType/{riskPlanActionTypeId}",1)
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk());

		verify(riskPlanActionTypeServiceMock, times(1)).delete(anyLong());
		verifyNoMoreInteractions(riskPlanActionTypeServiceMock);
	}
	
	@Test
	public void testFindSignalSourceById() throws Exception {
		RiskPlanActionTaskType riskPlanActionTaskType=new RiskPlanActionTaskType();

		when(riskPlanActionTypeServiceMock.findById(anyLong())).thenReturn(riskPlanActionTaskType);

		this.mockMvc
				.perform(get("/camunda/api/signal/riskPlanActionType/{riskPlanActionTypeId}", 1)
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"));

		verify(riskPlanActionTypeServiceMock, times(1)).findById(anyLong());
		verifyNoMoreInteractions(riskPlanActionTypeServiceMock);
	}
	
	@Test
	public void testFindAll() throws Exception {
		List<RiskPlanActionTaskType> riskPlanActionTaskTypes = new ArrayList<>();

		when(riskPlanActionTypeServiceMock.findAll()).thenReturn(riskPlanActionTaskTypes);

		this.mockMvc
				.perform(get("/camunda/api/signal/riskPlanActionType", 1)
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"));

		verify(riskPlanActionTypeServiceMock, times(1)).findAll();
		verifyNoMoreInteractions(riskPlanActionTypeServiceMock);

	}
}