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

import com.gammon.qs.domain.Repackaging;
import com.gammon.qs.service.RepackagingService;

@RestController
@RequestMapping(value = "service/repackaging/")
public class RepackagingController {
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private RepackagingService repackagingService;
	
	
	@RequestMapping(value = "getRepackagingEntriesByJobNo", method = RequestMethod.GET)
	public List<Repackaging> getRepackagingEntriesByJobNo(@RequestParam(name="jobNo") String jobNo){
		logger.info("jobNo: "+jobNo);
		List<Repackaging> repackagingList = null;
		try {
			
			repackagingList = repackagingService.getRepackagingEntriesByJobNo(jobNo);
			logger.info("repackagingList size: "+repackagingList.size());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return repackagingList;
	}
	
	@RequestMapping(value = "getLatestRepackagingEntry", method = RequestMethod.GET)
	public Repackaging getLatestRepackagingEntry(@RequestParam(name="jobNo") String jobNo){
		logger.info("jobNo: "+jobNo);
		Repackaging repackaging = null;
		try {
			repackaging = repackagingService.getLatestRepackagingEntry(jobNo);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return repackaging;
	}
	
	
	@RequestMapping(value = "addRepackagingEntry", method = RequestMethod.POST)
	public String addRepackagingEntry(@RequestParam(name="jobNo") String jobNo){
		String result = null;
		try {
			result = repackagingService.addRepackagingEntry(jobNo);
		} catch (Exception e) {
			result = "Repackaging cannot be created.";
			e.printStackTrace();
		} 
		return result;
	}
	
	@RequestMapping(value = "updateRepackagingEntry", method = RequestMethod.POST)
	public String  updateRepackagingEntry(@Valid @RequestBody Repackaging repackaging){
		String result = null;
		try {
			result = repackagingService.updateRepackagingEntry(repackaging.getId(), repackaging.getRemarks());
		} catch (Exception e) {
			result = "Repackaging cannot be updated.";
			e.printStackTrace();
		} 
		return result;
	}
	
	@RequestMapping(value = "generateSnapshot", method = RequestMethod.POST)
	public String  generateSnapshot(@RequestParam(name="id") String id, @RequestParam(name="jobNo") String jobNo){
		String result = null;
		try {
			result = repackagingService.generateSnapshot(Long.valueOf(id), jobNo);
		} catch (Exception e) {
			result = "Snapshot cannot be generated.";
			e.printStackTrace();
		} 
		return result;
	}
	 
	@RequestMapping(value = "deleteRepackagingEntry", method = RequestMethod.DELETE)
	public String deleteRepackagingEntry(@RequestParam(name="id") String id){
		String result = null;
		try {
			result = repackagingService.deleteRepackagingEntry(Long.valueOf(id));
		} catch (Exception e) {
			result = "Repackaging cannot be generated.";
			e.printStackTrace();
		} 
		return result;
	}
}
