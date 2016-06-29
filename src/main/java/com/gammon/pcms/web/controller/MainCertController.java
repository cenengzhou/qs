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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.qs.domain.MainCert;
import com.gammon.qs.domain.MainCertRetentionRelease;
import com.gammon.qs.service.MainCertRetentionReleaseService;
import com.gammon.qs.service.MainCertService;

@RestController
@RequestMapping(value = "service")
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

	@RequestMapping(value = "getRetentionReleaseList",
					method = RequestMethod.GET)
	public List<MainCertRetentionRelease> getRetentionReleaseList(@RequestParam(required = true) String noJob) {
		return mainCertRetentionReleaseService.getRetentionReleaseList(noJob);
	}

	// ---------------- update / calculate ----------------
	@RequestMapping(value = "calculateRetentionRelease",
					method = RequestMethod.PUT)
	public List<MainCertRetentionRelease> calculateRetentionRelease(@RequestParam(required = true) String noJob,
																			@RequestParam(required = true) Integer noMainCert) {
		return mainCertRetentionReleaseService.calculateRetentionRelease(noJob, noMainCert);
	}

	@RequestMapping(value = "updateRetentionRelease",
					method = RequestMethod.PUT)
	public Boolean updateRetentionRelease(	@RequestParam(required = true) String noJob,
											@Valid @RequestBody List<MainCertRetentionRelease> retentionReleaseList) {
		return mainCertRetentionReleaseService.updateRetentionRelease(noJob, retentionReleaseList);
	}
}
