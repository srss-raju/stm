package com.deloitte.smt.servicetest;

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
import com.deloitte.smt.dto.DetectionTopicDTO;
import com.deloitte.smt.service.SignalAdditionalService;
import com.deloitte.smt.service.SignalService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SignalManagementApplication.class)
@TestPropertySource(locations = { "classpath:test.properties" })
public class SignalAdditionalServiceTest {

	private final Logger logger = LogManager.getLogger(this.getClass());
	
	@MockBean
	SignalService signalService;
	
	@Autowired
	SignalAdditionalService signalAdditionalService;
	
	 @Test
	public void testSaveSignalsAndNonSignals() {
		try {
			List<DetectionTopicDTO> detectionTopicDTOs = new ArrayList<>();
			DetectionTopicDTO dto1 = new DetectionTopicDTO();
			dto1.setProductName("P1");
			dto1.setPtDesc("PT1");
			dto1.setRunInstanceId("1");
			dto1.setProductKey(1l);
			dto1.setPrrScore("1");
			dto1.setPrrCi95Lb("1");
			dto1.setPrrCi95Ub("1");
			dto1.setPrrStDev("1");
			dto1.setRorScore("1");
			dto1.setRorCi95Lb("1");
			dto1.setRorCi95Ub("1");
			dto1.setRorStDev("1");
			dto1.setRrrScore("1");
			dto1.setRrrCi95Lb("1");
			dto1.setRrrCi95Ub("1");
			dto1.setRrrStDev("1");
			dto1.setMgpsScore("1");
			dto1.setMgpsCi95Lb("1");
			dto1.setMgpsCi95Ub("1");
			dto1.setBcpnnScore("1");
			dto1.setBcpnnCi95Lb("1");
			dto1.setBcpnnCi95Ub("1");
			dto1.setISsignal(3l);
			DetectionTopicDTO dto2 = new DetectionTopicDTO();
			dto2.setProductName("P2");
			dto2.setPtDesc("PT2");
			dto2.setRunInstanceId("2");
			dto2.setProductKey(2l);
			dto2.setISsignal(0l);
			detectionTopicDTOs.add(dto1);
			detectionTopicDTOs.add(dto2);
			signalAdditionalService
					.saveSignalsAndNonSignals(detectionTopicDTOs);
		} catch (Exception ex) {
			logger.error(ex);
		}
	}
	 
		public void testSaveSignalsAndNonSignalsWithNulls() {
			try {
				List<DetectionTopicDTO> detectionTopicDTOs = new ArrayList<>();
				DetectionTopicDTO dto1 = new DetectionTopicDTO();
				dto1.setProductName("P1");
				dto1.setPtDesc("PT1");
				dto1.setRunInstanceId("1");
				dto1.setProductKey(1l);
				DetectionTopicDTO dto2 = new DetectionTopicDTO();
				dto2.setProductName("P2");
				dto2.setPtDesc("PT2");
				dto2.setRunInstanceId("2");
				dto2.setProductKey(2l);
				detectionTopicDTOs.add(dto1);
				detectionTopicDTOs.add(dto2);
				signalAdditionalService
						.saveSignalsAndNonSignals(detectionTopicDTOs);
			} catch (Exception ex) {
				logger.error(ex);
			}
		}
	
}
