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
import com.google.gwt.user.client.rpc.RemoteService;

public interface TransitRepositoryRemote extends RemoteService{
	TransitHeaderResultWrapper createOrUpdateTransitHeader(String jobNumber, String estimateNo, String matchingCode, boolean newJob) throws Exception;
	TransitHeader getTransitHeader(String jobNumber) throws Exception;
	PaginationWrapper<TransitBQ> getTransitBQItemsNewSearch(String jobNumber, String billNo, String subBillNo, 
			String pageNo, String itemNo, String description) throws Exception;
	PaginationWrapper<TransitBQ> getTransitBQItemsByPage(int pageNum);
	PaginationWrapper<TransitResourceWrapper> searchTransitResourcesByPage(String jobNumber, String billNo, String subBillNo,
			String pageNo, String itemNo, String resourceCode, String objectCode, String subsidiaryCode, String description, int pageNum) throws Exception;
	String saveTransitResources(String jobNumber, List<TransitResource> resources) throws Exception;
	String confirmResourcesAndCreatePackages(String jobNumber) throws Exception;
	String completeTransit(String jobNumber) throws Exception;
	Boolean allowPrint(String jobNumber);
	PaginationWrapper<TransitCodeMatch> searchTransitCodeMatches(String matchingType, String resourceCode, 
			String objectCode, String subsidiaryCode) throws Exception;
	PaginationWrapper<TransitCodeMatch> searchTransitCodeMatchesByPage(int pageNum);
	PaginationWrapper<TransitUomMatch> searchTransitUomMatches(String causewayUom, String jdeUom) throws Exception;
	PaginationWrapper<TransitUomMatch> searchTransitUomMatchesByPage(int pageNum);
	
	// added by brian on 20110228
	// get all transits information according to status
	// if status == "" or null, get all transits
	List<TransitHeader> getAllTransitHeaders(String status);
}
