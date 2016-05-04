package com.gammon.qs.service.scPayment;

import java.util.Date;
import java.util.List;

import com.gammon.qs.domain.SCPaymentDetail;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.util.DateUtil;

public class SCPaymentReportGenerator {
	
	private ExcelFile excelFile;
	private List<SCPaymentDetail> scPaymentDetailList;
	private String jobNumber;
	private String packageNo;
	private String paymentCertNo;
	
	public SCPaymentReportGenerator(List<SCPaymentDetail> scPaymentDetailList, String jobNumber, String packageNo,String paymentCertNo)
	{
		this.scPaymentDetailList = scPaymentDetailList;
		this.jobNumber = jobNumber;
		this.packageNo = packageNo;
		this.paymentCertNo = paymentCertNo;
	}
	
	public ExcelFile generate() throws Exception{
		
		excelFile = new ExcelFile();
		excelFile.setFileName("Subcontract Payment Details" + DateUtil.formatDate(new Date(), "yyyyMMddHHmmss")+ ExcelFile.EXTENSION);
		
		if(scPaymentDetailList.size()> 0 )
		{
			excelFile.setEmpty(false);
			excelFile.getDocument().setCurrentSheetName("job("+this.jobNumber+")-package("+packageNo+")-paymentCertNo("+paymentCertNo+")");
			
			generateSheet();
		}
		else{
			excelFile.setEmpty(true);
		}
		
		return excelFile;
	}
	
	
	private void generateSheet(){
		int sheetSize = 8;
		
		excelFile.getDocument().insertRow(createTitleRow(sheetSize));
		
		for(SCPaymentDetail scPaymentDetail: scPaymentDetailList)
		{
			excelFile.getDocument().insertRow(createContentRow(scPaymentDetail, sheetSize));
		}
		
		
		excelFile.getDocument().setColumnWidth(0, 13);
		excelFile.getDocument().setColumnWidth(1, 13);
		excelFile.getDocument().setColumnWidth(2, 14);
		excelFile.getDocument().setColumnWidth(3, 20);
		excelFile.getDocument().setColumnWidth(4, 20);
		excelFile.getDocument().setColumnWidth(5, 40);
		excelFile.getDocument().setColumnWidth(6, 20);
		excelFile.getDocument().setColumnWidth(7, 20);
	}
	
	private String[] createContentRow(SCPaymentDetail scPaymentDetail, int sheetSize)
	{
		String[] contentRows = new String[sheetSize];
		
		contentRows[0] = scPaymentDetail.getLineType() !=null ? scPaymentDetail.getLineType().toString(): "";
		contentRows[1] = scPaymentDetail.getBillItem() != null ? scPaymentDetail.getBillItem().trim(): "";
		contentRows[2] = scPaymentDetail.getMovementAmount()!=null ? scPaymentDetail.getMovementAmount().toString(): "0";
		contentRows[3] = scPaymentDetail.getCumAmount()!=null ? scPaymentDetail.getCumAmount().toString(): "0";
		
		Double postedAmt;
		if(scPaymentDetail.getCumAmount()!=null && scPaymentDetail.getMovementAmount()!=null ){
			postedAmt = scPaymentDetail.getCumAmount() - scPaymentDetail.getMovementAmount();
		}else
			postedAmt = 0.00;
		contentRows[4] = postedAmt.toString();
		contentRows[5] = scPaymentDetail.getScDetail() != null ? (scPaymentDetail.getScDetail().getDescription() != null ? scPaymentDetail.getScDetail().getDescription() : "") : "";
		contentRows[6] = scPaymentDetail.getObjectCode() != null ? scPaymentDetail.getObjectCode().toString() : "";
		contentRows[7] = scPaymentDetail.getSubsidiaryCode() != null ? scPaymentDetail.getSubsidiaryCode().toString() : "";

		return contentRows;
	}
	
	
	private String[] createTitleRow(int sheetSize){
		String[] titleRows = new String[sheetSize];
		
		titleRows[0] = "Line Type";
		titleRows[1] = "Bill Item";
		titleRows[2] = "Movement Amount";
		titleRows[3] = "Cumulative Certified Amount";
		titleRows[4] = "Posted Certified Amount";
		titleRows[5] = "Description";
		titleRows[6] = "Object Code";
		titleRows[7] = "Subsidiary Code";
		
		return titleRows;
	}

}
