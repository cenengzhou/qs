package com.gammon.pcms.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.dao.adl.AccountBalanceAAJIDao;
import com.gammon.pcms.dao.adl.AccountBalanceAAJISCDao;
import com.gammon.pcms.dao.adl.AccountBalanceDao;
import com.gammon.pcms.dao.adl.AccountLedgerDao;
import com.gammon.pcms.dao.adl.AccountMasterDao;
import com.gammon.pcms.dao.adl.AddressBookDao;
import com.gammon.pcms.dao.adl.ApprovalDetailDao;
import com.gammon.pcms.dao.adl.ApprovalHeaderDao;
import com.gammon.pcms.dao.adl.BusinessUnitDao;
import com.gammon.pcms.dto.rs.provider.response.adl.JobDashboardDTO;
import com.gammon.pcms.model.adl.AccountBalance;
import com.gammon.pcms.model.adl.AccountLedger;
import com.gammon.pcms.model.adl.AccountMaster;
import com.gammon.pcms.model.adl.AddressBook;
import com.gammon.pcms.model.adl.ApprovalDetail;
import com.gammon.pcms.model.adl.ApprovalHeader;
import com.gammon.pcms.model.adl.BusinessUnit;
import com.gammon.qs.application.exception.DatabaseOperationException;

@Service
@Transactional(	readOnly = true,
				rollbackFor = Exception.class,
				value = "adlTransactionManager")
public class ADLService {
	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private AccountMasterDao accountMasterDao;
	@Autowired
	private AccountLedgerDao accountLedgerDao;
	@Autowired
	private AccountBalanceDao accountBalanceDao;
	@Autowired
	private AccountBalanceAAJIDao accountBalanceAAJIDao;
	@Autowired
	private AccountBalanceAAJISCDao accountBalanceAAJISCDao;
	@Autowired
	private ApprovalHeaderDao approvalHeaderDao;
	@Autowired
	private ApprovalDetailDao approvalDetailDao;
	@Autowired
	private AddressBookDao addressBookDao;
	@Autowired
	private BusinessUnitDao businessUnitDao;

	/*
	 * ----------------------------------------------- JDE @ Data Layer -----------------------------------------------
	 */
	/**
	 * AA & JI Ledger only
	 *
	 * @param year
	 * @param month
	 * @param noJob
	 * @param noSubcontract
	 * @return
	 * @author tikywong
	 * @since Jul 6, 2016 1:49:12 PM
	 */
	public List<?> getMonthlyJobCostList(	BigDecimal year,
											BigDecimal month,
											String noJob,
											String noSubcontract) {
		if (StringUtils.isEmpty(noSubcontract))
			return accountBalanceAAJIDao.findMonthlyJobCost(year, month, noJob);
		else
			return accountBalanceAAJISCDao.findMonthlyJobCost(year, month, noJob, noSubcontract);
	}

	/**
	 * General searching with account code in AA & JI Ledger only
	 *
	 * @param yearStart
	 * @param yearEnd
	 * @param noJob
	 * @param codeObject
	 * @return
	 * @author tikywong
	 * @since Jul 5, 2016 10:06:26 AM
	 */
	public List<?> getAAJILedgerList(	BigDecimal yearStart,
										BigDecimal yearEnd,
										String noJob,
										String noSubcontract,
										String codeObject,
										String codeSubsidiary) {
		if (StringUtils.isEmpty(noSubcontract))
			return accountBalanceAAJIDao.find(yearStart, yearEnd, noJob, codeObject, codeSubsidiary);
		else
			return accountBalanceAAJISCDao.find(yearStart, yearEnd, noJob, noSubcontract, codeObject, codeSubsidiary);
	}

	/**
	 * Account Balance with all Ledger Types
	 *
	 * @param yearStart
	 * @param yearEnd
	 * @param typeLedger
	 * @param noJob
	 * @param codeObject
	 * @param codeSubsidiary
	 * @return
	 * @author tikywong
	 * @since Jul 7, 2016 5:01:29 PM
	 */
	public List<AccountBalance> getAccountBalanceList(	BigDecimal yearStart,
														BigDecimal yearEnd,
														String typeLedger,
														String noJob,
														String codeObject,
														String codeSubsidiary) {
		return accountBalanceDao.find(yearStart, yearEnd, typeLedger, noJob, codeObject, codeSubsidiary);
	}

	/**
	 * Monthly Cash Flow for Job Dash Board
	 *
	 * @param yearStart
	 * @param yearEnd
	 * @param noJob
	 * @return
	 * @author tikywong
	 * @since Jul 12, 2016 2:16:59 PM
	 */
	public JobDashboardDTO getJobDashboardData(	BigDecimal yearStart,
												BigDecimal yearEnd,
												String noJob) {

		List<AccountBalance> contractReceivableList = accountBalanceDao.find(yearStart, yearEnd, AccountBalance.TYPE_LEDGER.AA.toString(), noJob, AccountBalance.CODE_OBJECT_CONTRACT_RECEIVABLE_PREFIX, AccountBalance.CODE_SUBSIDIARY_EMPTY);
		List<AccountBalance> turnoverList = accountBalanceDao.find(yearStart, yearEnd, AccountBalance.TYPE_LEDGER.AA.toString(), noJob, AccountBalance.CODE_OBJECT_TURNOVER, AccountBalance.CODE_SUBSIDIARY_EMPTY);
		List<AccountBalance> originalBudgetList = accountBalanceDao.findAndGroup(yearStart, yearEnd, AccountBalance.TYPE_LEDGER.OB.toString(), noJob);

		return new JobDashboardDTO(contractReceivableList, turnoverList, originalBudgetList, null);
	}

	/**
	 * Account Ledger general searching
	 *
	 * @param yearStart
	 * @param yearEnd
	 * @param monthStart
	 * @param monthEnd
	 * @param typeLedger
	 * @param typeDocument
	 * @param noJob
	 * @param noSubcontract
	 * @param codeObject
	 * @param codeSubsidiary
	 * @return
	 * @author tikywong
	 * @since Jul 11, 2016 3:17:56 PM
	 */
	public List<AccountLedger> getAccountLedgerList(BigDecimal yearStart, BigDecimal yearEnd, BigDecimal monthStart, BigDecimal monthEnd, String typeLedger, String typeDocument, String noJob, String noSubcontract, String codeObject, String codeSubsidiary) {
		return accountLedgerDao.find(yearStart, yearEnd, monthStart, monthEnd, typeLedger, typeDocument, noJob, noSubcontract, codeObject, codeSubsidiary);
	}
	
	/**
	 * Account Master general searching
	 *
	 * @param noJob
	 * @return
	 * @author tikywong
	 * @since Jul 22, 2016 11:44:46 AM
	 */
	public List<AccountMaster> getAccountMasterList(String noJob) {
		return accountMasterDao.find(noJob);
	}
	
	/**
	 * Find unique Account Master
	 *
	 * @param noJob
	 * @param codeObject
	 * @param codeSubsidiary
	 * @return
	 * @author	tikywong
	 * @since	Jul 22, 2016 12:00:56 PM
	 */
	public AccountMaster getAccountMaster(String noJob, String codeObject, String codeSubsidiary){
		return accountMasterDao.findByAccountCode(noJob, codeObject, codeSubsidiary);
	}
	
	public List<AddressBook> getAddressBookListOfSubcontractorAndClient(){
		List<String> addressBookTypeList = new ArrayList<String>();
		// should be enum of AddressBook class
		addressBookTypeList.add(AddressBook.TYPE_VENDOR);
		addressBookTypeList.add(AddressBook.TYPE_CLIENT);
		
		return addressBookDao.findByAddressBookTypeList(addressBookTypeList);
	}
	
	public AddressBook getAddressBook(BigDecimal addressBookNo) throws DataAccessException {
		return addressBookDao.get(addressBookNo);
	}
	
	public BusinessUnit getBusinessUnit(String jobNo) throws DataAccessException {
		return businessUnitDao.find(jobNo);
	}
	
	/*
	 * ----------------------------------------------- Approval System @ Data Layer -----------------------------------------------
	 */
	/**
	 * 
	 *
	 * @param statusApproval
	 * @param noJob
	 * @param typeApprovalCategory
	 * @param noSubcontract
	 * @param noPayment
	 * @param noMainCert
	 * @param recordKeyInstance
	 * @return
	 * @author tikywong
	 * @since Jul 12, 2016 2:24:46 PM
	 */
	public List<ApprovalHeader> getApprovalHeaderList(String statusApproval, String noJob, String typeApprovalCategory, String typeApproval, String noSubcontract, String noPayment, String noMainCert, String noAddendum, BigDecimal recordKeyInstance) {
		// Search with recordKeyInstance
		if (recordKeyInstance.longValue() > 0) {
			List<ApprovalHeader> approvalHeaderList = new ArrayList<ApprovalHeader>();
			approvalHeaderList.add(approvalHeaderDao.findByRecordKeyInstance(recordKeyInstance));
			return approvalHeaderList;
		}
		else {
			// Search with prefixes
			List<String> recordKeyDocumentList = new ArrayList<String>();

			if (typeApproval.equals(ApprovalHeader.TYPE_APPROVAL.AWARD.toString())) {// Award - AW, ST, V5, V6
				recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.AW.toString());
				recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.ST.toString());
				recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.V5.toString());
				recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.V6.toString());

			} else if (typeApproval.equals(ApprovalHeader.TYPE_APPROVAL.PAYMENT.toString())) {// Payment - SP, SF, NP, FR
				if (StringUtils.isEmpty(noPayment)) {
					recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.SP.toString());
					recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.SF.toString());
					recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.NP.toString());
					recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.FR.toString());
				} else {
					recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.SP.toString() + "-" + StringUtils.leftPad(noPayment, 3, '0'));
					recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.SF.toString() + "-" + StringUtils.leftPad(noPayment, 3, '0'));
					recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.NP.toString() + "-" + StringUtils.leftPad(noPayment, 3, '0'));
					recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.FR.toString() + "-" + StringUtils.leftPad(noPayment, 3, '0'));
				}
			} else if (typeApproval.equals(ApprovalHeader.TYPE_APPROVAL.ADDENDUM.toString())) { // Addendum - SM, SL
				if (StringUtils.isEmpty(noAddendum)) {
					recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.SM.toString());
					recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.SL.toString());
				} else {
					recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.SM.toString() + "-" + StringUtils.leftPad(noAddendum, 3, '0'));
					recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.SL.toString() + "-" + StringUtils.leftPad(noAddendum, 3, '0'));
				}
			} else if (typeApproval.equals(ApprovalHeader.TYPE_APPROVAL.CERTIFICATE.toString())) // Main Certificate
				recordKeyDocumentList.add(noJob + "-" + noMainCert + "-" + ApprovalHeader.TYPE_DOCUMENT.RM.toString());
			else if (typeApproval.equals(ApprovalHeader.TYPE_APPROVAL.SPLIT.toString())) // Split Subcontract - VA
				recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.VA.toString());
			else if (typeApproval.equals(ApprovalHeader.TYPE_APPROVAL.TERMINATE.toString())) // Terminate Subcontract - VB
				recordKeyDocumentList.add(noJob + "-" + noSubcontract + "-" + ApprovalHeader.TYPE_DOCUMENT.VB.toString());

			// Search with prepared prefixes
			List<ApprovalHeader> approvalHeaderList = new ArrayList<ApprovalHeader>();
			for (String recordKeyDocument : recordKeyDocumentList) {
				approvalHeaderList = approvalHeaderDao.findByRecordKeyDocument(recordKeyDocument, statusApproval);
				if (!approvalHeaderList.isEmpty()) {
					logger.info("Found with prefix: " + recordKeyDocument);
					return approvalHeaderList;
				}
			}
		}

		logger.info("No record found.");
		return new ArrayList<ApprovalHeader>();
	}

	/**
	 * 
	 *
	 * @param statusApproval
	 * @param noJob
	 * @param typeApprovalCategory
	 * @param typeApproval
	 * @param noSubcontract
	 * @param noPayment
	 * @param noMainCert
	 * @param noAddendum
	 * @param recordKeyInstance
	 * @return
	 * @author	tikywong
	 * @since	Jul 19, 2016 12:08:23 PM
	 */
	public List<ApprovalDetail> getApprovalDetailList(String statusApproval, String noJob, String typeApprovalCategory, String typeApproval, String noSubcontract, String noPayment, String noMainCert, String noAddendum, BigDecimal recordKeyInstance) {
		if (recordKeyInstance.longValue() > 0)
			return approvalDetailDao.obtainApprovalDetailByRecordKeyInstance(recordKeyInstance);
		else {
			// Search for "recordKeyInstance"(s) from Approval Header
			List<ApprovalHeader> approvalHeaderList = getApprovalHeaderList(statusApproval, noJob, typeApprovalCategory, typeApproval, noSubcontract, noPayment, noMainCert, noAddendum, recordKeyInstance);
			if (approvalHeaderList.isEmpty()){
				logger.error("No Approval Header is found.");
				return new ArrayList<ApprovalDetail>();
			}
			else if (approvalHeaderList.size() > 1) {
				logger.error("Non unique Approval Headers are found.");
				return new ArrayList<ApprovalDetail>();
			} else {
				List<ApprovalDetail> approvalDetailList = approvalDetailDao.obtainApprovalDetailByRecordKeyInstance(approvalHeaderList.get(0).getRecordKeyInstance());
				if (!approvalDetailList.isEmpty())
					return approvalDetailList;
			}
			return new ArrayList<ApprovalDetail>();
		}
	}
}
