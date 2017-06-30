package com.deloitte.smt.servicetest;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.camunda.bpm.engine.CaseService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.mock.MockExpressionManager;
import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.constant.SignalConfigurationType;
import com.deloitte.smt.entity.AssignmentConfiguration;
import com.deloitte.smt.entity.Hlgt;
import com.deloitte.smt.entity.Hlt;
import com.deloitte.smt.entity.Ingredient;
import com.deloitte.smt.entity.License;
import com.deloitte.smt.entity.NonSignal;
import com.deloitte.smt.entity.Product;
import com.deloitte.smt.entity.Pt;
import com.deloitte.smt.entity.SignalConfiguration;
import com.deloitte.smt.entity.SignalStatistics;
import com.deloitte.smt.entity.SignalURL;
import com.deloitte.smt.entity.Soc;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.repository.AssessmentActionRepository;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.AssignmentConfigurationRepository;
import com.deloitte.smt.repository.AttachmentRepository;
import com.deloitte.smt.repository.HlgtRepository;
import com.deloitte.smt.repository.HltRepository;
import com.deloitte.smt.repository.IngredientRepository;
import com.deloitte.smt.repository.LicenseRepository;
import com.deloitte.smt.repository.NonSignalRepository;
import com.deloitte.smt.repository.ProductRepository;
import com.deloitte.smt.repository.PtRepository;
import com.deloitte.smt.repository.RiskPlanRepository;
import com.deloitte.smt.repository.SignalConfigurationRepository;
import com.deloitte.smt.repository.SignalURLRepository;
import com.deloitte.smt.repository.SocRepository;
import com.deloitte.smt.repository.TaskInstRepository;
import com.deloitte.smt.repository.TaskTemplateIngrediantRepository;
import com.deloitte.smt.repository.TaskTemplateRepository;
import com.deloitte.smt.repository.TopicRepository;
import com.deloitte.smt.service.AttachmentService;
import com.deloitte.smt.service.SearchService;
import com.deloitte.smt.service.SignalMatchService;
import com.deloitte.smt.service.SignalService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class SignalServiceTest {
	
	private static final Logger LOG = Logger.getLogger(SignalServiceTest.class);
	
	@Autowired
	SignalService signalService;
	
	@Autowired
	TaskService taskService;

	@Autowired
	SignalMatchService signalMatchService;

	@Autowired
	RuntimeService runtimeService;
	@MockBean
	TopicRepository topicRepository;
	@MockBean
	IngredientRepository ingredientRepository;
	@MockBean
	ProductRepository productRepository;

	@MockBean
	private LicenseRepository licenseRepository;
	@MockBean
	TaskInstRepository taskInstRepository;
	@MockBean
	SignalURLRepository signalURLRepository;

	@Autowired
	CaseService caseService;

	@MockBean
	AssessmentPlanRepository assessmentPlanRepository;

	@MockBean
	RiskPlanRepository riskPlanRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@MockBean
	SocRepository socRepository;

	@MockBean
	HlgtRepository hlgtRepository;

	@MockBean
	HltRepository hltRepository;

	@MockBean
	PtRepository ptRepository;

	@MockBean
	AssessmentActionRepository assessmentActionRepository;

	@MockBean
	TaskTemplateRepository taskTemplateRepository;

	@MockBean
	TaskTemplateIngrediantRepository taskTemplateIngrediantRepository;

	@Autowired
	SearchService searchService;

	@MockBean
	AttachmentRepository attachmentRepository;

	@MockBean
	SignalConfigurationRepository signalConfigurationRepository;

	@MockBean
	NonSignalRepository nonSignalRepository;

	@MockBean
	private AssignmentConfigurationRepository assignmentConfigurationRepository;

	@MockBean
	AttachmentService attachmentService;
	
		
	private static final ProcessEngineConfiguration processEngineConfiguration = new StandaloneInMemProcessEngineConfiguration() {
	    {
	      jobExecutorActivate = false;
	      expressionManager = new MockExpressionManager();
	      databaseSchemaUpdate = DB_SCHEMA_UPDATE_CREATE_DROP;
	    }
	  };
	  
	  private static final ProcessEngine PROCESS_ENGINE_NEEDS_CLOSE = processEngineConfiguration.buildProcessEngine();
	  
	  @Rule
	  public final ProcessEngineRule processEngine = new ProcessEngineRule(PROCESS_ENGINE_NEEDS_CLOSE);

	  @AfterClass
	  public static void shutdown() {
	    PROCESS_ENGINE_NEEDS_CLOSE.close();
	  }
	  
	  @Test
		public void testCreateTopic() {
			try{
				Topic topic = new Topic();
				topic.setSignalStatus("New");
				topic.setIngredient(setIngredient(topic));
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
				SignalConfiguration signalConfiguration = new SignalConfiguration();
				signalConfiguration.setCohortPercentage(95);
				signalConfiguration.setConfidenceIndex(45);
				given(this.signalConfigurationRepository.findByConfigName(SignalConfigurationType.DEFAULT_CONFIG.name())).willReturn(signalConfiguration);
				given(this.topicRepository.save(topic)).willReturn(topic);
				AssignmentConfiguration assignmentConfiguration = new AssignmentConfiguration();
				given(this.assignmentConfigurationRepository.findByIngredientAndSignalSource(topic.getIngredient().getIngredientName(), topic.getSourceName())).willReturn(assignmentConfiguration);
				given(this.ingredientRepository.save(topic.getIngredient())).willReturn(topic.getIngredient());
				given(this.socRepository.save(topic.getSocs())).willReturn(topic.getSocs());
				
				signalService.createTopic(topic, null);
			}catch(Exception ex){
				LOG.info(ex);
			}
		}
	  
    
	@Test
	public void testCreateOrupdateNonSignalMatch() {
		try{
			NonSignal nonSignal = new NonSignal();
			nonSignal.setProductKey("Test Product");
			nonSignal.setPtDesc("Test Pt");
			nonSignal.setName("Test Name");
			List<NonSignal> nonSignals = new ArrayList<>();
			nonSignals.add(nonSignal);
			given(this.nonSignalRepository.findAll()).willReturn(nonSignals);
			signalService.createOrupdateNonSignal(nonSignal);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testCreateOrupdateNonSignalMisMatch() {
		try{
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
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindById() {
		try{
			Topic topic = new Topic();
			topic.setSignalStatus("New");
			Ingredient ingredient = setIngredient(topic);
			setSoc(topic);
			
			given(this.topicRepository.findOne(1l)).willReturn(topic);
			given(this.ingredientRepository.findByTopicId(1l)).willReturn(ingredient);
			given(this.socRepository.findByTopicId(1l)).willReturn(topic.getSocs());
			signalService.findById(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindByIdWithNull() {
		try{
			signalService.findById(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	private List<Soc> setSoc(Topic topic) {
		List<Soc> socs = new ArrayList<>();
		Soc soc = new Soc();
		soc.setSocName("Test Soc");
		List<Hlgt> hlgts = new ArrayList<>();
		Hlgt hlgt = new Hlgt();
		hlgt.setHlgtName("Test Hlgt");
		hlgts.add(hlgt);
		List<Hlt> hlts = new ArrayList<>();
		Hlt hlt = new Hlt();
		hlt.setHltName("Test Hlt");
		hlts.add(hlt);
		List<Pt> pts = new ArrayList<>();
		Pt pt = new Pt();
		pt.setPtName("Test Pt");
		pts.add(pt);
		soc.setHlgts(hlgts);
		soc.setHlts(hlts);
		soc.setPts(pts);
		socs.add(soc);
		topic.setSocs(socs);
		return socs;
	}


	private Ingredient setIngredient(Topic topic) {
		topic.setId(1l);
		Ingredient ingredient = new Ingredient();
		ingredient.setIngredientName("Test Ingredient");
		
		List<Product> products = new ArrayList<>();
		Product product = new Product();
		product.setProductName("Test Product");
		products.add(product);
		ingredient.setProducts(products);
		
		List<License> licenses = new ArrayList<>();
		License license = new License();
		license.setLicenseName("Test License");
		licenses.add(license);
		ingredient.setLicenses(licenses);
		return ingredient;
	}
}
