package com.gammon.qs.service.scPayment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gammon.qs.io.ExcelWorkbook;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.wrapper.paymentCertView.PaymentCertHeaderWrapper;

public class SCPaymentCertHeaderGenerator {
	
	private List<ExcelFile> excelFileList = new ArrayList<ExcelFile>();
	private List<PaymentCertHeaderWrapper> wrapperList;
	private ExcelFile excelFile;
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass().getName());
	
	public SCPaymentCertHeaderGenerator(List<PaymentCertHeaderWrapper> wrapperList) throws IOException
	{
		this.wrapperList=wrapperList;
	}
	public List<ExcelFile> generate() throws Exception{
		generateSheet();
		
		return excelFileList;
	}
	
	private void generateSheet() throws IOException{
		int sheetSize = 18;
		int insertColumn = 3;
		Double totalAmount = 0.00;
		Integer pageCount = 1;
		excelFile = new ExcelFile(new ExcelWorkbook(new XSSFWorkbook(this.getClass().getResourceAsStream("/resources/excelFiles/NewPaymentCertHeaderTemplate.xls"))));
		if(wrapperList!=null||wrapperList.size()>0){
			for(int i = 0; i<wrapperList.size();i++){
				PaymentCertHeaderWrapper wrapper = wrapperList.get(i);
				if(insertColumn == 42){
					excelFile.getDocument().setCellValue(0, 1, pageCount.toString());
					excelFileList.add(excelFile);
					excelFile = new ExcelFile(new ExcelWorkbook(new XSSFWorkbook(this.getClass().getResourceAsStream("/resources/excelFiles/NewPaymentCertHeaderTemplate.xls"))));
					pageCount = pageCount + 1;
					insertColumn =3;
				}
				totalAmount = totalAmount + wrapper.getOpenAmount();
				excelFile = insert(insertColumn, createContentRow(wrapper, sheetSize), excelFile);
				insertColumn = insertColumn + 1;
				if(i==wrapperList.size()-1){
					excelFile.getDocument().setCellValue(17, insertColumn + 2, "No. of Record:    " + wrapperList.size());
					//excelFileList.get(pageCount - 1).getDocument().setCellAlignment((short)3, insertColumn+2, 4);
					excelFile.getDocument().setCellValue(15,insertColumn+2, "Total Amount: ");
					excelFile.getDocument().setCellValue(13,insertColumn+2,totalAmount.toString());
					excelFile.getDocument().setCellValue(0, 1, pageCount.toString());
					excelFileList.add(excelFile);
				}
			}
		}else{
			excelFileList.add(excelFile);
		}
		//excelFileList.remove(excelFileList.size()-1);
	}
	
	private String[] createContentRow(PaymentCertHeaderWrapper wrapper, int sheetSize)
	{
		String[] contentRows = new String[sheetSize];
		/*contentRows[0]= wrapper.getCompany()!= null? wrapper.getCompany():"-";
		contentRows[1]= wrapper.getSupplierName()!= null? wrapper.getSupplierName():"-";
		contentRows[2]= wrapper.getSupplierNo()!= null? wrapper.getSupplierNo():"-";
		contentRows[3]= wrapper.getCompanyCurrency()!= null? wrapper.getCompanyCurrency():"-";
		contentRows[4]= wrapper.getOpenAmount()!= null? wrapper.getOpenAmount().toString():"-";
		contentRows[5]= wrapper.getPaymentCurrency()!= null? wrapper.getPaymentCurrency():"-";
		contentRows[6]= wrapper.getForeignOpen()!= null? wrapper.getForeignOpen().toString():"-";
		logger.info(wrapper.getInvoiceNo());
		contentRows[7]= wrapper.getInvoiceNo()!= null? wrapper.getInvoiceNo():"-";
		contentRows[8]= wrapper.getJobDescription()!= null? wrapper.getJobDescription():"-";
		contentRows[9]= wrapper.getNscdsc()!= null? wrapper.getNscdsc():"-";
		contentRows[10]= wrapper.getPaymentTerms()!= null? wrapper.getPaymentTerms():"-";
		contentRows[11]= wrapper.getPaymentType()!= null? wrapper.getPaymentType():"-";
		contentRows[12]= wrapper.getDueDate() !=null? DateUtil.formatDate(wrapper.getDueDate(), "dd/MM/yyyy").toString():"";
		contentRows[13]= wrapper.getAsAtDate()!=null? DateUtil.formatDate(wrapper.getAsAtDate(), "dd/MM/yyyy").toString():"";
		contentRows[14]= wrapper.getValueDateOnCert()!=null? DateUtil.formatDate(wrapper.getValueDateOnCert(), "dd/MM/yyyy").toString():"";
		contentRows[15]= wrapper.getCertNo()!= null? wrapper.getCertNo():"-";
		contentRows[16]= wrapper.getRecievedDate()!=null? DateUtil.formatDate(wrapper.getRecievedDate(), "dd/MM/yyyy").toString():"";
		contentRows[17]= wrapper.getMainCertAmt().toString();*/
		
		return contentRows;
	}
	private ExcelFile insert(int columnToInsert, String[] content, ExcelFile curExcelFile){
		int row = 17;
		for(int i = 0; i < 18; i ++){
			curExcelFile.getDocument().setCellValue(row, columnToInsert, content[i]);
			row = row - 1;
		}
		return curExcelFile;
	}
}
