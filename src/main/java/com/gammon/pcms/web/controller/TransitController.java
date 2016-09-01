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
@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsEnq())")
@RequestMapping(value = "service/transit/")
public class TransitController {
	@Autowired
	private TransitService transitService;

	@RequestMapping(value = "obtainTransitCodeMatcheList", method = RequestMethod.POST)
	public List<TransitCodeMatch> obtainTransitCodeMatcheList(@RequestParam(defaultValue = "") String matchingType, 
			@RequestParam(defaultValue = "") String resourceCode,
			@RequestParam(defaultValue = "") String objectCode, 
			@RequestParam(defaultValue = "") String subsidiaryCode) {
		return transitService.obtainTransitCodeMatcheList(matchingType, resourceCode, objectCode, subsidiaryCode);
	}

	@RequestMapping(value = "obtainTransitUomMatcheList", method = RequestMethod.POST)
	public List<AppTransitUom> obtainTransitUomMatcheList(@RequestParam(defaultValue = "") String causewayUom, 
			@RequestParam(defaultValue = "") String jdeUom) {
		return transitService.obtainTransitUomMatcheList(causewayUom, jdeUom);
	}
	
	@RequestMapping(value = "getTransit", method = RequestMethod.POST)
	public Transit getTransit(@RequestParam String jobNumber){
		return transitService.getTransitHeader(jobNumber);
	}
	
	@RequestMapping(value = "getTransitBQItems", method = RequestMethod.POST)
	public List<TransitBpi> getTransitBQItems(@RequestParam String jobNumber){
		return transitService.getTransitBQItems(jobNumber);
	}

	@RequestMapping(value = "getTransitResources", method = RequestMethod.POST)
	public List<TransitResource> getTransitResources(@RequestParam String jobNumber){
		return transitService.searchTransitResources(jobNumber);
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "confirmResourcesAndCreatePackages", method = RequestMethod.POST)
	public String confirmResourcesAndCreatePackages(@RequestParam String jobNumber){
		return transitService.confirmResourcesAndCreatePackages(jobNumber);
	}

	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "completeTransit", method = RequestMethod.POST)
	public String completeTransit(@RequestParam String jobNumber){
		return transitService.completeTransit(jobNumber);
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "saveTransitResourcesList", method = RequestMethod.POST)
	public String saveTransitResourcesList(@RequestParam String jobNumber, @RequestBody List<TransitResource> resourcesList,
																HttpServletRequest request, HttpServletResponse response) throws Exception{
		String result = null;
//		try{
			result = transitService.saveTransitResources(jobNumber, resourcesList);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
		if(result != null){
			response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
		}
		return result;
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "saveTransitResources", method = RequestMethod.POST)
	public String saveTransitResources(@RequestParam String jobNumber, @RequestBody TransitResource resources,
													HttpServletRequest request, HttpServletResponse response) throws Exception{
		String result = null;
//		try{
			result = saveTransitResourcesList(jobNumber, Collections.singletonList(resources), request, response);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}

		return result;
	}

	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "createOrUpdateTransitHeader", method = RequestMethod.POST)
	public String createOrUpdateTransitHeader(@RequestParam(required = true) String jobNo, 
												@RequestParam(required = true) String estimateNo, 
												@RequestParam(required = true) String matchingCode, 
												@RequestParam(required = true) boolean newJob) throws Exception{
		String result = null;
//		try{
			result = transitService.createOrUpdateTransitHeader(jobNo, estimateNo, matchingCode, newJob);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}

		return result;
	}
	
}
