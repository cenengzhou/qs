/**
 * koeyyeung
 * Aug 28, 201411:13:31 AM
 */
package com.gammon.qs.service.finance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gammon.pcms.helper.DateHelper;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.wrapper.sclist.SCListWrapper;

/**
 * @author koeyyeung
 *
 */
public class SubcontractorAnalysisReportGenerator {
	private ExcelFile excelFile;
	private List<SCListWrapper> subcontractList;
	private ArrayList<String[]> data = new ArrayList<String[]>();
	private Double totalOriginalSubcontractSum = new Double(0);
	private Double totalAddendum = new Double(0);
	private Double totalRevisedSubcontractSum = new Double(0);
	private Double totalLiabilities = new Double(0);
	private Double totalProvision = new Double(0);
	private Double totalBalanceToComplete = new Double(0);
	private Double totalPostedCertAmt = new Double(0);
	private Double totalCCPostedAmt = new Double(0);
	private Double totalRetentionBalanceAmt = new Double(0);
	private Double totalNetCertAmt = new Double(0);
	
	private int sheetSize = 22;
	
	public SubcontractorAnalysisReportGenerator(List<SCListWrapper> scListWrapper){
		this.subcontractList = scListWrapper;
	}
	
	public ExcelFile generate() throws Exception{

		excelFile = new ExcelFile();

		excelFile.setFileName("SubcontractorAnalysisReport" + DateHelper.formatDate(new Date(), "yyyyMMdd" )+ ExcelFile.EXTENSION);
		excelFile.setEmpty(false);
		excelFile.getDocument().setCurrentSheetName("Subcontractor Analysis Report");

		// Create title row
		excelFile.getDocument().insertRow(createTitleRow());
		
		if(subcontractList.size()>0)
			generateSubcontractSheet();
		else
			excelFile.setEmpty(true);	

		

		for (int i = 0 ; i < sheetSize; i++){
			excelFile.getDocument().setColumnWidth(i, 20);
		}
		
		// Create data for last row
		if(data.size()>0)
			excelFile.getDocument().insertRow(data.get(0));
		
		return excelFile;

	}

	private void generateSubcontractSheet() throws Exception {
		for(int i =0; i<subcontractList.size(); i++){
			SCListWrapper scListWrapper = subcontractList.get(i);

			int index=0;
			
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getCompany(), true);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getDivision(), true);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getVendorNo(), true);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getVendorName(), true);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getJobNumber(), true);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getSoloJV(), true);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getJvPercentage()+"", false);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getJobDescription(), true);
			excelFile.getDocument().setCellValue(i+1, index++,DateHelper.formatDate(scListWrapper.getActualPCCDate(), "dd-MM-yyyy"), false);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getCompletionStatus(), true);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getPackageNo(), true);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getCurrency(), true);
			
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getOriginalSubcontractSum()+"", false);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getAddendum()+"", false);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getSubcontractSum()+"", false);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getNetCertifiedAmount()+"", false);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getRetentionBalanceAmt()+"", false);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getTotalPostedCertAmt()+"", false);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getTotalProvision()+"", false);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getTotalLiabilities()+"", false);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getBalanceToComplete()+"", false);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getTotalCCPostedAmt()+"", false);
			
			
			totalAddendum += scListWrapper.getAddendum()!=null&&!scListWrapper.getAddendum().toString().trim().equals("")?scListWrapper.getAddendum():new Double(0);
			totalRevisedSubcontractSum += scListWrapper.getSubcontractSum()!=null&&!scListWrapper.getSubcontractSum().toString().trim().equals("")?scListWrapper.getSubcontractSum():new Double(0);
			totalLiabilities += scListWrapper.getTotalLiabilities()!=null&&!scListWrapper.getTotalLiabilities().toString().trim().equals("")?scListWrapper.getTotalLiabilities():new Double(0);
			totalProvision += scListWrapper.getTotalProvision()!=null&&!scListWrapper.getTotalProvision().toString().trim().equals("")?scListWrapper.getTotalProvision():new Double(0);
			totalBalanceToComplete += scListWrapper.getBalanceToComplete()!=null&&!scListWrapper.getBalanceToComplete().toString().trim().equals("")?scListWrapper.getBalanceToComplete():new Double(0);
			totalPostedCertAmt += scListWrapper.getTotalPostedCertAmt()!=null&&!scListWrapper.getTotalPostedCertAmt().toString().trim().equals("")?scListWrapper.getTotalPostedCertAmt():new Double(0);
			totalCCPostedAmt += scListWrapper.getTotalCCPostedAmt()!=null&&!scListWrapper.getTotalCCPostedAmt().toString().trim().equals("")?scListWrapper.getTotalCCPostedAmt():new Double(0);
			totalRetentionBalanceAmt += scListWrapper.getRetentionBalanceAmt() != null && !scListWrapper.getRetentionBalanceAmt().toString().trim().equals("") ? scListWrapper.getRetentionBalanceAmt() : new Double(0);
			totalNetCertAmt += scListWrapper.getNetCertifiedAmount() != null && !scListWrapper.getNetCertifiedAmount().toString().trim().equals("") ? scListWrapper.getNetCertifiedAmount() : new Double(0);
			totalOriginalSubcontractSum += scListWrapper.getOriginalSubcontractSum() != null && !scListWrapper.getOriginalSubcontractSum().toString().trim().equals("") ? scListWrapper.getOriginalSubcontractSum() : new Double(0);
			
		}
		//Create last row (total)
		data.add(createLastRow());

	}


	private String[] createLastRow(){
		int index = 0;
		String lastRow[] = new String[sheetSize];
		lastRow[index++] = "";
		lastRow[index++] = "";
		lastRow[index++] = "";
		lastRow[index++] = "";
		lastRow[index++] = "";
		lastRow[index++] = "";
		lastRow[index++] = "";
		lastRow[index++] = "";
		lastRow[index++] = "";
		lastRow[index++] = "";
		
		lastRow[index++] = "";
		lastRow[index++] = "Total:";
		lastRow[index++] = totalOriginalSubcontractSum+ "";
		lastRow[index++] = totalAddendum+ "";
		lastRow[index++] = totalRevisedSubcontractSum+"";
		lastRow[index++] = totalNetCertAmt+"";
		lastRow[index++] = totalRetentionBalanceAmt+"";
		lastRow[index++] = totalPostedCertAmt +"";		
		lastRow[index++] = totalProvision +"";
		lastRow[index++] = totalLiabilities+"";
		
		lastRow[index++] = totalBalanceToComplete +"";
		lastRow[index++] = totalCCPostedAmt+"";

		return lastRow;
	}
	
	private String[] createTitleRow(){
		int index = 0;
		String[] titleRow = new String[sheetSize];
		titleRow[index++] = "Company";
		titleRow[index++] = "Division";
		titleRow[index++] = "Subcontractor No";
		titleRow[index++] = "Subcontractor Name";
		titleRow[index++] = "Job No";
		titleRow[index++] = "Solo/JV";
		titleRow[index++] = "JV%";
		titleRow[index++] = "Job Description";
		titleRow[index++] = "Actual PCC Date";
		titleRow[index++] = "Completion Status";
		titleRow[index++] = "SC No";
		titleRow[index++] = "Currency";
		titleRow[index++] = "Original Subcontract Sum";
		titleRow[index++] = "Addendum";
		titleRow[index++] = "Revised Subcontract Sum";
		titleRow[index++] = "Net Cert Amount(excl.CC)";
		titleRow[index++] = "Retention Balance";
		titleRow[index++] = "Cert. Amount(Posted)";
		titleRow[index++] = "Provision";
		titleRow[index++] = "Work Done Amount(Cum.)";
		titleRow[index++] = "Balance to Complete";
		titleRow[index++] = "Contra. Charge";

		return titleRow;

	}
}
