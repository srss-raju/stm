package com.deloitte.smt.controllertest;

import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.deloitte.smt.dto.DashboardDTO;
import com.deloitte.smt.dto.SignalDetectDTO;
import com.deloitte.smt.dto.SmtComplianceDto;
import com.deloitte.smt.dto.ValidationOutComesDTO;
import com.deloitte.smt.service.DashboardService;

/**
 * 
 * @author cavula
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SignalManagementApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = {"classpath:test.properties"})
public class DashboardControllerTest {
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	DashboardService dashboardServiceMock;
	
	@Test
	public void testGetSmtComplianceDetails() throws Exception{
		Map<String, List<SmtComplianceDto>> map=new HashMap<>();
		when(dashboardServiceMock.getSmtComplianceDetails()).thenReturn(map);
				

		this.mockMvc
				.perform(get("/camunda/api/dashboard/getSmtComplianceDetails")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	}
	
	@Test
	public void testGetSignalsByIngredient() throws Exception{
		DashboardDTO dashboardDto=new DashboardDTO();
		when(dashboardServiceMock.getDashboardData()).thenReturn(dashboardDto);
		
		this.mockMvc
		.perform(get("/camunda/api/dashboard/ingredient")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		
	}
	
	@Test
	public void testGetValidationOutcomes() throws Exception{
		List<ValidationOutComesDTO> list=new ArrayList<>();
		ValidationOutComesDTO dto=new ValidationOutComesDTO();
		list.add(dto);
		
		when(dashboardServiceMock.generateDataForValidateOutcomesChart()).thenReturn(list);
		
		this.mockMvc
		.perform(get("/camunda/api/dashboard/validationoutcomes")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	}
	
	@Test
	public void testGetDetectedSignalDetails() throws Exception{
		List<SignalDetectDTO> list=new ArrayList<>();
		when(dashboardServiceMock.getDetectedSignalDetails()).thenReturn(list);
		
		this.mockMvc
		.perform(get("/camunda/api/dashboard/getDetectedSignalDetails")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		
		
	} 
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testGetSignalStrength() throws Exception{
		Map map=new HashMap();
		when(dashboardServiceMock.getSignalStrength(anyList())).thenReturn(map);
		
		this.mockMvc
		.perform(get("/camunda/api/dashboard/signalStrength")
				.param("topicIds","1")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		
		
	} 
	
}
