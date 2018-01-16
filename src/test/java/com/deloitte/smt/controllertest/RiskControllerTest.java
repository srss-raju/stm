package com.deloitte.smt.controllertest;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.Task;
import com.deloitte.smt.service.RiskPlanService;

/**
 * 
 * @author cavula
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SignalManagementApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = {"classpath:test.properties"})
public class RiskControllerTest {
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	RiskPlanService riskPlanServiceMock;
	
	RiskPlanService riskPlanServiceMock1;

	
	@Before
	public void setU(){
		riskPlanServiceMock1=mock(RiskPlanService.class);
	}
	
	@Test
	public void testGetAssessmentActionById() throws Exception{
		Task riskTask=new Task();
		when(riskPlanServiceMock.findById(anyLong())).thenReturn(riskTask);
		
		this.mockMvc
		.perform(get("/camunda/api/signal/risk/task/{riskTaskId}",1)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		
	}
	
	@Test
	public void testGetAllByRiskId() throws Exception{
		
		List<Task> list=new ArrayList<>();
		when(riskPlanServiceMock.findAllByRiskId(anyLong(), anyString())).thenReturn(list);
		
		this.mockMvc
		.perform(get("/camunda/api/signal/risk/task/{riskId}/allRiskTasks",1)
				.param("status", "Completed")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		
	}
	
	@Test
	public void testDeleteById() throws Exception{
		doNothing().when(riskPlanServiceMock).delete(anyLong());
		
		this.mockMvc
		.perform(delete("/camunda/api/signal/risk/task/{riskTaskId}/{taskId}",1,1)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(status().isOk());
	}
	
	@Test
	public void testFindByRiskId() throws Exception{
		RiskPlan riskPlan=new RiskPlan();
		when(riskPlanServiceMock.findByRiskId(anyLong())).thenReturn(riskPlan);
		this.mockMvc
		.perform(get("/camunda/api/signal/risk/{id}",1)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(status().isOk());
	}
	
	@Test
	public void testGetAssessmentPlanByRiskId() throws Exception{
		RiskPlan  riskPlan =new RiskPlan ();
		when(riskPlanServiceMock.findByRiskId(anyLong())).thenReturn(riskPlan);
		this.mockMvc
		.perform(get("/camunda/api/signal/risk/{riskId}/assessmentPlan",1)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(status().isOk());
	}
	
}
