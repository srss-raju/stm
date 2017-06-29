package com.deloitte.smt.controllertest;

import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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
import com.deloitte.smt.entity.SignalStatistics;
import com.deloitte.smt.service.SignalStatisticsService;
import com.deloitte.smt.util.TestUtil;

/**
 * 
 * @author RajeshKumar
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SignalManagementApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SignalStatisticsControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	SignalStatisticsService signalStatisticsServiceMock;
	
	SignalStatisticsService signalStatisticsServiceMock1;

	@Before
	public void setUp(){
		signalStatisticsServiceMock1=mock(SignalStatisticsService.class);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCreateSignalStatistics() throws Exception {
		when(signalStatisticsServiceMock.insert(anyList())).thenReturn(anyList());
		
		List<SignalStatistics> typeList=new ArrayList<SignalStatistics>();
		SignalStatistics type=new SignalStatistics();
		typeList.add(type);
		
		this.mockMvc
				.perform(post("/camunda/api/signal/statistics")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(TestUtil.convertObjectToJsonBytes(typeList)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

		verify(signalStatisticsServiceMock, times(1)).insert(anyList());
		verifyNoMoreInteractions(signalStatisticsServiceMock);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testFindSignalStatisticsByRunInstanceId() throws Exception {

		when(signalStatisticsServiceMock.findSignalStatisticsByRunInstanceId(anyLong())).thenReturn(anyList());

		this.mockMvc
				.perform(get("/camunda/api/signal/statistics/{runInstanceId}", 1)
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"));

		verify(signalStatisticsServiceMock, times(1)).findSignalStatisticsByRunInstanceId(anyLong());
		verifyNoMoreInteractions(signalStatisticsServiceMock);

	}
}