package com.deloitte.smt.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.smt.dto.SignalAlgorithmDTO;
import com.deloitte.smt.service.ExportExcelService;

@RestController
@RequestMapping("/camunda/api/signal")
public class ExportToExcelController {
	
	@Autowired
	ExportExcelService exportExcelService;
	
	private static final String EXPORT_EXCEL="ExportDetectionDetails.xls";
	
	@RequestMapping("/exportExcel")
	public void generateExcel(@RequestBody List<SignalAlgorithmDTO> signalDTOList){
		try {
			exportExcelService.writeExcel(signalDTOList, EXPORT_EXCEL);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
