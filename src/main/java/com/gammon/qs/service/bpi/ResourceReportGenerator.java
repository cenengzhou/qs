package com.gammon.qs.service.bpi;

import java.util.Date;
import java.util.List;

import com.gammon.qs.domain.BpiItemResource;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.util.DateUtil;

public class ResourceReportGenerator {
	
	private ExcelFile excelFile;
	private List<BpiItemResource> resourceList;
	private String jobNumber;
	
	public ResourceReportGenerator(List<BpiItemResource> resourceList, String jobNumber){
		this.resourceList = resourceList;
		this.jobNumber = jobNumber;
	}
	
	public ExcelFile generate() throws Exception{
		excelFile = new ExcelFile();
		excelFile.setFileName("Resource Enquiry" + DateUtil.formatDate(new Date(), "yyyy-MM-dd")+ ExcelFile.EXTENSION);
		excelFile.getDocument().setCurrentSheetName("job("+ this.jobNumber+")");

		generateResource();

		return excelFile;
	}
	
	private void generateResource() throws Exception {
		int columns = 19;
		
		excelFile.getDocument().insertRow(createTitleRow(columns));
		
		int row = 1;
		int index = 0;
		for(BpiItemResource resource: resourceList){
			index= 0;
			excelFile.getDocument().insertRow(new String[19]);

			excelFile.getDocument().setCellValue(row, index++, resource.getBpiItem().getBpiPage().getBpiBill().getBillNo()!=null?resource.getBpiItem().getBpiPage().getBpiBill().getBillNo():"", true);
			excelFile.getDocument().setCellValue(row, index++, resource.getBpiItem().getBpiPage().getBpiBill().getSubBillNo()!=null ?resource.getBpiItem().getBpiPage().getBpiBill().getSubBillNo():"", true);
			excelFile.getDocument().setCellValue(row, index++, resource.getBpiItem().getBpiPage().getPageNo()!=null?resource.getBpiItem().getBpiPage().getPageNo():"",true);
			excelFile.getDocument().setCellValue(row, index++, resource.getBpiItem().getItemNo()!=null?resource.getBpiItem().getItemNo():"",true);
			excelFile.getDocument().setCellValue(row, index++, resource.getResourceNo()!=null ? resource.getResourceNo().toString():"",true);
			excelFile.getDocument().setCellValue(row, index++, resource.getObjectCode()!=null ? resource.getObjectCode().toString():"",true);
			excelFile.getDocument().setCellValue(row, index++, resource.getSubsidiaryCode()!=null ?resource.getSubsidiaryCode().toString():"",true);
			excelFile.getDocument().setCellValue(row, index++, resource.getDescription()!=null ? resource.getDescription():"",true);
			excelFile.getDocument().setCellValue(row, index++, resource.getQuantity()!=null && resource.getRemeasuredFactor() != null ? (new Double(resource.getQuantity() * resource.getRemeasuredFactor())).toString():"",false);
			excelFile.getDocument().setCellValue(row, index++, resource.getUnit()!=null ? resource.getUnit():"",true);
			excelFile.getDocument().setCellValue(row, index++, resource.getCostRate()!=null ?String.valueOf(resource.getCostRate()):"",false);
			excelFile.getDocument().setCellValue(row, index++, String.valueOf((resource.getQuantity()!=null && resource.getCostRate()!=null && resource.getRemeasuredFactor()!=null) ? resource.getQuantity() * resource.getCostRate() * resource.getRemeasuredFactor() : "0"),false);// cost amount
			excelFile.getDocument().setCellValue(row, index++, resource.getPackageNo()!=null ? resource.getPackageNo():"",true);
			excelFile.getDocument().setCellValue(row, index++, resource.getMaterialWastage()!=null ?  String.valueOf(resource.getMaterialWastage()):"",false);
			excelFile.getDocument().setCellValue(row, index++, resource.getSplitStatus()!=null? resource.getSplitStatus().toString():"",true);
			excelFile.getDocument().setCellValue(row, index++, resource.getPackageType()!=null? resource.getPackageType():"",true);
			excelFile.getDocument().setCellValue(row, index++, resource.getPackageNature()!=null? resource.getPackageNature():"",true);
			excelFile.getDocument().setCellValue(row, index++, resource.getPackageStatus()!=null ? resource.getPackageStatus():"",true);
			excelFile.getDocument().setCellValue(row, index++, resource.getResourceType()!=null ? resource.getResourceType():"",true);
			
			row++;
		}
		
		index= 0;
		excelFile.getDocument().setColumnWidth(index++, 10);
		excelFile.getDocument().setColumnWidth(index++, 10);
		excelFile.getDocument().setColumnWidth(index++, 10);
		excelFile.getDocument().setColumnWidth(index++, 10);
		excelFile.getDocument().setColumnWidth(index++, 10);

		excelFile.getDocument().setColumnWidth(index++, 15);
		excelFile.getDocument().setColumnWidth(index++, 15);
		excelFile.getDocument().setColumnWidth(index++, 60);
		excelFile.getDocument().setColumnWidth(index++, 10);
		excelFile.getDocument().setColumnWidth(index++, 10);
		
		excelFile.getDocument().setColumnWidth(index++, 10);
		excelFile.getDocument().setColumnWidth(index++, 15);
		excelFile.getDocument().setColumnWidth(index++, 15);
		excelFile.getDocument().setColumnWidth(index++, 15);
		excelFile.getDocument().setColumnWidth(index++, 15);
		
		excelFile.getDocument().setColumnWidth(index++, 15);
		excelFile.getDocument().setColumnWidth(index++, 15);
		excelFile.getDocument().setColumnWidth(index++, 15);
		excelFile.getDocument().setColumnWidth(index++, 10);

		excelFile.getDocument().enableTextWrapOnCell(0, 0, 0, 18);

	}
	
	private String[] createTitleRow(int columns){
		int index=0;
		String[] titleRows = new String[columns];
		
		titleRows[index++] = "Bill No.";
		titleRows[index++] = "Sub Bill No.";
		titleRows[index++] = "Page";
		titleRows[index++] = "Item";
		titleRows[index++] = "Resource No";
		titleRows[index++] = "Object Code";
		titleRows[index++] = "Subsidiary Code";
		titleRows[index++] = "Resource Description";
		titleRows[index++] = "Qty";
		titleRows[index++] = "Unit";
		titleRows[index++] = "Cost Rate";
		titleRows[index++] = "Cost Amount";
		titleRows[index++] = "Package No";
		titleRows[index++] = "Material Wastage %";
		titleRows[index++] = "Resource Status";	
		titleRows[index++] = "Package Type";
		titleRows[index++] = "Package Nature";
		titleRows[index++] = "Package Status";
		titleRows[index++] = "Resource Type";
		
		return titleRows;
	}

}
