package com.gammon.qs.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.gammon.qs.io.ExcelWorkbook;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.wrapper.UDC;

public class WorkScopeExcelGenerator {
	private ExcelFile excelFile;
	ExcelWorkbook excelDoc;
	private List<UDC> resultList;
	private SimpleDateFormat dateFormatter;
	
	
	private final static int NUMBER_OF_COLUMNS = 2;
	
	public WorkScopeExcelGenerator(List<UDC> resultList) {
		this.resultList = resultList;
		dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
	}
	
	public ExcelFile generate() {
		
		excelFile = new ExcelFile();
		excelFile.setFileName("Work Scope " + dateFormatter.format(new Date()) + ExcelFile.EXTENSION);
		
		excelDoc = excelFile.getDocument();
		
		if(resultList.size()> 0){
			excelFile.setEmpty(false);
			excelDoc.setCurrentSheetName("Work Scope");
			
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
		for(UDC udc : resultList) {
			excelDoc.insertRow(createContentRow(udc, NUMBER_OF_COLUMNS));
		}
		
		//Set Width size
		excelFile.getDocument().setColumnWidth(0, 20);
		excelFile.getDocument().setColumnWidth(1, 40);
	}
	
	private String[] createContentRow(UDC udc, int sheetSize) {
		String[] contentRow = new String[sheetSize];
				
		contentRow[0] = udc.getCode() != null ? udc.getCode() : "";
		contentRow[1] = udc.getDescription() != null ? udc.getDescription() : "";

		return contentRow;
	}
		
	private String[] getTitleRow(int sheetSize) {
		String[] titleRow = new String[sheetSize];
		
		titleRow[0] = "Work Scope";
		titleRow[1] = "Description";
		
		return titleRow;
	}

}
