package com.deloitte.smt.servicetest;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
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
import com.deloitte.smt.constant.DateKeyType;
import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.entity.DenominatorForPoisson;
import com.deloitte.smt.entity.Hlgt;
import com.deloitte.smt.entity.Hlt;
import com.deloitte.smt.entity.IncludeAE;
import com.deloitte.smt.entity.Ingredient;
import com.deloitte.smt.entity.License;
import com.deloitte.smt.entity.Product;
import com.deloitte.smt.entity.Pt;
import com.deloitte.smt.entity.QueryBuilder;
import com.deloitte.smt.entity.SignalDetection;
import com.deloitte.smt.entity.Soc;
import com.deloitte.smt.repository.DenominatorForPoissonRepository;
import com.deloitte.smt.repository.HlgtRepository;
import com.deloitte.smt.repository.HltRepository;
import com.deloitte.smt.repository.IncludeAERepository;
import com.deloitte.smt.repository.IngredientRepository;
import com.deloitte.smt.repository.LicenseRepository;
import com.deloitte.smt.repository.MeetingRepository;
import com.deloitte.smt.repository.ProductRepository;
import com.deloitte.smt.repository.PtRepository;
import com.deloitte.smt.repository.QueryBuilderRepository;
import com.deloitte.smt.repository.SignalDetectionRepository;
import com.deloitte.smt.repository.SocRepository;
import com.deloitte.smt.service.SignalDetectionService;
import com.deloitte.smt.util.SmtResponse;
import com.deloitte.smt.util.TestUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class SignalDetectionServiceTest {
	
	private static final Logger LOG = Logger.getLogger(SignalDetectionServiceTest.class);
	
	@Autowired
	private SignalDetectionService signalDetectionService;
	
	@MockBean
	MeetingRepository meetingRepository;

	@MockBean
	private IngredientRepository ingredientRepository;

	@MockBean
	private ProductRepository productRepository;

	@MockBean
	private LicenseRepository licenseRepository;

	@MockBean
	private SocRepository socRepository;

	@MockBean
	private HlgtRepository hlgtRepository;

	@MockBean
	private HltRepository hltRepository;

	@MockBean
	private PtRepository ptRepository;

	@MockBean
	private SignalDetectionRepository signalDetectionRepository;

	@MockBean
	private DenominatorForPoissonRepository denominatorForPoissonRepository;

	@MockBean
	private IncludeAERepository includeAERepository;
	
	@MockBean
	private QueryBuilderRepository queryBuilderRepository;

	@PersistenceContext
	private EntityManager entityManager;
	
		
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
	public void testCreateOrUpdateSignalDetection() {
		try{
			SignalDetection signalDetection = new SignalDetection();
			Ingredient ingredient = setIngredient(signalDetection);
			given(this.ingredientRepository.save(ingredient)).willReturn(ingredient);
			List<Soc> socs = setSoc(signalDetection);
			setOthers(signalDetection, ingredient);
			given(this.signalDetectionRepository.save(signalDetection)).willReturn(signalDetection);
			given(this.socRepository.save(socs)).willReturn(socs);
			signalDetectionService.createOrUpdateSignalDetection(signalDetection);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}

	@Test
	public void testCreateOrUpdateSignalDetectionWithNull() {
		try{
			SignalDetection signalDetection = new SignalDetection();
			signalDetectionService.createOrUpdateSignalDetection(signalDetection);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testDelete() {
		try{
			SignalDetection signalDetection = new SignalDetection();
			given(this.signalDetectionRepository.findOne(1l)).willReturn(signalDetection);
			signalDetectionService.delete(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testDeleteWithNull() {
		try{
			signalDetectionService.delete(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindById() {
		try{
			SignalDetection signalDetection = new SignalDetection();
			Ingredient ingredient = setIngredient(signalDetection);
			setSoc(signalDetection);
			setOthers(signalDetection, ingredient);
			given(this.signalDetectionRepository.findOne(1l)).willReturn(signalDetection);
			given(this.ingredientRepository.findByDetectionId(1l)).willReturn(ingredient);
			given(this.socRepository.findByDetectionId(1l)).willReturn(signalDetection.getSocs());
			
			given(this.denominatorForPoissonRepository.findByDetectionId(1l)).willReturn(signalDetection.getDenominatorForPoisson());
			given(this.includeAERepository.findByDetectionId(1l)).willReturn(signalDetection.getIncludeAEs());
			given(this.queryBuilderRepository.findByDetectionId(1l)).willReturn(signalDetection.getQueryBuilder());
			
			signalDetectionService.findById(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindByIdWithNull() {
		try{
			signalDetectionService.findById(1l);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindAllForSearchWithCreatedDate() {
		try{
			SearchDto searchDto = TestUtil.buildSearchDto(false, true);
			searchDto.setDateKey(DateKeyType.CREATED.name());
			signalDetectionService.findAllForSearch(searchDto);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindAllForSearchWithLastRunDate() {
		try{
			SearchDto searchDto = TestUtil.buildSearchDto(false, true);
			searchDto.setDateKey(DateKeyType.LASTRUN.name());
			signalDetectionService.findAllForSearch(searchDto);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testFindAllForSearchWithNextRunDate() {
		try{
			SearchDto searchDto = TestUtil.buildSearchDto(false, true);
			searchDto.setDateKey(DateKeyType.NEXTRUN.name());
			signalDetectionService.findAllForSearch(searchDto);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testGanttDetectionsDaily() {
		try{
			List<SignalDetection> detections = new ArrayList<>();
			SignalDetection signalDetection = new SignalDetection();
			signalDetection.setCreatedDate(new Date());
			signalDetection.setWindowType("1");
			signalDetection.setRunFrequency("Daily");
			detections.add(signalDetection);
			SmtResponse smtResponse = new SmtResponse();
			smtResponse.setResult(detections);
			signalDetectionService.ganttDetections(smtResponse);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testGanttDetectionsWeekly() {
		try{
			List<SignalDetection> detections = new ArrayList<>();
			SignalDetection signalDetection = new SignalDetection();
			signalDetection.setCreatedDate(new Date());
			signalDetection.setWindowType("1");
			signalDetection.setRunFrequency("Weekly");
			detections.add(signalDetection);
			SmtResponse smtResponse = new SmtResponse();
			smtResponse.setResult(detections);
			signalDetectionService.ganttDetections(smtResponse);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testGanttDetectionsMonthly() {
		try{
			List<SignalDetection> detections = new ArrayList<>();
			SignalDetection signalDetection = new SignalDetection();
			signalDetection.setCreatedDate(new Date());
			signalDetection.setWindowType("1");
			signalDetection.setRunFrequency("Monthly");
			detections.add(signalDetection);
			SmtResponse smtResponse = new SmtResponse();
			smtResponse.setResult(detections);
			signalDetectionService.ganttDetections(smtResponse);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testGanttDetectionsQuarterly() {
		try{
			SmtResponse smtResponse = new SmtResponse();
			List<SignalDetection> detections = new ArrayList<>();
			SignalDetection signalDetection = new SignalDetection();
			signalDetection.setCreatedDate(new Date());
			signalDetection.setWindowType("1");
			signalDetection.setRunFrequency("Quarterly");
			detections.add(signalDetection);
			smtResponse.setResult(detections);
			signalDetectionService.ganttDetections(smtResponse);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testGanttDetectionsYearly() {
		try{
			List<SignalDetection> detections = new ArrayList<>();
			SignalDetection signalDetection = new SignalDetection();
			signalDetection.setCreatedDate(new Date());
			signalDetection.setWindowType("1");
			signalDetection.setRunFrequency("Yearly");
			detections.add(signalDetection);
			SmtResponse smtResponse = new SmtResponse();
			smtResponse.setResult(detections);
			signalDetectionService.ganttDetections(smtResponse);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	private void setOthers(SignalDetection signalDetection, Ingredient ingredient) {
		List<IncludeAE> includeAEs = new ArrayList<>();
		IncludeAE includeAE = new IncludeAE();
		includeAE.setAeName("Test AE");
		includeAEs.add(includeAE);
		signalDetection.setIncludeAEs(includeAEs);
		
		List<DenominatorForPoisson> denominatorForPoissons = new ArrayList<>();
		DenominatorForPoisson denominatorForPoisson = new DenominatorForPoisson();
		denominatorForPoisson.setName("Test Denom");
		denominatorForPoissons.add(denominatorForPoisson);
		signalDetection.setDenominatorForPoisson(denominatorForPoissons);
		
		List<QueryBuilder> queryBuilders = new ArrayList<>();
		QueryBuilder queryBuilder = new QueryBuilder();
		queryBuilder.setName("Test Query");
		queryBuilders.add(queryBuilder);
		signalDetection.setQueryBuilder(queryBuilders);
		signalDetection.setRunFrequency("Daily");
		signalDetection.setCreatedDate(new Date());
		signalDetection.setIngredient(ingredient);
	}


	private List<Soc> setSoc(SignalDetection signalDetection) {
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
		signalDetection.setSocs(socs);
		return socs;
	}


	private Ingredient setIngredient(SignalDetection signalDetection) {
		signalDetection.setId(1l);
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
