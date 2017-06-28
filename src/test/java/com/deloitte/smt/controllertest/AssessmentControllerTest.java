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
import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.service.AssessmentPlanService;
import com.deloitte.smt.util.TestUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * 
 * @author cavula
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SignalManagementApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AssessmentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	AssessmentPlanService assessmentPlanServiceMock;

	AssessmentPlanService assessmentPlanServiceMock1;

	@Before
	public void setUp() {
		assessmentPlanServiceMock1 = mock(AssessmentPlanService.class);
	}

	@Test
	public void testGetAllAssessmentPlans() throws Exception {
		List<AssessmentPlan> planList = new ArrayList<AssessmentPlan>();
		AssessmentPlan type = new AssessmentPlan();
		planList.add(type);

		SearchDto searchDTO = new SearchDto();

		when(assessmentPlanServiceMock.findAllAssessmentPlans(Matchers.any(SearchDto.class))).thenReturn(planList);
		this.mockMvc
				.perform(post("/camunda/api/signal/allAssessmentPlans")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(TestUtil.convertObjectToJsonBytes(searchDTO)))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

	}

	@Test
	public void testGetAssessmentPlanById() throws Exception {
		AssessmentPlan assessmentPlan = new AssessmentPlan();
		when(assessmentPlanServiceMock.findById(anyLong())).thenReturn(assessmentPlan);

		this.mockMvc
				.perform(get("/camunda/api/signal/assessmentPlan/{id}", 1)
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

	}

	@Test
	public void testGetAllSignalsByAssessmentId() throws Exception {
		Set<Topic> topicSet = new HashSet<Topic>();
		Topic topic = new Topic();
		topicSet.add(topic);

		AssessmentPlan assessmentPlan = new AssessmentPlan();
		assessmentPlan.setTopics(topicSet);

		when(assessmentPlanServiceMock.findById(anyLong())).thenReturn(assessmentPlan);
		this.mockMvc
				.perform(get("/camunda/api/signal/{assessmentId}/allTopics", 1)
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

		verify(assessmentPlanServiceMock, times(1)).findById(anyLong());
		verifyNoMoreInteractions(assessmentPlanServiceMock);

	}

	@Test
	public void testUnlinkSignal() throws Exception{
		doNothing().when(assessmentPlanServiceMock1).unlinkSignalToAssessment(anyLong(), anyLong());

		this.mockMvc
				.perform(put("/camunda/api/signal/unlink/{assessmentId}/{topicId}", 1,1)
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk());

	}

	@Test
	public void testUpdateAssessment() throws Exception{

		doNothing().when(assessmentPlanServiceMock1).updateAssessment(Matchers.any(AssessmentPlan.class),anyObject());
		
		AssessmentPlan assessmentPlan=new AssessmentPlan();
		
		this.mockMvc
				.perform(post("/camunda/api/signal/updateAssessment")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.param("data", TestUtil.convertObjectToJsonString(assessmentPlan)))
						
				.andExpect(status().isOk());

	}
	
	@Test
		public void testFinalAssessment() throws Exception{
			doNothing().when(assessmentPlanServiceMock1).finalAssessment(Matchers.any(AssessmentPlan.class),anyObject());

			AssessmentPlan assessmentPlan=new AssessmentPlan();
			
			this.mockMvc
					.perform(post("/camunda/api/signal/finalAssessment")
							.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
					.param("data", TestUtil.convertObjectToJsonString(assessmentPlan)))
					.andExpect(status().isOk());

		}
	
}