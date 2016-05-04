package com.gammon.qs.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.gammon.qs.io.ExcelWorkbook;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.wrapper.RepackagingDetailComparisonWrapper;

/* generates an ExcelFile for a given List<RepackagingDetailComparisonWrapper> - matthewatc 3/2/12 */
public class RepackagingDetailExcelGenerator {
	private ExcelFile excelFile;
	ExcelWorkbook excelDoc;
	private List<RepackagingDetailComparisonWrapper> resultList;
	private SimpleDateFormat dateFormatter;
	
	
	private final static int NUMBER_OF_COLUMNS = 10;
	
	public RepackagingDetailExcelGenerator(List<RepackagingDetailComparisonWrapper> resultList) {
		this.resultList = resultList;
		dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
	}
	
	public ExcelFile generate() {
		
		excelFile = new ExcelFile();
		excelFile.setFileName("Repackaging Detail " + dateFormatter.format(new Date()) + ExcelFile.EXTENSION);
		
		excelDoc = excelFile.getDocument();
		
		if(resultList.size()> 0){
			excelFile.setEmpty(false);
			excelDoc.setCurrentSheetName("Repackaging Detail");
			
			generateSheet();
		} else {
			excelFile.setEmpty(true);
		}
		
		return excelFile;
	}
	
	private void generateSheet() {
		//Set Title Row
		excelDoc.insertRow(getTitleRow(NUMBER_OF_COLUMNS));
		excelDoc.setCellFontBold(0, 0, 0, NUMBER_OF_COLUMNS); //(start row, start column, end row, end column)
		
		//insert content rows
		int rowNumber = 1;
		for(RepackagingDetailComparisonWrapper wrapper : resultList){
			fillInContent(wrapper, rowNumber);
			rowNumber++;
		}
		
		//Set Width size
		excelFile.getDocument().setColumnWidth(0, 13);
		excelFile.getDocument().setColumnWidth(1, 13);
		excelFile.getDocument().setColumnWidth(2, 17); 
		excelFile.getDocument().setColumnWidth(3, 40);
		excelFile.getDocument().setColumnWidth(4, 5);
		excelFile.getDocument().setColumnWidth(5, 11);
		excelFile.getDocument().setColumnWidth(6, 18);
		excelFile.getDocument().setColumnWidth(7, 18);
		excelFile.getDocument().setColumnWidth(8, 18);
		excelFile.getDocument().setColumnWidth(9, 20);
	}
	
	/**
	 * 
	 * Fill in row by row and be able to set the cell type
	 * @author tikywong
	 * created on Jan 21, 2013 12:00:22 PM
	 */
	private void fillInContent(RepackagingDetailComparisonWrapper wrapper, int row){
		Double amount = wrapper.getAmount();
		Double prevAmount = wrapper.getPreviousAmount();
		Double variance = amount == null ? -prevAmount : prevAmount == null ? amount : amount - prevAmount;
		
		excelDoc.setCellValue(row, 0, wrapper.getPackageNo() 		!= null ? wrapper.getPackageNo().toString()		: "", true);
		excelDoc.setCellValue(row, 1, wrapper.getObjectCode() 		!= null ? wrapper.getObjectCode().toString()	: "", true);
		excelDoc.setCellValue(row, 2, wrapper.getSubsidiaryCode() 	!= null ? wrapper.getSubsidiaryCode().toString(): "", true);
		excelDoc.setCellValue(row, 3, wrapper.getDescription() 		!= null ? wrapper.getDescription() 				: "", true);
		excelDoc.setCellValue(row, 4, wrapper.getUnit()		 		!= null ? wrapper.getUnit()		 				: "");
		excelDoc.setCellValue(row, 5, wrapper.getRate()				!= null ? wrapper.getRate().toString()			: "");
		excelDoc.setCellValue(row, 6, prevAmount					!= null ? prevAmount.toString()					: "");
		excelDoc.setCellValue(row, 7, amount						!= null ? amount.toString()						: "");
		excelDoc.setCellValue(row, 8, variance						!= null ? variance.toString() 					: "");
		excelDoc.setCellValue(row, 9, wrapper.getResourceType()		!= null ? wrapper.getResourceType() 			: "");
	}
		
	private String[] getTitleRow(int sheetSize) {
		String[] titleRow = new String[sheetSize];
		
		titleRow[0] = "Package No.";
		titleRow[1] = "Object Code";
		titleRow[2] = "Subsidiary Code";
		titleRow[3] = "Resource Description";
		titleRow[4] = "Unit";
		titleRow[5] = "Rate";
		titleRow[6] = "Previous Amount";
		titleRow[7] = "Current Amount";
		titleRow[8] = "Variance";
		titleRow[9] = "Type";
		
		return titleRow;
	}

}
