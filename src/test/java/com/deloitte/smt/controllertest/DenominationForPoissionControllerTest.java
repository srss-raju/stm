package com.deloitte.smt.controllertest;

import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.entity.DenominatorForPoission;
import com.deloitte.smt.service.DenominationForPoissionService;
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
public class DenominationForPoissionControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	DenominationForPoissionService denominationForPoissionServiceMock;
	
	DenominationForPoissionService denominationForPoissionServiceMock1;

	@Before
	public void setUp(){
		denominationForPoissionServiceMock1=mock(DenominationForPoissionService.class);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCreateDenominationForPoission() throws Exception {
		when(denominationForPoissionServiceMock.insert(anyList())).thenReturn(anyList());
		
		List<DenominatorForPoission> typeList=new ArrayList<>();
		DenominatorForPoission denominationForPoission=new DenominatorForPoission();
		typeList.add(denominationForPoission);
		
		this.mockMvc
				.perform(post("/camunda/api/signal/denomination")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(TestUtil.convertObjectToJsonBytes(typeList)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

		verify(denominationForPoissionServiceMock, times(1)).insert(anyList());
		verifyNoMoreInteractions(denominationForPoissionServiceMock);
	}
	
	@Test
	public void testUpdateDenominationForPoission() throws Exception {
		DenominatorForPoission denominationForPoission=new DenominatorForPoission();

		when(denominationForPoissionServiceMock.update(Matchers.any(DenominatorForPoission.class))).thenReturn(denominationForPoission);

		this.mockMvc
				.perform(put("/camunda/api/signal/denomination")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(TestUtil.convertObjectToJsonBytes(denominationForPoission)))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));	
	}
	
	@Test
	public void testDeleteDenominationForPoission() throws Exception {
		
		doNothing().when(denominationForPoissionServiceMock).delete(anyLong());
		
		this.mockMvc
				.perform(delete("/camunda/api/signal/denomination/{denominationForPoissionId}",1)
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk());

		verify(denominationForPoissionServiceMock, times(1)).delete(anyLong());
		verifyNoMoreInteractions(denominationForPoissionServiceMock);
	}
	
	@Test
	public void testFindDenominationForPoissionById() throws Exception {
		DenominatorForPoission denominationForPoission=new DenominatorForPoission();

		when(denominationForPoissionServiceMock.findById(anyLong())).thenReturn(denominationForPoission);

		this.mockMvc
				.perform(get("/camunda/api/signal/denomination/{denominationForPoissionId}", 1)
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"));

		verify(denominationForPoissionServiceMock, times(1)).findById(anyLong());
		verifyNoMoreInteractions(denominationForPoissionServiceMock);
	}
	
	@Test
	public void testFindAll() throws Exception {
		List<DenominatorForPoission> denominatorForPoissions = new ArrayList<>();

		when(denominationForPoissionServiceMock.findByDetectionIdIsNull()).thenReturn(denominatorForPoissions);

		this.mockMvc
				.perform(get("/camunda/api/signal/denomination", 1)
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"));

		verify(denominationForPoissionServiceMock, times(1)).findByDetectionIdIsNull();
		verifyNoMoreInteractions(denominationForPoissionServiceMock);

	}
	
	@Test
	public void testFindByDetectionIdIsNullOrderByName() throws Exception {
		List<DenominatorForPoission> denominatorForPoissions = new ArrayList<>();

		when(denominationForPoissionServiceMock.findByDetectionIdIsNull()).thenReturn(denominatorForPoissions);

		this.mockMvc
				.perform(get("/camunda/api/signal/denomination/all", 1)
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"));

		verify(denominationForPoissionServiceMock, times(1)).findByDetectionIdIsNull();
		verifyNoMoreInteractions(denominationForPoissionServiceMock);

	}
}