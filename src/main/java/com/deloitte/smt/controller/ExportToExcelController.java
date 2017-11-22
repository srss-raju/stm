package com.deloitte.smt.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.smt.dto.SignalAlgorithmDTO;
import com.deloitte.smt.service.ExportExcelService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/camunda/api/signal")
public class ExportToExcelController {
	private static final Logger LOG = Logger
			.getLogger(ExportToExcelController.class);

	@Autowired
	ExportExcelService exportExcelService;

	private static final String EXPORT_EXCEL = "ExportDetectionDetails.xls";

	@PostMapping(value = "/exportExcel")
	public void generateExcel(
			@RequestParam(value = "data") String detectionDetails,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		List<SignalAlgorithmDTO> signalAlgorithmDtoList = new ObjectMapper()
				.readValue(detectionDetails,
						new TypeReference<List<SignalAlgorithmDTO>>() {
						});
		try {
			HSSFWorkbook workbook = exportExcelService.writeExcel(
					signalAlgorithmDtoList, EXPORT_EXCEL);
			response.setContentType("application/vnd.ms-excel");

			response.setHeader("Content-disposition", "attachment; filename="
					+ EXPORT_EXCEL);
			ServletOutputStream out = response.getOutputStream();
			workbook.write(out);
			out.flush();
			out.close();

		} catch (IOException e) {
			LOG.info("Exception occured while exporting excel sheet " + e);
		}
	}

}
