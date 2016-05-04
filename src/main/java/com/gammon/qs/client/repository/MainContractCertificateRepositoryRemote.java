package com.gammon.qs.client.repository;

import java.util.Date;
import java.util.List;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.MainCertificateContraCharge;
import com.gammon.qs.domain.MainContractCertificate;
import com.gammon.qs.wrapper.MainCertReceiveDateWrapper;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.mainCertContraCharge.MainCertContraChargeWrapper;
import com.gammon.qs.wrapper.mainContractCert.MainContractCertEnquiryResultWrapper;
import com.gammon.qs.wrapper.mainContractCert.MainContractCertEnquirySearchingWrapper;
import com.gammon.qs.wrapper.mainContractCert.MainContractCertWrapper;
import com.google.gwt.user.client.rpc.RemoteService;

public interface MainContractCertificateRepositoryRemote extends RemoteService {
	public List<MainContractCertificate> getMainContractCertificateList(String jobNumber) throws DatabaseOperationException;
	public MainContractCertEnquiryResultWrapper obtainMainContractCertificateList(MainContractCertEnquirySearchingWrapper wrapper, int pageNum) throws DatabaseOperationException;
	public PaginationWrapper<MainContractCertificate> getMainContractCertificateByPage(String jobNumber, int pageNum) throws DatabaseOperationException;
	public List<MainCertReceiveDateWrapper> getMainCertReceiveDateAndAmount(String company, String refDocNo)  throws DatabaseOperationException;
	public String updateMainContractCert(MainContractCertificate updatedMainCert) throws DatabaseOperationException;
	public String addMainContractCert(MainContractCertificate newMainContractCert) throws DatabaseOperationException;
	public MainContractCertificate getMainContractCert(String jobNumber, Integer mainCertNumber) throws DatabaseOperationException;
	public Long insertMainContractCert(MainContractCertWrapper mainCertWrapper)throws DatabaseOperationException;
	public String insertAndPostMainContractCert(String jobNumber, Integer mainCertNumber, Date asAtDate) throws DatabaseOperationException;
	public Long insertMainCertContraCharge(List<MainCertContraChargeWrapper> mainCertContraChargeWrapperList)throws DatabaseOperationException;
	public MainCertificateContraCharge getMainCertContraCharge(String objectCode, String subsidiaryCode, MainContractCertificate mainCert) throws DatabaseOperationException;
	public List<MainCertificateContraCharge> getMainCertCertContraChargeList(MainContractCertificate mainCert) throws DatabaseOperationException;
	public String addMainCertContraCharge(MainCertificateContraCharge mainCertContraCharge)throws DatabaseOperationException;
	public Boolean deleteMainCertContraCharge(MainCertificateContraCharge mainCertContraCharge) throws DatabaseOperationException;
	public String updateMainCertContraChargeList(List<MainCertificateContraCharge> contraChargeList, MainContractCertificate mainCert) throws DatabaseOperationException;
	public String postMainCertToARInterface(String jobNumber)throws DatabaseOperationException;
	public Long insertIPA(MainContractCertificate mainCert, String userID) throws DatabaseOperationException;
	public String insertIPAAndUpdateMainContractCert(MainContractCertificate mainCert) throws DatabaseOperationException;
	public Long insertAsAtDate(String jobNumber, Integer mainCertNumber, Date asAtDate, String userId)throws Exception;
	public Boolean updateMainCertificateStatus(String jobNumber, Integer mainCertNumber, String newCertificateStatus) throws DatabaseOperationException;
	public String resetMainContractCert(MainContractCertificate toBeUpdatedMainCert) throws DatabaseOperationException;
	public String confirmMainContractCert(MainContractCertificate toBeUpdatedMainCert) throws DatabaseOperationException;
	public List<Integer> obtainPaidMainCertList(String jobNo) throws DatabaseOperationException;
	public String submitNegativeMainCertForApproval(String jobNumber, Integer mainCertNumber, Double certAmount, String userID) throws DatabaseOperationException;
	public Boolean updateMainCertFromF03B14Manually () throws Exception;
	public Double getTotalPostContraChargeAmt(MainContractCertificate mainContractCertificate) throws DatabaseOperationException;
	public Double getCertifiedContraChargeAmtByContraChargeDetails(MainContractCertificate mainContractCertificate) throws DatabaseOperationException;
	public Integer deleteMainCertContraCharge(MainContractCertificate mainContractCertificate) throws DatabaseOperationException;
}
