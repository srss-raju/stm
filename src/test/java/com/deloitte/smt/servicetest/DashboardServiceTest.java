package com.deloitte.smt.servicetest;

import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.deloitte.smt.dto.AssessmentPlanDTO;
import com.deloitte.smt.dto.RiskPlanDTO;
import com.deloitte.smt.dto.SmtComplianceDto;
import com.deloitte.smt.dto.TopicDTO;
import com.deloitte.smt.dto.ValidationOutComesDTO;
import com.deloitte.smt.repository.TopicRepository;
import com.deloitte.smt.service.DashboardService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class DashboardServiceTest {
	
	private static final Logger LOG = Logger.getLogger(DashboardServiceTest.class);
	
	@Autowired
	private DashboardService dashboardService;
	
	@PersistenceContext
	private EntityManager entityManager;
	
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
	public void testGetSmtComplianceDetails() throws Exception {
		LOG.info("testGetSmtComplianceDetails");
		dashboardService.getSmtComplianceDetails();
	}
	
	@Test
	public void testGetDashboardData() throws Exception {
		try{
			List<TopicDTO> list = new ArrayList<>();
			TopicDTO topicDTO = new TopicDTO();
			topicDTO.setIngredientName("Test Ingredient");
			topicDTO.setSignalStatus("Completed");
			list.add(topicDTO);
			given(this.topicRepository.findByIngredientName()).willReturn(list);
			dashboardService.getDashboardData();
		}catch(Exception ex){
			LOG.info("Test getDashboardData");
		}
	}
	
	@Test
	public void testComplianceResponse() throws Exception {
		try{
			Map<String, List<SmtComplianceDto>> smtComplianceMap = new HashMap<>();
			List<SmtComplianceDto> list = new ArrayList<>();
			SmtComplianceDto dto = new SmtComplianceDto();
			list.add(dto);
			List<Object[]> signals = new ArrayList<>();
			Object[] objects = new Object[2];
			objects[0] = "Completed";
			objects[1] = BigInteger.valueOf(10);
			signals.add(objects);
			dashboardService.complianceResponse(smtComplianceMap, signals, "Signal");;
		}catch(Exception ex){
			LOG.info("Test getDashboardData");
		}
	}
	
	@Test
	public void testGenerateDataForValidateOutcomesChart() {
		try{
			dashboardService.generateDataForValidateOutcomesChart();
		}catch(Exception ex){
			LOG.info("ex");
		}
	}
	
	@Test
	public void testContinueToMontior() {
		try{
			List<ValidationOutComesDTO> validateOutComesList = new ArrayList<>();
			dashboardService.continueToMontior(validateOutComesList, BigInteger.valueOf(1l));
		}catch(Exception ex){
			LOG.info("ex");
		}
	}
	
	@Test
	public void testNonSignal() {
		try{
			List<ValidationOutComesDTO> validateOutComesList = new ArrayList<>();
			dashboardService.nonSignal(validateOutComesList, BigInteger.valueOf(1l));
		}catch(Exception ex){
			LOG.info("ex");
		}
	}
	
	@Test
	public void testDetectedSignals() {
		try{
			java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf("2007-09-23 10:10:10.0");
			List<Object[]> signals = new ArrayList<>();
			Object[] objects = new Object[5];
			objects[0] = timestamp;
			objects[1] = BigInteger.valueOf(1l);
			objects[2] = BigInteger.valueOf(1l);
			objects[3] = BigInteger.valueOf(1l);
			objects[4] = BigDecimal.valueOf(1l);
			signals.add(objects);
			dashboardService.detectedSignals(signals);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testValidatedSignalWithRisk() {
		try{
			List<ValidationOutComesDTO> validateOutComesList = new ArrayList<>();
			dashboardService.validatedSignalWithRisk(validateOutComesList, BigInteger.valueOf(1l));
		}catch(Exception ex){
			LOG.info("ex");
		}
	}
	
	@Test
	public void testValiatedSignalWithoutRisk() {
		try{
			List<ValidationOutComesDTO> validateOutComesList = new ArrayList<>();
			dashboardService.valiatedSignalWithoutRisk(validateOutComesList, BigInteger.valueOf(1l));
		}catch(Exception ex){
			LOG.info("ex");
		}
	}
	
	@Test
	public void testGetDetectedSignalDetails() {
		try{
			dashboardService.getDetectedSignalDetails();
		}catch(Exception ex){
			LOG.info("ex");
		}
	}
	
	@Test
	public void testCalculateAssessmentMetrics()  {
		try{
			List<AssessmentPlanDTO> plans = new ArrayList<>();
			AssessmentPlanDTO assessmentPlanDTO = new AssessmentPlanDTO();
			assessmentPlanDTO.setIngredientName("test");
			assessmentPlanDTO.setAssessmentPlanStatus("Completed");
			plans.add(assessmentPlanDTO);
			dashboardService.calculateAssessmentMetrics(plans);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}
	
	@Test
	public void testCalculatRiskMetrics()  {
		try{
			List<RiskPlanDTO> risks = new ArrayList<>();
			RiskPlanDTO riskPlanDTO = new RiskPlanDTO();
			riskPlanDTO.setIngredientName("test");
			riskPlanDTO.setRiskPlanStatus("Completed");
			risks.add(riskPlanDTO);
			dashboardService.calculatRiskMetrics(risks);
		}catch(Exception ex){
			LOG.info(ex);
		}
	}

}
