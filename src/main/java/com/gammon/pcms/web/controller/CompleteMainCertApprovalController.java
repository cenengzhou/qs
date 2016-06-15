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

import com.gammon.pcms.dto.rs.provider.request.CompleteMainCertApprovalRequest;
import com.gammon.pcms.dto.rs.provider.response.CompleteMainCertApprovalResponse;
import com.gammon.pcms.helper.RestTemplateHelper;
import com.gammon.qs.service.MainCertService;

@RestController
@RequestMapping(path = "ws")
public class CompleteMainCertApprovalController {
	@Autowired
	private MainCertService mainContractCertificateService;
	@Autowired
	private RestTemplateHelper restTemplateHelper;
	private RestTemplate restTemplate;


	/**
	 * REST mapping
	 * @param requestObj
	 * @param result
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path = "completeMainCertApproval")
	public CompleteMainCertApprovalResponse completeMainCertApproval(@Valid @RequestBody CompleteMainCertApprovalRequest requestObj, BindingResult result)
			throws Exception {
		if(result.hasErrors()) throw new IllegalArgumentException(result.getAllErrors().toString());
		CompleteMainCertApprovalResponse response = new CompleteMainCertApprovalResponse();
		response.setCompleted(mainContractCertificateService.toCompleteMainCertApproval(requestObj.getJobNumber(), requestObj.getMainCertNo(), requestObj.getApprovalDecision()));
		return response;
	}

	/**
	 * REST mapping accept @PathVariable as parameter and
	 * create request object then post via RestTemplate
	 * @param request
	 * @param jobNumber
	 * @param mainCertNo
	 * @param approvalDecision
	 * @return
	 */
	@RequestMapping(path = "completeMainCertApproval/{jobNumber}/{mainCertNo}/{approvalDecision}")
	public CompleteMainCertApprovalResponse completeMainCertApproval(HttpServletRequest request, 
			@PathVariable String jobNumber, @PathVariable String mainCertNo, @PathVariable String approvalDecision) {
		CompleteMainCertApprovalRequest requestObj = new CompleteMainCertApprovalRequest();
		requestObj.setJobNumber(jobNumber);
		requestObj.setMainCertNo(mainCertNo);
		requestObj.setApprovalDecision(approvalDecision);
		restTemplate = restTemplateHelper.getRestTemplateForWS(request.getServerName());
		CompleteMainCertApprovalResponse responseObj = restTemplate.postForObject(
				"http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() 
				+ "/ws/completeMainCertApproval", requestObj, CompleteMainCertApprovalResponse.class);
		return responseObj;
	}
}
