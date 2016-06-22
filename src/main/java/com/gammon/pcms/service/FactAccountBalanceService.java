package com.gammon.pcms.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.dao.adl.FactAccountBalanceAdlHBDao;
import com.gammon.pcms.model.adl.FactAccountBalance;
import com.gammon.qs.application.exception.DatabaseOperationException;
@Service
@Transactional(rollbackFor = Exception.class, value = "adlTransactionManager")
public class FactAccountBalanceService {
	@Autowired
	private FactAccountBalanceAdlHBDao factAccountBalanceAdlHBDao;
	
	public List<FactAccountBalance> findByFiscalYearAndAccountTypeLedgerAndEntityBusinessUnitKeyAndAccountSubsidiaryNot(
			BigDecimal fiscalYear, String accountTypeLedger, String entityBusinessUnitKey, String accountSubsidiary) throws DatabaseOperationException{
		return factAccountBalanceAdlHBDao.findByFiscalYearAndAccountTypeLedgerAndEntityBusinessUnitKeyAndAccountSubsidiaryNot(fiscalYear, accountTypeLedger, entityBusinessUnitKey, accountSubsidiary);
	}
}
