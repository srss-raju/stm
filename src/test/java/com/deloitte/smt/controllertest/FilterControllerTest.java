package com.deloitte.smt.controllertest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.dto.FilterDTO;
import com.deloitte.smt.dto.SearchCriteriaDTO;
import com.deloitte.smt.service.FiltersService;
import com.deloitte.smt.util.ServerResponseObject;
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
public class FilterControllerTest {

	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	FiltersService  filtersService;
	
	
	@Test
	public void testGetSignalDataByFilter() throws Exception{
		SearchCriteriaDTO searchCriteria = new SearchCriteriaDTO();
		ServerResponseObject object = new ServerResponseObject ();
		when(filtersService.getSignalDataByFilter("Signal",searchCriteria)).thenReturn(object);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/camunda/api/signal/filter/{type}/search","Signal")
				.accept(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonString(object))
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		logger.info(result);
	}
	
	
	
	@Test
	public void testGetFilters()  {
		try{
			List<FilterDTO> list = new ArrayList<>();
			
			when(filtersService.getFiltersByType("Signal")).thenReturn(list);
			this.mockMvc
					.perform(get("/camunda/api/signal/filter/{type}","Signal")
							.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	
}
