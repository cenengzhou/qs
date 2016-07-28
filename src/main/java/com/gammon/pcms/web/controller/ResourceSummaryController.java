/**

 * pcms-tc
 * com.gammon.pcms.web.controller
 * ResourceSummaryController.java
 * @since Jun 20, 2016
 * @author koeyyeung
 */
package com.gammon.pcms.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.qs.domain.ResourceSummary;
import com.gammon.qs.service.ResourceSummaryService;
import com.gammon.qs.wrapper.BQResourceSummaryWrapper;
import com.gammon.qs.wrapper.IVInputPaginationWrapper;
import com.gammon.qs.wrapper.ResourceSummarySplitMergeWrapper;

@RestController
@RequestMapping(value = "service/resourceSummary/")
public class ResourceSummaryController {
	private Logger logger = Logger.getLogger(getClass());
	
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
	
	@RequestMapping(value = "addResourceSummary", method = RequestMethod.POST)
	public String addResourceSummary(@RequestParam(required =true) String jobNo, 
									@RequestParam(required =true) String repackagingEntryId,
									@Valid @RequestBody ResourceSummary resourceSummary){
		String result = "";
		try {
			result = resourceSummaryService.addResourceSummary(jobNo, Long.valueOf(repackagingEntryId), resourceSummary);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return result;
	}
	
	@RequestMapping(value = "deleteResources", method = RequestMethod.POST)
	public String deleteResources(@Valid @RequestBody List<ResourceSummary> resourceSummaryList){
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
									@Valid @RequestBody List<ResourceSummary> resourceSummaryList){
		BQResourceSummaryWrapper wrapper = new BQResourceSummaryWrapper();
		try {
			wrapper = resourceSummaryService.updateResourceSummaries(resourceSummaryList, jobNo);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return wrapper.getError();
	}
	
	
	@RequestMapping(value = "splitOrMergeResources", method = RequestMethod.POST)
	public String splitOrMergeResources(@RequestParam(required =true) String repackagingEntryId, 
										@Valid @RequestBody ResourceSummarySplitMergeWrapper resourceSummarySplitMergeWrapper){
		BQResourceSummaryWrapper wrapper = new BQResourceSummaryWrapper();
		try {
			wrapper = resourceSummaryService.splitOrMergeResources(resourceSummarySplitMergeWrapper.getOldResourceSummaryList(), resourceSummarySplitMergeWrapper.getNewResourceSummaryList(), Long.valueOf(repackagingEntryId));
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return wrapper.getError();
	}
	

}

