package com.gammon.qs.service.jobCost;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.gammon.qs.io.ExcelWorkbook;
import com.gammon.pcms.helper.DateHelper;
import com.gammon.qs.domain.APRecord;
import com.gammon.qs.io.ExcelFile;

public class AccountPayableExcelGenerator {
	private ExcelFile excelFile;
	ExcelWorkbook excelDoc;
	private List<APRecord> apRecords;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	
	public AccountPayableExcelGenerator(List<APRecord> apRecords) {
		this.apRecords = apRecords;
	}

	/**
	 * @param apRecords the apRecords to set
	 */
	public void setApRecords(List<APRecord> apRecords) {
		this.apRecords = apRecords;
	}

	/**
	 * @return the apRecords
	 */
	public List<APRecord> getApRecords() {
		return apRecords;
	}

	public ExcelFile generate() throws Exception{
		
		excelFile = new ExcelFile();
		excelFile.setFileName("Supplier Ledger Enquiry " + DateHelper.formatDate(new Date(), "yyyy-MM-dd HHmmss")+ ExcelFile.EXTENSION);
		
		excelDoc = excelFile.getDocument();
		
		if(apRecords.size()> 0){
			excelFile.setEmpty(false);
			generateSheet();
		}
		else
			excelFile.setEmpty(true);
		
		logger.info("AccountPayableExcelGenerator - Excel File is generated:"+excelFile.getFileName());
		return excelFile;
	}
	
	private void generateSheet(){
		int sheetSize = 20;
		
		//Set Title Row
		excelDoc.insertRow(createTitleRow(sheetSize));
		excelDoc.setCellFontBold(0, 0, 0, sheetSize-1); //(start row, start column, end row, end column)
		
		//Set Date Rows
		for(APRecord apRecord: apRecords)
			excelDoc.insertRow(createContentRow(apRecord, sheetSize));
		
		//Set Width size
//		excelFile.getDocument().setColumnWidth(0, 15);
//		excelFile.getDocument().setColumnWidth(1, 20);
//		excelFile.getDocument().setColumnWidth(2, 50);
//		excelFile.getDocument().setColumnWidth(3, 20);
//		excelFile.getDocument().setColumnWidth(4, 20);
//		excelFile.getDocument().setColumnWidth(5, 15);			
	}
	
	private String[] createContentRow(APRecord apRecord, int sheetSize){
		String[] contentRows = new String[sheetSize];
		contentRows[0] = apRecord.getJobNumber();
		contentRows[1] = apRecord.getInvoiceNumber();
		contentRows[2] = apRecord.getSubledger();
		contentRows[3] = checkNull(apRecord.getSupplierNumber());
		contentRows[4] = apRecord.getDocumentType();
		contentRows[5] = checkNull(apRecord.getDocumentNumber());
		contentRows[6] = checkNull(apRecord.getGrossAmount());
		contentRows[7] = checkNull(apRecord.getOpenAmount());
		contentRows[8] = checkNull(apRecord.getForeignAmount());
		contentRows[9] = checkNull(apRecord.getForeignAmountOpen());
		contentRows[10] = apRecord.getPayStatus();
		contentRows[11] = apRecord.getCompany();
		contentRows[12] = apRecord.getCurrency();
		contentRows[13] = DateHelper.formatDate(apRecord.getInvoiceDate());
		contentRows[14] = DateHelper.formatDate(apRecord.getGlDate());
		contentRows[15] = DateHelper.formatDate(apRecord.getDueDate());
		contentRows[16] = checkNull(apRecord.getBatchNumber());
		contentRows[17] = apRecord.getBatchType();
		contentRows[18] = DateHelper.formatDate(apRecord.getBatchDate());
		contentRows[19] = apRecord.getSubledgerType();

		
		return contentRows;
	}
		
	private String[] createTitleRow(int sheetSize){
		String[] titleRows = new String[sheetSize];
		
		titleRows[0] = "Job Number";
		titleRows[1] = "Invoice Number";
		titleRows[2] = "Subledger";
		titleRows[3] = "Supplier Number";
		titleRows[4] = "Document Type";
		titleRows[5] = "Document Number";
		titleRows[6] = "Gross Amount";
		titleRows[7] = "Open Amount";
		titleRows[8] = "Foreign Amount";
		titleRows[9] = "Foreign Open Amount";
		titleRows[10] = "Pay Status";
		titleRows[11] = "Company";
		titleRows[12] = "Currency";
		titleRows[13] = "Invoice Date";
		titleRows[14] = "G/L Date";
		titleRows[15] = "Due Date";
		titleRows[16] = "Batch Number";
		titleRows[17] = "Batch Type";
		titleRows[18] = "Batch Date";
		titleRows[19] = "Subledge Type";
		
		return titleRows;
	}
	private String checkNull(Object obj){
		if (obj!=null)
			return obj.toString();
		return "";
	}
}
