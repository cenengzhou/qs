/**

 * pcms-tc
 * com.gammon.pcms.web.controller
 * ResourceSummaryController.java
 * @since Jun 20, 2016
 * @author koeyyeung
 */
package com.gammon.pcms.web.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.qs.domain.Repackaging;
import com.gammon.qs.service.RepackagingDetailService;
import com.gammon.qs.service.RepackagingService;
import com.gammon.qs.wrapper.RepackagingDetailComparisonWrapper;
import com.gammon.qs.wrapper.RepackagingPaginationWrapper;

@RestController
@RequestMapping(value = "service/repackaging/")
public class RepackagingController {
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private RepackagingService repackagingService;
	@Autowired
	private RepackagingDetailService  repackagingDetailService;
	
	
	@RequestMapping(value = "getRepackagingListByJobNo", method = RequestMethod.GET)
	public List<Repackaging> getRepackagingListByJobNo(@RequestParam(required = true) String jobNo){
		logger.info("jobNo: "+jobNo);
		List<Repackaging> repackagingList = null;
		try {
			
			repackagingList = repackagingService.getRepackagingListByJobNo(jobNo);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return repackagingList;
	}
	
	@RequestMapping(value = "getLatestRepackaging", method = RequestMethod.GET)
	public Repackaging getLatestRepackaging(@RequestParam(required = true) String jobNo){
		logger.info("jobNo: "+jobNo);
		Repackaging repackaging = null;
		try {
			repackaging = repackagingService.getLatestRepackaging(jobNo);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return repackaging;
	}
	
	@RequestMapping(value = "getRepackagingDetails", method = RequestMethod.GET)
	public RepackagingPaginationWrapper<RepackagingDetailComparisonWrapper> getRepackagingDetails(@RequestParam(required = true) String repackagingID, 
																									@RequestParam(required = true) boolean changesOnly){
		RepackagingPaginationWrapper<RepackagingDetailComparisonWrapper> wrapper = null;
		try {
			wrapper = repackagingDetailService.getRepackagingDetails(Long.valueOf(repackagingID), changesOnly);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return wrapper;
	}
	
	@RequestMapping(value = "addRepackaging", method = RequestMethod.POST)
	public String addRepackaging(@RequestParam(required = true) String jobNo){
		String result = null;
		try {
			result = repackagingService.addRepackaging(jobNo);
		} catch (Exception e) {
			result = "Repackaging cannot be created.";
			e.printStackTrace();
		} 
		return result;
	}
	
	@RequestMapping(value = "updateRepackaging", method = RequestMethod.POST)
	public String  updateRepackaging(@RequestBody Repackaging repackaging){
		String result = null;
		try {
			result = repackagingService.updateRepackaging(repackaging);
		} catch (Exception e) {
			result = "Repackaging cannot be updated.";
			e.printStackTrace();
		} 
		return result;
	}
	
	@RequestMapping(value = "generateSnapshot", method = RequestMethod.POST)
	public String  generateSnapshot(@RequestParam(required = true) String id, @RequestParam(required = true) String jobNo){
		String result = null;
		try {
			result = repackagingService.generateSnapshot(Long.valueOf(id), jobNo);
		} catch (Exception e) {
			result = "Snapshot cannot be generated.";
			e.printStackTrace();
		} 
		return result;
	}
	 
	@RequestMapping(value = "deleteRepackaging", method = RequestMethod.DELETE)
	public String deleteRepackaging(@RequestParam(required = true) String id){
		String result = null;
		try {
			result = repackagingService.deleteRepackaging(Long.valueOf(id));
		} catch (Exception e) {
			result = "Repackaging cannot be generated.";
			e.printStackTrace();
		} 
		return result;
	}

	@RequestMapping(value = "confirmAndPostRepackaingDetails", method = RequestMethod.POST)
	public String confirmAndPostRepackaingDetails(@RequestParam(required = true) String repackagingID){
		String result = null;
		try {
			result = repackagingService.confirmAndPostRepackaingDetails(Long.valueOf(repackagingID));
			
		} catch (Exception e) {
			result = "Repackaging cannot be generated.";
			e.printStackTrace();
		} 
		return result;
	}
	
	
	
}
