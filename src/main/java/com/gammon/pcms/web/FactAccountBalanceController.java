package com.gammon.pcms.web;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.pcms.model.adl.FactAccountBalance;
import com.gammon.pcms.service.FactAccountBalanceService;

@RestController
@RequestMapping(value = "service", method = RequestMethod.POST)
public class FactAccountBalanceController {

	@Autowired
	private FactAccountBalanceService factAccountBalanceService;
	
	@RequestMapping(value = "GetFactAccountBalanceByFiscalYearAccountTypeLedgerEntityBusinessUnitKeyNotAccountSubsidiary", method = RequestMethod.GET)
	public List<FactAccountBalance> findByFiscalYearAndAccountTypeLedgerAndEntityBusinessUnitKeyAndAccountSubsidiaryNot(
			@RequestParam BigDecimal fiscalYear, @RequestParam String accountTypeLedger, @RequestParam String entityBusinessUnitKey, @RequestParam(required = false) String accountSubsidiary) throws Exception{
		return factAccountBalanceService.findByFiscalYearAndAccountTypeLedgerAndEntityBusinessUnitKeyAndAccountSubsidiaryNot(fiscalYear, accountTypeLedger, entityBusinessUnitKey, accountSubsidiary);
	}

}
