package com.deloitte.smt.servicetest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.deloitte.smt.dto.SignalAlgorithmDTO;
import com.deloitte.smt.service.ExportExcelService;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = { "classpath:test.properties" })
public class ExportExcelServiceTest {
	
	@Autowired
	ExportExcelService exportExcelService;
	
	
	@Test
	public void testwriteExcel()throws Exception{
		File tempFile = new File("temp.txt");
		
		List<SignalAlgorithmDTO> signalDTOList=new ArrayList<>();
		SignalAlgorithmDTO dto =new SignalAlgorithmDTO();
		dto.setFamilyDescription("IBUPROFEN");
		dto.setSocDescription("INJURY, POISONING AND PROCEDURAL COMPLICATIONS");
		dto.setPtDescription("MEDICATION ERROR");
		dto.setCases(5f);
		dto.setOutcomeDE(1.22f);
		dto.setOutcomeCA(1.34f);
		dto.setOutcomeHO(1.56f);
		dto.setOutcomeLT(1.66f);
		dto.setOutcomeOT(1.13f);
		dto.setOutcomeRI(1.43f);
		dto.setAlgorithmBCPNNLB("3.21");
		dto.setAlgorithmBCPNNScore(2.15f);
		dto.setAlgorithmBCPNNUB("1.112");
		dto.setAlgorithmEBLB("2.22");
		dto.setAlgorithmEBScore(1.22f);
		dto.setAlgorithmEBUB("3.12");
		dto.setAlgorithmPRRLB(0.455f);
		dto.setAlgorithmPRRScore(2.67f);
		dto.setAlgorithmPRRSQ("1.12");
		dto.setAlgorithmPRRSTDev("2.25");
		dto.setAlgorithmPRRSTDev("3.33");
		dto.setAlgorithmPRRUB(4.31f);
		dto.setAlgorithmRORLB(1.12f);
		dto.setAlgorithmRORScore(1.43f);
		dto.setAlgorithmRORSTDev("2.11");
		dto.setAlgorithmRORUB(3.44f);
		dto.setAlgorithmRRRInfoComp("2.21");
		dto.setAlgorithmRRRLB(3.33f);
		dto.setAlgorithmRRRScore(2.54f);
		dto.setAlgorithmRRRSTDev("3.54");
		dto.setAlgorithmRRRUB(5.65f);
		dto.setPp(1.11f);
		dto.setSignal("Y");
		dto.setSignalId("110");
		signalDTOList.add(dto);
		
		tempFile.createNewFile();
		exportExcelService.writeExcel(signalDTOList,tempFile.getName());
		deleteFile(tempFile);
	}
	
	private void deleteFile(File tempFile){
		if(tempFile.exists()){
			tempFile.delete();
			
		}
	}
	

}
