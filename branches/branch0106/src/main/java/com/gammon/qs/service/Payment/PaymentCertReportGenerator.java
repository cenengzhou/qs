package com.gammon.qs.service.Payment;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gammon.qs.io.ExcelWorkbook;
import com.gammon.pcms.helper.DateHelper;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.wrapper.paymentCertView.PaymentCertViewWrapper;

public class PaymentCertReportGenerator {
	
	private ExcelFile excelFile;
	private PaymentCertViewWrapper wrapper;
	
	public PaymentCertReportGenerator(PaymentCertViewWrapper wrapper, InputStream template) throws IOException
	{
		XSSFWorkbook wb = new XSSFWorkbook(template);
		this.excelFile = new ExcelFile(new ExcelWorkbook(wb));
		//this.excelFile.setBytes(template);
		this.wrapper=wrapper;
	}
	public ExcelFile generate() throws Exception{
		excelFile.setFileName("Subcontract Payment Cert" + DateHelper.formatDate(new Date(), "yyyyMMddHHmmss")+ ExcelFile.EXTENSION);
		
		excelFile.getDocument().setCurrentSheetName("job("+this.wrapper.getJobNumber()+")-package("+wrapper.getSubContractNo()+")-paymentCertNo("+wrapper.getPaymentCertNo()+")");
		createTemplate();
		
		return excelFile;
	}
	
	private void createTemplate(){
		if(wrapper.getCompanyName()!=null)
			excelFile.getDocument().setCellValue(0, 1, wrapper.getCompanyName().trim());
		excelFile.getDocument().setCellValue(1, 1, "Subcontractor Payment Certificate");
		
		excelFile.getDocument().setCellValue(2,2,wrapper.getJobNumber());
		excelFile.getDocument().setCellValue(2,3,wrapper.getJobDescription());
		
		excelFile.getDocument().setCellValue(3,2,wrapper.getSubContractNo().toString());
		excelFile.getDocument().setCellValue(3,3,wrapper.getSubContractDescription());
	
		excelFile.getDocument().setCellValue(5,2,wrapper.getSubcontractorNo().toString());
		excelFile.getDocument().setCellValue(5,3,wrapper.getSubcontractorDescription());
		
		excelFile.getDocument().setCellValue(7,1,wrapper.getAddress1());
		excelFile.getDocument().setCellValue(8,1,wrapper.getAddress2());
		excelFile.getDocument().setCellValue(9,1,wrapper.getAddress3());
		excelFile.getDocument().setCellValue(10,1,wrapper.getAddress4());
		
		excelFile.getDocument().setCellValue(7,7,wrapper.getPaymentCertNo().toString());
		
		excelFile.getDocument().setCellValue(8,6,"AS AT DATE:           "+wrapper.getAsAtDate());
		
		excelFile.getDocument().setCellValue(9,7,wrapper.getPaymentType());
		
		excelFile.getDocument().setCellValue(10,7,wrapper.getCurrency());
		
		excelFile.getDocument().setCellValue(11,6,wrapper.getSubcontractorNature());
		
		excelFile.getDocument().setCellValue(12,1,wrapper.getPhone());
		
		excelFile.getDocument().setCellValue(14,2,wrapper.getSubcontractSum().toString());

		//
		// Hidden the VO amount in PDF (Changed by Peter Chan, requested by Vincent Mok @ 17 June 2010)
		//excelFile.getDocument().setCellValue(15,2,wrapper.getAddendum().toString());
		excelFile.getDocument().setCellValue(15,2,"0");

		//
		// Set Revised Value be SC Sum in PDF (Changed by Peter Chan, requested by Vincent Mok @ 17 June 2010)
		//excelFile.getDocument().setCellValue(16,2,wrapper.getRevisedValue().toString());
		excelFile.getDocument().setCellValue(16,2,wrapper.getSubcontractSum().toString());
		
		excelFile.getDocument().setCellValue(22,5,"MOVEMENT");
		excelFile.getDocument().setCellValue(22,7,"TOTAL");
		
		excelFile.getDocument().setCellValue(20,5,wrapper.getMeasuredWorksMovement().toString());
		excelFile.getDocument().setCellValue(20,7,wrapper.getMeasuredWorksTotal().toString());
		
		excelFile.getDocument().setCellValue(21,5,wrapper.getDayWorkSheetMovement().toString());
		excelFile.getDocument().setCellValue(21,7,wrapper.getDayWorkSheetTotal().toString());
		
		excelFile.getDocument().setCellValue(22,5,wrapper.getVariationMovement().toString());
		excelFile.getDocument().setCellValue(22,7,wrapper.getVariationTotal().toString());
		
		excelFile.getDocument().setCellValue(23,5,wrapper.getOtherMovement().toString());
		excelFile.getDocument().setCellValue(23,7,wrapper.getOtherTotal().toString());
	
		excelFile.getDocument().setCellValue(24,5,wrapper.getSubMovement1().toString());
		excelFile.getDocument().setCellValue(24,7,wrapper.getSubTotal1().toString());
		
		excelFile.getDocument().setCellValue(25,5,wrapper.getLessRetentionMovement1().toString());
		excelFile.getDocument().setCellValue(25,7,wrapper.getLessRetentionTotal1().toString());
		
		excelFile.getDocument().setCellValue(26,5,wrapper.getSubMovement2().toString());
		excelFile.getDocument().setCellValue(26,7,wrapper.getSubTotal2().toString());
		
		excelFile.getDocument().setCellValue(27,5,wrapper.getMaterialOnSiteMovement().toString());
		excelFile.getDocument().setCellValue(27,7,wrapper.getMaterialOnSiteTotal().toString());
		
		excelFile.getDocument().setCellValue(28,5,wrapper.getLessRetentionMovement2().toString());
		excelFile.getDocument().setCellValue(28,7,wrapper.getLessRetentionTotal2().toString());
		
		excelFile.getDocument().setCellValue(29,5,wrapper.getSubMovement3().toString());
		excelFile.getDocument().setCellValue(29,7,wrapper.getSubTotal3().toString());
		
		excelFile.getDocument().setCellValue(30,5,wrapper.getAdjustmentMovement().toString());
		excelFile.getDocument().setCellValue(30,7,wrapper.getAdjustmentTotal().toString());
		
		excelFile.getDocument().setCellValue(31,5,wrapper.getGstPayableMovement().toString());
		excelFile.getDocument().setCellValue(31,7,wrapper.getGstPayableTotal().toString());
	
		excelFile.getDocument().setCellValue(32,5,wrapper.getSubMovement4().toString());
		excelFile.getDocument().setCellValue(32,7,wrapper.getSubTotal4().toString());
		
		excelFile.getDocument().setCellValue(33,5,wrapper.getLessContraChargesMovement().toString());
		excelFile.getDocument().setCellValue(33,7,wrapper.getLessContraChargesTotal().toString());
		
		excelFile.getDocument().setCellValue(34,5,wrapper.getLessGSTReceivableMovement().toString());
		excelFile.getDocument().setCellValue(34,7,wrapper.getLessGSTReceivableTotal().toString());
		
		excelFile.getDocument().setCellValue(35,5,wrapper.getSubMovement5().toString());
		excelFile.getDocument().setCellValue(35,7,wrapper.getSubTotal5().toString());
		
		excelFile.getDocument().setCellValue(36,7,wrapper.getLessPreviousCertificationsMovement().toString());
		
		excelFile.getDocument().setCellValue(38,7,wrapper.getAmountDueMovement().toString());
		
	}
}
