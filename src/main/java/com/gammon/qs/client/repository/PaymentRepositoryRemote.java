package com.gammon.qs.client.repository;

import java.util.Date;
import java.util.List;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.domain.SCPaymentCert;
import com.gammon.qs.domain.SCPaymentDetail;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.PaymentPaginationWrapper;
import com.gammon.qs.wrapper.directPayment.SCPaymentExceptionalWrapper;
import com.gammon.qs.wrapper.paymentCertView.PaymentCertViewWrapper;
import com.gammon.qs.wrapper.scPayment.PaymentDueDateAndValidationResponseWrapper;
import com.gammon.qs.wrapper.scPayment.SCPaymentCertWrapper;
import com.gammon.qs.wrapper.scPayment.SCPaymentCertsWrapper;
import com.gammon.qs.wrapper.submitPayment.SubmitPaymentResponseWrapper;
import com.gammon.qs.wrapper.updatePaymentCert.UpdatePaymentCertificateWrapper;
import com.gammon.qs.wrapper.updatePaymentCert.UpdatePaymentCertificateResultWrapper;
import com.google.gwt.user.client.rpc.RemoteService;

public interface PaymentRepositoryRemote extends RemoteService {
	public String obtainPaymentHoldMessage();
	public List<SCPaymentCert> obtainSCPaymentCertListByStatus(String jobNumber, String packageNo, String status, String directPayment) throws DatabaseOperationException;
	public List<SCPackage> obtainPackageListForSCPaymentPanel(String jobNumber) throws Exception;
	public SCPaymentCertsWrapper obtainSCPackagePaymentCertificates(String jobNumber, String packageNo) throws DatabaseOperationException;
	public SCPaymentCert obtainPaymentLatestCert(String jobNumber, String packageNo) throws DatabaseOperationException;
	public List<SCPaymentDetail> getAllSCPaymentCertPaymentDetail(String jobNumber, String packageNo, Integer paymentCertNo ) throws Exception;
	public PaymentCertViewWrapper getSCPaymentDetailConsolidateView(String jobNumber, String packageNo, Integer paymentCertNo) throws Exception;
	public UpdatePaymentCertificateResultWrapper updatePaymentCertificate(UpdatePaymentCertificateWrapper updatePaymentCertificateWrapper);
	public SubmitPaymentResponseWrapper submitPayment(String jobNumber, Integer packageNo, Integer paymentCertNo,Double certAmount, String userID, String currentMainCertNo) throws Exception;
	public PaymentPaginationWrapper refreshPaymentDetailsCache(String jobNumber, String packageNo, Integer paymentCertNo, String lineTypeFilter) throws Exception;
	public PaymentPaginationWrapper getPaymentDetailsByPage(int pageNum);
	public String callSCPaymentBatch();
	public PaymentDueDateAndValidationResponseWrapper calculatePaymentDueDate(String jobNumber, String packageNo, Integer paymentCertNo,Integer mainCertNo, Date asAtDate, Date ipaRecDate, Date dueDate);
	public SCPaymentCert getSCPaymentCert(String jobNumber, String packageNo,	String paymentCertNo) throws DatabaseOperationException;
	public Boolean updateSCPaymentCertStatus(String jobNumber, String packageNo, String paymentCertNo, String newValue) throws DatabaseOperationException;
	public String updateSCPaymentCertAdmin(SCPaymentCert paymentCert);
	//Delete
	public Boolean deletePaymentCert(SCPaymentCert scPaymentCert);

	public PaginationWrapper<SCPaymentExceptionalWrapper> obtainSubcontractorPaymentExceptionReport(String division,String company,String jobNumber, String vendorNo, String packageNo, String scStatus, String username) throws DatabaseOperationException;
	public PaginationWrapper<SCPaymentExceptionalWrapper> obtainSubcontractorPaymentExceptionReportInPaginationWithPageNo(Integer pageNo) throws DatabaseOperationException;
	public PaginationWrapper<SCPaymentCertWrapper> obtainSCPaymentCertificatePaginationWrapper(SCPaymentCertWrapper scPaymentCertWrapper, String dueDateType) throws DatabaseOperationException;
	public PaginationWrapper<SCPaymentCertWrapper> obtainSCPaymentCertWrapperListByPage(int pageNum);
	
	//Stored Procedure Call
	public Boolean updateF58011FromSCPaymentCertManually () throws Exception;
	
	
}