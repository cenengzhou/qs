package com.gammon.qs.web;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.client.repository.PaymentRepositoryRemote;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.domain.SCPaymentCert;
import com.gammon.qs.domain.SCPaymentDetail;
import com.gammon.qs.service.PaymentService;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.PaymentPaginationWrapper;
import com.gammon.qs.wrapper.directPayment.SCPaymentExceptionalWrapper;
import com.gammon.qs.wrapper.paymentCertView.PaymentCertViewWrapper;
import com.gammon.qs.wrapper.scPayment.PaymentDueDateAndValidationResponseWrapper;
import com.gammon.qs.wrapper.scPayment.SCPaymentCertWrapper;
import com.gammon.qs.wrapper.scPayment.SCPaymentCertsWrapper;
import com.gammon.qs.wrapper.submitPayment.SubmitPaymentResponseWrapper;
import com.gammon.qs.wrapper.updatePaymentCert.UpdatePaymentCertificateResultWrapper;
import com.gammon.qs.wrapper.updatePaymentCert.UpdatePaymentCertificateWrapper;

import net.sf.gilead.core.PersistentBeanManager;

@Service
public class PaymentRepositoryController extends GWTSpringController implements PaymentRepositoryRemote{

	private static final long serialVersionUID = 6423992999267164976L;
	@Autowired
	private PaymentService paymentRepository;
	@Override
	@Autowired
	public void setBeanManager(PersistentBeanManager manager) {
		super.setBeanManager(manager);
	}
	
	public List<SCPackage> obtainPackageListForSCPaymentPanel(String jobNumber) throws Exception{
		return paymentRepository.obtainPackageListForSCPaymentPanel(jobNumber);
	}
	
	public SCPaymentCertsWrapper obtainSCPackagePaymentCertificates(String jobNumber, String packageNo) throws DatabaseOperationException{
		return paymentRepository.obtainSCPackagePaymentCertificates(jobNumber, packageNo);		
	}
	
	public List<SCPaymentDetail> getAllSCPaymentCertPaymentDetail(String jobNumber, String packageNo, Integer paymentCertNo ) throws Exception{
		return paymentRepository.obtainPaymentDetailList(jobNumber, packageNo, paymentCertNo);
	}
	public PaymentCertViewWrapper getSCPaymentDetailConsolidateView(String jobNumber, String packageNo, Integer paymentCertNo) throws Exception{
		return paymentRepository.calculatePaymentCertificateSummary(jobNumber, packageNo, paymentCertNo);
	}
	
	public UpdatePaymentCertificateResultWrapper updatePaymentCertificate(UpdatePaymentCertificateWrapper updatePaymentCertificateWrapper){		
		return paymentRepository.updatePaymentCertificate(updatePaymentCertificateWrapper);
	}

	public SubmitPaymentResponseWrapper submitPayment(String jobNumber, Integer packageNo, Integer paymentCertNo, Double certAmount, String userID, String currentMainCertNo) throws Exception {	
		return paymentRepository.submitPayment(jobNumber, packageNo, paymentCertNo, certAmount, userID, currentMainCertNo);
	}

	public PaymentPaginationWrapper refreshPaymentDetailsCache(String jobNumber, String packageNo, Integer paymentCertNo, String lineTypeFilter) throws Exception {
		return paymentRepository.refreshPaymentDetailsCache(jobNumber, packageNo, paymentCertNo, lineTypeFilter);
	}
	
	public PaymentPaginationWrapper getPaymentDetailsByPage(int pageNum) {
		return paymentRepository.getPaymentDetailsByPage(pageNum);
	}

	public String callSCPaymentBatch() {
		paymentRepository.runPaymentPosting();
		return "";
	}

	public PaymentDueDateAndValidationResponseWrapper calculatePaymentDueDate(String jobNumber, String packageNo, Integer paymentCertNo, Integer mainCertNo, Date asAtDate, Date ipaRecDate, Date dueDate) {
		return paymentRepository.calculatePaymentDueDate(jobNumber, packageNo, mainCertNo, asAtDate, ipaRecDate, dueDate);
	}

	public SCPaymentCert getSCPaymentCert(String jobNumber, String packageNo, String paymentCertNo) throws DatabaseOperationException {
		return paymentRepository.getSCPaymentCert(jobNumber,packageNo,paymentCertNo);
	}

	public Boolean updateSCPaymentCertStatus(String jobNumber, String packageNo, String paymentCertNo, String newValue) throws DatabaseOperationException {
		return paymentRepository.updateSCPaymentCertStatus(jobNumber,packageNo,paymentCertNo,newValue);
	}

	public String updateSCPaymentCertAdmin(SCPaymentCert paymentCert) {
		return paymentRepository.updateSCPaymentCertAdmin(paymentCert);
	}

	public PaginationWrapper<SCPaymentExceptionalWrapper> obtainSubcontractorPaymentExceptionReport(String division, String company, String jobNumber, String vendorNo, String packageNo, String scStatus, String username) throws DatabaseOperationException {
		return paymentRepository.obtainSubcontractorPaymentExceptionReportInPagination(division, company, jobNumber, vendorNo, packageNo, scStatus, username);
	}

	public PaginationWrapper<SCPaymentExceptionalWrapper> obtainSubcontractorPaymentExceptionReportInPaginationWithPageNo(Integer pageNo) throws DatabaseOperationException {
		return paymentRepository.obtainSubcontractorPaymentExceptionReportInPaginationWithPageNo(pageNo);
	}

	public PaginationWrapper<SCPaymentCertWrapper> obtainSCPaymentCertificatePaginationWrapper(SCPaymentCertWrapper scPaymentCertWrapper, String dueDateType) throws DatabaseOperationException {
		return paymentRepository.obtainSCPaymentCertificatePaginationWrapper(scPaymentCertWrapper, dueDateType);
	}

	public PaginationWrapper<SCPaymentCertWrapper> obtainSCPaymentCertWrapperListByPage(int pageNum) {
		return paymentRepository.obtainSCPaymentCertWrapperListByPage(pageNum);
	}

	public List<SCPaymentCert> obtainSCPaymentCertListByStatus(String jobNumber, String packageNo, String status, String directPayment) throws DatabaseOperationException {
		return paymentRepository.obtainSCPaymentCertListByStatus(jobNumber, packageNo, status, directPayment);
	}

	public SCPaymentCert obtainPaymentLatestCert(String jobNumber, String packageNo) throws DatabaseOperationException {
		return paymentRepository.obtainPaymentLatestCert(jobNumber, packageNo);
	}

	public Boolean deletePaymentCert(SCPaymentCert scPaymentCert) {
		return paymentRepository.deletePaymentCert(scPaymentCert);
	}

	//Stored Procedure Call
	public Boolean updateF58011FromSCPaymentCertManually() throws Exception {
		return paymentRepository.updateF58011FromSCPaymentCertManually();
	}

	public String obtainPaymentHoldMessage() {
		return paymentRepository.obtainPaymentHoldMessage();
	}
}
