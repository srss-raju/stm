package com.deloitte.smt.controllertest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.deloitte.smt.entity.AssessmentActionType;
import com.deloitte.smt.service.AssessmentActionTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = SignalManagementApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AssessmentActionTypeControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	AssessmentActionTypeService assessmentActionTypeServiceMock;
	
	AssessmentActionTypeService assessmentActionTypeServiceMock1;

	@Before
	public void setUp(){
		assessmentActionTypeServiceMock1=mock(AssessmentActionTypeService.class);
	}
	
	@Test
	public void testFindAssessmentActionTypeById() throws Exception {
		AssessmentActionType assessmentActionType = new AssessmentActionType();

		when(assessmentActionTypeServiceMock.findById(anyLong())).thenReturn(assessmentActionType);

		this.mockMvc
				.perform(get("/camunda/api/signal/assessmentActionType/{assessmentActionTypeId}", 1)
						.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"));

		verify(assessmentActionTypeServiceMock, times(1)).findById(anyLong());
		verifyNoMoreInteractions(assessmentActionTypeServiceMock);

	}

	@Test
	public void testDeleteAssessmentActionType() throws Exception {
		
		doNothing().when(assessmentActionTypeServiceMock1).delete(anyLong());
		
		this.mockMvc
				.perform(delete("/camunda/api/signal/assessmentActionType/{assessmentActionTypeId}",1)
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
						//.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk());
				//.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

		verify(assessmentActionTypeServiceMock, times(1)).delete(anyLong());
		verifyNoMoreInteractions(assessmentActionTypeServiceMock);

	}
	
	@Test
	public void testCreateNewAssessmentActionType() throws Exception {
		when(assessmentActionTypeServiceMock.insert(anyList())).thenReturn(anyList());
		
		List<AssessmentActionType> typeList=new ArrayList<AssessmentActionType>();
		AssessmentActionType type=new AssessmentActionType();
		typeList.add(type);
		
		this.mockMvc
				.perform(post("/camunda/api/signal/assessmentActionType")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(TestUtils.convertObjectToJsonBytes(typeList)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

		verify(assessmentActionTypeServiceMock, times(1)).insert(anyList());
		verifyNoMoreInteractions(assessmentActionTypeServiceMock);
	}

	
	@Test
	public void testUpdateAssessmentActionType() throws Exception {
		
		AssessmentActionType updateType=new AssessmentActionType();
		when(assessmentActionTypeServiceMock.update(Matchers.any(AssessmentActionType.class))).thenReturn(updateType);
		
		this.mockMvc
				.perform(put("/camunda/api/signal/assessmentActionType")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(TestUtils.convertObjectToJsonBytes(updateType)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

		verify(assessmentActionTypeServiceMock, times(1)).update(Matchers.any(AssessmentActionType.class));
		verifyNoMoreInteractions(assessmentActionTypeServiceMock);
	}
	
	@Test
		public void testFindAll() throws Exception {
		List<AssessmentActionType> typeList=new ArrayList<AssessmentActionType>();
		AssessmentActionType type=new AssessmentActionType();
		typeList.add(type);
		
			when(assessmentActionTypeServiceMock.findAll()).thenReturn(typeList);
			this.mockMvc
					.perform(get("/camunda/api/signal/assessmentActionType")
					.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

		}
		
	
}