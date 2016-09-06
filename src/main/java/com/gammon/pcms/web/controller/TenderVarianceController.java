package com.gammon.pcms.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.pcms.model.TenderVariance;
import com.gammon.pcms.service.TenderVarianceService;

@RestController
@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsEnq())")
@RequestMapping(value = "service/tenderVariance/")
public class TenderVarianceController {

	@Autowired
	private TenderVarianceService tenderVarianceService;
	
	@RequestMapping(value = "getTenderVarianceList", method = RequestMethod.GET)
	public List<TenderVariance> getTenderVarianceList(@RequestParam(name="jobNo") String jobNo, 
														@RequestParam(name="subcontractNo") String subcontractNo,
														@RequestParam(name="subcontractorNo") String subcontractorNo){
		List<TenderVariance> tenderVarianceList = null;
		tenderVarianceList = tenderVarianceService.obtainTenderVarianceList(jobNo, subcontractNo, subcontractorNo);
		return tenderVarianceList;
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "createTenderVariance", method = RequestMethod.POST)
	public String createTenderVariance(@RequestParam(name="jobNo") String jobNo, 
									@RequestParam(name="subcontractNo") String subcontractNo,
									@RequestParam(name="subcontractorNo") String subcontractorNo,
									@Valid @RequestBody List<TenderVariance> tenderVarianceList) throws Exception{
		String result = "";
		result = tenderVarianceService.createTenderVariance(jobNo, subcontractNo, subcontractorNo, tenderVarianceList);
		return result;
	}
	
}
