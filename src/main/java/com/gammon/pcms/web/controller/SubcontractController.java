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
import com.gammon.qs.domain.SubcontractDetail;
import com.gammon.qs.service.SubcontractService;
import com.gammon.qs.wrapper.UDC;
import com.gammon.qs.wrapper.finance.SubcontractListWrapper;
import com.gammon.qs.wrapper.sclist.SCListWrapper;

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
	public List<Subcontract> getSubcontractList(@RequestParam(required =true) String jobNo){
		List<Subcontract> subcontractList = null;
		try{
			subcontractList = subcontractService.obtainSubcontractList(jobNo);
		} catch (Exception e){
			logger.error("Database Exception: ");
			e.printStackTrace();
		}
		return subcontractList;
	}
	
	@RequestMapping(value = "obtainSubcontractListWithSCListWrapper", method = RequestMethod.POST)
	public List<SCListWrapper> obtainSubcontractListWithSCListWrapper(@RequestBody SubcontractListWrapper subcontractListWrapper){
		List<SCListWrapper> scListWrapper = null;
		try{
			scListWrapper = subcontractService.obtainSubcontractList(subcontractListWrapper);
		}catch(Exception e){
			logger.error("Database Exception: ");
			e.printStackTrace();
		}
		return scListWrapper;
	}
	
	@RequestMapping(value = "getWorkScope", method = RequestMethod.GET)
	public UDC getWorkScope(@RequestParam(required =true) String workScopeCode){
		UDC obtainWorkScope = null;
		try {
			obtainWorkScope = subcontractService.obtainWorkScope(workScopeCode);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return obtainWorkScope;
	}
	
	@RequestMapping(value = "getSubcontract", method = RequestMethod.GET)
	public Subcontract getSubcontract(@RequestParam(required =true) String jobNo, @RequestParam(required =true) String subcontractNo){
		Subcontract subcontract = null;
		try {
			subcontract = subcontractService.obtainSubcontract(jobNo, subcontractNo);
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
		return subcontract;
	}
	
	@RequestMapping(value = "getSCDetails", method = RequestMethod.GET)
	public List<SubcontractDetail> getSCDetails(@RequestParam(required =true) String jobNo, @RequestParam(required =true) String subcontractNo){
		List<SubcontractDetail> scDetails = null;
		try {
			scDetails = subcontractService.obtainSCDetails(jobNo, subcontractNo);
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
		return scDetails;
	}
	
	@RequestMapping(value = "getSubcontractDetailsDashboardData", method = RequestMethod.GET)
	public List<SubcontractDetail> getSubcontractDetailsDashboardData(@RequestParam(required =true) String jobNo, @RequestParam(required =true) String subcontractNo){
		List<SubcontractDetail> scDetails = null;
		scDetails = subcontractService.getSubcontractDetailsDashboardData(jobNo, subcontractNo);
		return scDetails;
	}
	
	@RequestMapping(value = "upateSubcontract", method = RequestMethod.POST)
	public String upateSubcontract(@RequestParam(required =true) String jobNo,  @RequestBody Subcontract subcontract){
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
	public String upateSubcontractDates(@RequestParam(required =true) String jobNo,  @RequestBody Subcontract subcontract){
		String result = null;
		try {
			result = subcontractService.upateSubcontractDates(jobNo, subcontract);
		} catch (Exception e) {
			result = "Subcontract cannot be updated.";
			e.printStackTrace();
		} 
		return result;
	}
	
	@RequestMapping(value = "submitAwardApproval", method = RequestMethod.POST)
	public String submitAwardApproval(@RequestParam(required =true) String jobNo, 
			@RequestParam(required =true) String subcontractNo){
		String result = null;
		try {
			result = subcontractService.submitAwardApproval(jobNo, subcontractNo);
		} catch (Exception e) {
			result = "Subcontract cannot be submitted.";
			e.printStackTrace();
		} 
		return result;
	}
	
	@RequestMapping(value = "runProvisionPostingManually", method = RequestMethod.POST)
	public void runProvisionPostingManually(@RequestParam(defaultValue = "") String jobNumber, @RequestParam Date glDate){
		subcontractService.runProvisionPostingManually(jobNumber, glDate, false, null);
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
		subcontractService.updateMultipleSystemConstants(appSubcontractStandardTermsList, null);
	}

	@RequestMapping(value = "inactivateSystemConstant", method = RequestMethod.POST)
	public void inactivateSystemConstant(@RequestBody List<AppSubcontractStandardTerms> appSubcontractStandardTermsList){
		subcontractService.inactivateSystemConstantList(appSubcontractStandardTermsList);
	}

	@RequestMapping(value = "createSystemConstant", method = RequestMethod.POST)
	public void createSystemConstant(@RequestBody AppSubcontractStandardTerms appSubcontractStandardTerms,
			HttpServletRequest request, HttpServletResponse response) {
		boolean result = false;
		result = subcontractService.createSystemConstant(appSubcontractStandardTerms, null);
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
