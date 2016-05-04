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

import com.gammon.pcms.dto.rs.provider.request.MakeHTMLStrForAddendumServiceRequest;
import com.gammon.pcms.dto.rs.provider.response.MakeHTMLStrForAddendumServiceResponse;
import com.gammon.pcms.helper.RestTemplateHelper;
import com.gammon.pcms.service.HTMLStringService;

@RestController
@RequestMapping(path = "ws")
public class MakeHTMLStrForAddendumController {

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
	@RequestMapping(path = "makeHTMLStrForAddendum")
	public MakeHTMLStrForAddendumServiceResponse makeHTMLStrForAddendum(@Valid @RequestBody MakeHTMLStrForAddendumServiceRequest requestObj, BindingResult result)
			throws Exception {
		if(result.hasErrors()) throw new IllegalArgumentException(result.getAllErrors().toString());
		MakeHTMLStrForAddendumServiceResponse responseObj = new MakeHTMLStrForAddendumServiceResponse();
		responseObj.setHtmlStr(htmlStringForApprovalContentService.makeHTMLStringForAddendumApproval( requestObj.getJobNumber(), requestObj.getPackageNo(), requestObj.getHtmlVersion()));
		return responseObj;
	}

	/**
	 * REST mapping accept @PathVariable as parameter and
	 * create request object then post via RestTemplate
	 * @param request
	 * @param jobNumber
	 * @param packageNo
	 * @param htmlVersion
	 * @return
	 */
	@RequestMapping(path = "makeHTMLStrForAddendum/{jobNumber}/{packageNo}/{htmlVersion}")
	public MakeHTMLStrForAddendumServiceResponse makeHTMLStrForAddendum(HttpServletRequest request, 
			 @PathVariable String jobNumber, @PathVariable String packageNo, @PathVariable String htmlVersion) {
		MakeHTMLStrForAddendumServiceRequest requestObj = new MakeHTMLStrForAddendumServiceRequest();
		requestObj.setJobNumber(jobNumber);
		requestObj.setPackageNo(packageNo);
		requestObj.setHtmlVersion(htmlVersion);
		restTemplate = restTemplateHelper.getLocalRestTemplateForWS(request.getServerName());
		MakeHTMLStrForAddendumServiceResponse responseObj = restTemplate.postForObject(
				"http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() 
				+ "/ws/makeHTMLStrForAddendum", requestObj, MakeHTMLStrForAddendumServiceResponse.class);
		return responseObj;
	}
}
