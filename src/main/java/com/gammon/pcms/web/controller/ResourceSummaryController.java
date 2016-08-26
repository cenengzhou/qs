/**

 * pcms-tc
 * com.gammon.pcms.web.controller
 * ResourceSummaryController.java
 * @since Jun 20, 2016
 * @author koeyyeung
 */
package com.gammon.pcms.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.pcms.dto.rs.provider.response.resourceSummary.ResourceSummayDashboardDTO;
import com.gammon.qs.domain.ResourceSummary;
import com.gammon.qs.service.ResourceSummaryService;
import com.gammon.qs.wrapper.BQResourceSummaryWrapper;
import com.gammon.qs.wrapper.IVInputPaginationWrapper;
import com.gammon.qs.wrapper.ResourceSummarySplitMergeWrapper;

@RestController
@RequestMapping(value = "service/resourceSummary/")
public class ResourceSummaryController {
	
	@Autowired
	private ResourceSummaryService resourceSummaryService;
	
	
	@RequestMapping(value = "getResourceSummaries", method = RequestMethod.GET)
	public List<ResourceSummary> getResourceSummaries(@RequestParam(required =true) String jobNo, 
													@RequestParam(name="subcontractNo") String subcontractNo, 
													@RequestParam(name="objectCode") String objectCode){
		List<ResourceSummary> resourceSummaries = null;
		try {
			
			resourceSummaries = resourceSummaryService.getResourceSummaries(jobNo, subcontractNo, objectCode);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return resourceSummaries;
	}
	
	@RequestMapping(value = "getResourceSummariesForAddendum", method = RequestMethod.GET)
	public List<ResourceSummary> getResourceSummariesForAddendum(@RequestParam(required =true) String jobNo){
		List<ResourceSummary> resourceSummaries = null;
		try {
			
			resourceSummaries = resourceSummaryService.getResourceSummariesForAddendum(jobNo);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return resourceSummaries;
	}
	
	@RequestMapping(value = "getResourceSummariesBySC", method = RequestMethod.GET)
	public List<ResourceSummary> getResourceSummariesBySC(@RequestParam(required =true) String jobNo, 
													@RequestParam(name="subcontractNo") String subcontractNo 
													){
		List<ResourceSummary> resourceSummaries = null;
		try {
			
			resourceSummaries = resourceSummaryService.getResourceSummariesBySC(jobNo, subcontractNo);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return resourceSummaries;
	}
	
	@RequestMapping(value = "getResourceSummariesByAccountCode", method = RequestMethod.GET)
	public List<ResourceSummary> getResourceSummariesByAccountCode(@RequestParam(required =true) String jobNo, 
													@RequestParam(name="subcontractNo") String subcontractNo,
													@RequestParam(name="objectCode") String objectCode,
													@RequestParam(name="subsidiaryCode") String subsidiaryCode
													){
		List<ResourceSummary> resourceSummaries = null;
		try {
			
			resourceSummaries = resourceSummaryService.getResourceSummariesByAccountCode(jobNo, subcontractNo, objectCode, subsidiaryCode);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return resourceSummaries;
	}
	
	@RequestMapping(value = "getResourceSummariesByLineType", method = RequestMethod.GET)
	public List<ResourceSummary> getResourceSummariesByLineType(@RequestParam(required =true) String jobNo, 
													@RequestParam(name="subcontractNo") String subcontractNo,
													@RequestParam(name="objectCode") String objectCode,
													@RequestParam(name="subsidiaryCode") String subsidiaryCode,
													@RequestParam(name="lineType") String lineType,
													@RequestParam(name="resourceNo") Integer resourceNo
													){
		List<ResourceSummary> resourceSummaries = null;
		try {
			
			resourceSummaries = resourceSummaryService.getResourceSummariesByLineType(jobNo, subcontractNo, objectCode, subsidiaryCode, lineType, resourceNo);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return resourceSummaries;
	}
	
	
	@RequestMapping(value = "getResourceSummariesForIV", method = RequestMethod.GET)
	public IVInputPaginationWrapper getResourceSummariesForIV(@RequestParam(required =true) String jobNo){
		IVInputPaginationWrapper ivInputPaginationWrapper = null;
		try {
			
			ivInputPaginationWrapper = resourceSummaryService.obtainResourceSummariesForIV(jobNo);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return ivInputPaginationWrapper;
	}
	
	@RequestMapping(value = "getResourceSummariesGroupByObjectCode", method = RequestMethod.GET)
	public List<ResourceSummayDashboardDTO> getResourceSummariesGroupByObjectCode(@RequestParam(required =true) String jobNo){
		List<ResourceSummayDashboardDTO> rsList = null;
		try {
			rsList = resourceSummaryService.getResourceSummariesGroupByObjectCode(jobNo);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return rsList;
	}


	@RequestMapping(value = "addResourceSummary", method = RequestMethod.POST)
	public String addResourceSummary(@RequestParam(required =true) String jobNo, 
									@RequestParam(required =true) String repackagingId,
									 @RequestBody ResourceSummary resourceSummary){
		String result = "";
		try {
			result = resourceSummaryService.addResourceSummary(jobNo, Long.valueOf(repackagingId), resourceSummary);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return result;
	}
	
	@RequestMapping(value = "deleteResources", method = RequestMethod.POST)
	public String deleteResources( @RequestBody List<ResourceSummary> resourceSummaryList){
		String result = "";
		try {
			result = resourceSummaryService.deleteResources(resourceSummaryList);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return result;
	}

	@RequestMapping(value = "updateResourceSummaries", method = RequestMethod.POST)
	public String updateResourceSummaries(@RequestParam(required =true) String jobNo, 
									 @RequestBody List<ResourceSummary> resourceSummaryList){
		BQResourceSummaryWrapper wrapper = new BQResourceSummaryWrapper();
		try {
			wrapper = resourceSummaryService.updateResourceSummaries(resourceSummaryList, jobNo);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return wrapper.getError();
	}
	
	
	@RequestMapping(value = "splitOrMergeResources", method = RequestMethod.POST)
	public String splitOrMergeResources(@RequestParam(required =true) String repackagingId, 
										 @RequestBody ResourceSummarySplitMergeWrapper resourceSummarySplitMergeWrapper){
		BQResourceSummaryWrapper wrapper = new BQResourceSummaryWrapper();
		try {
			wrapper = resourceSummaryService.splitOrMergeResources(resourceSummarySplitMergeWrapper.getOldResourceSummaryList(), resourceSummarySplitMergeWrapper.getNewResourceSummaryList(), Long.valueOf(repackagingId));
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return wrapper.getError();
	}
	
	
	@RequestMapping(value = "updateIVAmount", method = RequestMethod.POST)
	public String updateIVAmount(@RequestBody List<ResourceSummary> resourceSummaryList){
		String result = "";
		try {
			result = resourceSummaryService.updateIVAmount(resourceSummaryList);
		} catch (Exception e) {
			result = "IV cannot be updated.";
			e.printStackTrace();
		} 
		return result;
	}
	
	@RequestMapping(value = "postIVAmounts", method = RequestMethod.POST)
	public String postIVAmounts(@RequestParam(required =true) String jobNo, @RequestParam(required =true) boolean finalized){
		String result = "";
		try {
			result = resourceSummaryService.postIVAmounts(jobNo, finalized);
		} catch (Exception e) {
			result = "IV cannot be updated.";
			e.printStackTrace();
		} 
		return result;
	}

}

