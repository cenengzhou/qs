package com.gammon.pcms.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.dao.adl.FactAccountBalanceDao;
import com.gammon.pcms.dto.rs.provider.response.JobCostDTO;
import com.gammon.pcms.model.adl.FactAccountBalance;
import com.gammon.qs.application.exception.DatabaseOperationException;

@Service
@Transactional(	rollbackFor = Exception.class,
				value = "adlTransactionManager")
public class ADLService {
//	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private FactAccountBalanceDao factAccountBalanceDao;

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
	
	
}
