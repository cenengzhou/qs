package com.gammon.pcms.web;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.pcms.dto.rs.provider.response.MonthlyContractExpenditureDTO;
import com.gammon.pcms.service.ADLService;

@RestController
@RequestMapping(value = "service")
public class ADLController {

	@Autowired
	private ADLService adlService;

	@RequestMapping(value = "getMonthlyContractExpenditure",
					method = RequestMethod.GET)
	public List<MonthlyContractExpenditureDTO> getMonthlyContractExpenditure(	@RequestParam(required = true) BigDecimal year,
	                                                                         	@RequestParam(required = true) BigDecimal month,
																				@RequestParam(required = true) String typeLedger,
																				@RequestParam(required = true) String noJob,
																				@RequestParam(required = false) String noSubcontract) throws Exception {
		return adlService.getMonthlyContractExpenditure(year, month, typeLedger, noJob, noSubcontract);
	}

}
