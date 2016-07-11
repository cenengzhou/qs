/**
 * pcms-tc
 * com.gammon.pcms.web.controller
 * SubcontractController.java
 * @since May 9, 2016 4:46:10 PM
 * @author tikywong
 */
package com.gammon.pcms.web.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.AppSubcontractStandardTerms;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.service.SubcontractService;
import com.gammon.qs.service.security.SecurityService;
import com.gammon.qs.wrapper.UDC;

@RestController
@RequestMapping(value = "service/subcontract/"/*,
				consumes = MediaType.APPLICATION_JSON_VALUE,
				produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"*/)
public class SubcontractController {
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private SubcontractService subcontractService;
	@Autowired
	private SecurityService securityService;
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
	
	@RequestMapping(value = "upateSubcontract", method = RequestMethod.POST)
	public String upateSubcontract(@RequestParam(name="jobNo") String jobNo, @Valid @RequestBody Subcontract subcontract){
		String result = null;
		try {
			result = subcontractService.saveOrUpdateSCPackage(jobNo, subcontract);
		} catch (Exception e) {
			result = "Subcontract cannot be updated.";
			e.printStackTrace();
		} 
		return result;
	}
	
	@RequestMapping(value = "upateSubcontractDates", method = RequestMethod.POST)
	public String upateSubcontractDates(@RequestParam(name="jobNo") String jobNo, @Valid @RequestBody Subcontract subcontract){
		String result = null;
		try {
			result = subcontractService.upateSubcontractDates(jobNo, subcontract);
		} catch (Exception e) {
			result = "Subcontract cannot be updated.";
			e.printStackTrace();
		} 
		return result;
	}
	
	@RequestMapping(value = "runProvisionPostingManually", method = RequestMethod.POST)
	public void runProvisionPostingManually(@RequestParam(defaultValue = "") String jobNumber, @RequestParam Date glDate){
		String username = securityService.getCurrentUser().getUsername();
		Boolean overrideOldPosting = false;
		subcontractService.runProvisionPostingManually(jobNumber, glDate, overrideOldPosting, username);
	}
	
	@RequestMapping(value = "generateSCPackageSnapshotManually", method = RequestMethod.POST)
	public void generateSCPackageSnapshotManually(){
		subcontractService.generateSCPackageSnapshotManually();
	}
	
	@RequestMapping(value = "updateF58001FromSCPackageManually", method = RequestMethod.POST)
	public void updateF58001FromSCPackageManually(){
		subcontractService.updateF58001FromSCPackageManually();
	}

	@RequestMapping(value = "searchSystemConstants", method = RequestMethod.POST)
	public List<AppSubcontractStandardTerms> searchSystemConstants(){
		return subcontractService.searchSystemConstants(null, null, null, null, null, null, null, null);
	}

	@RequestMapping(value = "updateMultipleSystemConstants", method = RequestMethod.POST)
	public void updateMultipleSystemConstants(@RequestBody List<AppSubcontractStandardTerms> appSubcontractStandardTermsList){
		String username = securityService.getCurrentUser().getUsername();
		subcontractService.updateMultipleSystemConstants(appSubcontractStandardTermsList, username);
	}

	@RequestMapping(value = "inactivateSystemConstant", method = RequestMethod.POST)
	public void inactivateSystemConstant(@RequestBody List<AppSubcontractStandardTerms> appSubcontractStandardTermsList){
		String username = securityService.getCurrentUser().getUsername();
		for(AppSubcontractStandardTerms appSubcontractStandardTerms :appSubcontractStandardTermsList){
			subcontractService.inactivateSystemConstant(appSubcontractStandardTerms, username);
		}
	}

	@RequestMapping(value = "createSystemConstant", method = RequestMethod.POST)
	public void createSystemConstant(@RequestBody AppSubcontractStandardTerms appSubcontractStandardTerms,
			HttpServletRequest request, HttpServletResponse response) {
		String username = securityService.getCurrentUser().getUsername();
		boolean result = false;
		result = subcontractService.createSystemConstant(appSubcontractStandardTerms, username);
		if (!result) {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
		}
	}

	@RequestMapping(value = "updateSubcontractAdmin", method = RequestMethod.POST)
	public void updateSubcontractAdmin(@RequestBody Subcontract subcontract) {
		if(subcontract.getId() == null) throw new IllegalArgumentException("Invalid Subcontract");
		subcontractService.updateSubcontractAdmin(subcontract);
	}

}
