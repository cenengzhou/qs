/**
 * @author heisonwong
 * 
 * 3Aug 2015 14:40
 * 
 *  Excel and PDF Report for Purchase Order
 * 
 * **/

package com.gammon.qs.service.jobCost;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.gammon.qs.domain.PORecord;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.io.ExcelWorkbook;


public class PurchaseOrderExcelGenerator {
	
	private ExcelFile excelFile;
	private ExcelWorkbook excelDoc;
	private int numOfColumn = 11;
	
	private List<PORecord> purchaseOrderList;
	private ArrayList<String[]> data = new ArrayList<String[]>();
	private transient Logger logger = Logger.getLogger(PurchaseOrderExcelGenerator.class.getName());
	
	static final short ALIGN_RIGHT = 3;
	// constructor
	public PurchaseOrderExcelGenerator(List<PORecord> purchaseOrderList){
		this.purchaseOrderList = purchaseOrderList;
	}
	
	public ExcelFile generate() throws Exception{
		excelFile = new ExcelFile();
		excelFile.setFileName("Purchase Order Enquiry Search Result" + ExcelFile.EXTENSION);
		excelDoc = excelFile.getDocument();
		
		if(purchaseOrderList.size()>0){
			excelDoc.setCurrentSheetName("Purchase Order Enquiry");
			logger.info("PurchaseOrderExcelGenerator - Generating sheet");
			excelFile.getDocument().insertRow(this.createTitleRow());
			generatePurchaseOrderSheet();
		}else
			excelFile.setEmpty(true);			

		return this.excelFile;
	}
	
	// set the title row
	private String[] createTitleRow(){
		String[] titleRow = new String[numOfColumn];
		
		titleRow[0] = "Supplier No.";
		titleRow[1] = "Order No.";
		titleRow[2] = "Order Type";
		titleRow[3] = "Line Description";
		titleRow[4] = "Line Description2";
		titleRow[5] = "Object Code";
		titleRow[6] = "Subsidiary Code";
		titleRow[7] = "Currency";
		titleRow[8] = "Original Ordered Amount";
		titleRow[9] = "Order Date";
		titleRow[10] = "G/L Date";
		return titleRow;
	}
	
	// set one content row
	private String[] createContentRow(PORecord poRecord){
		int index = 0;
		String[] contentRow = new String[numOfColumn];

		contentRow[index++] =  poRecord.getAddressNumber().toString();
		contentRow[index++] =  poRecord.getDocumentOrderInvoiceE().toString();
		contentRow[index++] =  poRecord.getOrderType();
		contentRow[index++] =  poRecord.getDescriptionLine1();
		contentRow[index++] =  poRecord.getDescriptionLine2();
		contentRow[index++] =  poRecord.getObjectAccount();
		contentRow[index++] =  poRecord.getSubsidiary();
		contentRow[index++] =  poRecord.getCurrencyCodeFrom();
		contentRow[index++] =  poRecord.getAmountExtendedPrice().toString();
		contentRow[index++] =  date2String(poRecord.getDateTransactionJulian());
		contentRow[index++] =  date2String(poRecord.getDtForGLAndVouch1());

		return contentRow;
	}
	
	private String date2String(Date date){
		if (date!=null)
			return (new SimpleDateFormat("dd/MM/yyyy")).format(date);
		else
			return " ";
	}
	
	private void generatePurchaseOrderSheet() throws Exception {

		excelDoc.setCellFontBold(0, 0, 0, numOfColumn);
		
		for(PORecord poRecord: purchaseOrderList){
			String[] contentRow = createContentRow(poRecord);
			if (contentRow.length>0){
				if (contentRow[0]!=null && !contentRow[0].equals("")){
					data.add(contentRow);
				}
			}
		}
	
		// Create data
		for(int i = 0; i<data.size(); i++)
			excelFile.getDocument().insertRow(data.get(i));
		
		
		//set column size
		excelFile.getDocument().setColumnWidth(0, 12);
		excelFile.getDocument().setColumnWidth(1, 10);
		excelFile.getDocument().setColumnWidth(2, 11);
		excelFile.getDocument().setColumnWidth(3, 40);
		excelFile.getDocument().setColumnWidth(4, 40);
		excelFile.getDocument().setColumnWidth(5, 12);
		excelFile.getDocument().setColumnWidth(6, 15);
		excelFile.getDocument().setColumnWidth(7, 9);
		excelFile.getDocument().setColumnWidth(8, 23);
		excelFile.getDocument().setColumnWidth(9, 11);
		excelFile.getDocument().setColumnWidth(10, 11);
	
		for(int i = 0; i<data.size(); i++){//start at the sec column: data, format 1)negative number to in Red color, 2) "," to separate 3 characters
			excelFile.getDocument().setCellNegativeRed(i+1, 8);
			excelFile.getDocument().setDateStyle(i+1, 9); //datestyle dd/MM/yy
			excelFile.getDocument().setDateStyle(i+1, 10);
			} 	
		//excelFile.getDocument().setCellDataFormat("#,##0.00;[Red]-#,##0.00", 0, 8, purchaseOrderList.size(), 8); //format 1)negative number to in Red color, 2) "," to separate 3 characters
		//excelFile.getDocument().setCellAlignment(ExcelWorkbook.ALIGN_H_RIGHT, purchaseOrderList.size(), numOfColumn); //align all the fields to right	
	}



}
