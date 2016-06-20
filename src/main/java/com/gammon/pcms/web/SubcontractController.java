/**
 * pcms-tc
 * com.gammon.pcms.web.controller
 * SubcontractController.java
 * @since May 9, 2016 4:46:10 PM
 * @author tikywong
 */
package com.gammon.pcms.web;

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

@RestController
@RequestMapping(value = "service"/*,
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
	
	@RequestMapping(value = "addSubcontract", method = RequestMethod.POST)
	public String addSubcontract(@RequestParam(name="jobNo") String jobNo, @Valid @RequestBody Subcontract subcontract){
		logger.info("------------------------addSubcontract");
		logger.info("jobNo: "+jobNo);
		logger.info("packageNo: "+subcontract.getPackageNo());
		
		String result1 = null;
		try {
			result1 = subcontractService.saveOrUpdateSCPackage(jobNo, subcontract);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return result1;
	}
}
