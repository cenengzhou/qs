/**
 * PCMS-TC
 * com.gammon.pcms.web
 * MainCertController.java
 * @since Jun 27, 2016 11:20:41 AM
 * @author tikywong
 */
package com.gammon.pcms.web.controller;

import java.util.List;

import javax.validation.Valid;

import com.gammon.pcms.dto.rs.provider.response.maincert.MainCertDashboardDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.pcms.application.GlobalExceptionHandler;
import com.gammon.pcms.dto.rs.provider.response.PCMSDTO;
import com.gammon.pcms.dto.rs.provider.response.jde.MainCertReceiveDateResponse;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.MainCert;
import com.gammon.qs.domain.MainCertContraCharge;
import com.gammon.qs.domain.MainCertRetentionRelease;
import com.gammon.qs.service.JobInfoService;
import com.gammon.qs.service.MainCertContraChargeService;
import com.gammon.qs.service.MainCertRetentionReleaseService;
import com.gammon.qs.service.MainCertService;

@RestController
@RequestMapping(value = "service/mainCert/")
public class MainCertController {

	@Autowired
	private MainCertService mainCertService;
	@Autowired
	private JobInfoService jobInfoService;
	@Autowired
	private MainCertRetentionReleaseService mainCertRetentionReleaseService;
	@Autowired
	private MainCertContraChargeService mainCertContraChargeService;

	// ---------------- get ----------------
	@PreAuthorize(value = "@GSFService.isFnEnabled('MainCertController','getCertificateList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getCertificateList", method = RequestMethod.GET)
	public List<MainCert> getCertificateList(@RequestParam(required = true) String noJob) {

		return mainCertService.getCertificateList(noJob);
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('MainCertController','getPaidMainCertList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getPaidMainCertList", method = RequestMethod.GET)
	public List<Integer> getPaidMainCertList(@RequestParam(required = true) String noJob) throws DatabaseOperationException {
		List<Integer> mainCertList= null;
		mainCertList= mainCertService.getPaidMainCertList(noJob);
		return mainCertList;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('MainCertController','getMainCertNoList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getMainCertNoList", method = RequestMethod.GET)
	public List<Integer> getMainCertNoList(@RequestParam(required = true) String noJob) throws DatabaseOperationException {
		List<Integer> mainCertList= null;
		mainCertList= mainCertService.getMainCertNoList(noJob);
		return mainCertList;
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('MainCertController','getRetentionReleaseList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getRetentionReleaseList", method = RequestMethod.GET)
	public List<MainCertRetentionRelease> getRetentionReleaseList(@RequestParam(required = true) String noJob) {
		return mainCertRetentionReleaseService.getRetentionReleaseList(noJob);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('MainCertController','getMainCertContraChargeList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getMainCertContraChargeList", method = RequestMethod.GET)
	public List<MainCertContraCharge> getMainCertContraChargeList(@RequestParam(required = true) String noJob, @RequestParam(required = true) Integer noMainCert) {
		return mainCertContraChargeService.getMainCertContraChargeList(noJob, noMainCert);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('MainCertController','getCertificate', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getCertificate", method = RequestMethod.GET)
	public MainCert getCertificate(@RequestParam String jobNo, @RequestParam Integer certificateNumber){
		return mainCertService.getCertificate(jobNo, certificateNumber);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('MainCertController','getCertificateDashboardData', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getCertificateDashboardData", method = RequestMethod.GET)
	public List<MainCertDashboardDTO> getCertificateDashboardData(@RequestParam(required = true) String noJob,
																  @RequestParam(required = true, defaultValue = "0") String year
														) {
		return mainCertService.getCertificateDashboardData(noJob, year);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('MainCertController','getLatestMainCert', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getLatestMainCert", method = RequestMethod.GET)
	public MainCert getLatestMainCert(@RequestParam(required = true) String noJob, @RequestParam(required = false) String status) {
		return mainCertService.getLatestMainCert(noJob, status);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('MainCertController','getCumulativeRetentionReleaseByJob', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getCumulativeRetentionReleaseByJob", method = RequestMethod.GET)
	public PCMSDTO getCumulativeRetentionReleaseByJob(@RequestParam(required = true) String noJob, @RequestParam(required = true) Integer noMainCert) {
		return mainCertRetentionReleaseService.getCumulativeRetentionReleaseByJob(noJob, noMainCert);
	}
	
	// ---------------- update / calculate ----------------
	@PreAuthorize(value = "@GSFService.isFnEnabled('MainCertController','createMainCert', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "createMainCert",	method = RequestMethod.POST)
	public String createMainCert(@Valid @RequestBody MainCert mainCert) {
		String result = "";
		try {
			return mainCertService.createMainCert(mainCert);
		} catch (DatabaseOperationException e) {
			result = "Main Cert cannot be created.";
			e.printStackTrace();
			GlobalExceptionHandler.checkAccessDeniedException(e);
		}
		return result;
		
	}
	
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "deleteMainCert",	method = RequestMethod.DELETE)
	public String deleteMainCert(@RequestParam(required = true) String jobNo, @RequestParam(required = true) Integer mainCertNo) {
		String result = "";
		try {
			return mainCertService.deleteMainCert(jobNo, mainCertNo);
		} catch (Exception e) {
			result = "Main Cert cannot be deleted.";
			e.printStackTrace();
			GlobalExceptionHandler.checkAccessDeniedException(e);
		}
		return result;
		
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('MainCertController','updateRetentionRelease', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "updateRetentionRelease", method = RequestMethod.POST)
	public PCMSDTO updateRetentionRelease(	@RequestParam(required = true) String noJob,
											@Valid @RequestBody List<MainCertRetentionRelease> retentionReleaseList) {
		return mainCertRetentionReleaseService.updateRetentionRelease(noJob, retentionReleaseList);
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('MainCertController','updateMainCertFromF03B14Manually', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "updateMainCertFromF03B14Manually", method = RequestMethod.POST)
	public Boolean updateMainCertFromF03B14Manually(){
		return mainCertService.updateMainCertFromF03B14Manually();
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('MainCertController','updateCertificateByAdmin', @securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "updateCertificateByAdmin", method = RequestMethod.POST)
	public String updateCertificateByAdmin(@RequestBody MainCert mainCert) throws Exception{
		String result = jobInfoService.canAdminJob(mainCert.getJobNo());
		if(StringUtils.isEmpty(result)){
			result = mainCertService.updateMainContractCert(mainCert);
		} else {
			throw new IllegalAccessException(result);
		}
		return result;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('MainCertController','getMainCertReceiveDateAndAmount', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getMainCertReceiveDateAndAmount", method = RequestMethod.POST)
	public List<MainCertReceiveDateResponse> getMainCertReceiveDateAndAmount(@RequestParam String company, @RequestParam String refDocNo) throws DatabaseOperationException{
			return mainCertService.getMainCertReceiveDateAndAmount(company, refDocNo);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('MainCertController','updateCertificate', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "updateCertificate", method = RequestMethod.POST)
	public String updateCertificate(@RequestBody MainCert mainCert){
		return mainCertService.updateMainContractCert(mainCert);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('MainCertController','insertIPA', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "insertIPA", method = RequestMethod.POST)
	public String insertIPA(@Valid @RequestBody MainCert mainCert) throws DatabaseOperationException{
		return mainCertService.insertIPAAndUpdateMainContractCert(mainCert);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('MainCertController','confirmIPC', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "confirmIPC", method = RequestMethod.POST)
	public String confirmIPC(@Valid @RequestBody MainCert mainCert){
		return mainCertService.confirmMainContractCert(mainCert);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('MainCertController','resetIPC', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "resetIPC", method = RequestMethod.POST)
	public String resetIPC(@Valid @RequestBody MainCert mainCert) throws DatabaseOperationException{
		return mainCertService.resetMainContractCert(mainCert);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('MainCertController','postIPC', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "postIPC", method = RequestMethod.POST)
	public String postIPC(@RequestParam(required = true) String noJob, @RequestParam(required = true) Integer noMainCert) throws DatabaseOperationException{
		return mainCertService.insertAndPostMainContractCert(noJob, noMainCert);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('MainCertController','submitNegativeMainCertForApproval', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "submitNegativeMainCertForApproval", method = RequestMethod.POST)
	public String submitNegativeMainCertForApproval(@RequestParam(required = true) String noJob, 
													@RequestParam(required = true) Integer noMainCert,
													@RequestParam(required = true) Double certAmount
													) throws DatabaseOperationException{
		return mainCertService.submitNegativeMainCertForApproval(noJob, noMainCert, certAmount);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('MainCertController','getCalculatedRetentionRelease', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getCalculatedRetentionRelease", method = RequestMethod.POST)
	public List<MainCertRetentionRelease> getCalculatedRetentionRelease(@RequestParam(required = true) String noJob, 
																	@RequestParam(required = true) Integer noMainCert
																	) throws DatabaseOperationException{
		return mainCertRetentionReleaseService.getCalculatedRetentionRelease(noJob, noMainCert);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('MainCertController','updateMainCertContraChargeList', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "updateMainCertContraChargeList", method = RequestMethod.POST)
	public String updateMainCertContraChargeList(@RequestParam(required = true) String noJob, 
																	@RequestParam(required = true) Integer noMainCert,
																	@Valid @RequestBody List<MainCertContraCharge> contraChargeList
																	) throws DatabaseOperationException{
		return mainCertContraChargeService.updateMainCertContraChargeList(noJob, noMainCert, contraChargeList);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('MainCertController','deleteMainCertContraCharge', @securityConfig.getRolePcmsQs(), @securityConfig.getRolePcmsQsReviewer())")
	@RequestMapping(value = "deleteMainCertContraCharge", method = RequestMethod.POST)
	public String deleteMainCertContraCharge(@Valid @RequestBody MainCertContraCharge mainCertContraCharge) throws DatabaseOperationException{
		return mainCertContraChargeService.deleteMainCertContraCharge(mainCertContraCharge);
	}
}
