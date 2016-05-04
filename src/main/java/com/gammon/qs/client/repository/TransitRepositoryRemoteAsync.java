package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.domain.TransitBQ;
import com.gammon.qs.domain.TransitCodeMatch;
import com.gammon.qs.domain.TransitHeader;
import com.gammon.qs.domain.TransitResource;
import com.gammon.qs.domain.TransitUomMatch;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.TransitHeaderResultWrapper;
import com.gammon.qs.wrapper.TransitResourceWrapper;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TransitRepositoryRemoteAsync {
	void createOrUpdateTransitHeader(String jobNumber, String estimateNo, String matchingCode, boolean newJob, AsyncCallback<TransitHeaderResultWrapper> callback);
	void getTransitHeader(String jobNumber, AsyncCallback<TransitHeader> callback);
	void getTransitBQItemsNewSearch(String jobNumber, String billNo, String subBillNo, 
			String pageNo, String itemNo, String description, AsyncCallback<PaginationWrapper<TransitBQ>> callback);
	void getTransitBQItemsByPage(int pageNum, AsyncCallback<PaginationWrapper<TransitBQ>> callback);
	void searchTransitResourcesByPage(String jobNumber, String billNo, String subBillNo, String pageNo, 
			String itemNo, String resourceCode, String objectCode, String subsidiaryCode, 
			String description, int pageNum, AsyncCallback<PaginationWrapper<TransitResourceWrapper>> callback);
	void saveTransitResources(String jobNumber, List<TransitResource> resources, AsyncCallback<String> callback);
	void confirmResourcesAndCreatePackages(String jobNumber, AsyncCallback<String> callback);
	void completeTransit(String jobNumber, AsyncCallback<String> callback);
	void allowPrint(String jobNumber, AsyncCallback<Boolean> callback);
	void searchTransitCodeMatches(String matchingType, String resourceCode, 
			String objectCode, String subsidiaryCode, AsyncCallback<PaginationWrapper<TransitCodeMatch>> callback);
	void searchTransitCodeMatchesByPage(int pageNum, AsyncCallback<PaginationWrapper<TransitCodeMatch>> callback);
	void searchTransitUomMatches(String causewayUom, String jdeUom, AsyncCallback<PaginationWrapper<TransitUomMatch>> callback);
	void searchTransitUomMatchesByPage(int pageNum, AsyncCallback<PaginationWrapper<TransitUomMatch>> callback);
	
	// added by brian on 20110228
	// get all transits information according to status
	// if status == "" or null, get all transits
	void getAllTransitHeaders(String status, AsyncCallback<List<TransitHeader>> callback);
}
