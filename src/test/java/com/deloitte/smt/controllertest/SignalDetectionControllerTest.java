package com.deloitte.smt.controllertest;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.dto.DetectionTopicDTO;
import com.deloitte.smt.dto.PtDTO;
import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.dto.SmqDTO;
import com.deloitte.smt.entity.SignalDetection;
import com.deloitte.smt.service.SignalAdditionalService;
import com.deloitte.smt.service.SignalDetectionService;
import com.deloitte.smt.service.SmqService;
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
public class SignalDetectionControllerTest {
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	SignalDetectionService signalDetectionServiceMock;
	
	@MockBean
	SmqService smqService;
	
	@MockBean
	SignalAdditionalService signalAdditionalService;
	
	
	@Test
	public void testCreateDetection() throws Exception{
		SignalDetection signalDection = new SignalDetection();
		when(signalDetectionServiceMock.createOrUpdateSignalDetection(signalDection)).thenReturn(signalDection);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/camunda/api/signal/detect/createSignalDetection")
				.accept(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonString(signalDection))
				.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andReturn();
	}
	
	@Test
	public void testUpdateDetection() throws Exception{
		SignalDetection signalDection = new SignalDetection();
		when(signalDetectionServiceMock.createOrUpdateSignalDetection(signalDection)).thenReturn(signalDection);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.put("/camunda/api/signal/detect/createSignalDetection")
				.accept(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonString(signalDection))
				.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andReturn();
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
	public void testDeleteById() throws Exception{

		doNothing().when(signalDetectionServiceMock).delete(anyLong());

		this.mockMvc
				.perform(delete("/camunda/api/signal/detect/{signalDetectionId}", 1)
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk());
	}
	
	@Test
	public void testDeleteByAssigneeId() throws Exception{

		doNothing().when(signalDetectionServiceMock).deleteByAssigneeId(anyLong());

		this.mockMvc
				.perform(delete("/camunda/api/signal/detect/detectionassignee/{assigneeId}", 1)
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk());
	}
	
	@Test
	public void testFindAllSmqs() throws IOException, Exception {
		List<SmqDTO> list = new ArrayList<>();

		when(smqService.findAllSmqs()).thenReturn(list);

		this.mockMvc
				.perform(get("/camunda/api/signal/detect/allsmqs")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

	}
	
	@Test
	public void testFindPtsBySmqId() throws IOException, Exception {
		SearchDto searchDto = new SearchDto();
		List<PtDTO> list = new ArrayList<>();
		when(smqService.findPtsBySmqId(searchDto.getSmqIds())).thenReturn(list);

		this.mockMvc
				.perform(post("/camunda/api/signal/detect/allptsbysmqids")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(TestUtil.convertObjectToJsonString(searchDto)))
				.andExpect(status().isOk());
	}
	
	@Test
	public void testCreateTopic() throws Exception{
		List<DetectionTopicDTO> detectionTopicDTOs = new ArrayList<>();
		doNothing().when(signalAdditionalService).saveSignalsAndNonSignals(detectionTopicDTOs);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/camunda/api/signal/detect/createTopic")
				.accept(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonString(detectionTopicDTOs))
				.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andReturn();
	}
	
}
