/**
 * PCMS-TC
 * com.gammon.pcms.web.controller
 * TransitController.java
 * @since Jun 29, 2016 5:45:47 PM
 * @author tikywong
 */
package com.gammon.pcms.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gammon.pcms.dto.rs.provider.response.resourceSummary.ResourceSummayDashboardDTO;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.AppTransitUom;
import com.gammon.qs.domain.Transit;
import com.gammon.qs.domain.TransitBpi;
import com.gammon.qs.domain.TransitCodeMatch;
import com.gammon.qs.domain.TransitResource;
import com.gammon.qs.service.JobInfoService;
import com.gammon.qs.service.admin.MailContentGenerator;
import com.gammon.qs.service.transit.TransitImportResponse;
import com.gammon.qs.service.transit.TransitService;
import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "service/transit/")
public class TransitController {
	@Autowired
	private TransitService transitService;
	@Autowired
	private JobInfoService jobInfoService;
	@Autowired
	private MailContentGenerator mailContentGenerator;
	private Logger logger = Logger.getLogger(getClass());
	private String RESPONSE_CONTENT_TYPE_TEXT_HTML = "text/html";
	private String RESPONSE_HEADER_NAME_CACHE_CONTROL = "Cache-Control";
	private String RESPONSE_HEADER_VALUE_NO_CACHE = "no-cache";

	@PreAuthorize(value = "@GSFService.isFnEnabled('TransitController','getIncompleteTransitList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getIncompleteTransitList", method = RequestMethod.GET)
	public List<Transit> getIncompleteTransitList() throws Exception{
		return transitService.getIncompleteTransitList();
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('TransitController','obtainTransitCodeMatcheList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "obtainTransitCodeMatcheList", method = RequestMethod.GET)
	public List<TransitCodeMatch> obtainTransitCodeMatcheList(@RequestParam(defaultValue = "") String matchingType, 
			@RequestParam(defaultValue = "") String resourceCode,
			@RequestParam(defaultValue = "") String objectCode, 
			@RequestParam(defaultValue = "") String subsidiaryCode) {
		return transitService.obtainTransitCodeMatcheList(matchingType, resourceCode, objectCode, subsidiaryCode);
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('TransitController','obtainTransitUomMatcheList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "obtainTransitUomMatcheList", method = RequestMethod.GET)
	public List<AppTransitUom> obtainTransitUomMatcheList(@RequestParam(defaultValue = "") String causewayUom, 
			@RequestParam(defaultValue = "") String jdeUom) {
		return transitService.obtainTransitUomMatcheList(causewayUom, jdeUom);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('TransitController','getTransit', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getTransit", method = RequestMethod.GET)
	public Transit getTransit(@RequestParam String jobNumber){
		return transitService.getTransitHeader(jobNumber);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('TransitController','getTransitBQItems', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getTransitBQItems", method = RequestMethod.GET)
	public List<TransitBpi> getTransitBQItems(@RequestParam String jobNumber){
		return transitService.getTransitBQItems(jobNumber);
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('TransitController','getTransitResources', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getTransitResources", method = RequestMethod.GET)
	public List<TransitResource> getTransitResources(@RequestParam String jobNumber){
		return transitService.searchTransitResources(jobNumber);
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getTransitTotalAmount", method = RequestMethod.GET)
	public Double getTransitTotalAmount(@RequestParam String jobNo, @RequestParam String type){
		Double totalAmount = transitService.getTransitTotalAmount(jobNo, type);
		return totalAmount;
	}

	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getBQResourceGroupByObjectCode", method = RequestMethod.GET)
	public List<ResourceSummayDashboardDTO> getBQResourceGroupByObjectCode(@RequestParam String jobNo){
		List<ResourceSummayDashboardDTO> dataList = new ArrayList<ResourceSummayDashboardDTO>();
		
		dataList = transitService.getBQResourceGroupByObjectCode(jobNo);
		return dataList;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('TransitController','confirmResourcesAndCreatePackages', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "confirmResourcesAndCreatePackages", method = RequestMethod.POST)
	public String confirmResourcesAndCreatePackages(@RequestParam String jobNumber, @RequestParam Boolean createPackage) throws DatabaseOperationException{
		String msg = transitService.confirmResourcesAndCreatePackages(jobNumber, createPackage);
		transitService.unlockTransitHeader(jobNumber);;
		return msg;
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('TransitController','completeTransit', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "completeTransit", method = RequestMethod.POST)
	public String completeTransit(@RequestParam String jobNumber) throws DatabaseOperationException{
		String msg = transitService.completeTransit(jobNumber);
		transitService.unlockTransitHeader(jobNumber);
		return msg;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('TransitController','saveTransitResourcesList', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "saveTransitResourcesList", method = RequestMethod.POST)
	public String saveTransitResourcesList(@RequestParam String jobNumber, @RequestBody List<TransitResource> resourcesList) throws Exception{
		String result = null;
		result = transitService.saveTransitResources(jobNumber, resourcesList);
		return result;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('TransitController','saveTransitResources', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "saveTransitResources", method = RequestMethod.POST)
	public String saveTransitResources(@RequestParam String jobNumber, @RequestBody TransitResource resources) throws Exception{
		String result = null;
		result = saveTransitResourcesList(jobNumber, Collections.singletonList(resources));
		return result;
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('TransitController','createOrUpdateTransitHeader', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "createOrUpdateTransitHeader", method = RequestMethod.POST)
	public String createOrUpdateTransitHeader(@RequestParam(required = true) String jobNo, 
												@RequestParam(required = true) String estimateNo, 
												@RequestParam(required = true) String matchingCode, 
												@RequestParam(required = true) boolean newJob) throws Exception{
		String result = null;
		result = transitService.createOrUpdateTransitHeader(jobNo, estimateNo, matchingCode, newJob);
		return result;
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('TransitController','uploadTransit', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "transitUpload", method = RequestMethod.POST)
	public void uploadTransit(@RequestParam(required = true, value = "jobNumber") String jobNumber, 
								@RequestParam(required = true, value = "type") String type,
								@RequestParam("files") List<MultipartFile> multipartFiles,
								HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("Upload Transit - START");
		
		response.setContentType(RESPONSE_CONTENT_TYPE_TEXT_HTML);
		response.setHeader(RESPONSE_HEADER_NAME_CACHE_CONTROL, RESPONSE_HEADER_VALUE_NO_CACHE);
	
//		List<MultipartFile> multipartFiles = FileUtil.getMultipartFiles(request);

		TransitImportResponse transitImportResponse = null;
		
		for (MultipartFile multipartFile : multipartFiles) {
			byte[] file = multipartFile.getBytes();
			if (file != null) {

				transitImportResponse =  transitService.importBqItemsOrResourcesFromXls(jobNumber, type, file); 
				
				Map<String, Object> resultMap = new HashMap<String, Object>();
				if (transitImportResponse == null) {
					resultMap.put("success", true);
					logger.info("Upload Transit: success.");
				} else {
					resultMap.put("success", false);
					resultMap.put("error", transitImportResponse);
					logger.info("error: " + transitImportResponse.getMessage());
					transitService.unlockTransitHeader(jobNumber);
				}
				
//				response.setContentType("text/html");
				response.getWriter().print((new Gson()).toJson(transitImportResponse));
				logger.info("Upload Transit -END");
			}
		}
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('SystemController','invalidateSessionList', @securityConfig.getRolePcmsImsAdmin(), @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "unlockTransitAdmin", method = RequestMethod.POST)
	public void unlockTransitAdmin(@RequestParam(required = true) String jobNumber) throws Exception {
		String result = jobInfoService.canAdminJob(jobNumber);
		if(StringUtils.isEmpty(result)){
			transitService.unlockTransitHeader(jobNumber);
			mailContentGenerator.sendAdminFnEmail("unlockTransitAdmin", "jobNumber:" + jobNumber);
		} else {
			throw new IllegalAccessException(result);
		}
	}

}
