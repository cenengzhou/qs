/**
 * pcms-tc
 * com.gammon.pcms.web.controller
 * SubcontractController.java
 * @since May 9, 2016 4:46:10 PM
 * @author tikywong
 */
package com.gammon.pcms.web;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.service.PackageService;

@RestController
@RequestMapping(value = "service",
				method = RequestMethod.POST/*,
				consumes = MediaType.APPLICATION_JSON_VALUE,
				produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"*/)
public class PackageController {
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private PackageService packageService;
	
	/*@RequestMapping(value = "GetSubcontractList.json")
//	public void getSubcontractList(@RequestBody GetSubcontractListRequest request){
	public List<SCPackage> getSubcontractList(@RequestParam(name="jobNo") String jobNo){
		List<SCPackage> subcontractList = null;
		try{
			subcontractList = packageService.obtainSubcontractList(null, null, jobNo, null, null, null, null, null, null, null, null, null);
			logger.info("----------------------------SERVER: getSubcontractList Size: "+subcontractList.size());
		}catch(DatabaseOperationException databaseOperationException){
			logger.error("Database Exception: ");
			databaseOperationException.printStackTrace();
		}
		return subcontractList;
	}*/
}
