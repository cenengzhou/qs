package com.gammon.qs.client.repository;

import java.util.Date;
import java.util.List;

import com.gammon.qs.domain.MainCertificateContraCharge;
import com.gammon.qs.domain.MainContractCertificate;
import com.gammon.qs.wrapper.MainCertReceiveDateWrapper;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.mainCertContraCharge.MainCertContraChargeWrapper;
import com.gammon.qs.wrapper.mainContractCert.MainContractCertEnquiryResultWrapper;
import com.gammon.qs.wrapper.mainContractCert.MainContractCertEnquirySearchingWrapper;
import com.gammon.qs.wrapper.mainContractCert.MainContractCertWrapper;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MainContractCertificateRepositoryRemoteAsync {
	public void getMainContractCertificateList(String jobNumber, AsyncCallback<List<MainContractCertificate>> asyncCallback );
	public void obtainMainContractCertificateList(MainContractCertEnquirySearchingWrapper wrapper, int pageNum, AsyncCallback<MainContractCertEnquiryResultWrapper> asyncCallback );
	public void getMainContractCertificateByPage(String jobNumber, int pageNum, AsyncCallback<PaginationWrapper<MainContractCertificate>> asyncCallback);
	public void getMainCertReceiveDateAndAmount(String company, String refDocNo, AsyncCallback<List<MainCertReceiveDateWrapper>> callback);
	public void updateMainContractCert(MainContractCertificate updatedMainCert,AsyncCallback<String> asyncCallback);
	public void addMainContractCert(MainContractCertificate newMainContractCert,AsyncCallback<String> asyncCallback);
	public void getMainContractCert(String jobNumber, Integer mainCertNumber, AsyncCallback<MainContractCertificate> asyncCallback);
	public void insertMainContractCert(MainContractCertWrapper mainCertWrapper, AsyncCallback<Long> asyncCallback);
	public void insertAndPostMainContractCert(String jobNumber, Integer mainCertNumber, Date asAtDate, AsyncCallback<String> asyncCallback);
	public void insertMainCertContraCharge(List<MainCertContraChargeWrapper> mainCertContraChargeWrapperList, AsyncCallback<Long> asyncCallback);
	public void getMainCertContraCharge(String objectCode, String subsidiaryCode, MainContractCertificate mainCert,AsyncCallback<MainCertificateContraCharge> asyncCallback);
	public void getMainCertCertContraChargeList(MainContractCertificate mainCert,AsyncCallback<List<MainCertificateContraCharge>> asyncCallback);
	public void addMainCertContraCharge(MainCertificateContraCharge mainCertContraCharge,AsyncCallback<String> asyncCallback);
	public void deleteMainCertContraCharge(MainCertificateContraCharge mainCertContraCharge,AsyncCallback<Boolean> asyncCallback);
	public void updateMainCertContraChargeList(List<MainCertificateContraCharge> contraChargeList,MainContractCertificate mainCert,AsyncCallback<String> asyncCallback);
	public void postMainCertToARInterface(String jobNumber,AsyncCallback<String> asyncCallback);
	public void insertIPA(MainContractCertificate mainCert, String userID, AsyncCallback<Long> asyncCallback);
	public void insertIPAAndUpdateMainContractCert(MainContractCertificate mainCert, AsyncCallback<String> asyncCallback);
	public void insertAsAtDate(String jobNumber, Integer mainCertNumber, Date asAtDate, String userId, AsyncCallback<Long> asyncCallback);
	public void updateMainCertificateStatus(String jobNumber, Integer mainCertNumber, String newCertificateStatus, AsyncCallback<Boolean> asyncCallback);
	public void resetMainContractCert(MainContractCertificate toBeUpdatedMainCert, AsyncCallback<String> asyncCallback);
	public void confirmMainContractCert(MainContractCertificate toBeUpdatedMainCert, AsyncCallback<String> asyncCallback);
	public void obtainPaidMainCertList(String jobNo, AsyncCallback<List<Integer>> asyncCallback);
	public void submitNegativeMainCertForApproval(String jobNumber, Integer mainCertNumber, Double certAmount, String userID, AsyncCallback<String> asyncCallback);
	public void updateMainCertFromF03B14Manually (AsyncCallback<Boolean> callback);
	public void getTotalPostContraChargeAmt(MainContractCertificate mainContractCertificate, AsyncCallback<Double> callback);
	public void getCertifiedContraChargeAmtByContraChargeDetails(MainContractCertificate mainContractCertificate, AsyncCallback<Double> callback);
	public void deleteMainCertContraCharge(MainContractCertificate mainContractCertificate, AsyncCallback<Integer> callback);
}
