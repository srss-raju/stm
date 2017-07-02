package com.deloitte.smt.controllertest;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.service.SignalService;

/**
 * 
 * @author RajeshKumar
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SignalManagementApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SignalControllerTest {

	private static final Logger LOG = Logger.getLogger(SignalControllerTest.class);
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	SignalService signalServiceMock;
	
	SignalService signalServiceMock1;
	
	@Before
	public void setUp() {
		signalServiceMock1 = mock(SignalService.class);
	}
	

	@Test
	public void testGetTopicById()  {
		try{
			Topic topic = new Topic();
			when(signalServiceMock.findById(anyLong())).thenReturn(topic);
			this.mockMvc
					.perform(get("/camunda/api/signal/{signalId}", 1)
							.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindTopicsByRunInstanceId()  {
		try{
			when(signalServiceMock.findTopicsByRunInstanceId(anyLong())).thenReturn(null);
			this.mockMvc
					.perform(get("/camunda/api/signal/run/{runInstanceId}", 1)
							.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		}catch(Exception ex){
			LOG.info(ex);
		}
	}

	@Test
	public void testDeleteSignalURL() throws Exception{
		try{
			doNothing().when(signalServiceMock).deleteSignalURL(anyLong());
			this.mockMvc
					.perform(delete("/camunda/api/signal/url/{signalUrlId}", 1)
							.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(status().isOk());
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
}
