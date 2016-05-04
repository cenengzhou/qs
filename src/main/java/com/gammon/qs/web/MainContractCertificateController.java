package com.gammon.qs.web;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.client.repository.MainContractCertificateRepositoryRemote;
import com.gammon.qs.domain.MainCertificateContraCharge;
import com.gammon.qs.domain.MainContractCertificate;
import com.gammon.qs.service.MainContractCertificateService;
import com.gammon.qs.wrapper.MainCertReceiveDateWrapper;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.mainCertContraCharge.MainCertContraChargeWrapper;
import com.gammon.qs.wrapper.mainContractCert.MainContractCertEnquiryResultWrapper;
import com.gammon.qs.wrapper.mainContractCert.MainContractCertEnquirySearchingWrapper;
import com.gammon.qs.wrapper.mainContractCert.MainContractCertWrapper;

import net.sf.gilead.core.PersistentBeanManager;
@Service
public class MainContractCertificateController extends GWTSpringController implements MainContractCertificateRepositoryRemote {
	private static final long serialVersionUID = -467063499156181138L;
	@Autowired
	private MainContractCertificateService mainContractCertificateRepository;
	@Override
	@Autowired
	public void setBeanManager(PersistentBeanManager manager) {
		super.setBeanManager(manager);
	}
	
	public String addMainContractCert(MainContractCertificate newMainContractCert) throws DatabaseOperationException {
		return mainContractCertificateRepository.addMainContractCert(newMainContractCert);
	}

	public String updateMainContractCert(MainContractCertificate updatedMainCert) throws DatabaseOperationException {
		return mainContractCertificateRepository.updateMainContractCert(updatedMainCert);
	}

	public List<MainContractCertificate> getMainContractCertificateList(String jobNumber) throws DatabaseOperationException {
		return mainContractCertificateRepository.getMainContractCertificateList(jobNumber);
	}

	/**
	 * @author matthewatc
	 * 16:11:23 19 Dec 2011 (UTC+8)
	 * Added method to fetch certificate by page, uses PaginationWrapper. The page length is specified in com.gammon.qs.dao.MainContractCertificateHBDaoImpl.
	 */
	public PaginationWrapper<MainContractCertificate> getMainContractCertificateByPage(String jobNumber, int pageNum) throws DatabaseOperationException {
		return mainContractCertificateRepository.getMainContractCertificateByPage(jobNumber, pageNum);
	}

	public MainContractCertificate getMainContractCert(String jobNumber, Integer mainCertNumber) throws DatabaseOperationException {
		return mainContractCertificateRepository.getMainContractCert(jobNumber, mainCertNumber);
	}

	public Long insertMainContractCert(MainContractCertWrapper mainCertWrapper) throws DatabaseOperationException {
		return mainContractCertificateRepository.insertMainContractCert(mainCertWrapper);
	}

	public Long insertMainCertContraCharge(List<MainCertContraChargeWrapper> mainCertContraChargeWrapperList) throws DatabaseOperationException {
		return mainContractCertificateRepository.insertMainCertContraCharge(mainCertContraChargeWrapperList);
	}

	public MainCertificateContraCharge getMainCertContraCharge(String objectCode, String subsidiaryCode, MainContractCertificate mainCert) throws DatabaseOperationException {
		return mainContractCertificateRepository.getMainCertContraCharge(objectCode, subsidiaryCode, mainCert);
	}

	public List<MainCertificateContraCharge> getMainCertCertContraChargeList(MainContractCertificate mainCert) throws DatabaseOperationException {
		return mainContractCertificateRepository.getMainCertCertContraChargeList(mainCert);
	}

	public String addMainCertContraCharge(MainCertificateContraCharge mainCertContraCharge) throws DatabaseOperationException {
		return mainContractCertificateRepository.addMainCertContraCharge(mainCertContraCharge);
	}

	public Boolean deleteMainCertContraCharge(MainCertificateContraCharge mainCertContraCharge) throws DatabaseOperationException {
		return mainContractCertificateRepository.deleteMainCertContraCharge(mainCertContraCharge);
	}

	public String updateMainCertContraChargeList(List<MainCertificateContraCharge> contraChargeList, MainContractCertificate mainCert) throws DatabaseOperationException {
		return mainContractCertificateRepository.updateMainCertContraChargeList(contraChargeList, mainCert);
	}

	public String postMainCertToARInterface(String jobNumber) throws DatabaseOperationException {
		return mainContractCertificateRepository.postMainCertToARInterface(jobNumber);
	}

	public Long insertIPA(MainContractCertificate mainCert, String userID)
			throws DatabaseOperationException {
		return mainContractCertificateRepository.insertIPA(mainCert, userID);
	}

	public Long insertAsAtDate(String jobNumber, Integer mainCertNumber,
			Date asAtDate, String userId) throws Exception {
		return mainContractCertificateRepository.insertAsAtDate(jobNumber, mainCertNumber, asAtDate, userId);
	}

	/**
	 * @author tikywong
	 * modified on November 20, 2012
	 */
	public String insertAndPostMainContractCert(String jobNumber, Integer mainCertNumber, Date asAtDate) throws DatabaseOperationException {

		return mainContractCertificateRepository.insertAndPostMainContractCert(jobNumber, mainCertNumber, asAtDate);
	}

	/**
	 * @author tikywong
	 * created on August 23, 2012
	 */
	public String insertIPAAndUpdateMainContractCert(MainContractCertificate mainCert) throws DatabaseOperationException {
		return mainContractCertificateRepository.insertIPAAndUpdateMainContractCert(mainCert);
	}

	/**
	 * @author tikywong
	 * created on September 03, 2012
	 */
	public Boolean updateMainCertificateStatus(String jobNumber, Integer mainCertNumber, String newCertificateStatus) throws DatabaseOperationException {
		return mainContractCertificateRepository.updateMainCertificateStatus(jobNumber, mainCertNumber, newCertificateStatus);
	}

	/**
	 * 
	 * @author tikywong
	 * created on Oct 25, 2012 10:51:21 AM
	 */
	public String resetMainContractCert(MainContractCertificate toBeUpdatedMainCert) throws DatabaseOperationException {
		return mainContractCertificateRepository.resetMainContractCert(toBeUpdatedMainCert);
	}

	/**
	 * @author tikywong
	 * created on November 20, 2012
	 */
	public String confirmMainContractCert(MainContractCertificate toBeUpdatedMainCert) throws DatabaseOperationException {
		return mainContractCertificateRepository.confirmMainContractCert(toBeUpdatedMainCert);
	}
	
	/**
	 * 
	 * @author tikywong
	 * created on May 14, 2013 5:46:49 PM
	 */
	public List<MainCertReceiveDateWrapper> getMainCertReceiveDateAndAmount(String company, String refDocNo)  throws DatabaseOperationException {
		return mainContractCertificateRepository.getMainCertReceiveDateAndAmount(company, refDocNo);
	}
	
	public List<Integer> obtainPaidMainCertList(String jobNo) throws DatabaseOperationException{
		return mainContractCertificateRepository.obtainPaidMainCertList(jobNo);
	}

	public String submitNegativeMainCertForApproval(String jobNumber, Integer mainCertNumber, Double certAmount, String userID) throws DatabaseOperationException {
		return mainContractCertificateRepository.submitNegativeMainCertForApproval(jobNumber, mainCertNumber, certAmount, userID);
	}

	/**
	 * @author koeyyeung
	 * created on 5th Aug, 2015**/
	public Boolean updateMainCertFromF03B14Manually() throws Exception {
		return mainContractCertificateRepository.updateMainCertFromF03B14Manually();
	}

	@Override
	public MainContractCertEnquiryResultWrapper obtainMainContractCertificateList(
			MainContractCertEnquirySearchingWrapper wrapper, int pageNum)
			throws DatabaseOperationException {
		wrapper. setCompanyNo(wrapper.getCompanyNo());
		wrapper.setDivisionCode(wrapper.getDivisionCode());
		wrapper.setJobNo(wrapper.getJobNo());
		return mainContractCertificateRepository.obtainMainContractCert(wrapper, pageNum);
	}

	@Override
	public Double getTotalPostContraChargeAmt(MainContractCertificate mainContractCertificate) throws DatabaseOperationException {
		return mainContractCertificateRepository.getTotalPostContraChargeAmt(mainContractCertificate);
	}

	@Override
	public Double getCertifiedContraChargeAmtByContraChargeDetails(MainContractCertificate mainContractCertificate) throws DatabaseOperationException {
		return mainContractCertificateRepository.getCertifiedContraChargeAmtByContraChargeDetails(mainContractCertificate);
	}

	@Override
	public Integer deleteMainCertContraCharge(MainContractCertificate mainContractCertificate)
			throws DatabaseOperationException {
		return mainContractCertificateRepository.deleteMainCertContraCharge(mainContractCertificate);
	}
	
}
