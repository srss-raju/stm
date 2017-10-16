package com.deloitte.smt.controllertest;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.log4j.Logger;
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
import com.deloitte.smt.service.RiskPlanService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SignalManagementApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = {"classpath:test.properties"})
public class RiskNameUpdateControllerTest {
	
	private static final Logger LOG = Logger.getLogger(RiskNameUpdateControllerTest.class);
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	RiskPlanService riskPlanServiceMock;
	
	@Before
	public void setU(){
		riskPlanServiceMock=mock(RiskPlanService.class);
	}
	
	@Test
	public void testUpdateRiskName(){
		LOG.info("Testing the Update Risk Name");
		try {
			this.mockMvc.perform(get("/camunda/api/signal/{id}/updateRiskName/{riskName}",1,"TestRisk1").
					contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
			.andExpect(status().isOk());
		} catch (Exception e) {
			LOG.error(e);
		}
	}
	

	
}
