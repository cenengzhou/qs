/**

 * pcms-tc
 * com.gammon.pcms.web.controller
 * ResourceSummaryController.java
 * @since Jun 20, 2016
 * @author koeyyeung
 */
package com.gammon.pcms.web.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gammon.pcms.application.GlobalExceptionHandler;
import com.gammon.qs.domain.Repackaging;
import com.gammon.qs.service.RepackagingDetailService;
import com.gammon.qs.service.RepackagingService;
import com.gammon.qs.service.admin.MailService;
import com.gammon.qs.wrapper.EmailMessage;
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
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private MailService mailService;
	
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
			result = "Repackaging cannot be deleted.";
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

	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "sendEmailToReviewer", method = RequestMethod.POST)
	public boolean sendEmailToReviewer(@RequestBody Map<String, String> mailData){
		boolean result;
		String toAddress = mailData.get("contacts");
		String ccAddress = mailData.get("contactsCc");
		String emailSubject = mailData.get("emailSubject");
		String emailContext = mailData.get("emailContext");
		
		EmailMessage emailMessage = new EmailMessage();
		List<String> recipients = Arrays.asList(toAddress.split(";"));
		List<String> ccRecipients = Arrays.asList(ccAddress.split(";"));
		if(!toAddress.isEmpty() && recipients.size() > 0) emailMessage.setRecipients(recipients);
		if(!ccAddress.isEmpty() && ccRecipients.size() > 0) emailMessage.setCcRecipients(ccRecipients);
		emailMessage.setSubject(emailSubject);
		emailMessage.setContent(emailContext);
		result = mailService.sendEmail(emailMessage);
		return result;
	}
	
	@RequestMapping(value = "getReviewerList", method = RequestMethod.GET)
	public String getReviewerList(@Value("#{'file:///' + linkConfig.getPcmsLink('REVIEWER_JSON')}") Resource reviewerJson) throws IOException{
		BufferedReader  br = new BufferedReader (new InputStreamReader(reviewerJson.getInputStream()));
		String result = "", line;
		while((line = br.readLine()) != null){
			result += line;
		}
		return result;
	}

}
