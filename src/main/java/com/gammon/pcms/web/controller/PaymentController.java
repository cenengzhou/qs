/**
 * pcms-tc
 * com.gammon.pcms.web.controller
 * PaymentController.java
 * @since Jun 14, 2016 6:04:10 PM
 * @author koeyyeung
 */
package com.gammon.pcms.web.controller;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.pcms.scheduler.service.PaymentPostingService;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.PaymentCert;
import com.gammon.qs.domain.PaymentCertDetail;
import com.gammon.qs.service.PaymentService;
import com.gammon.qs.wrapper.paymentCertView.PaymentCertViewWrapper;
import com.gammon.qs.wrapper.scPayment.PaymentDueDateAndValidationResponseWrapper;
import com.gammon.qs.wrapper.scPayment.SCPaymentCertsWrapper;

@RestController
@RequestMapping(value = "service/payment/"/*,
				consumes = MediaType.APPLICATION_JSON_VALUE,
				produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"*/)
public class PaymentController {
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private PaymentPostingService paymentPostingService;	
	
	
	@RequestMapping(value = "getLatestPaymentCert", method = RequestMethod.GET)
	public PaymentCert getLatestPaymentCert(@RequestParam(name="jobNo") String jobNo, @RequestParam(name="subcontractNo") String subcontractNo){
		PaymentCert paymentCert = null;
		try{
			paymentCert = paymentService.obtainPaymentLatestCert(jobNo, subcontractNo);
		}catch(DatabaseOperationException databaseOperationException){
			databaseOperationException.printStackTrace();
		}
		return paymentCert;
	}
	
	@RequestMapping(value = "getPaymentCertList", method = RequestMethod.GET)
	public List<PaymentCert> getPaymentCertList(@RequestParam(name="jobNo") String jobNo, @RequestParam(name="subcontractNo") String subcontractNo){
		List<PaymentCert> paymentCertList = null;
		try{
			paymentCertList = paymentService.getPaymentCertList(jobNo, subcontractNo);
		}catch(DatabaseOperationException databaseOperationException){
			databaseOperationException.printStackTrace();
		}
		return paymentCertList;
	}
	
	@RequestMapping(value = "getPaymentCert", method = RequestMethod.GET)
	public PaymentCert getPaymentCert(@RequestParam(name="jobNo") String jobNo, 
													@RequestParam(name="subcontractNo") String subcontractNo,
													@RequestParam(name="paymentCertNo") String paymentCertNo){
		PaymentCert scPaymentCert = null;
		try{
			scPaymentCert = paymentService.obtainPaymentCertificate(jobNo, subcontractNo, Integer.valueOf(paymentCertNo));
		}catch(DatabaseOperationException databaseOperationException){
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
			exception.printStackTrace();
		}
		return paymentCertViewWrapper;
	}
	
	@RequestMapping(value = "calculatePaymentDueDate", method = RequestMethod.GET)
	public PaymentDueDateAndValidationResponseWrapper calculatePaymentDueDate(@RequestParam(required = true) String jobNo, 
															@RequestParam(required = true) String subcontractNo, 
															@RequestParam(required = true) Integer mainCertNo, 
															@RequestParam(required = false) Date asAtDate,
															@RequestParam(required = false) Date ipaOrInvoiceDate,
															@RequestParam(required = false) Date dueDate){
		PaymentDueDateAndValidationResponseWrapper paymentCertViewWrapper = null;
		try{
			paymentCertViewWrapper = paymentService.calculatePaymentDueDate(jobNo, subcontractNo, mainCertNo, asAtDate, ipaOrInvoiceDate, dueDate);
		}catch(Exception exception){
			exception.printStackTrace();
		}
		return paymentCertViewWrapper;
	}
	
	
	@RequestMapping(value = "createPayment", method = RequestMethod.POST)
	public String createPayment(@RequestParam(name="jobNo") String jobNo,
												@RequestParam(name="subcontractNo") String subcontractNo){
		String result = "";
		try{
			result = paymentService.createPayment(jobNo, subcontractNo);
		}catch(Exception exception){
			exception.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping(value = "updateF58011FromSCPaymentCertManually", method = RequestMethod.POST)
	public void updateF58011FromSCPaymentCertManually(){
		paymentService.updateF58011FromSCPaymentCertManually();
	}
	
	@RequestMapping(value = "updatePaymentCert", method = RequestMethod.POST)
	public String updateSubcontract(@RequestBody PaymentCert paymentCert) {
		if(paymentCert.getId() == null) throw new IllegalArgumentException("Invalid Payment Cert");
		String result = paymentService.updateSCPaymentCertAdmin(paymentCert);
		return result;
	}

	@RequestMapping(value = "runPaymentPosting", method = RequestMethod.POST)
	public void runPaymentPosting(){
		paymentPostingService.runPaymentPosting();
	}
}
