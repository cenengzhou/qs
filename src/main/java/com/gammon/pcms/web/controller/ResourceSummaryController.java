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

@RestController
@RequestMapping(value = "service/resourceSummary/")
public class ResourceSummaryController {
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private ResourceSummaryService resourceSummaryService;
	
	
	@RequestMapping(value = "getResourceSummaries", method = RequestMethod.GET)
	public List<ResourceSummary> getResourceSummaries(@RequestParam(name="jobNo") String jobNo, 
													@RequestParam(name="packageNo") String packageNo, 
													@RequestParam(name="objectCode") String objectCode){
		logger.info("jobNo: "+jobNo);
		List<ResourceSummary> resourceSummaries = null;
		try {
			
			resourceSummaries = resourceSummaryService.getResourceSummaries(jobNo, packageNo, objectCode);
			logger.info("resourceSummaries size: "+resourceSummaries.size());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return resourceSummaries;
	}
	
	@RequestMapping(value = "getResourceSummariesForIV", method = RequestMethod.GET)
	public IVInputPaginationWrapper getResourceSummariesForIV(@RequestParam(name="jobNo") String jobNo){
		logger.info("jobNo: "+jobNo);
		IVInputPaginationWrapper ivInputPaginationWrapper = null;
		try {
			
			ivInputPaginationWrapper = resourceSummaryService.obtainResourceSummariesForIV(jobNo);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return ivInputPaginationWrapper;
	}
	
	@RequestMapping(value = "addResourceSummary", method = RequestMethod.POST)
	public String addResourceSummary(@RequestParam(name="jobNo") String jobNo, 
									@RequestParam(name="repackagingEntryId") String repackagingEntryId,
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
	public String updateResourceSummaries(@RequestParam(name="jobNo") String jobNo, 
									@Valid @RequestBody List<ResourceSummary> resourceSummaryList){
		BQResourceSummaryWrapper wrapper = new BQResourceSummaryWrapper();
		try {
			wrapper = resourceSummaryService.updateResourceSummaries(resourceSummaryList, jobNo);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return wrapper.getError();
	}
	

}

