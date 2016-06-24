package com.gammon.pcms.web;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.pcms.dto.rs.provider.response.JobCostDTO;
import com.gammon.pcms.model.adl.FactAccountBalance;
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
	public List<FactAccountBalance> getTurnover(	@RequestParam(required = true) BigDecimal yearStart,
																		@RequestParam(required = true) BigDecimal yearEnd,
																		@RequestParam(required = true) String typeLedger,
																		@RequestParam(required = true) String noJob) {
		return adlService.getTurnover(yearStart, yearEnd, typeLedger, noJob);
	}

}
