package com.deloitte.smt.controllertest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.dto.ConditionResponse;
import com.deloitte.smt.dto.MedraBrowserDTO;
import com.deloitte.smt.dto.ProductResponse;
import com.deloitte.smt.dto.ProductSearchDTO;
import com.deloitte.smt.dto.SocSearchDTO;
import com.deloitte.smt.service.ConditonService;
import com.deloitte.smt.service.ProductHierarchyService;
import com.deloitte.smt.service.ProductService;
import com.deloitte.smt.service.SignalService;
import com.deloitte.smt.service.SocHierarchyService;
import com.deloitte.smt.util.TestUtil;

/**
 * 
 * @author RajeshKumar
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SignalManagementApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MedraBrowserControllerTest {

	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	SignalService signalServiceMock;
	
	SignalService signalServiceMock1;
	
	@MockBean
	SocHierarchyService socHierarchyService;
	@MockBean
	ConditonService condtionService;
	@MockBean
	ProductHierarchyService productHierarchyService;
	@MockBean
	ProductService productService;
	
	@Before
	public void setUp() {
		signalServiceMock1 = mock(SignalService.class);
	}
	
	@Test
	public void testGetAllbySocName() throws Exception{
		MedraBrowserDTO medraBrowserDto = new MedraBrowserDTO();
		medraBrowserDto.setSelectLevel("One");
		List<SocSearchDTO> socList = new ArrayList<>();
		when(socHierarchyService.getHierarchyByCode(medraBrowserDto)).thenReturn(socList);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/camunda/api/signal/condition")
				.accept(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonString(medraBrowserDto))
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		logger.info(result);
	}
	
	@Test
	public void testGetAllbySocNameNull() throws Exception{
		MedraBrowserDTO medraBrowserDto = new MedraBrowserDTO();
		List<SocSearchDTO> socList = new ArrayList<>();
		when(socHierarchyService.getHierarchyByCode(medraBrowserDto)).thenReturn(socList);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/camunda/api/signal/condition")
				.accept(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonString(medraBrowserDto))
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		logger.info(result);
	}
	

	@Test
	public void testGetAllConditionsLevel()  {
		try{
			ConditionResponse conditionResponse = new ConditionResponse();
			when(condtionService.getAllConditionLevels()).thenReturn(conditionResponse);
			this.mockMvc
					.perform(get("/camunda/api/signal/condition/product")
							.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testUpdateShowCodes() throws Exception{
		ConditionResponse conditionResponse = new ConditionResponse();
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put("/camunda/api/signal/condition/product")
				.accept(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonString(conditionResponse))
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		logger.info(result);
	}
	
	@Test
	public void testGetAllProductsLevel()  {
		try{
			ProductResponse  productResponse  = new ProductResponse ();
			when(productService.getAllProductsLevel()).thenReturn(productResponse);
			this.mockMvc
					.perform(get("/camunda/api/signal/product/product")
							.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testUpdatePShowCodes() throws Exception{
		ProductResponse  conditionResponse = new ProductResponse ();
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put("/camunda/api/signal/product/product")
				.accept(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonString(conditionResponse))
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		logger.info(result);
	}
	
	@Test
	public void testGetAllbyProductName() throws Exception{
		MedraBrowserDTO medraBrowserDto = new MedraBrowserDTO();
		medraBrowserDto.setSelectLevel("One");
		List<ProductSearchDTO> socList = new ArrayList<>();
		when(productHierarchyService.getHierarchyByCode(medraBrowserDto)).thenReturn(socList);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/camunda/api/signal/product")
				.accept(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonString(medraBrowserDto))
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		logger.info(result);
	}
	
	@Test
	public void testGetAllbyProductNameNull() throws Exception{
		MedraBrowserDTO medraBrowserDto = new MedraBrowserDTO();
		List<ProductSearchDTO> socList = new ArrayList<>();
		when(productHierarchyService.getHierarchyByCode(medraBrowserDto)).thenReturn(socList);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/camunda/api/signal/product")
				.accept(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonString(medraBrowserDto))
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		logger.info(result);
	}
	
}
