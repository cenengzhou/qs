/**
 * pcms-tc
 * com.gammon.pcms.web.controller
 * PaymentController.java
 * @since Jun 14, 2016 6:04:10 PM
 * @author koeyyeung
 */
package com.gammon.pcms.web;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.SCPaymentCert;
import com.gammon.qs.service.PaymentService;

@RestController
@RequestMapping(value = "service",
				method = RequestMethod.POST/*,
				consumes = MediaType.APPLICATION_JSON_VALUE,
				produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"*/)
public class PaymentController {
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private PaymentService paymentService;
	
	
	@RequestMapping(value = "getSCPaymentCertList.json")
	public List<SCPaymentCert> getSCPaymentCertList(@RequestParam(name="jobNo") String jobNo, @RequestParam(name="subcontractNo") String subcontractNo){
		List<SCPaymentCert> paymentCertList = null;
		try{
			paymentCertList = paymentService.obtainSCPaymentCertListByPackageNo(jobNo, Integer.valueOf(subcontractNo));
		}catch(DatabaseOperationException databaseOperationException){
			logger.error("Database Exception: ");
			databaseOperationException.printStackTrace();
		}
		return paymentCertList;
	}
}
