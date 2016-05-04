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
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BQRepositoryRemoteAsync {
	void obtainUneditableResources(Job job, AsyncCallback<List<String>> callback);
	void getBillListWithPagesByJob(Job job, AsyncCallback<List<Bill>> callback);
	void getBQItemListByBPI(String jobNumber, String billNo, String subBillNo, String sectionNo, String pageNo,  AsyncCallback<List<BQItem>> callback);
	void obtainResourcesByWrapper(ResourceWrapper wrapper, AsyncCallback<BQPaginationWrapper> callback);
	void obtainResourcesByPage(int pageNum, AsyncCallback<BQPaginationWrapper> callback);
	void obtainBQItem(String jobNumber, String billNo, String subbillNo, String pageNo, String itemNo, String bqDesc, AsyncCallback<BQItemPaginationWrapper> callback);
	void getBqItemsByPage(int pageNum, AsyncCallback<BQItemPaginationWrapper> asyncCallback);
	void searchBQItemsByPage(String jobNumber, String billNo, String subBillNo, String pageNo,
			String itemNo, String description, int pageNum, AsyncCallback<RepackagingPaginationWrapper<BQItem>> callback);
	void updateBQItemQuantities(Job job, List<BQItem> bqItems, AsyncCallback<String> callback);
	void searchResourcesByPage(String jobNumber, String billNo, String subBillNo, String pageNo,
			String itemNo, String packageNo, String objectCode, String subsidiaryCode, String description, int pageNum, AsyncCallback<RepackagingPaginationWrapper<Resource>> callback);
	void getResourcesByBqItemId(Long id, AsyncCallback<List<Resource>> callback);
	void saveResourceUpdates(List<Resource> resources, AsyncCallback<String> callback);
	void saveSplitMergeResources(List<Resource> oldResources, List<Resource> newResources, AsyncCallback<String> callback);
	void saveBalancedResources(List<Resource> resources, Double remeasuredQuantity, AsyncCallback<String> callback);
	void getBillItemFieldsForChangeOrder(Job job, String bqType, AsyncCallback<BQItem> callback);
	void validateBqItemChangeOrder(BQItem bqItem, AsyncCallback<String> callback);
	void saveChangeOrderBqAndResources(BQItem bqItem, List<Resource> resourceList, AsyncCallback<String> callback);
	void getBqItemWithResources(Long id, AsyncCallback<BQItem> callback);
	void saveResourceSubcontractAddendums(List<Resource> resources, AsyncCallback<String> callback);
	void getResourcesByBpi(String jobNumber, String bpi, AsyncCallback<List<Resource>> callback);
	/**
	 * @author tikywong
	 * Apr 1, 2011 5:31:40 PM
	 */
	void searchBQItemsForIVInput(String jobNumber, String billNo, String subBillNo, String pageNo, String itemNo, String bqDescription,
								AsyncCallback<IVInputByBQItemPaginationWrapper> asyncCallback);
	/**
	 * @author tikywong
	 * Apr 1, 2011 5:39:51 PM
	 */
	void getBQItemsForIVInputByPage(int pageNum, AsyncCallback<IVInputByBQItemPaginationWrapper> asyncCallback);
	/**
	 * @author tikywong
	 * Apr 7, 2011 10:49:46 AM
	 */
	void searchResourcesForIVInput(String jobNumber, String billNo, String subBillNo, String pageNo, String itemNo,
			String resourceDescription, String objectCode, String subsidiaryCode, String packageNo,
			AsyncCallback<IVInputByResourcePaginationWrapper> asyncCallback);
	/**
	 * @author tikywong
	 * Apr 7, 2011 1:40:55 PM
	 */
	void getResourcesForIVInputByPage(int pageNum, AsyncCallback<IVInputByResourcePaginationWrapper> asyncCallback);
	/**
	 * @author tikywong
	 * Apr 8, 2011 10:38:02 AM
	 */
	void updateBQItemsIVAmount(List<BQItem> bqItems, String username, AsyncCallback<Boolean> asyncCallback);
	/**
	 * @author tikywong
	 * Apr 11, 2011 10:10:12 AM
	 */
	void updateResourcesIVAmount(List<Resource> resources, String username, AsyncCallback<Boolean> asyncCallback);
	/**
	 * @author tikywong
	 * May 20, 2011 2:38:08 PM
	 */
	void recalculateIVAmountsByResourceForMethodThree(String jobNumber, AsyncCallback<Boolean> asyncCallback);
}
