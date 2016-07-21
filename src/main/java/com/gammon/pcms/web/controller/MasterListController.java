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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.qs.domain.MasterListObject;
import com.gammon.qs.domain.MasterListSubsidiary;
import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.service.MasterListService;

@RestController
@RequestMapping(value = "service/masterList/")
public class MasterListController {
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private MasterListService masterListService;
	
	@RequestMapping(value = "getSubcontractor", method = RequestMethod.GET)
	public MasterListVendor getSubcontractor(@RequestParam(name="subcontractorNo") String subcontractorNo){

		MasterListVendor masterListVendor = null;
		try {
			
			masterListVendor = masterListService.obtainVendorByVendorNo(subcontractorNo);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return masterListVendor;
	}
	
	
	@RequestMapping(value = "getSubcontractorList", method = RequestMethod.GET)
	public List<MasterListVendor> getSubcontractorList(@RequestParam(name="searchStr") String searchStr){

		List<MasterListVendor> masterListVendor = null;
		try {
			
			masterListVendor = masterListService.searchVendorList(searchStr);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return masterListVendor;
	}
	
	@RequestMapping(value = "searchObjectList", method = RequestMethod.POST)
	public List<MasterListObject> searchObjectList(@RequestParam String searchStr){

		List<MasterListObject> masterListObject = null;
		try {
			
			masterListObject = masterListService.searchObjectList(searchStr);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return masterListObject;
	}
	
	@RequestMapping(value = "searchSubsidiaryList", method = RequestMethod.POST)
	public List<MasterListSubsidiary> searchSubsidiaryList(@RequestParam String searchStr){

		List<MasterListSubsidiary> masterListSubsidiary = null;
		try {
			masterListSubsidiary = masterListService.searchSubsidiaryList(searchStr);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return masterListSubsidiary;
	}
	
}

