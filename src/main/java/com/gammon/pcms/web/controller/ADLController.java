package com.gammon.pcms.web.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.pcms.dto.rs.provider.response.JobCostDTO;
import com.gammon.pcms.model.adl.DimAccountMaster;
import com.gammon.pcms.model.adl.FactAccountBalance;
import com.gammon.pcms.model.adl.FactAccountLedger;
import com.gammon.pcms.model.adl.RptIvAcctBalance;
import com.gammon.pcms.model.adl.RptIvAcctBalanceSl;
import com.gammon.pcms.service.ADLService;

@RestController
@RequestMapping(value = "service")
public class ADLController {

	@Autowired
	private ADLService adlService;

	@RequestMapping(value = "getMonthlyJobCost",
					method = RequestMethod.GET)
	public List<JobCostDTO> getMonthlyJobCost(	@RequestParam(required = true) BigDecimal year,
												@RequestParam(required = true) BigDecimal month,
												@RequestParam(required = true) String typeLedger,
												@RequestParam(required = true) String noJob,
												@RequestParam(required = false) String noSubcontract) {
		return adlService.getMonthlyJobCost(year, month, typeLedger, noJob, noSubcontract);
	}

	@RequestMapping(value = "getTurnover",
					method = RequestMethod.GET)
	public List<FactAccountBalance> getTurnover(@RequestParam(required = true) BigDecimal yearStart,
												@RequestParam(required = true) BigDecimal yearEnd,
												@RequestParam(required = true) String typeLedger,
												@RequestParam(required = true) String noJob) {
		return adlService.getTurnover(yearStart, yearEnd, typeLedger, noJob);
	}

	@RequestMapping(value = "testFactAccountLedger", method = RequestMethod.GET)
	public List<FactAccountLedger> testFactAccountLedger(
			@RequestParam(defaultValue = "JI") String account_type_ledger,
			@RequestParam(defaultValue = "06089") String entity_doc_company_key,
			@RequestParam(defaultValue = "1535300") BigDecimal number_document,
			@RequestParam(defaultValue = "  ") String number_je_line_extension,
			@RequestParam(defaultValue = "1") BigDecimal number_journal_entry_line,
			@RequestParam(defaultValue = "JI") String type_document
			){
		return adlService.testFactAccountLedger(
				account_type_ledger,
				entity_doc_company_key,
				number_document,
				number_je_line_extension,
				number_journal_entry_line,
				type_document
				);
	}

	@RequestMapping(value = "testRptIvAcctBalance", method = RequestMethod.GET)
	public List<RptIvAcctBalance> testRptIvAcctBalance(
			@RequestParam(defaultValue = "00010048") DimAccountMaster dimAccountMaster,
			@RequestParam(defaultValue = "16") BigDecimal fiscalYear,
			@RequestParam(defaultValue = "12") String accountPeriod) {
		return adlService.testRptIvAcctBalance(dimAccountMaster, fiscalYear, accountPeriod);
	}

	@RequestMapping(value = "testRptIvAcctBalanceSl", method = RequestMethod.GET)
	public List<RptIvAcctBalanceSl> testRptIvAcctBalanceSl(
			@RequestParam(defaultValue = "00010048") DimAccountMaster dimAccountMaster,
			@RequestParam(defaultValue = "16") BigDecimal fiscalYear,
			@RequestParam(defaultValue = "12") String accountPeriod,
			@RequestParam(defaultValue = "00001001") String accountSubLedger,
			@RequestParam(defaultValue = "A") String accountTypeSubLedger) {
		return adlService.testRptIvAcctBalanceSl(dimAccountMaster, fiscalYear, accountPeriod, accountSubLedger, accountTypeSubLedger);
	}
}
