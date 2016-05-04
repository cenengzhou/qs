package com.gammon.qs.client.repository;

import java.util.Date;
import java.util.List;

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
import com.gammon.qs.wrapper.updatePaymentCert.UpdatePaymentCertificateResultWrapper;
import com.gammon.qs.wrapper.updatePaymentCert.UpdatePaymentCertificateWrapper;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PaymentRepositoryRemoteAsync {
	void obtainPaymentHoldMessage(AsyncCallback<String> callback);
	void obtainSCPaymentCertListByStatus(String jobNumber, String packageNo, String status, String directPayment, AsyncCallback<List<SCPaymentCert>> callback);
	void obtainPackageListForSCPaymentPanel(String jobNumber, AsyncCallback<List<SCPackage> > callback );
	void obtainSCPackagePaymentCertificates(String jobNumber, String packageNo, AsyncCallback<SCPaymentCertsWrapper> callback );
	void obtainPaymentLatestCert(String jobNumber, String packageNo, AsyncCallback<SCPaymentCert> callback);
	void getAllSCPaymentCertPaymentDetail(String jobNumber, String packageNo, Integer paymentCertNo, AsyncCallback<List<SCPaymentDetail> > callback );
	void getSCPaymentDetailConsolidateView(String jobNumber, String packageNo, Integer paymentCertNo, AsyncCallback<PaymentCertViewWrapper> callback);
	void updatePaymentCertificate(UpdatePaymentCertificateWrapper updatePaymentCertificateWrapper, AsyncCallback<UpdatePaymentCertificateResultWrapper> callback);
	void submitPayment(String jobNumber, Integer packageNo,	Integer paymentCertNo,Double certAmount,String userID, String currentMainCertNo, AsyncCallback<SubmitPaymentResponseWrapper> callback);
	void refreshPaymentDetailsCache(String jobNumber, String packageNo, Integer paymentCertNo, String lineTypeFilter, AsyncCallback<PaymentPaginationWrapper> callback);
	void getPaymentDetailsByPage(int pageNum, AsyncCallback<PaymentPaginationWrapper> callback);
	void callSCPaymentBatch(AsyncCallback<String> callback);
	void calculatePaymentDueDate(String jobNumber, String packageNo, Integer paymentCertNo,Integer mainCertNo, Date asAtDate, Date ipaRecDate,Date dueDate, AsyncCallback<PaymentDueDateAndValidationResponseWrapper> callback);
	void getSCPaymentCert(String jobNumber, String packageNo,	String paymentCertNo, AsyncCallback<SCPaymentCert> callback);
	void updateSCPaymentCertStatus(String jobNumber, String packageNo, String paymentCertNo, String newValue, AsyncCallback<Boolean> asyncCallback);
	void updateSCPaymentCertAdmin(SCPaymentCert paymentCert, AsyncCallback<String> asyncCallback);
	void obtainSubcontractorPaymentExceptionReport(String division,String company,String jobNumber, String vendorNo, String packageNo, String scStatus, String username, AsyncCallback<PaginationWrapper<SCPaymentExceptionalWrapper>> asynchCallback);
	void obtainSubcontractorPaymentExceptionReportInPaginationWithPageNo(Integer pageNo,AsyncCallback<PaginationWrapper<SCPaymentExceptionalWrapper>> asynchCallback);
	void obtainSCPaymentCertificatePaginationWrapper(SCPaymentCertWrapper scPaymentCertWrapper,String dueDateType, AsyncCallback<PaginationWrapper<SCPaymentCertWrapper>> callback);
	void obtainSCPaymentCertWrapperListByPage(int pageNum, AsyncCallback<PaginationWrapper<SCPaymentCertWrapper>> callback);
	void deletePaymentCert(SCPaymentCert scPaymentCert, AsyncCallback<Boolean> callback);
	void updateF58011FromSCPaymentCertManually (AsyncCallback<Boolean> callback);
}
