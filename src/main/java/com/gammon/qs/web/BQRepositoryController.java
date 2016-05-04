package com.gammon.qs.web;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.client.repository.BQRepositoryRemote;
import com.gammon.qs.domain.BQItem;
import com.gammon.qs.domain.Bill;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.Resource;
import com.gammon.qs.service.BQService;
import com.gammon.qs.wrapper.BQItemPaginationWrapper;
import com.gammon.qs.wrapper.BQPaginationWrapper;
import com.gammon.qs.wrapper.RepackagingPaginationWrapper;
import com.gammon.qs.wrapper.updateIVAmountByMethodThree.IVInputByBQItemPaginationWrapper;
import com.gammon.qs.wrapper.updateIVAmountByMethodThree.IVInputByResourcePaginationWrapper;
import com.gammon.qs.wrapper.updateIVByResource.ResourceWrapper;

import net.sf.gilead.core.PersistentBeanManager;

@Service("bqRepositoryController")
public class BQRepositoryController extends GWTSpringController implements BQRepositoryRemote {

	private static final long serialVersionUID = -2039340037014330336L;
	@Autowired
	private BQService bqRepository;
	@Override
	@Autowired
	public void setBeanManager(PersistentBeanManager manager) {
		super.setBeanManager(manager);
	}
	
	public List<BQItem> getBQItemListByBPI(String jobNumber, String billNo, String subBillNo, String sessionNo, String pageNo)  throws Exception{
		return bqRepository.getBQItemListByBPI(jobNumber, billNo, subBillNo, sessionNo, pageNo);
	}

	public BQItemPaginationWrapper obtainBQItem(
			String jobNumber, String billNo, String subbillNo, String pageNo, String itemNo, String bqDesc)  throws Exception {
		
		return this.bqRepository.obtainBQItem(jobNumber, billNo, subbillNo, pageNo, itemNo, bqDesc);
	}

	public List<Bill> getBillListWithPagesByJob(Job job) throws Exception {
		return bqRepository.getBillListWithPagesByJob(job);
	}

	public RepackagingPaginationWrapper<BQItem> searchBQItemsByPage(
			String jobNumber, String billNo, String subBillNo, String pageNo,
			String itemNo, String description, int pageNum) throws Exception {
		return bqRepository.searchBQItemsByPage(jobNumber, billNo, subBillNo, pageNo, itemNo, description, pageNum);
	}

	public String updateBQItemQuantities(Job job, List<BQItem> bqItems)
			throws Exception {
		return bqRepository.updateBQItemQuantities(job, bqItems);
	}

	public RepackagingPaginationWrapper<Resource> searchResourcesByPage(
			String jobNumber, String billNo, String subBillNo, String pageNo,
			String itemNo, String packageNo, String objectCode,
			String subsidiaryCode, String description, int pageNum)
			throws Exception {
		return bqRepository.searchResourcesByPage(jobNumber, billNo, subBillNo, pageNo, itemNo, packageNo, objectCode, subsidiaryCode, description, pageNum);
	}
	
	public List<Resource> getResourcesByBqItemId(Long id) throws Exception{
		return bqRepository.getResourcesByBqItemId(id);
	}

	public String saveResourceUpdates(List<Resource> resources) throws Exception{
		return bqRepository.saveResourceUpdates(resources);
	}
	
	public String saveSplitMergeResources(List<Resource> oldResources,
			List<Resource> newResources) throws Exception {
		return bqRepository.saveSplitMergeResources(oldResources, newResources);
	}
	
	public BQItemPaginationWrapper getBqItemsByPage(int pageNum)
			throws Exception {
		return bqRepository.getBqItemsByPage(pageNum);
	}
	
	public String saveBalancedResources(List<Resource> resources, Double remeasuredQuantity)
			throws Exception {
		return bqRepository.saveBalancedResources(resources, remeasuredQuantity);
	}

	public BQItem getBillItemFieldsForChangeOrder(Job job, String bqType)
			throws Exception {
		return bqRepository.getBillItemFieldsForChangeOrder(job, bqType);
	}
	
	public String validateBqItemChangeOrder(BQItem bqItem) throws Exception{
		return bqRepository.validateBqItemChangeOrder(bqItem);
	}

	public String saveChangeOrderBqAndResources(BQItem bqItem, List<Resource> resourceLIst) throws Exception {
		return bqRepository.saveChangeOrderBqAndResources(bqItem, resourceLIst);
	}

	public BQItem getBqItemWithResources(Long id) throws Exception {
		return bqRepository.getBqItemWithResources(id);
	}

	public String saveResourceSubcontractAddendums(List<Resource> resources)
			throws Exception {
		return bqRepository.saveResourceSubcontractAddendums(resources);
	}

	public List<Resource> getResourcesByBpi(String jobNumber, String bpi)
			throws Exception {
		return bqRepository.getResourcesByBpi(jobNumber, bpi);
	}

	/**
	 * @author tikywong
	 * May 20, 2011 2:40:00 PM
	 * @see com.gammon.qs.client.repository.BQRepositoryRemote#searchBQItemsForIVInput(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public IVInputByBQItemPaginationWrapper searchBQItemsForIVInput(String jobNumber, String billNo, String subBillNo, 
																	String pageNo, String itemNo, String bqDescription) throws Exception {
		return bqRepository.searchBQItemsForIVInput(jobNumber, billNo, subBillNo, pageNo, itemNo, bqDescription);
	}

	/**
	 * @author tikywong
	 * May 20, 2011 2:40:05 PM
	 * @see com.gammon.qs.client.repository.BQRepositoryRemote#getBQItemsForIVInputByPage(int)
	 */
	public IVInputByBQItemPaginationWrapper getBQItemsForIVInputByPage(int pageNum) throws Exception {
		return bqRepository.getBQItemsForIVInputByPage(pageNum);
	}

	/**
	 * @author tikywong
	 * May 20, 2011 2:40:10 PM
	 * @see com.gammon.qs.client.repository.BQRepositoryRemote#searchResourcesForIVInput(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public IVInputByResourcePaginationWrapper searchResourcesForIVInput(String jobNumber, String billNo, String subBillNo, String pageNo,
																		String itemNo, String resourceDescription, String objectCode,
																		String subsidiaryCode, String packageNo) throws Exception {
		return bqRepository.searchResourcesForIVInput(jobNumber, billNo, subBillNo, pageNo, itemNo, resourceDescription, objectCode, subsidiaryCode, packageNo);
	}

	/**
	 * @author tikywong
	 * May 20, 2011 2:40:16 PM
	 * @see com.gammon.qs.client.repository.BQRepositoryRemote#getResourcesForIVInputByPage(int)
	 */
	public IVInputByResourcePaginationWrapper getResourcesForIVInputByPage(int pageNum) throws Exception {
		return bqRepository.getResourcesForIVInputByPage(pageNum);
	}

	/**
	 * @author tikywong
	 * May 20, 2011 2:40:21 PM
	 * @see com.gammon.qs.client.repository.BQRepositoryRemote#updateBQItemsIVAmount(java.util.List)
	 */
	public Boolean updateBQItemsIVAmount(List<BQItem> bqItems, String username) throws Exception {
		return bqRepository.updateBQItemsIVAmount(bqItems, username);
	}

	/**
	 * @author tikywong
	 * May 20, 2011 2:40:29 PM
	 * @see com.gammon.qs.client.repository.BQRepositoryRemote#updateResourcesIVAmount(java.util.List)
	 */
	public Boolean updateResourcesIVAmount(List<Resource> resources, String username) throws Exception {
		return bqRepository.updateResourcesIVAmount(resources, username);
	}

	/**
	 * @author tikywong
	 * May 20, 2011 2:39:33 PM
	 * @throws Exception 
	 * @see com.gammon.qs.client.repository.BQRepositoryRemote#recalculateIVAmountsByResourceForMethodThree(java.lang.String)
	 */
	public Boolean recalculateIVAmountsByResourceForMethodThree(String jobNumber) throws Exception {
		return bqRepository.recalculateIVAmountsByResourceForMethodThree(jobNumber);
	}

	public BQPaginationWrapper obtainResourcesByWrapper(ResourceWrapper wrapper)throws Exception {
		return bqRepository.obtainResourcesByWrapper(wrapper);
	}

	public BQPaginationWrapper obtainResourcesByPage(int pageNum) {
		return bqRepository.obtainResourcesByPage(pageNum);
	}

	public List<String> obtainUneditableResources(Job job) throws Exception {
		return bqRepository.obtainUneditableResources(job);
	}
}
