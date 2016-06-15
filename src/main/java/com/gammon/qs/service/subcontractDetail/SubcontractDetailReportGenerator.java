package com.gammon.qs.service.subcontractDetail;

import java.util.Date;
import java.util.List;

import com.gammon.qs.domain.SubcontractDetail;
import com.gammon.qs.domain.SubcontractDetailBQ;
import com.gammon.qs.domain.SubcontractDetailCC;
import com.gammon.qs.domain.SubcontractDetailOA;
import com.gammon.qs.domain.SubcontractDetailVO;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.util.DateUtil;
import com.gammon.qs.util.RoundingUtil;

public class SubcontractDetailReportGenerator {
	
	private ExcelFile excelFile;
	private List<SubcontractDetail> scDetailsList;
	private String jobNumber;
	
	public SubcontractDetailReportGenerator(List<SubcontractDetail> scPackageList, String jobNumber)
	{
		this.scDetailsList = scPackageList;
		this.jobNumber = jobNumber;
	}
	
	public ExcelFile generate() throws Exception{
		
		excelFile = new ExcelFile();
		excelFile.setFileName("Subcontract Details " + DateUtil.formatDate(new Date(), "yyyyMMddHHmmss")+ ExcelFile.EXTENSION);
		
		if(scDetailsList.size()> 0 )
		{
			excelFile.setEmpty(false);
			excelFile.getDocument().setCurrentSheetName("job("+this.jobNumber+")");
			
			generateSheet();
		}
		else{
			excelFile.setEmpty(true);
		}
		
		return excelFile;
	}
	
	
	private void generateSheet(){
		int sheetSize = 28;
		
		excelFile.getDocument().insertRow(createTitleRow(sheetSize));
		
		int rowNumber = 1;
		for(SubcontractDetail scDetails: scDetailsList){
//			excelFile.getDocument().insertRow(createContentRow(scDetails, sheetSize));
			fillInContent(scDetails, rowNumber);
			rowNumber++;
		}
		
		
		excelFile.getDocument().setColumnWidth(0, 13);
		excelFile.getDocument().setColumnWidth(1, 13);
		excelFile.getDocument().setColumnWidth(2, 14);
		excelFile.getDocument().setColumnWidth(3, 50);
		excelFile.getDocument().setColumnWidth(4, 12);
		
		excelFile.getDocument().setColumnWidth(5, 13);		
		excelFile.getDocument().setColumnWidth(6, 10);
		excelFile.getDocument().setColumnWidth(7, 10);	
		excelFile.getDocument().setColumnWidth(8, 14);	
		excelFile.getDocument().setColumnWidth(9, 12);
		
		excelFile.getDocument().setColumnWidth(10, 20);
		excelFile.getDocument().setColumnWidth(11, 12);
		excelFile.getDocument().setColumnWidth(12, 15);
		excelFile.getDocument().setColumnWidth(13, 10);
		//excelFile.getDocument().enableTextWrapOnCell(0, 13);
		excelFile.getDocument().setColumnWidth(14, 9);
		
		excelFile.getDocument().setColumnWidth(15, 5);
		excelFile.getDocument().setColumnWidth(16, 20);
		excelFile.getDocument().setColumnWidth(17, 20);
		excelFile.getDocument().setColumnWidth(18, 20);
		//excelFile.getDocument().enableTextWrapOnCell(0, 18);
		excelFile.getDocument().setColumnWidth(19, 20);
		
		excelFile.getDocument().setColumnWidth(20, 20);
		excelFile.getDocument().setColumnWidth(21, 20);
		excelFile.getDocument().setColumnWidth(22, 20);
		excelFile.getDocument().setColumnWidth(23, 20);
		excelFile.getDocument().setColumnWidth(24, 20);
		
		excelFile.getDocument().setColumnWidth(25, 20);
		excelFile.getDocument().setColumnWidth(26, 20);
		excelFile.getDocument().setColumnWidth(27, 20);
	}
	
	/**
	 * Fill in row by row and be able to set the cell type
	 * @author tikywong
	 * created on Jan 21, 2013 2:14:47 PM
	 */
	private void fillInContent(SubcontractDetail scDetails, int row){
		excelFile.getDocument().setCellValue(row, 0, scDetails.getSequenceNo()!=null ? scDetails.getSequenceNo().toString(): "");
		excelFile.getDocument().setCellValue(row, 1, scDetails.getResourceNo() != null ? scDetails.getResourceNo().toString(): "");
		excelFile.getDocument().setCellValue(row, 2, scDetails.getBillItem()!=null ? scDetails.getBillItem().trim(): "", true);
		excelFile.getDocument().setCellValue(row, 3, scDetails.getDescription() !=null ? scDetails.getDescription().trim(): "", true);
		excelFile.getDocument().setCellValue(row, 4, scDetails.getQuantity()!=null ? scDetails.getQuantity().toString():"0");
		
		if (scDetails instanceof SubcontractDetailBQ){
			excelFile.getDocument().setCellValue(row, 5, ((SubcontractDetailBQ)scDetails).getToBeApprovedQuantity()!=null ? ((SubcontractDetailBQ)scDetails).getToBeApprovedQuantity().toString(): "0");
			excelFile.getDocument().setCellValue(row, 6, ((SubcontractDetailBQ)scDetails).getCostRate() !=null ? ((SubcontractDetailBQ)scDetails).getCostRate().toString() : "0");
			excelFile.getDocument().setCellValue(row, 9, ((SubcontractDetailBQ)scDetails).getTotalAmount()!=null ? ((SubcontractDetailBQ)scDetails).getTotalAmount().toString(): "0");
			excelFile.getDocument().setCellValue(row, 10, ((SubcontractDetailBQ)scDetails).getToBeApprovedAmount()!=null ? ((SubcontractDetailBQ)scDetails).getToBeApprovedAmount().toString(): "0");
		}else {
			excelFile.getDocument().setCellValue(row, 5, "0");
			excelFile.getDocument().setCellValue(row, 6, "0");
			excelFile.getDocument().setCellValue(row, 9, "0");
			excelFile.getDocument().setCellValue(row, 10, "0");
		}
		
		excelFile.getDocument().setCellValue(row, 7, scDetails.getScRate() != null ? scDetails.getScRate().toString(): "0");
				
		if (scDetails instanceof SubcontractDetailVO)
			excelFile.getDocument().setCellValue(row, 8, ((SubcontractDetailVO)scDetails).getToBeApprovedRate()!=null ? ((SubcontractDetailVO)scDetails).getToBeApprovedRate().toString(): "0");
		else
			excelFile.getDocument().setCellValue(row, 8, "0");
			
		excelFile.getDocument().setCellValue(row, 11, scDetails.getObjectCode()!=null ? scDetails.getObjectCode().trim(): "", true);
		excelFile.getDocument().setCellValue(row, 12, scDetails.getSubsidiaryCode()!=null ? scDetails.getSubsidiaryCode().trim():"", true);
		excelFile.getDocument().setCellValue(row, 13, scDetails.getLineType()!=null ? scDetails.getLineType().trim(): "", true);
		
		String approved = scDetails.getApproved();
		if(approved==null)
			approved = "";
		else if(!SubcontractDetail.APPROVED.equals(approved) && !SubcontractDetail.SUSPEND.equals(approved))
			approved = SubcontractDetail.NOT_APPROVED;
		excelFile.getDocument().setCellValue(row, 14, approved, true);

		excelFile.getDocument().setCellValue(row, 15, scDetails.getUnit()!=null? scDetails.getUnit().trim() : "", true);
		excelFile.getDocument().setCellValue(row, 16, scDetails.getRemark()!=null ? scDetails.getRemark().trim(): "", true);
		
		if (scDetails instanceof SubcontractDetailCC)
			excelFile.getDocument().setCellValue(row, 17, ((SubcontractDetailCC)scDetails).getContraChargeSCNo() !=null ? ((SubcontractDetailCC)scDetails).getContraChargeSCNo().trim(): "");
		else if (scDetails instanceof SubcontractDetailVO)
			excelFile.getDocument().setCellValue(row, 17, ((SubcontractDetailVO)scDetails).getContraChargeSCNo() !=null ? ((SubcontractDetailVO)scDetails).getContraChargeSCNo().trim(): "");
		else
			excelFile.getDocument().setCellValue(row, 17, "");
		
		if (scDetails instanceof SubcontractDetailOA){
			Double postedWorkQty = ((SubcontractDetailOA)scDetails).getPostedWorkDoneQuantity();
			if(postedWorkQty == null)
				postedWorkQty = Double.valueOf(0);
			Double cumWorkQty = ((SubcontractDetailOA)scDetails).getCumWorkDoneQuantity();
			if(cumWorkQty == null)
				cumWorkQty = Double.valueOf(0);
			
			excelFile.getDocument().setCellValue(row, 18, postedWorkQty.toString());
			excelFile.getDocument().setCellValue(row, 19, Double.toString(postedWorkQty * scDetails.getScRate()));
			excelFile.getDocument().setCellValue(row, 20, cumWorkQty.toString());
			excelFile.getDocument().setCellValue(row, 21, Double.toString(cumWorkQty * scDetails.getScRate()));
		}else{
			excelFile.getDocument().setCellValue(row, 18, "");
			excelFile.getDocument().setCellValue(row, 19, "");
		}
				
		Double postedCertQty = scDetails.getPostedCertifiedQuantity();
		if(postedCertQty == null)
			postedCertQty = Double.valueOf(0);
		Double cumCertQty = scDetails.getCumCertifiedQuantity();
		if(cumCertQty == null)
			cumCertQty = Double.valueOf(0);
		
		excelFile.getDocument().setCellValue(row, 22, postedCertQty.toString());
		excelFile.getDocument().setCellValue(row, 23, Double.toString(postedCertQty * scDetails.getScRate()));
		excelFile.getDocument().setCellValue(row, 24, cumCertQty.toString());
		excelFile.getDocument().setCellValue(row, 25, Double.toString(cumCertQty * scDetails.getScRate()));
		excelFile.getDocument().setCellValue(row, 26, scDetails.getBalanceType()!=null ? scDetails.getBalanceType().trim():"");
		excelFile.getDocument().setCellValue(row, 27, scDetails.getCostRate()!=null&&scDetails.getCumWorkDoneQuantity()!=null?
														Double.toString(RoundingUtil.multiple(scDetails.getCostRate(),scDetails.getCumWorkDoneQuantity())):"0.0");
	}
	
	/*private String[] createContentRow(SCDetails scDetails, int sheetSize)
	{
		String[] contentRows = new String[sheetSize];
		
		contentRows[0] = scDetails.getSequenceNo()!=null ? scDetails.getSequenceNo().toString(): "";
		contentRows[1] = scDetails.getResourceNo() != null ? scDetails.getResourceNo().toString(): "";
		contentRows[2] = scDetails.getBillItem()!=null ? scDetails.getBillItem().trim(): "";
		contentRows[3] = scDetails.getDescription() !=null ? scDetails.getDescription().trim(): "";
		contentRows[4] = scDetails.getQuantity()!=null ? scDetails.getQuantity().toString():"0";
		if (scDetails instanceof SCDetailsBQ){
			contentRows[5] = ((SCDetailsBQ)scDetails).getToBeApprovedQuantity()!=null ? ((SCDetailsBQ)scDetails).getToBeApprovedQuantity().toString(): "0";
			contentRows[6] = ((SCDetailsBQ)scDetails).getCostRate() !=null ? ((SCDetailsBQ)scDetails).getCostRate().toString() : "0";
			contentRows[9] = ((SCDetailsBQ)scDetails).getTotalAmount()!=null ? ((SCDetailsBQ)scDetails).getTotalAmount().toString(): "0";
			contentRows[10] = ((SCDetailsBQ)scDetails).getToBeApprovedAmount()!=null ? ((SCDetailsBQ)scDetails).getToBeApprovedAmount().toString(): "0";
		}else {
			contentRows[5] = "0";
			contentRows[6] = "0";
			contentRows[9] = "0";
			contentRows[10] = "0";
		}
		contentRows[7] = scDetails.getScRate() != null ? scDetails.getScRate().toString(): "0";
		if (scDetails instanceof SCDetailsVO){
			contentRows[8] = ((SCDetailsVO)scDetails).getToBeApprovedRate()!=null ? ((SCDetailsVO)scDetails).getToBeApprovedRate().toString(): "0";
		}else{
			contentRows[8] = "0";
		}
		if (scDetails instanceof SCDetailsCC){
			contentRows[17] = ((SCDetailsCC)scDetails).getContraChargeSCNo() !=null ? ((SCDetailsCC)scDetails).getContraChargeSCNo().trim(): "";
		}else if (scDetails instanceof SCDetailsVO){
			contentRows[17] = ((SCDetailsVO)scDetails).getContraChargeSCNo() !=null ? ((SCDetailsVO)scDetails).getContraChargeSCNo().trim(): "";
		}else{
			contentRows[17] ="";
		}
		if (scDetails instanceof SCDetailsOA){
			Double postedWorkQty = ((SCDetailsOA)scDetails).getPostedWorkDoneQuantity();
			if(postedWorkQty == null)
				postedWorkQty = Double.valueOf(0);
			Double cumWorkQty = ((SCDetailsOA)scDetails).getCumWorkDoneQuantity();
			if(cumWorkQty == null)
				cumWorkQty = Double.valueOf(0);
			contentRows[18] = postedWorkQty.toString();
			contentRows[19] = Double.toString(postedWorkQty * scDetails.getScRate());
			contentRows[20] = cumWorkQty.toString();
			contentRows[21] = Double.toString(cumWorkQty * scDetails.getScRate());
		}else{
			contentRows[18]="0";
			contentRows[18]="0";
		}
		
		contentRows[11] = scDetails.getObjectCode()!=null ? scDetails.getObjectCode().trim(): "";
		contentRows[12] = scDetails.getSubsidiaryCode()!=null ? scDetails.getSubsidiaryCode().trim():"";
		contentRows[13] = scDetails.getLineType()!=null ? scDetails.getLineType().trim(): "";
		contentRows[14] = scDetails.getApproved()!=null ? scDetails.getApproved().trim(): "";
		
		contentRows[15] = scDetails.getUnit()!=null? scDetails.getUnit().trim() : "";
		contentRows[16] = scDetails.getRemark()!=null ? scDetails.getRemark().trim(): "";
		
		Double postedCertQty = scDetails.getPostedCertifiedQuantity();
		if(postedCertQty == null)
			postedCertQty = Double.valueOf(0);
		Double cumCertQty = scDetails.getCumCertifiedQuantity();
		if(cumCertQty == null)
			cumCertQty = Double.valueOf(0);
		contentRows[22] = postedCertQty.toString();
		contentRows[23] = Double.toString(postedCertQty * scDetails.getScRate());
		contentRows[24] = cumCertQty.toString();
		contentRows[25] = Double.toString(cumCertQty * scDetails.getScRate());
		contentRows[26] = scDetails.getBalanceType()!=null ? scDetails.getBalanceType().trim():"";
		contentRows[27] = scDetails.getCostRate()!=null&&scDetails.getCumWorkDoneQuantity()!=null?
							Double.toString(RoundingUtil.multiple(scDetails.getCostRate(),scDetails.getCumWorkDoneQuantity())):"0.0";
		
		return contentRows;
	}*/
	
	
	private String[] createTitleRow(int sheetSize){
		String[] titleRows = new String[sheetSize];
		
		titleRows[0] = "Sequence No.";
		titleRows[1] = "Resource No.";
		titleRows[2] = "BQ Item";
		titleRows[3] = "Description";
		titleRows[4] = "BQ Quantity";
		
		titleRows[5] = "To be Approved Qty";
		titleRows[6] = "ECA Rate";
		titleRows[7] = "SC Rate";
		titleRows[8] = "To be Approved Rate";
		titleRows[9] = "Total Amount";
		
		titleRows[10] = "To be Approved Amount";
		titleRows[11] = "Object Code";
		titleRows[12] = "Subsidiary Code";
		titleRows[13] = "Line Type";
		titleRows[14] = "Approved";
		
		titleRows[15] = "Unit";
		titleRows[16] = "Remark";
		titleRows[17] = "Contra Charge SC No";
		titleRows[18] = "Posted Work Done Qty";
		titleRows[19] = "Posted Work Done Amount";
		
		titleRows[20] = "Cumulative Work Done Qty";
		titleRows[21] = "Cumulative Work Done Amount";
		titleRows[22] = "Posted Certified Qty";
		titleRows[23] = "Posted Certified Amount";
		titleRows[24] = "Cumulative Certified Qty";
		
		titleRows[25] = "Cumulative Certified Amount";
		titleRows[26] = "Balance Type";
		titleRows[27] = "IV Amount";
		
		return titleRows;
	}

}
