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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.pcms.application.GlobalExceptionHandler;
import com.gammon.qs.domain.Repackaging;
import com.gammon.qs.service.RepackagingDetailService;
import com.gammon.qs.service.RepackagingService;
import com.gammon.qs.wrapper.RepackagingDetailComparisonWrapper;
import com.gammon.qs.wrapper.RepackagingPaginationWrapper;

@RestController
@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsEnq())")
@RequestMapping(value = "service/repackaging/")
public class RepackagingController {
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private RepackagingService repackagingService;
	@Autowired
	private RepackagingDetailService  repackagingDetailService;
	
	
	@RequestMapping(value = "getRepackagingEntry", method = RequestMethod.GET)
	public Repackaging getRepackagingEntry(@RequestParam(required = true) String repackagingID) throws Exception{
		Repackaging repackaging = null;
		repackaging = repackagingService.getRepackagingEntry(Long.valueOf(repackagingID));
		return repackaging;
	}
	
	@RequestMapping(value = "getRepackagingListByJobNo", method = RequestMethod.GET)
	public List<Repackaging> getRepackagingListByJobNo(@RequestParam(required = true) String jobNo) throws Exception{
		logger.info("jobNo: "+jobNo);
		List<Repackaging> repackagingList = null;
		repackagingList = repackagingService.getRepackagingListByJobNo(jobNo);
		return repackagingList;
	}
	
	@RequestMapping(value = "getLatestRepackaging", method = RequestMethod.GET)
	public Repackaging getLatestRepackaging(@RequestParam(required = true) String jobNo) throws Exception{
		logger.info("jobNo: "+jobNo);
		Repackaging repackaging = null;
		repackaging = repackagingService.getLatestRepackaging(jobNo);
		return repackaging;
	}
	
	@RequestMapping(value = "getRepackagingDetails", method = RequestMethod.GET)
	public RepackagingPaginationWrapper<RepackagingDetailComparisonWrapper> getRepackagingDetails(@RequestParam(required = true) String repackagingID, 
																									@RequestParam(required = true) boolean changesOnly) throws NumberFormatException, Exception{
		RepackagingPaginationWrapper<RepackagingDetailComparisonWrapper> wrapper = null;
		wrapper = repackagingDetailService.getRepackagingDetails(Long.valueOf(repackagingID), changesOnly);
		return wrapper;
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "addRepackaging", method = RequestMethod.POST)
	public String addRepackaging(@RequestParam(required = true) String jobNo){
		String result = null;
		try {
			result = repackagingService.addRepackaging(jobNo);
		} catch (Exception e) {
			result = "Repackaging cannot be created.";
			e.printStackTrace();
			GlobalExceptionHandler.checkAccessDeniedException(e);
		} 
		return result;
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "updateRepackaging", method = RequestMethod.POST)
	public String  updateRepackaging(@RequestBody Repackaging repackaging){
		String result = null;
		try {
			result = repackagingService.updateRepackaging(repackaging);
		} catch (Exception e) {
			result = "Repackaging cannot be updated.";
			e.printStackTrace();
			GlobalExceptionHandler.checkAccessDeniedException(e);
		} 
		return result;
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "generateSnapshot", method = RequestMethod.POST)
	public String  generateSnapshot(@RequestParam(required = true) String id, @RequestParam(required = true) String jobNo){
		String result = null;
		try {
			result = repackagingService.generateSnapshot(Long.valueOf(id), jobNo);
		} catch (Exception e) {
			result = "Snapshot cannot be generated.";
			e.printStackTrace();
			GlobalExceptionHandler.checkAccessDeniedException(e);
		} 
		return result;
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "deleteRepackaging", method = RequestMethod.DELETE)
	public String deleteRepackaging(@RequestParam(required = true) String id){
		String result = null;
		try {
			result = repackagingService.deleteRepackaging(Long.valueOf(id));
		} catch (Exception e) {
			result = "Repackaging cannot be generated.";
			e.printStackTrace();
			GlobalExceptionHandler.checkAccessDeniedException(e);
		} 
		return result;
	}

	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQsReviewer)")
	@RequestMapping(value = "confirmAndPostRepackaingDetails", method = RequestMethod.POST)
	public String confirmAndPostRepackaingDetails(@RequestParam(required = true) String repackagingID){
		String result = null;
		try {
			result = repackagingService.confirmAndPostRepackaingDetails(Long.valueOf(repackagingID));
			
		} catch (Exception e) {
			result = "Repackaging cannot be generated.";
			e.printStackTrace();
			GlobalExceptionHandler.checkAccessDeniedException(e);
		} 
		return result;
	}

}
