/**
 * pcms-tc
 * com.gammon.pcms.web.controller
 * PaymentController.java
 * @since Jun 14, 2016 6:04:10 PM
 * @author koeyyeung
 */
package com.gammon.pcms.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.gammon.qs.wrapper.scPayment.PaymentCertWrapper;
import com.gammon.qs.wrapper.scPayment.PaymentDueDateAndValidationResponseWrapper;

@RestController
@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsEnq())")
@RequestMapping(value = "service/payment/")
public class PaymentController {
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private PaymentPostingService paymentPostingService;	
	
	
	@RequestMapping(value = "getLatestPaymentCert", method = RequestMethod.GET)
	public PaymentCert getLatestPaymentCert(@RequestParam(required = true) String jobNo, @RequestParam(required = true) String subcontractNo) throws DatabaseOperationException{
		PaymentCert paymentCert = null;
		paymentCert = paymentService.obtainPaymentLatestCert(jobNo, subcontractNo);
		return paymentCert;
	}
	
	@RequestMapping(value = "getPaymentCertList", method = RequestMethod.GET)
	public List<PaymentCert> getPaymentCertList(@RequestParam(required = true) String jobNo, @RequestParam(required = true) String subcontractNo) throws DatabaseOperationException{
		List<PaymentCert> paymentCertList = null;
		paymentCertList = paymentService.getPaymentCertList(jobNo, subcontractNo);
		return paymentCertList;
	}
	
	@RequestMapping(value = "getPaymentCert", method = RequestMethod.GET)
	public PaymentCert getPaymentCert(@RequestParam(required = true) String jobNo, 
													@RequestParam(required = true) String subcontractNo,
													@RequestParam(required = true) String paymentCertNo) throws NumberFormatException, DatabaseOperationException{
		PaymentCert scPaymentCert = null;
		scPaymentCert = paymentService.obtainPaymentCertificate(jobNo, subcontractNo, Integer.valueOf(paymentCertNo));
		return scPaymentCert;
	}
	
	@RequestMapping(value = "getPaymentDetailList", method = RequestMethod.GET)
	public List<PaymentCertDetail> getPaymentDetailList(@RequestParam(required = true) String jobNo, 
													@RequestParam(required = true) String subcontractNo,
													@RequestParam(required = true) String paymentCertNo) throws NumberFormatException, Exception{
		List<PaymentCertDetail> paymentDetailList = null;
		paymentDetailList = paymentService.obtainPaymentDetailList(jobNo, subcontractNo, Integer.valueOf(paymentCertNo));
		return paymentDetailList;
	}
	
	@RequestMapping(value = "getTotalPostedCertAmount", method = RequestMethod.GET)
	public Double getTotalPostedCertAmount(@RequestParam(required = true) String jobNo, 
																	@RequestParam(required = true) String subcontractNo){
		Double totalPostedCertAmount = null;
		totalPostedCertAmount = paymentService.getTotalPostedCertAmount(jobNo, subcontractNo);
		return totalPostedCertAmount;
	}
	
	@RequestMapping(value = "getPaymentCertSummary", method = RequestMethod.GET)
	public PaymentCertViewWrapper getSCPaymentCertSummary(@RequestParam(required = true) String jobNo, 
																	@RequestParam(required = true) String subcontractNo,
																	@RequestParam(required = true) String paymentCertNo) throws Exception{
		PaymentCertViewWrapper paymentCertViewWrapper = null;
		paymentCertViewWrapper = paymentService.getSCPaymentCertSummaryWrapper(jobNo, subcontractNo, paymentCertNo, true);
		return paymentCertViewWrapper;
	}
	
	@RequestMapping(value = "getGSTAmount", method = RequestMethod.GET)
	public Double getGSTAmount(@RequestParam(required = true) String jobNo, 
								@RequestParam(required = true) String subcontractNo,
								@RequestParam(required = true) Integer paymentCertNo, 
								@RequestParam(required = true) String lineType){
		Double gstAmount = null;
		gstAmount = paymentService.obtainPaymentGstAmount(jobNo, subcontractNo, paymentCertNo, lineType);
		return gstAmount;
	}
	
	
	@RequestMapping(value = "getPaymentResourceDistribution", method = RequestMethod.GET)
	public Double getPaymentResourceDistribution(@RequestParam(required = true) String jobNo, 
								@RequestParam(required = true) String subcontractNo,
								@RequestParam(required = true) String lineType,
								@RequestParam(required = true) String dataType,
								@RequestParam(required = false) Integer paymentCertNo){
		Double amount = null;
		amount = paymentService.getPaymentResourceDistribution(jobNo, subcontractNo, paymentCertNo, lineType, dataType);
		return amount;
	}
	
	@RequestMapping(value = "calculatePaymentDueDate", method = RequestMethod.GET)
	public PaymentDueDateAndValidationResponseWrapper calculatePaymentDueDate(@RequestParam(required = true) String jobNo, 
															@RequestParam(required = true) String subcontractNo, 
															@RequestParam(required = false) Integer mainCertNo, 
															@RequestParam(required = false) String asAtDate,
															@RequestParam(required = false) String ipaOrInvoiceDate,
															@RequestParam(required = false) String dueDate){
		PaymentDueDateAndValidationResponseWrapper paymentCertViewWrapper = null;
		
		paymentCertViewWrapper = paymentService.calculatePaymentDueDate(jobNo, subcontractNo, mainCertNo, asAtDate, ipaOrInvoiceDate, dueDate);
		return paymentCertViewWrapper;
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "createPayment", method = RequestMethod.POST)
	public String createPayment(@RequestParam(required = true) String jobNo,
												@RequestParam(required = true) String subcontractNo){
		String result = "";
		result = paymentService.createPayment(jobNo, subcontractNo);
		return result;
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "updatePaymentCertificate", method = RequestMethod.POST)
	public String updatePaymentCertificate(@RequestParam(required = true) String jobNo,
												@RequestParam(required = true) String subcontractNo,
												@RequestParam(required = true) Integer paymentCertNo,
												@RequestParam(required = true) String paymentTerms,
												@RequestParam(required = false) Double gstPayable,
												@RequestParam(required = false) Double gstReceivable,
												@RequestBody PaymentCert paymentCert){
		String result = "";
		result = paymentService.updatePaymentCertificate(jobNo, subcontractNo, paymentCertNo, paymentTerms,  paymentCert, gstPayable, gstReceivable);
		return result;
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "updatePaymentDetails", method = RequestMethod.POST)
	public String updatePaymentDetails(@RequestParam(required = true) String jobNo,
												@RequestParam(required = true) String subcontractNo,
												@RequestParam(required = true) Integer paymentCertNo,
												@RequestParam(required = true) String paymentType,
												@RequestBody(required = false) List<PaymentCertDetail> paymentDetails){
		String result = "";
		result = paymentService.updatePaymentDetails(jobNo, subcontractNo, paymentCertNo, paymentType, paymentDetails);
		return result;
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "submitPayment", method = RequestMethod.POST)
	public String submitPayment(@RequestParam(required = true) String jobNo,
												@RequestParam(required = true) String subcontractNo,
												@RequestParam(required = true) Integer paymentCertNo) throws Exception{
		String result = "";
		result = paymentService.submitPayment(jobNo, subcontractNo, paymentCertNo);
		return result;
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "updateF58011FromSCPaymentCertManually", method = RequestMethod.POST)
	public void updateF58011FromSCPaymentCertManually(){
		paymentService.updateF58011FromSCPaymentCertManually();
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "updatePaymentCert", method = RequestMethod.POST)
	public String updatePaymentCert(@RequestBody PaymentCert paymentCert) {
		if(paymentCert.getId() == null) throw new IllegalArgumentException("Invalid Payment Cert");
		String result = paymentService.updateSCPaymentCertAdmin(paymentCert);
		return result;
	}

	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "runPaymentPosting", method = RequestMethod.POST)
	public void runPaymentPosting(){
		paymentPostingService.runPaymentPosting();
	}
	
	@RequestMapping(value = "obtainPaymentCertificateList", method = RequestMethod.POST)
	public List<PaymentCertWrapper> obtainPaymentCertificateList(@RequestBody PaymentCertWrapper paymentCertWrapper, @RequestParam String dueDateType) throws DatabaseOperationException{
		List<PaymentCertWrapper> wrapperList = new ArrayList<PaymentCertWrapper>();
		wrapperList.addAll(paymentService.obtainPaymentCertificateList(paymentCertWrapper, dueDateType));
		return wrapperList;
	}

}
