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

import com.gammon.pcms.dto.rs.provider.request.MakeHTMLStrForAwardServiceRequest;
import com.gammon.pcms.dto.rs.provider.response.MakeHTMLStrForAwardServiceResponse;
import com.gammon.pcms.helper.RestTemplateHelper;
import com.gammon.pcms.service.HTMLStringService;

@RestController
@RequestMapping(path = "ws")
public class MakeHTMLStrForAwardController {

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
	@RequestMapping(path = "makeHTMLStrForAward")
	public MakeHTMLStrForAwardServiceResponse makeHTMLStrForAward(@Valid @RequestBody MakeHTMLStrForAwardServiceRequest requestObj, BindingResult result)
			throws Exception {
		if(result.hasErrors()) throw new IllegalArgumentException(result.getAllErrors().toString());
		MakeHTMLStrForAwardServiceResponse responseObj = new MakeHTMLStrForAwardServiceResponse();
		responseObj.setHtmlStr(htmlStringForApprovalContentService.makeHTMLStringForTenderAnalysis( requestObj.getJobNumber(), requestObj.getPackageNo(), requestObj.getHtmlVersion()));
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
	@RequestMapping(path = "makeHTMLStrForAward/{jobNumber}/{packageNo}/{htmlVersion}")
	public MakeHTMLStrForAwardServiceResponse makeHTMLStrForAward(HttpServletRequest request, 
			 @PathVariable String jobNumber, @PathVariable String packageNo, @PathVariable String htmlVersion) {
		MakeHTMLStrForAwardServiceRequest requestObj = new MakeHTMLStrForAwardServiceRequest();
		requestObj.setJobNumber(jobNumber);
		requestObj.setPackageNo(packageNo);
		requestObj.setHtmlVersion(htmlVersion);
		restTemplate = restTemplateHelper.getLocalRestTemplateForWS(request.getServerName());
		MakeHTMLStrForAwardServiceResponse responseObj = restTemplate.postForObject(
				"http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() 
				+ "/ws/makeHTMLStrForAward", requestObj, MakeHTMLStrForAwardServiceResponse.class);
		return responseObj;
	}
}
