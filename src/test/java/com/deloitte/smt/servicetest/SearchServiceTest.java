package com.deloitte.smt.servicetest;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.deloitte.smt.dto.SearchDto;
import com.deloitte.smt.entity.Ingredient;
import com.deloitte.smt.entity.License;
import com.deloitte.smt.entity.Product;
import com.deloitte.smt.repository.AssessmentPlanRepository;
import com.deloitte.smt.repository.HlgtRepository;
import com.deloitte.smt.repository.HltRepository;
import com.deloitte.smt.repository.IngredientRepository;
import com.deloitte.smt.repository.LicenseRepository;
import com.deloitte.smt.repository.MeetingRepository;
import com.deloitte.smt.repository.ProductRepository;
import com.deloitte.smt.repository.PtRepository;
import com.deloitte.smt.repository.RiskPlanRepository;
import com.deloitte.smt.repository.SocRepository;
import com.deloitte.smt.repository.TopicRepository;
import com.deloitte.smt.service.SearchService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class SearchServiceTest {
	
	@Autowired
	private SearchService searchService;
	@MockBean
	MeetingRepository meetingRepository;
	@MockBean
    IngredientRepository ingredientRepository;
	@MockBean
    ProductRepository productRepository;
	@MockBean
    LicenseRepository licenseRepository;
	@MockBean
    SocRepository socRepository;
	@MockBean
    HlgtRepository hlgtRepository;
	@MockBean
    HltRepository hltRepository;
	@MockBean
    PtRepository ptRepository;
	@MockBean
    AssessmentPlanRepository assessmentPlanRepository;
	@MockBean
    RiskPlanRepository riskPlanRepository;
	@MockBean
    TopicRepository topicRepository;
	
		
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
	public void testGetFiltersForSignal() {
		searchService.getFiltersForSignal();
	}
	
	@Test
	public void testGetFiltersForSignalDetection() {
		searchService.getFiltersForSignalDetection();
	}
	
	@Test
	public void testGetAllFiltersForAssessmentPlan() {
		Set<Long> topicIds = new HashSet<>();
		topicIds.add(1l);
		given(this.assessmentPlanRepository.findAllSignalIds()).willReturn(topicIds);
		searchService.getAllFiltersForAssessmentPlan();
	}
	
	@Test
	public void testGetAllFiltersForRiskPlan() {
		Set<Long> topicIds = new HashSet<>();
		topicIds.add(1l);
		given(this.riskPlanRepository.findAllSignalIds()).willReturn(topicIds);
		searchService.getAllFiltersForRiskPlan();
	}
	
	@Test
	public void testGetIngredients() {
		searchService.getIngredients();
	}
	
	@Test
	public void testGetSignalIdsForSearchWithLicense() {
		SearchDto searchDto = new SearchDto();
		List<String> licenses = new ArrayList<>();
		licenses.add("Test License");
		searchDto.setLicenses(licenses);
		List<Long> topicIds = new ArrayList<>();
		topicIds.add(1l);
		List<License> licenseList = new ArrayList<>();
		License license = new License();
		license.setTopicId(1l);
		licenseList.add(license);
		given(this.licenseRepository.findAllByLicenseNameIn(licenses)).willReturn(licenseList);
		searchService.getSignalIdsForSearch(searchDto, topicIds, false);
	}
	
	@Test
	public void testGetSignalIdsForSearchWithIngredient() {
		SearchDto searchDto = new SearchDto();
		List<String> ingredients = new ArrayList<>();
		ingredients.add("Test Ingredient");
		searchDto.setIngredients(ingredients);
		List<Long> topicIds = new ArrayList<>();
		topicIds.add(1l);
		List<Ingredient> ingredientList = new ArrayList<>();
		Ingredient ingredient = new Ingredient();
		ingredient.setTopicId(1l);
		ingredientList.add(ingredient);
		given(this.ingredientRepository.findAllByIngredientNameIn(ingredients)).willReturn(ingredientList);
		searchService.getSignalIdsForSearch(searchDto, topicIds, false);
	}
	
	@Test
	public void testGetSignalIdsForSearchWithProduct() {
		SearchDto searchDto = new SearchDto();
		List<String> products = new ArrayList<>();
		products.add("Test Product");
		searchDto.setProducts(products);
		List<Long> topicIds = new ArrayList<>();
		topicIds.add(1l);
		List<Product> productList = new ArrayList<>();
		Product product = new Product();
		product.setTopicId(1l);
		productList.add(product);
		given(this.productRepository.findAllByProductNameIn(products)).willReturn(productList);
		searchService.getSignalIdsForSearch(searchDto, topicIds, false);
	}
}
