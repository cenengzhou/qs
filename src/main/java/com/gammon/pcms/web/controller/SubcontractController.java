/**
 * pcms-tc
 * com.gammon.pcms.web.controller
 * SubcontractController.java
 * @since May 9, 2016 4:46:10 PM
 * @author tikywong
 */
package com.gammon.pcms.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.service.SubcontractService;
import com.gammon.qs.wrapper.UDC;

@RestController
@RequestMapping(value = "service/subcontract/"/*,
				consumes = MediaType.APPLICATION_JSON_VALUE,
				produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"*/)
public class SubcontractController {
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private SubcontractService subcontractService;
	//@Autowired
	//private SCPackageSPDao scPackageSPDao;
	
	/*@RequestMapping(value = "GetSubcontractList.json")
	public List<Subcontract> getSubcontractList(@RequestParam(name="jobNo") String jobNo){
		List<Subcontract> subcontractList = null;
		try{
			subcontractList = scPackageSPDao.findByJobInfo_JobNumberAndSubcontractStatusAndSystemStatus(jobNo, 500, "ACTIVE");
		}catch(DatabaseOperationException databaseOperationException){
			logger.error("Database Exception: ");
			databaseOperationException.printStackTrace();
		}
		return subcontractList;
	}*/
	
	@RequestMapping(value = "getSubcontractList", method = RequestMethod.GET)
	public List<Subcontract> getSubcontractList(@RequestParam(name="jobNo") String jobNo){
		List<Subcontract> subcontractList = null;
		try{
			subcontractList = subcontractService.obtainSubcontractList(jobNo);
		}catch(DatabaseOperationException databaseOperationException){
			logger.error("Database Exception: ");
			databaseOperationException.printStackTrace();
		}
		return subcontractList;
	}
	
	
	@RequestMapping(value = "getWorkScope", method = RequestMethod.GET)
	public UDC getWorkScope(@RequestParam(name="workScopeCode") String workScopeCode){
		logger.info("------------------------obtainWorkScope");
		
		UDC obtainWorkScope = null;
		try {
			obtainWorkScope = subcontractService.obtainWorkScope(workScopeCode);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return obtainWorkScope;
	}
	
	@RequestMapping(value = "getSubcontract", method = RequestMethod.GET)
	public Subcontract getSubcontract(@RequestParam(name="jobNo") String jobNo, @RequestParam(name="subcontractNo") String subcontractNo){
		Subcontract subcontract = null;
		try {
			subcontract = subcontractService.obtainSubcontract(jobNo, subcontractNo);
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
		return subcontract;
	}
	
	@RequestMapping(value = "addSubcontract", method = RequestMethod.POST)
	public String addSubcontract(@RequestParam(name="jobNo") String jobNo, @Valid @RequestBody Subcontract subcontract){
		logger.info("------------------------addSubcontract");
		logger.info("jobNo: "+jobNo);
		logger.info("packageNo: "+subcontract.getPackageNo());
		
		String result = null;
		try {
			result = subcontractService.saveOrUpdateSCPackage(jobNo, subcontract);
		} catch (Exception e) {
			result = "Subcontract cannot be updated.";
			e.printStackTrace();
		} 
		return result;
	}
	
	
	
	
}
