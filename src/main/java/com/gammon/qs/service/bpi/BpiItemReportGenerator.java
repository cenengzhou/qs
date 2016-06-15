package com.gammon.qs.service.bpi;

import java.util.Date;
import java.util.List;

import com.gammon.qs.domain.BpiItem;
import com.gammon.qs.domain.BQLineType;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.util.DateUtil;
import com.gammon.qs.util.RoundingUtil;

public class BpiItemReportGenerator {
	
	private ExcelFile excelFile;
	private List<BpiItem> bqList;
	private String jobNumber;
	
	public BpiItemReportGenerator(List<BpiItem> bqList, String jobNumber){
		this.bqList = bqList;
		this.jobNumber = jobNumber;
	}
	
	
	
	public ExcelFile generate() throws Exception{
		excelFile = new ExcelFile();
		excelFile.setFileName("BQ Item Enquiry " + DateUtil.formatDate(new Date(), "yyyy-MM-dd")+ ExcelFile.EXTENSION);
		excelFile.getDocument().setCurrentSheetName("job("+ this.jobNumber+")");

		generateBQItem();
		return excelFile;
	}
	
	
	
	private void generateBQItem() throws Exception {
		int sheetSize = 13;
		excelFile.getDocument().insertRow(createTitleRow(sheetSize));
		
		for(BpiItem bqItem: bqList){
			excelFile.getDocument().insertRow(createContentRow(bqItem, sheetSize));
		}
		
		excelFile.getDocument().setColumnWidth(0, 7);
		excelFile.getDocument().setColumnWidth(1, 15);
		excelFile.getDocument().setColumnWidth(2, 50);
		excelFile.getDocument().setColumnWidth(3, 7);
		excelFile.getDocument().setColumnWidth(4, 8);
		excelFile.getDocument().setColumnWidth(5, 10);
		excelFile.getDocument().enableTextWrapOnCell(0, 5);
		
		excelFile.getDocument().setColumnWidth(6, 10);
		excelFile.getDocument().enableTextWrapOnCell(0, 6);		
		excelFile.getDocument().setColumnWidth(7, 10);
		excelFile.getDocument().enableTextWrapOnCell(0, 7);	
		excelFile.getDocument().setColumnWidth(8, 10);
		excelFile.getDocument().enableTextWrapOnCell(0, 8);
		excelFile.getDocument().setColumnWidth(9, 10);
		excelFile.getDocument().enableTextWrapOnCell(0, 9);	

		excelFile.getDocument().setColumnWidth(10, 10);
		excelFile.getDocument().setColumnWidth(11, 10);
		
		excelFile.getDocument().setColumnWidth(12, 11);
		excelFile.getDocument().enableTextWrapOnCell(0, 12);	
		
	}
	
	private String[] createContentRow(BpiItem bqItem, int sheetSize){
		String[] contentRows = new String[sheetSize];
		if(bqItem.getBQLineType().getName().equals(BQLineType.BQ_LINE)){
			contentRows[0]= bqItem.getSequenceNo()==null?" ":bqItem.getSequenceNo().toString();
			contentRows[1]= (bqItem.getBpiPage().getBpiBill().getBillNo()!=null?bqItem.getBpiPage().getBpiBill().getBillNo().trim():"")+"/"
								+(bqItem.getBpiPage().getBpiBill().getSubBillNo()!=null?bqItem.getBpiPage().getBpiBill().getSubBillNo().trim():"")+"/"
								+(bqItem.getBpiPage().getBpiBill().getSectionNo()!=null?bqItem.getBpiPage().getBpiBill().getSectionNo().trim():"")+"/"	
								+(bqItem.getBpiPage().getPageNo()!=null?bqItem.getBpiPage().getPageNo().trim():"")+"/"
								+(bqItem.getItemNo()!=null?bqItem.getItemNo().trim():"");
			
			contentRows[2] = bqItem.getDescription()!=null ?bqItem.getDescription().toString().trim():"";
			contentRows[3] = bqItem.getQuantity()!=null ? bqItem.getQuantity().toString().trim():"";
			contentRows[4] = bqItem.getUnit()!=null ? bqItem.getUnit().trim():"";
			contentRows[5] = bqItem.getSellingRate()!=null? bqItem.getSellingRate().toString().trim():"";
			contentRows[6] = bqItem.getSellingRate()!=null&&bqItem.getQuantity()!=null ? this.calcuTotalSellingValue(bqItem.getSellingRate(), bqItem.getQuantity()).toString().trim():"";
			contentRows[7] = bqItem.getCostRate()!=null? bqItem.getCostRate().toString().trim():"";
			contentRows[8] = bqItem.getRemeasuredQuantity()!=null ? bqItem.getRemeasuredQuantity().toString().trim():"";
			contentRows[9] = (bqItem.getRemeasuredQuantity()!=null&&bqItem.getCostRate()!=null)? (RoundingUtil.multiple(bqItem.getRemeasuredQuantity(),bqItem.getCostRate()))+"":"";
			contentRows[10] = bqItem.getBqType()!=null ? bqItem.getBqType().toString().trim():"";
			contentRows[11] = bqItem.getBqStatus()!=null ? bqItem.getBqStatus():"";
			
			contentRows[12] = bqItem.getGenuineMarkupRate()!=null ? bqItem.getGenuineMarkupRate().toString().trim():"";
		}
		else{
			contentRows[0]= bqItem.getSequenceNo()==null?" ":bqItem.getSequenceNo().toString();
			contentRows[1] = "";
			contentRows[2] = bqItem.getDescription()!=null ?bqItem.getDescription().toString().trim():"";
			contentRows[3] = "";
			contentRows[4] = "";
			contentRows[5] = "";
			contentRows[6] = "";
			contentRows[7] = "";
			contentRows[8] = "";
			contentRows[9] = "";
			contentRows[10]= "";
			contentRows[11]= bqItem.getBqStatus()!=null ? bqItem.getBqStatus():"";
			
			contentRows[12]="";
		}
		
		return contentRows;
		
	}
	
	
	private String[] createTitleRow(int sheetSize){
		
		String[] titleRows = new String[sheetSize];
		
		titleRows[0]= "Seq No";
		titleRows[1]= "B//P//I";
		titleRows[2]= "Description";
		titleRows[3]= "Qty";
		titleRows[4]= "Unit";
		titleRows[5] = "Selling Rate";
		titleRows[6] =  "Selling Value";
		titleRows[7] =  "Cost Rate";
		titleRows[8] =  "Remeasured Qty";
		titleRows[9] =  "Cost Amount";
		titleRows[10] = "BQ Type";
		titleRows[11] = "BQ Status";
		titleRows[12] ="Genuine Markup Rate";
		
		return titleRows;
		
	}
	
	// calculation 
	public Double calcuTotalSellingValue(Double sellingRate, Double quantity)
	{
		
		return new Double(Math.round(sellingRate * quantity));
		
	}
	
	public Double calcuIVPostedAmount(Double ivPostedQty, Double costRate)
	{
		
		return new Double(Math.round(ivPostedQty*costRate));
		
	}
	
	public Double calcuIVCumAmount(Double ivCumQty, Double costRate)
	{
		
		return new Double(Math.round(ivCumQty*costRate));
		
	}
}

