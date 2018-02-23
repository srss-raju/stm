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
import com.deloitte.smt.dto.ConditionProductDTO;
import com.deloitte.smt.entity.AssessmentPlan;
import com.deloitte.smt.entity.AssignmentCondition;
import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.entity.AssignmentProduct;
import com.deloitte.smt.entity.Comments;
import com.deloitte.smt.entity.NonSignal;
import com.deloitte.smt.entity.Pt;
import com.deloitte.smt.entity.SignalConfidence;
import com.deloitte.smt.entity.SignalStatistics;
import com.deloitte.smt.entity.SignalStrength;
import com.deloitte.smt.entity.SignalURL;
import com.deloitte.smt.entity.Soc;
import com.deloitte.smt.entity.Task;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.entity.TopicCondition;
import com.deloitte.smt.entity.TopicConditionValues;
import com.deloitte.smt.entity.TopicProduct;
import com.deloitte.smt.entity.TopicProductValues;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.AssignmentConfigurationRepository;
import com.deloitte.smt.repository.CommentsRepository;
import com.deloitte.smt.repository.NonSignalRepository;
import com.deloitte.smt.repository.PtRepository;
import com.deloitte.smt.repository.RiskPlanRepository;
import com.deloitte.smt.repository.SignalConfidenceRepository;
import com.deloitte.smt.repository.SocRepository;
import com.deloitte.smt.repository.TaskRepository;
import com.deloitte.smt.repository.TaskTemplateRepository;
import com.deloitte.smt.repository.TopicRepository;
import com.deloitte.smt.service.AssignmentService;
import com.deloitte.smt.service.SignalAssignmentService;
import com.deloitte.smt.service.SignalMatchService;
import com.deloitte.smt.service.SignalService;
import com.deloitte.smt.service.TaskService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SignalManagementApplication.class)
@TestPropertySource(locations = { "classpath:test.properties" })
public class SignalServiceTest {

	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@MockBean
	AssignmentService assignmentService;
	
	@MockBean
	SignalAssignmentService signalAssignmentService;
	
	@Autowired
	SignalService signalService;
	
	@MockBean
	TaskRepository taskRepository;
	
	@MockBean
	CommentsRepository commentsRepository;

	@Autowired
	TaskService taskService;

	@Autowired
	SignalMatchService signalMatchService;

	@MockBean
	TopicRepository topicRepository;

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
	SignalConfidenceRepository signalConfigurationRepository;

	@MockBean
	NonSignalRepository nonSignalRepository;

	@MockBean
	private AssignmentConfigurationRepository assignmentConfigurationRepository;

	@Test
	public void testCreateTopic() {
		try {
			AssignmentConfiguration assignmentConfiguration = new AssignmentConfiguration();
			ConditionProductDTO conditionProductDTO = new ConditionProductDTO();
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
			assignmentConfiguration.setSignalOwner("A");
			
			List<AssignmentProduct> products = new ArrayList<>();
			AssignmentProduct productConfig = new AssignmentProduct();
			productConfig.setRecordKey("1@#2");
			products.add(productConfig);
			
			List<AssignmentCondition> conditions = new ArrayList<>();
			AssignmentCondition socConfig = new AssignmentCondition();
			conditions.add(socConfig);
			
			assignmentConfiguration.setProducts(products);
			assignmentConfiguration.setConditions(conditions);
			
			List<SignalStrength> signalStrengthList = new ArrayList<>();
			SignalStrength signalStrength = new SignalStrength();
			signalStrengthList.add(signalStrength);
			topic.setSignalStrengthAtrributes(signalStrengthList);
			
			List<TopicCondition> updatedConditionList = new ArrayList<>();
			TopicCondition topicCondition = new TopicCondition();
			List<TopicConditionValues> clist = new ArrayList<>();
			TopicConditionValues record = new TopicConditionValues();
			clist.add(record);
			topicCondition.setRecordValues(clist);
			updatedConditionList.add(topicCondition);
			
			List<TopicProduct> updatedProductList = new ArrayList<>();
			TopicProduct topicProduct= new TopicProduct();
			List<TopicProductValues> plist = new ArrayList<>();
			TopicProductValues precord = new TopicProductValues();
			plist.add(precord);
			topicProduct.setRecordValues(plist);
			updatedProductList.add(topicProduct);
			
			topic.setProducts(updatedProductList);
			topic.setConditions(updatedConditionList);
			
			given(this.signalConfigurationRepository.findByConfigName(SignalConfigurationType.DEFAULT_CONFIG.name())).willReturn(signalConfiguration);
			given(this.assignmentService.getAssignmentConfiguration(assignmentConfiguration,conditionProductDTO)).willReturn(assignmentConfiguration);
			given(this.topicRepository.save(topic)).willReturn(topic);
			given(this.signalAssignmentService.saveSignalAssignmentAssignees(assignmentConfiguration,topic)).willReturn(topic);
			given(this.signalAssignmentService.convertToAssignmentConfiguration(topic)).willReturn(assignmentConfiguration);
			given(this.signalAssignmentService.saveSignalAssignmentAssignees(assignmentConfiguration, topic)).willReturn(topic);
			
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
			topic.setId(2l);
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
			given(this.topicRepository.findOne(1l)).willReturn(topic);
			given(this.topicRepository.save(topic)).willReturn(topic);
			signalService.updateTopic(topic);
		} catch (Exception ex) {
			logger.info(ex);
		}
	}
	
	
	@Test
	public void testUpdateTopicOwnerCheck() {
		try {
			Topic topic = new Topic();
			topic.setId(2l);
			topic.setOwner("A");
			given(this.topicRepository.findOne(2l)).willReturn(topic);
			given(this.topicRepository.save(topic)).willReturn(topic);
			signalService.updateTopic(topic);
		} catch (Exception ex) {
			logger.info(ex);
		}
	}

	
	@Test
	public void testUpdateComments() {
		try {
			List<Comments> list = new ArrayList<>();
			Comments comments = new Comments();
			list.add(comments);
			Topic topic = new Topic();
			topic.setComments(list);
			signalService.updateComments(topic);
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
	public void testValidateAndPrioritize() {
		try{
			ConditionProductDTO conditionProductDTO = new ConditionProductDTO();
			AssignmentConfiguration assignmentConfiguration = new AssignmentConfiguration();
			Topic topic = new Topic();
			AssessmentPlan assessmentPlan = new AssessmentPlan();
			assignmentConfiguration.setAssessmentOwner("A");
			given(this.topicRepository.findOne(1l)).willReturn(topic);
			given(this.signalAssignmentService.convertToAssignmentConfiguration(topic)).willReturn(assignmentConfiguration);
			given(this.signalAssignmentService.getAssignmentConfiguration(assignmentConfiguration, conditionProductDTO)).willReturn(assignmentConfiguration);
			given(this.assessmentPlanRepository.countByAssessmentNameIgnoreCase("A")).willReturn(0l);
			signalService.validateAndPrioritize(1l, assessmentPlan);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testAssociateTemplateTasks() {
		try{
			List<Long> templateIds = new ArrayList<>();
			templateIds.add(1l);
			List<Task> actions = new ArrayList<>();
			Task task = new Task();
			task.setStatus("Completed");
			task.setInDays(5);
			actions.add(task);
			AssessmentPlan assessmentPlan = new AssessmentPlan();
			assessmentPlan.setTemplateIds(templateIds);
			given(this. taskRepository.findAllByTemplateId(1l)).willReturn(actions);
			signalService.associateTemplateTasks(assessmentPlan);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	
	@Test
	public void testUpdateTopicWithNull() {
		try{
			Topic topic = new Topic();
			signalService.updateTopic(topic);
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
