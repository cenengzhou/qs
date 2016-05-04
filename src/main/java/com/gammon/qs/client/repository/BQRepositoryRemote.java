package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.domain.BQItem;
import com.gammon.qs.domain.Bill;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.Resource;
import com.gammon.qs.wrapper.BQItemPaginationWrapper;
import com.gammon.qs.wrapper.BQPaginationWrapper;
import com.gammon.qs.wrapper.updateIVAmountByMethodThree.*;
import com.gammon.qs.wrapper.updateIVByResource.ResourceWrapper;
import com.gammon.qs.wrapper.RepackagingPaginationWrapper;
import com.google.gwt.user.client.rpc.RemoteService;

public interface BQRepositoryRemote extends RemoteService {
	
	public List<String> obtainUneditableResources(Job job) throws Exception;
	public List<Bill> getBillListWithPagesByJob(Job job)  throws Exception;
	public List <BQItem> getBQItemListByBPI(String jobNumber, String billNo, String subBillNo, String sectionNo, String pageNo)  throws Exception;
	public BQPaginationWrapper obtainResourcesByWrapper(ResourceWrapper wrapper)  throws Exception;
	public BQPaginationWrapper obtainResourcesByPage(int pageNum);
	
	public BQItemPaginationWrapper obtainBQItem(String jobNumber, String billNo, String subbillNo, String pageNo, String itemNo, String bqDesc)  throws Exception;
	public BQItemPaginationWrapper getBqItemsByPage(int pageNum) throws Exception;
	public RepackagingPaginationWrapper<BQItem> searchBQItemsByPage(String jobNumber, String billNo, String subBillNo, String pageNo,
			String itemNo, String description, int pageNum) throws Exception;
	public String updateBQItemQuantities(Job job, List<BQItem> bqItems) throws Exception;
	public RepackagingPaginationWrapper<Resource> searchResourcesByPage(String jobNumber, String billNo, String subBillNo, String pageNo,
			String itemNo, String packageNo, String objectCode, String subsidiaryCode, String description, int pageNum) throws Exception;
	public List<Resource> getResourcesByBqItemId(Long id) throws Exception;
	public String saveResourceUpdates(List<Resource> resources) throws Exception;
	public String saveSplitMergeResources(List<Resource> oldResources, List<Resource> newResources) throws Exception;
	public String saveBalancedResources(List<Resource> resources, Double remeasuredQuantity) throws Exception;
	public BQItem getBillItemFieldsForChangeOrder(Job job, String bqType) throws Exception;
	public String validateBqItemChangeOrder(BQItem bqItem) throws Exception;
	public String saveChangeOrderBqAndResources(BQItem bqItem, List<Resource> resourceList) throws Exception;
	public BQItem getBqItemWithResources(Long id) throws Exception;
	public String saveResourceSubcontractAddendums(List<Resource> resources) throws Exception;
	public List<Resource> getResourcesByBpi(String jobNumber, String bpi) throws Exception;
	
	/**
	 * @author tikywong
	 * Apr 1, 2011 5:31:40 PM
	 */
	public IVInputByBQItemPaginationWrapper searchBQItemsForIVInput(String jobNumber, String billNo, String subBillNo, String pageNo, String itemNo, String bqDescription) throws Exception;
	/**
	 * @author tikywong
	 * Apr 1, 2011 5:39:51 PM
	 */
	public IVInputByBQItemPaginationWrapper getBQItemsForIVInputByPage(int pageNum) throws Exception;
	
	/**
	 * @author tikywong
	 * Apr 7, 2011 11:11:36 AM
	 */
	public IVInputByResourcePaginationWrapper searchResourcesForIVInput(String jobNumber, String billNo, String subBillNo, String pageNo, String itemNo,
																		String resourceDescription, String objectCode, String subsidiaryCode, String packageNo) throws Exception;
	/**
	 * @author tikywong
	 * Apr 7, 2011 1:40:55 PM
	 */
	public IVInputByResourcePaginationWrapper getResourcesForIVInputByPage(int pageNum) throws Exception;
	
	/**
	 * @author tikywong
	 * Apr 8, 2011 10:38:09 AM
	 */
	public Boolean updateBQItemsIVAmount(List<BQItem> bqItems, String username) throws Exception;
	/**
	 * @author tikywong
	 * Apr 11, 2011 10:10:12 AM
	 */
	public Boolean updateResourcesIVAmount(List<Resource> resources, String username) throws Exception;
	
	/**
	 * @author tikywong
	 * May 20, 2011 2:38:52 PM
	 */
	public Boolean recalculateIVAmountsByResourceForMethodThree(String jobNumber) throws Exception;
}
