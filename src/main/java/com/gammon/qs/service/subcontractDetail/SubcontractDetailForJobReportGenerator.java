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

public class SubcontractDetailForJobReportGenerator {
	
	private ExcelFile excelFile;
	private List<SubcontractDetail> scDetailsList;
	private String jobNumber;
	
	public SubcontractDetailForJobReportGenerator(List<SubcontractDetail> scPackageList, String jobNumber)
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
		int sheetSize = 29;
		
		excelFile.getDocument().insertRow(createTitleRow(sheetSize));
		
		for(SubcontractDetail scDetails: scDetailsList)
		{
			excelFile.getDocument().insertRow(createContentRow(scDetails, sheetSize));
		}
		
		excelFile.getDocument().setColumnWidth(0, 13);
		
		excelFile.getDocument().setColumnWidth(1, 13);
		excelFile.getDocument().setColumnWidth(2, 13);
		excelFile.getDocument().setColumnWidth(3, 14);
		excelFile.getDocument().setColumnWidth(4, 50);
		excelFile.getDocument().setColumnWidth(5, 12);
		
		excelFile.getDocument().setColumnWidth(6, 13);		
		excelFile.getDocument().setColumnWidth(7, 10);
		excelFile.getDocument().setColumnWidth(8, 10);	
		excelFile.getDocument().setColumnWidth(9, 14);	
		excelFile.getDocument().setColumnWidth(10, 12);
		
		excelFile.getDocument().setColumnWidth(11, 20);
		excelFile.getDocument().setColumnWidth(12, 12);
		excelFile.getDocument().setColumnWidth(13, 15);
		excelFile.getDocument().setColumnWidth(14, 10);
		//excelFile.getDocument().enableTextWrapOnCell(0, 13);
		excelFile.getDocument().setColumnWidth(15, 9);
		
		excelFile.getDocument().setColumnWidth(16, 5);
		excelFile.getDocument().setColumnWidth(17, 20);
		excelFile.getDocument().setColumnWidth(18, 20);
		excelFile.getDocument().setColumnWidth(19, 20);
		//excelFile.getDocument().enableTextWrapOnCell(0, 18);
		excelFile.getDocument().setColumnWidth(20, 20);
		
		excelFile.getDocument().setColumnWidth(21, 20);
		excelFile.getDocument().setColumnWidth(22, 20);
		excelFile.getDocument().setColumnWidth(23, 20);
		excelFile.getDocument().setColumnWidth(24, 20);
		excelFile.getDocument().setColumnWidth(25, 20);
		
		excelFile.getDocument().setColumnWidth(26, 20);
		excelFile.getDocument().setColumnWidth(27, 20);
		excelFile.getDocument().setColumnWidth(28, 20);
	}
	
	private String[] createContentRow(SubcontractDetail scDetails, int sheetSize)
	{
		String[] contentRows = new String[sheetSize];
		
		contentRows[0] = scDetails.getSubcontract().getPackageNo();
		            
		contentRows[1] = scDetails.getSequenceNo()!=null ? scDetails.getSequenceNo().toString(): "";
		contentRows[2] = scDetails.getResourceNo() != null ? scDetails.getResourceNo().toString(): "";
		contentRows[3] = scDetails.getBillItem()!=null ? scDetails.getBillItem().trim(): "";
		contentRows[4] = scDetails.getDescription() !=null ? scDetails.getDescription().trim(): "";
		contentRows[5] = scDetails.getQuantity()!=null ? scDetails.getQuantity().toString():"0";
		if (scDetails instanceof SubcontractDetailBQ){
			contentRows[6] = ((SubcontractDetailBQ)scDetails).getToBeApprovedQuantity()!=null ? ((SubcontractDetailBQ)scDetails).getToBeApprovedQuantity().toString(): "0";
			contentRows[7] = ((SubcontractDetailBQ)scDetails).getCostRate() !=null ? ((SubcontractDetailBQ)scDetails).getCostRate().toString() : "0";
			contentRows[10] = ((SubcontractDetailBQ)scDetails).getTotalAmount()!=null ? ((SubcontractDetailBQ)scDetails).getTotalAmount().toString(): "0";
			contentRows[11] = ((SubcontractDetailBQ)scDetails).getToBeApprovedAmount()!=null ? ((SubcontractDetailBQ)scDetails).getToBeApprovedAmount().toString(): "0";
		}else {
			contentRows[6] = "0";
			contentRows[7] = "0";
			contentRows[10] = "0";
			contentRows[11] = "0";
		}
		contentRows[8] = scDetails.getScRate() != null ? scDetails.getScRate().toString(): "0";
		if (scDetails instanceof SubcontractDetailVO){
			contentRows[9] = ((SubcontractDetailVO)scDetails).getToBeApprovedRate()!=null ? ((SubcontractDetailVO)scDetails).getToBeApprovedRate().toString(): "0";
		}else{
			contentRows[9] = "0";
		}
		if (scDetails instanceof SubcontractDetailCC){
			contentRows[18] = (((SubcontractDetailCC)scDetails).getContraChargeSCNo() !=null && !((SubcontractDetailCC)scDetails).getContraChargeSCNo().trim().equals("0"))? ((SubcontractDetailCC)scDetails).getContraChargeSCNo().trim(): "";
		}else if (scDetails instanceof SubcontractDetailVO){
			contentRows[18] = (((SubcontractDetailVO)scDetails).getContraChargeSCNo() !=null && !((SubcontractDetailVO)scDetails).getContraChargeSCNo().trim().equals("0")) ? ((SubcontractDetailVO)scDetails).getContraChargeSCNo().trim(): "";
		}else{
			contentRows[18] ="";
		}
		if (scDetails instanceof SubcontractDetailOA){
			Double postedWorkQty = ((SubcontractDetailOA)scDetails).getPostedWorkDoneQuantity();
			if(postedWorkQty == null)
				postedWorkQty = Double.valueOf(0);
			Double cumWorkQty = ((SubcontractDetailOA)scDetails).getCumWorkDoneQuantity();
			if(cumWorkQty == null)
				cumWorkQty = Double.valueOf(0);
			contentRows[19] = postedWorkQty.toString();
			contentRows[20] = Double.toString(postedWorkQty * scDetails.getScRate());
			contentRows[21] = cumWorkQty.toString();
			contentRows[22] = Double.toString(cumWorkQty * scDetails.getScRate());
		}else{
			contentRows[19]="0";
			contentRows[20]="0";
		}
		
		contentRows[12] = scDetails.getObjectCode()!=null ? scDetails.getObjectCode().trim(): "";
		contentRows[13] = scDetails.getSubsidiaryCode()!=null ? scDetails.getSubsidiaryCode().trim():"";
		contentRows[14] = scDetails.getLineType()!=null ? scDetails.getLineType().trim(): "";
		
		String approved = scDetails.getApproved();
		if(approved==null)
			approved = "";
		else if(!SubcontractDetail.APPROVED.equals(approved) && !SubcontractDetail.SUSPEND.equals(approved))
			approved = SubcontractDetail.NOT_APPROVED;
		contentRows[15] = approved;
		
		contentRows[16] = scDetails.getUnit()!=null? scDetails.getUnit().trim() : "";
		contentRows[17] = scDetails.getRemark()!=null ? scDetails.getRemark().trim(): "";
		
		Double postedCertQty = scDetails.getPostedCertifiedQuantity();
		if(postedCertQty == null)
			postedCertQty = Double.valueOf(0);
		Double cumCertQty = scDetails.getCumCertifiedQuantity();
		if(cumCertQty == null)
			cumCertQty = Double.valueOf(0);
		contentRows[23] = postedCertQty.toString();
		contentRows[24] = Double.toString(postedCertQty * scDetails.getScRate());
		contentRows[25] = cumCertQty.toString();
		contentRows[26] = Double.toString(cumCertQty * scDetails.getScRate());
		contentRows[27] = scDetails.getBalanceType()!=null ? scDetails.getBalanceType().trim():"";
		contentRows[28] = scDetails.getCostRate()!=null&&scDetails.getCumWorkDoneQuantity()!=null?
							Double.toString(RoundingUtil.multiple(scDetails.getCostRate(),scDetails.getCumWorkDoneQuantity())):"0.0";
		
		return contentRows;
	}
	
	
	private String[] createTitleRow(int sheetSize){
		String[] titleRows = new String[sheetSize];
		
		titleRows[0] = "Package No";

		titleRows[1] = "Sequence No.";
		titleRows[2] = "Resource No.";
		titleRows[3] = "BQ Item";
		titleRows[4] = "Description";
		titleRows[5] = "BQ Quantity";
		
		titleRows[6] = "To be Approved Qty";
		titleRows[7] = "ECA Rate";
		titleRows[8] = "SC Rate";
		titleRows[9] = "To be Approved Rate";
		titleRows[10] = "Total Amount";
		
		titleRows[11] = "To be Approved Amount";
		titleRows[12] = "Object Code";
		titleRows[13] = "Subsidiary Code";
		titleRows[14] = "Line Type";
		titleRows[15] = "Approved";
		
		titleRows[16] = "Unit";
		titleRows[17] = "Remark";
		titleRows[18] = "Contra Charge SC No";
		titleRows[19] = "Posted Work Done Qty";
		titleRows[20] = "Posted Work Done Amount";
		
		titleRows[21] = "Cumulative Work Done Qty";
		titleRows[22] = "Cumulative Work Done Amount";
		titleRows[23] = "Posted Certified Qty";
		titleRows[24] = "Posted Certified Amount";
		titleRows[25] = "Cumulative Certified Qty";
		
		titleRows[26] = "Cumulative Certified Amount";
		titleRows[27] = "Balance Type";
		titleRows[28] = "IV Amount";
		
		return titleRows;
	}

}
