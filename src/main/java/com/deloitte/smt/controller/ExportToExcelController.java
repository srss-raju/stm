package com.deloitte.smt.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	public FileOutputStream generateExcel(@RequestBody List<SignalAlgorithmDTO> signalDTOList,HttpServletRequest request,HttpServletResponse response){
		FileOutputStream outputStream= null;
		try {
			 byte[] writeExcel = exportExcelService.writeExcel(signalDTOList, EXPORT_EXCEL);
			 
			response.setHeader("Content-disposition", "attachment; filename=ExportDetectionDetails.xls");
		response.getOutputStream().write(writeExcel);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outputStream;
	}
}
