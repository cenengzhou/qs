
package com.gammon.pcms.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.service.SubcontractorService;
import com.gammon.qs.wrapper.SubcontractorWrapper;
import com.gammon.qs.wrapper.tenderAnalysis.SubcontractorTenderAnalysisWrapper;

@RestController
@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsEnq())")
@RequestMapping(value = "service/subcontractor/")
public class SubcontractorController {

	@Autowired
	private SubcontractorService subcontractorService;
	
	@RequestMapping(value = "obtainSubcontractorWrappers", method = RequestMethod.POST)
	public List<SubcontractorWrapper> obtainSubcontractorWrappers(
			@RequestParam(defaultValue = "") String workScope, @RequestParam(defaultValue = "") String subcontractor) throws DatabaseOperationException{
		List<SubcontractorWrapper> wrapperList = new ArrayList<SubcontractorWrapper>();
		if(workScope.equals("") && subcontractor.equals("")) {
			throw new IllegalArgumentException("Please input work scope or subcontractor to search");
		}
		wrapperList.addAll(subcontractorService.obtainSubcontractorWrappers(workScope, subcontractor));
		return wrapperList;
	}

	@RequestMapping(value = "obtainClientWrappers", method = RequestMethod.POST)
	public List<SubcontractorWrapper> obtainClientWrappers(@RequestParam String client) throws DatabaseOperationException{
		List<SubcontractorWrapper> wrapperList = new ArrayList<SubcontractorWrapper>();
		wrapperList.addAll(subcontractorService.obtainClientWrappers(client));
		return wrapperList;
	}

	@RequestMapping(value = "obtainSubconctractorStatistics", method = RequestMethod.POST)
	public List<String> obtainSubconctractorStatistics(@RequestParam String vendorNo) throws DatabaseOperationException {
		return subcontractorService.obtainSubconctractorStatistics(vendorNo);
	}

	@RequestMapping(value = "obtainPackagesByVendorNo", method = RequestMethod.POST)
	public List<Subcontract> obtainPackagesByVendorNo(@RequestParam String vendorNo) throws DatabaseOperationException {
		return subcontractorService.obtainPackagesByVendorNo(vendorNo);
	}

	@RequestMapping(value = "obtainTenderAnalysisWrapperByVendorNo", method = RequestMethod.POST)
	public List<SubcontractorTenderAnalysisWrapper> obtainTenderAnalysisWrapperByVendorNo(@RequestParam String vendorNo) throws DatabaseOperationException  {
		return subcontractorService.obtainTenderAnalysisWrapperByVendorNo(vendorNo);
	}


}
