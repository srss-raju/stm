package com.deloitte.smt.servicetest;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.deloitte.smt.entity.DenominatorForPoission;
import com.deloitte.smt.entity.DetectionAssignees;
import com.deloitte.smt.entity.IncludeAE;
import com.deloitte.smt.entity.Pt;
import com.deloitte.smt.entity.QueryBuilder;
import com.deloitte.smt.entity.SignalDetection;
import com.deloitte.smt.entity.Smq;
import com.deloitte.smt.entity.Soc;
import com.deloitte.smt.entity.TopicCondition;
import com.deloitte.smt.entity.TopicConditionValues;
import com.deloitte.smt.entity.TopicProduct;
import com.deloitte.smt.entity.TopicProductValues;
import com.deloitte.smt.repository.DenominatorForPoissionRepository;
import com.deloitte.smt.repository.DetectionAssigneesRepository;
import com.deloitte.smt.repository.IncludeAERepository;
import com.deloitte.smt.repository.MeetingRepository;
import com.deloitte.smt.repository.PtRepository;
import com.deloitte.smt.repository.SignalDetectionRepository;
import com.deloitte.smt.repository.SmqRepository;
import com.deloitte.smt.repository.SocRepository;
import com.deloitte.smt.repository.TopicConditionRepository;
import com.deloitte.smt.repository.TopicConditionValuesRepository;
import com.deloitte.smt.repository.TopicProductRepository;
import com.deloitte.smt.repository.TopicProductValuesRepository;
import com.deloitte.smt.service.SignalDetectionService;
import com.deloitte.smt.util.SmtResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class SignalDetectionServiceTest {
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private SignalDetectionService signalDetectionService;
	
	@MockBean
	MeetingRepository meetingRepository;
	
	@MockBean
	DetectionAssigneesRepository  detectionAssigneesRepository;

	@MockBean
	private SocRepository socRepository;
	
	@MockBean
	SmqRepository smqRepository;

	@MockBean
	private PtRepository ptRepository;

	@MockBean
	private SignalDetectionRepository signalDetectionRepository;

	@MockBean
	private DenominatorForPoissionRepository denominatorForPoissonRepository;

	@MockBean
	private IncludeAERepository includeAERepository;
	
	@MockBean
	TopicConditionRepository topicSocAssignmentConfigurationRepository;
	@MockBean
	TopicProductRepository topicProductAssignmentConfigurationRepository;
	
	@MockBean
	TopicConditionValuesRepository topicAssignmentConditionRepository;
	
	@MockBean
	TopicProductValuesRepository topicAssignmentProductRepository;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Test
	public void testCreateOrUpdateSignalDetectionIdNull() {
		try{
			SignalDetection signalDetection = new SignalDetection();
			signalDetection.setId(1l);
			List<Soc> socs = setSoc(signalDetection);
			setOthers(signalDetection);
			given(this.signalDetectionRepository.countByNameIgnoreCase("A")).willReturn(1l);
			given(this.signalDetectionRepository.save(signalDetection)).willReturn(signalDetection);
			given(this.socRepository.save(socs)).willReturn(socs);
			signalDetectionService.createOrUpdateSignalDetection(signalDetection);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testCreateOrUpdateSignalDetection() {
		try{
			SignalDetection signalDetection = new SignalDetection();
			signalDetection.setId(1l);
			List<Soc> socs = setSoc(signalDetection);
			setOthers(signalDetection);
			
			TopicCondition socConfig = new TopicCondition();
			TopicProduct productConfig = new TopicProduct();
			
			List<TopicConditionValues> topicConditionValues = new ArrayList<>();
			TopicConditionValues topicConditionValue = new TopicConditionValues();
			topicConditionValues.add(topicConditionValue);
			socConfig.setRecordValues(topicConditionValues);


			List<TopicProductValues> topicProductValues = new ArrayList<>();
			TopicProductValues topicProductValue = new TopicProductValues();
			topicProductValues.add(topicProductValue);
			productConfig.setRecordKey("A@#B");
			productConfig.setRecordValues(topicProductValues);

			Smq smq = new Smq();
			List<Pt> pts = new ArrayList<>();
			Pt pt = new Pt();
			pts.add(pt);
			smq.setPts(pts);
			
			given(this.topicSocAssignmentConfigurationRepository.findByDetectionId(1l)).willReturn(signalDetection.getConditions());
			given(this.topicProductAssignmentConfigurationRepository.findByDetectionId(1l)).willReturn(signalDetection.getProducts());
			given(this.topicProductAssignmentConfigurationRepository.save(signalDetection.getProducts())).willReturn(signalDetection.getProducts());
			given(this.topicSocAssignmentConfigurationRepository.save(signalDetection.getConditions())).willReturn(signalDetection.getConditions());
			given(this.topicAssignmentConditionRepository.save(socConfig.getRecordValues())).willReturn(socConfig.getRecordValues());
			given(this.topicAssignmentProductRepository.save(productConfig.getRecordValues())).willReturn(productConfig.getRecordValues());
			given(this.signalDetectionRepository.save(signalDetection)).willReturn(signalDetection);
			given(this.socRepository.save(socs)).willReturn(socs);
			given(this.smqRepository.save(signalDetection.getSmqs())).willReturn(signalDetection.getSmqs());
			given(this.ptRepository.save(smq.getPts())).willReturn(smq.getPts());
			signalDetectionService.createOrUpdateSignalDetection(signalDetection);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testCreateOrUpdateSignalDetectionDuplicate() {
		try{
			SignalDetection signalDetection = new SignalDetection();
			List<Soc> socs = setSoc(signalDetection);
			setOthers(signalDetection);
			given(this.signalDetectionRepository.countByNameIgnoreCase("A")).willReturn(1l);
			given(this.socRepository.save(socs)).willReturn(socs);
			signalDetectionService.createOrUpdateSignalDetection(signalDetection);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	

	@Test
	public void testCreateOrUpdateSignalDetectionWithNull() {
		try{
			SignalDetection signalDetection = new SignalDetection();
			signalDetectionService.createOrUpdateSignalDetection(signalDetection);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testDelete() {
		try{
			SignalDetection signalDetection = new SignalDetection();
			given(this.signalDetectionRepository.findOne(1l)).willReturn(signalDetection);
			signalDetectionService.delete(1l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testDeleteByAssigneeId() {
		try{
			DetectionAssignees assignee = new DetectionAssignees("","");
			given(this.detectionAssigneesRepository.findOne(1l)).willReturn(assignee);
			signalDetectionService.deleteByAssigneeId(1l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testDeleteByAssigneeIdNull() {
		try{
			signalDetectionService.deleteByAssigneeId(1l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	
	@Test
	public void testDeleteWithNull() {
		try{
			signalDetectionService.delete(1l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testFindById() {
		try{
			SignalDetection signalDetection = new SignalDetection();
			setSoc(signalDetection);
			setOthers(signalDetection);
			signalDetection.setId(1l);
			given(this.signalDetectionRepository.findOne(1l)).willReturn(signalDetection);
			given(this.socRepository.findByDetectionId(1l)).willReturn(signalDetection.getSocs());
			given(this.smqRepository.findByDetectionId(1l)).willReturn(signalDetection.getSmqs());
			given(this.smqRepository.save(signalDetection.getSmqs())).willReturn(signalDetection.getSmqs());
			given(this.includeAERepository.findByDetectionId(1l)).willReturn(signalDetection.getIncludeAEs());
			given(this.topicSocAssignmentConfigurationRepository.findByDetectionId(1l)).willReturn(signalDetection.getConditions());
			given(this.topicProductAssignmentConfigurationRepository.findByDetectionId(1l)).willReturn(signalDetection.getProducts());
			given(this.topicAssignmentConditionRepository.findByTopicSocAssignmentConfigurationId(1l)).willReturn(signalDetection.getConditions().get(0).getRecordValues());
			given(this.topicAssignmentProductRepository.findByTopicProductAssignmentConfigurationId(1l)).willReturn(signalDetection.getProducts().get(0).getRecordValues());
			
			signalDetectionService.findById(1l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testFindByIdWithNull() {
		try{
			signalDetectionService.findById(1l);
		}catch(Exception ex){
			logger.info(ex);
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
			logger.info(ex);
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
			logger.info(ex);
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
			logger.info(ex);
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
			logger.info(ex);
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
			logger.info(ex);
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
		signalDetection.setRunFrequency("Daily");
		signalDetection.setCreatedDate(new Date());
		List<TopicCondition> conditions = new ArrayList<>();
		TopicCondition socConfig = new TopicCondition();
		List<TopicConditionValues> topicConditionValues = new ArrayList<>();
		TopicConditionValues topicConditionValue = new TopicConditionValues();
		topicConditionValues.add(topicConditionValue);
		socConfig.setRecordValues(topicConditionValues);
		socConfig.setRecordKey("A@#B");
		conditions.add(socConfig);
		List<TopicProduct> products = new ArrayList<>();
		TopicProduct productConfig = new TopicProduct();
		List<TopicProductValues> topicProductValues = new ArrayList<>();
		TopicProductValues topicProductValue = new TopicProductValues();
		topicProductValues.add(topicProductValue);
		productConfig.setRecordKey("A@#B");
		productConfig.setRecordValues(topicProductValues);
		products.add(productConfig);
		signalDetection.setConditions(conditions);
		signalDetection.setProducts(products);
		
		List<Smq> smqs = new ArrayList<>();
		Smq smq = new Smq();
		List<Pt> pts = new ArrayList<>();
		Pt pt = new Pt();
		pts.add(pt);
		smq.setPts(pts);
		smq.setDetectionId(1l);
		smqs.add(smq);
		signalDetection.setSmqs(smqs);
	}


	private List<Soc> setSoc(SignalDetection signalDetection) {
		List<Soc> socs = new ArrayList<>();
		Soc soc = new Soc();
		soc.setDetectionId(1l);
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
