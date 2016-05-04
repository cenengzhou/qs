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

import com.gammon.pcms.dto.rs.provider.request.CompleteSplitTerminateRequest;
import com.gammon.pcms.dto.rs.provider.response.CompleteSplitTerminateResponse;
import com.gammon.pcms.helper.RestTemplateHelper;
import com.gammon.qs.service.PackageService;

@RestController
@RequestMapping(path = "ws")
public class CompleteSplitTerminateController {
	@Autowired
	private PackageService packageService;
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
	@RequestMapping(path = "completeSplitTerminate")
	public CompleteSplitTerminateResponse completeSplitTerminate(@Valid @RequestBody CompleteSplitTerminateRequest requestObj, BindingResult result)
			throws Exception {
		if(result.hasErrors()) throw new IllegalArgumentException(result.getAllErrors().toString());
		CompleteSplitTerminateResponse responseObj = new CompleteSplitTerminateResponse();
		responseObj.setCompleted(packageService.toCompleteSplitTerminate(requestObj.getJobNumber(), requestObj.getPackageNo(), requestObj.getApprovedOrRejected(), requestObj.getSplitOrTerminate()));
		return responseObj;
	}

	/**
	 * REST mapping accept @PathVariable as parameter and
	 * create request object then post via RestTemplate
	 * @param request
	 * @param jobNumber
	 * @param packageNo
	 * @param approvedOrRejected
	 * @return
	 */
	@RequestMapping(path = "completeSplitTerminate/{jobNumber}/{packageNo}/{approvedOrRejected}/{splitOrTerminate}")
	public CompleteSplitTerminateResponse completeSplitTerminate(HttpServletRequest request, 
			 @PathVariable String jobNumber, @PathVariable String packageNo, @PathVariable String approvedOrRejected, @PathVariable String splitOrTerminate) {
		CompleteSplitTerminateRequest requestObj = new CompleteSplitTerminateRequest();
		requestObj.setJobNumber(jobNumber);
		requestObj.setPackageNo(packageNo);
		requestObj.setApprovedOrRejected(approvedOrRejected);
		requestObj.setSplitOrTerminate(splitOrTerminate);
		restTemplate = restTemplateHelper.getLocalRestTemplateForWS(request.getServerName());
		CompleteSplitTerminateResponse responseObj = restTemplate.postForObject(
				"http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() 
				+ "/ws/awardSCPackage", requestObj, CompleteSplitTerminateResponse.class);
		return responseObj;
	}
}
