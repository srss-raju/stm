package com.deloitte.smt.servicetest;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.deloitte.smt.constant.SignalConfigurationType;
import com.deloitte.smt.constant.SmtConstant;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.NonSignal;
import com.deloitte.smt.entity.Pt;
import com.deloitte.smt.entity.RiskPlan;
import com.deloitte.smt.entity.SignalConfidence;
import com.deloitte.smt.entity.SignalStatistics;
import com.deloitte.smt.entity.SignalURL;
import com.deloitte.smt.entity.Soc;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.AssignmentConfigurationRepository;
import com.deloitte.smt.repository.AttachmentRepository;
import com.deloitte.smt.repository.NonSignalRepository;
import com.deloitte.smt.repository.PtRepository;
import com.deloitte.smt.repository.RiskPlanRepository;
import com.deloitte.smt.repository.SignalConfidenceRepository;
import com.deloitte.smt.repository.SignalURLRepository;
import com.deloitte.smt.repository.SocRepository;
import com.deloitte.smt.repository.TaskTemplateRepository;
import com.deloitte.smt.repository.TopicRepository;
import com.deloitte.smt.service.AttachmentService;
import com.deloitte.smt.service.SignalMatchService;
import com.deloitte.smt.service.SignalService;
import com.deloitte.smt.service.TaskService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SignalManagementApplication.class)
@TestPropertySource(locations = { "classpath:test.properties" })
public class SignalServiceTest {

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	SignalService signalService;

	@Autowired
	TaskService taskService;

	@Autowired
	SignalMatchService signalMatchService;

	@MockBean
	TopicRepository topicRepository;

	@MockBean
	SignalURLRepository signalURLRepository;

	@MockBean
	AssessmentPlanRepository assessmentPlanRepository;

	@MockBean
	RiskPlanRepository riskPlanRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@MockBean
	SocRepository socRepository;

	@MockBean
	PtRepository ptRepository;

	@MockBean
	TaskTemplateRepository taskTemplateRepository;

	@MockBean
	AttachmentRepository attachmentRepository;

	@MockBean
	SignalConfidenceRepository signalConfigurationRepository;

	@MockBean
	NonSignalRepository nonSignalRepository;

	@MockBean
	private AssignmentConfigurationRepository assignmentConfigurationRepository;

	@MockBean
	AttachmentService attachmentService;

	
	@Test
	public void testCreateTopic() {
		try {
			Topic topic = new Topic();
			topic.setSignalStatus("New");
			setSoc(topic);
			topic.setSourceName("Test Source");
			List<SignalURL> urls = new ArrayList<>();
			SignalURL url = new SignalURL();
			url.setUrl("Test Url");
			urls.add(url);
			topic.setSignalUrls(urls);
			Set<SignalStatistics> stats = new HashSet<>();
			SignalStatistics signalStatistics = new SignalStatistics();
			signalStatistics.setScore(1);
			stats.add(signalStatistics);
			topic.setSignalStatistics(stats);
			SignalConfidence signalConfiguration = new SignalConfidence();
			signalConfiguration.setCohortPercentage(95);
			signalConfiguration.setConfidenceIndex(45);
			given(this.signalConfigurationRepository.findByConfigName(SignalConfigurationType.DEFAULT_CONFIG.name())).willReturn(signalConfiguration);
			given(this.topicRepository.save(topic)).willReturn(topic);

			signalService.createTopic(topic);
		} catch (Exception ex) {
			logger.info(ex);
		}
	}
	
	@Test
	public void testCreateTopicAssignNull() {
		try {
			Topic topic = new Topic();
			topic.setSignalStatus("New");
			setSoc(topic);
			topic.setSourceName("Test Source");
			List<SignalURL> urls = new ArrayList<>();
			SignalURL url = new SignalURL();
			url.setUrl("Test Url");
			urls.add(url);
			topic.setSignalUrls(urls);
			Set<SignalStatistics> stats = new HashSet<>();
			SignalStatistics signalStatistics = new SignalStatistics();
			signalStatistics.setScore(1);
			stats.add(signalStatistics);
			topic.setSignalStatistics(stats);
			SignalConfidence signalConfiguration = new SignalConfidence();
			signalConfiguration.setCohortPercentage(95);
			signalConfiguration.setConfidenceIndex(45);
			given(this.signalConfigurationRepository.findByConfigName(SignalConfigurationType.DEFAULT_CONFIG.name())).willReturn(signalConfiguration);
			given(this.topicRepository.save(topic)).willReturn(topic);

			signalService.createTopic(topic);
		} catch (Exception ex) {
			logger.info(ex);
		}
	}

	@Test
	public void testUpdateTopic() {
		try {
			Topic topic = new Topic();
			topic.setSignalStatus("New");
			setSoc(topic);
			topic.setSourceName("Test Source");
			List<SignalURL> urls = new ArrayList<>();
			SignalURL url = new SignalURL();
			url.setUrl("Test Url");
			urls.add(url);
			topic.setSignalUrls(urls);
			Set<SignalStatistics> stats = new HashSet<>();
			SignalStatistics signalStatistics = new SignalStatistics();
			signalStatistics.setScore(1);
			stats.add(signalStatistics);
			topic.setSignalStatistics(stats);
			given(this.topicRepository.save(topic)).willReturn(topic);
			signalService.updateTopic(topic);
		} catch (Exception ex) {
			logger.info(ex);
		}
	}

	@Test
	public void testUpdateTopicWithNull() {
		try {
			Topic topic = new Topic();
			signalService.updateTopic(topic);
		} catch (Exception ex) {
			logger.info(ex);
		}
	}
	
	
	
	@Test
	public void testValidateAndPrioritizeNull() {
		try{
			signalService.validateAndPrioritize(1l, null);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testGetCountsByFilter() {
		try{
			List<Long> topicIds = new ArrayList<>();
			topicIds.add(1l);
			List<Topic> signals = new ArrayList<>();
			Topic signal = new Topic();
			AssessmentPlan assessmentPlan = new AssessmentPlan();
			assessmentPlan.setAssessmentPlanStatus("Completed");
			RiskPlan riskPlan = new RiskPlan();
			riskPlan.setStatus("Completed");
			assessmentPlan.setRiskPlan(riskPlan);
			signal.setAssessmentPlan(assessmentPlan);
			signals.add(signal);
			given(this.topicRepository.findAllByIdInAndAssignToAndSignalStatusNotLikeOrderByCreatedDateDesc(topicIds, "test", SmtConstant.COMPLETED.getDescription())).willReturn(signals);
			signalService.getCountsByFilter("test");
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	
	@Test
	public void testDeleteSignalURL() {
		try{
			SignalURL signalURL = new SignalURL();
			given(this.signalURLRepository.findOne(1l)).willReturn(signalURL);
			signalService.deleteSignalURL(1l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testDeleteSignalURLWithNull() {
		try{
			signalService.deleteSignalURL(1l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testFindNonSignalsByRunInstanceId() {
		try{
			signalService.findNonSignalsByRunInstanceId(1l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	

	@Test
	public void testCreateOrupdateNonSignalMatch() {
		try {
			NonSignal nonSignal = new NonSignal();
			nonSignal.setProductKey("Test Product");
			nonSignal.setPtDesc("Test Pt");
			nonSignal.setName("Test Name");
			List<NonSignal> nonSignals = new ArrayList<>();
			nonSignals.add(nonSignal);
			given(this.nonSignalRepository.findAll()).willReturn(nonSignals);
			signalService.createOrupdateNonSignal(nonSignal);
		} catch (Exception ex) {
			logger.info(ex);
		}
	}

	@Test
	public void testCreateOrupdateNonSignalMisMatch() {
		try {
			NonSignal nonSignal = new NonSignal();
			nonSignal.setProductKey("Test Product");
			nonSignal.setPtDesc("Test Pt");
			nonSignal.setName("Test Name");

			NonSignal nonSignal2 = new NonSignal();
			nonSignal2.setProductKey("Test Product2");
			nonSignal2.setPtDesc("Test Pt2");
			nonSignal2.setName("Test Name2");

			List<NonSignal> nonSignals = new ArrayList<>();
			nonSignals.add(nonSignal);
			given(this.nonSignalRepository.findAll()).willReturn(nonSignals);
			signalService.createOrupdateNonSignal(nonSignal2);
		} catch (Exception ex) {
			logger.info(ex);
		}
	}

	@Test
	public void testFindById() {
		try {
			Topic topic = new Topic();
			topic.setId(1l);
			topic.setSignalStatus("New");
			setSoc(topic);

			given(this.topicRepository.findOne(1l)).willReturn(topic);
			given(this.topicRepository.save(topic)).willReturn(topic);
			signalService.findById(1l);
		} catch (Exception ex) {
			logger.info(ex);
		}
	}

	@Test
	public void testFindByIdWithNull() {
		try {
			signalService.findById(1l);
		} catch (Exception ex) {
			logger.info(ex);
		}
	}

	private List<Soc> setSoc(Topic topic) {
		List<Soc> socs = new ArrayList<>();
		Soc soc = new Soc();
		soc.setSocName("Test Soc");
		List<Pt> pts = new ArrayList<>();
		Pt pt = new Pt();
		pt.setPtName("Test Pt");
		pts.add(pt);
		soc.setPts(pts);
		socs.add(soc);
		return socs;
	} 

}
