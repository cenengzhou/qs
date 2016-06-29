package com.gammon.pcms.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.dao.adl.FactAccountBalanceDao;
import com.gammon.pcms.dao.adl.FactAccountLedgerDao;
import com.gammon.pcms.dao.adl.RptIvAcctBalanceDao;
import com.gammon.pcms.dao.adl.RptIvAcctBalanceSlDao;
import com.gammon.pcms.dto.rs.provider.response.JobCostDTO;
import com.gammon.pcms.model.adl.DimAccountMaster;
import com.gammon.pcms.model.adl.FactAccountBalance;
import com.gammon.pcms.model.adl.FactAccountLedger;
import com.gammon.pcms.model.adl.RptIvAcctBalance;
import com.gammon.pcms.model.adl.RptIvAcctBalanceSl;
import com.gammon.qs.application.exception.DatabaseOperationException;

@Service
@Transactional(	rollbackFor = Exception.class,
				value = "adlTransactionManager")
public class ADLService {
//	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private FactAccountBalanceDao factAccountBalanceDao;
	@Autowired
	private FactAccountLedgerDao factAccountLedgerDao;
	@Autowired
	private RptIvAcctBalanceDao rptIvAcctBalanceDao;
	@Autowired
	private RptIvAcctBalanceSlDao rptIvAcctBalanceSlDao;

	/**
	 * To get Job Cost records based on year, month, ledger type, job and sub-contract <br/>
	 * AA Ledger - Actual Spending with SCRate (including Document Type: PS, JS, RI, RM, JE, etc...) <br/>
	 * JI Ledger - Internal Valuation with CostRate (including Document Type: JI) <br/>
	 * Account Codes limited to Object Codes start with '1' <br/>
	 *
	 * @param year
	 * @param month
	 * @param typeLedger
	 * @param noJob
	 * @param noSubcontract
	 * @return
	 * @throws DatabaseOperationException
	 * @author tikywong
	 * @since Jun 24, 2016 9:39:33 AM
	 */
	public List<JobCostDTO> getMonthlyJobCost(	BigDecimal year,
																	BigDecimal month,
																	String typeLedger,
																	String noJob,
																	String noSubcontract) {
		try {
			if (StringUtils.isEmpty(noSubcontract))
				return factAccountBalanceDao.findMonthlyJobCost(year, month, typeLedger, noJob);
			else
				return factAccountBalanceDao.findMonthlyJobCostBySubcontract(year, month, typeLedger, noJob, noSubcontract);
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
			return new ArrayList<JobCostDTO>();
		}
	}

	/**
	 * Turnover (Internal Valuation) - AA Ledger, JI Document Type, Object Code: 221110 or 510000 / 520000 <br/>
	 * Summary Amount of JI Ledger with JI Document Type <br/>
	 *
	 * @param yearStart
	 * @param yearEnd
	 * @param typeLedger
	 * @param noJob
	 * @return
	 * @author tikywong
	 * @since Jun 24, 2016 2:51:07 PM
	 */
	public List<FactAccountBalance> getTurnover(BigDecimal yearStart,
												BigDecimal yearEnd,
												String typeLedger,
												String noJob) {
		try {
			return factAccountBalanceDao.findByFiscalYearRangeAndAccountTypeLedgerAndEntityBusinessUnitKeyAndAccountObject(yearStart, yearEnd, FactAccountBalance.TYPE_LEDGER_AA, noJob, FactAccountBalance.CODE_OBJECT_TURNOVER);
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
			return new ArrayList<FactAccountBalance>();
		}
	}
	

	public List<FactAccountLedger> testFactAccountLedger(String account_type_ledger, String entity_doc_company_key, BigDecimal number_document, String number_je_line_extension, BigDecimal number_journal_entry_line, String type_document) {
		List<FactAccountLedger> factAccountLedgerList = new ArrayList<FactAccountLedger>();
		try {
			factAccountLedgerList.addAll(factAccountLedgerDao.obtainFactAccountLedger(
					account_type_ledger,
					entity_doc_company_key,
					number_document,
					number_je_line_extension,
					number_journal_entry_line,
					type_document
					));
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
		return factAccountLedgerList;
	}

	public List<RptIvAcctBalance> testRptIvAcctBalance(DimAccountMaster dimAccountMaster, BigDecimal fiscalYear,
			String accountPeriod) {
		List<RptIvAcctBalance> rptIvAcctBalanceList = new ArrayList<RptIvAcctBalance>();
		try {
			rptIvAcctBalanceList.addAll(rptIvAcctBalanceDao.obtainRptIvAcctBalance(dimAccountMaster, fiscalYear, accountPeriod));
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
		return rptIvAcctBalanceList;
	}

	public List<RptIvAcctBalanceSl> testRptIvAcctBalanceSl(DimAccountMaster dimAccountMaster, BigDecimal fiscalYear,
			String accountPeriod, String accountSubLedger, String accountTypeSubLedger) {
		List<RptIvAcctBalanceSl> rptIvAcctBalanceSlList = new ArrayList<RptIvAcctBalanceSl>();
		try {
			rptIvAcctBalanceSlList = rptIvAcctBalanceSlDao.obtainRptIvAcctBalanceSl(dimAccountMaster, fiscalYear, accountPeriod, accountSubLedger, accountTypeSubLedger);
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
		return rptIvAcctBalanceSlList;
	}

}
