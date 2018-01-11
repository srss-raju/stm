package com.deloitte.smt.servicetest;

import static org.mockito.BDDMockito.given;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.AssignmentConfigurationRepository;
import com.deloitte.smt.repository.CommentsRepository;
import com.deloitte.smt.repository.MeetingRepository;
import com.deloitte.smt.repository.RiskPlanRepository;
import com.deloitte.smt.repository.SignalURLRepository;
import com.deloitte.smt.service.AttachmentService;
import com.deloitte.smt.service.RiskPlanService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class RiskPlanServiceTest {
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private RiskPlanService riskPlanService;
	
	@MockBean
	MeetingRepository meetingRepository;

	@MockBean
	AttachmentService attachmentService;
	
	@MockBean
	AssessmentPlanRepository assessmentPlanRepository;

	@MockBean
	RiskPlanRepository riskPlanRepository;


	@PersistenceContext
	private EntityManager entityManager;

	@MockBean
	CommentsRepository commentsRepository;

	@MockBean
	SignalURLRepository signalURLRepository;

	@MockBean
	AssignmentConfigurationRepository assignmentConfigurationRepository;
	
	@Test
	public void testDelete() {
		try{
			riskPlanService.delete(1l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	
	
	@Test
	public void testFindByRiskId() {
		try{
			RiskPlan riskPlan = new RiskPlan();
			riskPlan.setId(1l);
			riskPlan.setStatus("New");
			given(this.riskPlanRepository.findOne(1l)).willReturn(riskPlan);
			given(this.riskPlanRepository.save(riskPlan)).willReturn(riskPlan);
			riskPlanService.findByRiskId(1l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testFindByRiskIdForNull() {
		try{
			riskPlanService.findByRiskId(1l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	
	
}
