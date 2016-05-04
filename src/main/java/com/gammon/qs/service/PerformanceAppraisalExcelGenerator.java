package com.gammon.qs.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.gammon.qs.io.ExcelWorkbook;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.wrapper.performanceAppraisal.PerformanceAppraisalWrapper;

/* generates an ExcelFile for a given List<PerformanceAppraisalWrapper>. Need to specify a Map for the performance group descriptions
 * with the performance group codes as keys with each value containing a description of its key code - matthewatc 3/2/12 */
public class PerformanceAppraisalExcelGenerator {
	private ExcelFile excelFile;
	private ExcelWorkbook excelDoc;
	private List<PerformanceAppraisalWrapper> wrapperList;
	private SimpleDateFormat dateFormatter;
	private Map<String,String> performanceGroupMap;
	
	private transient Logger logger = Logger.getLogger(PerformanceAppraisalExcelGenerator.class.getName());
	
	private final static int NUMBER_OF_COLUMNS = 10;
	
	public PerformanceAppraisalExcelGenerator(List<PerformanceAppraisalWrapper> wrapperList, Map<String,String> performanceGroupMap) {
		this.wrapperList = wrapperList;
		dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		this.performanceGroupMap = performanceGroupMap;
	}
	
	public ExcelFile generate() {
		
		excelFile = new ExcelFile();
		excelFile.setFileName("Performance Appraisal " + dateFormatter.format(new Date()) + ExcelFile.EXTENSION);
		
		excelDoc = excelFile.getDocument();
		
		if(wrapperList != null && wrapperList.size() > 0){
			logger.info("PerformanceAppraisalExcelGenerator");

			excelFile.setEmpty(false);
			excelDoc.setCurrentSheetName("Performance Appraisal");
			logger.info("PerformanceAppraisalExcelGenerator - Generating sheet");
			generateSheet();
		} else {
			logger.info("PerformanceAppraisalExcelGenerator - NULL");
			excelFile.setEmpty(true);
		}
		
		return excelFile;
	}
	
	private void generateSheet() {
		//Set Title Row
		excelDoc.insertRow(getTitleRow(NUMBER_OF_COLUMNS));
		excelDoc.setCellFontBold(0, 0, 0, NUMBER_OF_COLUMNS); //(start row, start column, end row, end column)
		
		//insert content rows
		for(PerformanceAppraisalWrapper wrapper : wrapperList) {
			excelDoc.insertRow(createContentRow(wrapper, NUMBER_OF_COLUMNS));
		}
		
		//Set Width size
		excelFile.getDocument().setColumnWidth(0, 13);
		excelFile.getDocument().setColumnWidth(1, 20);
		excelFile.getDocument().setColumnWidth(2, 30);
		excelFile.getDocument().setColumnWidth(3, 16);
		excelFile.getDocument().setColumnWidth(4, 16);
		excelFile.getDocument().setColumnWidth(5, 37);
		excelFile.getDocument().setColumnWidth(6, 14);
		excelFile.getDocument().setColumnWidth(7, 19);
		excelFile.getDocument().setColumnWidth(8, 30);
		excelFile.getDocument().setColumnWidth(9, 8);
	}
	
	private String[] createContentRow(PerformanceAppraisalWrapper wrapper, int sheetSize) {
		String[] contentRow = new String[sheetSize];
		
		if(wrapper == null) {
			throw new IllegalArgumentException("createContentRow was passed a null PerformanceAppraisalWrapper");
		}
		
		String performanceGroupDescription = "";
		if(wrapper.getPerformanceGroup() != null && performanceGroupMap.containsKey(wrapper.getPerformanceGroup().trim())) {
			performanceGroupDescription = performanceGroupMap.get(wrapper.getPerformanceGroup().trim());
		} else {
			logger.info("performance group description for code: " + wrapper.getPerformanceGroup().trim() + " not found in performanceGroupMap with keys: " + performanceGroupMap.keySet().toArray(new String[0]).toString());
		}
			
		contentRow[0] = wrapper.getJobNumber()				!= null ? wrapper.getJobNumber()					: "";
		contentRow[1] = wrapper.getSubcontractNumber() 		!= null ? wrapper.getSubcontractNumber().toString()	: "";
		contentRow[2] = wrapper.getSubcontractDescription() != null ? wrapper.getSubcontractDescription() 		: "";
		contentRow[3] = wrapper.getReviewNumber() 			!= null ? wrapper.getReviewNumber().toString() 		: "";
		contentRow[4] = wrapper.getVendorNumber() 			!= null ? wrapper.getVendorNumber().toString() 		: "";
		contentRow[5] = wrapper.getVendorName()				!= null ? wrapper.getVendorName()					: "";
		contentRow[6] = wrapper.getScore()					!= null ? wrapper.getScore().toString()				: "";
		contentRow[7] = wrapper.getPerformanceGroup()		!= null ? wrapper.getPerformanceGroup()				: "";
		contentRow[8] = performanceGroupDescription;
		contentRow[9] = wrapper.getStatus()					!= null ? wrapper.getStatus() 						: "";

		return contentRow;
	}
		
	private String[] getTitleRow(int sheetSize) {
		String[] titleRow = new String[sheetSize];
		
		titleRow[0] = "Job Number";
		titleRow[1] = "Subcontract Number";
		titleRow[2] = "Subcontract Description";
		titleRow[3] = "Review Number";
		titleRow[4] = "Vendor Number";
		titleRow[5] = "Vendor Name";
		titleRow[6] = "Performance";
		titleRow[7] = "Performance Group";
		titleRow[8] = "Performance Group Description";
		titleRow[9] = "Status";
		
		return titleRow;
	}
}
