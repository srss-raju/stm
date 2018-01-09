package com.deloitte.smt.servicetest;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

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
import com.deloitte.smt.entity.SignalStatistics;
import com.deloitte.smt.repository.SignalStatisticsRepository;
import com.deloitte.smt.service.SignalStatisticsService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class SignalStatisticsServiceTest {
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private SignalStatisticsService signalStatisticsService;
	
	@MockBean
	SignalStatisticsRepository signalStatisticsRepository;
	
    
	@Test
	public void testInsert() throws Exception{
		logger.info("testInsert");
		List<SignalStatistics> signalStatistics = new ArrayList<>();
		given(this.signalStatisticsRepository.save(signalStatistics)).willReturn(signalStatistics);
		signalStatisticsService.insert(signalStatistics);
	}
	
	@Test
	public void testFindById() throws Exception{
		given(this.signalStatisticsRepository.findByRunInstanceId(1l)).willReturn(null);
		signalStatisticsService.findSignalStatisticsByRunInstanceId(1l);
	}
	
}
