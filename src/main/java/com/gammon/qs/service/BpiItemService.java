package com.gammon.qs.service;


import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.helper.DateHelper;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.application.exception.ValidateBusinessLogicException;
import com.gammon.qs.dao.AuditResourceSummaryHBDao;
import com.gammon.qs.dao.BpiItemHBDao;
import com.gammon.qs.dao.ResourceSummaryHBDao;
import com.gammon.qs.dao.BpiBillHBDao;
import com.gammon.qs.dao.BpiPageHBDao;
import com.gammon.qs.dao.JobInfoHBDao;
import com.gammon.qs.dao.RepackagingHBDao;
import com.gammon.qs.dao.BpiItemResourceHBDao;
import com.gammon.qs.dao.SubcontractDetailHBDao;
import com.gammon.qs.dao.PaymentCertHBDao;
import com.gammon.qs.domain.ResourceSummaryAuditCustom;
import com.gammon.qs.domain.BQLineType;
import com.gammon.qs.domain.ResourceSummary;
import com.gammon.qs.domain.BpiBill;
import com.gammon.qs.domain.BpiItem;
import com.gammon.qs.domain.BpiPage;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.BpiItemResource;
import com.gammon.qs.domain.SubcontractDetail;
import com.gammon.qs.domain.SubcontractDetailBQ;
import com.gammon.qs.domain.SubcontractDetailOA;
import com.gammon.qs.domain.SubcontractDetailVO;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.domain.PaymentCert;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.io.ExcelWorkbook;
import com.gammon.qs.io.ExcelWorkbookProcessor;
import com.gammon.qs.service.bpi.BpiItemReportGenerator;
import com.gammon.qs.service.bpi.ResourceReportGenerator;
import com.gammon.qs.service.bpi.UploadIVByExcelResponse;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.util.ComparatorUtilResource;
import com.gammon.qs.util.RoundingUtil;
import com.gammon.qs.wrapper.BQItemPaginationWrapper;
import com.gammon.qs.wrapper.BQPaginationWrapper;
import com.gammon.qs.wrapper.RepackagingPaginationWrapper;
import com.gammon.qs.wrapper.updateIVAmountByMethodThree.IVBpiItemGroupedByIDWrapper;
import com.gammon.qs.wrapper.updateIVAmountByMethodThree.IVBQItemWrapper;
import com.gammon.qs.wrapper.updateIVAmountByMethodThree.IVBpiResourceSummaryGroupedBySCWrapper;
import com.gammon.qs.wrapper.updateIVAmountByMethodThree.IVBQResourceSummaryWrapper;
import com.gammon.qs.wrapper.updateIVAmountByMethodThree.IVInputByBQItemPaginationWrapper;
import com.gammon.qs.wrapper.updateIVAmountByMethodThree.IVInputByResourcePaginationWrapper;
import com.gammon.qs.wrapper.updateIVAmountByMethodThree.IVResourceWrapper;
import com.gammon.qs.wrapper.updateIVAmountByMethodThree.IVSCDetailsWrapper;
import com.gammon.qs.wrapper.updateIVByResource.ResourceWrapper;
import com.gammon.qs.wrapper.updateIVByResource.UpdateIVByResourceUpdateWrapper;
@Service
//SpringSession workaround: change "session" to "request"
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "request")
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class BpiItemService implements Serializable {
	private static final long serialVersionUID = 6521439591143450718L;
	private final int uploadBatchSize = 100;
	private transient Logger logger = Logger.getLogger(BpiItemService.class.getName());
	@Autowired
	private transient BpiItemHBDao bqItemHBDao;
	@Autowired
	private transient ResourceSummaryHBDao bqResourceSummaryDao;
	@Autowired
	private transient JobInfoHBDao jobHBDao;
	@Autowired
	private transient BpiBillHBDao billHBDao;
	@Autowired
	private transient BpiPageHBDao pageHBDao;
	@Autowired
	private transient BpiItemResourceHBDao resourceHBDao;
	@Autowired
	private transient RepackagingHBDao repackagingEntryDao;
	@Autowired
	private transient SubcontractService packageRepository;
	@Autowired
	private transient SubcontractDetailHBDao scDetailsHBDao;
	@Autowired
	private transient TenderService tenderAnalysisRepository;
	@Autowired
	private transient MasterListService masterListRepository;
	@Autowired
	private transient ResourceSummaryService bqResourceSummaryRepositoryHB;
	@Autowired
	private transient SubcontractDetailHBDao scDetailsHBDaoImpl;
	@Autowired
	private transient PaymentCertHBDao scPaymentCertHBDao;
	@Autowired
	private AuditResourceSummaryHBDao auditDao;
	
	private ArrayList<BpiItem> cachedBQEnquiry;
	private ArrayList<BpiItemResource> cachedResourceEnquiry;
	private ArrayList<BpiItemResource> cachedResourceList;
	
	//For Update IV by BQ Item
	private ArrayList<BpiItem> listOfBQItem;
	private IVInputByBQItemPaginationWrapper ivInputByBQItemPaginationWrapper;
	
	//For Update IV by Resource
	private ArrayList<BpiItemResource> listOfResource;
	private IVInputByResourcePaginationWrapper ivInputByResourcePaginationWrapper;
	
	static final int RECORDS_PER_PAGE = 200;
	@Autowired
	private transient ExcelWorkbookProcessor excelFileProcessor;
	
	//pagination cache
	@SuppressWarnings("unused")
	private static final int PAGE_SIZE = GlobalParameter.PAGE_SIZE;

	public BpiItemService(){
		
	}

	public List<BpiItemResource> getCachedResourceList() {
		return cachedResourceList;
	}

	public void setCachedResourceList(ArrayList<BpiItemResource> cachedResourceList) {
		this.cachedResourceList = cachedResourceList;
	}
	
	public IVInputByBQItemPaginationWrapper getIvInputByBQItemPaginationWrapper() {
		return ivInputByBQItemPaginationWrapper;
	}

	public void setIvInputByBQItemPaginationWrapper(
			IVInputByBQItemPaginationWrapper ivInputByBQItemPaginationWrapper) {
		this.ivInputByBQItemPaginationWrapper = ivInputByBQItemPaginationWrapper;
	}

	public IVInputByResourcePaginationWrapper getIvInputByResourcePaginationWrapper() {
		return ivInputByResourcePaginationWrapper;
	}

	public void setIvInputByResourcePaginationWrapper(
			IVInputByResourcePaginationWrapper ivInputByResourcePaginationWrapper) {
		this.ivInputByResourcePaginationWrapper = ivInputByResourcePaginationWrapper;
	}

	public ExcelWorkbookProcessor getExcelFileProcessor() {
		return excelFileProcessor;
	}

	public void setExcelFileProcessor(ExcelWorkbookProcessor excelFileProcessor) {
		this.excelFileProcessor = excelFileProcessor;
	}
	
	public List<BpiBill> getBillListWithPagesByJob(JobInfo job) throws Exception {
		return billHBDao.getBillListWithPagesByJob(job);
	}

	public List<BpiItem> getBQItemListByBPI(String jobNumber, String billNo, String subBillNo, String sectionNo, String pageNo) throws Exception {
		return  bqItemHBDao.getBpiItemListByBP(jobNumber, billNo, subBillNo, sectionNo, pageNo);
		
	}
	
	public List<BpiItem> obtainBQItemList(String jobNumber, String billNo, String subbillNo, String pageNo, String itemNo, String bqDesc)  throws Exception{
		return bqItemHBDao.obtainBpiItem(jobNumber, billNo, subbillNo, pageNo, itemNo, bqDesc);
	}
	
	public List<BpiItemResource> obtainResources(ResourceWrapper wrapper)throws Exception{
		return resourceHBDao.obtainResources(wrapper);
	}

	public List<String> obtainUneditableResources(JobInfo job) throws Exception {
		List<String> uneditableResourceIDs = new ArrayList<String>(); 

		List<BpiItemResource> resources = resourceHBDao.obtainSCResources(job.getJobNumber());
		
		List<String> uneditablePackageNos = new ArrayList<String>(); 
		List<String> awardedPackageNos = packageRepository.getUneditablePackageNos(job);
		if(awardedPackageNos!=null && awardedPackageNos.size()>0)
			uneditablePackageNos.addAll(awardedPackageNos);
		
		List<String> unawardedPackageNosUnderRequisition = packageRepository.obtainSCPackageNosUnderPaymentRequisition(job.getJobNumber());
		
		for(BpiItemResource resource: resources){
			if(resource.getPackageNo()!=null && !"".equals(resource.getPackageNo())){
				if(!uneditablePackageNos.contains(resource.getPackageNo()) && unawardedPackageNosUnderRequisition.contains(resource.getPackageNo())){
					String billItem = "";
					billItem += resource.getRefBillNo() == null ? "/" : resource.getRefBillNo() + "/";
					billItem += resource.getRefSubBillNo() == null ? "/" : resource.getRefSubBillNo() + "/";
					billItem += resource.getRefSectionNo() == null ? "/" : resource.getRefSectionNo() + "/";
					billItem += resource.getRefPageNo() == null ? "/" : resource.getRefPageNo() + "/";
					billItem += resource.getRefItemNo() == null ? "" : resource.getRefItemNo();

					SubcontractDetail scDetail = scDetailsHBDaoImpl.obtainSCDetailsByBQItem(job.getJobNumber(), resource.getPackageNo(), billItem, 
							resource.getObjectCode(), resource.getSubsidiaryCode(), 
							resource.getResourceNo());

					if(scDetail!=null && (scDetail.getPostedCertifiedQuantity()!=0.0 || scDetail.getPostedWorkDoneQuantity()!=0.0 || scDetail.getCumWorkDoneQuantity()!=0.0)){
						uneditableResourceIDs.add(String.valueOf(resource.getId()));
					}
				}
			}
		}

		return uneditableResourceIDs;
	}
	
	/**
	 * @author koeyyeung
	 * modified on 6 Aug, 2013
	 * **/
	public BQPaginationWrapper obtainResourcesByWrapper(ResourceWrapper wrapper)  throws Exception {
		logger.info("obtainResourcesByWrapper - STARTED");
		cachedResourceEnquiry = new ArrayList<BpiItemResource>();
		cachedResourceEnquiry.addAll(obtainResources(wrapper));
		if (cachedResourceEnquiry == null || cachedResourceEnquiry.size()<1)
			return null;
		
		return obtainResourcesByPage(0);
	}

	public BQPaginationWrapper obtainResourcesByPage(int pageNum){
		BQPaginationWrapper wrapper = new BQPaginationWrapper();
		wrapper.setCurrentPage(pageNum);
		int size = cachedResourceEnquiry.size();
		wrapper.setTotalRecords(size);
		wrapper.setTotalPage((size + RECORDS_PER_PAGE - 1)/RECORDS_PER_PAGE);
		int fromInd = pageNum * RECORDS_PER_PAGE;
		int toInd = (pageNum + 1) * RECORDS_PER_PAGE;
		if(toInd > cachedResourceEnquiry.size())
			toInd = cachedResourceEnquiry.size();
		ArrayList<BpiItemResource> resources = new ArrayList<BpiItemResource>();
		resources.addAll(cachedResourceEnquiry.subList(fromInd, toInd));
		wrapper.setCurrentPageContentList(resources);
		double totalCost = 0.0;
		for(BpiItemResource resource: cachedResourceEnquiry){
			totalCost += (resource.getQuantity()==null?0.0: resource.getQuantity())* (resource.getCostRate()==null?0.0: resource.getCostRate()) * (resource.getRemeasuredFactor()==null?0.0: resource.getRemeasuredFactor());
		}
		wrapper.setTotalCost(totalCost);
		
		logger.info("obtainResourcesByWrapper - END");
		return wrapper;
	}	
	
	/**
	 * @author koeyyeung
	 * modified on 6 Aug, 2013
	 * **/
	public BQItemPaginationWrapper obtainBQItem(String jobNumber, String billNo, String subbillNo, String pageNo, String itemNo, String bqDesc)  throws Exception {
		cachedBQEnquiry = new ArrayList<BpiItem>(obtainBQItemList(jobNumber, billNo, subbillNo, pageNo, itemNo, bqDesc));
		if (cachedBQEnquiry == null||cachedBQEnquiry.size()<1){
			return null;
		}
		return getBqItemsByPage(0);
	}


	public BQItemPaginationWrapper getBqItemsByPage(int pageNum){
		BQItemPaginationWrapper wrapper = new BQItemPaginationWrapper();
		wrapper.setCurrentPage(pageNum);
		int size = cachedBQEnquiry.size();
		wrapper.setTotalRecords(size);
		wrapper.setTotalPage((size + RECORDS_PER_PAGE - 1)/RECORDS_PER_PAGE);
		int fromInd = pageNum * RECORDS_PER_PAGE;
		int toInd = (pageNum + 1) * RECORDS_PER_PAGE;
		if(toInd > cachedBQEnquiry.size())
			toInd = cachedBQEnquiry.size();
		ArrayList<BpiItem> bqItems = new ArrayList<BpiItem>();
		bqItems.addAll(cachedBQEnquiry.subList(fromInd, toInd));
		wrapper.setCurrentPageContentList(bqItems);
		double totalSellingValue = 0.0;
		for(BpiItem curBQItem: cachedBQEnquiry){
			totalSellingValue += (curBQItem.getSellingRate()==null?0.0:curBQItem.getSellingRate()) * (curBQItem.getRemeasuredQuantity()==null?0.0:curBQItem.getRemeasuredQuantity());
		}
		wrapper.setTotalSellingValue(totalSellingValue);
		return wrapper;
	}	
	

	/**
	 * @author koeyyeung
	 * modified on 09 Aug, 2013
	 * **/
	public ExcelFile getIVResourceExcelFileByJob(ResourceWrapper resourceWrapper) throws Exception {
		List<BpiItemResource> resourceList = obtainResources(resourceWrapper);
		ResourceReportGenerator reportGenerator = new ResourceReportGenerator(resourceList, resourceWrapper.getJobNumber());
		ExcelFile excelFile = reportGenerator.generate();
		
		return excelFile;
	}
	
	/**
	 * @author koeyyeung
	 * modified on 09 Aug, 2013
	 * **/
	public ExcelFile getBQExcelFileByJob(String jobNumber, String billNo, String subBillNo, String pageNo, String itemNo, String bqDesc) throws Exception{
		List<BpiItem> bqList = obtainBQItemList(jobNumber, billNo, subBillNo, pageNo, itemNo, bqDesc);
		BpiItemReportGenerator reportGenerator = new BpiItemReportGenerator(bqList, jobNumber);
		ExcelFile excelFile = reportGenerator.generate();
		
		return excelFile;
	}

	public UploadIVByExcelResponse uploadIVByExcel(String jobNumber, String userID,  byte[] file) throws Exception {
		
		UploadIVByExcelResponse uploadIVByExcelResponse = new UploadIVByExcelResponse();
		NumberFormat formatter = new DecimalFormat("#####");
		long startWS=0;
		
		try{
			excelFileProcessor.openFile(file);
			
			List<UpdateIVByResourceUpdateWrapper> updateWrapperList = new ArrayList<UpdateIVByResourceUpdateWrapper>(excelFileProcessor.getNumRow());
			
			excelFileProcessor.readLine(20); // to dump away the header column
			for (int i=0; i<(excelFileProcessor.getNumRow())-1; i++) {
				String[] line = excelFileProcessor.readLine(20);
				UpdateIVByResourceUpdateWrapper updateWrapper = new UpdateIVByResourceUpdateWrapper();
				
				String billItem = line[0];
				
//				logger.log(Level.SEVERE,line[0]);
				
				String tempBillItemStr = billItem;
				
				for(int j = 0 ; tempBillItemStr.indexOf("/")>=0; j++)
				{
					String curStr = tempBillItemStr.substring(0, tempBillItemStr.indexOf("/"));
										
					if(j ==0)
						updateWrapper.setBill(curStr);
					else if (j ==1)
						updateWrapper.setSubBill(curStr);
					else if (j ==2)
						updateWrapper.setSection(curStr);
					else if (j ==3)
						updateWrapper.setPage(curStr);
					
					tempBillItemStr = tempBillItemStr.substring(tempBillItemStr.indexOf("/")+1, tempBillItemStr.length());
				}
				updateWrapper.setItemNo(tempBillItemStr);
				
				
				
				updateWrapper.setJobNumber(jobNumber);
				updateWrapper.setSubsidiaryCode(line[1]!=null && !"".equals(line[1].trim())? new Integer(formatter.parse(line[1].trim()).toString()):null);
				updateWrapper.setObjectCode(line[2]!=null && !"".equals(line[2].trim())? new Integer(formatter.parse(line[2].trim()).toString()):null);
				updateWrapper.setResourceNo(line[3]!=null && !"".equals(line[3].trim())? new Integer(formatter.parse(line[3].trim()).toString()):null);
				updateWrapper.setResourceType(line[4]);
				updateWrapper.setPackageNo(line[5]!=null && !"".equals(line[5].trim())? new Integer(formatter.parse(line[5].trim()).toString()):null);
				updateWrapper.setPackageNature(line[6]);
				updateWrapper.setCumulativeQty(line[7]!=null&& !"".equals(line[7].trim())?new Double(formatter.parse(line[7].trim()).toString()):new Double(0));				
				Double resourceQty = (line[11]!=null && !"".equals(line[11].trim())?new Double(line[11].trim()):new Double(0));
			
				updateWrapper.setUserId(userID);
				
				if(Math.abs(resourceQty)< Math.abs(updateWrapper.getCumulativeQty()))
				{
					logger.info("Validation Fail! resourceQty= "+resourceQty+"  CumulativeQty="+updateWrapper.getCumulativeQty());
					
					uploadIVByExcelResponse.setSuccess(false);
					uploadIVByExcelResponse.setMessage("Validation Error at row : " + (i+2) );
					return uploadIVByExcelResponse;
					
				}
				
				
				updateWrapperList.add(updateWrapper);
				
			}
			
			boolean result = true;
			startWS = System.currentTimeMillis();
			//batch update for a limited size;	
			for(int i =0; (i*/*100*/uploadBatchSize) < updateWrapperList.size(); i ++)
			{
								
				int begin = i*/*100*/uploadBatchSize;
				int end = ((i+1)*/*100*/uploadBatchSize)>updateWrapperList.size()?updateWrapperList.size():(i+1)*/*100*/uploadBatchSize;
				
				logger.info("Update form "+begin+" to "+end);
				
				
				boolean curResult = false;
				
				result = (curResult && result);
				
			}
			
			if(result){
				uploadIVByExcelResponse.setSuccess(true);
				uploadIVByExcelResponse.setNumRecordImported(updateWrapperList.size());
				uploadIVByExcelResponse.setMessage("File upload and updated successfully.");
			}
			else{
				uploadIVByExcelResponse.setSuccess(false);
				uploadIVByExcelResponse.setMessage("Update fail");
			}
			
			
		}catch(ArrayIndexOutOfBoundsException ex) {
			logger.log(Level.SEVERE, "SERVICE EXCEPTION:", ex);
			uploadIVByExcelResponse.setSuccess(false);
			uploadIVByExcelResponse.setMessage("Malformed or Corrupted File");
		}catch (org.springframework.transaction.UnexpectedRollbackException ex) {
			logger.log(Level.SEVERE, "SERVICE EXCEPTION:", ex);
			uploadIVByExcelResponse.setSuccess(false);
			uploadIVByExcelResponse.setMessage("IV Upload Fail! Try to upload it again. <br> Please raise helpdesk call when this error come up again!");
		}catch(Exception ex) {
			logger.log(Level.SEVERE, "SERVICE EXCEPTION:", ex);
			uploadIVByExcelResponse.setSuccess(false);
			uploadIVByExcelResponse.setMessage(ex.getMessage());
		}finally{
			long finishWS = System.currentTimeMillis();
			logger.info("Total spend for uploading whole IV Records: "+(finishWS-startWS)/1000);
		}
		
		
		
		return uploadIVByExcelResponse;
	}
	
	public RepackagingPaginationWrapper<BpiItem> searchBQItemsByPage(String jobNumber, 
			String billNo, String subBillNo, String pageNo,	String itemNo, String description, 
			int pageNum) throws Exception{
//		long start = System.nanoTime();
		RepackagingPaginationWrapper<BpiItem> wrapper = bqItemHBDao.searchBpiItemsByRefByPage(jobNumber, 
				billNo, subBillNo, pageNo, itemNo, description, pageNum);
//		logger.info("Time taken for searchBQItemsByPage: " + (System.nanoTime() - start));
		return wrapper;
	}
	
	public String updateBQItemQuantities(JobInfo job, List<BpiItem> bqItems) throws Exception{
		logger.info("Updating BQ Items (Remeasurement)");
		Long repackagingEntryId = repackagingEntryDao.getIdOfLatestRepackagingEntry(job);
		for(BpiItem bqItem : bqItems){
			BpiItem bqItemDB = bqItemHBDao.get(bqItem.getId());
			Double remeasuredFactor = bqItem.getRemeasuredQuantity()/bqItemDB.getQuantity();
			List<BpiItemResource> resources = resourceHBDao.getResourcesByBpiItem(bqItemDB);
			//Validate - make sure resources can be updated (not tied to SC Details that are locked)
			for(BpiItemResource resource : resources){
				if(resource.getPackageNo() != null && !"0".equals(resource.getPackageNo()) && resource.getObjectCode().startsWith("14")){
					String error = checkPackageDetailsCanBeEdited(resource);
					if(error != null){
						String bpi = "";
						bpi += bqItemDB.getRefBillNo() == null ? "/" : bqItemDB.getRefBillNo() + "/";
						bpi += bqItemDB.getRefSubBillNo() == null ? "/" : bqItemDB.getRefSubBillNo() + "/";
						bpi += bqItemDB.getRefSectionNo() == null ? "/" : bqItemDB.getRefSectionNo() + "/";
						bpi += bqItemDB.getRefPageNo() == null ? "/" : bqItemDB.getRefPageNo() + "/";
						bpi += bqItemDB.getItemNo() == null ? "" : bqItemDB.getItemNo();
						return "BQ Item " + bpi + " could not be updated:<br/>" + error;
					}
				}
			}
			//Update
			for(BpiItemResource resource : resources){
				resource.setRemeasuredFactor(remeasuredFactor);
				if(resource.getPackageNo() != null && !"0".equals(resource.getPackageNo()) && resource.getObjectCode().startsWith("14")){
					updateTaOrScDetails(resource);
				}
			}
			
			ResourceSummaryAuditCustom audit = new ResourceSummaryAuditCustom();
			audit.setResourceSummaryId(bqItemDB.getId());
			audit.setDataType(ResourceSummaryAuditCustom.TYPE_BQ_ITEM);
			audit.setRepackagingId(repackagingEntryId);
			audit.setActionType(ResourceSummaryAuditCustom.ACTION_UPDATE);
			audit.setValueType(ResourceSummaryAuditCustom.VALUE_RE_QTY);
			audit.setValueFrom(bqItemDB.getRemeasuredQuantity().toString());
			audit.setValueTo(bqItem.getRemeasuredQuantity().toString());
			auditDao.saveOrUpdate(audit);
			bqItemDB.setRemeasuredQuantity(bqItem.getRemeasuredQuantity());
			bqItemHBDao.saveOrUpdate(bqItemDB); //save is cascaded to resources
		}
		return null;
	}
	
	public RepackagingPaginationWrapper<BpiItemResource> searchResourcesByPage(String jobNumber, String billNo, 
			String subBillNo, String pageNo, String itemNo, String packageNo, String objectCode, 
			String subsidiaryCode, String description, int pageNum) throws Exception{
		long start = System.nanoTime();
		RepackagingPaginationWrapper<BpiItemResource> wrapper = resourceHBDao.searchResourcesByPage(jobNumber, 
				billNo, subBillNo, pageNo, itemNo, packageNo, objectCode, subsidiaryCode, description, pageNum);
		logger.info("Time taken for searchBQItemsByPage: " + (System.nanoTime() - start));
		return wrapper;
	}
	
	public List<BpiItemResource> getResourcesByBqItemId(Long id) throws Exception{
		return resourceHBDao.getResourcesByBpiItemId(id);
	}
	
	public String saveResourceUpdates(List<BpiItemResource> resources) throws Exception{
		return saveResourceUpdates(resources, true, true);
	}
	
	private String saveResourceUpdates(List<BpiItemResource> resources, boolean doValidate, boolean doSplit) throws Exception{
		logger.info("Saving resources");
		//validate all - return error message if there is one, otherwise continue and save
		String jobNumber = resources.get(0).getJobNumber();
		JobInfo job = jobHBDao.obtainJobInfo(jobNumber);
		if(doValidate){
			StringBuffer errorSB = new StringBuffer();
			List<String> tempPackageNos = new ArrayList<String>();
			List<String> uneditableUnawardedPackageNos = packageRepository.obtainUneditableUnawardedPackageNos(job);
			for(BpiItemResource resource: resources){
				validateResource(resource, errorSB);
				BpiItemResource resourceInDB = resource.getId() != null ? resourceHBDao.getResourceWithBpiItem(resource.getId()) : null;
				if(resourceInDB!=null){
					if(resourceInDB.getPackageNo()!=null && !"".equals(resourceInDB.getPackageNo()) && !"0".equals(resourceInDB.getPackageNo())){
						if(uneditableUnawardedPackageNos.contains(resourceInDB.getPackageNo()) && !tempPackageNos.contains(resourceInDB.getPackageNo())){
							errorSB.append("Package No."+resourceInDB.getPackageNo()+" cannot be edited. It has Payment Requisition submitted.<br/>");
							tempPackageNos.add(resourceInDB.getPackageNo());
						}else{
							String billItem = "";
							billItem += resourceInDB.getRefBillNo() == null ? "/" : resourceInDB.getRefBillNo() + "/";
							billItem += resourceInDB.getRefSubBillNo() == null ? "/" : resourceInDB.getRefSubBillNo() + "/";
							billItem += resourceInDB.getRefSectionNo() == null ? "/" : resourceInDB.getRefSectionNo() + "/";
							billItem += resourceInDB.getRefPageNo() == null ? "/" : resourceInDB.getRefPageNo() + "/";
							billItem += resourceInDB.getRefItemNo() == null ? "" : resourceInDB.getRefItemNo();

							logger.info("BillItem: "+job.getJobNumber()+" - "+ resourceInDB.getPackageNo()+ " - "+ billItem+ " - "+ 
									resourceInDB.getObjectCode()+ " - "+ resourceInDB.getSubsidiaryCode()+" - "+ 
									resourceInDB.getResourceNo());

							SubcontractDetail scDetail = scDetailsHBDaoImpl.obtainSCDetailsByBQItem(job.getJobNumber(), resourceInDB.getPackageNo(), billItem, 
									resourceInDB.getObjectCode(), resourceInDB.getSubsidiaryCode(), 
									resourceInDB.getResourceNo());

							if(scDetail!=null && (scDetail.getPostedCertifiedQuantity()!=0.0 || scDetail.getPostedWorkDoneQuantity()!=0.0 || scDetail.getCumWorkDoneQuantity()!=0.0)){
								errorSB.append("Resource "+resourceInDB.getDescription()+" cannot be edited. It is being used in Payment Requisition.<br/>");
							}
						}
					}else{///Check if the assigned Package No is able to be used or not
						if(uneditableUnawardedPackageNos.contains(resource.getPackageNo()) && !tempPackageNos.contains(resource.getPackageNo())){
							errorSB.append("Package No."+resource.getPackageNo()+" cannot be assigned to Resource "+resource.getDescription()+". It has Payment Requisition submitted.<br/>");
							tempPackageNos.add(resource.getPackageNo());
						}
					}
				}else{
					//Check if the newly added resource with the packageNo assigned is ready to be used or not
					if(uneditableUnawardedPackageNos.contains(resource.getPackageNo()) && !tempPackageNos.contains(resource.getPackageNo())){
						errorSB.append("Package No."+resource.getPackageNo()+" cannot be assigned to New Resource. It has Payment Requisition submitted.<br/>");
						tempPackageNos.add(resource.getPackageNo());
					}
				}
			}
			
			if(errorSB.length() != 0)
				return errorSB.toString();
		}
		
		Long repackagingEntryId = repackagingEntryDao.getIdOfLatestRepackagingEntry(job);
		
		//If valid, save updates
		for(BpiItemResource resource: resources){
			BpiItemResource resourceInDB = resource.getId() != null ? resourceHBDao.getResourceWithBpiItem(resource.getId()) : null;
			String newPackageNo = resource.getPackageNo();
			if(resourceInDB != null){
				String oldPackageNo = resourceInDB.getPackageNo();
				//if doSplit, create new resource if quant or rate has changed.
				if(doSplit){
					//split if quant changed
					Double splitQuantity = resourceInDB.getQuantity() - resource.getQuantity();
					if(!splitQuantity.equals(Double.valueOf(0))){
						String oldQty = resourceInDB.getQuantity().toString();
						
						BpiItemResource splitResource = new BpiItemResource(resourceInDB);
						splitResource.setQuantity(splitQuantity);
						Integer resourceNo = resourceHBDao.getNextResourceNoForBpiItem(splitResource.getBpiItem());
						splitResource.setResourceNo(resourceNo);
						resourceInDB.setQuantity(resource.getQuantity());
						resourceHBDao.saveOrUpdate(resourceInDB);
						resourceHBDao.saveOrUpdate(splitResource);
						if(!"0".equals(oldPackageNo) && resource.getObjectCode().startsWith("14")){
							updateTaOrScDetails(resourceInDB);
							updateTaOrScDetails(splitResource);
						}
						
						ResourceSummaryAuditCustom audit = new ResourceSummaryAuditCustom();
						audit.setResourceSummaryId(resourceInDB.getId());
						audit.setDataType(ResourceSummaryAuditCustom.TYPE_RESOURCE);
						audit.setRepackagingId(repackagingEntryId);
						audit.setActionType(ResourceSummaryAuditCustom.ACTION_UPDATE);
						audit.setValueType(ResourceSummaryAuditCustom.VALUE_QUANTITY);
						audit.setValueFrom(oldQty);
						audit.setValueTo(resource.getQuantity().toString());
						auditDao.saveOrUpdate(audit);
						
						ResourceSummaryAuditCustom auditSplit = new ResourceSummaryAuditCustom();
						auditSplit.setResourceSummaryId(splitResource.getId());
						auditSplit.setDataType(ResourceSummaryAuditCustom.TYPE_RESOURCE);
						auditSplit.setRepackagingId(repackagingEntryId);
						auditSplit.setActionType(ResourceSummaryAuditCustom.ACTION_SPLIT_UPDATE);
						auditSplit.setValueType(ResourceSummaryAuditCustom.VALUE_QUANTITY);
						auditSplit.setValueFrom(resourceInDB.getId().toString());
						auditDao.saveOrUpdate(auditSplit);
						continue;
					}
					//split if rate changed
					Double splitRate = resourceInDB.getCostRate() - resource.getCostRate();
					if(!splitRate.equals(Double.valueOf(0))){
						String oldRate = resourceInDB.getCostRate().toString();
						
						BpiItemResource splitResource = new BpiItemResource(resourceInDB);
						splitResource.setCostRate(splitRate);
						Integer resourceNo = resourceHBDao.getNextResourceNoForBpiItem(splitResource.getBpiItem());
						splitResource.setResourceNo(resourceNo);
						resourceInDB.setCostRate(resource.getCostRate());
						resourceHBDao.saveOrUpdate(resourceInDB);
						resourceHBDao.saveOrUpdate(splitResource);
						if(!"0".equals(oldPackageNo) && resource.getObjectCode().startsWith("14")){
							updateTaOrScDetails(resourceInDB);
							updateTaOrScDetails(splitResource);
						}
						
						ResourceSummaryAuditCustom audit = new ResourceSummaryAuditCustom();
						audit.setResourceSummaryId(resourceInDB.getId());
						audit.setDataType(ResourceSummaryAuditCustom.TYPE_RESOURCE);
						audit.setRepackagingId(repackagingEntryId);
						audit.setActionType(ResourceSummaryAuditCustom.ACTION_UPDATE);
						audit.setValueType(ResourceSummaryAuditCustom.VALUE_RATE);
						audit.setValueFrom(oldRate);
						audit.setValueTo(resource.getCostRate().toString());
						auditDao.saveOrUpdate(audit);
						
						ResourceSummaryAuditCustom auditSplit = new ResourceSummaryAuditCustom();
						auditSplit.setResourceSummaryId(splitResource.getId());
						auditSplit.setDataType(ResourceSummaryAuditCustom.TYPE_RESOURCE);
						auditSplit.setRepackagingId(repackagingEntryId);
						auditSplit.setActionType(ResourceSummaryAuditCustom.ACTION_SPLIT_UPDATE);
						auditSplit.setValueType(ResourceSummaryAuditCustom.VALUE_RATE);
						auditSplit.setValueFrom(resourceInDB.getId().toString());
						auditDao.saveOrUpdate(auditSplit);
						continue;
					}
				}
				//update fields
				if(!resourceInDB.getQuantity().equals(resource.getQuantity())){					
					ResourceSummaryAuditCustom audit = new ResourceSummaryAuditCustom();
					audit.setResourceSummaryId(resourceInDB.getId());
					audit.setDataType(ResourceSummaryAuditCustom.TYPE_RESOURCE);
					audit.setRepackagingId(repackagingEntryId);
					audit.setActionType(ResourceSummaryAuditCustom.ACTION_UPDATE);
					audit.setValueType(ResourceSummaryAuditCustom.VALUE_QUANTITY);
					audit.setValueFrom(resourceInDB.getQuantity().toString());
					audit.setValueTo(resource.getQuantity().toString());
					auditDao.saveOrUpdate(audit);
					
					resourceInDB.setQuantity(resource.getQuantity());
				}
				if(!resourceInDB.getCostRate().equals(resource.getCostRate())){					
					ResourceSummaryAuditCustom audit = new ResourceSummaryAuditCustom();
					audit.setResourceSummaryId(resourceInDB.getId());
					audit.setDataType(ResourceSummaryAuditCustom.TYPE_RESOURCE);
					audit.setRepackagingId(repackagingEntryId);
					audit.setActionType(ResourceSummaryAuditCustom.ACTION_UPDATE);
					audit.setValueType(ResourceSummaryAuditCustom.VALUE_OBJECT);
					audit.setValueFrom(resourceInDB.getCostRate().toString());
					audit.setValueTo(resource.getCostRate().toString());
					auditDao.saveOrUpdate(audit);
					
					resourceInDB.setCostRate(resource.getCostRate());
				}
				//If resource was removed from package, delete TA detail
				if(!oldPackageNo.equals(newPackageNo)){		
					ResourceSummaryAuditCustom audit = new ResourceSummaryAuditCustom();
					audit.setResourceSummaryId(resourceInDB.getId());
					audit.setDataType(ResourceSummaryAuditCustom.TYPE_RESOURCE);
					audit.setRepackagingId(repackagingEntryId);
					audit.setActionType(ResourceSummaryAuditCustom.ACTION_UPDATE);
					audit.setValueType(ResourceSummaryAuditCustom.VALUE_PACKAGE);
					audit.setValueFrom(oldPackageNo);
					audit.setValueTo(newPackageNo);
					auditDao.saveOrUpdate(audit);
					
					// modified by brian on 20110413
					// move the assigning of the new package no down after delete resource from Tender Analysis
//					resourceInDB.setPackageNo(newPackageNo);
					if(!"0".equals(oldPackageNo)){
						resourceInDB.setPackageNature(" ");
						resourceInDB.setPackageStatus(" ");
						resourceInDB.setPackageType(" ");
						tenderAnalysisRepository.deleteTADetailFromResource(resourceInDB);
					}
					// modified by brian on 20110413
					resourceInDB.setPackageNo(newPackageNo);
				}
				
				if(!resourceInDB.getObjectCode().equals(resource.getObjectCode())){					
					ResourceSummaryAuditCustom audit = new ResourceSummaryAuditCustom();
					audit.setResourceSummaryId(resourceInDB.getId());
					audit.setDataType(ResourceSummaryAuditCustom.TYPE_RESOURCE);
					audit.setRepackagingId(repackagingEntryId);
					audit.setActionType(ResourceSummaryAuditCustom.ACTION_UPDATE);
					audit.setValueType(ResourceSummaryAuditCustom.VALUE_OBJECT);
					audit.setValueFrom(resourceInDB.getObjectCode());
					audit.setValueTo(resource.getObjectCode());
					auditDao.saveOrUpdate(audit);
					
					resourceInDB.setObjectCode(resource.getObjectCode());
				}
				if(!resourceInDB.getSubsidiaryCode().equals(resource.getSubsidiaryCode())){
					ResourceSummaryAuditCustom audit = new ResourceSummaryAuditCustom();
					audit.setResourceSummaryId(resourceInDB.getId());
					audit.setDataType(ResourceSummaryAuditCustom.TYPE_RESOURCE);
					audit.setRepackagingId(repackagingEntryId);
					audit.setActionType(ResourceSummaryAuditCustom.ACTION_UPDATE);
					audit.setValueType(ResourceSummaryAuditCustom.VALUE_SUBSID);
					audit.setValueFrom(resourceInDB.getSubsidiaryCode());
					audit.setValueTo(resource.getSubsidiaryCode());
					auditDao.saveOrUpdate(audit);
					
					resourceInDB.setSubsidiaryCode(resource.getSubsidiaryCode());
				}
				if(!resourceInDB.getUnit().equals(resource.getUnit())){
					ResourceSummaryAuditCustom audit = new ResourceSummaryAuditCustom();
					audit.setResourceSummaryId(resourceInDB.getId());
					audit.setDataType(ResourceSummaryAuditCustom.TYPE_RESOURCE);
					audit.setRepackagingId(repackagingEntryId);
					audit.setActionType(ResourceSummaryAuditCustom.ACTION_UPDATE);
					audit.setValueType(ResourceSummaryAuditCustom.VALUE_UNIT);
					audit.setValueFrom(resourceInDB.getUnit());
					audit.setValueTo(resource.getUnit());
					auditDao.saveOrUpdate(audit);
					
					resourceInDB.setUnit(resource.getUnit());
				}
				if(!resource.getDescription().equals(resourceInDB.getDescription())){
					ResourceSummaryAuditCustom audit = new ResourceSummaryAuditCustom();
					audit.setResourceSummaryId(resourceInDB.getId());
					audit.setDataType(ResourceSummaryAuditCustom.TYPE_RESOURCE);
					audit.setRepackagingId(repackagingEntryId);
					audit.setActionType(ResourceSummaryAuditCustom.ACTION_UPDATE);
					audit.setValueType(ResourceSummaryAuditCustom.VALUE_DESCRIPT);
					audit.setValueFrom(resourceInDB.getDescription());
					audit.setValueTo(resource.getDescription());
					auditDao.saveOrUpdate(audit);
					
					resourceInDB.setDescription(resource.getDescription());	
				}
			}
			else{
				resourceInDB = resource;
			}
			
			if(!"0".equals(newPackageNo)){
				String nature = newPackageNo.startsWith("1") ? "DSC" : newPackageNo.startsWith("2") ? "NDSC" : "NSC";
				resourceInDB.setPackageNature(nature);
				resourceInDB.setPackageStatus("100");
				resourceInDB.setPackageType("S");
			}
			if(resourceInDB.getBpiItem() == null){ //New resource, created by split/merge or CO
				BpiItem bqItem = bqItemHBDao.getBpiItemByRef(resourceInDB.getJobNumber(), resourceInDB.getRefBillNo(),
						resourceInDB.getRefSubBillNo(), resourceInDB.getRefSectionNo(), resourceInDB.getRefPageNo(), 
						resourceInDB.getRefItemNo());
				resourceInDB.setBpiItem(bqItem);
			}
			if(resourceInDB.getResourceNo() == null || resourceInDB.getResourceNo().equals(Integer.valueOf(0))){
				Integer resourceNo = resourceHBDao.getNextResourceNoForBpiItem(resourceInDB.getBpiItem());
				resourceInDB.setResourceNo(resourceNo);
			}
			
			resourceHBDao.saveOrUpdate(resourceInDB);
			//If resource is in package, update TA detail (creates TA detail if resource is added to a package)
			if(!"0".equals(newPackageNo) && resourceInDB.getObjectCode().startsWith("14")){
				updateTaOrScDetails(resourceInDB);
			}
		}
		return null;
	}
	
	public String saveSplitMergeResources(List<BpiItemResource> oldResources, List<BpiItemResource> newResources) throws Exception{
		logger.info("Split or merge resources");
		
		StringBuffer errorSB = new StringBuffer();
		List<String> tempPackageNos = new ArrayList<String>();
		try {
			JobInfo job = jobHBDao.obtainJobInfo(oldResources.get(0).getJobNumber());
			List<String> uneditableUnawardedPackageNos = packageRepository.obtainUneditableUnawardedPackageNos(job);
			for(BpiItemResource resource: oldResources){
				BpiItemResource resourceInDB = resource.getId() != null ? resourceHBDao.getResourceWithBpiItem(resource.getId()) : null;
				
				if(resourceInDB.getPackageNo()!=null && !"".equals(resourceInDB.getPackageNo()) && !"0".equals(resourceInDB.getPackageNo())){
					if(uneditableUnawardedPackageNos.contains(resourceInDB.getPackageNo()) && !tempPackageNos.contains(resourceInDB.getPackageNo())){
						errorSB.append("Package No."+resourceInDB.getPackageNo()+" cannot be 'Split' or 'Merge'. It has Payment Requisition submitted.<br/>");
						tempPackageNos.add(resourceInDB.getPackageNo());
					}else{
						String billItem = "";
						billItem += resourceInDB.getRefBillNo() == null ? "/" : resourceInDB.getRefBillNo() + "/";
						billItem += resourceInDB.getRefSubBillNo() == null ? "/" : resourceInDB.getRefSubBillNo() + "/";
						billItem += resourceInDB.getRefSectionNo() == null ? "/" : resourceInDB.getRefSectionNo() + "/";
						billItem += resourceInDB.getRefPageNo() == null ? "/" : resourceInDB.getRefPageNo() + "/";
						billItem += resourceInDB.getRefItemNo() == null ? "" : resourceInDB.getRefItemNo();

						logger.info("BillItem: "+job.getJobNumber()+" - "+ resourceInDB.getPackageNo()+ " - "+ billItem+ " - "+ 
								resourceInDB.getObjectCode()+ " - "+ resourceInDB.getSubsidiaryCode()+" - "+ 
								resourceInDB.getResourceNo());
						
						SubcontractDetail scDetail = scDetailsHBDaoImpl.obtainSCDetailsByBQItem(job.getJobNumber(), resourceInDB.getPackageNo(), billItem, 
								resourceInDB.getObjectCode(), resourceInDB.getSubsidiaryCode(), 
								resourceInDB.getResourceNo());

						if(scDetail!=null && (scDetail.getPostedCertifiedQuantity()!=0.0 || scDetail.getPostedWorkDoneQuantity()!=0.0 || scDetail.getCumWorkDoneQuantity()!=0.0)){
							errorSB.append("Resource "+resourceInDB.getDescription()+" cannot be 'Split' or 'Merge'. It is being used in Payment Requisition.<br/>");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(errorSB.length() != 0)
			return errorSB.toString();
		
		String error = saveResourceUpdates(newResources, true, false);
		//if new resources were saved successfully (error == null), delete/inactivate oldResources
		if(error == null){
			String jobNumber = newResources.get(0).getJobNumber();
			JobInfo job = jobHBDao.obtainJobInfo(jobNumber);
			Long repackagingEntryId = repackagingEntryDao.getIdOfLatestRepackagingEntry(job);
			
			//Delete TA details and inactivate old records
			for(BpiItemResource resource : oldResources){
				BpiItemResource resourceInDB = resourceHBDao.get(resource.getId());
				//If resource was in package, delete TA detail
				if(resourceInDB.getPackageNo() != null && !"0".equals(resourceInDB.getPackageNo()))
					tenderAnalysisRepository.deleteTADetailFromResource(resourceInDB);
//				resourceHBDao.delete(resource);
				resourceInDB.inactivate();
				resourceHBDao.saveOrUpdate(resourceInDB);
				
				//AUDIT old resources
				ResourceSummaryAuditCustom audit = new ResourceSummaryAuditCustom();
				audit.setResourceSummaryId(resource.getId());
				audit.setDataType(ResourceSummaryAuditCustom.TYPE_RESOURCE);
				audit.setRepackagingId(repackagingEntryId);
				if(oldResources.size() == 1)
					audit.setActionType(ResourceSummaryAuditCustom.ACTION_DELETE_SPLIT);
				else{
					audit.setActionType(ResourceSummaryAuditCustom.ACTION_DELETE_MERGE);
					audit.setValueTo(newResources.get(0).getId().toString());
				}
				auditDao.saveOrUpdate(audit);
			}
			//AUDIT new resources
			for(BpiItemResource resource : newResources){
				ResourceSummaryAuditCustom audit = new ResourceSummaryAuditCustom();
				audit.setResourceSummaryId(resource.getId());
				audit.setDataType(ResourceSummaryAuditCustom.TYPE_RESOURCE);
				audit.setRepackagingId(repackagingEntryId);
				if(oldResources.size() == 1){
					audit.setActionType(ResourceSummaryAuditCustom.ACTION_ADD_FROM_SPLIT);
					audit.setValueFrom(oldResources.get(0).getId().toString());
				}
				else
					audit.setActionType(ResourceSummaryAuditCustom.ACTION_ADD_FROM_MERGE);
				auditDao.saveOrUpdate(audit);
			}
			
		}
		return error;
	}
	
	public String saveBalancedResources(List<BpiItemResource> resources, Double remeasuredQuantity) throws Exception{
		logger.info("Saving balanced resources");
		JobInfo job = jobHBDao.obtainJobInfo(resources.get(0).getJobNumber());
		Long repackagingEntryId = repackagingEntryDao.getIdOfLatestRepackagingEntry(job);
		
		StringBuilder sb = new StringBuilder();
		List<BpiItemResource> scResourcesToUpdate = new ArrayList<BpiItemResource>();
		//go through resource to verify that quant can be updated (i.e. resource is not tied to a SC Detail that is locked)
		for(BpiItemResource resource : resources){
			if(resource.getId() != null){
				if(resource.getPackageNo() != null && !"0".equals(resource.getPackageNo()) && resource.getObjectCode().startsWith("14")){
					BpiItemResource resourceInDb = resourceHBDao.get(resource.getId());
					double oldQuant = resourceInDb.getQuantity() * resourceInDb.getRemeasuredFactor();
					double newQuant = resource.getQuantity() * resource.getRemeasuredFactor();
					if(RoundingUtil.round(oldQuant, 4) != RoundingUtil.round(newQuant, 4)){
						String error = checkPackageDetailsCanBeEdited(resource);
						if(error != null){
							sb.append(error);
							continue;
						}
						scResourcesToUpdate.add(resourceInDb);
					}
				}
			}
			else{
				//new 'Balance' resource - set bqItem and resourceNo
				BpiItem bqItem = bqItemHBDao.getBpiItemByRef(resource.getJobNumber(), resource.getRefBillNo(),
						resource.getRefSubBillNo(), resource.getRefSectionNo(), resource.getRefPageNo(), 
						resource.getRefItemNo());
				resource.setBpiItem(bqItem);
				Integer resourceNo = resourceHBDao.getNextResourceNoForBpiItem(resource.getBpiItem());
				resource.setResourceNo(resourceNo);
			}
		}
		//Check if there were any errors
		if(sb.length() != 0){
			return sb.toString();
		}
		//iterate through again and save
		for(BpiItemResource resource : resources){
			if(resource.getId() != null){
				BpiItemResource resourceInDb = resourceHBDao.get(resource.getId());
				
				ResourceSummaryAuditCustom audit = new ResourceSummaryAuditCustom();
				audit.setResourceSummaryId(resource.getId());
				audit.setDataType(ResourceSummaryAuditCustom.TYPE_RESOURCE);
				audit.setRepackagingId(repackagingEntryId);
				audit.setActionType(ResourceSummaryAuditCustom.ACTION_UPDATE);
				audit.setValueType(ResourceSummaryAuditCustom.VALUE_QUANTITY);
				audit.setValueFrom(resourceInDb.getQuantity().toString());
				audit.setValueTo(resource.getQuantity().toString());
				auditDao.saveOrUpdate(audit);
				
				resourceInDb.setRemeasuredFactor(resource.getRemeasuredFactor());
				resourceInDb.setQuantity(resource.getQuantity());
				resourceHBDao.saveOrUpdate(resourceInDb);
			}
			else{
				resourceHBDao.saveOrUpdate(resource);
				
				ResourceSummaryAuditCustom audit = new ResourceSummaryAuditCustom();
				audit.setResourceSummaryId(resource.getId());
				audit.setDataType(ResourceSummaryAuditCustom.TYPE_RESOURCE);
				audit.setRepackagingId(repackagingEntryId);
				audit.setActionType(ResourceSummaryAuditCustom.ACTION_ADD);
				auditDao.saveOrUpdate(audit);
			}
		}
		for(BpiItemResource resource : scResourcesToUpdate)
			updateTaOrScDetails(resource);
		
		if(remeasuredQuantity != null){
			BpiItemResource resource = resources.get(0);
			BpiItem bqItem = bqItemHBDao.getBpiItemByRef(resource.getJobNumber(), resource.getRefBillNo(),
					resource.getRefSubBillNo(), resource.getRefSectionNo(), resource.getRefPageNo(), 
					resource.getRefItemNo());
			
			ResourceSummaryAuditCustom audit = new ResourceSummaryAuditCustom();
			audit.setResourceSummaryId(bqItem.getId());
			audit.setDataType(ResourceSummaryAuditCustom.TYPE_BQ_ITEM);
			audit.setRepackagingId(repackagingEntryId);
			audit.setActionType(ResourceSummaryAuditCustom.ACTION_UPDATE);
			audit.setValueType(ResourceSummaryAuditCustom.VALUE_RE_QTY);
			audit.setValueFrom(bqItem.getRemeasuredQuantity().toString());
			audit.setValueTo(remeasuredQuantity.toString());
			auditDao.saveOrUpdate(audit);
			
			bqItem.setRemeasuredQuantity(remeasuredQuantity);
			bqItemHBDao.saveOrUpdate(bqItem);
		}
		return null;
	}
	
	public String saveResourceSubcontractAddendums(List<BpiItemResource> resources) throws Exception{
		logger.info("Saving sc addendums");
		//Lists of things to save later (validate and prepare everything before saving)
		List<BpiItemResource> resourcesDb = new ArrayList<BpiItemResource>(resources.size());
		List<SubcontractDetail> scDetailsToDelete = new ArrayList<SubcontractDetail>();
		List<SubcontractDetail> scDetailsToUpdate = new ArrayList<SubcontractDetail>();
		List<BpiItemResource> taResourcesToDelete = new ArrayList<BpiItemResource>();
		List<ResourceSummaryAuditCustom> audits = new ArrayList<ResourceSummaryAuditCustom>();
		Map<String, Integer> sequenceNos = new HashMap<String, Integer>(); //map to keep track of sequence nos (PackageNo -> SequenceNo)
		
		JobInfo job = jobHBDao.obtainJobInfo(resources.get(0).getJobNumber());
		Long repackagingEntryId = repackagingEntryDao.getIdOfLatestRepackagingEntry(job);

		for(BpiItemResource resource : resources){
			logger.info("PackageNo: "+resource.getPackageNo()+" - bpi: "+resource.getRefBillNo()+resource.getRefPageNo()+resource.getRefItemNo());
			//Get Resource from db
			BpiItemResource resourceInDb = resourceHBDao.get(resource.getId());
			if(!resourceInDb.getPackageNo().equals(resource.getPackageNo())){
				String bpi = "";
				bpi += resourceInDb.getRefBillNo() == null ? "/" : resourceInDb.getRefBillNo() + "/";
				bpi += resourceInDb.getRefSubBillNo() == null ? "/" : resourceInDb.getRefSubBillNo() + "/";
				bpi += resourceInDb.getRefSectionNo() == null ? "/" : resourceInDb.getRefSectionNo() + "/";
				bpi += resourceInDb.getRefPageNo() == null ? "/" : resourceInDb.getRefPageNo() + "/";
				bpi += resourceInDb.getRefItemNo() == null ? "" : resourceInDb.getRefItemNo();
				//remove the old addendum
				if(!"0".equals(resourceInDb.getPackageNo())){
					Subcontract scPackage = packageRepository.obtainSCPackage(resourceInDb.getJobNumber(), resourceInDb.getPackageNo());
					//if the package is awarded, check if the corresponding scDetail can be removed or changed
					//(must have type V1 or V3, i.e. a previous addendum, and cannot have any IV)
					if(scPackage.isAwarded()){
						if(scPackage.getSubmittedAddendum() != null && scPackage.getSubmittedAddendum().trim().equals("1"))
							return "Error removing SC detail - Package: " + scPackage.getPackageNo() + ", Description: " + resourceInDb.getDescription() +
							"</br>The package has been submitted.";
						if(scPackage.getSplitTerminateStatus()!= null && scPackage.getSplitTerminateStatus().trim().equals("1") || scPackage.getSplitTerminateStatus().trim().equals("2"))
							return "Error removing SC detail - Package: " + scPackage.getPackageNo() + ", Description: " + resourceInDb.getDescription() +
							"</br>The package has been split or terminated.";
						if(scPackage.getPaymentStatus() !=null && scPackage.getPaymentStatus().trim().equals("F"))
							return "Error removing SC detail - Package: " + scPackage.getPackageNo() + ", Description: " + resourceInDb.getDescription() +
							"</br>The package's payment status is final.";
							
							
						SubcontractDetail scDetail = packageRepository.getSCDetail(scPackage, bpi, resourceInDb.getResourceNo());
						if(scDetail != null){
							if(!("V1".equals(scDetail.getLineType()) || "V3".equals(scDetail.getLineType())))
								return "Error removing SC detail - Package: " + scPackage.getPackageNo() + ", Description: " + resourceInDb.getDescription() +
								"</br>SC detail is not an addendum.";
							else if((scDetail.getCumCertifiedQuantity() != null && scDetail.getCumCertifiedQuantity().doubleValue() != 0.0)
									|| (scDetail.getPostedCertifiedQuantity() != null && scDetail.getPostedCertifiedQuantity().doubleValue() != 0.0)
									|| (scDetail.getCumWorkDoneQuantity() != null && scDetail.getCumWorkDoneQuantity().doubleValue() != 0.0)
									|| (scDetail.getPostedWorkDoneQuantity() != null && scDetail.getPostedWorkDoneQuantity().doubleValue() != 0.0))
								return "Error removing SC detail - Package: " + scPackage.getPackageNo() + ", Description: " + resourceInDb.getDescription() 
								+ "</br>Addendum has work done/certified quantity.";
							else if(SubcontractDetail.APPROVED.equals(scDetail.getApproved()))
								return "Error removing SC detail - Package: " + scPackage.getPackageNo() + ", Description: " + resourceInDb.getDescription() 
								+ "</br>Addendum has been approved.";
							else
								scDetailsToDelete.add(scDetail);
						}
					}
					else if(!Integer.valueOf(340).equals(scPackage.getSubcontractStatus()))//if non-awarded, add the resource to the taResourcesToDelete list (delete the TenderAnalysisDetail later)
						taResourcesToDelete.add(resource);
				}
				//add the new addendum
				if(!"0".equals(resource.getPackageNo())){
					Subcontract scPackage = packageRepository.obtainSCPackage(resourceInDb.getJobNumber(), resource.getPackageNo());
					if(scPackage.isAwarded()){
						if(scPackage.getSubmittedAddendum() != null && scPackage.getSubmittedAddendum().trim().equals("1"))
							return "Error adding SC detail - Package: " + scPackage.getPackageNo() + ", Description: " + resourceInDb.getDescription() +
							"</br>The package has been submitted.";
						if(scPackage.getSplitTerminateStatus()!= null && scPackage.getSplitTerminateStatus().trim().equals("1") || scPackage.getSplitTerminateStatus().trim().equals("2"))
							return "Error adding SC detail - Package: " + scPackage.getPackageNo() + ", Description: " + resourceInDb.getDescription() +
							"</br>The package has been split or terminated.";
						if(scPackage.getPaymentStatus() !=null && scPackage.getPaymentStatus().trim().equals("F"))
							return "Error adding SC detail - Package: " + scPackage.getPackageNo() + ", Description: " + resourceInDb.getDescription() +
							"</br>The package's payment status is final.";
					}
					SubcontractDetailVO scDetail = new SubcontractDetailVO();
					scDetail.setApproved(SubcontractDetail.NOT_APPROVED);
					scDetail.setSubcontract(scPackage);
					scDetail.setJobNo(resourceInDb.getJobNumber());
					scDetail.setBillItem(bpi);
					scDetail.setResourceNo(resourceInDb.getResourceNo());
					Integer sequenceNo = sequenceNos.get(resource.getPackageNo());
					if(sequenceNo == null)
						sequenceNo = packageRepository.getNextSequenceNo(scPackage);
					else
						sequenceNo++;
					scDetail.setSequenceNo(sequenceNo);
					sequenceNos.put(resource.getPackageNo(), sequenceNo);
					scDetail.setObjectCode(resourceInDb.getObjectCode());
					scDetail.setSubsidiaryCode(resourceInDb.getSubsidiaryCode());
					scDetail.setDescription(resource.getDescription());
					scDetail.setUnit(resourceInDb.getUnit());
					//By Peter Chan 2011-09-20
					scDetail.setQuantity(resourceInDb.getQuantity()*resourceInDb.getRemeasuredFactor()); //Should we set Quantity, or only toBeApprovedQuantity?
					scDetail.setToBeApprovedQuantity(resourceInDb.getQuantity()*resourceInDb.getRemeasuredFactor());
					scDetail.setCostRate(resourceInDb.getCostRate());
					scDetail.setScRate(resourceInDb.getCostRate());
					scDetail.setToBeApprovedRate(resourceInDb.getCostRate());
	
					String billNo = resourceInDb.getRefBillNo();
					//check bill no to see if bill item is a VO (CC, CL, DW, or VO)
					if("94".equals(billNo) || "95".equals(billNo) || "96".equals(billNo) || "99".equals(billNo))
						scDetail.setLineType("V1");
					else{ //have to get bqItem and check if type is OI
						BpiItem bqItem = bqItemHBDao.getBpiItemByRef(resourceInDb.getJobNumber(), resourceInDb.getRefBillNo(), resourceInDb.getRefSubBillNo(), 
								resourceInDb.getRefSectionNo(), resourceInDb.getRefPageNo(), resourceInDb.getRefItemNo());
						if("OI".equals(bqItem.getBqType()))
							scDetail.setLineType("V1");
						else
							scDetail.setLineType("V3");
					}
					
					scDetailsToUpdate.add(scDetail);
				}
			}
			//swap package nos (need the old packageno for taResourcesToDelete)
			String oldPackageNo = resourceInDb.getPackageNo();
			resourceInDb.setPackageNo(resource.getPackageNo());
			resource.setPackageNo(oldPackageNo);
			resource.setResourceNo(resourceInDb.getResourceNo());
			resourceInDb.setDescription(resource.getDescription());
			resourcesDb.add(resourceInDb);
			
			ResourceSummaryAuditCustom audit = new ResourceSummaryAuditCustom();
			audit.setRepackagingId(repackagingEntryId);
			audit.setResourceSummaryId(resourceInDb.getId());
			audit.setDataType(ResourceSummaryAuditCustom.TYPE_RESOURCE);
			audit.setActionType(ResourceSummaryAuditCustom.ACTION_UPDATE);
			audit.setValueType(ResourceSummaryAuditCustom.VALUE_PACKAGE);
			audit.setValueFrom(oldPackageNo);
			audit.setValueTo(resourceInDb.getPackageNo());
			audits.add(audit);
		}
		
		//Delete TA details
		for(BpiItemResource taResource : taResourcesToDelete)
			tenderAnalysisRepository.deleteTADetailFromResource(taResource);
		//update scDetails
		for(SubcontractDetail scDetail : scDetailsToUpdate)
			packageRepository.updateSCDetail(scDetail);
		//Inactive scDetails
		for(SubcontractDetail scDetail : scDetailsToDelete)
			packageRepository.inactivateSCDetail(scDetail);
		//Save resources
		for(BpiItemResource resource : resourcesDb)
			resourceHBDao.saveOrUpdate(resource);
		for(ResourceSummaryAuditCustom audit : audits)
			auditDao.saveOrUpdate(audit);
		
		return null; //Return null if there are no errors
	}
	
	public void validateResource(BpiItemResource resource, StringBuffer errorSB) throws Exception{
		String objectCode = resource.getObjectCode();
		String subsidiaryCode = resource.getSubsidiaryCode();
		String jobNumber = resource.getJobNumber();
		//validate account code
		if(GenericValidator.isBlankOrNull(objectCode))
			errorSB.append("Invalid object code (must not be blank)<br/>");
		else if(GenericValidator.isBlankOrNull(subsidiaryCode))
			errorSB.append("Invalid subsidiary code (must not be blank)<br/>");
		else{
			String validateError = masterListRepository.validateAndCreateAccountCode(jobNumber, objectCode, subsidiaryCode);
			if(validateError != null)
				errorSB.append(validateError);
		}
		//unit
		if(GenericValidator.isBlankOrNull(resource.getUnit()))
			errorSB.append("Invalid unit (must not be blank)<br/>");
		//resourceDescription
		if(GenericValidator.isBlankOrNull(resource.getDescription()))
			errorSB.append("Invalid resource description (must not be blank)<br/>");
	}
	
	/**
	 * 
	 * @author tikywong
	 * modified on Aug 27, 2012 11:07:26 AM
	 * More checking is added to prevent null pointer exception
	 */
	private String checkPackageDetailsCanBeEdited(BpiItemResource resource) throws Exception{
		String jobNumber = resource.getJobNumber();
		String packageNo = resource.getPackageNo();
		Subcontract scPackage = packageRepository.obtainSCPackage(jobNumber, packageNo);
		
		if(scPackage==null)
			return "Package cannot be found. Job: "+jobNumber+" Package No.: "+packageNo;
		
		if(scPackage.isAwarded()){
			if(scPackage.getSubmittedAddendum() != null && scPackage.getSubmittedAddendum().trim().equals("1"))
				return "Cannot modify resource - Package: " + scPackage.getPackageNo() + ", Description: " + resource.getDescription() +
				"</br>The package has been submitted.<br/>";
			//By Peter Chan 2011-09-20
			if(scPackage.getSplitTerminateStatus()!= null && scPackage.getSplitTerminateStatus().trim().equals("1") || scPackage.getSplitTerminateStatus().trim().equals("2") || scPackage.getSplitTerminateStatus().trim().equals("4"))
				return "Cannot modify resource - Package: " + scPackage.getPackageNo() + ", Description: " + resource.getDescription() +
				"</br>The package has been split or terminated.(Status:"+scPackage.getSplitTerminateStatus()+")<br/>";
			if(scPackage.getPaymentStatus() !=null && scPackage.getPaymentStatus().trim().equals("F"))
				return "Cannot modify resource - Package: " + scPackage.getPackageNo() + ", Description: " + resource.getDescription() +
				"</br>The package's payment status is final.<br/>";
			
			//By Peter Chan 2011-09-20
			String bpi = "";
			bpi += resource.getRefBillNo() == null ? "/" : resource.getRefBillNo() + "/";
			bpi += resource.getRefSubBillNo() == null ? "/" : resource.getRefSubBillNo() + "/";
			bpi += resource.getRefSectionNo() == null ? "/" : resource.getRefSectionNo() + "/";
			bpi += resource.getRefPageNo() == null ? "/" : resource.getRefPageNo() + "/";
			bpi += resource.getRefItemNo() == null ? "" : resource.getRefItemNo();

			SubcontractDetailBQ scDetails = (SubcontractDetailBQ) scDetailsHBDao.getSCDetail(scPackage, bpi, resource.getResourceNo());
			if(scDetails==null)
				return "SCDetail cannot be found. Job: "+jobNumber+" Package No.: "+packageNo+" BPI: "+bpi+" Resource No.: "+resource.getResourceNo()+"</br>";
			
			if (scDetails.getCumCertifiedQuantity()>resource.getQuantity()*resource.getRemeasuredFactor())
				return "Cannot modify resouce - Quantity of BPI: "+bpi+" Resource No:"+resource.getResourceNo()+" is smaller than Cumulative Certified Quantity in Package:"+packageNo+"</br>";
		}
		else if(scPackage.getSubcontractStatus()!=null && Integer.valueOf(330).equals(scPackage.getSubcontractStatus())){
			return 	"Cannot modify resource - Package: " + scPackage.getPackageNo() + ", Description: " + resource.getDescription() +
					"</br>Package has been submitted for approval (Tender Analysis).<br/>";
		}else if(scPackage.getSubcontractStatus()!=null && (Integer.valueOf(160).equals(scPackage.getSubcontractStatus()) || Integer.valueOf(340).equals(scPackage.getSubcontractStatus()))){
			PaymentCert latestPaymentCert = scPaymentCertHBDao.obtainPaymentLatestCert(jobNumber, packageNo);
			
			if(latestPaymentCert!=null &&
					(PaymentCert.PAYMENTSTATUS_SBM_SUBMITTED.equals(latestPaymentCert.getPaymentStatus())
					|| PaymentCert.PAYMENTSTATUS_PCS_WAITING_FOR_POSTING.equals(latestPaymentCert.getPaymentStatus())
					|| PaymentCert.PAYMENTSTATUS_UFR_UNDER_FINANCE_REVIEW.equals(latestPaymentCert.getPaymentStatus()))){
				
				return "Package No."+scPackage.getPackageNo()+" cannot be edited. It has Payment Requisition submitted.<br/>";
			}
			
			String billItem = "";
			billItem += resource.getRefBillNo() == null ? "/" : resource.getRefBillNo() + "/";
			billItem += resource.getRefSubBillNo() == null ? "/" : resource.getRefSubBillNo() + "/";
			billItem += resource.getRefSectionNo() == null ? "/" : resource.getRefSectionNo() + "/";
			billItem += resource.getRefPageNo() == null ? "/" : resource.getRefPageNo() + "/";
			billItem += resource.getRefItemNo() == null ? "" : resource.getRefItemNo();
			
			logger.info("BillItem: "+jobNumber+" - "+ packageNo+ " - "+ billItem+ " - "+ 
					resource.getObjectCode()+ " - "+ resource.getSubsidiaryCode()+" - "+ 
					resource.getResourceNo());

			SubcontractDetail scDetail = scDetailsHBDaoImpl.obtainSCDetailsByBQItem(jobNumber, packageNo, billItem, 
																			resource.getObjectCode(), resource.getSubsidiaryCode(), 
																			resource.getResourceNo());
			
			if(scDetail!=null && (scDetail.getPostedCertifiedQuantity()!=0.0 || scDetail.getPostedWorkDoneQuantity()!=0.0 || scDetail.getCumWorkDoneQuantity()!=0.0))
				return "Resource "+resource.getDescription()+" cannot be edited. It is being used in Payment Requisition.<br/>";
		}
		return null;
	}
	
	private void updateTaOrScDetails(BpiItemResource resource) throws Exception{
		logger.info("Updating ta/sc detail from resource");
		String jobNumber = resource.getJobNumber();
		String packageNo = resource.getPackageNo();
		Subcontract scPackage = packageRepository.obtainSCPackage(jobNumber, packageNo);
		if(scPackage == null)
			return;
		// If package is awarded, update SC details. Otherwise, update TA details (if not submitted)
		if(scPackage.isAwarded()){
			packageRepository.updateScDetailFromResource(resource, scPackage);
		}
		else if(!Integer.valueOf(330).equals(scPackage.getSubcontractStatus())){
			tenderAnalysisRepository.createOrUpdateTADetailFromResource(resource);
		}
		return;
	}
	
	public BpiItem getBillItemFieldsForChangeOrder(JobInfo job, String bqType) throws Exception{
		BpiItem bqItem = new BpiItem();
		bqItem.setRefJobNumber(job.getJobNumber());
		bqItem.setBqType(bqType);
		if("CC".equals(bqType)){
			bqItem.setRefBillNo("99");
			bqItem.setItemNo(bqItemHBDao.getNextItemNoForBill(job.getJobNumber(), "99"));
		}
		else if("CL".equals(bqType)){
			bqItem.setRefBillNo("96");
//			bqItem.setSubsidiaryCode("49019999");
			bqItem.setCostRate(Double.valueOf(1));
			bqItem.setItemNo(bqItemHBDao.getNextItemNoForBill(job.getJobNumber(), "96"));
		}
		else if("DW".equals(bqType)){
			bqItem.setRefBillNo("94");
//			bqItem.setSubsidiaryCode("49039999");
			bqItem.setItemNo(bqItemHBDao.getNextItemNoForBill(job.getJobNumber(), "94"));
		}
		else if("VO".equals(bqType)){
			bqItem.setRefBillNo("95");
//			bqItem.setSubsidiaryCode("49809999");
			bqItem.setCostRate(Double.valueOf(1));
			bqItem.setItemNo(bqItemHBDao.getNextItemNoForBill(job.getJobNumber(), "95"));
		}
		return bqItem;
	}
	
	public String validateBqItemChangeOrder(BpiItem bqItem) throws Exception{
		String error = "";
		//Validate bpi for OI (check for duplicates)
		if("OI".equals(bqItem.getBqType())){
			BpiItem bqItemInDb = bqItemHBDao.getBpiItemByRef(bqItem.getRefJobNumber(), bqItem.getRefBillNo(), 
					bqItem.getRefSubBillNo(), bqItem.getRefSectionNo(), bqItem.getRefPageNo(), bqItem.getItemNo());
			if(bqItemInDb != null && !(bqItemInDb.getId().equals(bqItem.getId())))
				error += "Duplicate b/p/i.";
		}
		//Return the error message, or null if there are no errors
		if(error.length() != 0)
			return error;
		return null; 
	}
	
	public String saveChangeOrderBqAndResources(BpiItem bqItem, List<BpiItemResource> resources) throws Exception{
		logger.info("Saving BQ Item Change Order");
		//Separate the bqItem and resources (so when we save the bqItem, it doesn't cascade and save the resources)
		
		JobInfo job = jobHBDao.obtainJobInfo(bqItem.getRefJobNumber());
		Long repackagingEntryId = repackagingEntryDao.getIdOfLatestRepackagingEntry(job);
		
		//Validate resources
		StringBuffer errorSB = new StringBuffer();
		List<String> tempPackageNos = new ArrayList<String>();
		List<String> uneditableUnawardedPackageNos = packageRepository.obtainUneditableUnawardedPackageNos(job);
		for(BpiItemResource resource: resources){
			validateResource(resource, errorSB);
			BpiItemResource resourceInDB = resource.getId() != null ? resourceHBDao.getResourceWithBpiItem(resource.getId()) : null;
			if(resourceInDB!=null){
				if(resourceInDB.getPackageNo()!=null && !"".equals(resourceInDB.getPackageNo()) && !"0".equals(resourceInDB.getPackageNo())){
					if(uneditableUnawardedPackageNos.contains(resourceInDB.getPackageNo()) && !tempPackageNos.contains(resourceInDB.getPackageNo())){
						errorSB.append("Package No."+resourceInDB.getPackageNo()+" cannot be edited. It has Payment Requisition submitted.<br/>");
						tempPackageNos.add(resourceInDB.getPackageNo());
					}else{
						String billItem = "";
						billItem += resourceInDB.getRefBillNo() == null ? "/" : resourceInDB.getRefBillNo() + "/";
						billItem += resourceInDB.getRefSubBillNo() == null ? "/" : resourceInDB.getRefSubBillNo() + "/";
						billItem += resourceInDB.getRefSectionNo() == null ? "/" : resourceInDB.getRefSectionNo() + "/";
						billItem += resourceInDB.getRefPageNo() == null ? "/" : resourceInDB.getRefPageNo() + "/";
						billItem += resourceInDB.getRefItemNo() == null ? "" : resourceInDB.getRefItemNo();

						logger.info("BillItem: "+job.getJobNumber()+" - "+ resourceInDB.getPackageNo()+ " - "+ billItem+ " - "+ 
								resourceInDB.getObjectCode()+ " - "+ resourceInDB.getSubsidiaryCode()+" - "+ 
								resourceInDB.getResourceNo());

						SubcontractDetail scDetail = scDetailsHBDaoImpl.obtainSCDetailsByBQItem(job.getJobNumber(), resourceInDB.getPackageNo(), billItem, 
								resourceInDB.getObjectCode(), resourceInDB.getSubsidiaryCode(), 
								resourceInDB.getResourceNo());

						if(scDetail!=null && (scDetail.getPostedCertifiedQuantity()!=0.0 || scDetail.getPostedWorkDoneQuantity()!=0.0 || scDetail.getCumWorkDoneQuantity()!=0.0)){
							errorSB.append("Resource "+resourceInDB.getDescription()+" cannot be edited. It is being used in Payment Requisition.<br/>");
						}
					}
				}else{///Check if the assigned Package No is able to be used or not
					if(uneditableUnawardedPackageNos.contains(resource.getPackageNo()) && !tempPackageNos.contains(resource.getPackageNo())){
						errorSB.append("Package No."+resource.getPackageNo()+" cannot be assigned to Resource "+resource.getDescription()+". It has Payment Requisition submitted.<br/>");
						tempPackageNos.add(resource.getPackageNo());
					}
				}
			}else{
				//Check if the newly added resource with the packageNo assigned is ready to be used or not
				if(uneditableUnawardedPackageNos.contains(resource.getPackageNo()) && !tempPackageNos.contains(resource.getPackageNo())){
					errorSB.append("Package No."+resource.getPackageNo()+" cannot be assigned to New Resource. It has Payment Requisition submitted.<br/>");
					tempPackageNos.add(resource.getPackageNo());
				}
			}
		}

		if(errorSB.length() != 0)
			return errorSB.toString();
		
		boolean newCo = true;
		
		//Get bqItem from db, or create a new one if it doesn't exist
		if(bqItem.getId() != null){
			newCo = false;
			//Get bqItem from db, and update
			BpiItem bqItemInDb = bqItemHBDao.get(bqItem.getId());
			bqItem.setDescription(bqItem.getDescription());
			bqItem.setCostRate(bqItem.getCostRate());
			bqItem.setSellingRate(bqItem.getSellingRate());
			bqItem.setQuantity(bqItem.getQuantity());
			bqItem.setRemeasuredQuantity(bqItem.getRemeasuredQuantity());
			bqItem.setUnit(bqItem.getUnit());
			bqItemHBDao.saveOrUpdate(bqItemInDb);
			bqItem = bqItemInDb;
		}
		else{
			//Create new BQItem
			//Check if page exists
			BpiPage page = pageHBDao.getPage(bqItem.getRefJobNumber(), bqItem.getRefBillNo(), 
					bqItem.getRefSubBillNo(), bqItem.getRefSectionNo(), bqItem.getRefPageNo());
			//If page is null, create new page
			if(page == null){
				//Check if bill exists
				BpiBill bill = billHBDao.getBill(bqItem.getRefJobNumber(), bqItem.getRefBillNo(), 
						bqItem.getRefSubBillNo(), bqItem.getRefSectionNo());
				//If bill is null, create new bill
				if(bill == null){
					bill = new BpiBill();
					bill.setJobInfo(job);
					bill.setBillNo(bqItem.getRefBillNo());
					bill.setSubBillNo(bqItem.getRefSubBillNo());
					bill.setSectionNo(bqItem.getRefSectionNo());
					logger.info("Creating new bill: " + bill.getBillNo());
					billHBDao.saveOrUpdate(bill);
				}
				page = new BpiPage();
				page.setBpiBill(bill);
				page.setPageNo(bqItem.getRefPageNo());
				logger.info("Creating new page: " + page.getPageNo());
				pageHBDao.saveOrUpdate(page);
			}
			bqItem.setBpiPage(page);
			//Ensure the item no is unique
			if(!"OI".equals(bqItem.getBqType()))
				bqItem.setItemNo(bqItemHBDao.getNextItemNoForBill(bqItem.getRefJobNumber(), bqItem.getRefBillNo()));
			logger.info("Creating new bq item: " + bqItem.getItemNo());
			bqItemHBDao.saveOrUpdate(bqItem);
			
			ResourceSummaryAuditCustom audit = new ResourceSummaryAuditCustom();
			audit.setResourceSummaryId(bqItem.getId());
			audit.setDataType(ResourceSummaryAuditCustom.TYPE_BQ_ITEM);
			audit.setRepackagingId(repackagingEntryId);
			audit.setActionType(ResourceSummaryAuditCustom.ACTION_ADD);
			auditDao.saveOrUpdate(audit);
		}
		
		//Set the resource bq item and try and save the resources
		for(BpiItemResource resource : resources){
			resource.setBpiItem(bqItem);
			resource.setRefItemNo(bqItem.getItemNo());
		}
		String error = saveResourceUpdates(resources, false, false);
		if(error == null && newCo){
			//AUDIT			
			for(BpiItemResource resource : resources){
				ResourceSummaryAuditCustom audit = new ResourceSummaryAuditCustom();
				audit.setResourceSummaryId(resource.getId());
				audit.setDataType(ResourceSummaryAuditCustom.TYPE_RESOURCE);
				audit.setRepackagingId(repackagingEntryId);
				audit.setActionType(ResourceSummaryAuditCustom.ACTION_ADD);
				auditDao.saveOrUpdate(audit);
			}
		}
		return error;
	}
	
	public void splitTerminateResourceFromScDetail(SubcontractDetail scDetail) throws Exception{
		if(scDetail.getBillItem() == null || scDetail.getResourceNo() == null || scDetail.getNewQuantity() == null)
			return;
		//Split bill item ref
		String[] bpi = scDetail.getBillItem().trim().split("/");
		if(bpi.length != 5)
			return;
		String billNo = bpi[0];
		String subBillNo = bpi[1].length() == 0 ? null : bpi[1];
		String sectionNo = bpi[2].length() == 0 ? null : bpi[2];
		String pageNo = bpi[3].length() == 0 ? null : bpi[3];
		String itemNo = bpi[4].length() == 0 ? null : bpi[4];
		
		//Get bq item and resource
		BpiItem bqItem = bqItemHBDao.getBpiItemByRef(scDetail.getJobNo(), billNo, subBillNo, sectionNo, pageNo, itemNo);
		BpiItemResource resource = resourceHBDao.getResourceByBpiItemAndResourceNo(bqItem, scDetail.getResourceNo());
		if(resource == null)
			return;
		resource.setBpiItem(bqItem);
		
		//Calculate new quants
		Double scDetailAmount = scDetail.getCostRate() * scDetail.getNewQuantity();
		Double resourceAmount = resource.getCostRate() * resource.getQuantity() * resource.getRemeasuredFactor();
		Double newResourceQuantity = scDetailAmount / (resource.getCostRate() * resource.getRemeasuredFactor());
		Double splitResourceQuantity = (resourceAmount - scDetailAmount) / (resource.getCostRate() * resource.getRemeasuredFactor());
		
		//Set resource quant
		resource.setQuantity(newResourceQuantity);
		
		//Create split resource
		BpiItemResource splitResource = new BpiItemResource(resource);
		splitResource.setQuantity(splitResourceQuantity);
		splitResource.setPackageNo("0");
		splitResource.setResourceNo(resourceHBDao.getNextResourceNoForBpiItem(bqItem));
		
/*		// Never implement
 		//update IV amount (For method 3)
		if (resource.getIvCumAmount()!=null && Math.abs(resource.getIvCumAmount().doubleValue())>=0.01 ){
			resource.setIvCumAmount(resource.getCostRate() * resource.getQuantity() * resource.getRemeasuredFactor());
			resource.setIvCumQuantity(newResourceQuantity);
			resource.setIvMovementAmount(resource.getIvCumAmount()-resource.getIvPostedAmount());
		}*/
			
		
		//Save
		resourceHBDao.saveOrUpdate(resource);
		resourceHBDao.saveOrUpdate(splitResource);		
	}
	
	public BpiItem getBqItemWithResources(Long id) throws Exception{
		return bqItemHBDao.getBpiItemWithResources(id);
	}
	
	public List<BpiItemResource> getResourcesByBpi(String jobNumber, String bpi) throws Exception{
		return resourceHBDao.getResourcesByBpi(jobNumber, bpi);
	}
	
	/**
	 * @author tikywong
	 * April 1, 2011
	 */
	public IVInputByBQItemPaginationWrapper searchBQItemsForIVInput(String jobNumber, String billNo, String subBillNo, 
														String pageNo, String itemNo, String bqDescription) 
														throws Exception {
		logger.info("STARTED -> searchBQItemsForInput()");
		ivInputByBQItemPaginationWrapper = new IVInputByBQItemPaginationWrapper();
		//HB returned records
		List<BpiItem> bqItemList = obtainBQItemList(jobNumber, billNo, subBillNo, pageNo, itemNo, bqDescription);

		//Keep the records in the application server for pagination
		listOfBQItem = new ArrayList<BpiItem>();
		
		//Remove non-BQ item -- BQ_LINE(with BQStatus), MAJOR_HEADING(no item number), COMMENT
		for(BpiItem bqItem: bqItemList){
			if(bqItem.getBQLineType()!=null && bqItem.getBQLineType().getName().equals(BQLineType.BQ_LINE))
				listOfBQItem.add(bqItem);
		}

		ivInputByBQItemPaginationWrapper.setCurrentPageContentList(listOfBQItem);
		logger.info("RETURNED BQITEM RECORDS(FULL LIST FROM HB)SIZE: "+ listOfBQItem.size());
		
		
		ivInputByBQItemPaginationWrapper.setTotalPage(listOfBQItem.size()%RECORDS_PER_PAGE==0?listOfBQItem.size()/RECORDS_PER_PAGE:listOfBQItem.size()/200+1);
		ivInputByBQItemPaginationWrapper.setCurrentPage(0);
		ivInputByBQItemPaginationWrapper.setTotalRecords(listOfBQItem.size());
		
		double totalPostedIVAmount = ivInputByBQItemPaginationWrapper.getTotalPostedIVAmount().doubleValue();
		double totalCumulativeIVAmount = ivInputByBQItemPaginationWrapper.getTotalCumulativeIVAmount().doubleValue();
		double totalIVMovementAmount = ivInputByBQItemPaginationWrapper.getTotalIVMovementAmount().doubleValue();
		
		for(BpiItem bqItem:listOfBQItem){	
			totalPostedIVAmount+=(bqItem.getIvPostedAmount()==null?0.00:bqItem.getIvPostedAmount().doubleValue());
			totalCumulativeIVAmount+=(bqItem.getIvCumAmount()==null?0.00:bqItem.getIvCumAmount().doubleValue());
			totalIVMovementAmount+=((bqItem.getIvCumAmount()==null?0.00:bqItem.getIvCumAmount().doubleValue()) - 
									(bqItem.getIvPostedAmount()==null?0.00:bqItem.getIvPostedAmount().doubleValue()));
//			logger.info("totalCumulativeIVAmount-totalPostedIVAmount="+totalCumulativeIVAmount+"-"+totalPostedIVAmount+"="+totalIVMovementAmount);
		}
		
		ivInputByBQItemPaginationWrapper.setTotalPostedIVAmount(totalPostedIVAmount);
		ivInputByBQItemPaginationWrapper.setTotalCumulativeIVAmount(totalCumulativeIVAmount);
		ivInputByBQItemPaginationWrapper.setTotalIVMovementAmount(totalIVMovementAmount);		
		
		logger.info("DONE -> searchBQItemsForInput()");
		return ivInputByBQItemPaginationWrapper;
	}
	
	/**
	 * @author tikywong
	 * April 1, 2011
	 */
	public IVInputByBQItemPaginationWrapper getBQItemsForIVInputByPage(int pageNum) throws Exception {
		logger.info("STARTED -> getBQItemsForIVInputByPage()");
		IVInputByBQItemPaginationWrapper paginationWrapper = new IVInputByBQItemPaginationWrapper();
		paginationWrapper.setTotalPage(ivInputByBQItemPaginationWrapper.getTotalPage());
		paginationWrapper.setCurrentPage(pageNum);
		paginationWrapper.setTotalCumulativeIVAmount(ivInputByBQItemPaginationWrapper.getTotalCumulativeIVAmount());
		paginationWrapper.setTotalPostedIVAmount(ivInputByBQItemPaginationWrapper.getTotalPostedIVAmount());
		paginationWrapper.setTotalIVMovementAmount(ivInputByBQItemPaginationWrapper.getTotalIVMovementAmount());
		
		int start = pageNum*RECORDS_PER_PAGE;
		int end = (pageNum*RECORDS_PER_PAGE)+RECORDS_PER_PAGE;
		
		if(listOfBQItem!=null && listOfBQItem.size()<=end)
			end = listOfBQItem.size();
		
		if(listOfBQItem==null ||listOfBQItem.size()==0){
			paginationWrapper.setCurrentPageContentList(new ArrayList<BpiItem>());
			paginationWrapper.setTotalRecords(0);
		}
		else{
			paginationWrapper.setCurrentPageContentList(new ArrayList<BpiItem>(listOfBQItem.subList(start, end)));
			paginationWrapper.setTotalRecords(paginationWrapper.getCurrentPageContentList().size());
		}
		
		logger.info("RETURNED BQITEM RECORDS(PAGINATION) SIZE: "+paginationWrapper.getCurrentPageContentList().size()+" RANGE:"+(start+1)+"-"+end);
		return paginationWrapper;
	}
	
	public List<BpiItemResource> searchResourcesByBPIDescriptionObjSubsiPackageNo(String jobNumber, String billNo, String subBillNo, String pageNo, String itemNo, String resourceDescription, String objectCode, String subsidiaryCode, String packageNo) throws Exception {
		return resourceHBDao.searchResourcesByBPIDescriptionObjSubsiPackageNo(jobNumber, billNo, subBillNo, pageNo, itemNo, resourceDescription, objectCode, subsidiaryCode, packageNo);
	}

	/**
	 * @author tikywong
	 * April 7, 2011
	 */
	public IVInputByResourcePaginationWrapper searchResourcesForIVInput(String jobNumber, String billNo, String subBillNo, 
																		String pageNo, String itemNo, String resourceDescription, 
																		String objectCode,String subsidiaryCode, String packageNo) 
																		throws Exception {
		logger.info("STARTED -> searchResourcesForIVInput()");
		ivInputByResourcePaginationWrapper = new IVInputByResourcePaginationWrapper();
		//HB returned records
		List<BpiItemResource> resourceList = searchResourcesByBPIDescriptionObjSubsiPackageNo(jobNumber, billNo, subBillNo, pageNo, itemNo, resourceDescription, objectCode, subsidiaryCode, packageNo);

		//Keep the records in the application server for pagination
		listOfResource = new ArrayList<BpiItemResource>();
		listOfResource.addAll(resourceList);

		ivInputByResourcePaginationWrapper.setCurrentPageContentList(listOfResource);
		logger.info("RETURNED RESOURCE RECORDS(FULL LIST FROM HB)SIZE: "+ listOfResource.size());

		ivInputByResourcePaginationWrapper.setTotalPage(listOfResource.size()%RECORDS_PER_PAGE==0?listOfResource.size()/RECORDS_PER_PAGE:listOfResource.size()/200+1);
		ivInputByResourcePaginationWrapper.setCurrentPage(0);
		ivInputByResourcePaginationWrapper.setTotalRecords(listOfResource.size());
		
		double totalPostedIVAmount = ivInputByResourcePaginationWrapper.getTotalPostedIVAmount().doubleValue();
		double totalCumulativeIVAmount = ivInputByResourcePaginationWrapper.getTotalCumulativeIVAmount().doubleValue();
		double totalIVMovementAmount = ivInputByResourcePaginationWrapper.getTotalIVMovementAmount().doubleValue();
		
		for(BpiItemResource resource:listOfResource){
			totalPostedIVAmount+=(resource.getIvPostedAmount()==null?0.00:resource.getIvPostedAmount().doubleValue());
			totalCumulativeIVAmount+=(resource.getIvCumAmount()==null?0.00:resource.getIvCumAmount().doubleValue());
			totalIVMovementAmount+=((resource.getIvCumAmount()==null?0.00:resource.getIvCumAmount().doubleValue()) -
									(resource.getIvPostedAmount()==null?0.00:resource.getIvPostedAmount().doubleValue()));
			
//			logger.info("totalCumulativeIVAmount-totalPostedIVAmount="+totalCumulativeIVAmount+"-"+totalPostedIVAmount+"="+totalIVMovementAmount);
		}
		
		ivInputByResourcePaginationWrapper.setTotalPostedIVAmount(totalPostedIVAmount);
		ivInputByResourcePaginationWrapper.setTotalCumulativeIVAmount(totalCumulativeIVAmount);
		ivInputByResourcePaginationWrapper.setTotalIVMovementAmount(totalIVMovementAmount);
		
		
		logger.info("DONE -> searchResourcesForIVInput()");
		return ivInputByResourcePaginationWrapper;
	}

	/**
	 * @author tikywong
	 * April 7, 2011
	 */
	public IVInputByResourcePaginationWrapper getResourcesForIVInputByPage(int pageNum) {
		logger.info("STARTED -> getResourcesForIVInputByPage()");
		IVInputByResourcePaginationWrapper paginationWrapper = new IVInputByResourcePaginationWrapper();
		paginationWrapper.setTotalPage(ivInputByResourcePaginationWrapper.getTotalPage());
		paginationWrapper.setCurrentPage(pageNum);
		paginationWrapper.setTotalCumulativeIVAmount(ivInputByResourcePaginationWrapper.getTotalCumulativeIVAmount());
		paginationWrapper.setTotalPostedIVAmount(ivInputByResourcePaginationWrapper.getTotalPostedIVAmount());
		paginationWrapper.setTotalIVMovementAmount(ivInputByResourcePaginationWrapper.getTotalIVMovementAmount());
		
		int start = pageNum*RECORDS_PER_PAGE;
		int end = (pageNum*RECORDS_PER_PAGE)+RECORDS_PER_PAGE;
		
		if(listOfResource!=null && listOfResource.size()<=end)
			end = listOfResource.size();
		
		if(listOfResource==null ||listOfResource.size()==0){
			paginationWrapper.setCurrentPageContentList(new ArrayList<BpiItemResource>());
			paginationWrapper.setTotalRecords(0);
		}
		else{
			paginationWrapper.setCurrentPageContentList(new ArrayList<BpiItemResource>(listOfResource.subList(start, end)));
			paginationWrapper.setTotalRecords(paginationWrapper.getCurrentPageContentList().size());
		}
		
		logger.info("RETURNED RESOURCE RECORDS(PAGINATION) SIZE: "+paginationWrapper.getCurrentPageContentList().size()+" RANGE:"+(start+1)+"-"+end);
		return paginationWrapper;
	}

	/**
	 * @author tikywong
	 * Apr 27, 2011 6:06:51 PM
	 */
	public Boolean updateBQItemsIVAmount(List<BpiItem> bqItems, String username) throws Exception {
		logger.info("STARTED -> updateBQItemsIVAmount()");
		String jobNumber = null;
		for(BpiItem bqItem: bqItems){
			BpiItem bqItemInDB = bqItemHBDao.get(bqItem.getId());
			
			if(bqItemInDB!=null){
				if(jobNumber==null)
					jobNumber = bqItemInDB.getRefJobNumber();
				
				IVBQItemWrapper bqItemWrapper = new IVBQItemWrapper(bqItemInDB);
				bqItemInDB = bqItemWrapper.getBqItem();
				
				bqItemWrapper.setUpdatedIVCumulativeAmount(RoundingUtil.round(bqItem.getIvCumAmount(), 2));
				bqItemWrapper.setUpdatedIVCumulativeQuantity(RoundingUtil.round(bqItem.getIvCumQty(), 2));
				
				//UPDATE
				//1.BQItem IV
				logger.info("UPDATE - J#"+bqItemInDB.getRefJobNumber()+" ID:"+bqItemInDB.getId()+
							" Updated Cumulative IV Quantity: "+bqItemWrapper.getUpdatedIVCumulativeQuantity()+
							" Updated Cumulative IV Amount: "+bqItemWrapper.getUpdatedIVCumulativeAmount());
				//2.Resources IV from BQItem
				List<IVResourceWrapper> updatedresourceWrappers = updateResourcesIVAmountByBQItem(bqItemWrapper);
				
				//3.Resource Summaries IV from Resource
				List<IVBQResourceSummaryWrapper> updatedBQResourceSummaryWrappers = bqResourceSummaryRepositoryHB.updateResourceSummariesIVAmountByResource(updatedresourceWrappers);
				
				//4.SC Details Workdone from Resource
				List<IVSCDetailsWrapper> updatedSCDetailWrappers = packageRepository.updateSCDetailsWorkdoneQuantityByResource(updatedresourceWrappers);
				
				//SAVE
				saveIVAmountForUpdateByBQItemOrResource(bqItemWrapper, updatedresourceWrappers, updatedBQResourceSummaryWrappers, updatedSCDetailWrappers, username);
			}
			else
				throw new Exception("BQITEM ID:"+bqItem.getId()+" does not exist in the database.");
		}
		
		//UPDATE & SAVE - SCPackage Total Cumulative Workdone Quantity
		if(jobNumber!=null)
			packageRepository.recalculateTotalWDAmount(jobNumber);
		
		logger.info("NUMBER OF UPDATED BQITEM RECORDS: "+bqItems.size());
		return true;
	}
	
	/**
	 * @author tikywong
	 * Apr 26, 2011 10:17:18 AM
	 * @see com.gammon.qs.service.BpiItemService#updateResourcesIVAmount(java.util.List)
	 */
	public Boolean updateResourcesIVAmount(List<BpiItemResource> resources, String username) throws Exception {
		logger.info("STARTED -> updateResourcesIVAmount()");
		String jobNumber = null;
		for(BpiItemResource resource: resources){
			BpiItemResource resourceInDB = resourceHBDao.get(resource.getId());
			
			if(resourceInDB!=null){
				if(jobNumber==null)
					jobNumber = resourceInDB.getJobNumber();
				
				IVResourceWrapper resourceWrapper = new IVResourceWrapper(resourceInDB);
				BpiItemResource updatedResource = resourceWrapper.getResource();
				
				resourceWrapper.setUpdatedIVCumQuantity(RoundingUtil.round(resource.getIvCumQuantity(), 2));
				resourceWrapper.setUpdatedIVCumAmount(RoundingUtil.round(resource.getIvCumAmount(), 2));
				resourceWrapper.setUpdatedIVMovementAmount(RoundingUtil.round(resource.getIvMovementAmount(), 2));
				
				//UPDATE
				//1.Resource IV
				logger.info("UPDATE - J#"+updatedResource.getJobNumber()+" ID:"+updatedResource.getId()+
							" Cumulative IV Quantity: "+resourceWrapper.getUpdatedIVCumQuantity()+
							" Cumulative IV Amount: "+resourceWrapper.getUpdatedIVCumAmount()+
							" IV Movement Amount: "+resourceWrapper.getUpdatedIVMovementAmount());		
				//2.BQItem IV from Resource
				IVBQItemWrapper updatedBQItemWrapper = updateBQItemIVAmountByResource(resourceWrapper);
				
				//3.Resource Summary IV from Resource
				List<IVResourceWrapper> updatedResourceWrappers = new ArrayList<IVResourceWrapper>();
				updatedResourceWrappers.add(resourceWrapper);
				List<IVBQResourceSummaryWrapper> updatedBQResourceSummaryWrappers = bqResourceSummaryRepositoryHB.updateResourceSummariesIVAmountByResource(updatedResourceWrappers);
				
				//4.SC Detail Workdone from Resource
				List<IVSCDetailsWrapper> updatedSCDetailWrappers = packageRepository.updateSCDetailsWorkdoneQuantityByResource(updatedResourceWrappers);
				
				//SAVE
				if(updatedBQItemWrapper.getBqItem()!=null){
					saveIVAmountForUpdateByBQItemOrResource(updatedBQItemWrapper, updatedResourceWrappers, updatedBQResourceSummaryWrappers, updatedSCDetailWrappers, username);
				}	
			}
			else
				throw new Exception("RESOURCE ID:"+resource.getId()+" does not exist in the database.");
		}
		
		//UPDATE & SAVE - Recalculate SCPackage Total Cumulative Workdone Quantity
		if(jobNumber!=null)
			packageRepository.recalculateTotalWDAmount(jobNumber);
		
		logger.info("NUMBER OF UPDATED RESOURCE RECORDS: "+resources.size());
		return true;
	}

	/**
	 * @author tikywong
	 * Apr 26, 2011 4:15:38 PM
	 */
	public List<IVResourceWrapper> updateResourcesIVAmountByBQItem(IVBQItemWrapper bqItemWrapper) throws Exception {
		logger.info("STARTED -> updateResourcesIVAmountByBQItem()");
		BpiItem bqItemInDB = bqItemWrapper.getBqItem();
		
		//sort the resource list to ensure the rounding amount will fall into the resource with the smallest resource number
		List<BpiItemResource> resourcesInDB = 	resourceHBDao.getResourcesByBpiItem(bqItemInDB);
		Collections.sort(resourcesInDB,new ComparatorUtilResource());
		
		List<IVResourceWrapper> updatedresourceWrappers  = new ArrayList<IVResourceWrapper>();
		
		double updatedBQCumulativeIVAmount = bqItemWrapper.getUpdatedIVCumulativeAmount();
		double totalResourceAmountOfBQItem = resourceHBDao.getTotalResourceAmountbyBPI(bqItemInDB.getRefJobNumber(), bqItemInDB.getRefBillNo(), bqItemInDB.getRefSubBillNo(), bqItemInDB.getRefPageNo(), bqItemInDB.getItemNo());
		
		String billNo = bqItemInDB.getRefBillNo();
		boolean isBill80 = billNo!=null && !billNo.trim().equals("") && billNo.trim().equals("80");
		
		//Taking care of rounding issue
		double totalUpdatedResourceCumulativeIVAmount = 0.00;
		
		for(BpiItemResource resourceInDB: resourcesInDB){
			IVResourceWrapper updatedResourceWrapper 	= new IVResourceWrapper(resourceInDB);
			resourceInDB 								= updatedResourceWrapper.getResource();
			
			//skip if resource has been deleted
			if(resourceInDB.getSystemStatus()==null || resourceInDB.getSystemStatus().equals(BpiItemResource.INACTIVE))
				continue;
			
			double resourceRate 		= resourceInDB.getCostRate();
			double resourceQuantity		= resourceInDB.getQuantity();
			//By Peter Chan 2011-09-20
			double resourceAmount 		= isBill80? resourceQuantity : (resourceRate * resourceQuantity * resourceInDB.getRemeasuredFactor());
			double totalResourceAmount	= isBill80? resourceAmount: totalResourceAmountOfBQItem * resourceInDB.getRemeasuredFactor();
			
			//IV Calculations
			double resourceCumulativeIVAmount 	= totalResourceAmount==0? 0.00 : (updatedBQCumulativeIVAmount * (resourceAmount / totalResourceAmount));
			double resourceCumulativeIVQuantity = isBill80?resourceCumulativeIVAmount:(resourceRate==0 ? 0.00 : (resourceCumulativeIVAmount / resourceRate));
			double resourceIVMovementAmount 	= resourceCumulativeIVAmount-resourceInDB.getIvPostedAmount();
			
			//Object update
			updatedResourceWrapper.setUpdatedIVCumAmount(RoundingUtil.round(resourceCumulativeIVAmount, 2));
			updatedResourceWrapper.setUpdatedIVCumQuantity(RoundingUtil.round(resourceCumulativeIVQuantity, 2));
			updatedResourceWrapper.setUpdatedIVMovementAmount(RoundingUtil.round(resourceIVMovementAmount, 2));

//			logger.info("UPDATE - J#"+resourceInDB.getJobNumber()+" ID:"+resourceInDB.getId()+
//						" Updated Cumulative IV Quantity: "+updatedResourceWrapper.getUpdatedIVCumAmount()+
//						" Updated Cumulative IV Amount: "+updatedResourceWrapper.getUpdatedIVCumAmount()+
//						" Updated IV Movement Amount: "+updatedResourceWrapper.getUpdatedIVMovementAmount());
			updatedresourceWrappers.add(updatedResourceWrapper);	
			
			//rounding calculation
			totalUpdatedResourceCumulativeIVAmount += RoundingUtil.round(resourceCumulativeIVAmount, 2);
		}
		
		//Rounding Validation
		if(updatedresourceWrappers.size()>0 && updatedBQCumulativeIVAmount!=totalUpdatedResourceCumulativeIVAmount){
			double roundingDifference = updatedBQCumulativeIVAmount-totalUpdatedResourceCumulativeIVAmount;
			
			IVResourceWrapper updatedResourceWrapper = updatedresourceWrappers.get(0);
			
			//Reference
			double resourceRate	= updatedResourceWrapper.getResource().getCostRate();
			
			//IV Calculations
			double resourceCumulativeIVAmount 	= updatedResourceWrapper.getUpdatedIVCumAmount() + roundingDifference;
			double resourceCumulativeIVQuantity = isBill80?resourceCumulativeIVAmount:(resourceRate==0 ? 0.00 : (resourceCumulativeIVAmount / resourceRate));
			double resourceIVMovementAmount 	= resourceCumulativeIVAmount-updatedResourceWrapper.getResource().getIvPostedAmount();
			
			//Object update
			updatedResourceWrapper.setUpdatedIVCumAmount(RoundingUtil.round(resourceCumulativeIVAmount, 2));
			updatedResourceWrapper.setUpdatedIVCumQuantity(RoundingUtil.round(resourceCumulativeIVQuantity, 2));
			updatedResourceWrapper.setUpdatedIVMovementAmount(RoundingUtil.round(resourceIVMovementAmount, 2));	
		}

		return updatedresourceWrappers;
	}
	
	/**
	 * @author tikywong
	 * Apr 27, 2011 6:07:39 PM
	 * @see com.gammon.qs.service.BpiItemService#updateBQItemIVAmountByResource(com.gammon.qs.wrapper.IVResourceWrapper)
	 */
	public IVBQItemWrapper updateBQItemIVAmountByResource(IVResourceWrapper resourceWrapper) throws Exception {
		logger.info("STARTED -> updateBQItemIVAmountByResource()");
		
		BpiItemResource updatedResource = resourceWrapper.getResource();
		
		//reference only
		String billNo = updatedResource.getRefBillNo();
		boolean isBill80 = billNo!=null && !billNo.trim().equals("") && billNo.trim().equals("80");
		
		double resourceRate							= updatedResource.getCostRate();
		double updatedResourceCumulativeIVAmount 	= resourceWrapper.getUpdatedIVCumAmount();
		double updatedResourceCumulativeIVQuantity 	= isBill80 ? updatedResourceCumulativeIVAmount : (resourceRate==0? 0.00 : (updatedResourceCumulativeIVAmount/resourceRate));

		double originalResourceCumulativeIVAmount = updatedResource.getIvCumAmount(); 
		double originalResourceCumulativeIVQuantity = updatedResource.getIvCumQuantity();
		
		BpiItem bqItemInDB = updatedResource.getBpiItem();
		IVBQItemWrapper bqItemWrapper = new IVBQItemWrapper(bqItemInDB);
		bqItemInDB = bqItemWrapper.getBqItem();
		
		double originalBQCumulativeIVAmount 		= bqItemInDB.getIvCumAmount();
		double originalBQCumulativeIVQuantity		= bqItemInDB.getIvCumQty();
		
		//IV Calculations
		double updatedBQCumulativeIVAmount = originalBQCumulativeIVAmount +(updatedResourceCumulativeIVAmount - originalResourceCumulativeIVAmount);
		double updatedBQCumulativeIVQuantity = originalBQCumulativeIVQuantity + (updatedResourceCumulativeIVQuantity - originalResourceCumulativeIVQuantity); 
		
		//Object update
		bqItemWrapper.setUpdatedIVCumulativeAmount(RoundingUtil.round(updatedBQCumulativeIVAmount, 2));
		bqItemWrapper.setUpdatedIVCumulativeQuantity(RoundingUtil.round(updatedBQCumulativeIVQuantity, 2));
		
//		logger.info("UPDATE - J#"+bqItemInDB.getRefJobNumber()+" ID:"+bqItemInDB.getId()+"" +
//					" Cumulative IV Quantity: "+bqItemWrapper.getUpdatedIVCumulativeQuantity()+
//					" Cumulative IV Amount: "+bqItemWrapper.getUpdatedIVCumulativeAmount());
		
		return bqItemWrapper;
	}

	/**
	 * @author tikywong
	 * Apr 26, 2011 4:17:11 PM
	 * @see com.gammon.qs.service.BpiItemService#saveIVAmountForUpdateByBQItemOrResource(com.gammon.qs.wrapper.IVBQItemWrapper, java.util.List, java.util.List, java.util.List)
	 */
	@Transactional(rollbackFor = Exception.class, value = "transactionManager")
	public Boolean saveIVAmountForUpdateByBQItemOrResource(	IVBQItemWrapper bqItemWrapper, 
															List<IVResourceWrapper> resourceWrappers,
															List<IVBQResourceSummaryWrapper> bqResourceSummaryWrappers,
															List<IVSCDetailsWrapper> scDetailWrappers,
															String username) throws Exception {
		logger.info("STARTED -> saveIVAmountForUpdateByBQItemOrResource()");
		
		if(bqItemWrapper==null || resourceWrappers==null || bqResourceSummaryWrappers==null || scDetailWrappers==null){
			logger.info("FAILED -> NULL? BQItem:"+(bqItemWrapper==null)+
						"resources:"+(resourceWrappers==null)+
						"bqResourceSummaries:"+(bqResourceSummaryWrappers==null)+
						"scDetails:"+(scDetailWrappers==null));
			return false;
		}
		
		try{
		//1.BQItem
		BpiItem bqItem = bqItemWrapper.getBqItem();
		bqItem.setIvCumAmount(bqItemWrapper.getUpdatedIVCumulativeAmount());
		bqItem.setIvCumQty(bqItemWrapper.getUpdatedIVCumulativeQuantity());
//		logger.info("SAVE - BQItem J#"+bqItem.getRefJobNumber()+" ID:"+bqItem.getId()+
//					" Cumulative IV Quantity: "+bqItem.getIvCumQty()+
//					" Cumulative IV Amount: "+bqItem.getIvCumAmount());
		//update by HQL
//		bqHBDao.updateBQItemIVAmountByHQL(bqItem, username);
		
		//2.Resources
		for(IVResourceWrapper resourceWrapper:resourceWrappers){
			BpiItemResource resource = resourceWrapper.getResource();
			resource.setIvCumAmount(resourceWrapper.getUpdatedIVCumAmount());
			resource.setIvCumQuantity(resourceWrapper.getUpdatedIVCumQuantity());
			resource.setIvMovementAmount(resourceWrapper.getUpdatedIVMovementAmount());
//			logger.info("SAVE - Resource J#"+resource.getJobNumber()+" ID:"+resource.getId()+
//						" Cumulative IV Quantity: "+resource.getIvCumQuantity()+
//						" Cumulative IV Amount: "+resource.getIvCumAmount()+
//						" IV Movement Amount: "+resource.getIvMovementAmount());
			//update by HQL
//			resourceHBDao.updateResourceIVAmountByHQL(resource, username);
		}
		
		//save by Object
//		bqHBDao.save(bqItem);
		
		//update by Object
		bqItemHBDao.update(bqItem); //updated BQItem & Resource
		
		//3.Resource Summary
		List<ResourceSummary> bqResourceSummaries = new ArrayList<ResourceSummary>();
		for(IVBQResourceSummaryWrapper bqResourceSummaryWrapper:bqResourceSummaryWrappers){
			ResourceSummary bqResourceSummary = bqResourceSummaryWrapper.getBqResourceSummary();
			bqResourceSummary.setCurrIVAmount(bqResourceSummaryWrapper.getUpdatedCurrentIVAmount());
//			logger.info("SAVE - BQResourceSummary J#"+bqResourceSummary.getJob().getJobNumber()+" ID:"+bqResourceSummary.getId()+
//						" Current IV Amount:"+bqResourceSummary.getCurrIVAmount());
			
			//save by Object
//			bqResourceSummaryRepositoryHB.getBqResourceSummaryDao().save(bqResourceSummary);
			
			//update by HQL
//			bqResourceSummaryRepositoryHB.updateBQResourceSummaryIVAmountByHQL(bqResourceSummary, username);
			
			//update by Batch
			bqResourceSummaries.add(bqResourceSummary);
		}
		//update by Batch
		bqResourceSummaryDao.updateBQResourceSummaries(bqResourceSummaries);
		
		
		//4.SC Detail
		List<SubcontractDetail> scDetails = new ArrayList<SubcontractDetail>();
		for(IVSCDetailsWrapper scDetailWrapper: scDetailWrappers){
			SubcontractDetail scDetail = scDetailWrapper.getScDetail();
			((SubcontractDetailOA) scDetail).setCumWorkDoneQuantity(scDetailWrapper.getUpdatedCumulativeWDQuantity());
//			logger.info("SAVE - SCDetail J#"+scDetail.getJobNo()+" ID:"+scDetail.getId()+
//						" Cumulative Workdone Quantity:"+scDetail.getCumWorkDoneQuantity());
			
			//save by Object
//			packageRepository.saveSCDetail(scDetail);
			
			//update by HQL
//			packageRepository.updateSCDetailWorkdoneQtybyHQL(scDetail, username);
			
			//update By Batch
			scDetails.add(scDetail);
			
		}
		//update by Batch
		scDetailsHBDao.updateSCDetails(scDetails);
		
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
//		logger.info("DONE -> saveIVAmountForUpdateByBQItemOrResource()");
		return true;
	}

	/**
	 * @author tikywong
	 * April 19, 2011
	 */
	public ExcelFile downloadBQItemIVExcel(String jobNumber, String billNo, String subBillNo, String pageNo, String itemNo, String bqDescription) throws Exception {
		logger.info("STARTED -> downloadBQItemIVExcel()");
		List<BpiItem> bqItems = obtainBQItemList(jobNumber, billNo, subBillNo, pageNo, itemNo, bqDescription);
		
		if(bqItems==null || bqItems.size()==0)
			return null;
		
		ExcelFile excel = new ExcelFile();
		ExcelWorkbook doc = excel.getDocument();
		String filename = jobNumber+" IVByBQItem "+DateHelper.formatDate(new Date())+ExcelFile.EXTENSION;
		excel.setFileName(filename);
		logger.info("J#"+jobNumber+" Creating Excel file: "+filename);
		
		//Column Headers
		String[] colHeaders = new String[14];
		colHeaders[0] = "Sequence No.";
		colHeaders[1] = "Bill Item (B/P/I)";
		colHeaders[2] = "Bill No.";
		colHeaders[3] = "SubBill No.";
		colHeaders[4] = "Page No.";
		colHeaders[5] = "Item No.";
		colHeaders[6] = "BQ Description";
		colHeaders[7] = "Remeasured Quantity";
		colHeaders[8] = "Unit";
		colHeaders[9] = "Cost Rate";
		colHeaders[10] = "Cost Amount";
		colHeaders[11] = "Posted IV Amount";
		colHeaders[12] = "Cumulative IV Amount";
		colHeaders[13] = "BQ Type";
		
		doc.insertRow(colHeaders);
		doc.setCellFontBold(0, 0, 0, 13);
		
		//Insert rows
		logger.info("Inserting rows of Excel file: "+filename);
		for(BpiItem bqItem : bqItems){
			//Remove non-BQ item -- BQ_LINE(with BQStatus), MAJOR_HEADING(no item number), COMMENT
			if(bqItem.getBQLineType()!=null && bqItem.getBQLineType().getName().equals(BQLineType.BQ_LINE)){
				String bpi = 	(bqItem.getRefBillNo()==null?"":bqItem.getRefBillNo().trim()) + "/" +
								(bqItem.getRefSubBillNo()==null?"":bqItem.getRefSubBillNo().trim()) + "/" +
								"/"+
								(bqItem.getRefPageNo()==null?"":bqItem.getRefPageNo().trim()) + "/" +
								(bqItem.getItemNo()==null?"":bqItem.getItemNo().trim());
				
				Double remeasuredQuantity = bqItem.getRemeasuredQuantity();
				Double costRate = bqItem.getCostRate();
				Double costAmount = RoundingUtil.round(remeasuredQuantity.doubleValue() * costRate.doubleValue(), 2);
				
				Double postedIVAmount = bqItem.getIvPostedAmount();
				Double cumulativeIVAmount = bqItem.getIvCumAmount();
				
				String[] row = new String[]{bqItem.getSequenceNo().toString(),
											bpi, 
											bqItem.getRefBillNo(),
											bqItem.getRefSubBillNo(),
											bqItem.getRefPageNo(),
											bqItem.getItemNo(),
											bqItem.getDescription(),
											remeasuredQuantity.toString(),
											bqItem.getUnit(),
											costRate.toString(),
											costAmount.toString(),
											postedIVAmount.toString(),
											cumulativeIVAmount.toString(),	//column 12 is editable
											bqItem.getBqType()};
				doc.insertRow(row);
				
				//set Editable Column
				doc.setCellFontEditable(1, 12, bqItems.size(), 12);
				
				//Set columns' width
				doc.setColumnWidth(0, 15);
				doc.setColumnWidth(1, 15);
				doc.setColumnWidth(2, 10);
				doc.setColumnWidth(3, 10);
				doc.setColumnWidth(4, 10);
				doc.setColumnWidth(5, 10);
				doc.setColumnWidth(6, 50);
				doc.setColumnWidth(7, 20);
				doc.setColumnWidth(8, 6);
				doc.setColumnWidth(9, 10);
				doc.setColumnWidth(10, 10);
				doc.setColumnWidth(11, 20);
				doc.setColumnWidth(12, 25);
				doc.setColumnWidth(13, 10);
			}
		}

		logger.info("DONE -> downloadBQItemIVExcel()");
		return excel;
	}

	/**
	 * @author tikywong
	 * April 20, 2011
	 */
	public ExcelFile downloadResourceIVExcel(String jobNumber, String billNo, String subBillNo, String pageNo, String itemNo, String resourceDescription, String objectCode, String subsidiaryCode, String packageNo) throws Exception {
	logger.info("STARTED -> downloadResourceIVExcel()");
		List<BpiItemResource> resourcesList = searchResourcesByBPIDescriptionObjSubsiPackageNo(jobNumber, billNo, subBillNo, pageNo, itemNo, resourceDescription, objectCode, subsidiaryCode, packageNo);

		if(resourcesList==null || resourcesList.size()==0)
			return null;
		
		ExcelFile excel = new ExcelFile();
		ExcelWorkbook doc = excel.getDocument();
		String filename = jobNumber+" IVByResource "+DateHelper.formatDate(new Date())+ExcelFile.EXTENSION;
		excel.setFileName(filename);
		logger.info("J#"+jobNumber+" Creating Excel file: "+filename);
		
		//Column Headers
		String[] colHeaders = new String[12];
		colHeaders[0] = "Bill Item (B/P/I)";
		colHeaders[1] = "Object Code";
		colHeaders[2] = "Subsidiary Code";
		colHeaders[3] = "Resource No.";
		colHeaders[4] = "Package No.";
		colHeaders[5] = "Resource Description";
		colHeaders[6] = "Quantity";
		colHeaders[7] = "Unit";
		colHeaders[8] = "Resource Rate";
		colHeaders[9] = "Resource Amount";
		colHeaders[10] = "Posted IV Amount";
		colHeaders[11] = "Cumulative IV Amount";

		
		doc.insertRow(colHeaders);
		doc.setCellFontBold(0, 0, 0, 12);
		
		//Insert rows
		logger.info("Inserting rows of Excel file: "+filename);
		for(BpiItemResource resource : resourcesList){
			String bpi = 	(resource.getRefBillNo()==null?"":resource.getRefBillNo().trim()) + "/" +
							(resource.getRefSubBillNo()==null?"":resource.getRefSubBillNo().trim()) + "/" +
							"/"+
							(resource.getRefPageNo()==null?"":resource.getRefPageNo().trim()) + "/" +
							(resource.getRefItemNo()==null?"":resource.getRefItemNo().trim());
			
			//By Peter Chan 2011-09-20
			Double quantity = resource.getQuantity() * resource.getRemeasuredFactor();
			Double resourceRate = resource.getCostRate();
			Double resourceAmount = RoundingUtil.round(quantity.doubleValue() * resourceRate.doubleValue(), 2);
			
			Double postedIVAmount = resource.getIvPostedAmount();
			Double cumulativeIVAmount = resource.getIvCumAmount();
			
			String[] row = new String[]{bpi,
										resource.getObjectCode(),
										resource.getSubsidiaryCode(),
										resource.getResourceNo().toString(),
										resource.getPackageNo(),
										resource.getDescription(),
										quantity.toString(),
										resource.getUnit(),
										resourceRate.toString(),
										resourceAmount.toString(),
										postedIVAmount.toString(),
										cumulativeIVAmount.toString()	//column 11 is editable
										};
			doc.insertRow(row);
			
			//set Editable Column
			doc.setCellFontEditable(1, 11, resourcesList.size(), 11);
			
			//Set columns' width
			doc.setColumnWidth(0, 15);
			doc.setColumnWidth(1, 10);
			doc.setColumnWidth(2, 15);
			doc.setColumnWidth(3, 12);
			doc.setColumnWidth(4, 12);
			doc.setColumnWidth(5, 50);
			doc.setColumnWidth(6, 10);
			doc.setColumnWidth(7, 6);
			doc.setColumnWidth(8, 15);
			doc.setColumnWidth(9, 15);
			doc.setColumnWidth(10, 20);
			doc.setColumnWidth(11, 20);
		}

		logger.info("DONE -> downloadResourceIVExcel()");
		return excel;
	}

	/**
	 * @author tikywong
	 * April 20, 2011
	 */
	public String uploadBQItemIVExcel(String jobNumber, String username, byte[] file) throws Exception {
		logger.info("STARTED -> uploadBQItemIVExcel()");
		try{		
			//Open Excel File
			excelFileProcessor.openFile(file);
			logger.info("J#"+jobNumber+" Opened file successfully. Rows = "+excelFileProcessor.getNumRow());
			
			//Read Headers of Row 0
			excelFileProcessor.readLine(0);	
			
			List<BpiItem> updatedBQItems = new ArrayList<BpiItem>(excelFileProcessor.getNumRow());
			
			//Read lines and validate
			logger.info("J#"+jobNumber+" Reading rows of Excel file.");
			for(int i=1; i<excelFileProcessor.getNumRow(); i++){
				String[] line = excelFileProcessor.readLine(14);
				
//				String sequenceNo = (line[0]==null || line[0].trim().length()==0)?null:line[0].trim();
				String bpi = (line[1]==null || line[1].trim().length()==0)?null:line[1].trim();
//				String billNo = (line[2]==null || line[2].trim().length()==0)?null:line[2].trim();
//				String subBillNo = (line[3]==null || line[3].trim().length()==0)?null:line[3].trim();
//				String pageNo = (line[4]==null || line[4].trim().length()==0)?null:line[4].trim();
//				String itemNo = (line[5]==null || line[5].trim().length()==0)?null:line[5].trim();
				String bqDescription = (line[6]==null || line[6].trim().length()==0)?null:line[6].trim();
//				double remeasuredQuantity = Double.valueOf((line[7]==null||line[7].trim().length()==0)?"0.00":line[7].trim());
				String unit = (line[8]==null || line[8].trim().length()==0)?null:line[8].trim();
				double costRate = Double.valueOf((line[9]==null||line[9].trim().length()==0)?"0.00":line[9].trim());
//				double costAmount = Double.valueOf((line[10]==null||line[10].trim().length()==0)?"0.00":line[10].trim());
//				double postedIVAmount = Double.valueOf((line[11]==null||line[11].trim().length()==0)?"0.00":line[11].trim());
				double updatedCumulativeIVAmount = RoundingUtil.round(Double.valueOf((line[12]==null||line[12].trim().length()==0)?"0.00":line[12].trim()), 2);
//				String bqType = (line[13]==null || line[13].trim().length()==0)?null:line[13].trim();
				
				String[] bpiSplit = bpi.split("/");
				BpiItem bqItemInDB = bqItemHBDao.searchBpiItem(jobNumber, bpiSplit[0], bpiSplit[1], bpiSplit[3], bpiSplit[4], bqDescription, new Double(costRate), unit);
				if(bqItemInDB==null)
					return "Could not find BQItem in DataBase. Row "+(i+1);
				
				//Validations
				String billNoInDB = bqItemInDB.getRefBillNo();
				boolean isBill80 = billNoInDB!=null && !billNoInDB.trim().equals("") && billNoInDB.trim().equals("80");
				
				double cumulativeIVAmountInDB = RoundingUtil.round(bqItemInDB.getIvCumAmount(), 2);
				double remeasuredQuantityInDB = bqItemInDB.getRemeasuredQuantity();
				double costRateInDB = bqItemInDB.getCostRate();
				double costAmountInDB = RoundingUtil.round(isBill80 ? remeasuredQuantityInDB : remeasuredQuantityInDB * costRateInDB, 2);				
				
				boolean toBeUpdated = false;
				
				//update only the line which has different cumulative IV Amount than database 
				if(updatedCumulativeIVAmount!=cumulativeIVAmountInDB){			
					//Validation 1: if imported cumulative IV amount = 0, update it
					if(updatedCumulativeIVAmount==0)
						toBeUpdated = true;
					//Validation 2: if DB cost amount = 0, imported IV amount has to be 0
					else if(costAmountInDB==0){
						logger.info("Validation 2: if DB cost amount = 0, imported IV amount has to be 0 - ImportedCumulativeIVAmount:"+updatedCumulativeIVAmount+" DBCostAmount:"+costAmountInDB);
						return "Cost Amount=0 - Cumulative IV Amount must be 0. Row "+(i+1);
					}
					//Validation 3: imported cumulative IV amount cannot exceed DB cost amount
					else if(updatedCumulativeIVAmount/costAmountInDB>1 && updatedCumulativeIVAmount!=costAmountInDB){
						logger.info("Validation 3: imported cumulative IV amount cannot exceed DB cost amount - ImportedCumulativeIVAmount:"+updatedCumulativeIVAmount+" DBCostAmount:"+costAmountInDB);
						return "Cumulative IV Amount cannot be greater in magnitude than Cost Amount. Row "+(i+1);
					}
					//Validation 4: imported cumulative IV amount needs to have same sign as DB cost amount
					else if(updatedCumulativeIVAmount/costAmountInDB<0){
						logger.info("Validation 4: imported cumulative IV amount needs to have same sign as DB cost amount - ImportedCumulativeIVAmount:"+updatedCumulativeIVAmount+" DBCostAmount:"+costAmountInDB);
						return "Cumulative IV Amount must have the same sign(+/-) as Cost Amount. Row "+(i+1);
					}
					//Passes all the validations 
					else
						toBeUpdated = true;
				}
				
				//update cumulative amount
				if(toBeUpdated){
					double updatedCumulativeIVQuantity = isBill80?updatedCumulativeIVAmount:(costRateInDB==0?0.00:RoundingUtil.round(updatedCumulativeIVAmount, 2) / costRateInDB);
					
//					bqItemInDB.setIvCumAmount(Double.valueOf(RoundingUtil.round(updatedCumulativeIVAmount, 2)));
//					bqItemInDB.setIvCumQty(Double.valueOf(RoundingUtil.round(updatedCumulativeIVQuantity,2)));
//					updatedBQItems.add(bqItemInDB);
					
					BpiItem updatedBQItem = new BpiItem();
					updatedBQItem.setId(bqItemInDB.getId());
					updatedBQItem.setRefJobNumber(bqItemInDB.getRefJobNumber());
					updatedBQItem.setIvCumAmount(Double.valueOf(RoundingUtil.round(updatedCumulativeIVAmount, 2)));
					updatedBQItem.setIvCumQty(Double.valueOf(RoundingUtil.round(updatedCumulativeIVQuantity,2)));
					updatedBQItems.add(updatedBQItem);
//					logger.info("UPDATE - J#"+bqItemInDB.getRefJobNumber()+" ID:"+updatedBQItem.getId()+
//								" Updated Cumulative IV Quantity: "+updatedBQItem.getIvCumQty()+
//								" Updated Cumulative IV Amount: "+updatedBQItem.getIvCumAmount());	
				}
			}
			
			//Save bqItems
			logger.info("J#"+jobNumber+" Saving Imported Excel file.");
			updateBQItemsIVAmount(updatedBQItems, username);
			
			logger.info("DONE -> uploadBQItemIVExcel()");
			return null;
		}catch(Exception e){
			logger.severe(e.getMessage());
			e.printStackTrace();
			return "Error reading file:<br/>" + e.getMessage();
		}
	}
	
	/**
	 * @author tikywong
	 * April 20, 2011
	 */
	public String uploadResourceIVExcel(String jobNumber, String username, byte[] file) throws Exception {
		logger.info("STARTED -> uploadResourceIVExcel()");
		try{
			//Open Excel File
			excelFileProcessor.openFile(file);
			logger.info("J#"+jobNumber+" Opened file successfully. Rows = "+excelFileProcessor.getNumRow());
			
			//Read Headers of Row 0
			excelFileProcessor.readLine(0);	
			
			List<BpiItemResource> updatedResources = new ArrayList<BpiItemResource>(excelFileProcessor.getNumRow());
			
			//Read lines and validate
			logger.info("J#"+jobNumber+" Reading rows of Excel file.");
			for(int i=1; i<excelFileProcessor.getNumRow(); i++){
				String[] line = excelFileProcessor.readLine(12);
				
				String bpi = (line[0]==null || line[0].trim().length()==0)?null:line[0].trim();
				String objectCode = (line[1]==null || line[1].trim().length()==0)?null:line[1].trim();
				String subsidiaryCode = (line[2]==null || line[2].trim().length()==0)?null:line[2].trim();
				int resourceNo = Integer.valueOf((line[3]==null||line[3].trim().length()==0)?"0":line[3].trim());
				String packageNo = (line[4]==null || line[4].trim().length()==0)?null:line[4].trim();	
				String resourceDescription = (line[5]==null || line[5].trim().length()==0)?null:line[5].trim();
//				double quantity = Double.valueOf((line[6]==null||line[6].trim().length()==0)?"0.00":line[6].trim());
				String unit = (line[7]==null || line[7].trim().length()==0)?null:line[7].trim();
				double resourceRate = Double.valueOf((line[8]==null||line[8].trim().length()==0)?"0.00":line[8].trim());
//				double resourceAmount = Double.valueOf((line[9]==null||line[9].trim().length()==0)?"0.00":line[9].trim());
//				double postedIVAmount = Double.valueOf((line[10]==null||line[10].trim().length()==0)?"0.00":line[10].trim());
				double updatedCumulativeIVAmount = RoundingUtil.round(Double.valueOf((line[11]==null||line[11].trim().length()==0)?"0.00":line[11].trim()), 2);
				
				String[] bpiSplit = bpi.split("/");
				BpiItemResource resourceInDB = resourceHBDao.obtainResource(jobNumber, bpiSplit[0], bpiSplit[1], bpiSplit[3], bpiSplit[4], packageNo, objectCode, subsidiaryCode, resourceDescription, new Double(resourceRate), unit,new Integer(resourceNo));
				
				if(resourceInDB==null)
					return "Could not find Resource in DataBase. Row "+(i+1);
			
				//Validations				
				String billNo = resourceInDB.getRefBillNo();
				boolean isBill80 = billNo!=null && !billNo.trim().equals("") && billNo.trim().equals("80");

				double cumulativeIVAmountInDB = RoundingUtil.round(resourceInDB.getIvCumAmount(), 2);
				double quantityInDB = resourceInDB.getQuantity();
				//By Peter Chan 2011-09-20
				double remeasurementFactorInDB = resourceInDB.getRemeasuredFactor();
				double costRateInDB = resourceInDB.getCostRate();
				//By Peter Chan 2011-09-20
				double costAmountInDB = RoundingUtil.round(isBill80?quantityInDB :quantityInDB * costRateInDB * remeasurementFactorInDB, 2);
				
				boolean toBeUpdated = false;
				
				//update only the line which has different cumulative IV Amount than database 
				if(updatedCumulativeIVAmount!=cumulativeIVAmountInDB){			
					//Validation 1: if imported cumulative IV amount = 0, update it
					if(updatedCumulativeIVAmount==0)
						toBeUpdated = true;
					//Validation 2: if DB cost amount = 0, imported IV amount has to be 0
					else if(costAmountInDB==0 ){
						logger.info("Validation 2: if DB cost amount = 0, imported IV amount has to be 0 - ImportedCumulativeIVAmount:"+updatedCumulativeIVAmount+" DBCostAmount:"+costAmountInDB);
						return "Cost Amount=0 - Cumulative IV Amount must be 0. Row "+(i+1);
					}
					//Validation 3: imported cumulative IV amount cannot exceed DB cost amount
					else if(updatedCumulativeIVAmount/costAmountInDB>1 && updatedCumulativeIVAmount!=costAmountInDB){
						logger.info("Validation 3: imported cumulative IV amount cannot exceed DB cost amount - ImportedCumulativeIVAmount:"+updatedCumulativeIVAmount+" DBCostAmount:"+costAmountInDB);
						return "Cumulative IV Amount cannot be greater in magnitude than Cost Amount. Row "+(i+1);
					}
					//Validation 4: imported cumulative IV amount needs to have same sign as DB cost amount
					else if(updatedCumulativeIVAmount/costAmountInDB<0){
						logger.info("Validation 4: imported cumulative IV amount needs to have same sign as DB cost amount - ImportedCumulativeIVAmount:"+updatedCumulativeIVAmount+" DBCostAmount:"+costAmountInDB);
						return "Cumulative IV Amount must have the same sign(+/-) as Cost Amount. Row "+(i+1);
					}
					//Passes all validations
					else
						toBeUpdated = true;
				}
				
				//Update Cumulative IV Quantity, Cumulative IV Amount & Cumulative IV Movement Amount 
				if(toBeUpdated){
					double updatedCumulativeIVQuantity = costRateInDB==0?0.00:RoundingUtil.round(updatedCumulativeIVAmount, 2) / costRateInDB;
			
//					resourceInDB.setIvCumAmount(RoundingUtil.round(updatedCumulativeIVAmount, 2));
//					resourceInDB.setIvCumQuantity(RoundingUtil.round(updatedCumulativeIVQuantity, 2));
//					resourceInDB.setIvMovementAmount(RoundingUtil.round(updatedCumulativeIVAmount, 2) - (resourceInDB.getIvPostedAmount()==null?0.00:resourceInDB.getIvPostedAmount()));
//					updatedResources.add(resourceInDB);

					BpiItemResource updatedResource = new BpiItemResource();
					updatedResource.setId(resourceInDB.getId());
					updatedResource.setJobNumber(resourceInDB.getJobNumber());
					updatedResource.setIvCumAmount(RoundingUtil.round(updatedCumulativeIVAmount, 2));
					updatedResource.setIvCumQuantity(RoundingUtil.round(updatedCumulativeIVQuantity, 2));
					updatedResource.setIvMovementAmount(RoundingUtil.round(updatedCumulativeIVAmount, 2) - (resourceInDB.getIvPostedAmount()==null?0.00:RoundingUtil.round(resourceInDB.getIvPostedAmount(), 2)));
					updatedResources.add(updatedResource);
//					logger.info("UPDATE - J#"+resourceInDB.getJobNumber()+" ID:"+updatedResource.getId()+
//								" Updated Cumulative IV Quantity: "+updatedResource.getIvCumQuantity()+
//								" Updated Cumulative IV Amount: "+updatedResource.getIvCumAmount());
				}
			}
			
			//Update resources
			logger.info("J#"+jobNumber+" Saving Imported Excel file.");
			updateResourcesIVAmount(updatedResources, username);
			
			logger.info("DONE -> uploadResourceIVExcel()");
			return null;
		}catch(Exception e){
			logger.severe(e.getMessage());
			e.printStackTrace();
			return "Error reading file:<br/>" + e.getMessage();
		}
	}

	/**
	 * @author tikywong
	 * May 20, 2011 2:42:49 PM
	 * @see com.gammon.qs.service.BpiItemService#recalculateIVAmountsByResourceForMethodThree(java.lang.String)
	 */
	public Boolean recalculateIVAmountsByResourceForMethodThree(String jobNumber)throws Exception {
		logger.info("STARTED -> recalculateIVAmountsByResourceForMethodThree()");
		Boolean toBeUpdated = true;
		//UPDATE	
		//2.BQItem IV from Resource
		toBeUpdated = toBeUpdated && recalculateBQItemIVAmountByResource(jobNumber);
		
		//3.Resource Summary IV from Resource
		toBeUpdated = toBeUpdated && recalculateBQResourceSummaryIVAmountByResource(jobNumber);

		//4.SC Detail Workdone from Resource
		toBeUpdated = toBeUpdated && recalculateSCDetailsWorkdoneQuantityByResource(jobNumber);
		
		//5.SC Package Total Liability Amount
		toBeUpdated = toBeUpdated && packageRepository.recalculateTotalWDAmount(jobNumber);
		
		logger.info("DONE -> recalculateIVAmountsByResourceForMethodThree()");
		return toBeUpdated;
	}

	/**
	 * @author tikywong
	 * May 20, 2011 3:20:43 PM
	 */
	public Boolean recalculateBQItemIVAmountByResource(String jobNumber)throws DatabaseOperationException {
		logger.info("Recalculating BQItems...");
		double total = 0;
		List<IVBpiItemGroupedByIDWrapper> updatedWrappers = resourceHBDao.groupResourcesToBpiItems(jobNumber);
		
		//1. BQItems save
		List<BpiItem> bqItems = new ArrayList<BpiItem>();
		for(IVBpiItemGroupedByIDWrapper updatedWrapper : updatedWrappers){
			BpiItem bqItem = bqItemHBDao.get(updatedWrapper.getBqItemID());
			if(RoundingUtil.round(bqItem.getIvCumAmount(), 2) != RoundingUtil.round(updatedWrapper.getUpdatedIVCumulativeAmount(), 2)){
				bqItem.setIvCumAmount(RoundingUtil.round(updatedWrapper.getUpdatedIVCumulativeAmount(), 2));
				if(bqItem.getRefBillNo().equals("80"))
					bqItem.setIvCumQty(RoundingUtil.round(updatedWrapper.getUpdatedIVCumulativeAmount(), 2));
				else
					bqItem.setIvCumQty(bqItem.getCostRate()==0?0.00:RoundingUtil.round(updatedWrapper.getUpdatedIVCumulativeAmount()/bqItem.getCostRate(), 2));
				bqItems.add(bqItem);
//				bqHBDao.save(bqItem);
			}
			total+=bqItem.getIvCumAmount();
		}
		
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setRoundingMode(RoundingMode.HALF_UP);
		numberFormat.setMaximumFractionDigits(4);
		logger.info("BQItems Total IVAmount: "+numberFormat.format(total));
		bqItemHBDao.updateBpiItems(bqItems);
		return true;
	}
	
	/**
	 * @author tikywong
	 * May 23, 2011 5:24:57 PM
	 */
	public Boolean recalculateBQResourceSummaryIVAmountByResource(String jobNumber) throws Exception{
		logger.info("Recalculating BQResourceSummaries...");
		double total = 0;
		List<IVBpiResourceSummaryGroupedBySCWrapper> updatedSCWrappers = resourceHBDao.groupResourcesToBQResourceSummariesForSC(jobNumber);
		List<IVBpiResourceSummaryGroupedBySCWrapper> updatedNonSCWrappers = resourceHBDao.groupResourcesToBpiResourceSummariesForNonSC(jobNumber);
		
		//2. BQResourceSummaries save
		JobInfo job = jobHBDao.obtainJobInfo(jobNumber);
		List<ResourceSummary> bqResourceSummaries = new ArrayList<ResourceSummary>();
		
		//SC
//		logger.info("Updating SC BQResourceSummaries");
		for(IVBpiResourceSummaryGroupedBySCWrapper updatedWrapper : updatedSCWrappers){
//			logger.info("Updating SCBQResourceSummary: packageNo: "+wrapper.getPackageNo()+" objectCode: "+wrapper.getObjectCode()+" subsidiaryCode: "+wrapper.getSubsidiaryCode());
			ResourceSummary bqResourceSummary = null;
			String packageNo = (updatedWrapper.getPackageNo()==null||updatedWrapper.getPackageNo().equals("")||updatedWrapper.getPackageNo().trim().equals("0"))?null:updatedWrapper.getPackageNo();
			List<ResourceSummary> scBQResourceSummaries = bqResourceSummaryDao.getResourceSummariesForAccount(job, packageNo, updatedWrapper.getObjectCode(), updatedWrapper.getSubsidiaryCode());
			if(scBQResourceSummaries==null || scBQResourceSummaries.size()!=1)
				throw new ValidateBusinessLogicException("SCBQResourceSummary doesn't exist or have more than one record: jobNumber: "+jobNumber+" packageNo: "+updatedWrapper.getPackageNo()+" objectCode: "+updatedWrapper.getObjectCode()+" subsidiaryCode: "+updatedWrapper.getSubsidiaryCode());
			else{
				bqResourceSummary = scBQResourceSummaries.get(0);
				if(RoundingUtil.round(bqResourceSummary.getCurrIVAmount(), 2) != RoundingUtil.round(updatedWrapper.getUpdatedCurrentIVAmount(), 2)){
					bqResourceSummary.setCurrIVAmount(RoundingUtil.round(updatedWrapper.getUpdatedCurrentIVAmount(), 2));
					bqResourceSummaries.add(bqResourceSummary);
//					bqResourceSummaryRepositoryHB.getBqResourceSummaryDao().save(bqResourceSummary);
				}
				total+=bqResourceSummary.getCurrIVAmount();
			}
		}
		//Non-SC
//		logger.info("Updating Non-SC BQResourceSummaries");
		for(IVBpiResourceSummaryGroupedBySCWrapper updatedWrapper : updatedNonSCWrappers){
//			logger.info("Updating NonSCBQResourceSummary: packageNo: "+wrapper.getPackageNo()+" objectCode: "+wrapper.getObjectCode()+" subsidiaryCode: "+wrapper.getSubsidiaryCode()+" Description:"+wrapper.getResourceDescription()+" Unit:"+wrapper.getUnit()+" Rate:"+wrapper.getRate());
			String packageNo = (updatedWrapper.getPackageNo()==null||updatedWrapper.getPackageNo().equals("")||updatedWrapper.getPackageNo().trim().equals("0"))?null:updatedWrapper.getPackageNo();
			ResourceSummary bqResourceSummary = bqResourceSummaryDao.getResourceSummary(job, packageNo, updatedWrapper.getObjectCode(), updatedWrapper.getSubsidiaryCode(), updatedWrapper.getResourceDescription(), updatedWrapper.getUnit(), updatedWrapper.getSubsidiaryCode().startsWith("9")?new Double(1):updatedWrapper.getRate());
			
			if(bqResourceSummary == null)
				throw new ValidateBusinessLogicException("BQResourceSummary doesn't exist: packageNo: "+updatedWrapper.getPackageNo()+" objectCode: "+updatedWrapper.getObjectCode()+" subsidiaryCode: "+updatedWrapper.getSubsidiaryCode()+" Description:"+updatedWrapper.getResourceDescription()+" Unit:"+updatedWrapper.getUnit()+" Rate:"+updatedWrapper.getRate());
			else{
				if(RoundingUtil.round(bqResourceSummary.getCurrIVAmount(), 2) != RoundingUtil.round(updatedWrapper.getUpdatedCurrentIVAmount(), 2)){
					bqResourceSummary.setCurrIVAmount(RoundingUtil.round(updatedWrapper.getUpdatedCurrentIVAmount(), 2));
					bqResourceSummaries.add(bqResourceSummary);
//					bqResourceSummaryRepositoryHB.getBqResourceSummaryDao().save(bqResourceSummary);
				}
				total+=bqResourceSummary.getCurrIVAmount();
			}
		}
		
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setRoundingMode(RoundingMode.HALF_UP);
		numberFormat.setMaximumFractionDigits(4);
		logger.info("BQResourceSummaries Total IVAmount: "+numberFormat.format(total));
		bqResourceSummaryDao.updateBQResourceSummaries(bqResourceSummaries);
		return true;
	}
	
	/**
	 * @author tikywong
	 * May 24, 2011 9:16:07 AM
	 * @throws Exception 
	 */
	public Boolean recalculateSCDetailsWorkdoneQuantityByResource(String jobNumber) throws Exception {
		logger.info("Recalculating SCDetails...");
		double total = 0;
		List<BpiItemResource> resources = resourceHBDao.obtainSCResources(jobNumber);
		
		List<IVResourceWrapper> resourceWrappers = new ArrayList<IVResourceWrapper>();
		for(BpiItemResource resource : resources)
			resourceWrappers.add(new IVResourceWrapper(resource));
		
		List<IVSCDetailsWrapper> updatedWrappers = packageRepository.updateSCDetailsWorkdoneQuantityByResource(resourceWrappers);
		//3. SCDetails save
		List<SubcontractDetail> scDetails = new ArrayList<SubcontractDetail>();
		for(IVSCDetailsWrapper updatedWrapper : updatedWrappers){
			SubcontractDetail scDetail = updatedWrapper.getScDetail();
			if(RoundingUtil.round(scDetail.getCumWorkDoneQuantity(), 4) != RoundingUtil.round(updatedWrapper.getUpdatedCumulativeWDQuantity(), 4)){
				((SubcontractDetailOA) scDetail).setCumWorkDoneQuantity(RoundingUtil.round(updatedWrapper.getUpdatedCumulativeWDQuantity(), 4));
				scDetails.add(scDetail);
//				packageRepository.getScDetailsHBDao().save(scDetail);
			}
			total+=(RoundingUtil.round(scDetail.getCumWorkDoneQuantity()*scDetail.getScRate(), 2));
		}
		
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setRoundingMode(RoundingMode.HALF_UP);
		numberFormat.setMaximumFractionDigits(4);
		logger.info("SCDetails Total WorkdoneAmount: "+numberFormat.format(total));
		scDetailsHBDao.updateSCDetails(scDetails);
		return true;
	}
	
}
