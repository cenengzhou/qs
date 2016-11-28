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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.gammon.pcms.application.GlobalExceptionHandler;
import com.gammon.pcms.dto.rs.provider.response.subcontract.SubcontractDashboardDTO;
import com.gammon.pcms.dto.rs.provider.response.view.ProvisionPostingHistView;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.AppSubcontractStandardTerms;
import com.gammon.qs.domain.ProvisionPostingHist;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.domain.SubcontractDetail;
import com.gammon.qs.domain.SubcontractDetailOA;
import com.gammon.qs.service.SubcontractService;
import com.gammon.qs.wrapper.UDC;
import com.gammon.qs.wrapper.performanceAppraisal.PerformanceAppraisalWrapper;

@RestController
@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsEnq())")
@RequestMapping(value = "service/subcontract/", produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
public class SubcontractController {
//	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private SubcontractService subcontractService;
	
	
	@RequestMapping(value = "getSubcontractDetailByID",	method = RequestMethod.GET)
	public SubcontractDetail getSubcontractDetailByID(@RequestParam(required = true) String id) {
		return subcontractService.getSubcontractDetailByID(Long.valueOf(id));
	}

	@RequestMapping(value = "getSubcontractList", method = RequestMethod.GET)
	public List<Subcontract> getSubcontractList(@RequestParam(required = true) String jobNo,
												@RequestParam(	required = true,
																defaultValue = "false") boolean awardedOnly) {
		try {
			return subcontractService.getSubcontractList(jobNo, awardedOnly);
		} catch (Exception e) {
			e.printStackTrace();
			GlobalExceptionHandler.checkAccessDeniedException(e);
			return new ArrayList<Subcontract>();
		}
	}

	@RequestMapping(value = "getSubcontractSnapshotList", method = RequestMethod.GET)
	public List<?> getSubcontractSnapshotList(	@RequestParam(required = false) String noJob,
															@RequestParam(required = true) BigDecimal year,
															@RequestParam(required = true) BigDecimal month,
															@RequestParam(	required = true, defaultValue = "false") boolean awardedOnly,
															@RequestParam(	required = true, defaultValue = "false") boolean showJobInfo) {
		try {
			return subcontractService.getSubcontractSnapshotList(noJob, year, month, awardedOnly, showJobInfo);
		} catch (Exception e) {
			e.printStackTrace();
			GlobalExceptionHandler.checkAccessDeniedException(e);
			return new ArrayList<Subcontract>();
		}
	}
	
	@RequestMapping(value = "getWorkScope", method = RequestMethod.GET)
	public UDC getWorkScope(@RequestParam(required =true) String workScopeCode) throws DatabaseOperationException{
		UDC obtainWorkScope = null;
		obtainWorkScope = subcontractService.obtainWorkScope(workScopeCode);
		return obtainWorkScope;
	}
	
	@RequestMapping(value = "getSubcontract", method = RequestMethod.GET)
	public Subcontract getSubcontract(@RequestParam(required =true) String jobNo, @RequestParam(required =true) String subcontractNo) throws DatabaseOperationException{
		Subcontract subcontract = null;
		subcontract = subcontractService.obtainSubcontract(jobNo, subcontractNo);
		return subcontract;
	}
	
	@RequestMapping(value = "getSCDetails", method = RequestMethod.GET)
	public List<SubcontractDetail> getSCDetails(@RequestParam(required =true) String jobNo, @RequestParam(required =true) String subcontractNo) throws DatabaseOperationException{
		List<SubcontractDetail> scDetails = null;
		scDetails = subcontractService.obtainSCDetails(jobNo, subcontractNo);
		return scDetails;
	}
	
	@RequestMapping(value = "getSubcontractDetailForWD", method = RequestMethod.GET)
	public List<SubcontractDetail> getSubcontractDetailForWD(@RequestParam(required =true) String jobNo, @RequestParam(required =true) String subcontractNo){
		List<SubcontractDetail> scDetails = null;
		scDetails = subcontractService.getSubcontractDetailForWD(jobNo, subcontractNo);
		return scDetails;
	}
	
	@RequestMapping(value = "getOtherSubcontractDetails", method = RequestMethod.GET)
	public List<SubcontractDetail> getOtherSubcontractDetails(@RequestParam(required =true) String jobNo, @RequestParam(required =true) String subcontractNo){
		List<SubcontractDetail> scDetails = null;
		scDetails = subcontractService.getOtherSubcontractDetails(jobNo, subcontractNo);
		return scDetails;
	}
	
	
	@RequestMapping(value = "getSubcontractDetailsWithBudget", method = RequestMethod.GET)
	public List<SubcontractDetail> getSubcontractDetailsWithBudget(@RequestParam(required =true) String jobNo, @RequestParam(required =true) String subcontractNo){
		List<SubcontractDetail> scDetails = null;
		try {
			scDetails = subcontractService.getSubcontractDetailsWithBudget(jobNo, subcontractNo);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return scDetails;
	}
	
	
	@RequestMapping(value = "getSCDetailForAddendumUpdate", method = RequestMethod.GET)
	public List<SubcontractDetail> getSCDetailForAddendumUpdate(@RequestParam(required =true) String jobNo, @RequestParam(required =true) String subcontractNo){
		List<SubcontractDetail> scDetails = null;
		scDetails = subcontractService.getSCDetailForAddendumUpdate(jobNo, subcontractNo);
		return scDetails;
	}
	
	@RequestMapping(value = "getSCDetailList", method = RequestMethod.GET)
	public List<SubcontractDetail> getSCDetailList(@RequestParam(required =true) String jobNo) throws DatabaseOperationException{
		List<SubcontractDetail> scDetails = new ArrayList<SubcontractDetail>();
		scDetails.addAll(subcontractService.getScDetails(jobNo));
		return scDetails;
	}
	
	@RequestMapping(value = "getSubcontractDashboardData", method = RequestMethod.GET)
	public List<SubcontractDashboardDTO> getSubcontractDashboardData(@RequestParam(required =true) String jobNo, 
																@RequestParam(required =true) String subcontractNo, 
																@RequestParam(required =true) String year){
		List<SubcontractDashboardDTO> subcontractDashboardWrapperList = null;
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
	public List<String> getAwardedSubcontractNos(@RequestParam(required =true) String jobNo) throws Exception{
		List<String> awardedSubcontractNos = new ArrayList<String>();
		awardedSubcontractNos = subcontractService.getAwardedSubcontractNos(jobNo);
		return awardedSubcontractNos;
	}
	
	@RequestMapping(value = "getUnawardedSubcontractNosUnderPaymentRequisition", method = RequestMethod.GET)
	public List<String> getUnawardedSubcontractNosUnderPaymentRequisition(@RequestParam(required =true) String jobNo) throws Exception{
		List<String> unawardedSubcontractNos = new ArrayList<String>();
		unawardedSubcontractNos = subcontractService.getUnawardedSubcontractNosUnderPaymentRequisition(jobNo);
		return unawardedSubcontractNos;
	}
	
	@RequestMapping(value = "getDefaultValuesForSubcontractDetails", method = RequestMethod.GET)
	public SubcontractDetail getDefaultValuesForSubcontractDetails(@RequestParam(required =true) String jobNo, 
																@RequestParam(required =true) String subcontractNo, 
																@RequestParam(required =true) String lineType) throws Exception{
		SubcontractDetail subcontractDetail = new SubcontractDetail();
		subcontractDetail = subcontractService.getDefaultValuesForSubcontractDetails(jobNo, subcontractNo, lineType);
		return subcontractDetail;
	}
	
	
	
	@RequestMapping(value = "getSubcontractDetailTotalNewAmount", method = RequestMethod.GET)
	public List<BigDecimal> getSubcontractDetailTotalNewAmount(@RequestParam(required =true) String jobNo, 
																@RequestParam(required =true) String subcontractNo) throws Exception{
		return subcontractService.getSubcontractDetailTotalNewAmount(jobNo, subcontractNo);
	}
	
	@RequestMapping(value = "getFinalizedSubcontractNos", method = RequestMethod.GET)
	public List<String> getFinalizedSubcontractNos(@RequestParam(required =true) String jobNo, 
												@RequestParam(required =false) String subcontractNo) throws Exception{
		return subcontractService.getFinalizedSubcontractNos(jobNo, subcontractNo);
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "addAddendumToSubcontractDetail", method = RequestMethod.POST)
	public String addAddendumToSubcontractDetail(@RequestParam(required =true) String jobNo, 
												@RequestParam(required =true) String subcontractNo, 
												@RequestBody SubcontractDetail subcontractDetail) throws Exception{
		String result = null;
		result = subcontractService.addAddendumToSubcontractDetail(jobNo, subcontractNo, subcontractDetail);
		return result;
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "updateSubcontractDetailAddendum", method = RequestMethod.POST)
	public String updateSubcontractDetailAddendum(@RequestBody SubcontractDetail subcontractDetail){
		String result = null;
		result = subcontractService.updateSubcontractDetailAddendum(subcontractDetail);
		return result;
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "deleteSubcontractDetailAddendum", method = RequestMethod.POST)
	public String deleteSubcontractDetailAddendum(@RequestParam(required =true) String jobNo, 
								@RequestParam(required =true) String subcontractNo, 
								@RequestParam(required =true) Integer sequenceNo,
								@RequestParam(required =true) String lineType
								) throws Exception{
		String result = null;
		result = subcontractService.deleteSubcontractDetailAddendum(jobNo, subcontractNo, sequenceNo, lineType);
		return result;
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "upateSubcontract", method = RequestMethod.POST)
	public String upateSubcontract(@RequestParam(required =true) String jobNo,  @RequestBody Subcontract subcontract){
		String result = null;
		try {
			result = subcontractService.saveOrUpdateSCPackage(jobNo, subcontract);
		} catch (Exception e) {
			result = "Subcontract cannot be updated.";
			e.printStackTrace();
			GlobalExceptionHandler.checkAccessDeniedException(e);
		} 
		return result;
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "updateWDandIV", method = RequestMethod.POST)
	public String updateWDandIV(@RequestParam(required =true) String jobNo,
								@RequestParam(required =true) String subcontractNo,
								@RequestBody SubcontractDetailOA scDetail){
		String result = null;
		try {
			result = subcontractService.updateWDandIV(jobNo, subcontractNo, scDetail);
		} catch (Exception e) {
			result = "Subcontract Details cannot be updated.";
			e.printStackTrace();
			GlobalExceptionHandler.checkAccessDeniedException(e);
		} 
		return result;
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "updateWDandIVList", method = RequestMethod.POST)
	public String updateWDandIVList(@RequestParam(required =true) String jobNo,
								@RequestParam(required =true) String subcontractNo,
								@RequestBody List<SubcontractDetailOA> scDetailList){
			String message = "";
			for(SubcontractDetailOA scDetail : scDetailList){
				String result = updateWDandIV(jobNo, subcontractNo, scDetail);
				if(result != ""){
					message += "Subcontract detail ID " + scDetail.getId() + ":" + result + "\n";
				}
			}
			return message;
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
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
			GlobalExceptionHandler.checkAccessDeniedException(e);
		} 
		return result;
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "upateSubcontractDates", method = RequestMethod.POST)
	public String upateSubcontractDates(@RequestParam(required =true) String jobNo,  @RequestBody Subcontract subcontract){
		String result = null;
		try {
			result = subcontractService.upateSubcontractDates(jobNo, subcontract);
		} catch (Exception e) {
			result = "Subcontract cannot be updated.";
			e.printStackTrace();
			GlobalExceptionHandler.checkAccessDeniedException(e);
		} 
		return result;
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "recalculateResourceSummaryIV", method = RequestMethod.POST)
	public boolean recalculateResourceSummaryIV(@RequestParam(required =true) String jobNo, @RequestParam(required =false) String subcontractNo,   @RequestParam(required =false) boolean recalculateFinalizedPackage){
		boolean result = false;
		result = subcontractService.recalculateResourceSummaryIV(jobNo, subcontractNo, recalculateFinalizedPackage);
		return result;
	}
	
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "calculateTotalWDandCertAmount", method = RequestMethod.POST)
	public boolean calculateTotalWDandCertAmount(@RequestParam(required =true) String jobNo, @RequestParam(required =false) String subcontractNo,   @RequestParam(required =false) boolean recalculateRententionAmount){
		boolean result = false;
		result = subcontractService.calculateTotalWDandCertAmount(jobNo, subcontractNo, recalculateRententionAmount);
		return result;
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "updateSCDetailsNewQuantity", method = RequestMethod.POST)
	public String updateSCDetailsNewQuantity(@RequestBody List<SubcontractDetail> subcontractDetailList){
		String result = null;
		try {
			result = subcontractService.updateSCDetailsNewQuantity(subcontractDetailList);
		} catch (Exception e) {
			result = "Subcontract Details cannot be updated.";
			e.printStackTrace();
			GlobalExceptionHandler.checkAccessDeniedException(e);
		} 
		return result;
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "submitAwardApproval", method = RequestMethod.POST)
	public String submitAwardApproval(@RequestParam(required =true) String jobNo, 
			@RequestParam(required =true) String subcontractNo){
		String result = null;
		try {
			result = subcontractService.submitAwardApproval(jobNo, subcontractNo);
		} catch (Exception e) {
			result = "Subcontract cannot be submitted.";
			e.printStackTrace();
			GlobalExceptionHandler.checkAccessDeniedException(e);
		} 
		return result;
	}
	
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "submitSplitTerminateSC", method = RequestMethod.POST)
	public String submitSplitTerminateSC(@RequestParam(required =true) String jobNo, 
										@RequestParam(required =true) String subcontractNo,
										@RequestParam(required =true) String splitTerminate
										){
		String result = null;
		try {
			result = subcontractService.submitSplitTerminateSC(jobNo, subcontractNo, splitTerminate);
		} catch (Exception e) {
			result = "Subcontract cannot be submitted.";
			e.printStackTrace();
			GlobalExceptionHandler.checkAccessDeniedException(e);
		} 
		return result;
	}
	
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "generateSCDetailsForPaymentRequisition", method = RequestMethod.POST)
	public String generateSCDetailsForPaymentRequisition(@RequestParam(required =true) String jobNo, 
										@RequestParam(required =true) String subcontractNo
										){
		String result = null;
		try {
			result = subcontractService.generateSCDetailsForPaymentRequisition(jobNo, subcontractNo);
		} catch (Exception e) {
			result = "Payment Requisition cannot be processed.";
			e.printStackTrace();
			GlobalExceptionHandler.checkAccessDeniedException(e);
		} 
		return result;
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "runProvisionPostingManually", method = RequestMethod.POST)
	public void runProvisionPostingManually(@RequestParam(defaultValue = "") String jobNumber, @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam Date glDate){
		subcontractService.runProvisionPostingManually(jobNumber, glDate, false);
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "generateSCPackageSnapshotManually", method = RequestMethod.POST)
	public void generateSCPackageSnapshotManually(){
		subcontractService.generateSCPackageSnapshotManually();
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "updateF58001FromSCPackageManually", method = RequestMethod.POST)
	public void updateF58001FromSCPackageManually(){
		subcontractService.updateF58001FromSCPackageManually();
	}

	@RequestMapping(value = "searchSystemConstants", method = RequestMethod.POST)
	public List<AppSubcontractStandardTerms> searchSystemConstants(){
		return subcontractService.searchSystemConstants(null, null, null, null, null, null, null, null);
	}

	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "updateMultipleSystemConstants", method = RequestMethod.POST)
	public void updateMultipleSystemConstants(@RequestBody List<AppSubcontractStandardTerms> appSubcontractStandardTermsList){
		subcontractService.updateMultipleSystemConstants(appSubcontractStandardTermsList, null);
	}

	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "inactivateSystemConstant", method = RequestMethod.POST)
	public void inactivateSystemConstant(@RequestBody List<AppSubcontractStandardTerms> appSubcontractStandardTermsList){
		subcontractService.inactivateSystemConstantList(appSubcontractStandardTermsList);
	}

	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "createSystemConstant", method = RequestMethod.POST)
	public void createSystemConstant(@RequestBody AppSubcontractStandardTerms appSubcontractStandardTerms,
			HttpServletRequest request, HttpServletResponse response) {
		boolean result = false;
		result = subcontractService.createSystemConstant(appSubcontractStandardTerms, null);
		if (!result) {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
		}
	}

	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "updateSubcontractAdmin", method = RequestMethod.POST)
	public String updateSubcontractAdmin(@RequestBody Subcontract subcontract) {
		if(subcontract.getId() == null) throw new IllegalArgumentException("Invalid Subcontract");
		return subcontractService.updateSubcontractAdmin(subcontract);
	}

	@RequestMapping(value = "getPerforamceAppraisalsList", method = RequestMethod.POST)
	public List<PerformanceAppraisalWrapper> getPerforamceAppraisalsList(
			@RequestParam String jobNumber, 
			@RequestParam(required = false) Integer vendorNumber, 
			@RequestParam(required = false) Integer subcontractNumber, 
			@RequestParam(required = false) String groupCode, 
			@RequestParam(required = false) String status){
			return subcontractService.getPerformanceAppraisalWrapperList(jobNumber, null, null, groupCode, vendorNumber, subcontractNumber, status);
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
			GlobalExceptionHandler.checkAccessDeniedException(e);
			return new ArrayList<ProvisionPostingHist>();
		}
	}
}
