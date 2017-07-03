package com.deloitte.smt.controllertest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
import com.deloitte.smt.entity.NonSignal;
import com.deloitte.smt.service.SignalService;
import com.deloitte.smt.util.TestUtil;
import org.junit.Test;


/**
 * 
 * @author cavula
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SignalManagementApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class NonSignalControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	SignalService signalServiceMock;
	
	@Test
	public void testCreateNonSignal() throws Exception{
		
		NonSignal nonSignal=new NonSignal();
		
		when(signalServiceMock.createOrupdateNonSignal(Matchers.any(NonSignal.class))).thenReturn(nonSignal);

		this.mockMvc
				.perform(post("/camunda/api/signal/nonsignal")
						.param("data",TestUtil.convertObjectToJsonString(nonSignal))
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk());

	}
	
	@Test
	public void testUpdateNonSignal() throws Exception{
		
		NonSignal nonSignal=new NonSignal();
		
		when(signalServiceMock.createOrupdateNonSignal(Matchers.any(NonSignal.class))).thenReturn(nonSignal);

		this.mockMvc
				.perform(post("/camunda/api/signal/nonsignal/update")
						.param("data",TestUtil.convertObjectToJsonString(nonSignal))
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk());

	}
	
	
}
