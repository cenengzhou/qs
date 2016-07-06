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

import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.service.MasterListService;

@RestController
@RequestMapping(value = "service/masterList/")
public class MasterListController {
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private MasterListService masterListService;
	
	
	@RequestMapping(value = "getVendorList", method = RequestMethod.GET)
	public List<MasterListVendor> getVendorList(@RequestParam(name="searchStr") String searchStr){

		List<MasterListVendor> masterListVendor = null;
		try {
			
			masterListVendor = masterListService.searchVendorList(searchStr);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return masterListVendor;
	}
	
	
}

