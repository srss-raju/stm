package com.deloitte.smt.controllertest;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.RiskTask;
import com.deloitte.smt.service.RiskPlanService;
import com.deloitte.smt.util.SmtResponse;
import com.deloitte.smt.util.TestUtil;

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
	
	private static final Logger LOG = Logger.getLogger(RiskControllerTest.class);

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
	public void testCreateRiskPlan() throws Exception {
		RiskPlan riskPlan=new RiskPlan();
		
		when(riskPlanServiceMock.insert(Matchers.any(RiskPlan.class), Mockito.any(MultipartFile[].class), anyLong())).thenReturn(riskPlan);
		
		this.mockMvc
		.perform(post("/camunda/api/signal/risk")
				.param("data",TestUtil.convertObjectToJsonString(riskPlan))
				.param("assessmentId","1")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	}
	
	@Test
	public void testGetAllRiskPlans() throws IOException, Exception{
		
		List<RiskPlan> riskPlans=new ArrayList<>();
		SearchDto searchDto=new SearchDto();
		SmtResponse smtResponse = new SmtResponse();
		smtResponse.setResult(riskPlans);
		when(riskPlanServiceMock.findAllRiskPlansForSearch(Matchers.any(SearchDto.class))).thenReturn(smtResponse);
		
		this.mockMvc
		.perform(post("/camunda/api/signal/risk/search")
				.content(TestUtil.convertObjectToJsonBytes(searchDto))
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		
		
	}
	
	
	@Test
	public void testCreateRiskAction() throws Exception{
		
		doNothing().when(riskPlanServiceMock1).createRiskTask(Matchers.any(RiskTask.class), Mockito.any(MultipartFile[].class));
		
		RiskTask riskTask=new RiskTask();

		this.mockMvc
		.perform(post("/camunda/api/signal/risk/task/create")
				.param("data",TestUtil.convertObjectToJsonString(riskTask))
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(status().isOk());
		
	}
	
	@Test
	public void testGetAssessmentActionById() throws Exception{
		RiskTask riskTask=new RiskTask();
		when(riskPlanServiceMock.findById(anyLong())).thenReturn(riskTask);
		
		this.mockMvc
		.perform(get("/camunda/api/signal/risk/task/{riskTaskId}",1)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		
	}
	
	@Test
	public void testGetAllByRiskId() throws Exception{
		
		List<RiskTask> list=new ArrayList<>();
		when(riskPlanServiceMock.findAllByRiskId(anyString(), anyString())).thenReturn(list);
		
		this.mockMvc
		.perform(get("/camunda/api/signal/risk/task/{riskId}/allRiskTasks",1)
				.param("status", "Completed")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		
	}
	
	@Test
	public void testDeleteById() throws Exception{
		doNothing().when(riskPlanServiceMock).delete(anyLong(),anyString());
		
		this.mockMvc
		.perform(delete("/camunda/api/signal/risk/task/{riskTaskId}/{taskId}",1,1)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(status().isOk());
	}
	
	@Test
	public void testUpdateRiskTask() throws Exception{
		try{
			doNothing().when(riskPlanServiceMock).updateRiskTask(Matchers.any(RiskTask.class), Mockito.any(MultipartFile[].class));
			RiskTask riskTask=new RiskTask();
			this.mockMvc
					.perform(post("/camunda/api/signal/risk/task/updateRiskTask")
							.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
							.param("data", TestUtil.convertObjectToJsonString(riskTask)))
					.andExpect(status().isOk());
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testUpdateRiskPlan() throws Exception{
		try{
			doNothing().when(riskPlanServiceMock).updateRiskPlan(Matchers.any(RiskPlan.class), Mockito.any(MultipartFile[].class));
			RiskPlan riskPlan=new RiskPlan();
			this.mockMvc
					.perform(post("/camunda/api/signal/risk/updateRiskPlan")
							.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
							.param("data", TestUtil.convertObjectToJsonString(riskPlan)))
					.andExpect(status().isOk());
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testRiskPlanSummary() throws Exception{
		try{
			doNothing().when(riskPlanServiceMock).riskPlanSummary(Matchers.any(RiskPlan.class), Mockito.any(MultipartFile[].class));
			RiskPlan riskPlan=new RiskPlan();
			this.mockMvc
					.perform(post("/camunda/api/signal/risk/riskPlanSummary")
							.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
							.param("data", TestUtil.convertObjectToJsonString(riskPlan)))
					.andExpect(status().isOk());
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testUpdateRiskTaskWithNull() throws Exception{
		try{
			doNothing().when(riskPlanServiceMock).updateRiskTask(Matchers.any(RiskTask.class), Mockito.any(MultipartFile[].class));
			this.mockMvc
					.perform(post("/camunda/api/signal/risk/task/updateRiskTask")
							.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
							.param("data", "test"))
					.andExpect(status().isOk());
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testUpdateRiskPlanWithNull() throws Exception{
		try{
			doNothing().when(riskPlanServiceMock).updateRiskPlan(Matchers.any(RiskPlan.class), Mockito.any(MultipartFile[].class));
			this.mockMvc
					.perform(post("/camunda/api/signal/risk/updateRiskPlan")
							.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
							.param("data", "test"))
					.andExpect(status().isOk());
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testRiskPlanSummaryWithNull() throws Exception{
		try{
			doNothing().when(riskPlanServiceMock).riskPlanSummary(Matchers.any(RiskPlan.class), Mockito.any(MultipartFile[].class));
			this.mockMvc
					.perform(post("/camunda/api/signal/risk/riskPlanSummary")
							.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
							.param("data", "test"))
					.andExpect(status().isOk());
		}catch(Exception ex){
			LOG.info(ex);
		}
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
