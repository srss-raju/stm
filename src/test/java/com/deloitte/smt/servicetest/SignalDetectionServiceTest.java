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
import com.deloitte.smt.entity.DenominatorForPoission;
import com.deloitte.smt.entity.IncludeAE;
import com.deloitte.smt.entity.Pt;
import com.deloitte.smt.entity.QueryBuilder;
import com.deloitte.smt.entity.SignalDetection;
import com.deloitte.smt.entity.Soc;
import com.deloitte.smt.repository.DenominatorForPoissionRepository;
import com.deloitte.smt.repository.IncludeAERepository;
import com.deloitte.smt.repository.MeetingRepository;
import com.deloitte.smt.repository.PtRepository;
import com.deloitte.smt.repository.QueryBuilderRepository;
import com.deloitte.smt.repository.SignalDetectionRepository;
import com.deloitte.smt.repository.SocRepository;
import com.deloitte.smt.service.SignalDetectionService;
import com.deloitte.smt.util.SmtResponse;

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
	private SocRepository socRepository;

	@MockBean
	private PtRepository ptRepository;

	@MockBean
	private SignalDetectionRepository signalDetectionRepository;

	@MockBean
	private DenominatorForPoissionRepository denominatorForPoissonRepository;

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
			List<Soc> socs = setSoc(signalDetection);
			setOthers(signalDetection);
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
			setSoc(signalDetection);
			setOthers(signalDetection);
			given(this.signalDetectionRepository.findOne(1l)).willReturn(signalDetection);
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
	private void setOthers(SignalDetection signalDetection) {
		List<IncludeAE> includeAEs = new ArrayList<>();
		IncludeAE includeAE = new IncludeAE();
		includeAE.setAeName("Test AE");
		includeAEs.add(includeAE);
		signalDetection.setIncludeAEs(includeAEs);
		
		List<DenominatorForPoission> denominatorForPoissions = new ArrayList<>();
		DenominatorForPoission denominatorForPoisson = new DenominatorForPoission();
		denominatorForPoisson.setName("Test Denom");
		denominatorForPoissions.add(denominatorForPoisson);
		signalDetection.setDenominatorForPoission(denominatorForPoissions);
		
		List<QueryBuilder> queryBuilders = new ArrayList<>();
		QueryBuilder queryBuilder = new QueryBuilder();
		queryBuilder.setName("Test Query");
		queryBuilders.add(queryBuilder);
		signalDetection.setQueryBuilder(queryBuilders);
		signalDetection.setRunFrequency("Daily");
		signalDetection.setCreatedDate(new Date());
	}


	private List<Soc> setSoc(SignalDetection signalDetection) {
		List<Soc> socs = new ArrayList<>();
		Soc soc = new Soc();
		soc.setSocName("Test Soc");
		List<Pt> pts = new ArrayList<>();
		Pt pt = new Pt();
		pt.setPtName("Test Pt");
		pts.add(pt);
		soc.setPts(pts);
		socs.add(soc);
		signalDetection.setSocs(socs);
		return socs;
	}

}
