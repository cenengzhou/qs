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

import com.gammon.pcms.dto.rs.provider.request.MakeHTMLStrForPaymentCertServiceRequest;
import com.gammon.pcms.dto.rs.provider.response.MakeHTMLStrForPaymentCertServiceResponse;
import com.gammon.pcms.helper.RestTemplateHelper;
import com.gammon.pcms.service.HTMLStringService;

@RestController
@RequestMapping(path = "ws")
public class MakeHTMLStrForPaymentCertController {

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
	@RequestMapping(path = "makeHTMLStrForPaymentCert")
	public MakeHTMLStrForPaymentCertServiceResponse makeHTMLStrForPaymentCert(@Valid @RequestBody MakeHTMLStrForPaymentCertServiceRequest requestObj, BindingResult result)
			throws Exception {
		if(result.hasErrors()) throw new IllegalArgumentException(result.getAllErrors().toString());
		MakeHTMLStrForPaymentCertServiceResponse responseObj = new MakeHTMLStrForPaymentCertServiceResponse();
		responseObj.setHtmlStr(htmlStringForApprovalContentService.makeHTMLStringForSCPaymentCert( requestObj.getJobNumber(), requestObj.getPackageNo(), requestObj.getPaymentNo(), requestObj.getHtmlVersion()));
		return responseObj;
	}

	/**
	 * REST mapping accept @PathVariable as parameter and
	 * create request object then post via RestTemplate
	 * @param request
	 * @param jobNumber
	 * @param packageNo
	 * @param paymentNo
	 * @param htmlVersion
	 * @return
	 */
	@RequestMapping(path = "makeHTMLStrForPaymentCert/{jobNumber}/{packageNo}/{paymentNo}{htmlVersion}")
	public MakeHTMLStrForPaymentCertServiceResponse makeHTMLStrForPaymentCert(HttpServletRequest request, 
			 @PathVariable String jobNumber, @PathVariable String packageNo, @PathVariable String paymentNo, @PathVariable String htmlVersion) {
		MakeHTMLStrForPaymentCertServiceRequest requestObj = new MakeHTMLStrForPaymentCertServiceRequest();
		requestObj.setJobNumber(jobNumber);
		requestObj.setPackageNo(packageNo);
		requestObj.setPaymentNo(paymentNo);
		requestObj.setHtmlVersion(htmlVersion);
		restTemplate = restTemplateHelper.getLocalRestTemplateForWS(request.getServerName());
		MakeHTMLStrForPaymentCertServiceResponse responseObj = restTemplate.postForObject(
				"http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() 
				+ "/ws/makeHTMLStrForPaymentCert", requestObj, MakeHTMLStrForPaymentCertServiceResponse.class);
		return responseObj;
	}
}
