package com.gammon.pcms.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.dao.adl.FactAccountBalanceADLHBDao;
import com.gammon.pcms.dto.rs.provider.response.MonthlyContractExpenditureDTO;
import com.gammon.qs.application.exception.DatabaseOperationException;

@Service
@Transactional(	rollbackFor = Exception.class,
				value = "adlTransactionManager")
public class ADLService {
	@Autowired
	private FactAccountBalanceADLHBDao factAccountBalanceADLHBDao;

	public List<MonthlyContractExpenditureDTO> getMonthlyContractExpenditure(	BigDecimal year,
																				BigDecimal month,
																				String typeLedger,
																				String noJob,
																				String noSubcontract) throws DatabaseOperationException {
		if (StringUtils.isEmpty(noSubcontract))
			return factAccountBalanceADLHBDao.findByFiscalYearAndAccountTypeLedgerAndEntityBusinessUnitKey(year, month, typeLedger, noJob);
		else
			return factAccountBalanceADLHBDao.findByFiscalYearAndAccountTypeLedgerAndEntityBusinessUnitKeyAndAccountSubLedger(year, month, typeLedger, noJob, noSubcontract);

	}
}
