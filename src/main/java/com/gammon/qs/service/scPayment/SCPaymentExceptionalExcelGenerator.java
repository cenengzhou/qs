/**
 * 
 */
package com.gammon.qs.service.scPayment;

import java.util.List;
import java.util.logging.Logger;

import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.util.DateUtil;
import com.gammon.qs.wrapper.directPayment.SCPaymentExceptionalWrapper;

/**
 * @author peterchan 
 * 
 */
public class SCPaymentExceptionalExcelGenerator {
	
	private static Logger logger = Logger.getLogger(SCPaymentExceptionalExcelGenerator.class.getName());
	private static final String[] TABLE_HEADER = {
		"Division",
		"Company",
		"Job Number",
		"Job Description",
		"Package Description",
		"Package No",
		"Subcontract Status",
		"Subcontractor Name",
		"Subcontractor Number",
		"SC Payment Number",
		"Payment Amount",
		"As At Date",
		"Cert. Issue Date",
		"Due Date",
		"Created User"
	};
	private static final int[] COLUMN_WIDTH = {
		10,
		10,
		13,
		40,
		40,
		12,
		10,
		40,
		20,
		8,
		25,
		25,
		25,
		25,
		30
	};
	private static final boolean[] STRING_COLUMN = {
		true,//"Division"
		true,//"Company"
		true,//"Job Number"
		true,//"Job Description"
		true,//"Package Description"
		true,//"Package No"
		true,//"Subcontract Status"
		true,//"Subcontractor Name"
		true,//"Subcontractor Number"
		false,//"SC Payment Number"
		false,//"Payment Amount"
		false,//"As At Date"
		false,//"Cert. Issue Date"
		false,//"Due Date"
		true//"Created User"
	};		
	public static ExcelFile generate(List<SCPaymentExceptionalWrapper> scPaymentExceptionalReportList){
		
		logger.info("Create and generate Subcontractor Payment Exceptional Report");
		
		ExcelFile file = new ExcelFile();
		file.setFileName("SubcontractorPaymentExceptionalReport"+ExcelFile.EXTENSION);
		file.getDocument().setCurrentSheetName("Report");

		int y = 0; // row
//		int x = 0; // column

		for(int i = 0; i < TABLE_HEADER.length; i++){
			file.getDocument().setCellValue(y, (i), TABLE_HEADER[i]);
			file.getDocument().setColumnWidth(i, COLUMN_WIDTH[i]);
		}
		if (scPaymentExceptionalReportList!=null && scPaymentExceptionalReportList.size()>0){
			for (SCPaymentExceptionalWrapper wrapper:scPaymentExceptionalReportList){
				String[] rowRecord = {
					wrapper.getDivision(),
					wrapper.getCompany(),
					wrapper.getJobNumber(),
					wrapper.getJobDescription(),
					wrapper.getPackageDescription(),
					wrapper.getPackageNo(),
					wrapper.getScStatus()!=null?wrapper.getScStatus().toString():"",
					wrapper.getSubcontractorName(),
					wrapper.getSubcontractorNumber(),
					wrapper.getPaymentNo().toString(),
					wrapper.getPaymentAmount()!=null?wrapper.getPaymentAmount().toString():"0.00",
					wrapper.getAsAtDate()!=null?DateUtil.formatDate(wrapper.getAsAtDate()):"",
					wrapper.getCertIssueDate()!=null?DateUtil.formatDate(wrapper.getCertIssueDate()):"",
					wrapper.getDueDate()!=null?DateUtil.formatDate(wrapper.getDueDate()):"",
					wrapper.getCreateUser()
				};
				y++;
				for(int i = 0; i < rowRecord.length; i++){
//					if (STRING_COLUMN[i]){
//						file.getDocument().setCellTypeToString(y, i);
//					}
					file.getDocument().setCellValue(y, (i), rowRecord[i],STRING_COLUMN[i]);
				}				
			}
			logger.info(y+" records exported for Subcontractor Payment Exceptional Report");
		}else{
			String[] rowRecord = {
					"",
					"",
					"",
					"No record found",
					"",
					"",
					"",
					"",
					"",
					"",
					"",
					"",
					"",
					"",
					""
				};
			y++;
			for(int i = 0; i < rowRecord.length; i++){
				file.getDocument().setCellValue(y, (i), rowRecord[i]);
			}
			logger.info("No Record found for Subcontractor Payment Exceptional Report");
		}
		return file;
	}
}
