package com.gammon.qs.service.jobCost;

import java.util.Date;
import java.util.List;

import com.gammon.qs.io.ExcelWorkbook;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.util.DateUtil;
import com.gammon.qs.wrapper.monthEndResult.AccountBalanceByDateRangeWrapper;

public class AccountBalanceExcelGenerator {
	private ExcelFile excelFile;
	ExcelWorkbook excelDoc;
	private List<AccountBalanceByDateRangeWrapper> accountBalanceWrapperList;
	private String jobNumber;
	
	public AccountBalanceExcelGenerator(List<AccountBalanceByDateRangeWrapper> accountBalanceByDateRangeWrapper, String jobNumber){
		this.accountBalanceWrapperList = accountBalanceByDateRangeWrapper;
		this.jobNumber = jobNumber;
	}
	
	public ExcelFile generate() throws Exception{
		
		excelFile = new ExcelFile();
		excelFile.setFileName("J" + jobNumber+ " Account Balance " + DateUtil.formatDate(new Date(), "yyyy-MM-dd HHmmss")+ ExcelFile.EXTENSION);
		
		excelDoc = excelFile.getDocument();
		
		if(accountBalanceWrapperList.size()> 0){
			excelFile.setEmpty(false);
			excelDoc.setCurrentSheetName("job("+this.jobNumber+")");
			
			generateSheet();
		}
		else
			excelFile.setEmpty(true);
		
		System.err.println("AccountBalanceExcelGenerator - Excel File is generated:"+excelFile.getFileName());
		return excelFile;
	}
	
	private void generateSheet(){
		int sheetSize = 6;
		
		//Set Title Row
		excelDoc.insertRow(createTitleRow(sheetSize));
		excelDoc.setCellFontBold(0, 0, 0, 6); //(start row, start column, end row, end column)
		
		//Set Date Rows
		int row = 1;
		for(AccountBalanceByDateRangeWrapper accountBalanceWrapper: this.accountBalanceWrapperList){
			int index = 0;
			excelDoc.insertRow(new String[5]);

			excelDoc.setCellValue(row, index++, accountBalanceWrapper.getObjectCode()!=null ? accountBalanceWrapper.getObjectCode().toString():"", true);
			excelDoc.setCellValue(row, index++, accountBalanceWrapper.getSubsidiaryCode()!= null ? accountBalanceWrapper.getSubsidiaryCode().toString():"", true);
			excelDoc.setCellValue(row, index++, accountBalanceWrapper.getAccountCodeDescription()!=null ? accountBalanceWrapper.getAccountCodeDescription().trim():"", true);
			excelDoc.setCellValue(row, index++, accountBalanceWrapper.getAmountJI()!=null ? accountBalanceWrapper.getAmountJI().toString():"0", false);
			excelDoc.setCellValue(row, index++, accountBalanceWrapper.getAmountAA()!=null ? accountBalanceWrapper.getAmountAA().toString():"0", false);
		
			Double variance = (accountBalanceWrapper.getAmountJI()!=null && accountBalanceWrapper.getAmountAA()!=null)?accountBalanceWrapper.getAmountJI()-accountBalanceWrapper.getAmountAA():0.00;
			excelDoc.setCellValue(row, index++, variance.toString(), false);
			
		row++;
		}
		//Set Width size
		excelFile.getDocument().setColumnWidth(0, 15);
		excelFile.getDocument().setColumnWidth(1, 20);
		excelFile.getDocument().setColumnWidth(2, 50);
		excelFile.getDocument().setColumnWidth(3, 20);
		excelFile.getDocument().setColumnWidth(4, 20);
		excelFile.getDocument().setColumnWidth(5, 15);			
	}
	
		
	private String[] createTitleRow(int sheetSize){
		String[] titleRows = new String[sheetSize];
		
		titleRows[0] = "Object Code";
		titleRows[1] = "Subsidiary Code";
		titleRows[2] = "Description";
		titleRows[3] = "Internal Value(JI)";
		titleRows[4] = "Actual Value(AA)";
		titleRows[5] = "Variance";
		
		return titleRows;
	}

}
