package com.deloitte.smt.servicetest;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.deloitte.smt.SignalManagementApplication;
import com.deloitte.smt.dto.SignalAlgorithmDTO;
import com.deloitte.smt.service.ExportExcelService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SignalManagementApplication.class)
@TestPropertySource(locations = {"classpath:test.properties"})
public class ExportExcelServiceTest {
	
	@Autowired
	private ExportExcelService exportExcelService;
	
	@Test
	public void testInsert() {
		try{
			List<SignalAlgorithmDTO> signalDTOList = new ArrayList<>();
			SignalAlgorithmDTO signalAlgorithmDTO = new SignalAlgorithmDTO();
			signalAlgorithmDTO.setAlgorithmBCPNNLB("A");
			signalAlgorithmDTO.setAlgorithmBCPNNScore(1);
			signalAlgorithmDTO.setAlgorithmBCPNNUB("B");
			signalAlgorithmDTO.setAlgorithmEBLB("C");
			signalAlgorithmDTO.setAlgorithmEBScore(2);
			signalAlgorithmDTO.setAlgorithmEBUB("D");
			signalAlgorithmDTO.setAlgorithmPRRLB(3);
			signalAlgorithmDTO.setAlgorithmPRRScore(4);
			signalAlgorithmDTO.setAlgorithmPRRScore(5);
			signalAlgorithmDTO.setAlgorithmPRRSQ("E");
			signalAlgorithmDTO.setAlgorithmPRRUB(7);
			signalAlgorithmDTO.setSocDescription("Soc");
			signalAlgorithmDTO.setSmq(true);
			signalAlgorithmDTO.setSignal("Y");
			signalAlgorithmDTO.setPtDescription("PT");
			signalAlgorithmDTO.setPp(6);
			signalAlgorithmDTO.setOutcomeRI(7);
			signalAlgorithmDTO.setOutcomeOT(8);
			signalAlgorithmDTO.setOutcomeLT(6);
			signalAlgorithmDTO.setOutcomeHO(9);
			signalAlgorithmDTO.setOutcomeDE(2);
			signalAlgorithmDTO.setOutcomeCA(4);
			signalAlgorithmDTO.setFamilyDescription("XYZ");
			signalAlgorithmDTO.setAlgorithmRRRUB(3);
			signalAlgorithmDTO.setAlgorithmRRRScore(4);
			signalDTOList.add(signalAlgorithmDTO);
			exportExcelService.writeExcel(signalDTOList, "/src");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}

}
