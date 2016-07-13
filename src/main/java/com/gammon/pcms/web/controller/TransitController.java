/**
 * PCMS-TC
 * com.gammon.pcms.web.controller
 * TransitController.java
 * @since Jun 29, 2016 5:45:47 PM
 * @author tikywong
 */
package com.gammon.pcms.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.pcms.dto.rs.provider.response.PCMSDTO;
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

	@RequestMapping(value = "confirmBPIResources", method = RequestMethod.POST)
	public PCMSDTO confirmBPIResources() {
		return null;
	}

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
	
	@RequestMapping(value = "confirmResourcesAndCreatePackages", method = RequestMethod.POST)
	public String confirmResourcesAndCreatePackages(@RequestParam String jobNumber){
		return transitService.confirmResourcesAndCreatePackages(jobNumber);
	}

	@RequestMapping(value = "completeTransit", method = RequestMethod.POST)
	public String completeTransit(@RequestParam String jobNumber){
		return transitService.completeTransit(jobNumber);
	}

}
