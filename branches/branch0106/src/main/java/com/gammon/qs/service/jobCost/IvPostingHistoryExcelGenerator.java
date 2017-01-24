package com.gammon.qs.service.jobCost;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.gammon.qs.domain.IVPostingHist;
import com.gammon.qs.io.ExcelWorkbook;
import com.gammon.qs.io.ExcelFile;

public class IvPostingHistoryExcelGenerator {
	private ExcelFile excelFile;
	ExcelWorkbook excelDoc;
	private List<IVPostingHist> ivPostingHistoryList;
	private String jobNumber;
	private SimpleDateFormat dateFormatter;
	
	
	private final static int NUMBER_OF_COLUMNS = 10;
	
	public IvPostingHistoryExcelGenerator(List<IVPostingHist> ivPostingHistoryList, String jobNumber) {
		this.ivPostingHistoryList = ivPostingHistoryList;
		this.jobNumber = jobNumber;
		
		dateFormatter = new SimpleDateFormat("dd/MM/yyyy"); // for the spreadsheet date column and filename
	}
	
	public ExcelFile generate() throws Exception {
		
		excelFile = new ExcelFile();
		excelFile.setFileName("J" + jobNumber + " IV Posting History " + dateFormatter.format(new Date()) + ExcelFile.EXTENSION);
		
		excelDoc = excelFile.getDocument();
		
		if(ivPostingHistoryList.size()> 0){
			excelFile.setEmpty(false);
			excelDoc.setCurrentSheetName("job("+this.jobNumber+")");
			
			generateSheet();
		}
		else
			excelFile.setEmpty(true);
		
		System.err.println("IvPostingHistoryExcelGenerator - Excel File is generated:"+excelFile.getFileName());
		return excelFile;
	}
	
	private void generateSheet(){
		//Set Title Row
		excelDoc.insertRow(getTitleRow(NUMBER_OF_COLUMNS));
		excelDoc.setCellFontBold(0, 0, 0, NUMBER_OF_COLUMNS); //(start row, start column, end row, end column)
		
		//insert content rows
		for(IVPostingHist ivPostingHistory : ivPostingHistoryList) {
			excelDoc.insertRow(createContentRow(ivPostingHistory, NUMBER_OF_COLUMNS));
		}
		
		//Set Width size
		excelFile.getDocument().setColumnWidth(0, 15);
		excelFile.getDocument().setColumnWidth(1, 15);
		excelFile.getDocument().setColumnWidth(2, 12);
		excelFile.getDocument().setColumnWidth(3, 15);
		excelFile.getDocument().setColumnWidth(4, 16);
		excelFile.getDocument().setColumnWidth(5, 30);
		excelFile.getDocument().setColumnWidth(6, 5);
		excelFile.getDocument().setColumnWidth(7, 15);
		excelFile.getDocument().setColumnWidth(8, 15);
		excelFile.getDocument().setColumnWidth(9, 15);

	}
	
	private String[] createContentRow(IVPostingHist ivPostingHistory, int sheetSize){
		String[] contentRow = new String[sheetSize];
				
		contentRow[0] = ivPostingHistory.getCreatedDate() != null ? dateFormatter.format(ivPostingHistory.getCreatedDate()) : "";
		contentRow[1] = ivPostingHistory.getCreatedUser() != null ? ivPostingHistory.getCreatedUser() : "";
		contentRow[2] = ivPostingHistory.getPackageNo() != null ? ivPostingHistory.getPackageNo() : "";
		contentRow[3] = ivPostingHistory.getObjectCode() != null ? ivPostingHistory.getObjectCode() : "";
		contentRow[4] = ivPostingHistory.getSubsidiaryCode() != null ? ivPostingHistory.getSubsidiaryCode() : "";
		contentRow[5] = ivPostingHistory.getResourceDescription() != null ? ivPostingHistory.getResourceDescription() : "";
		contentRow[6] = ivPostingHistory.getUnit() != null ? ivPostingHistory.getUnit() : "";
		contentRow[7] = ivPostingHistory.getRate() != null ? ivPostingHistory.getRate().toString() : "";
		contentRow[8] = ivPostingHistory.getIvMovementAmount() != null ? ivPostingHistory.getIvMovementAmount().toString() : "";
		contentRow[9] = ivPostingHistory.getDocumentNo() != null ? ivPostingHistory.getDocumentNo().toString().trim() : "";

		return contentRow;
	}
		
	private String[] getTitleRow(int sheetSize){
		String[] titleRow = new String[sheetSize];
		
		titleRow[0] = "Date";
		titleRow[1] = "User";
		titleRow[2] = "Package No.";
		titleRow[3] = "Object Code";
		titleRow[4] = "Subsidiary Code";
		titleRow[5] = "Resource Description";
		titleRow[6] = "Unit";
		titleRow[7] = "Rate";
		titleRow[8] = "IV Movement";
		titleRow[9] = "Document No.";
		
		return titleRow;
	}

}
