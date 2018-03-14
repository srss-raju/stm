package com.deloitte.smt.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.HSSFRegionUtil;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deloitte.smt.constant.SmtConstant;
import com.deloitte.smt.dto.SignalAlgorithmDTO;
import com.deloitte.smt.entity.DectectionDataExport;
import com.deloitte.smt.repository.DectectionDataExportRepository;
import com.deloitte.smt.util.ServerResponseObject;

@Service
public class ExportExcelService {
	private static final Logger LOG = Logger.getLogger(ExportExcelService.class);
	/**
	 * This method attempts to write the workbook to fileoutputstream
	 * @param signalDTOList
	 * @param excelFilePath
	 * @return
	 * @throws IOException
	 */
	@Autowired
	private DectectionDataExportRepository dectectionDataExportRepository;
	
	public HSSFWorkbook writeExcel(List<SignalAlgorithmDTO> signalDTOList,
			String excelFilePath) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();

		CellStyle style = createStyle(workbook);

		createHeaderRow(signalDTOList,sheet, style, workbook);

		writeSignalDTO(signalDTOList, sheet, workbook);

		FileOutputStream outputStream = new FileOutputStream(excelFilePath);
		workbook.write(outputStream);
		return workbook;
	}

	private CellStyle createStyle(HSSFWorkbook wb) {

		HSSFCellStyle style = wb.createCellStyle();
		/* Create HSSFFont object from the workbook */
		HSSFFont hssfFont = wb.createFont();
		/* set the weight of the font */
		hssfFont.setBold(true);
		/* Also make the font color to Blue */
		hssfFont.setColor(HSSFColor.BLUE.index);
		/* attach the font to the style created earlier */
		style.setFont(hssfFont);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);

		style.setBorderLeft(BorderStyle.THIN);

		style.setBorderRight(BorderStyle.THIN);
		/* Add dashed top border */
		style.setBorderTop(BorderStyle.THIN);
		/* Add dotted bottom border */
		style.setBorderBottom(BorderStyle.THIN);
		
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		return style;

	}

	private void createHeaderRow(List<SignalAlgorithmDTO> signalDTOList,Sheet sheet, CellStyle style,
			HSSFWorkbook workbook) {

		sheet.setColumnWidth(0, 5000);
		sheet.setColumnWidth(1, 5000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(4, 3500);
			
		workbook.createCellStyle();
		/* Create HSSFFont object from the workbook */
		HSSFFont hssfFont = workbook.createFont();
		/* set the weight of the font */
		hssfFont.setBold(true);
		/* Also make the font color to RED */
		hssfFont.setColor(HSSFColor.BLUE.index);
		/* attach the font to the style created earlier */
		style.setFont(hssfFont);
		
		Row header = sheet.createRow(0);

		Cell cell0 = header.createCell(0);
		cell0.setCellStyle(style);
		cell0.setCellValue("DRUG");
	
		Cell cell1 = header.createCell(1);
		cell1.setCellStyle(style);
		cell1.setCellValue("CONDITION");

		Cell cell2 = header.createCell(2);
		cell2.setCellStyle(style);
		cell2.setCellValue("CASES");
		
		Cell cell10 = header.createCell(3);
		cell10.setCellStyle(style);
		cell10.setCellValue("ALGORITHMS");

		Cell cell25 = header.createCell(18);
		cell25.setCellStyle(style);
		cell25.setCellValue("SIGNAL");

		
		Row header2 = sheet.createRow(1);
		Cell row2cell10 = header2.createCell(3);
		row2cell10.setCellStyle(style);
		row2cell10.setCellValue("PRR");

		Cell row2cell13 = header2.createCell(6);
		row2cell13.setCellStyle(style);
		row2cell13.setCellValue("ROR");

		Cell row2cell16 = header2.createCell(9);
		row2cell16.setCellStyle(style);
		row2cell16.setCellValue("RRR");

		Cell row2cell19 = header2.createCell(12);
		row2cell19.setCellStyle(style);
		row2cell19.setCellValue("EBGM");

		Cell row2cell22 = header2.createCell(15);
		row2cell22.setCellStyle(style);
		row2cell22.setCellValue("BCPNN");

		/*Row header3 = sheet.createRow(2);
		Cell row3cell4 = header3.createCell(4);
		row3cell4.setCellStyle(style);
		row3cell4.setCellValue("DE");
		Cell row3cell5 = header3.createCell(5);
		row3cell5.setCellStyle(style);
		row3cell5.setCellValue("HO");
		Cell row3cell6 = header3.createCell(6);
		row3cell6.setCellStyle(style);
		row3cell6.setCellValue("LT");

		Cell row3cell7 = header3.createCell(7);
		row3cell7.setCellStyle(style);
		row3cell7.setCellValue("CA");
		Cell row3cell8 = header3.createCell(8);
		row3cell8.setCellStyle(style);
		row3cell8.setCellValue("RI");
		Cell row3cell9 = header3.createCell(9);
		row3cell9.setCellStyle(style);
		row3cell9.setCellValue("OT");*/

		
		Row header3 = sheet.createRow(2);
		/** Algorithm attributes **/
		Cell row3cell10 = header3.createCell(3);
		row3cell10.setCellStyle(style);
		row3cell10.setCellValue(SmtConstant.SCORE.toString());
		Cell row3cell11 = header3.createCell(4);
		row3cell11.setCellStyle(style);
		row3cell11.setCellValue("LB");
		Cell row3cell12 = header3.createCell(5);
		row3cell12.setCellStyle(style);
		row3cell12.setCellValue("UB");

		Cell row3cell13 = header3.createCell(6);
		row3cell13.setCellStyle(style);
		row3cell13.setCellValue(SmtConstant.SCORE.toString());
		Cell row3cell14 = header3.createCell(7);
		row3cell14.setCellStyle(style);
		row3cell14.setCellValue("LB");
		Cell row3cell15 = header3.createCell(8);
		row3cell15.setCellStyle(style);
		row3cell15.setCellValue("UB");

		Cell row3cell16 = header3.createCell(9);
		row3cell16.setCellStyle(style);
		row3cell16.setCellValue(SmtConstant.SCORE.toString());
		Cell row3cell17 = header3.createCell(10);
		row3cell17.setCellStyle(style);
		row3cell17.setCellValue("LB");
		Cell row3cell18 = header3.createCell(11);
		row3cell18.setCellStyle(style);
		row3cell18.setCellValue("UB");
		
		Cell row3cell19 = header3.createCell(12);
		row3cell19.setCellStyle(style);
		row3cell19.setCellValue(SmtConstant.SCORE.toString());
		Cell row3cell20 = header3.createCell(13);
		row3cell20.setCellStyle(style);
		row3cell20.setCellValue("LB");
		Cell row3cell21 = header3.createCell(14);
		row3cell21.setCellStyle(style);
		row3cell21.setCellValue("UB");
		
		Cell row3cell22 = header3.createCell(15);
		row3cell22.setCellStyle(style);
		row3cell22.setCellValue(SmtConstant.SCORE.toString());
		Cell row3cell23 = header3.createCell(16);
		row3cell23.setCellStyle(style);
		row3cell23.setCellValue("LB");
		Cell row3cell24 = header3.createCell(17);
		row3cell24.setCellStyle(style);
		row3cell24.setCellValue("UB");
		
		
		// ** this method creates the cell range**/
				createCellRangeAddress(sheet,workbook);
	}
	
	

	private void createCellRangeAddress(Sheet sheet,HSSFWorkbook workbook) {
		List<CellRangeAddress> cellRangeAdressList=new ArrayList<>();
		//DRUG
		CellRangeAddress cellRangeAddress0 = new CellRangeAddress(0, 2, 0, 0);
		sheet.addMergedRegion(cellRangeAddress0);
		cellRangeAdressList.add(cellRangeAddress0);
		//SOC
		/*CellRangeAddress cellRangeAddress1 = new CellRangeAddress(0, 2, 1, 1);
		sheet.addMergedRegion(cellRangeAddress1);
		cellRangeAdressList.add(cellRangeAddress1);*/
		//CONDITION
		//CellRangeAddress cellRangeAddress2 = new CellRangeAddress(0, 2, 2, 2);
		CellRangeAddress cellRangeAddress2 = new CellRangeAddress(0, 2, 1, 1);
		sheet.addMergedRegion(cellRangeAddress2);
		cellRangeAdressList.add(cellRangeAddress2);
		//CASES
		//CellRangeAddress cellRangeAddress3 = new CellRangeAddress(0, 2, 3, 3);
		CellRangeAddress cellRangeAddress3 = new CellRangeAddress(0, 2, 2, 2);
		sheet.addMergedRegion(cellRangeAddress3);
		cellRangeAdressList.add(cellRangeAddress3);

		/*CellRangeAddress cellRangeAddress4 = new CellRangeAddress(0, 1, 4, 9);
		sheet.addMergedRegion(cellRangeAddress4);
		cellRangeAdressList.add(cellRangeAddress4);*/

		
		CellRangeAddress cellRangeAddress10 = new CellRangeAddress(0, 0, 3, 17);
		sheet.addMergedRegion(cellRangeAddress10);
		cellRangeAdressList.add(cellRangeAddress10);
		
		
		
		
		CellRangeAddress cellRangeAddress25 = new CellRangeAddress(0, 2, 18, 18);
		sheet.addMergedRegion(cellRangeAddress25);
		cellRangeAdressList.add(cellRangeAddress25);
		
		CellRangeAddress cellRangeAddress12 = new CellRangeAddress(1,1,3,5);
		sheet.addMergedRegion(cellRangeAddress12);
		cellRangeAdressList.add(cellRangeAddress12);
		
		CellRangeAddress cellRangeAddress14 = new CellRangeAddress(1,1,6,8);
		sheet.addMergedRegion(cellRangeAddress14);
		cellRangeAdressList.add(cellRangeAddress14);
		
		CellRangeAddress cellRangeAddress17 = new CellRangeAddress(1,1,9,11);
		sheet.addMergedRegion(cellRangeAddress17);
		cellRangeAdressList.add(cellRangeAddress17);
		
		CellRangeAddress cellRangeAddress19 = new CellRangeAddress(1, 1, 12, 14);
		sheet.addMergedRegion(cellRangeAddress19);
		cellRangeAdressList.add(cellRangeAddress19);
		
		CellRangeAddress cellRangeAddress22 = new CellRangeAddress(1, 1, 15, 17);
		sheet.addMergedRegion(cellRangeAddress22);
		cellRangeAdressList.add(cellRangeAddress22);
		
		for (CellRangeAddress rangeAddress : cellRangeAdressList) {
		
		HSSFRegionUtil.setBorderBottom(BorderStyle.THIN.ordinal(),
				rangeAddress, (HSSFSheet) sheet, workbook);
		HSSFRegionUtil.setBorderTop(BorderStyle.THIN.ordinal(),
				rangeAddress, (HSSFSheet) sheet, workbook);
		HSSFRegionUtil.setBorderLeft(BorderStyle.THIN.ordinal(),
				rangeAddress, (HSSFSheet) sheet, workbook);
		HSSFRegionUtil.setBorderRight(BorderStyle.THIN.ordinal(),
				rangeAddress, (HSSFSheet) sheet, workbook);
		}
	}
	
	private void writeSignalDTO(List<SignalAlgorithmDTO> signalDTOList,
			Sheet sheet, HSSFWorkbook workbook) {
		

		HSSFCellStyle style=	 workbook.createCellStyle();
			/* Create HSSFFont object from the workbook */
			HSSFFont hssfFont = workbook.createFont();
			/* set the weight of the font */
			hssfFont.setBold(false);
			/* Also make the font color to RED */
			hssfFont.setColor(HSSFColor.BLACK.index);
			/* attach the font to the style created earlier */
			style.setFont(hssfFont);
		
			int rowCount = 2;
			for (SignalAlgorithmDTO signalDTO : signalDTOList) {
				
				Row row = sheet.createRow(++rowCount);
			
			Cell cell = row.createCell(0);
			cell.setCellValue(signalDTO.getFamilyDescription());

			/*cell = row.createCell(1);
			cell.setCellValue(signalDTO.getSocDescription());*/

			cell = row.createCell(1);
			cell.setCellValue(signalDTO.getPtDescription());
			
			cell=row.createCell(2);
			cell.setCellValue(signalDTO.getCases());
			
			/*cell=row.createCell(4);
			cell.setCellValue(signalDTO.getOutcomeDE());
			
			cell=row.createCell(5);
			cell.setCellValue(signalDTO.getOutcomeHO());
			
			cell=row.createCell(6);
			cell.setCellValue(signalDTO.getOutcomeLT());
			
			cell=row.createCell(7);
			cell.setCellValue(signalDTO.getOutcomeCA());
			
			cell=row.createCell(8);
			cell.setCellValue(signalDTO.getOutcomeRI());
			
			cell=row.createCell(9);
			cell.setCellValue(signalDTO.getOutcomeOT());*/
			
			cell=row.createCell(3);
			cell.setCellValue(signalDTO.getAlgorithmPRRScore());
			
			cell=row.createCell(4);
			cell.setCellValue(signalDTO.getAlgorithmPRRLB());
			
			cell=row.createCell(5);
			cell.setCellValue(signalDTO.getAlgorithmPRRUB());
			
			cell=row.createCell(6);
			cell.setCellValue(signalDTO.getAlgorithmRORScore());
			
			cell=row.createCell(7);
			cell.setCellValue(signalDTO.getAlgorithmRORLB());
			
			cell=row.createCell(8);
			cell.setCellValue(signalDTO.getAlgorithmRORUB());
			
			cell=row.createCell(9);
			cell.setCellValue(signalDTO.getAlgorithmRRRScore());
			
			cell=row.createCell(10);
			cell.setCellValue(signalDTO.getAlgorithmRRRLB());
			
			cell=row.createCell(11);
			cell.setCellValue(signalDTO.getAlgorithmRRRUB());
			
			cell=row.createCell(12);
			cell.setCellValue(signalDTO.getAlgorithmEBScore());
			
			cell=row.createCell(13);
			cell.setCellValue(signalDTO.getAlgorithmEBLB());
			
			cell=row.createCell(14);
			cell.setCellValue(signalDTO.getAlgorithmEBUB());
			
			cell=row.createCell(15);
			cell.setCellValue(signalDTO.getAlgorithmBCPNNScore());
			
			cell=row.createCell(16);
			cell.setCellValue(signalDTO.getAlgorithmBCPNNLB());
			
			cell=row.createCell(17);
			cell.setCellValue(signalDTO.getAlgorithmBCPNNUB());
			
			cell=row.createCell(18);
			cell.setCellValue(signalDTO.getSignal());

		}
	}

	public ServerResponseObject uploadExcelData(String data) {
		ServerResponseObject res=null;
		try {
			res = new ServerResponseObject();
			DectectionDataExport d = new DectectionDataExport();
			d.setData(data);
			dectectionDataExportRepository.save(d);
			res.setStatus("Success");
			res.setResponse(d.getId());
			
		} catch (Exception e) {
			LOG.error("Error in Storing data....");
		}
		return res;
	}

	public DectectionDataExport findById(Long reportid) {
		return dectectionDataExportRepository.findOne(reportid);
	}
}
