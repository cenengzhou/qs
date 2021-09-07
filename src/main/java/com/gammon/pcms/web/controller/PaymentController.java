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

import com.gammon.pcms.scheduler.service.PaymentPostingService;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.PaymentCert;
import com.gammon.qs.domain.PaymentCertDetail;
import com.gammon.qs.service.JobInfoService;
import com.gammon.qs.service.PaymentService;
import com.gammon.qs.service.admin.MailContentGenerator;
import com.gammon.qs.wrapper.paymentCertView.PaymentCertViewWrapper;
import com.gammon.qs.wrapper.scPayment.PaymentCertWrapper;
import com.gammon.qs.wrapper.scPayment.PaymentDueDateAndValidationResponseWrapper;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "service/payment/")
public class PaymentController {
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private PaymentService paymentService;
	@Autowired JobInfoService jobInfoService;
	@Autowired
	private PaymentPostingService paymentPostingService;

	@Autowired
	private MailContentGenerator mailContentGenerator;
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('PaymentController','getLatestPaymentCert', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getLatestPaymentCert", method = RequestMethod.GET)
	public PaymentCert getLatestPaymentCert(@RequestParam(required = true) String jobNo, @RequestParam(required = true) String subcontractNo) throws DatabaseOperationException{
		PaymentCert paymentCert = null;
		paymentCert = paymentService.obtainPaymentLatestCert(jobNo, subcontractNo);
		return paymentCert;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('PaymentController','getPaymentCertList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getPaymentCertList", method = RequestMethod.GET)
	public List<PaymentCert> getPaymentCertList(@RequestParam(required = true) String jobNo, @RequestParam(required = true) String subcontractNo) throws DatabaseOperationException{
		List<PaymentCert> paymentCertList = null;
		paymentCertList = paymentService.getPaymentCertList(jobNo, subcontractNo);
		return paymentCertList;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('PaymentController','getPaymentCert', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getPaymentCert", method = RequestMethod.GET)
	public PaymentCert getPaymentCert(@RequestParam(required = true) String jobNo, 
													@RequestParam(required = true) String subcontractNo,
													@RequestParam(required = true) String paymentCertNo) throws NumberFormatException, DatabaseOperationException{
		PaymentCert scPaymentCert = null;
		scPaymentCert = paymentService.obtainPaymentCertificate(jobNo, subcontractNo, Integer.valueOf(paymentCertNo));
		return scPaymentCert;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('PaymentController','getPaymentDetailList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getPaymentDetailList", method = RequestMethod.GET)
	public List<PaymentCertDetail> getPaymentDetailList(@RequestParam(required = true) String jobNo, 
													@RequestParam(required = true) String subcontractNo,
													@RequestParam(required = true) String paymentCertNo) throws NumberFormatException, Exception{
		List<PaymentCertDetail> paymentDetailList = null;
		paymentDetailList = paymentService.obtainPaymentDetailList(jobNo, subcontractNo, Integer.valueOf(paymentCertNo));
		return paymentDetailList;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('PaymentController','getTotalPostedCertAmount', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getTotalPostedCertAmount", method = RequestMethod.GET)
	public Double getTotalPostedCertAmount(@RequestParam(required = true) String jobNo, 
																	@RequestParam(required = true) String subcontractNo){
		Double totalPostedCertAmount = null;
		totalPostedCertAmount = paymentService.getTotalPostedCertAmount(jobNo, subcontractNo);
		return totalPostedCertAmount;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('PaymentController','getSCPaymentCertSummary', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getPaymentCertSummary", method = RequestMethod.GET)
	public PaymentCertViewWrapper getSCPaymentCertSummary(@RequestParam(required = true) String jobNo, 
																	@RequestParam(required = true) String subcontractNo,
																	@RequestParam(required = true) String paymentCertNo) throws Exception{
		PaymentCertViewWrapper paymentCertViewWrapper = null;
		paymentCertViewWrapper = paymentService.getSCPaymentCertSummaryWrapper(jobNo, subcontractNo, paymentCertNo);
		return paymentCertViewWrapper;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('PaymentController','getGSTAmount', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getGSTAmount", method = RequestMethod.GET)
	public Double getGSTAmount(@RequestParam(required = true) String jobNo, 
								@RequestParam(required = true) String subcontractNo,
								@RequestParam(required = true) Integer paymentCertNo, 
								@RequestParam(required = true) String lineType){
		Double gstAmount = null;
		gstAmount = paymentService.obtainPaymentGstAmount(jobNo, subcontractNo, paymentCertNo, lineType);
		return gstAmount;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('PaymentController','getPaymentResourceDistribution', @securityConfig.getRolePcmsEnq())")
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
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('PaymentController','calculatePaymentDueDate', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "calculatePaymentDueDate", method = RequestMethod.GET)
	public PaymentDueDateAndValidationResponseWrapper calculatePaymentDueDate(@RequestParam(required = true) String jobNo, 
															@RequestParam(required = true) String subcontractNo, 
															@RequestParam(required = false) Integer mainCertNo, 
															@RequestParam(required = false) String asAtDate,
															@RequestParam(required = false) String ipaOrInvoiceDate,
															@RequestParam(required = false) String dueDate){
		PaymentDueDateAndValidationResponseWrapper paymentCertViewWrapper = null;
		
		paymentCertViewWrapper = paymentService.calculatePaymentDueDate(jobNo, subcontractNo, mainCertNo, asAtDate, ipaOrInvoiceDate, dueDate, PaymentCert.BYPASS_PAYMENT_TERMS.N.toString());
		return paymentCertViewWrapper;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('PaymentController','createPayment', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "createPayment", method = RequestMethod.POST)
	public String createPayment(@RequestParam(required = true) String jobNo,
												@RequestParam(required = true) String subcontractNo){
		String result = "";
		result = paymentService.createPayment(jobNo, subcontractNo);
		return result;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('PaymentController','updatePaymentCertificate', @securityConfig.getRolePcmsQs())")
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
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('PaymentController','updatePaymentDetails', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "updatePaymentDetails", method = RequestMethod.POST)
	public String updatePaymentDetails(@RequestParam(required = true) String jobNo,
												@RequestParam(required = true) String subcontractNo,
												@RequestParam(required = true) Integer paymentCertNo,
												@RequestParam(required = false) String paymentType,
												@RequestBody(required = false) List<PaymentCertDetail> paymentDetails) throws NumberFormatException, Exception{
		String result = "";
		result = paymentService.updatePaymentDetails(jobNo, subcontractNo, paymentCertNo, paymentType, paymentDetails);
		return result;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('PaymentController','submitPayment', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "submitPayment", method = RequestMethod.POST)
	public String submitPayment(@RequestParam(required = true) String jobNo,
												@RequestParam(required = true) String subcontractNo,
												@RequestParam(required = true) Integer paymentCertNo) throws Exception{
		String result = "";
		result = paymentService.submitPayment(jobNo, subcontractNo, paymentCertNo);
		return result;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('PaymentController','updateF58011FromSCPaymentCertManually', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "updateF58011FromSCPaymentCertManually", method = RequestMethod.POST)
	public void updateF58011FromSCPaymentCertManually(){
		paymentService.updateF58011FromSCPaymentCertManually();
		mailContentGenerator.sendAdminFnEmail("updateF58011FromSCPaymentCertManually", "");
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('PaymentController','updatePaymentCert', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "updatePaymentCert", method = RequestMethod.POST)
	public String updatePaymentCert(@RequestBody PaymentCert paymentCert) throws Exception {
		if(paymentCert.getId() == null) throw new IllegalArgumentException("Invalid Payment Cert");
		String result = jobInfoService.canAdminJob(paymentCert.getJobNo());
		if(StringUtils.isEmpty(result)){
			result = paymentService.updateSCPaymentCertAdmin(paymentCert);
			mailContentGenerator.sendAdminFnEmail("updatePaymentCert", paymentCert.toString());
		} else {
			throw new IllegalAccessException(result);
		}
		return result;
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('PaymentController','runPaymentPosting', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "runPaymentPosting", method = RequestMethod.POST)
	public void runPaymentPosting(){
		paymentPostingService.runPaymentPosting();
		mailContentGenerator.sendAdminFnEmail("runPaymentPosting", "");
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('PaymentController','obtainPaymentCertificateList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "obtainPaymentCertificateList", method = RequestMethod.POST)
	public List<PaymentCertWrapper> obtainPaymentCertificateList(@RequestBody PaymentCertWrapper paymentCertWrapper, @RequestParam String dueDateType) throws DatabaseOperationException{
		List<PaymentCertWrapper> wrapperList = new ArrayList<PaymentCertWrapper>();
		wrapperList.addAll(paymentService.obtainPaymentCertificateList(paymentCertWrapper, dueDateType));
		return wrapperList;
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('PaymentController','deletePendingPaymentAndDetails', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "deletePendingPaymentAndDetails", method = RequestMethod.POST)
	public void deletePendingPaymentAndDetails(@RequestParam long paymentCertId) throws Exception{
		paymentService.deletePendingPaymentAndDetails(paymentCertId);
		mailContentGenerator.sendAdminFnEmail("deletePendingPaymentAndDetails", "paymentCertId:" + paymentCertId);
	}
	

}
