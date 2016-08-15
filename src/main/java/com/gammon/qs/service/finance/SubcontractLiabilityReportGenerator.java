package com.gammon.qs.service.finance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gammon.pcms.helper.DateHelper;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.wrapper.sclist.SCListWrapper;

/**
 * koeyyeung
 * Aug 27, 2014 11:54:16 AM
 */
public class SubcontractLiabilityReportGenerator {
	private ExcelFile excelFile;
	private List<SCListWrapper> subcontractList;
	private ArrayList<String[]> data = new ArrayList<String[]>();

	private Double totalAddendum = new Double(0);
	private Double totalRevisedSubcontractSum = new Double(0);
	private Double totalLiabilities = new Double(0);
	private Double totalProvision = new Double(0);
	private Double totalBalanceToComplete = new Double(0);
	private Double totalPostedCertAmt = new Double(0);
	private Double totalCCPostedAmt = new Double(0);
	private Double totalMOSPostedAmt = new Double(0);
	private Double totalRetentionBalanceAmt = new Double(0);
	
	private int sheetSize = 16;
	
	public SubcontractLiabilityReportGenerator(List<SCListWrapper> scListWrapper){
		this.subcontractList = scListWrapper;
	}
	
	public ExcelFile generate() throws Exception{

		excelFile = new ExcelFile();

		excelFile.setFileName("Subcontract Liability Report " + DateHelper.formatDate(new Date(), "yyyy-MM-dd" )+ ExcelFile.EXTENSION);
		excelFile.setEmpty(false);
		excelFile.getDocument().setCurrentSheetName("Subcontract Liability Report");

		if(subcontractList.size()>0)
			generateSubcontractLiabilitySheet();
		else
			excelFile.setEmpty(true);	

		// Create title row
		excelFile.getDocument().insertRow(createTitleRow());

		for (int i = 0 ; i < sheetSize; i++){
			excelFile.getDocument().setColumnWidth(i, 15);
		}
		// Create data
		for(int i = 0; i<data.size(); i++)
			excelFile.getDocument().insertRow(data.get(i));
		return excelFile;

	}

	private void generateSubcontractLiabilitySheet() throws Exception {

		for(SCListWrapper scListWrapper: subcontractList){
			String[] contentRow = createContentRow(scListWrapper);
			if (contentRow.length>0){
				if (contentRow[0]!=null && !contentRow[0].equals("")){
					data.add(contentRow);
				}
			}
		}
		//Create last row (total)
		data.add(createLastRow());

	}

	private String[] createContentRow(SCListWrapper scListWrapper) throws DatabaseOperationException{
		int index = 0;
		String[] contentRows = new String[sheetSize];
		contentRows[index++] = scListWrapper.getJobNumber();
		contentRows[index++] = scListWrapper.getJobDescription();
		
		contentRows[index++] = scListWrapper.getPackageNo();
		contentRows[index++] = scListWrapper.getVendorNo();
		contentRows[index++] = scListWrapper.getVendorName();
		contentRows[index++] = scListWrapper.getDescription();
		
		contentRows[index++] = scListWrapper.getAddendum()+"";
		contentRows[index++] = scListWrapper.getSubcontractSum()+"";
		contentRows[index++] = scListWrapper.getRetentionBalanceAmt()+"";
		
		contentRows[index++] = scListWrapper.getPaymentStatus();
		
		contentRows[index++] = scListWrapper.getTotalLiabilities()+"";
		contentRows[index++] = scListWrapper.getTotalPostedCertAmt()+"";
		contentRows[index++] = scListWrapper.getTotalProvision()+"";
		contentRows[index++] = scListWrapper.getBalanceToComplete()+"";
		contentRows[index++] = scListWrapper.getTotalCCPostedAmt()+"";
		contentRows[index++] = scListWrapper.getTotalMOSPostedAmt()+"";
		
		
		if(scListWrapper.getAddendum()!=null)
			totalAddendum += scListWrapper.getAddendum();
		if(scListWrapper.getSubcontractSum()!=null)
			totalRevisedSubcontractSum += scListWrapper.getSubcontractSum();
		
		totalLiabilities += scListWrapper.getTotalLiabilities()!=null&&!scListWrapper.getTotalLiabilities().toString().trim().equals("")?scListWrapper.getTotalLiabilities():new Double(0);
		totalProvision += scListWrapper.getTotalProvision()!=null&&!scListWrapper.getTotalProvision().toString().trim().equals("")?scListWrapper.getTotalProvision():new Double(0);
		totalBalanceToComplete += scListWrapper.getBalanceToComplete()!=null&&!scListWrapper.getBalanceToComplete().toString().trim().equals("")?scListWrapper.getBalanceToComplete():new Double(0);
		totalPostedCertAmt += scListWrapper.getTotalPostedCertAmt()!=null&&!scListWrapper.getTotalPostedCertAmt().toString().trim().equals("")?scListWrapper.getTotalPostedCertAmt():new Double(0);
		totalCCPostedAmt += scListWrapper.getTotalCCPostedAmt()!=null&&!scListWrapper.getTotalCCPostedAmt().toString().trim().equals("")?scListWrapper.getTotalCCPostedAmt():new Double(0);
		totalMOSPostedAmt += scListWrapper.getTotalMOSPostedAmt()!=null&&!scListWrapper.getTotalMOSPostedAmt().toString().trim().equals("")?scListWrapper.getTotalMOSPostedAmt():new Double(0);
		totalRetentionBalanceAmt += scListWrapper.getRetentionBalanceAmt() != null && !scListWrapper.getRetentionBalanceAmt().toString().trim().equals("") ? scListWrapper.getRetentionBalanceAmt() : new Double(0);
		
		return contentRows;

	}

	private String[] createLastRow(){
		int index = 0;
		String lastRow[] = new String[sheetSize];
		lastRow[index++] = "";
		lastRow[index++] = "";
		lastRow[index++] = "";
		lastRow[index++] = "";
		lastRow[index++] = "";
		lastRow[index++] = "Total:";
		
		lastRow[index++] = totalAddendum+ "";
		lastRow[index++] = totalRevisedSubcontractSum+"";
		lastRow[index++] = totalRetentionBalanceAmt+"";
		lastRow[index++] = "";
		
		lastRow[index++] = totalLiabilities+"";
		lastRow[index++] = totalPostedCertAmt +"";
		lastRow[index++] = totalProvision +"";
		lastRow[index++] = totalBalanceToComplete +"";
		lastRow[index++] = totalCCPostedAmt+"";
		lastRow[index++] = totalMOSPostedAmt +"";
		
		return lastRow;
	}
	private String[] createTitleRow(){
		int index = 0;
		String[] titleRow = new String[sheetSize];
		titleRow[index++] = "Job No";
		titleRow[index++] = "Job Description";
		titleRow[index++] = "SC No";
		titleRow[index++] = "Subcontractor No";
		titleRow[index++] = "Subcontractor Name";
		titleRow[index++] = "Description";
		
		titleRow[index++] = "Addendum";
		titleRow[index++] = "Revised Subcontract Sum";
		titleRow[index++] = "Retention Balance";
		
		titleRow[index++] = "Payment Type";
		titleRow[index++] = "Work Done Amount(Cum.)";
		titleRow[index++] = "Cert. Amount(Posted)";
		titleRow[index++] = "Provision";
		titleRow[index++] = "Balance to Complete";
		titleRow[index++] = "Contra. Charge";
		titleRow[index++] = "Material On Site";

		return titleRow;
	}
	
	
}
