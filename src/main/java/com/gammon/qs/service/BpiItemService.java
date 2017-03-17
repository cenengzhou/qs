package com.gammon.qs.service;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.qs.dao.BpiItemHBDao;
import com.gammon.qs.dao.BpiItemResourceHBDao;
import com.gammon.qs.domain.BQLineType;
import com.gammon.qs.domain.BpiItem;
import com.gammon.qs.domain.BpiItemResource;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.shared.GlobalParameter;
import com.gammon.qs.wrapper.updateIVAmountByMethodThree.IVInputByBQItemPaginationWrapper;
import com.gammon.qs.wrapper.updateIVAmountByMethodThree.IVInputByResourcePaginationWrapper;
@Service
//SpringSession workaround: change "session" to "request"
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "request")
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class BpiItemService implements Serializable {
	private static final long serialVersionUID = 6521439591143450718L;
	private transient Logger logger = Logger.getLogger(BpiItemService.class.getName());
	@Autowired
	private transient BpiItemHBDao bqItemHBDao;
	@Autowired
	private transient BpiItemResourceHBDao resourceHBDao;
	
	//For Update IV by BQ Item
	private ArrayList<BpiItem> listOfBQItem;
	private IVInputByBQItemPaginationWrapper ivInputByBQItemPaginationWrapper;
	
	//For Update IV by Resource
	private ArrayList<BpiItemResource> listOfResource;
	private IVInputByResourcePaginationWrapper ivInputByResourcePaginationWrapper;
	
	static final int RECORDS_PER_PAGE = 200;
	//pagination cache
	@SuppressWarnings("unused")
	private static final int PAGE_SIZE = GlobalParameter.PAGE_SIZE;

	public BpiItemService(){
		
	}

	
	public List<BpiItem> obtainBQItemList(String jobNumber, String billNo, String subbillNo, String pageNo, String itemNo, String bqDesc)  throws Exception{
		return bqItemHBDao.obtainBpiItem(jobNumber, billNo, subbillNo, pageNo, itemNo, bqDesc);
	}
	
	
	public List<BpiItemResource> getResourcesByBqItemId(Long id) throws Exception{
		return resourceHBDao.getResourcesByBpiItemId(id);
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
	
	
	public BpiItem getBqItemWithResources(Long id) throws Exception{
		return bqItemHBDao.getBpiItemWithResources(id);
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

	
	
}
