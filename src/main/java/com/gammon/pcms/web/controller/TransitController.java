/**
 * PCMS-TC
 * com.gammon.pcms.web.controller
 * TransitController.java
 * @since Jun 29, 2016 5:45:47 PM
 * @author tikywong
 */
package com.gammon.pcms.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.pcms.dto.rs.provider.response.PCMSDTO;
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

	@RequestMapping(value = "confirmBPIResources",
					method = RequestMethod.POST)
	public PCMSDTO confirmBPIResources() {
		return null;
	}

	public void printBPIItemReconcilliationReport() {
		// TODO: return PDF report
	}

	public void printBPIResourceReconcilliationReport() {
		// TODO: return PDF report
	}

	@RequestMapping(value = "completeTransit",
					method = RequestMethod.POST)
	public PCMSDTO completeTransit() {
		return null;
	}

}
