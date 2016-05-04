/**
 * 
 */
package com.gammon.qs.service.retentionReleaseSchedule;

import java.util.List;

import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.util.DateUtil;
import com.gammon.qs.wrapper.RetentionReleaseScheduleEnquiryWindowsWrapper;

/**
 * 
 * @author peterchan 
 *
 */
public class RetentionReleaseScheduleExcelGenerator {
	
	private ExcelFile excelFile;
	
	private int numOfColumn = 22;
	
	private List<RetentionReleaseScheduleEnquiryWindowsWrapper> wrapperList;
	
	// constructor
	public RetentionReleaseScheduleExcelGenerator(List<RetentionReleaseScheduleEnquiryWindowsWrapper> wrapperList){
		this.wrapperList = wrapperList;
	}
	
	public ExcelFile generate(){
		this.excelFile = new ExcelFile();
		this.excelFile.setFileName("Retention Release Schedule" + ExcelFile.EXTENSION);
		
		// write title
		this.excelFile.getDocument().insertRow(this.setTitleRow());
		
		// write content
		for(int i = 0; i < wrapperList.size(); i++){
			this.setContent(i);
		}
		
		return this.excelFile;
	}
	
	// set the title row
	private String[] setTitleRow(){
		String[] titleRow = new String[numOfColumn];
		int index = 0;
		
		titleRow[index] = "Company";
		titleRow[index++] = "Company Name";
		titleRow[index++] = "Division";
		titleRow[index++] = "Job";
		titleRow[index++] = "Job Description";
		titleRow[index++] = "Currency";
		titleRow[index++] = "Solo/JV";
		titleRow[index++] = "Projected Contract Value";
		titleRow[index++] = "Max Retention %";
		titleRow[index++] = "Estimated Total Retention";
		titleRow[index++] = "Actual PCC Date";
		titleRow[index++] = "Anticipated Completion Date";
		titleRow[index++] = "DLP";
		titleRow[index++] = "Cumulative Retention Receivable";
		titleRow[index++] = "Retention Release Sequence";
		titleRow[index++] = "Receipt Amount";
		titleRow[index++] = "Receipt Date";
		titleRow[index++] = "Main Contract Certificate Number";
		titleRow[index++] = "Outstanding Retention Receivable";
		titleRow[index++] = "Anticipated Due Date";
		titleRow[index++] = "Contractual Due Date";
		titleRow[index++] = "Status";
		
		return titleRow;
	}
	
	// set one content row
	private void setContent(int i){
		String[] contentRow = new String[numOfColumn];
		int index = 0;
		contentRow[index] = this.wrapperList.get(i).getCompany() == null ? "" : this.wrapperList.get(i).getCompany();
		contentRow[index++] = this.wrapperList.get(i).getCompanyName() == null ? "" : this.wrapperList.get(i).getCompanyName();
		contentRow[index++] = this.wrapperList.get(i).getDivision() == null ? "" : this.wrapperList.get(i).getDivision();
		contentRow[index++] = this.wrapperList.get(i).getJobNo() == null ? "" : this.wrapperList.get(i).getJobNo();
		contentRow[index++] = this.wrapperList.get(i).getJobDesc() == null ? "" : this.wrapperList.get(i).getJobDesc();
		contentRow[index++] = this.wrapperList.get(i).getCurrency() == null ? "" : this.wrapperList.get(i).getCurrency();
		contentRow[index++] = this.wrapperList.get(i).getSoloJV() == null ? "" : this.wrapperList.get(i).getSoloJV();
		contentRow[index++] = this.wrapperList.get(i).getProjectedContractValue() == null ? "" : this.wrapperList.get(i).getProjectedContractValue().toString();
		contentRow[index++] = this.wrapperList.get(i).getMaxRetentionPercentage() == null ? "" : this.wrapperList.get(i).getMaxRetentionPercentage().toString();
		contentRow[index++] = this.wrapperList.get(i).getEstimatedRetention() == null ? "" : this.wrapperList.get(i).getEstimatedRetention().toString();
		contentRow[index++] = this.wrapperList.get(i).getActualPCCDate() == null ? "" :DateUtil.formatDate(this.wrapperList.get(i).getActualPCCDate(),"dd/MM/yyyy");
		contentRow[index++] = this.wrapperList.get(i).getCompletionDate() == null ? "" :DateUtil.formatDate(this.wrapperList.get(i).getCompletionDate(),"dd/MM/yyyy");
		contentRow[index++] = this.wrapperList.get(i).getDlp() == null ? "" : this.wrapperList.get(i).getDlp().toString();
		contentRow[index++] = this.wrapperList.get(i).getCumRetentionRec() == null ? "" : this.wrapperList.get(i).getCumRetentionRec().toString();
		contentRow[index++] = this.wrapperList.get(i).getRetentionReleaseSeq() == null ? "" : this.wrapperList.get(i).getRetentionReleaseSeq().toString();
		contentRow[index++] = this.wrapperList.get(i).getReceiptAmt() == null ? "" : this.wrapperList.get(i).getReceiptAmt().toString();
		contentRow[index++] = this.wrapperList.get(i).getReceiptDate() == null ? "" : DateUtil.formatDate(this.wrapperList.get(i).getReceiptDate(),"dd/MM/yyyy");
		contentRow[index++] = this.wrapperList.get(i).getMainCert() == null ? "" : this.wrapperList.get(i).getMainCert().toString();
		contentRow[index++] = this.wrapperList.get(i).getOutstandingAmt() == null ? "" : this.wrapperList.get(i).getOutstandingAmt().toString();
		contentRow[index++] = this.wrapperList.get(i).getDueDate() == null ? "" : DateUtil.formatDate(this.wrapperList.get(i).getDueDate(),"dd/MM/yyyy");
		contentRow[index++] = this.wrapperList.get(i).getContractualDueDate() == null ? "" : DateUtil.formatDate(this.wrapperList.get(i).getContractualDueDate(),"dd/MM/yyyy");
		contentRow[index++] = this.wrapperList.get(i).getStatus() == null ? "" :this.wrapperList.get(i).getStatus() ;
		
	
		for(int j = 0; j < numOfColumn; j++){
			this.excelFile.getDocument().setCellValue((i+1), j, contentRow[j]);
		}
		return ;
	}
	


}
