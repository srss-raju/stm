package com.deloitte.smt.servicetest;

import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.deloitte.smt.dto.SmtComplianceDto;
import com.deloitte.smt.dto.ValidationOutComesDTO;
import com.deloitte.smt.entity.SignalStatistics;
import com.deloitte.smt.entity.SignalURL;
import com.deloitte.smt.entity.Topic;
import com.deloitte.smt.exception.ApplicationException;
import com.deloitte.smt.repository.SignalStatisticsRepository;
import com.deloitte.smt.repository.TopicRepository;
import com.deloitte.smt.service.DashboardService;
import com.deloitte.smt.service.SignalMatchService;
import com.deloitte.smt.service.SignalService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class DashboardServiceTest {
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private DashboardService dashboardService;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@MockBean
	TopicRepository topicRepository;
	
	@MockBean
	SignalService signalService;
	
	@MockBean
	private SignalMatchService signalMatchService;
	
	
	@MockBean
	private SignalStatisticsRepository signalStatisticsRepository;
	
		
	@Test
	public void testGetSmtComplianceDetails() throws Exception {
		logger.info("testGetSmtComplianceDetails");
		dashboardService.getSmtComplianceDetails();
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
			logger.info("Test getDashboardData");
		}
	}
	
	@Test
	public void testGenerateDataForValidateOutcomesChart() {
		try{
			dashboardService.generateDataForValidateOutcomesChart();
		}catch(Exception ex){
			logger.info("ex");
		}
	}
	
	@Test
	public void testContinueToMontior() {
		try{
			List<ValidationOutComesDTO> validateOutComesList = new ArrayList<>();
			dashboardService.continueToMontior(validateOutComesList, BigInteger.valueOf(1l));
		}catch(Exception ex){
			logger.info("ex");
		}
	}
	
	@Test
	public void testNonSignal() {
		try{
			List<ValidationOutComesDTO> validateOutComesList = new ArrayList<>();
			dashboardService.nonSignal(validateOutComesList, BigInteger.valueOf(1l));
		}catch(Exception ex){
			logger.info("ex");
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
			logger.info(ex);
		}
	}
	
	@Test
	public void testValidatedSignalWithRisk() {
		try{
			List<ValidationOutComesDTO> validateOutComesList = new ArrayList<>();
			dashboardService.validatedSignalWithRisk(validateOutComesList, BigInteger.valueOf(1l));
		}catch(Exception ex){
			logger.info("ex");
		}
	}
	
	@Test
	public void testValiatedSignalWithoutRisk() {
		try{
			List<ValidationOutComesDTO> validateOutComesList = new ArrayList<>();
			dashboardService.valiatedSignalWithoutRisk(validateOutComesList, BigInteger.valueOf(1l));
		}catch(Exception ex){
			logger.info("ex");
		}
	}
	
	@Test
	public void testGetDetectedSignalDetails() {
		try{
			dashboardService.getDetectedSignalDetails();
		}catch(Exception ex){
			logger.info("ex");
		}
	}
	
	@Test
	public void testGetSignalStrength() throws ApplicationException{
		
		Calendar calendarMonthOld = Calendar.getInstance();
		calendarMonthOld.add(Calendar.MONTH, -1);

		
		Topic topic = new Topic();
		topic.setId(1L);
		topic.setSignalStatus("Completed");
		topic.setSourceName("Test Source");
		topic.setCreatedDate(calendarMonthOld.getTime());
		List<SignalURL> urls = new ArrayList<>();
		SignalURL url = new SignalURL();
		url.setUrl("Test Url");
		urls.add(url);
		topic.setSignalUrls(urls);
		Set<SignalStatistics> stats = new HashSet<>();
		SignalStatistics signalStatistics = new SignalStatistics();
		signalStatistics.setScore(1);
		signalStatistics.setLb(1.0);
		signalStatistics.setUb(10.0);
		signalStatistics.setAlgorithm("ROR");
		signalStatistics.setTopic(topic);
		stats.add(signalStatistics);
		topic.setSignalStatistics(stats);
		
		given(this.signalService.findById(1L)).willReturn(topic);
		
		List<Topic> list=new ArrayList<>();
		given(this.signalMatchService.getMatchingSignals(topic)).willReturn(list);
		Set<Long> set=new HashSet<>();
		set.add(1l);
		List<SignalStatistics> statList = stats.stream().collect(Collectors.toList());
		
		given(this.signalStatisticsRepository.findStatisticsByTopicsIds(set)).willReturn(statList);
		
		List<Long> list1=new ArrayList<>();
		list1.add(1L);
		dashboardService.getSignalStrength(list1);
	}
	

}
