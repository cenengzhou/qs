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
@RequestMapping(value = "service/subcontract/", produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
public class SubcontractController {
//	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private SubcontractService subcontractService;
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','getSubcontractDetailByID', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getSubcontractDetailByID",	method = RequestMethod.GET)
	public SubcontractDetail getSubcontractDetailByID(@RequestParam(required = true) String id) {
		return subcontractService.getSubcontractDetailByID(Long.valueOf(id));
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','getSubcontractList', @securityConfig.getRolePcmsEnq())")
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

	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','getSubcontractSnapshotList', @securityConfig.getRolePcmsEnq())")
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
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','getWorkScope', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getWorkScope", method = RequestMethod.GET)
	public UDC getWorkScope(@RequestParam(required =true) String workScopeCode) throws DatabaseOperationException{
		UDC obtainWorkScope = null;
		obtainWorkScope = subcontractService.obtainWorkScope(workScopeCode);
		return obtainWorkScope;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','getSubcontract', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getSubcontract", method = RequestMethod.GET)
	public Subcontract getSubcontract(@RequestParam(required =true) String jobNo, @RequestParam(required =true) String subcontractNo) throws DatabaseOperationException{
		Subcontract subcontract = null;
		subcontract = subcontractService.obtainSubcontract(jobNo, subcontractNo);
		return subcontract;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','getSCDetails', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getSCDetails", method = RequestMethod.GET)
	public List<SubcontractDetail> getSCDetails(@RequestParam(required =true) String jobNo, @RequestParam(required =true) String subcontractNo) throws DatabaseOperationException{
		List<SubcontractDetail> scDetails = null;
		scDetails = subcontractService.obtainSCDetails(jobNo, subcontractNo);
		return scDetails;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','getSubcontractDetailForWD', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getSubcontractDetailForWD", method = RequestMethod.GET)
	public List<SubcontractDetail> getSubcontractDetailForWD(@RequestParam(required =true) String jobNo, @RequestParam(required =true) String subcontractNo){
		List<SubcontractDetail> scDetails = null;
		scDetails = subcontractService.getSubcontractDetailForWD(jobNo, subcontractNo);
		return scDetails;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','getOtherSubcontractDetails', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getOtherSubcontractDetails", method = RequestMethod.GET)
	public List<SubcontractDetail> getOtherSubcontractDetails(@RequestParam(required =true) String jobNo, @RequestParam(required =true) String subcontractNo){
		List<SubcontractDetail> scDetails = null;
		scDetails = subcontractService.getOtherSubcontractDetails(jobNo, subcontractNo);
		return scDetails;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','getSubcontractDetailsWithBudget', @securityConfig.getRolePcmsEnq())")
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
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','getSCDetailForAddendumUpdate', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getSCDetailForAddendumUpdate", method = RequestMethod.GET)
	public List<SubcontractDetail> getSCDetailForAddendumUpdate(@RequestParam(required =true) String jobNo, @RequestParam(required =true) String subcontractNo){
		List<SubcontractDetail> scDetails = null;
		scDetails = subcontractService.getSCDetailForAddendumUpdate(jobNo, subcontractNo);
		return scDetails;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','getSCDetailList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getSCDetailList", method = RequestMethod.GET)
	public List<SubcontractDetail> getSCDetailList(@RequestParam(required =true) String jobNo) throws DatabaseOperationException{
		List<SubcontractDetail> scDetails = new ArrayList<SubcontractDetail>();
		scDetails.addAll(subcontractService.getScDetails(jobNo));
		return scDetails;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','getSubcontractDashboardData', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getSubcontractDashboardData", method = RequestMethod.GET)
	public List<SubcontractDashboardDTO> getSubcontractDashboardData(@RequestParam(required =true) String jobNo, 
																@RequestParam(required =true) String subcontractNo, 
																@RequestParam(required =true) String year){
		List<SubcontractDashboardDTO> subcontractDashboardWrapperList = null;
		subcontractDashboardWrapperList = subcontractService.getSubcontractDashboardData(jobNo, subcontractNo, year);
		return subcontractDashboardWrapperList;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','getSubcontractDetailsDashboardData', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getSubcontractDetailsDashboardData", method = RequestMethod.GET)
	public List<SubcontractDetail> getSubcontractDetailsDashboardData(@RequestParam(required =true) String jobNo, @RequestParam(required =true) String subcontractNo){
		List<SubcontractDetail> scDetails = null;
		scDetails = subcontractService.getSubcontractDetailsDashboardData(jobNo, subcontractNo);
		return scDetails;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','getAwardedSubcontractNos', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getAwardedSubcontractNos", method = RequestMethod.GET)
	public List<String> getAwardedSubcontractNos(@RequestParam(required =true) String jobNo) throws Exception{
		List<String> awardedSubcontractNos = new ArrayList<String>();
		awardedSubcontractNos = subcontractService.getAwardedSubcontractNos(jobNo);
		return awardedSubcontractNos;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','getUnawardedSubcontractNosUnderPaymentRequisition', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getUnawardedSubcontractNosUnderPaymentRequisition", method = RequestMethod.GET)
	public List<String> getUnawardedSubcontractNosUnderPaymentRequisition(@RequestParam(required =true) String jobNo) throws Exception{
		List<String> unawardedSubcontractNos = new ArrayList<String>();
		unawardedSubcontractNos = subcontractService.getUnawardedSubcontractNosUnderPaymentRequisition(jobNo);
		return unawardedSubcontractNos;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','getDefaultValuesForSubcontractDetails', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getDefaultValuesForSubcontractDetails", method = RequestMethod.GET)
	public SubcontractDetail getDefaultValuesForSubcontractDetails(@RequestParam(required =true) String jobNo, 
																@RequestParam(required =true) String subcontractNo, 
																@RequestParam(required =true) String lineType) throws Exception{
		SubcontractDetail subcontractDetail = new SubcontractDetail();
		subcontractDetail = subcontractService.getDefaultValuesForSubcontractDetails(jobNo, subcontractNo, lineType);
		return subcontractDetail;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','getSubcontractDetailTotalNewAmount', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getSubcontractDetailTotalNewAmount", method = RequestMethod.GET)
	public List<BigDecimal> getSubcontractDetailTotalNewAmount(@RequestParam(required =true) String jobNo, 
																@RequestParam(required =true) String subcontractNo) throws Exception{
		return subcontractService.getSubcontractDetailTotalNewAmount(jobNo, subcontractNo);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','getFinalizedSubcontractNos', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getFinalizedSubcontractNos", method = RequestMethod.GET)
	public List<String> getFinalizedSubcontractNos(@RequestParam(required =true) String jobNo, 
												@RequestParam(required =false) String subcontractNo) throws Exception{
		return subcontractService.getFinalizedSubcontractNos(jobNo, subcontractNo);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','getCompanyBaseCurrency', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getCompanyBaseCurrency", method = RequestMethod.GET)
	public String getCompanyBaseCurrency(@RequestParam(required =true) String jobNo) throws Exception{
		return subcontractService.getCompanyBaseCurrency(jobNo);
	}

	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','addAddendumToSubcontractDetail', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "addAddendumToSubcontractDetail", method = RequestMethod.POST)
	public String addAddendumToSubcontractDetail(@RequestParam(required =true) String jobNo, 
												@RequestParam(required =true) String subcontractNo, 
												@RequestBody SubcontractDetail subcontractDetail) throws Exception{
		String result = null;
		result = subcontractService.addAddendumToSubcontractDetail(jobNo, subcontractNo, subcontractDetail);
		return result;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','updateSubcontractDetailAddendum', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "updateSubcontractDetailAddendum", method = RequestMethod.POST)
	public String updateSubcontractDetailAddendum(@RequestBody SubcontractDetail subcontractDetail){
		String result = null;
		result = subcontractService.updateSubcontractDetailAddendum(subcontractDetail);
		return result;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','deleteSubcontractDetailAddendum', @securityConfig.getRolePcmsQs())")
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
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','upateSubcontract', @securityConfig.getRolePcmsQs())")
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
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','updateWDandIV', @securityConfig.getRolePcmsQs())")
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
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','updateWDandIVList', @securityConfig.getRolePcmsQs())")
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
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','updateFilteredWDandIVByPercent', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "updateFilteredWDandIVByPercent", method = RequestMethod.POST)
	public String updateFilteredWDandIVByPercent(@RequestParam(required =true) String jobNo,
								@RequestParam(required =true) String subcontractNo,
								@RequestBody(required =true) List<Long> filteredIds,
								@RequestParam(required =true) Double percent){
		String result = null;
		try {
			result = subcontractService.updateFilteredWDandIVByPercent(jobNo, subcontractNo, filteredIds, percent);
		} catch (Exception e) {
			result = "Subcontract Details cannot be updated.";
			e.printStackTrace();
			GlobalExceptionHandler.checkAccessDeniedException(e);
		} 
		return result;
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','upateSubcontractDates', @securityConfig.getRolePcmsQs())")
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
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','recalculateResourceSummaryIV', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "recalculateResourceSummaryIV", method = RequestMethod.POST)
	public boolean recalculateResourceSummaryIV(@RequestParam(required =true) String jobNo, @RequestParam(required =false) String subcontractNo,   @RequestParam(required =false) boolean recalculateFinalizedPackage){
		boolean result = false;
		result = subcontractService.recalculateResourceSummaryIV(jobNo, subcontractNo, recalculateFinalizedPackage);
		return result;
	}
	
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','calculateTotalWDandCertAmount', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "calculateTotalWDandCertAmount", method = RequestMethod.POST)
	public boolean calculateTotalWDandCertAmount(@RequestParam(required =true) String jobNo, @RequestParam(required =false) String subcontractNo,   @RequestParam(required =false) boolean recalculateRententionAmount){
		boolean result = false;
		result = subcontractService.calculateTotalWDandCertAmount(jobNo, subcontractNo, recalculateRententionAmount);
		return result;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','updateSCDetailsNewQuantity', @securityConfig.getRolePcmsQs())")
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
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','submitAwardApproval', @securityConfig.getRolePcmsQs())")
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
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','submitSplitTerminateSC', @securityConfig.getRolePcmsQs())")
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
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','generateSCDetailsForPaymentRequisition', @securityConfig.getRolePcmsQs())")
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
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','runProvisionPostingManually', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "runProvisionPostingManually", method = RequestMethod.POST)
	public void runProvisionPostingManually(@RequestParam(defaultValue = "") String jobNumber, @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam Date glDate){
		subcontractService.runProvisionPostingManually(jobNumber, glDate, false);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','generateSCPackageSnapshotManually', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "generateSCPackageSnapshotManually", method = RequestMethod.POST)
	public void generateSCPackageSnapshotManually(){
		subcontractService.generateSCPackageSnapshotManually();
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','updateF58001FromSCPackageManually', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "updateF58001FromSCPackageManually", method = RequestMethod.POST)
	public void updateF58001FromSCPackageManually(){
		subcontractService.updateF58001FromSCPackageManually();
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','searchSystemConstants', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "searchSystemConstants", method = RequestMethod.POST)
	public List<AppSubcontractStandardTerms> searchSystemConstants(){
		return subcontractService.searchSystemConstants(null, null, null, null, null, null, null, null);
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','updateMultipleSystemConstants', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "updateMultipleSystemConstants", method = RequestMethod.POST)
	public void updateMultipleSystemConstants(@RequestBody List<AppSubcontractStandardTerms> appSubcontractStandardTermsList){
		subcontractService.updateMultipleSystemConstants(appSubcontractStandardTermsList, null);
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','inactivateSystemConstant', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "inactivateSystemConstant", method = RequestMethod.POST)
	public void inactivateSystemConstant(@RequestBody List<AppSubcontractStandardTerms> appSubcontractStandardTermsList){
		subcontractService.inactivateSystemConstantList(appSubcontractStandardTermsList);
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','createSystemConstant', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "createSystemConstant", method = RequestMethod.POST)
	public void createSystemConstant(@RequestBody AppSubcontractStandardTerms appSubcontractStandardTerms,
			HttpServletRequest request, HttpServletResponse response) {
		boolean result = false;
		result = subcontractService.createSystemConstant(appSubcontractStandardTerms, null);
		if (!result) {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
		}
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','updateSubcontractAdmin', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "updateSubcontractAdmin", method = RequestMethod.POST)
	public String updateSubcontractAdmin(@RequestBody Subcontract subcontract) {
		if(subcontract.getId() == null) throw new IllegalArgumentException("Invalid Subcontract");
		return subcontractService.updateSubcontractAdmin(subcontract);
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','getPerforamceAppraisalsList', @securityConfig.getRolePcmsEnq())")
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
	@PreAuthorize(value = "@GSFService.isFnEnabled('SubcontractController','getProvisionPostingHistList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getProvisionPostingHistList", method = RequestMethod.GET)
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
