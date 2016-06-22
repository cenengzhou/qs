/**

 * pcms-tc
 * com.gammon.pcms.web.controller
 * ResourceSummaryController.java
 * @since Jun 20, 2016
 * @author koeyyeung
 */
package com.gammon.pcms.web;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.qs.domain.ResourceSummary;
import com.gammon.qs.service.ResourceSummaryService;
import com.gammon.qs.wrapper.IVInputPaginationWrapper;

@RestController
@RequestMapping(value = "service")
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
	
	
	
}
