package com.deloitte.smt.controllertest;

import static org.mockito.Matchers.anyLong;
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
import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.entity.SignalDetection;
import com.deloitte.smt.service.SignalDetectionService;
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
public class SignalDetectionControllerTest {
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	SignalDetectionService signalDetectionServiceMock;
	
	SignalDetectionService signalDetectionServiceMock1;
	
	@Before
	public void setUp() {
		signalDetectionServiceMock1 = mock(SignalDetectionService.class);
	}
	

	@Test
	public void testCreateSignalDetection() throws IOException, Exception {
		SignalDetection signalDection = new SignalDetection();

		when(signalDetectionServiceMock.createOrUpdateSignalDetection(Matchers.any(SignalDetection.class)))
				.thenReturn(signalDection);

		this.mockMvc
				.perform(post("/camunda/api/signal/detect/createSignalDetection")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).param("data",
								TestUtil.convertObjectToJsonString(signalDection)))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	}
	
	@Test
	public void testGetSignalDetectionById() throws IOException, Exception {
		SignalDetection signalDection = new SignalDetection();

		when(signalDetectionServiceMock.findById(anyLong())).thenReturn(signalDection);

		this.mockMvc
				.perform(get("/camunda/api/signal/detect/{signalDetectionId}", 1)
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

	}

	@Test
	public void testUpdateTopic() throws IOException, Exception {
		SignalDetection signalDection = new SignalDetection();

		when(signalDetectionServiceMock.createOrUpdateSignalDetection(Matchers.any(SignalDetection.class)))
				.thenReturn(signalDection);

		this.mockMvc
				.perform(post("/camunda/api/signal/detect/updateSignalDetection")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).param("data",
								TestUtil.convertObjectToJsonString(signalDection)))
				.andExpect(status().isOk());
	}
	
	
	@Test
	public void testDeleteById() throws Exception{

		doNothing().when(signalDetectionServiceMock1).delete(anyLong());

		this.mockMvc
				.perform(delete("/camunda/api/signal/detect/{signalDetectionId}", 1)
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk());
	}
	
	@Test
	public void testGetAllByStatus() throws IOException, Exception{
		
		SearchDto searchDto=new SearchDto();
		searchDto.setGantt(true);
		
		List<SignalDetection> detectionList=new ArrayList<>();
		SignalDetection signalDection = new SignalDetection();
		detectionList.add(signalDection);
		SmtResponse smtResponse = new SmtResponse();
		smtResponse.setResult(detectionList);
		when(signalDetectionServiceMock.ganttDetections(smtResponse)).thenReturn(smtResponse);
		
		this.mockMvc
		.perform(post("/camunda/api/signal/detect/all")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(TestUtil.convertObjectToJsonBytes(searchDto)))
		.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		
	}
	
	@Test
	public void testGetAllByStatusFalse() throws IOException, Exception{
		
		SearchDto searchDto=new SearchDto();
		searchDto.setGantt(false);
		
		List<SignalDetection> detectionList=new ArrayList<>();
		SignalDetection signalDection = new SignalDetection();
		detectionList.add(signalDection);
		SmtResponse smtResponse = new SmtResponse();
		smtResponse.setResult(detectionList);
		when(signalDetectionServiceMock.ganttDetections(smtResponse)).thenReturn(smtResponse);
		
		this.mockMvc
		.perform(post("/camunda/api/signal/detect/all")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(TestUtil.convertObjectToJsonBytes(searchDto)))
		.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		
	}
}
