/**
 * PCMS-TC
 * com.gammon.pcms.web
 * MainCertController.java
 * @since Jun 27, 2016 11:20:41 AM
 * @author tikywong
 */
package com.gammon.pcms.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.qs.domain.MainCert;
import com.gammon.qs.service.MainCertService;

@RestController
@RequestMapping(value = "service")
public class MainCertController {

	@Autowired
	private MainCertService mainCertService;

	@RequestMapping(value = "getCertificates",
					method = RequestMethod.GET)
	public List<MainCert> getCertificates(@RequestParam(required = true) String noJob) {
		return mainCertService.getCertificates(noJob);
	}
}
