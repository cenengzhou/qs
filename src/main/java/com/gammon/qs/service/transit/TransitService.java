package com.gammon.qs.service.transit;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.config.JasperConfig;
import com.gammon.pcms.dto.rs.provider.response.resourceSummary.ResourceSummayDashboardDTO;
import com.gammon.pcms.helper.FileHelper;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.application.exception.ValidateBusinessLogicException;
import com.gammon.qs.dao.AppTransitUomHBDao;
import com.gammon.qs.dao.BpiBillHBDao;
import com.gammon.qs.dao.BpiItemHBDao;
import com.gammon.qs.dao.BpiItemResourceHBDao;
import com.gammon.qs.dao.BpiPageHBDao;
import com.gammon.qs.dao.SubcontractHBDao;
import com.gammon.qs.dao.TransitBpiHBDao;
import com.gammon.qs.dao.TransitCodeMatchHBDao;
import com.gammon.qs.dao.TransitHBDao;
import com.gammon.qs.dao.TransitResourceHBDao;
import com.gammon.qs.domain.AppTransitUom;
import com.gammon.qs.domain.BpiBill;
import com.gammon.qs.domain.BpiItem;
import com.gammon.qs.domain.BpiItemResource;
import com.gammon.qs.domain.BpiPage;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.domain.Transit;
import com.gammon.qs.domain.TransitBpi;
import com.gammon.qs.domain.TransitCodeMatch;
import com.gammon.qs.domain.TransitResource;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.io.ExcelWorkbook;
import com.gammon.qs.io.ExcelWorkbookProcessor;
import com.gammon.qs.service.BudgetPostingService;
import com.gammon.qs.service.JobCostService;
import com.gammon.qs.service.JobInfoService;
import com.gammon.qs.service.MasterListService;
import com.gammon.qs.service.ResourceSummaryService;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.shared.util.CalculationUtil;
import com.gammon.qs.util.JasperReportHelper;
import com.gammon.qs.util.RoundingUtil;
import com.gammon.qs.wrapper.transitBQMasterReconciliationReport.TransitBQMasterReconciliationReportRecordWrapper;
import com.gammon.qs.wrapper.transitBQResourceReconciliationReportRecordWrapper.TransitBQResourceReconciliationReportRecordWrapper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
@Component
//change to session scope for download import transit error report
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session") 
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class TransitService implements Serializable {
	private static final long serialVersionUID = -1930185573387732835L;
	@Autowired
	private transient JobInfoService jobRepository;
	@Autowired
	private transient TransitHBDao transitHeaderDao;
	@Autowired
	private transient TransitBpiHBDao transitBqDao;
	@Autowired
	private transient TransitResourceHBDao transitResourceDao;
	@Autowired
	private transient MasterListService masterListRepository;
	@Autowired
	private transient ExcelWorkbookProcessor excelFileProcessor;
	@Autowired
	private transient TransitCodeMatchHBDao transitCodeMatchDao;
	@Autowired
	private transient AppTransitUomHBDao transitUomMatchDao;
	@Autowired
	private transient BpiBillHBDao billDao;
	@Autowired
	private transient BpiPageHBDao pageDao;
	@Autowired
	private transient BpiItemHBDao bqItemDao;
	@Autowired
	private transient BpiItemResourceHBDao bqItemResourceDao;
	@Autowired
	private transient SubcontractHBDao scPackageDao;
	@Autowired
	private transient JasperConfig jasperConfig;
	@Autowired
	private transient JobCostService jobCostService;
	@Autowired
	private transient BudgetPostingService budgetPostingService;
	@Autowired
	private transient ResourceSummaryService resourceSummaryService;
	
	private List<TransitCodeMatch> codeMatchCache;
	private List<AppTransitUom> uomMatchCache;
	private List<String> errorList;
	private List<String> warningList; // added by brian on 20110224
	
	public static final int RECORDS_PER_PAGE = 200;
	
	private transient Logger logger = Logger.getLogger(TransitService.class.getName());
	
	private BpiItem bqItemFromTransit(TransitBpi transitBq){
		BpiItem bqItem = new BpiItem();
		bqItem.setRefBillNo(transitBq.getBillNo());
		bqItem.setRefSubBillNo(transitBq.getSubBillNo());
		bqItem.setRefPageNo(transitBq.getPageNo());
		bqItem.setItemNo(transitBq.getItemNo());
		bqItem.setSequenceNo(transitBq.getSequenceNo());
		bqItem.setSortDisplaySeqNo(transitBq.getSequenceNo());
		bqItem.setDescription(transitBq.getDescription());
		bqItem.setQuantity(transitBq.getQuantity());
		bqItem.setRemeasuredQuantity(transitBq.getQuantity());
		if(transitBq.getBillNo().equals("80"))
			bqItem.setGenuineMarkupRate(new Double(1));
		else{
			bqItem.setCostRate(transitBq.getCostRate());
			bqItem.setSellingRate(transitBq.getSellingRate());
			/**
			 * @author koeyyeung
			 * modified on 20 Feb, 2017
			 * Convert to Amount Based**/
			bqItem.setAmountBudget(CalculationUtil.round(transitBq.getAmountBudget().doubleValue(), 2));
			bqItem.setAmountSelling(transitBq.getValue());
		}
		bqItem.setUnit(transitBq.getUnit());
		bqItem.setBqType("TI");
		
		return bqItem;
	}
	
	private BpiItemResource resourceFromTransit(TransitResource transitResource){
		BpiItemResource resource = new BpiItemResource();
		String obj = transitResource.getObjectCode();
		resource.setObjectCode(obj);
		if(obj.startsWith("11"))
			resource.setResourceType("LR");
		else if(obj.startsWith("12"))
			resource.setResourceType("PT");
		else if(obj.startsWith("13"))
			resource.setResourceType("ML");
		else if(obj.startsWith("14"))
			resource.setResourceType("SC");
		else if(obj.startsWith("15"))
			resource.setResourceType("OS");
		else if(obj.startsWith("19"))
			resource.setResourceType("IC");
		resource.setSubsidiaryCode(transitResource.getSubsidiaryCode());
		String packageNo = transitResource.getPackageNo();
		resource.setPackageNo(packageNo);
		if(packageNo != null && !packageNo.equals("0")){
			if(packageNo.startsWith("1")){
				resource.setPackageNature("DSC");
				resource.setPackageType("S");
			}
			else if(packageNo.startsWith("2")){
				resource.setPackageNature("NDSC");
				resource.setPackageType("S");
			}
			else if(packageNo.startsWith("3")){
				resource.setPackageNature("NSC");
				resource.setPackageType("S");
			}
			else if(packageNo.startsWith("6")){
				resource.setPackageNature("DS");
				resource.setPackageType("M");
			}
		}
		resource.setResourceNo(transitResource.getResourceNo());
		resource.setDescription(transitResource.getDescription());
		resource.setQuantity(transitResource.getTotalQuantity());
		resource.setRemeasuredFactor(new Double(1));
		resource.setMaterialWastage(transitResource.getWaste());
		resource.setUnit(transitResource.getUnit());
		resource.setCostRate(transitResource.getRate());
		resource.setSplitStatus("100");
		/**
		 * @author koeyyeung
		 * created on 20 Feb, 2017
		 * Convert to Amount Based**/
		resource.setAmountBudget(transitResource.getValue());
		return resource;
	}

	public TransitImportResponse importBqItemsOrResourcesFromXls(String jobNumber, String type, byte[] file) throws Exception{
		if(type.equals(GlobalParameter.TRANSIT_BQ))
			return importBqItems(jobNumber, file);			
		else if(type.equals(GlobalParameter.TRANSIT_RESOURCE))
			return importResources(jobNumber, file);				
		else if(type.equals(GlobalParameter.TRANSIT_CODE_MATCHING))
			return importResourceCodeMatching(file);
		else if(type.equals(GlobalParameter.TRANSIT_UOM_MATCHING))
			return importUomMatching(file);
		else{
			TransitImportResponse response = new TransitImportResponse();
			response.setMessage("Import failed.<br/>If problem persists, please contact support.");
			return response;
		}
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
	public TransitImportResponse importBqItems(String jobNumber, byte[] file) throws Exception{
		TransitImportResponse response = new TransitImportResponse();
		errorList = new ArrayList<String>();
		// added by brian on 20110224
		warningList = new ArrayList<String>();
		
		Transit header = transitHeaderDao.getTransitHeader(jobNumber);
		if(header == null){
			response.setMessage("Please create a header before importing items");
			return response;
		}
		else if(Transit.TRANSIT_COMPLETED.equals(header.getStatus())){
			response.setMessage("Transit for this job has already been completed");
			return response;
		}
		try {
			transitHeaderDao.lock(header);
		}catch(IllegalAccessException e) {
			e.printStackTrace();
			response.setMessage(e.getMessage());
			return response;
		}
		transitResourceDao.deleteResourcesByHeader(header);
		transitBqDao.deleteTransitBqItemsByHeader(header);
		
		int sequenceNo = 1;
		int importBQCount = 0;
		int i = 0;
		List<TransitBpi> transitBpiList = new ArrayList<TransitBpi>();
		List<TransitBpi> headersList = new ArrayList<TransitBpi>(); 
		try{
			List<Object[]> uomCodeMatches = transitUomMatchDao.getAllUomMatches();
			Map<String, String> uomMap = new HashMap<String, String>(uomCodeMatches.size());
			for(Object[] codeMatch : uomCodeMatches)
				uomMap.put(((String)codeMatch[0]).toUpperCase(), ((String)codeMatch[1]).toUpperCase());
			
			//Open file
			excelFileProcessor.openFile(file);
			//Skip header 
			excelFileProcessor.readLine(0);
			
			//Map to hold the bqItems (does not include headers), to check for duplicates - maps item to line number
			HashMap<TransitBpi, Integer> bqItemMap = new HashMap<TransitBpi, Integer>();
			//temp list to store the headers before you can fill bill/subBill/page, then move to headersList
			List<TransitBpi> headersTempList = new ArrayList<TransitBpi>(); 
			
			for(i = 2; i <= excelFileProcessor.getNumRow(); i++){
				String[] row = excelFileProcessor.readLine(8);
				if(row == null || isRowEmpty(row))
					continue;
				
				String description = row[3];
				if(description == null || description.length() == 0){
					errorList.add("Line " + i+": [Column D] Description is blank.");
					continue;		
				}
				
				// added by brian on 30110119
				// check description longer than 1000 or not, if yes, log in error report and don't insert into database
				if(description != null && description.length() > 1000){
					errorList.add("Line " + i+": [Column D] Description is longer than 1000 characters.");
					continue;
				}
				
				TransitBpi bqItem = new TransitBpi();
				bqItem.setTransit(header);
				bqItem.setDescription(description);
				bqItem.setSequenceNo(sequenceNo++);
				importBQCount += 1;
				
				
				
				if(!isHeader(row)){
					//First 2 digits: Bill, 3-5: SubBill
					if(row[0]== null || row[0].length() < 2){
						errorList.add("Line " + i+": [Column A] Bill should not be less than 2 digits.");
						continue;
					}
					
					String billNo = row[0].substring(0, 2);
					String subBillNo = row[0].substring(2);
					// check if the sub bill number is "0" or "00" or "000"
					try {
						if(Integer.valueOf(subBillNo) == 0)
							subBillNo = null;
					} catch (Exception e) {
						
					}
					/*if("00000".substring(0, subBillNo.length()).equals(subBillNo))
						subBillNo = null;*/
					String pageNo = row[1];
					if(pageNo == null){
						errorList.add("Line " + i+": [Column B] Page is blank.");
						continue;
					}
					String itemNo = row[2];
					if(itemNo == null){
						errorList.add("Line " + i+": [Column C] Item is blank.");
						continue;
					}
					//Validate unit
					String causewayUom = row[5];
					if(causewayUom == null){
						errorList.add("Line " + i+": [Column F] Units is blank.");
						continue;
					}
					else{
						String jdeUnit = uomMap.get(causewayUom.toUpperCase().trim());
						if(jdeUnit == null){
							errorList.add("Line " + i+": [Column F] Units: "+causewayUom+" does not exsit in Causeway UOM.");
							continue;
						}
						
						// added by brian on 20110119
						// check if unit too long
						if(jdeUnit != null && jdeUnit.length() > 10){
							errorList.add("Line " + i+": [Column F] Units is longer than 10 characters.");
							continue;
						}
						
						bqItem.setUnit(jdeUnit);
					}
					//Update fields
					bqItem.setBillNo(billNo);
					bqItem.setSubBillNo(subBillNo);
					bqItem.setPageNo(pageNo);
					bqItem.setItemNo(itemNo);
					// logic to get quantity, rate and total value
					double quantity = row[4] == null ? 0.0 : Double.parseDouble(row[4]);
					bqItem.setQuantity(CalculationUtil.round(quantity, 4));
					double rate = row[6] == null ? 0.0 : Double.parseDouble(row[6]);
					bqItem.setSellingRate(CalculationUtil.round(rate, 4));
					
					// added by brian on 20110224 - start
					// Get the total value from Excel
					if(row[7] != null && row[7].length() > 0){
						double value = Double.parseDouble(row[7]);
						bqItem.setValue(CalculationUtil.round(value, 2));
					}
					else{
						this.errorList.add("Line "+i+": [Column H] Total Value is blank.");
						continue;
					}
					
					// check is there any different between total value and Qty*Rate (include rounding error)
					double difference = CalculationUtil.round((bqItem.getValue()-(bqItem.getSellingRate()*bqItem.getQuantity())), 2);
					if(difference != 0.0){
						this.warningList.add("Line " + i+": [Column E] Qty: "+bqItem.getQuantity()+" * [Column G] Rate: "+bqItem.getSellingRate()
								+" does not equal to [Column H] Total Value: "+bqItem.getValue()
								+ ". Difference: " + difference );
						
						response.setHaveWarning(true);
					}
					
					// added by brian on 20110224 - end
					
					//Check that bqItem doesn't already exist (same bill, subBill, page, item - done in hashCode/equals)
					Integer itemLineNo = bqItemMap.get(bqItem);
					if(itemLineNo != null){
						errorList.add("Lines " + itemLineNo.toString() + " and " + i+
								": "+bqItem.getBillNo()+"/"+bqItem.getSubBillNo()+"/"+bqItem.getPageNo()+"/"+bqItem.getItemNo()+" (Bill/SubBill/Page/Item) is duplicated.");
						continue;
					}
					else
						bqItemMap.put(bqItem, Integer.valueOf(i));
					
					transitBpiList.add(bqItem);
					
					
					//Check if header fields need to be filled
					if(headersTempList.size() != 0){
						for(TransitBpi bqHeader : headersTempList){
							bqHeader.setBillNo(billNo);
							bqHeader.setSubBillNo(subBillNo);
							bqHeader.setPageNo(pageNo);
							
							headersList.add(bqHeader);
						}
						headersTempList.clear();
					}
				}
				else{
					//Add header to temp list - have to scan ahead to find the bill/subBill/page, then go back and fill the header(s)
					headersTempList.add(bqItem);
				}
			}
		}
		catch(Exception e){
			errorList.add("An error occurred while trying to process the excel file, at line " + i + ": " + e.getMessage());
			e.printStackTrace();
		}

		if(errorList.size() == 0){
			header.setStatus(Transit.BQ_IMPORTED);
			transitHeaderDao.saveOrUpdate(header);
			for(TransitBpi transitBpi: transitBpiList){
				transitBqDao.saveOrUpdate(transitBpi);
			}
			for(TransitBpi transitBpi: headersList){
				transitBqDao.saveOrUpdate(transitBpi);
			}
//			response.setNumRecordImported(sequenceNo);
			// modified by brian on 20110118
			// since sequenceNo start from 1 so the import count is incorrect,
			// so change to importBQCount which start from 0
			response.setNumRecordImported(importBQCount);
			response.setSuccess(true);
			
			// added by brian on 20110225
			if(warningList.size() > 0){
				response.setHaveWarning(true);
				response.setMessage(GlobalParameter.TRANSIT_SUCCESS_WITH_WARNING);
			}
			else if(warningList.size() == 0){
				response.setHaveWarning(false);
			}
			
		}
		else
			response.setMessage(GlobalParameter.TRANSIT_ERROR);
		transitHeaderDao.unlock(header);
		return response;
	}
	
	public void unlockTransitHeader(String jobNumber) throws DatabaseOperationException {
		Transit transit = transitHeaderDao.getTransitHeader(jobNumber);
		transitHeaderDao.unlock(transit);
	}
	
	public TransitImportResponse importResources(String jobNumber, byte[] file) throws Exception{
		TransitImportResponse response = new TransitImportResponse();
		
		Transit header = transitHeaderDao.getTransitHeader(jobNumber);
		if(Transit.TRANSIT_COMPLETED.equals(header.getStatus())){
			response.setMessage("Transit for this job has already been completed");
			return response;
		}
		try {
			transitHeaderDao.lock(header);
		}catch(IllegalAccessException e) {
			e.printStackTrace();
			response.setMessage(e.getMessage());
			return response;
		}
		errorList = new ArrayList<String>();
		// added by brian on 20110225
		warningList = new ArrayList<String>();
		
		transitResourceDao.deleteResourcesByHeader(header);
		transitBqDao.deleteTransitBqBill80ByHeader(header);//Reserved for Genuine Markup
		List<TransitBpi> transitBQList = transitBqDao.obtainTransitBQByTransitHeader(header);
		Map<String, TransitBpi> bqItemMap = new HashMap<String, TransitBpi>(transitBQList.size());
		String matchingType = header.getMatchingCode();
		
		for(TransitBpi bqItem : transitBQList){
			if(bqItem.getItemNo() != null){
				String bpi = bqItem.getBillNo() + "/" + (bqItem.getSubBillNo() != null ? bqItem.getSubBillNo() : "") + 
							"//" + (bqItem.getPageNo() != null ? bqItem.getPageNo() : "") + "/" + bqItem.getItemNo();
				bqItemMap.put(bpi, bqItem);
			}
		}
		
		int i = 0;
		int resourceNo = 1;
		int count = 0;
		List<TransitResource> resources = null;
		try{
			//Prepare unit and resource code matching maps
			List<Object[]> uomCodeMatches = transitUomMatchDao.getAllUomMatches();
			Map<String, String> uomMap = new HashMap<String, String>(uomCodeMatches.size());
			for(Object[] codeMatch : uomCodeMatches)
				uomMap.put(((String)codeMatch[0]).toUpperCase(), ((String)codeMatch[1]).toUpperCase());
			List<Object[]> resourceCodeMatches = transitCodeMatchDao.getCodeMatchesByType(matchingType);
			Map<String, String> codeMap = new HashMap<String, String>(resourceCodeMatches.size());
			for(Object[] codeMatch : resourceCodeMatches)
				codeMap.put((String)codeMatch[0], (String)codeMatch[1] + "." + (String)codeMatch[2]);
			Set<String> accountCodes = new HashSet<String>();
			
			TransitBpi bqItem = null;
			//Open file
			excelFileProcessor.openFile(file);
			//skip the header line
			excelFileProcessor.readLine(0);
			resources = new ArrayList<TransitResource>(excelFileProcessor.getNumRow());
			for(i = 2; i <= excelFileProcessor.getNumRow(); i++){
				String[] row = excelFileProcessor.readLine(20);
				if(row == null || isRowEmpty(row))
					continue;
				
				//Skip non-resource lines, check if resource has code, type, desc
				String resourceCode = (row[8] != null && row[8].trim().length() != 0) ? row[8].trim() : null;
				String type = (row[9] != null && row[9].trim().length() != 0) ? row[9].trim() : null;
				String description = (row[10] != null && row[10].trim().length() != 0) ? row[10].trim() : null;
				
				
				if(!(resourceCode != null && type != null && description != null)){
					if(resourceCode != null || type != null || description != null)
						errorList.add("Line " + i+": [Column I] Resource code, [Column J] Type, and [Column K] Description must not be blank.");
					continue;
				}
				
				// added by brian on 20110119
				// check if resource code too long
				if(resourceCode != null && resourceCode.length() > 10){
					errorList.add("Line " + i+": [Column I] Resource Code is longer than 10 characters.");
					continue;
				}
				
				// added by brian on 20110119
				// check if type valid
				List<String> typeLimitation = new ArrayList<String>();
				typeLimitation.add("IC"); typeLimitation.add("L");typeLimitation.add("M");
				typeLimitation.add("O"); typeLimitation.add("S"); typeLimitation.add("P");
				if(type != null && !typeLimitation.contains(type)){
					errorList.add("Line " + i+": Only IC , L , M , O , S and P are allowed to be [Column J] Resource Type.");
					continue;
				}
				
				// added by brian on 20110119
				// check if description too long
				if(description != null && description.length() > 255){
					errorList.add("Line " + i + ":[Column K] Description is longer than 255 characters.");
					continue;
				}
				
				//Check if resource is under a new bqItem, and if so, get it from the map
				if(row[0] != null && row[0].length() > 0){
					//Get bpi
					String billNo = row[0].substring(0, 2);
					String subBillNo = row[0].substring(2);
//					if(Integer.parseInt(subBillNo) == 0)
					// check if the sub bill number is "0" or "00" or "000"
					if("00000".substring(0, subBillNo.length()).equals(subBillNo))
						subBillNo = "";
					String pageNo = row[1];
					if(pageNo == null){
						errorList.add(" Line " + i+": [Column B] Page is blank.");
						continue;
					}
					String itemNo = row[2];
					if(itemNo == null){
						errorList.add(" Line " + i+": [Column C] Item is blank.");
						continue;
					}
					String bpi = billNo + "/" + subBillNo + "//" + pageNo + "/" + itemNo;
					//Get item from map
					bqItem = bqItemMap.get(bpi);
					//Make sure item is not null
					if(bqItem == null){
						errorList.add(" Line " + i+": Could not find BQ Item (you may have to import the BQ items again).");
						continue;
					}
					//reset resourceNo
					resourceNo = 1;
				}
				
				TransitResource resource = new TransitResource();
				resource.setTransitBpi(bqItem);
				resource.setResourceNo(resourceNo);
				resource.setResourceCode(resourceCode);
				resource.setType(type);
				resource.setDescription(description);
				
				// For ERROR Report
				String bpi = resource.getTransitBpi().getBillNo() + "/" 
				+ (resource.getTransitBpi().getSubBillNo()!=null?resource.getTransitBpi().getSubBillNo():"") + "//" 
				+ resource.getTransitBpi().getPageNo() + "/" 
				+ resource.getTransitBpi().getItemNo();
				
				// Old Transit Resource logic for calculate total value, total qty and rate (rate can get from value/qty)
				
				// added by brian on 20110224
				if(row[16] != null && row[16].length() > 0){
					Double totalValue = new Double(row[16]);
					resource.setValue(CalculationUtil.round(totalValue, 2));
				}
				else{
					errorList.add("Line " + i+": [Column Q] Total Value of " + bpi + "is blank.");
					continue;
				}
				
				// Get the Resource Quantity
				if(row[13] != null && row[13].length() > 0){
					Double totalQuantity = new Double(row[13]);
					resource.setTotalQuantity(CalculationUtil.round(totalQuantity, 4));
				}
				else if("S".equals(resource.getType())){
					//Quantity can be calculated from excel if Type = "S"
					resource.setTotalQuantity(CalculationUtil.round(bqItem.getQuantity(), 4));
				}
				else{
					errorList.add("Line " + i+": [Column N] Total Qty of " + bpi + " is blank.");
					continue;
				}
				
				// Get Resource Rate 
				if(row[15] != null && row[15].length() > 0){
					Double rate = new Double(row[15]);
					resource.setRate(CalculationUtil.round(rate, 4));
				}
				else if("S".equals(resource.getType())){
					//Rate can be calculated from excel if Type = "S"
					if(bqItem.getValue()== 0.0)
						resource.setRate(0.0);
					else
						resource.setRate(CalculationUtil.round(resource.getValue()/resource.getTotalQuantity(), 4));
				}
				else{
					errorList.add("Line " + i+": [Column P] Rate of " + bpi + " is blank.");
					continue;
				}
								
				
				
				resource.setWaste(row[12] != null ? new Double(row[12].trim()) : new Double(0));
				
				
				//Verify Unit
				if(row[14] != null && row[14].length() > 0){
					String causewayUom = row[14].trim().toUpperCase();
					String jdeUom = uomMap.get(causewayUom);
					if(jdeUom == null){
						errorList.add("Line " + i+": [Column O] Units: "+causewayUom+" does not exsit in Causeway UOM.");
						continue;
					}

					if(jdeUom != null && jdeUom.length() > 10){
						errorList.add("Line " + i+": [Column O] Unit is longer than 10 characters.");
						continue;
					}
					resource.setUnit(jdeUom);
				}
				else{
					//Unit can be copied from BQ if Type is "S"
					if("S".equals(resource.getType()))
						resource.setUnit(bqItem.getUnit());
					else{
						errorList.add("Line " + i+": [Column O] Unit of " + bpi + " is blank.");
						continue;
					}
				}
				
				
				// check if there is rounding difference
				double difference = CalculationUtil.round((resource.getValue()-(resource.getRate()*resource.getTotalQuantity())), 2);
				if(difference != 0.0){
					this.warningList.add("Line " + i+": [Column N] Total Qty: "+resource.getTotalQuantity()+" * [Column P] Rate: "+resource.getRate()
							+" does not equal to [Column Q] Total Value: "+resource.getValue()
							+ ". Difference: " + difference );
					
					response.setHaveWarning(true);
				}
				
				
				if(row[17] != null && row[17].trim().length() > 0 && isDigits(row[17].trim())
						&& row[18] != null && row[18].trim().length() > 0 && isDigitsOrLetter(row[18].trim())){
					
					if(row[17] != null && row[17].trim().length() > 6){
						errorList.add(" Line " + i+": [Column R] Object Code is longer than 6 characters.");
						continue;
					}
					
					if(row[18] != null && row[18].trim().length() > 8){
						errorList.add(" Line " + i+": [Column S] Subsidiary Code is longer than 8 characters.");
						continue;
					}
					
					resource.setObjectCode(row[17].trim());
					resource.setSubsidiaryCode(row[18].trim());
					//Put account codes in a set to validate later (so you don't validate duplicates)
					String accountCode = row[17].trim() + "." + row[18].trim();
					accountCodes.add(accountCode);
				}
				else{
					if(resourceCode.length() < 5) {
						errorList.add("Line " + i + ": " + "[Column I] Resource code less then 5 characters");
						continue;
					}
					String accountCode = codeMap.get(resourceCode.substring(0, 5).toUpperCase());
					if(accountCode != null){
						String[] objSub = accountCode.split("\\.");
						
						if(objSub[0] != null && objSub[0].length() > 6){
							errorList.add(" Line " + i+": [Column R] Object Code is longer than 6 characters.");
							continue;
						}
						
						if(objSub[1] != null && objSub[1].length() > 8){
							errorList.add(" Line " + i+": [Column S] Subsidiary Code is longer than 8 characters.");
							continue;
						}
						
						resource.setObjectCode(objSub[0]);
						resource.setSubsidiaryCode(objSub[1]);
						accountCodes.add(accountCode);
					}
				}
				
				//Verify Package Number for Material and Subcontract package
				if(row[19] != null && row[19].trim().length() > 0 && isDigits(row[19].trim())){
					String packageNo = row[19].trim();
					String obj = resource.getObjectCode().substring(0, 2);
					if("13".equals(obj) || "14".equals(obj)){
						if("13".equals(obj) && !packageNo.startsWith("6")){
							errorList.add(" Line " + i+": [Column T] Package No. should start with '6' for object code '13xxx' (Material Package).");
							continue;
						}
						else if("14".equals(obj) && resource.getSubsidiaryCode().startsWith("3") && !packageNo.startsWith("3")){
							errorList.add(" Line " + i+": [Column T] Package No. should start with '3' for object code '14xxx' and subsidiary code '3xxxxxxx' (NSC).");
							continue;
						}
						else if("14".equals(obj) && !resource.getSubsidiaryCode().startsWith("3") && !packageNo.startsWith("1") && !packageNo.startsWith("2")){
							errorList.add(" Line " + i+": [Column T] Package No. should start with '1' or '2' for object code '14xxx' (DSC, NDSC)." );
							continue;
						}
					}else{
						errorList.add(" Line " + i+": [Column T] Package No. can be only assigned to object code '14xxx' and '13xxx'." );
						continue;
					}

					resource.setPackageNo(row[19].trim());
				}
				
				
				resources.add(resource);
				resourceNo++;
				count++;
			}
			
			for(String accountCode : accountCodes){
				String[] objSub = accountCode.split("\\.");
				String validateObjSubError = masterListRepository.validateObjectAndSubsidiaryCodes(objSub[0], objSub[1]);
				if(validateObjSubError != null){
					errorList.add(validateObjSubError);
				}
			}
		}catch(Exception e){
			errorList.add("An error occurred while trying to process the excel file, at line " + i + ": " + e.getMessage());
			e.printStackTrace();
		}
		transitHeaderDao.unlock(header);
		if(errorList.size() == 0){
			transitResourceDao.saveResources(resources);
			header.setStatus(Transit.RESOURCES_IMPORTED);
			transitHeaderDao.saveOrUpdate(header);
			response.setSuccess(true);
			response.setNumRecordImported(count);
			
			// added by brian on 20110225
			if(warningList.size() > 0){
				response.setHaveWarning(true);
				response.setMessage(GlobalParameter.TRANSIT_SUCCESS_WITH_WARNING);
			}
			else if(warningList.size() == 0){
				response.setHaveWarning(false);
			}
		}
		else
			response.setMessage(GlobalParameter.TRANSIT_ERROR);
		return response;
	}
	
	
	public ExcelFile downloadErrorList(){
		if(errorList == null || errorList.size() == 0)
			return null;
		ExcelFile excel = new ExcelFile();
		ExcelWorkbook doc = excel.getDocument();
		String name = "Error Report";
		excel.setFileName(name + ExcelFile.EXTENSION);
		doc.setCurrentSheetName(name);
		for(String error : errorList){
			doc.insertRow(new String[]{error});
		}
		errorList = null;
		return excel;
	}
	
	// added by brian on 20110224
	public ExcelFile downloadWarningList(){
		if(warningList == null || warningList.size() == 0)
			return null;
		ExcelFile excel = new ExcelFile();
		ExcelWorkbook doc = excel.getDocument();
		String name = "Warning Report";
		excel.setFileName(name + ExcelFile.EXTENSION);
		doc.setCurrentSheetName(name);
		for(String warning : warningList){
			doc.insertRow(new String[]{warning});
		}
		warningList = null;
		return excel;
	}
	
	
	private TransitImportResponse importResourceCodeMatching(byte[] file) throws Exception{
		TransitImportResponse response = new TransitImportResponse();
		List<TransitCodeMatch> codeMatches;
		int i = 0;
		try{
			//Open file
			excelFileProcessor.openFile(file);
			//skip the header line
			excelFileProcessor.readLine(0);
			
			codeMatches = new ArrayList<TransitCodeMatch>(excelFileProcessor.getNumRow());
			//Iterate through the rows
			for(i = 2; i <= excelFileProcessor.getNumRow(); i++){
				TransitCodeMatch codeMatch = new TransitCodeMatch();
				String[] row = excelFileProcessor.readLine(4);
				
				codeMatch.setMatchingType(row[0].trim().toUpperCase());
				codeMatch.setResourceCode(row[1].trim().toUpperCase());
				codeMatch.setObjectCode(row[2].trim());
				codeMatch.setSubsidiaryCode(row[3].trim());
				
				codeMatches.add(codeMatch);
			}
		}catch(Exception e){
			response.setMessage("An error occured while reading the excel file. (Line " + i + ")");
			return response;
		}
		
		String saveError = transitCodeMatchDao.saveAll(codeMatches);
		if(saveError != null){
			response.setMessage(saveError);
		}
		else{
			response.setSuccess(true);
			response.setNumRecordImported(codeMatches.size());
		}
		return response;
	}
	
	private TransitImportResponse importUomMatching(byte[] file) throws Exception{
		TransitImportResponse response = new TransitImportResponse();
		List<AppTransitUom> uomCodeMatches;
		int i = 0;
		try{
			//Open file
			excelFileProcessor.openFile(file);
			//skip the header line
			excelFileProcessor.readLine(0);
			
			uomCodeMatches = new ArrayList<AppTransitUom>(excelFileProcessor.getNumRow());
			//Iterate through the rows
			for(i = 2; i <= excelFileProcessor.getNumRow(); i++){
				AppTransitUom codeMatch = new AppTransitUom();
				String[] row = excelFileProcessor.readLine(2);
				
				codeMatch.setCausewayUom(row[0].trim().toUpperCase());
				codeMatch.setJdeUom(row[1].trim().toUpperCase());
				
				uomCodeMatches.add(codeMatch);
			}
		}catch(Exception e){
			response.setMessage("An error occured while reading the excel file. (Line " + i + ")");
			return response;
		}
		
		String saveError = transitUomMatchDao.saveAll(uomCodeMatches);
		if(saveError != null){
			response.setMessage(saveError);
		}
		else{
			response.setSuccess(true);
			response.setNumRecordImported(uomCodeMatches.size());
		}
		return response;
	}
	
	public List<TransitCodeMatch> obtainTransitCodeMatcheList(String matchingType, String resourceCode, 
			String objectCode, String subsidiaryCode){
		List<TransitCodeMatch> transitCodeMatchList = null;
		try {
			transitCodeMatchList = transitCodeMatchDao.searchTransitCodeMatches(matchingType, resourceCode, objectCode, subsidiaryCode);
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
		Collections.sort(transitCodeMatchList, new Comparator<TransitCodeMatch>(){
			public int compare(TransitCodeMatch cm1, TransitCodeMatch cm2) {
				int typeComp = cm1.getMatchingType().compareTo(cm2.getMatchingType());
				if(typeComp != 0)
					return typeComp;
				return cm1.getResourceCode().compareTo(cm2.getResourceCode());
			}
		});
		return transitCodeMatchList;
	}

	public ExcelFile downloadCodeMatching() throws Exception{
		if(codeMatchCache == null || codeMatchCache.size() == 0)
			return null;
		ExcelFile excel = new ExcelFile();
		ExcelWorkbook doc = excel.getDocument();
		String name = "Resource Code Matching";
		excel.setFileName(name + ExcelFile.EXTENSION);
		doc.setCurrentSheetName(name);
		doc.insertRow(new String[]{"Matching Type", "Resource Code", "Object Code", "Subsidiary Code"});
		doc.setCellFontBold(0, 0, 0, 3);
		for(TransitCodeMatch codeMatch : codeMatchCache){
			doc.insertRow(new String[]{
					codeMatch.getMatchingType(),
					codeMatch.getResourceCode(),
					codeMatch.getObjectCode(),
					codeMatch.getSubsidiaryCode()
			});
		}
		doc.setColumnWidth(0, 15);
		doc.setColumnWidth(1, 15);
		doc.setColumnWidth(2, 15);
		doc.setColumnWidth(3, 15);
		return excel;
	}
	
	public List<AppTransitUom> obtainTransitUomMatcheList(String causewayUom, String jdeUom){
		List<AppTransitUom> appTransitUomList = null;
		try {
			appTransitUomList = transitUomMatchDao.searchTransitUomMatches(causewayUom, jdeUom);
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
		Collections.sort(appTransitUomList, new Comparator<AppTransitUom>(){
			public int compare(AppTransitUom o1, AppTransitUom o2) {
				return o1.getCausewayUom().compareTo(o2.getCausewayUom());
			}
		});
		return appTransitUomList;
	}

	public ExcelFile downloadUomMatching() throws Exception{
		if(uomMatchCache == null || uomMatchCache.size() == 0)
			return null;
		ExcelFile excel = new ExcelFile();
		ExcelWorkbook doc = excel.getDocument();
		String name = "Unit Code Matching";
		excel.setFileName(name + ExcelFile.EXTENSION);
		doc.setCurrentSheetName(name);
		doc.insertRow(new String[]{"Causeway Unit Code", "JDE Unit Code"});
		doc.setCellFontBold(0, 0, 0, 1);
		for(AppTransitUom codeMatch : uomMatchCache){
			doc.insertRow(new String[]{
					codeMatch.getCausewayUom(),
					codeMatch.getJdeUom()
			});
		}
		doc.setColumnWidth(0, 15);
		doc.setColumnWidth(1, 15);
		return excel;
	}
	
	private boolean isDigits(String code){
		for(char c : code.toCharArray()){
			if(!Character.isDigit(c))
				return false;
		}
		return true;
	}
	
	private boolean isDigitsOrLetter(String code){
		for(char c : code.toCharArray()){
			if(!Character.isDigit(c) && (c<'A'||c>'Z'))
				return false;
		}
		return true;
	}
	
	private boolean isRowEmpty(String[] row){
		for(String val : row)
			if(val != null && val.length() > 0)
				return false;
		return true;
	}
	
	private boolean isHeader(String[] row){
		boolean isHeader = false;
		if(row[3] != null && row[3].length() > 0){
			isHeader = true;
		}
		if((row[4] != null && row[4].length() > 0) ||
				(row[5] != null && row[5].length() > 0) ||
				(row[6] != null && row[6].length() > 0) ||
				(row[7] != null && row[7].length() > 0)){
			isHeader = false;
		}
		return isHeader;
	}

	// Last modified: Brian Tse
	// call callJasperReport to generate TransitBQ Resource Reconciliation Report
	public ByteArrayOutputStream GenerateTransitBQResourceReconciliationJasperReport(String jobNumber)throws Exception{
		logger.info("Generating TransitBQ Resource Reconciliation JasperReport");
		// get the transit header of the job
		Transit header = transitHeaderDao.getTransitHeader(jobNumber);
		List<TransitBQResourceReconciliationReportRecordWrapper> reportWrapperList = transitResourceDao.getBQResourceTransitReportFields(header);
		if(reportWrapperList != null){
			// changed by brian on 20110208 because change to allow print after complete so won't change status if completed
			if(!Transit.TRANSIT_COMPLETED.equals(header.getStatus())){
				header.setStatus(Transit.REPORT_PRINTED);
				transitHeaderDao.saveOrUpdate(header);
			}
		}
		return callJasperReportforBQResourceReconciliationReport(jobNumber, reportWrapperList, "reconciliationReportonBQResourceTransit");
	}
	
	// Last modified: Brian Tse 
	// call callJasperReport to generate TransitBQ Master Reconciliation Report
	public ByteArrayOutputStream GenerateTransitBQMasterReconciliationJasperReport(String jobNumber)throws Exception{
		logger.info("Generating TransitBQ Master Reconciliation JasperReport");
		// get the transit header of the job
		Transit header = transitHeaderDao.getTransitHeader(jobNumber);
		List<TransitBQMasterReconciliationReportRecordWrapper> reportWrapperList = transitBqDao.getBQMasterTransitReportFields(jobNumber);
		if(reportWrapperList != null){
			// changed by brian on 20110208 because change to allow print after complete so won't change status if completed
			if(!Transit.TRANSIT_COMPLETED.equals(header.getStatus())){
				header.setStatus(Transit.REPORT_PRINTED);
				transitHeaderDao.saveOrUpdate(header);
			}
		}
		return callJasperReportforBQMasterReconciliationReport(jobNumber, reportWrapperList, "reconciliationReportonBQMasterTransit");
	}
	
	// Last modified: Brian Tse
	// Generate the TransitBQ Resource Reconciliation report by using Jasper library and return ByteArrayOutputStream
	private ByteArrayOutputStream callJasperReportforBQResourceReconciliationReport(String jobNumber, List<TransitBQResourceReconciliationReportRecordWrapper> reportWrapperList, String templateName) throws JRException, IOException{
		
		logger.info("Call Jasper Report for BQ Resource Reconciliation Report");
		
		
		HashMap<String,Object> parameters = new HashMap<String, Object>();
		parameters.put("IMAGE_PATH", FileHelper.getConfigFilePath(jasperConfig.getTemplatePath()).toUri().getPath());
		
		//return outputStream;
		try {
			return JasperReportHelper.get()	
					.setCurrentReport(reportWrapperList, FileHelper.getConfigFilePath(jasperConfig.getTemplatePath()).toUri().getPath() + templateName, parameters)
					.compileAndAddReport().exportAsPDF();		} catch (IOException e) {
			logger.info(e.getMessage());
			throw e;
		}
		
	}
	
	// Last Modified: Brian Tse
	// Generate the TransitBQ Master Reconciliation report by using Jasper library and return ByteArrayOutputStream
	private ByteArrayOutputStream callJasperReportforBQMasterReconciliationReport(String jobNumber, List<TransitBQMasterReconciliationReportRecordWrapper> reportWrapperList, String templateName) throws JRException, FileNotFoundException{
		logger.info("Call Jasper Report for BQ Master Reconciliation Report");
		
		// Calculate the remaining data member of reportRecordWrapperList
		UpdateTransitBQMasterReportReocrdField(jobNumber, reportWrapperList);
		
		HashMap<String,Object> parameters = new HashMap<String, Object>();
		parameters.put("IMAGE_PATH", FileHelper.getConfigFilePath(jasperConfig.getTemplatePath()).toUri().getPath());
		
		JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(reportWrapperList);
		
		// Locate the jasper report template and import here
		FileInputStream jrInputStream = new FileInputStream(FileHelper.getConfigFilePath(jasperConfig.getTemplatePath()).toUri().getPath() + templateName + ".jasper");

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		//filling data to Jasper Report
		JasperPrint jasperReport = JasperFillManager.fillReport(jrInputStream, parameters, beanDataSource);
		
		//Converse Jasper Report to PDF
		JasperExportManager.exportReportToPdfStream(jasperReport, outputStream);

		return outputStream;
	}

	// Last Modified: Brian Tse
	private boolean UpdateTransitBQMasterReportReocrdField(String jobNumber, List<TransitBQMasterReconciliationReportRecordWrapper> reportWrapperList){
		if(reportWrapperList == null)
			return false;
		
		for(TransitBQMasterReconciliationReportRecordWrapper wrapper:reportWrapperList){
			if(wrapper.getBillNo().trim().equals("80")){
				wrapper.setGenuineMarkup(wrapper.getSellingValue());
				wrapper.setInternalValue(wrapper.getSellingValue()!=null ?CalculationUtil.roundToBigDecimal(wrapper.getSellingValue(), 2): new BigDecimal(0));
				//wrapper.setGrossProfit(Double.valueOf(0));
				
				wrapper.setSellingValue(Double.valueOf(0));
				wrapper.seteCAValue(new BigDecimal(0));
			}
		}
		return true;
	}
	
	
	
	public List<Transit> getIncompleteTransitList() {
		try {
			return transitHeaderDao.getAllTransitHeaders(Transit.TRANSIT_COMPLETED);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*************************************** FUNCTIONS FOR PCMS**************************************************************/
	
	public Transit getTransitHeader(String jobNumber) {
		Transit result = null;
		try {
			result = transitHeaderDao.getTransitHeader(jobNumber);
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<TransitBpi> getTransitBQItems(String jobNumber) {
		List<TransitBpi> transitBpiList = null;
		try {
			transitBpiList = transitBqDao.getTransitBQItems(jobNumber, null, null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transitBpiList;
	}
	
	public List<TransitResource> searchTransitResources(String jobNumber) {
		List<TransitResource> resourceList = null;
		try {
			Transit transit = transitHeaderDao.getTransitHeader(jobNumber);
			resourceList = transitResourceDao.searchTransitResources(transit);
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
		return resourceList;
	}

	
	public Double getTransitTotalAmount(String jobNo, String type) {
		Double totalAmount = 0.0;
		if(type.equals("Selling")){
			totalAmount = transitBqDao.getTransitTotalSellingAmount(jobNo);			
		}else if(type.equals("ECA")){
			totalAmount = transitResourceDao.getTransitTotalECAAmount(jobNo);
		}
		return totalAmount;
	}
	
	public List<ResourceSummayDashboardDTO> getBQResourceGroupByObjectCode(String jobNo) {
		return transitResourceDao.getResourceGroupByObjectCode(jobNo);
	}
	
	public String confirmResourcesAndCreatePackages(String jobNumber, Boolean createPackage) {	
		logger.info("TRANSIT: confirming resources for job " + jobNumber);
		Transit header = null;
		try {
			header = transitHeaderDao.getTransitHeader(jobNumber);
		if(header == null)
			return "Please create a transit header";
		else if(Transit.TRANSIT_COMPLETED.equals(header.getStatus()))
			return "Transit for this job has already been completed";
		// modified by brian on 20110117
		// for change the error message prompted when confirming resources
//		else if(!(TransitHeader.RESOURCES_IMPORTED.equals(header.getStatus()) || TransitHeader.RESOURCES_UPDATED.equals(header.getStatus())))
//			return "Please import resources";
		else if(Transit.RESOURCES_CONFIRMED.equals(header.getStatus()) || Transit.REPORT_PRINTED.equals(header.getStatus()))
			return "Transit Resources for this job has already been confirmed.";
		try {
			transitHeaderDao.lock(header);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		//Check that there are no dummy account numbers (obj and sub codes all 0s)
		if(transitResourceDao.dummyAccountCodesExist(header))
			return "There are resources with dummy account codes (all '0's). Please update these to valid account codes before confirming resources.";
		List<TransitBpi> markupBqs = transitBqDao.getTransitBQItems(jobNumber, "80", null, null, null, null);
		for(TransitBpi markupBq : markupBqs)
			transitBqDao.delete(markupBq); //child resources are deleted too

		List<TransitBpi> bqItems = transitBqDao.getTransitBqByHeaderNoCommentLines(header);
		Set<String> accountCodes = new HashSet<String>();
		
		Map<String, String> packages = new HashMap<String, String>();
		int dscNo = 1001;
		int nscNo = 3001;
		int matNo = 6001;
		
		double totalMarkup = 0;
		
		StringBuilder errorMsg = new StringBuilder();

		for(TransitBpi bqItem : bqItems){
			double totalResourceValue = 0;
			List<TransitResource> resources = transitResourceDao.obtainTransitResourceListByTransitBQ(bqItem);
			for(TransitResource resource : resources){
				totalResourceValue += resource.getValue();
				
				if (createPackage){
					String obj = resource.getObjectCode().substring(0, 2);
					if(obj.equals("14") || obj.equals("13")){
						// test take 5 digits in resource code to group
						String resourceCode = resource.getResourceCode();
						String res = resource.getResourceCode().substring(0, resourceCode.length() >= 5 ? 5 : resourceCode.length()) + obj + resource.getSubsidiaryCode().charAt(0);
						String packageNo = packages.get(res);
						if(packageNo == null){
							if(obj.equals("13"))
								packageNo = Integer.toString(matNo++);	
							else if(resource.getSubsidiaryCode().startsWith("3"))
								packageNo = Integer.toString(nscNo++);
							else
								packageNo = Integer.toString(dscNo++);
							packages.put(res, packageNo);
						}

						// check if package number too long
						if(packageNo != null && packageNo.length() > 4)
							errorMsg.append("Package Number: " + packageNo + " is longer than 4 characters." + "<br/>");

						resource.setPackageNo(packageNo);
					}
					else
						resource.setPackageNo("0");
				}

				accountCodes.add(resource.getObjectCode() + "." + resource.getSubsidiaryCode());
			}
			//Calculate Genuine Markup
			totalMarkup += CalculationUtil.round(bqItem.getValue() - totalResourceValue, 2);
			//Set Budget Amount in BQ Item
			bqItem.setAmountBudget(CalculationUtil.roundToBigDecimal(totalResourceValue, 2));
		}
		
		for(String accountCode : accountCodes){
			String[] objSub = accountCode.split("\\.");
			if(!masterListRepository.createAccountCode(jobNumber, objSub[0], objSub[1])){
				errorMsg.append("Error creating account code: " + jobNumber + "." + accountCode + "<br/>");
			}
		}
		if(errorMsg.length() != 0)
			return errorMsg.toString();
		
		totalMarkup = RoundingUtil.round(totalMarkup, 4);
		
		//BILL 80 - Genuine Markup
		TransitBpi markupBq = new TransitBpi();
		markupBq.setTransit(header);
		markupBq.setBillNo("80");
		markupBq.setPageNo("1");
		markupBq.setItemNo("A");
		markupBq.setSequenceNo(Integer.valueOf(10001));
		markupBq.setDescription("Genuine Markup");
		markupBq.setQuantity(totalMarkup);
		markupBq.setValue(totalMarkup);
		markupBq.setSellingRate(new Double(1));
		markupBq.setAmountBudget(CalculationUtil.roundToBigDecimal(totalMarkup, 2));
		markupBq.setUnit("NO");
		TransitResource markupResource = new TransitResource();
		markupResource.setTransitBpi(markupBq);
		markupResource.setResourceNo(Integer.valueOf(1));
		markupResource.setObjectCode("199999");
		markupResource.setSubsidiaryCode("99019999");
		markupResource.setType("IC");
		markupResource.setDescription("Genuine Markup");
		markupResource.setTotalQuantity(totalMarkup);
		markupResource.setUnit("NO");
		transitBqDao.saveOrUpdate(markupBq);
		transitResourceDao.saveOrUpdate(markupResource);
		//BILL 80 - Genuine Markup Change Order
		TransitBpi markupBqCo = new TransitBpi();
		markupBqCo.setTransit(header);
		markupBqCo.setBillNo("80");
		markupBqCo.setPageNo("2");
		markupBqCo.setItemNo("A");
		markupBqCo.setSequenceNo(Integer.valueOf(10000));
		markupBqCo.setDescription("Genuine Markup of Change Order");
		markupBqCo.setUnit("NO");
		TransitResource markupResourceCo = new TransitResource();
		markupResourceCo.setTransitBpi(markupBqCo);
		markupResourceCo.setResourceNo(Integer.valueOf(1));
		markupResourceCo.setObjectCode("199999");
		markupResourceCo.setSubsidiaryCode("99019999");
		markupResourceCo.setType("IC");
		markupResourceCo.setDescription("Genuine Markup of Change Order");
		markupResourceCo.setUnit("NO");
		transitBqDao.saveOrUpdate(markupBqCo);
		transitResourceDao.saveOrUpdate(markupResourceCo);
		
		header.setStatus(Transit.RESOURCES_CONFIRMED);
		transitHeaderDao.saveOrUpdate(header); //cascades down to bq items and resources
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
		transitHeaderDao.unlock(header);
		return null;
	}
	
	public String completeTransit(String jobNumber) {
		logger.info("TRANSIT: complete transit for job " + jobNumber);
		String message = "";
		try {
			JobInfo job = jobRepository.obtainJob(jobNumber);
		job.setAllowManualInputSCWorkDone("Y");
		job.setConversionStatus("Y");
		jobRepository.updateJob(job);
		
		Transit header = transitHeaderDao.getTransitHeader(jobNumber);
		if(Transit.TRANSIT_COMPLETED.equals(header.getStatus())){
			message = "Transit for this job has already been completed"; 
			return message; 
		}
		else if(!header.getStatus().equals(Transit.REPORT_PRINTED)){
			message = "Please confirm resources and create reports before completing the transit process."; 
			return message;
		}
		try {
			transitHeaderDao.lock(header);
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
			return e1.getMessage();
		}
		
		List<TransitBpi> transitBqItems = transitBqDao.obtainTransitBQByTransitHeader(header);
		Collections.sort(transitBqItems, new Comparator<TransitBpi>(){
			public int compare(TransitBpi bq1, TransitBpi bq2) {
				int billComp = bq1.getBillNo().compareTo(bq2.getBillNo());
				if(billComp != 0)
					return billComp;
				int subBillComp = bq1.getSubBillNo() != null && bq2.getSubBillNo() != null ? bq1.getSubBillNo().compareTo(bq2.getSubBillNo()) : 
					bq1.getSubBillNo() != null ? 1 : bq2.getSubBillNo() != null ? -1 : 0;
				if(subBillComp != 0)
					return subBillComp;
				int pageComp = bq1.getPageNo() != null && bq2.getPageNo() != null ? bq1.getPageNo().compareTo(bq2.getPageNo()) : 
					bq1.getPageNo() != null ? 1 : bq2.getPageNo() != null ? -1 : 0;
				return pageComp;
			}
		});
		String billNo = "";
		String subBillNo = "";
		String pageNo = "";
		BpiBill bill = null;
		BpiPage page = null;
		Set<String> packageNos = new HashSet<String>();
		List<BpiItem> bqItems = new ArrayList<BpiItem>();
		List<BpiItemResource> resourceList = new ArrayList<BpiItemResource>();
		for(TransitBpi transitBq : transitBqItems){
			//Create bill, page if necessary
			if(!billNo.equals(transitBq.getBillNo()) || 
					(subBillNo != null && !subBillNo.equals(transitBq.getSubBillNo())) || 
					(subBillNo == null && transitBq.getSubBillNo() != null)){
				billNo = transitBq.getBillNo();
				subBillNo = transitBq.getSubBillNo();
				bill = new BpiBill();
				bill.setBillNo(billNo);
				bill.setSubBillNo(subBillNo);
				bill.setJobInfo(job);
				billDao.saveOrUpdate(bill);
			}
			if(page == null || page.getBpiBill() != bill || 
					(pageNo != null && !pageNo.equals(transitBq.getPageNo())) ||
					(pageNo == null && transitBq.getPageNo() != null)){
				pageNo = transitBq.getPageNo();
				page = new BpiPage();
				page.setPageNo(pageNo);
				page.setBpiBill(bill);
				pageDao.saveOrUpdate(page);
			}
			//create BQItem from transitBQ
			BpiItem bqItem = bqItemFromTransit(transitBq);
			bqItem.setRefJobNumber(jobNumber);
			bqItem.setBpiPage(page);
			bqItems.add(bqItem);
			
			for(TransitResource transitResource : transitResourceDao.obtainTransitResourceListByTransitBQ(transitBq)){
				BpiItemResource resource = resourceFromTransit(transitResource);
				resource.setBpiItem(bqItem);
				resource.setJobNumber(jobNumber);
				resource.setRefBillNo(billNo);
				resource.setRefSubBillNo(subBillNo);
				resource.setRefPageNo(pageNo);
				resource.setRefItemNo(bqItem.getItemNo());
				resource.setBpiItem(bqItem);
				if(resource.getPackageNo() != null && !resource.getPackageNo().equals("0"))
					packageNos.add(resource.getPackageNo());
				
				resourceList.add(resource);
			}
		}
		logger.info("saving bqItems");
		bqItemDao.saveBpiItems(bqItems);
		logger.info("saving bqItemResource");
		bqItemResourceDao.saveBpiItemResources(resourceList);
		
		for(String packageNo : packageNos){
			Subcontract scPackage = new Subcontract();
			scPackage.setJobInfo(job);
			scPackage.setPackageNo(packageNo);
			scPackage.setDescription("Subcontract " + packageNo);
			if(packageNo.startsWith("1")){
				scPackage.setSubcontractorNature("DSC");
				scPackage.setPackageType("S");
			}
			else if(packageNo.startsWith("2")){
				scPackage.setSubcontractorNature("NDSC");
				scPackage.setPackageType("S");
			}
			else if(packageNo.startsWith("3")){
				scPackage.setSubcontractorNature("NSC");
				scPackage.setPackageType("S");
			}
			else if(packageNo.startsWith("6")){
				scPackage.setSubcontractorNature("DS");
				scPackage.setPackageType("M");
			}
			scPackageDao.saveOrUpdate(scPackage);
		}

		header.setStatus(Transit.TRANSIT_COMPLETED);
		transitHeaderDao.saveOrUpdate(header);
		logger.info("Step 1: Transit has been completed.");
		
		//2. Create Account Master
		jobCostService.createAccountMasterByGroup(true, false, false, false, jobNumber);
		logger.info("Step 2: Create Account Master completes.");
		
		//3. Post Budget
		String budgetPostingError = budgetPostingService.postBudget(jobNumber, null);
		if(budgetPostingError !=null && budgetPostingError.length()>0){
			logger.info("Step 3: Post Budget ERROR: "+budgetPostingError);
			message = budgetPostingError;
			return message; 
		}
		else
			logger.info("Step 3: Post Budget completes.");
		
			//4. Generate Resource Summary
			try {
				String error = resourceSummaryService.generateResourceSummaries(jobNumber);
				if(error.length()==0)
					logger.info("Step 4: Generate Resource Summary completes.");
				else
					logger.info("Step 4: Generate Resource Summary ERROR: "+error);
			} catch (Exception e) {
				e.printStackTrace();
			}
			transitHeaderDao.unlock(header);
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		} catch (ValidateBusinessLogicException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String saveTransitResources(String jobNumber, List<TransitResource> resources) throws Exception{
		Transit header = transitHeaderDao.getTransitHeader(jobNumber);
		if(Transit.TRANSIT_COMPLETED.equals(header.getStatus()))
			return "Transit for this job has already been completed";
		StringBuilder sbError = new StringBuilder();
		//Validate 
		for(TransitResource resource : resources){
			String objectCode = resource.getObjectCode();
			String subsidiaryCode = resource.getSubsidiaryCode();
			String error = masterListRepository.validateObjectAndSubsidiaryCodes(objectCode, subsidiaryCode);
			
			String packageNo = resource.getPackageNo()!=null?resource.getPackageNo().trim(): "";
			if(packageNo.length()>0 && !"0".equals(packageNo)){
				String obj = resource.getObjectCode().substring(0, 2);
				String bpi = resource.getTransitBpi().getBillNo()+"/"+resource.getTransitBpi().getSubBillNo()+"//"+resource.getTransitBpi().getPageNo()+"/"+resource.getTransitBpi().getItemNo();
				if("13".equals(obj) || "14".equals(obj)){
					if("13".equals(obj) && !packageNo.startsWith("6")){
						sbError.append(bpi+": Package No. should start with '6' for object code '13xxx' (Material Package).");
					}
					else if("14".equals(obj) && resource.getSubsidiaryCode().startsWith("3") && !packageNo.startsWith("3")){
						sbError.append(bpi+": Package No. should start with '3' for object code '14xxx' and subsidiary code '3xxxxxxx' (NSC).");
					}
					else if("14".equals(obj) && !resource.getSubsidiaryCode().startsWith("3") && !packageNo.startsWith("1") && !packageNo.startsWith("2")){
						sbError.append(bpi+": Package No. should start with '1' or '2' for object code '14xxx' (DSC, NDSC).");
					}
				}else{
					sbError.append(bpi+ ": Package No. can be only assigned to object code '14xxx' and '13xxx'.");
				}	
			}

			
			if(error != null)
				sbError.append(error);
			
			if(resource.getDescription() == null || resource.getDescription().trim().length() == 0)
				sbError.append("Description must not be blank");
		}
		//Save if all are valid
		if(sbError.length() == 0){
			for(TransitResource resource : resources){
				/*TransitResource resourceInDb = transitResourceDao.get(resource.getId());
				resourceInDb.setObjectCode(resource.getObjectCode());
				resourceInDb.setSubsidiaryCode(resource.getSubsidiaryCode());
				resourceInDb.setDescription(resource.getDescription().trim());
				resourceInDb.setDescription(resource.getDescription().trim());*/
				
				//transitResourceDao.saveOrUpdate(resourceInDb);
				transitResourceDao.saveOrUpdate(resource);
			}
			header.setStatus(Transit.RESOURCES_UPDATED);
			transitHeaderDao.saveOrUpdate(header);
			return null;
		}
		else
			return sbError.toString();
	}

	
	public String createOrUpdateTransitHeader(String jobNo, String estimateNo, String matchingCode, boolean newJob) throws Exception{
		String error = "";

		try {
			Transit header = transitHeaderDao.getTransitHeader(jobNo);
			if(header == null){
				JobInfo job = jobRepository.obtainJob(jobNo);
				//If job doesn't exist, create it.
				if(job == null){
					job = jobRepository.createNewJob(jobNo);
					if(job == null){
						error = "There was an error creating this job. Please ensure that it already exists in the JDE system.";
						return error;
					}
				}
				else if(newJob){
					error = "This job already exists. Please open the job before continuing the transit process.";
					return error;
				}
				//If job already exists make sure there are no BQ records
				else if(billDao.billsExistUnderJob(job)){
					error = "Transit for the job has already been completed.";
					return error;
				}
				
				header = new Transit();
				header.setJobNumber(jobNo);
				header.setJobDescription(job.getDescription());
				header.setCompany(job.getCompany());
				header.setStatus(Transit.HEADER_CREATED);
			}
			else if(newJob){
				error ="This job already exists. Please open the job before continuing the transit process.";
				return error;
			}
			//make sure transit has not been completed
			else if(Transit.TRANSIT_COMPLETED.equals(header.getStatus())){
				error = "Transit for the job has already been completed.";
				return error;
			}
			//Update and save
			header.setEstimateNo(estimateNo);
			header.setMatchingCode(matchingCode);
			transitHeaderDao.insert(header);
		} catch (Exception e) {
			error = "Job header cannot be created.";
			e.printStackTrace();
		}
		return error;
	}
	/*************************************** FUNCTIONS FOR PCMS - END**************************************************************/

}
