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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.pcms.dto.rs.provider.response.PCMSDTO;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.MainCert;
import com.gammon.qs.domain.MainCertRetentionRelease;
import com.gammon.qs.service.MainCertRetentionReleaseService;
import com.gammon.qs.service.MainCertService;

@RestController
@RequestMapping(value = "service/mainCert/")
public class MainCertController {

	@Autowired
	private MainCertService mainCertService;
	@Autowired
	private MainCertRetentionReleaseService mainCertRetentionReleaseService;

	// ---------------- get ----------------
	@RequestMapping(value = "getCertificateList",
					method = RequestMethod.GET)
	public List<MainCert> getCertificateList(@RequestParam(required = true) String noJob) {

		return mainCertService.getCertificateList(noJob);
	}

	@RequestMapping(value = "getPaidMainCertList", method = RequestMethod.GET)
	public List<Integer> getPaidMainCertList(@RequestParam(required = true) String noJob) {
		List<Integer> mainCertList= null;
		try {
			mainCertList= mainCertService.getPaidMainCertList(noJob);
		} catch (DatabaseOperationException e) {
			e.printStackTrace();
		}
		return mainCertList;
	}
	
	
	@RequestMapping(value = "getRetentionReleaseList",
					method = RequestMethod.GET)
	public List<MainCertRetentionRelease> getRetentionReleaseList(@RequestParam(required = true) String noJob) {
		return mainCertRetentionReleaseService.getRetentionReleaseList(noJob);
	}

	@RequestMapping(value = "getCertificate", method = RequestMethod.GET)
	public MainCert getCertificate(@RequestParam String jobNo, @RequestParam Integer certificateNumber){
		return mainCertService.getCertificate(jobNo, certificateNumber);
	}
	
	// ---------------- update / calculate ----------------
	@RequestMapping(value = "updateRetentionRelease",
					method = RequestMethod.POST)
	public PCMSDTO updateRetentionRelease(	@RequestParam(required = true) String noJob,
											@Valid @RequestBody List<MainCertRetentionRelease> retentionReleaseList) {
		return mainCertRetentionReleaseService.updateRetentionRelease(noJob, retentionReleaseList);
	}

	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "updateMainCertFromF03B14Manually", method = RequestMethod.POST)
	public void updateMainCertFromF03B14Manually(){
		mainCertService.updateMainCertFromF03B14Manually();
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQsAdmin())")
	@RequestMapping(value = "updateCertificate", method = RequestMethod.POST)
	public String updateCertificate(@RequestBody MainCert mainCert){
		return mainCertService.updateMainContractCert(mainCert);
	}
	

}
