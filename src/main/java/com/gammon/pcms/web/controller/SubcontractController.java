/**
 * pcms-tc
 * com.gammon.pcms.web.controller
 * SubcontractController.java
 * @since May 9, 2016 4:46:10 PM
 * @author tikywong
 */
package com.gammon.pcms.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.gammon.pcms.dto.rs.provider.response.view.JobInfoView;
import com.gammon.pcms.dto.rs.provider.response.view.ProvisionPostingHistView;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.AppSubcontractStandardTerms;
import com.gammon.qs.domain.ProvisionPostingHist;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.domain.SubcontractDetail;
import com.gammon.qs.service.SubcontractService;
import com.gammon.qs.wrapper.UDC;
import com.gammon.qs.wrapper.subcontractDashboard.SubcontractDashboardWrapper;

@RestController
@RequestMapping(value = "service/subcontract/",
				produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
public class SubcontractController {
//	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private SubcontractService subcontractService;
	
	@RequestMapping(value = "getSubcontractList",
					method = RequestMethod.GET)
	public List<Subcontract> getSubcontractList(@RequestParam(required = true) String jobNo,
												@RequestParam(	required = true,
																defaultValue = "false") boolean awardedOnly) {
		try {
			return subcontractService.getSubcontractList(jobNo, awardedOnly);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Subcontract>();
		}
	}

	@RequestMapping(value = "getSubcontractSnapshotList",
					method = RequestMethod.GET)
	public List<?> getSubcontractSnapshotList(	@RequestParam(required = false) String noJob,
															@RequestParam(required = true) BigDecimal year,
															@RequestParam(required = true) BigDecimal month,
															@RequestParam(	required = true,
																			defaultValue = "false") boolean awardedOnly,
															@RequestParam(	required = true,
																			defaultValue = "false") boolean showJobInfo) {
		try {
			return subcontractService.getSubcontractSnapshotList(noJob, year, month, awardedOnly, showJobInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Subcontract>();
		}
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
	
	@RequestMapping(value = "getSubcontractDetailForWD", method = RequestMethod.GET)
	public List<SubcontractDetail> getSubcontractDetailForWD(@RequestParam(required =true) String jobNo, @RequestParam(required =true) String subcontractNo){
		List<SubcontractDetail> scDetails = null;
		try {
			scDetails = subcontractService.getSubcontractDetailForWD(jobNo, subcontractNo);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return scDetails;
	}
	
	@RequestMapping(value = "getSCDetailList", method = RequestMethod.GET)
	public List<SubcontractDetail> getSCDetailList(@RequestParam(required =true) String jobNo){
		List<SubcontractDetail> scDetails = new ArrayList<SubcontractDetail>();
		try {
			scDetails.addAll(subcontractService.getScDetails(jobNo));
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
		return scDetails;
	}
	
	@RequestMapping(value = "getSubcontractDashboardData", method = RequestMethod.GET)
	public List<SubcontractDashboardWrapper> getSubcontractDashboardData(@RequestParam(required =true) String jobNo, 
																@RequestParam(required =true) String subcontractNo, 
																@RequestParam(required =true) String year){
		List<SubcontractDashboardWrapper> subcontractDashboardWrapperList = null;
		subcontractDashboardWrapperList = subcontractService.getSubcontractDashboardData(jobNo, subcontractNo, year);
		return subcontractDashboardWrapperList;
	}
	
	@RequestMapping(value = "getSubcontractDetailsDashboardData", method = RequestMethod.GET)
	public List<SubcontractDetail> getSubcontractDetailsDashboardData(@RequestParam(required =true) String jobNo, @RequestParam(required =true) String subcontractNo){
		List<SubcontractDetail> scDetails = null;
		scDetails = subcontractService.getSubcontractDetailsDashboardData(jobNo, subcontractNo);
		return scDetails;
	}
	
	@RequestMapping(value = "getAwardedSubcontractNos", method = RequestMethod.GET)
	public List<String> getAwardedSubcontractNos(@RequestParam(required =true) String jobNo){
		List<String> awardedSubcontractNos = new ArrayList<String>();
		try {
			awardedSubcontractNos = subcontractService.getAwardedSubcontractNos(jobNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return awardedSubcontractNos;
	}
	
	@RequestMapping(value = "getUnawardedSubcontractNosUnderPaymentRequisition", method = RequestMethod.GET)
	public List<String> getUnawardedSubcontractNosUnderPaymentRequisition(@RequestParam(required =true) String jobNo){
		List<String> unawardedSubcontractNos = new ArrayList<String>();
		try {
			unawardedSubcontractNos = subcontractService.getUnawardedSubcontractNosUnderPaymentRequisition(jobNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return unawardedSubcontractNos;
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
	
	@RequestMapping(value = "updateWDandIV", method = RequestMethod.POST)
	public String updateWDandIV(@RequestParam(required =true) String jobNo,
								@RequestParam(required =true) String subcontractNo,
								@RequestBody SubcontractDetail scDetail){
		String result = null;
		try {
			result = subcontractService.updateWDandIV(jobNo, subcontractNo, scDetail);
		} catch (Exception e) {
			result = "Subcontract Details cannot be updated.";
			e.printStackTrace();
		} 
		return result;
	}
	
	
	@RequestMapping(value = "updateWDandIVByPercent", method = RequestMethod.POST)
	public String updateWDandIVByPercent(@RequestParam(required =true) String jobNo,
								@RequestParam(required =true) String subcontractNo,
								@RequestParam(required =true) Double percent){
		String result = null;
		try {
			result = subcontractService.updateWDandIVByPercent(jobNo, subcontractNo, percent);
		} catch (Exception e) {
			result = "Subcontract Details cannot be updated.";
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
	
	@RequestMapping(value = "recalculateResourceSummaryIV", method = RequestMethod.POST)
	public boolean recalculateResourceSummaryIV(@RequestParam(required =true) String jobNo, @RequestParam(required =false) String subcontractNo,   @RequestParam(required =false) boolean recalculateFinalizedPackage){
		boolean result = false;
		try {
			result = subcontractService.recalculateResourceSummaryIV(jobNo, subcontractNo, recalculateFinalizedPackage);
		} catch (Exception e) {
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

	
	/**
	 * For Provision Posting History Enquiry
	 *
	 * @param jobNo
	 * @param subcontractNo
	 * @param year
	 * @param month
	 * @return
	 * @author	tikywong
	 * @since	Aug 2, 2016 3:59:30 PM
	 */
	@JsonView(ProvisionPostingHistView.Detached.class)
	@RequestMapping(value = "getProvisionPostingHistList",
					method = RequestMethod.GET)
	public List<ProvisionPostingHist> getProvisionPostingHistList(	@RequestParam(required = true) String jobNo,
																@RequestParam(required = false) String subcontractNo,
																@RequestParam(required = true) BigDecimal year,
																@RequestParam(required = true) BigDecimal month) {
		try {
			return subcontractService.getProvisionPostingHistList(jobNo, subcontractNo, year.intValue(), month.intValue());
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<ProvisionPostingHist>();
		}
	}
}
