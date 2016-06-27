/**
 * pcms-tc
 * com.gammon.pcms.web.controller
 * PaymentController.java
 * @since Jun 14, 2016 6:04:10 PM
 * @author koeyyeung
 */
package com.gammon.pcms.web.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.PaymentCert;
import com.gammon.qs.domain.PaymentCertDetail;
import com.gammon.qs.service.PaymentService;
import com.gammon.qs.wrapper.paymentCertView.PaymentCertViewWrapper;
import com.gammon.qs.wrapper.scPayment.SCPaymentCertsWrapper;

@RestController
@RequestMapping(value = "service"/*,
				consumes = MediaType.APPLICATION_JSON_VALUE,
				produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"*/)
public class PaymentController {
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private PaymentService paymentService;
	
	
	@RequestMapping(value = "getPaymentCertList", method = RequestMethod.GET)
	public SCPaymentCertsWrapper getPaymentCertList(@RequestParam(name="jobNo") String jobNo, @RequestParam(name="subcontractNo") String subcontractNo){
		logger.info("IN Controller");;
		SCPaymentCertsWrapper scPaymentCertsWrapper = null;
		try{
			scPaymentCertsWrapper = paymentService.obtainSCPackagePaymentCertificates(jobNo, subcontractNo);
		}catch(DatabaseOperationException databaseOperationException){
			logger.error("Database Exception: ");
			databaseOperationException.printStackTrace();
		}
		return scPaymentCertsWrapper;
	}
	
	@RequestMapping(value = "getPaymentCert", method = RequestMethod.GET)
	public PaymentCert getPaymentCert(@RequestParam(name="jobNo") String jobNo, 
													@RequestParam(name="subcontractNo") String subcontractNo,
													@RequestParam(name="paymentCertNo") String paymentCertNo){
		PaymentCert scPaymentCert = null;
		try{
			scPaymentCert = paymentService.obtainPaymentCertificate(jobNo, subcontractNo, Integer.valueOf(paymentCertNo));
		}catch(DatabaseOperationException databaseOperationException){
			logger.error("Database Exception: ");
			databaseOperationException.printStackTrace();
		}
		return scPaymentCert;
	}
	
	@RequestMapping(value = "getPaymentDetailList", method = RequestMethod.GET)
	public List<PaymentCertDetail> getPaymentDetailList(@RequestParam(name="jobNo") String jobNo, 
													@RequestParam(name="subcontractNo") String subcontractNo,
													@RequestParam(name="paymentCertNo") String paymentCertNo){
		List<PaymentCertDetail> paymentDetailList = null;
		try{
			paymentDetailList = paymentService.obtainPaymentDetailList(jobNo, subcontractNo, Integer.valueOf(paymentCertNo));
		}catch(Exception exception){
			logger.error("Exception: ");
			exception.printStackTrace();
		}
		return paymentDetailList;
	}
	
	@RequestMapping(value = "getPaymentCertSummary", method = RequestMethod.GET)
	public PaymentCertViewWrapper getSCPaymentCertSummary(@RequestParam(name="jobNo") String jobNo, 
																	@RequestParam(name="subcontractNo") String subcontractNo,
																	@RequestParam(name="paymentCertNo") String paymentCertNo){
		PaymentCertViewWrapper paymentCertViewWrapper = null;
		try{
			paymentCertViewWrapper = paymentService.getSCPaymentCertSummaryWrapper(jobNo, subcontractNo, paymentCertNo, true);
		}catch(Exception exception){
			logger.error("Exception: ");
			exception.printStackTrace();
		}
		return paymentCertViewWrapper;
	}
}
