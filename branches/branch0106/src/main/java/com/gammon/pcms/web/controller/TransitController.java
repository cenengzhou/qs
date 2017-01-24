/**
 * PCMS-TC
 * com.gammon.pcms.web.controller
 * TransitController.java
 * @since Jun 29, 2016 5:45:47 PM
 * @author tikywong
 */
package com.gammon.pcms.web.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.qs.domain.AppTransitUom;
import com.gammon.qs.domain.Transit;
import com.gammon.qs.domain.TransitBpi;
import com.gammon.qs.domain.TransitCodeMatch;
import com.gammon.qs.domain.TransitResource;
import com.gammon.qs.service.transit.TransitService;

@RestController
@RequestMapping(value = "service/transit/")
public class TransitController {
	@Autowired
	private TransitService transitService;

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
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('TransitController','confirmResourcesAndCreatePackages', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "confirmResourcesAndCreatePackages", method = RequestMethod.POST)
	public String confirmResourcesAndCreatePackages(@RequestParam String jobNumber){
		return transitService.confirmResourcesAndCreatePackages(jobNumber);
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('TransitController','completeTransit', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "completeTransit", method = RequestMethod.POST)
	public String completeTransit(@RequestParam String jobNumber){
		return transitService.completeTransit(jobNumber);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('TransitController','saveTransitResourcesList', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "saveTransitResourcesList", method = RequestMethod.POST)
	public String saveTransitResourcesList(@RequestParam String jobNumber, @RequestBody List<TransitResource> resourcesList,
																HttpServletRequest request, HttpServletResponse response) throws Exception{
		String result = null;
		result = transitService.saveTransitResources(jobNumber, resourcesList);
		if(result != null){
			response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
		}
		return result;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('TransitController','saveTransitResources', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "saveTransitResources", method = RequestMethod.POST)
	public String saveTransitResources(@RequestParam String jobNumber, @RequestBody TransitResource resources,
													HttpServletRequest request, HttpServletResponse response) throws Exception{
		String result = null;
		result = saveTransitResourcesList(jobNumber, Collections.singletonList(resources), request, response);
		return result;
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('TransitController','createOrUpdateTransitHeader', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "createOrUpdateTransitHeader", method = RequestMethod.POST)
	public String createOrUpdateTransitHeader(@RequestParam(required = true) String jobNo, 
												@RequestParam(required = true) String estimateNo, 
												@RequestParam(required = true) String matchingCode, 
												@RequestParam(required = true) boolean newJob) throws Exception{
		String result = null;
		result = transitService.createOrUpdateTransitHeader(jobNo, estimateNo, matchingCode, newJob);
		return result;
	}

}
