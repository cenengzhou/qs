package com.gammon.pcms.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.pcms.service.HTMLService;

@RestController
@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsEnq())")
@RequestMapping(value = "service/html/")
public class HTMLController {

	@Autowired
	private HTMLService htmlService;


	@RequestMapping(value = "makeHTMLStringForSCPaymentCert", method = RequestMethod.POST)
	public String makeHTMLStringForSCPaymentCert(
			@RequestParam String jobNumber, @RequestParam String subcontractNumber, 
			@RequestParam String paymentNo, @RequestParam String htmlVersion){
		return htmlService.makeHTMLStringForSCPaymentCert(jobNumber, subcontractNumber, paymentNo, htmlVersion);
	}

}
