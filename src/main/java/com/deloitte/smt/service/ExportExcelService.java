package com.deloitte.smt.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.stereotype.Service;

import com.deloitte.smt.dto.SignalAlgorithmDTO;

@Service
public class ExportExcelService {

	/**
	 * This method attempts to write the workbook to fileoutputstream
	 * @param signalDTOList
	 * @param excelFilePath
	 * @return
	 * @throws IOException
	 */
	public HSSFWorkbook writeExcel(List<SignalAlgorithmDTO> signalDTOList,
			String excelFilePath) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();

		CellStyle style = createStyle(workbook);

		createHeaderRow(sheet, style, workbook);

		writeSignalDTO(signalDTOList, sheet, workbook);

		FileOutputStream outputStream = new FileOutputStream(excelFilePath);
		workbook.write(outputStream);
		return workbook;
	}

	@SuppressWarnings("deprecation")
	private CellStyle createStyle(HSSFWorkbook wb) {

		HSSFCellStyle style = wb.createCellStyle();
		/* Create HSSFFont object from the workbook */
		HSSFFont my_font = wb.createFont();
		/* set the weight of the font */
		my_font.setBold(true);
		/* Also make the font color to Blue */
		my_font.setColor(HSSFColor.BLUE.index);
		/* attach the font to the style created earlier */
		style.setFont(my_font);
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

	private void createHeaderRow(Sheet sheet, CellStyle style,
			HSSFWorkbook workbook) {

		sheet.setColumnWidth(0, 5000);
		sheet.setColumnWidth(1, 15000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(4, 3500);
		sheet.setColumnWidth(7, 3500);
		sheet.setColumnWidth(10, 3500);
		
		workbook.createCellStyle();
		/* Create HSSFFont object from the workbook */
		HSSFFont my_font = workbook.createFont();
		/* set the weight of the font */
		my_font.setBold(true);
		/* Also make the font color to RED */
		my_font.setColor(HSSFColor.BLUE.index);
		/* attach the font to the style created earlier */
		style.setFont(my_font);
		
		Row header = sheet.createRow(0);

		Cell cell0 = header.createCell(0);
		cell0.setCellStyle(style);
		cell0.setCellValue("PRODUCT");

		Cell cell1 = header.createCell(1);
		cell1.setCellStyle(style);
		cell1.setCellValue("SOC");

		Cell cell2 = header.createCell(2);
		cell2.setCellStyle(style);
		cell2.setCellValue("PT");

		Cell cell3 = header.createCell(3);
		cell3.setCellStyle(style);
		cell3.setCellValue("CASES");

		Cell cell10 = header.createCell(4);
		cell10.setCellStyle(style);
		cell10.setCellValue("ALGORITHMS");

		Cell cell21 = header.createCell(15);
		cell21.setCellStyle(style);
		cell21.setCellValue("PP");

		Cell cell22 = header.createCell(16);
		cell22.setCellStyle(style);
		cell22.setCellValue("SIGNAL");

		Row header2 = sheet.createRow(1);
		Cell row2cell10 = header2.createCell(4);
		row2cell10.setCellStyle(style);
		row2cell10.setCellValue("PRR");

		Cell row2cell13 = header2.createCell(7);
		row2cell13.setCellStyle(style);
		row2cell13.setCellValue("ROR");

		Cell row2cell16 = header2.createCell(10);
		row2cell16.setCellStyle(style);
		row2cell16.setCellValue("RRR");

		Cell row2cell19 = header2.createCell(13);
		row2cell19.setCellStyle(style);
		row2cell19.setCellValue("EB05");

		Cell row2cell20 = header2.createCell(14);
		row2cell20.setCellStyle(style);
		row2cell20.setCellValue("BCPNN");

		Row header3 = sheet.createRow(2);

		/** Algorithm attributes **/
		Cell row3cell10 = header3.createCell(4);
		row3cell10.setCellStyle(style);
		row3cell10.setCellValue("PRRSCORE");
		Cell row3cell11 = header3.createCell(5);
		row3cell11.setCellStyle(style);
		row3cell11.setCellValue("PRRLB");
		Cell row3cell12 = header3.createCell(6);
		row3cell12.setCellStyle(style);
		row3cell12.setCellValue("PRRUB");

		Cell row3cell13 = header3.createCell(7);
		row3cell13.setCellStyle(style);
		row3cell13.setCellValue("RORSCORE");
		Cell row3cell14 = header3.createCell(8);
		row3cell14.setCellStyle(style);
		row3cell14.setCellValue("RORLB");
		Cell row3cell15 = header3.createCell(9);
		row3cell15.setCellStyle(style);
		row3cell15.setCellValue("RORUB");

		Cell row3cell16 = header3.createCell(10);
		row3cell16.setCellStyle(style);
		row3cell16.setCellValue("RRRSCORE");
		Cell row3cell17 = header3.createCell(11);
		row3cell17.setCellStyle(style);
		row3cell17.setCellValue("RRRLB");
		Cell row3cell18 = header3.createCell(12);
		row3cell18.setCellStyle(style);
		row3cell18.setCellValue("RRRUB");

		
		// ** this method creates the cell range**/
				createCellRangeAddress(sheet,workbook);
	}
	
	

	private void createCellRangeAddress(Sheet sheet,HSSFWorkbook workbook) {
		List<CellRangeAddress> cellRangeAdressList=new ArrayList<CellRangeAddress>();
	
		
		CellRangeAddress cellRangeAddress0 = new CellRangeAddress(0, 2, 0, 0);
		sheet.addMergedRegion(cellRangeAddress0);
		
		cellRangeAdressList.add(cellRangeAddress0);
		
		CellRangeAddress cellRangeAddress1 = new CellRangeAddress(0, 2, 1, 1);
		sheet.addMergedRegion(cellRangeAddress1);
		cellRangeAdressList.add(cellRangeAddress1);
		CellRangeAddress cellRangeAddress2 = new CellRangeAddress(0, 2, 2, 2);
		sheet.addMergedRegion(cellRangeAddress2);
		cellRangeAdressList.add(cellRangeAddress2);
		CellRangeAddress cellRangeAddress3 = new CellRangeAddress(0, 2, 3, 3);
		sheet.addMergedRegion(cellRangeAddress3);
		cellRangeAdressList.add(cellRangeAddress3);

		CellRangeAddress cellRangeAddress4 = new CellRangeAddress(0, 0, 4, 14);
		sheet.addMergedRegion(cellRangeAddress4);
		cellRangeAdressList.add(cellRangeAddress4);

		CellRangeAddress cellRangeAddress21 = new CellRangeAddress(0, 2, 15, 15);
		sheet.addMergedRegion(cellRangeAddress21);
		cellRangeAdressList.add(cellRangeAddress21);
		
		CellRangeAddress cellRangeAddress22 = new CellRangeAddress(0, 2, 16, 16);
		sheet.addMergedRegion(cellRangeAddress22);
		cellRangeAdressList.add(cellRangeAddress22);
		
		CellRangeAddress cellRangeAddress6 = new CellRangeAddress(1,1,4,6);
		sheet.addMergedRegion(cellRangeAddress6);
		cellRangeAdressList.add(cellRangeAddress6);
		
		CellRangeAddress cellRangeAddress7 = new CellRangeAddress(1,1,7,9);
		sheet.addMergedRegion(cellRangeAddress7);
		cellRangeAdressList.add(cellRangeAddress7);
		
		CellRangeAddress cellRangeAddress10 = new CellRangeAddress(1,1,10,12);
		sheet.addMergedRegion(cellRangeAddress10);
		cellRangeAdressList.add(cellRangeAddress10);
		
		CellRangeAddress cellRangeAddress13 = new CellRangeAddress(1,2,13,13);
		sheet.addMergedRegion(cellRangeAddress13);
		cellRangeAdressList.add(cellRangeAddress13);
		
		CellRangeAddress cellRangeAddress14 = new CellRangeAddress(1,2,14,14);
		sheet.addMergedRegion(cellRangeAddress14);
		cellRangeAdressList.add(cellRangeAddress14);
		 
		
		for (CellRangeAddress rangeAddress : cellRangeAdressList) {
		
		HSSFRegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN,
				rangeAddress, (HSSFSheet) sheet, workbook);
		HSSFRegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN,
				rangeAddress, (HSSFSheet) sheet, workbook);
		HSSFRegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN,
				rangeAddress, (HSSFSheet) sheet, workbook);
		HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN,
				rangeAddress, (HSSFSheet) sheet, workbook);
		}
	}
	
	private void writeSignalDTO(List<SignalAlgorithmDTO> signalDTOList,
			Sheet sheet, HSSFWorkbook workbook) {
		

		HSSFCellStyle style=	 workbook.createCellStyle();
			/* Create HSSFFont object from the workbook */
			HSSFFont my_font = workbook.createFont();
			/* set the weight of the font */
			my_font.setBold(false);
			/* Also make the font color to RED */
			my_font.setColor(HSSFColor.BLACK.index);
			/* attach the font to the style created earlier */
			style.setFont(my_font);
		
		int rowCount = 2;
		for (SignalAlgorithmDTO signalDTO : signalDTOList) {

			Row row = sheet.createRow(++rowCount);

			Cell cell = row.createCell(0);
			cell.setCellStyle(style);
			cell.setCellValue(signalDTO.getFamilyDescription());

			cell = row.createCell(1);
			cell.setCellStyle(style);
			cell.setCellValue(signalDTO.getSocDescription());

			cell = row.createCell(2);
			cell.setCellStyle(style);
			cell.setCellValue(signalDTO.getPtDescription());

			cell = row.createCell(3);
			cell.setCellStyle(style);
			cell.setCellValue(signalDTO.getCases());

			cell = row.createCell(4);
			cell.setCellStyle(style);
			cell.setCellValue(signalDTO.getAlgorithmPRRScore());

			cell = row.createCell(5);
			cell.setCellStyle(style);
			cell.setCellValue(signalDTO.getAlgorithmPRRLB());

			cell = row.createCell(6);
			cell.setCellStyle(style);
			cell.setCellValue(signalDTO.getAlgorithmPRRUB());

			cell = row.createCell(7);
			cell.setCellStyle(style);
			cell.setCellValue(signalDTO.getAlgorithmRORScore());

			cell = row.createCell(8);
			cell.setCellStyle(style);
			cell.setCellValue(signalDTO.getAlgorithmRORLB());

			cell = row.createCell(9);
			cell.setCellStyle(style);
			cell.setCellValue(signalDTO.getAlgorithmRORUB());

			cell = row.createCell(10);
			cell.setCellStyle(style);
			cell.setCellValue(signalDTO.getAlgorithmRRRScore());

			cell = row.createCell(11);
			cell.setCellStyle(style);
			cell.setCellValue(signalDTO.getAlgorithmRRRLB());

			cell = row.createCell(12);
			cell.setCellStyle(style);
			cell.setCellValue(signalDTO.getAlgorithmRRRUB());

			cell = row.createCell(13);
			cell.setCellStyle(style);
			cell.setCellValue(signalDTO.getAlgorithmEBScore());

			cell = row.createCell(14);
			cell.setCellStyle(style);
			cell.setCellValue(signalDTO.getAlgorithmBCPNNScore());

			cell = row.createCell(15);
			cell.setCellStyle(style);
			cell.setCellValue(signalDTO.getPp());
			cell = row.createCell(16);
			cell.setCellStyle(style);
			cell.setCellValue(signalDTO.getSignal());

		}
	}
}
