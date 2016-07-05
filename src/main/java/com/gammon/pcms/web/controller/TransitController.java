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
import com.gammon.qs.domain.TransitCodeMatch;
import com.gammon.qs.service.transit.TransitService;

@RestController
@RequestMapping(value = "service/transit/")
public class TransitController {
	@Autowired
	private TransitService transitService;

	public void importBPIItems() {
		// TODO: import Excel file
	}

	public void importBPIResources() {
		// TODO: import Excel file
	}

	@RequestMapping(value = "confirmBPIResources", method = RequestMethod.POST)
	public PCMSDTO confirmBPIResources() {
		return null;
	}

	public void printBPIItemReconcilliationReport() {
		// TODO: return PDF report
	}

	public void printBPIResourceReconcilliationReport() {
		// TODO: return PDF report
	}

	@RequestMapping(value = "completeTransit", method = RequestMethod.POST)
	public PCMSDTO completeTransit() {
		return null;
	}

	@RequestMapping(value = "ObtainTransitCodeMatcheList", method = RequestMethod.POST)
	public List<TransitCodeMatch> obtainTransitCodeMatcheList(@RequestParam(defaultValue = "") String matchingType, 
			@RequestParam(defaultValue = "") String resourceCode,
			@RequestParam(defaultValue = "") String objectCode, 
			@RequestParam(defaultValue = "") String subsidiaryCode) {
		return transitService.obtainTransitCodeMatcheList(matchingType, resourceCode, objectCode, subsidiaryCode);
	}

	@RequestMapping(value = "ObtainTransitUomMatcheList", method = RequestMethod.POST)
	public List<AppTransitUom> obtainTransitUomMatcheList(@RequestParam(defaultValue = "") String causewayUom, 
			@RequestParam(defaultValue = "") String jdeUom) {
		return transitService.obtainTransitUomMatcheList(causewayUom, jdeUom);
	}
}
