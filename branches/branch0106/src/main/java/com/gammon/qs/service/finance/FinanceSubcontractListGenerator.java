package com.gammon.qs.service.finance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gammon.pcms.helper.DateHelper;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.wrapper.sclist.SCListWrapper;

public class FinanceSubcontractListGenerator {
	private ExcelFile excelFile;
	private List<SCListWrapper> subcontractList;
	private ArrayList<String[]> data = new ArrayList<String[]>();
	
	private Double totalOriginalSubcontractSum = new Double(0);
	private Double totalRemeasuredSubcontractSum = new Double(0);
	private Double totalAddendum = new Double(0);
	private Double totalRevisedSubcontractSum = new Double(0);
	private Double totalAccumlatedRetentionAmt = new Double(0);
	private Double totalRetentionReleasedAmt = new Double(0);
	private Double totalRetentionBalanceAmt = new Double(0);
	private Double totalNetCertAmt = new Double(0);
	private Double totalPostedCertAmt = new Double(0);
	private Double totalCumCertAmt = new Double(0);
	private Double totalLiabilities = new Double(0);
	private Double totalProvision = new Double(0);
	private Double totalBalanceToComplete = new Double(0);
	private Double totalCCPostedAmt = new Double(0);
	private Double totalMOSPostedAmt = new Double(0);
	
	
	private Boolean includeJobCompletionDate;
	
	private int sheetSize = 43;
	
	public FinanceSubcontractListGenerator(List<SCListWrapper> scListWrapper, Boolean includeJobCompletionDate){
		this.subcontractList = scListWrapper;
		this.includeJobCompletionDate=includeJobCompletionDate;
		
		if(includeJobCompletionDate)
			sheetSize=44;
	}
	
	public ExcelFile generate() throws Exception{

		excelFile = new ExcelFile();

		excelFile.setFileName("Subcontract Enquiry " + DateHelper.formatDate(new Date(), "yyyyMMdd" )+ ExcelFile.EXTENSION);
		excelFile.setEmpty(false);
		excelFile.getDocument().setCurrentSheetName("Subcontract Enquiry");

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
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getJobNumber(), true);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getJobDescription(), true);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getSoloJV(), true);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getJvPercentage()+"", false);
			if(includeJobCompletionDate)
				excelFile.getDocument().setCellValue(i+1, index++,DateHelper.formatDate(scListWrapper.getJobAnticipatedCompletionDate(), "dd-MM-yyyy"), true);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getClientNo(), true);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getPackageNo(), true);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getVendorNo(), true);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getVendorName(), true);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getDescription(), true);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getSubcontractorNature(), true);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getCurrency(), true);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getWorkScope(), true);
			
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getOriginalSubcontractSum()+"", false);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getRemeasuredSubcontractSum()+"", false);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getAddendum()+"", false);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getSubcontractSum()+"", false);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getAccumlatedRetentionAmt()+"", false);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getRetentionReleasedAmt()+"", false);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getRetentionBalanceAmt()+"", false);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getNetCertifiedAmount()+"", false);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getTotalPostedCertAmt()+"", false);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getTotalCumCertAmt()+"", false);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getTotalLiabilities()+"", false);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getTotalProvision()+"", false);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getBalanceToComplete()+"", false);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getTotalCCPostedAmt()+"", false);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getTotalMOSPostedAmt()+"", false);

			
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getSplitTerminateStatus(), true);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getPaymentStatus(), true);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getPaymentTerms(), true);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getSubcontractTerm(), true);
			
			excelFile.getDocument().setCellValue(i+1, index++,DateHelper.formatDate(scListWrapper.getActualPCCDate(), "dd-MM-yyyy"), false);
			excelFile.getDocument().setCellValue(i+1, index++,DateHelper.formatDate(scListWrapper.getRequisitionApprovedDate(), "dd-MM-yyyy"), false);
			excelFile.getDocument().setCellValue(i+1, index++,DateHelper.formatDate(scListWrapper.getTenderAnalysisApprovedDate(), "dd-MM-yyyy"), false);
			excelFile.getDocument().setCellValue(i+1, index++,DateHelper.formatDate(scListWrapper.getPreAwardMeetingDate(), "dd-MM-yyyy"), false);
			excelFile.getDocument().setCellValue(i+1, index++,DateHelper.formatDate(scListWrapper.getLoaSignedDate(), "dd-MM-yyyy"), false);
			excelFile.getDocument().setCellValue(i+1, index++,DateHelper.formatDate(scListWrapper.getScDocScrDate(), "dd-MM-yyyy"), false);
			excelFile.getDocument().setCellValue(i+1, index++,DateHelper.formatDate(scListWrapper.getScDocLegalDate(), "dd-MM-yyyy"), false);
			excelFile.getDocument().setCellValue(i+1, index++,DateHelper.formatDate(scListWrapper.getWorkCommenceDate(), "dd-MM-yyyy"), false);
			excelFile.getDocument().setCellValue(i+1, index++,DateHelper.formatDate(scListWrapper.getOnSiteStartDate(), "dd-MM-yyyy"), false);
			excelFile.getDocument().setCellValue(i+1, index++,scListWrapper.getCompletionStatus(), true);
			
			
			totalRemeasuredSubcontractSum += scListWrapper.getRemeasuredSubcontractSum()!=null&&!scListWrapper.getRemeasuredSubcontractSum().toString().trim().equals("")?scListWrapper.getRemeasuredSubcontractSum():new Double(0);
			totalAddendum += scListWrapper.getAddendum()!=null&&!scListWrapper.getAddendum().toString().trim().equals("")?scListWrapper.getAddendum():new Double(0);
			totalRevisedSubcontractSum += scListWrapper.getSubcontractSum()!=null&&!scListWrapper.getSubcontractSum().toString().trim().equals("")?scListWrapper.getSubcontractSum():new Double(0);
			totalLiabilities += scListWrapper.getTotalLiabilities()!=null&&!scListWrapper.getTotalLiabilities().toString().trim().equals("")?scListWrapper.getTotalLiabilities():new Double(0);
			totalProvision += scListWrapper.getTotalProvision()!=null&&!scListWrapper.getTotalProvision().toString().trim().equals("")?scListWrapper.getTotalProvision():new Double(0);
			totalBalanceToComplete += scListWrapper.getBalanceToComplete()!=null&&!scListWrapper.getBalanceToComplete().toString().trim().equals("")?scListWrapper.getBalanceToComplete():new Double(0);
			totalPostedCertAmt += scListWrapper.getTotalPostedCertAmt()!=null&&!scListWrapper.getTotalPostedCertAmt().toString().trim().equals("")?scListWrapper.getTotalPostedCertAmt():new Double(0);
			totalCumCertAmt += scListWrapper.getTotalCumCertAmt()!=null&&!scListWrapper.getTotalCumCertAmt().toString().trim().equals("")?scListWrapper.getTotalCumCertAmt():new Double(0);
			totalCCPostedAmt += scListWrapper.getTotalCCPostedAmt()!=null&&!scListWrapper.getTotalCCPostedAmt().toString().trim().equals("")?scListWrapper.getTotalCCPostedAmt():new Double(0);
			totalMOSPostedAmt += scListWrapper.getTotalMOSPostedAmt()!=null&&!scListWrapper.getTotalMOSPostedAmt().toString().trim().equals("")?scListWrapper.getTotalMOSPostedAmt():new Double(0);
			totalAccumlatedRetentionAmt += scListWrapper.getAccumlatedRetentionAmt() != null && !scListWrapper.getAccumlatedRetentionAmt().toString().trim().equals("") ? scListWrapper.getAccumlatedRetentionAmt() : new Double(0);
			totalRetentionReleasedAmt += scListWrapper.getRetentionReleasedAmt() != null && !scListWrapper.getRetentionReleasedAmt().toString().trim().equals("") ? scListWrapper.getRetentionReleasedAmt() : new Double(0);
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
		if(includeJobCompletionDate)
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
		lastRow[index++] = totalRemeasuredSubcontractSum+ "";
		lastRow[index++] = totalAddendum+ "";
		lastRow[index++] = totalRevisedSubcontractSum+"";
		lastRow[index++] = totalAccumlatedRetentionAmt+"";
		lastRow[index++] = totalRetentionReleasedAmt+"";
		lastRow[index++] = totalRetentionBalanceAmt+"";
		lastRow[index++] = totalNetCertAmt+"";
		lastRow[index++] = totalPostedCertAmt +"";
		lastRow[index++] = totalCumCertAmt +"";
		lastRow[index++] = totalLiabilities+"";
		lastRow[index++] = totalProvision +"";
		lastRow[index++] = totalBalanceToComplete +"";
		lastRow[index++] = totalCCPostedAmt+"";
		lastRow[index++] = totalMOSPostedAmt +"";

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
		lastRow[index++] = "";
		lastRow[index++] = "";
		lastRow[index++] = "";
		
		return lastRow;
	}
	
	private String[] createTitleRow(){
		int index = 0;
		String[] titleRow = new String[sheetSize];
		
		titleRow[index++] = "Company";
		titleRow[index++] = "Division";
		titleRow[index++] = "Job No";
		titleRow[index++] = "Job Description";
		titleRow[index++] = "Solo/JV";
		titleRow[index++] = "JV%";
		if(includeJobCompletionDate)
			titleRow[index++] = "Job Completion Date";
		titleRow[index++] = "Client No";
		titleRow[index++] = "SC No";
		titleRow[index++] = "Subcontractor No";
		titleRow[index++] = "Subcontractor Name";
		titleRow[index++] = "Description";
		titleRow[index++] = "Subcontractor Nature";
		titleRow[index++] = "Currency";
		titleRow[index++] = "Work Scope (Trade)";

		titleRow[index++] = "Original Subcontract Sum";
		titleRow[index++] = "Remeasured Subcontract Sum";
		titleRow[index++] = "Addendum";
		titleRow[index++] = "Revised Subcontract Sum";
		titleRow[index++] = "Accumlated Rentention(RT)";
		titleRow[index++] = "Retention Released(RR)";
		titleRow[index++] = "Retention Balance";
		titleRow[index++] = "Net Cert. Amount(excl.CC)";
		titleRow[index++] = "Cert. Amount(Posted)";
		titleRow[index++] = "Cert. Amount(Cum.)";
		titleRow[index++] = "Work Done Amount(Cum.)";
		titleRow[index++] = "Provision";
		titleRow[index++] = "Balance to Complete";
		titleRow[index++] = "Contra. Charge";
		titleRow[index++] = "Material On Site";
		
		titleRow[index++] = "Split Terminate Status";
		titleRow[index++] = "Payment Type";
		titleRow[index++] = "Payment Terms";
		titleRow[index++] = "SC Term";
		
		titleRow[index++] = "Actual PCC Date";
		titleRow[index++] = "SC Requisition Approved Date";
		titleRow[index++] = "SC Tender Analysis Approved Date";
		titleRow[index++] = "Pre-Award Finalization Meeting Date";
		titleRow[index++] = "LOA Signed Date";
		titleRow[index++] = "SC Doc executed by Subcontractor Date";
		titleRow[index++] = "SC Doc executed by Legal Date";
		titleRow[index++] = "Work Commence Date";
		titleRow[index++] = "On Site Start Date";
		titleRow[index++] = "Completion Status";
		
		return titleRow;

	}
}
