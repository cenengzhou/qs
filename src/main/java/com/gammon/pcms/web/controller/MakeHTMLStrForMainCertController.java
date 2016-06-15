package com.gammon.pcms.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.gammon.pcms.dto.rs.provider.request.MakeHTMLStrForMainCertServiceRequest;
import com.gammon.pcms.dto.rs.provider.response.MakeHTMLStrForMainCertServiceResponse;
import com.gammon.pcms.helper.RestTemplateHelper;
import com.gammon.pcms.service.HTMLStringService;

@RestController
@RequestMapping(path = "ws")
public class MakeHTMLStrForMainCertController {

	private RestTemplate restTemplate;
	@Autowired
	private HTMLStringService htmlStringForApprovalContentService;
	@Autowired
	private RestTemplateHelper restTemplateHelper;
	/**
	 * REST mapping
	 * @param requestObj
	 * @param result
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path = "makeHTMLStrForMainCert")
	public MakeHTMLStrForMainCertServiceResponse makeHTMLStrForMainCert(@Valid @RequestBody MakeHTMLStrForMainCertServiceRequest requestObj, BindingResult result)
			throws Exception {
		if(result.hasErrors()) throw new IllegalArgumentException(result.getAllErrors().toString());
		MakeHTMLStrForMainCertServiceResponse responseObj = new MakeHTMLStrForMainCertServiceResponse();
		responseObj.setHtmlStr(htmlStringForApprovalContentService.makeHTMLStringForMainCert( requestObj.getJobNumber(), requestObj.getMainCertNo(), requestObj.getHtmlVersion()));
		return responseObj;
	}

	/**
	 * REST mapping accept @PathVariable as parameter and
	 * create request object then post via RestTemplate
	 * @param request
	 * @param jobNumber
	 * @param mainCertNo
	 * @param htmlVersion
	 * @return
	 */
	@RequestMapping(path = "makeHTMLStrForMainCert/{jobNumber}/{mainCertNo}/{htmlVersion}")
	public MakeHTMLStrForMainCertServiceResponse makeHTMLStrForMainCert(HttpServletRequest request, 
			 @PathVariable String jobNumber, @PathVariable String mainCertNo, @PathVariable String htmlVersion) {
		MakeHTMLStrForMainCertServiceRequest requestObj = new MakeHTMLStrForMainCertServiceRequest();
		requestObj.setJobNumber(jobNumber);
		requestObj.setMainCertNo(mainCertNo);
		requestObj.setHtmlVersion(htmlVersion);
		restTemplate = restTemplateHelper.getRestTemplateForWS(request.getServerName());
		MakeHTMLStrForMainCertServiceResponse responseObj = restTemplate.postForObject(
				"http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() 
				+ "/ws/makeHTMLStrForMainCert", requestObj, MakeHTMLStrForMainCertServiceResponse.class);
		return responseObj;
	}
}
