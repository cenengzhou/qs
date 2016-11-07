/**
 * PCMS-TC
 * com.gammon.pcms.web
 * MainCertController.java
 * @since Jun 27, 2016 11:20:41 AM
 * @author tikywong
 */
package com.gammon.pcms.web.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

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
import com.gammon.qs.service.MainCertContraChargeService;
import com.gammon.qs.service.MainCertRetentionReleaseService;
import com.gammon.qs.service.MainCertService;

@RestController
@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsEnq())")
@RequestMapping(value = "service/mainCert/")
public class MainCertController {

	@Autowired
	private MainCertService mainCertService;
	@Autowired
	private MainCertRetentionReleaseService mainCertRetentionReleaseService;
	@Autowired
	private MainCertContraChargeService mainCertContraChargeService;

	// ---------------- get ----------------
	@RequestMapping(value = "getCertificateList", method = RequestMethod.GET)
	public List<MainCert> getCertificateList(@RequestParam(required = true) String noJob) {

		return mainCertService.getCertificateList(noJob);
	}

	@RequestMapping(value = "getPaidMainCertList", method = RequestMethod.GET)
	public List<Integer> getPaidMainCertList(@RequestParam(required = true) String noJob) throws DatabaseOperationException {
		List<Integer> mainCertList= null;
		mainCertList= mainCertService.getPaidMainCertList(noJob);
		return mainCertList;
	}
	
	@RequestMapping(value = "getMainCertNoList", method = RequestMethod.GET)
	public List<Integer> getMainCertNoList(@RequestParam(required = true) String noJob) throws DatabaseOperationException {
		List<Integer> mainCertList= null;
		mainCertList= mainCertService.getMainCertNoList(noJob);
		return mainCertList;
	}

	
	@RequestMapping(value = "getRetentionReleaseList", method = RequestMethod.GET)
	public List<MainCertRetentionRelease> getRetentionReleaseList(@RequestParam(required = true) String noJob) {
		return mainCertRetentionReleaseService.getRetentionReleaseList(noJob);
	}
	
	
	@RequestMapping(value = "getMainCertContraChargeList", method = RequestMethod.GET)
	public List<MainCertContraCharge> getMainCertContraChargeList(@RequestParam(required = true) String noJob, @RequestParam(required = true) Integer noMainCert) {
		return mainCertContraChargeService.getMainCertContraChargeList(noJob, noMainCert);
	}
	
	@RequestMapping(value = "getCertificate", method = RequestMethod.GET)
	public MainCert getCertificate(@RequestParam String jobNo, @RequestParam Integer certificateNumber){
		return mainCertService.getCertificate(jobNo, certificateNumber);
	}
	
	@RequestMapping(value = "getCertificateDashboardData", method = RequestMethod.GET)
	public List<BigDecimal> getCertificateDashboardData(@RequestParam(required = true) String noJob,
														@RequestParam(	required = true) String type,
														@RequestParam(	required = false,
														defaultValue = "0") BigDecimal year,
														@RequestParam(	required = false,
														defaultValue = "0") BigDecimal month 
														) {
		return mainCertService.getCertificateDashboardData(year, month, noJob, type);
	}
	
	@RequestMapping(value = "getLatestMainCert", method = RequestMethod.GET)
	public MainCert getLatestMainCert(@RequestParam(required = true) String noJob, @RequestParam(required = false) String status) {
		return mainCertService.getLatestMainCert(noJob, status);
	}
	
	
	@RequestMapping(value = "getCumulativeRetentionReleaseByJob", method = RequestMethod.GET)
	public PCMSDTO getCumulativeRetentionReleaseByJob(@RequestParam(required = true) String noJob, @RequestParam(required = true) Integer noMainCert) {
		return mainCertRetentionReleaseService.getCumulativeRetentionReleaseByJob(noJob, noMainCert);
	}
	
	// ---------------- update / calculate ----------------
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "createMainCert",	method = RequestMethod.POST)
	public String createMainCert(@Valid @RequestBody MainCert mainCert) {
		String result = "";
		try {
			return mainCertService.createMainCert(mainCert);
		} catch (DatabaseOperationException e) {
			result = "Main Cert caanot be created.";
			e.printStackTrace();
			GlobalExceptionHandler.checkAccessDeniedException(e);
		}
		return result;
		
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "updateRetentionRelease", method = RequestMethod.POST)
	public PCMSDTO updateRetentionRelease(	@RequestParam(required = true) String noJob,
											@Valid @RequestBody List<MainCertRetentionRelease> retentionReleaseList) {
		return mainCertRetentionReleaseService.updateRetentionRelease(noJob, retentionReleaseList);
	}

	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "updateMainCertFromF03B14Manually", method = RequestMethod.POST)
	public void updateMainCertFromF03B14Manually(){
		mainCertService.updateMainCertFromF03B14Manually();
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "updateCertificate", method = RequestMethod.POST)
	public String updateCertificate(@RequestBody MainCert mainCert){
		return mainCertService.updateMainContractCert(mainCert);
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "updateCertificateByAdmin", method = RequestMethod.POST)
	public String updateCertificateByAdmin(@RequestBody MainCert mainCert){
		return mainCertService.updateMainContractCert(mainCert);
	}
	
	@RequestMapping(value = "getMainCertReceiveDateAndAmount", method = RequestMethod.POST)
	public List<MainCertReceiveDateResponse> getMainCertReceiveDateAndAmount(@RequestParam String company, @RequestParam String refDocNo) throws DatabaseOperationException{
			return mainCertService.getMainCertReceiveDateAndAmount(company, refDocNo);
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "insertIPA", method = RequestMethod.POST)
	public String insertIPA(@Valid @RequestBody MainCert mainCert) throws DatabaseOperationException{
		return mainCertService.insertIPAAndUpdateMainContractCert(mainCert);
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "confirmIPC", method = RequestMethod.POST)
	public String confirmIPC(@Valid @RequestBody MainCert mainCert){
		return mainCertService.confirmMainContractCert(mainCert);
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "resetIPC", method = RequestMethod.POST)
	public String resetIPC(@Valid @RequestBody MainCert mainCert) throws DatabaseOperationException{
		return mainCertService.resetMainContractCert(mainCert);
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "postIPC", method = RequestMethod.POST)
	public String postIPC(@RequestParam(required = true) String noJob, @RequestParam(required = true) Integer noMainCert) throws DatabaseOperationException{
		return mainCertService.insertAndPostMainContractCert(noJob, noMainCert);
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "submitNegativeMainCertForApproval", method = RequestMethod.POST)
	public String submitNegativeMainCertForApproval(@RequestParam(required = true) String noJob, 
													@RequestParam(required = true) Integer noMainCert,
													@RequestParam(required = true) Double certAmount
													) throws DatabaseOperationException{
		return mainCertService.submitNegativeMainCertForApproval(noJob, noMainCert, certAmount);
	}
	
	
	@RequestMapping(value = "getCalculatedRetentionRelease", method = RequestMethod.POST)
	public List<MainCertRetentionRelease> getCalculatedRetentionRelease(@RequestParam(required = true) String noJob, 
																	@RequestParam(required = true) Integer noMainCert
																	) throws DatabaseOperationException{
		return mainCertRetentionReleaseService.getCalculatedRetentionRelease(noJob, noMainCert);
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "updateMainCertContraChargeList", method = RequestMethod.POST)
	public String updateMainCertContraChargeList(@RequestParam(required = true) String noJob, 
																	@RequestParam(required = true) Integer noMainCert,
																	@Valid @RequestBody List<MainCertContraCharge> contraChargeList
																	) throws DatabaseOperationException{
		return mainCertContraChargeService.updateMainCertContraChargeList(noJob, noMainCert, contraChargeList);
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "deleteMainCertContraCharge", method = RequestMethod.POST)
	public String deleteMainCertContraCharge(@Valid @RequestBody MainCertContraCharge mainCertContraCharge) throws DatabaseOperationException{
		return mainCertContraChargeService.deleteMainCertContraCharge(mainCertContraCharge);
	}
}
