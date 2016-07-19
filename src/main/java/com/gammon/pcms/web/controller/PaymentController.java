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
	public PaymentCert getLatestPaymentCert(@RequestParam(required = true) String jobNo, @RequestParam(required = true) String subcontractNo){
		PaymentCert paymentCert = null;
		try{
			paymentCert = paymentService.obtainPaymentLatestCert(jobNo, subcontractNo);
		}catch(DatabaseOperationException databaseOperationException){
			databaseOperationException.printStackTrace();
		}
		return paymentCert;
	}
	
	@RequestMapping(value = "getPaymentCertList", method = RequestMethod.GET)
	public List<PaymentCert> getPaymentCertList(@RequestParam(required = true) String jobNo, @RequestParam(required = true) String subcontractNo){
		List<PaymentCert> paymentCertList = null;
		try{
			paymentCertList = paymentService.getPaymentCertList(jobNo, subcontractNo);
		}catch(DatabaseOperationException databaseOperationException){
			databaseOperationException.printStackTrace();
		}
		return paymentCertList;
	}
	
	@RequestMapping(value = "getPaymentCert", method = RequestMethod.GET)
	public PaymentCert getPaymentCert(@RequestParam(required = true) String jobNo, 
													@RequestParam(required = true) String subcontractNo,
													@RequestParam(required = true) String paymentCertNo){
		PaymentCert scPaymentCert = null;
		try{
			scPaymentCert = paymentService.obtainPaymentCertificate(jobNo, subcontractNo, Integer.valueOf(paymentCertNo));
		}catch(DatabaseOperationException databaseOperationException){
			databaseOperationException.printStackTrace();
		}
		return scPaymentCert;
	}
	
	@RequestMapping(value = "getPaymentDetailList", method = RequestMethod.GET)
	public List<PaymentCertDetail> getPaymentDetailList(@RequestParam(required = true) String jobNo, 
													@RequestParam(required = true) String subcontractNo,
													@RequestParam(required = true) String paymentCertNo){
		List<PaymentCertDetail> paymentDetailList = null;
		try{
			paymentDetailList = paymentService.obtainPaymentDetailList(jobNo, subcontractNo, Integer.valueOf(paymentCertNo));
		}catch(Exception exception){
			exception.printStackTrace();
		}
		return paymentDetailList;
	}
	
	@RequestMapping(value = "getPaymentCertSummary", method = RequestMethod.GET)
	public PaymentCertViewWrapper getSCPaymentCertSummary(@RequestParam(required = true) String jobNo, 
																	@RequestParam(required = true) String subcontractNo,
																	@RequestParam(required = true) String paymentCertNo){
		PaymentCertViewWrapper paymentCertViewWrapper = null;
		try{
			paymentCertViewWrapper = paymentService.getSCPaymentCertSummaryWrapper(jobNo, subcontractNo, paymentCertNo, true);
		}catch(Exception exception){
			exception.printStackTrace();
		}
		return paymentCertViewWrapper;
	}
	
	@RequestMapping(value = "getGSTAmount", method = RequestMethod.GET)
	public Double getGSTAmount(@RequestParam(required = true) String jobNo, 
								@RequestParam(required = true) String subcontractNo,
								@RequestParam(required = true) Integer paymentCertNo, 
								@RequestParam(required = true) String lineType){
		Double gstAmount = null;
		try{
			gstAmount = paymentService.obtainPaymentGstAmount(jobNo, subcontractNo, paymentCertNo, lineType);
		}catch(Exception exception){
			exception.printStackTrace();
		}
		return gstAmount;
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
	public String createPayment(@RequestParam(required = true) String jobNo,
												@RequestParam(required = true) String subcontractNo){
		String result = "";
		try{
			result = paymentService.createPayment(jobNo, subcontractNo);
		}catch(Exception exception){
			exception.printStackTrace();
		}
		return result;
	}
	
	
	@RequestMapping(value = "updatePaymentCertificate", method = RequestMethod.POST)
	public String updatePaymentCertificate(@RequestParam(required = true) String jobNo,
												@RequestParam(required = true) String subcontractNo,
												@RequestParam(required = true) Integer paymentCertNo,
												@RequestParam(required = true) String paymentTerms,
												@RequestParam(required = true) Double gstPayable,
												@RequestParam(required = true) Double gstReceivable,
												@RequestBody PaymentCert paymentCert){
		String result = "";
		try{
			result = paymentService.updatePaymentCertificate(jobNo, subcontractNo, paymentCertNo, paymentTerms,  paymentCert, gstPayable, gstReceivable);
		}catch(Exception exception){
			exception.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping(value = "updatePaymentDetails", method = RequestMethod.POST)
	public String updatePaymentDetails(@RequestParam(required = true) String jobNo,
												@RequestParam(required = true) String subcontractNo,
												@RequestParam(required = true) Integer paymentCertNo,
												@RequestParam(required = true) String paymentType,
												@RequestBody(required = false) List<PaymentCertDetail> paymentDetails){
		String result = "";
		try{
			result = paymentService.updatePaymentDetails(jobNo, subcontractNo, paymentCertNo, paymentType, paymentDetails);
		}catch(Exception exception){
			exception.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping(value = "submitPayment", method = RequestMethod.POST)
	public String submitPayment(@RequestParam(required = true) String jobNo,
												@RequestParam(required = true) String subcontractNo,
												@RequestParam(required = true) Integer paymentCertNo){
		String result = "";
		try{
			result = paymentService.submitPayment(jobNo, subcontractNo, paymentCertNo);
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
	public String updatePaymentCert(@RequestBody PaymentCert paymentCert) {
		if(paymentCert.getId() == null) throw new IllegalArgumentException("Invalid Payment Cert");
		String result = paymentService.updateSCPaymentCertAdmin(paymentCert);
		return result;
	}

	@RequestMapping(value = "runPaymentPosting", method = RequestMethod.POST)
	public void runPaymentPosting(){
		paymentPostingService.runPaymentPosting();
	}
}
