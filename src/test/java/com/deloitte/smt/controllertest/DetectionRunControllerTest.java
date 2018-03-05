package com.deloitte.smt.controllertest;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.entity.DetectionRun;
import com.deloitte.smt.service.DetectionRunService;
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
public class DetectionRunControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	DetectionRunService detectionRunServiceMock;

	@Test
	public void testCreateDetectionRun() throws IOException, Exception {

		DetectionRun detectionRun = new DetectionRun();

		when(detectionRunServiceMock.insert(Matchers.any(DetectionRun.class))).thenReturn(detectionRun);

		this.mockMvc
				.perform(post("/camunda/api/signal/detectionrun").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(TestUtil.convertObjectToJsonBytes(detectionRun)))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	}
	
	@Test
	public void testUpdateDetectionRun() throws Exception{
		DetectionRun detectionRun = new DetectionRun();
		when(detectionRunServiceMock.update(detectionRun)).thenReturn(detectionRun);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put("/camunda/api/signal/detectionrun")
				.accept(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonString(detectionRun))
				.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andReturn();
	}
	

	@Test
	public void testFindAll() throws Exception {
		List<DetectionRun> list = new ArrayList<>();
		DetectionRun drun = new DetectionRun();
		list.add(drun);

		when(detectionRunServiceMock.findAll()).thenReturn(list);
		
		this.mockMvc
		.perform(get("/camunda/api/signal/detectionrun").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
				
	}
	
	
	@Test
	public void testfindByDetectionId() throws Exception{
		DetectionRun dRun=new DetectionRun();
		when(detectionRunServiceMock.findById(anyLong())).thenReturn(dRun);
		
		this.mockMvc
		.perform(get("/camunda/api/signal/detectionrun/detection/{detectionId}",1).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		
	}
	
	@Test
	public void testfindDetectionByRunId() throws Exception{
		DetectionRun dRun=new DetectionRun();
		when(detectionRunServiceMock.findById(anyLong())).thenReturn(dRun);
		
		this.mockMvc
		.perform(get("/camunda/api/signal/detectionrun/{detectionRunId}",1).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		
	}

}
