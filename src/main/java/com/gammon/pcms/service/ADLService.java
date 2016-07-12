package com.gammon.pcms.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.dao.adl.AccountBalanceAAJIDao;
import com.gammon.pcms.dao.adl.AccountBalanceAAJISCDao;
import com.gammon.pcms.dao.adl.AccountBalanceDao;
import com.gammon.pcms.dao.adl.AccountLedgerDao;
import com.gammon.pcms.dto.rs.provider.response.adl.MonthlyCashFlowDTO;
import com.gammon.pcms.model.adl.AccountBalance;
import com.gammon.pcms.model.adl.AccountLedger;

@Service
@Transactional(	readOnly = true,
				rollbackFor = Exception.class,
				value = "adlTransactionManager")
public class ADLService {
	// private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private AccountLedgerDao accountLedgerDao;
	@Autowired
	private AccountBalanceDao accountBalanceDao;
	@Autowired
	private AccountBalanceAAJIDao accountBalanceAAJIDao;
	@Autowired
	private AccountBalanceAAJISCDao accountBalanceAAJISCDao;
	
	
	/*
	 * -----------------------------------------------
	 * JDE @ Data Layer
	 * -----------------------------------------------
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
	 * @author	tikywong
	 * @since	Jul 12, 2016 2:16:59 PM
	 */
	public MonthlyCashFlowDTO getMonthlyCashFlow(	BigDecimal yearStart,
													BigDecimal yearEnd,
													String noJob) {

		List<AccountBalance> contractReceivableList = accountBalanceDao.find(yearStart, yearEnd, AccountBalance.TYPE_LEDGER_AA, noJob, AccountBalance.CODE_OBJECT_CONTRACT_RECEIVABLE_PREFIX, AccountBalance.CODE_SUBSIDIARY_EMPTY);
		List<AccountBalance> turnoverList = accountBalanceDao.find(yearStart, yearEnd, AccountBalance.TYPE_LEDGER_AA, noJob, AccountBalance.CODE_OBJECT_TURNOVER, AccountBalance.CODE_SUBSIDIARY_EMPTY);
		List<AccountBalance> originalBudgetList = accountBalanceDao.findAndGroup(yearStart, yearEnd, AccountBalance.TYPE_LEDGER_OB, noJob);

		return new MonthlyCashFlowDTO(contractReceivableList, turnoverList, originalBudgetList, null);
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
	 * @author	tikywong
	 * @since	Jul 11, 2016 3:17:56 PM
	 */
	public List<AccountLedger> getAccountLedgerList(BigDecimal yearStart, BigDecimal yearEnd, BigDecimal monthStart, BigDecimal monthEnd, String typeLedger, String typeDocument, String noJob, String noSubcontract, String codeObject, String codeSubsidiary) {
		return accountLedgerDao.find(yearStart, yearEnd, monthStart, monthEnd, typeLedger, typeDocument, noJob, noSubcontract, codeObject, codeSubsidiary);
	}

	/*
	 * -----------------------------------------------
	 * Approval System @ Data Layer
	 * -----------------------------------------------
	 */
	/**
	 * 
	 *
	 * @param statusApproval
	 * @param noJob
	 * @param typeApproval
	 * @param noSubcontract
	 * @param noPayment
	 * @param noMainCert
	 * @param recordKeyInstance
	 * @return
	 * @author	tikywong
	 * @since	Jul 12, 2016 2:24:46 PM
	 */
	public Object getApprovalHeader(String statusApproval, String noJob, String typeApproval, String noSubcontract, String noPayment, String noMainCert, BigDecimal recordKeyInstance) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 
	 *
	 * @param statusApproval
	 * @param noJob
	 * @param typeApproval
	 * @param noSubcontract
	 * @param noPayment
	 * @param noMainCert
	 * @param recordKeyInstance
	 * @return
	 * @author	tikywong
	 * @since	Jul 12, 2016 2:29:46 PM
	 */
	public List<Object> getApprovalDetailList(String statusApproval, String noJob, String typeApproval, String noSubcontract, String noPayment, String noMainCert, BigDecimal recordKeyInstance) {
		// TODO Auto-generated method stub
		return null;
	}
}
