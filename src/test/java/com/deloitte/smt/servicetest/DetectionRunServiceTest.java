package com.deloitte.smt.servicetest;

import static org.mockito.BDDMockito.given;

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
import com.deloitte.smt.entity.DetectionRun;
import com.deloitte.smt.repository.DetectionRunRepository;
import com.deloitte.smt.service.DetectionRunService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class DetectionRunServiceTest {
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	private DetectionRunService detectionRunService;
	
	@MockBean
	DetectionRunRepository detectionRunRepository;
		  
	@Test
	public void testInsert() throws Exception {
		logger.info("testGetSmtComplianceDetails");
		DetectionRun detectionRun = new DetectionRun();
		detectionRunService.insert(detectionRun);
	}
	
	@Test
	public void testFindById() throws Exception {
		logger.info("Test findById");
		DetectionRun detectionRun = new DetectionRun();
		given(this.detectionRunRepository.findOne(1l)).willReturn(detectionRun);
		detectionRunService.findById(1l);
	}
	
	@Test
	public void testFindByIdWithNull() throws Exception {
		try{
			detectionRunService.findById(1l);
		}catch(Exception ex){
			logger.info(ex);
		}
	}
	
	@Test
	public void testFindAll() throws Exception {
		detectionRunService.findAll();
	}
	
	@Test
	public void testFindByDetectionId() throws Exception {
		detectionRunService.findByDetectionId(1l);
	}

}
