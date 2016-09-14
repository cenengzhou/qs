/**

 * pcms-tc
 * com.gammon.pcms.web.controller
 * MasterListController.java
 * @since Jul 4, 2016
 * @author koeyyeung
 */
package com.gammon.pcms.web.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.MasterListObject;
import com.gammon.qs.domain.MasterListSubsidiary;
import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.service.MasterListService;
import com.gammon.qs.wrapper.WorkScopeWrapper;

@RestController
@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsEnq())")
@RequestMapping(value = "service/masterList/")
public class MasterListController {
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private MasterListService masterListService;
	
	@RequestMapping(value = "getSubcontractor", method = RequestMethod.GET)
	public MasterListVendor getSubcontractor(@RequestParam(name="subcontractorNo") String subcontractorNo) throws DatabaseOperationException{

		MasterListVendor masterListVendor = null;
		masterListVendor = masterListService.obtainVendorByVendorNo(subcontractorNo);
		return masterListVendor;
	}
	
	
	@RequestMapping(value = "getSubcontractorList", method = RequestMethod.GET)
	public List<MasterListVendor> getSubcontractorList(@RequestParam(name="searchStr") String searchStr) throws Exception{

		List<MasterListVendor> masterListVendor = null;
		masterListVendor = masterListService.searchVendorList(searchStr);
		return masterListVendor;
	}
	
	@RequestMapping(value = "searchObjectList", method = RequestMethod.POST)
	public List<MasterListObject> searchObjectList(@RequestParam String searchStr) throws Exception{

		List<MasterListObject> masterListObject = null;
		masterListObject = masterListService.searchObjectList(searchStr);
		return masterListObject;
	}
	
	@RequestMapping(value = "searchSubsidiaryList", method = RequestMethod.POST)
	public List<MasterListSubsidiary> searchSubsidiaryList(@RequestParam String searchStr) throws Exception{

		List<MasterListSubsidiary> masterListSubsidiary = null;
		masterListSubsidiary = masterListService.searchSubsidiaryList(searchStr);
		return masterListSubsidiary;
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "validateAndCreateAccountCode", method = RequestMethod.POST)
	public String validateAndCreateAccountCode(@RequestParam String jobNo, @RequestParam String objectCode, @RequestParam String subsidiaryCode) throws Exception{
		return masterListService.validateAndCreateAccountCode(jobNo, objectCode, subsidiaryCode);
	}
	
	@RequestMapping(value = "getSubcontractorWorkScope", method = RequestMethod.POST)
	public List<WorkScopeWrapper> getSubcontractorWorkScope(@RequestParam String vendorNo) throws Exception{
		return masterListService.getSubcontractorWorkScope(vendorNo);
	}

	@RequestMapping(value = "searchVendorAddressDetails", method = RequestMethod.POST)
	public MasterListVendor searchVendorAddressDetails(@RequestParam String vendorNo) throws Exception{
		return masterListService.searchVendorAddressDetails(vendorNo);
	}

}

