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

import com.gammon.pcms.dto.rs.provider.request.AwardSCPackageRequest;
import com.gammon.pcms.dto.rs.provider.response.AwardSCPackageResponse;
import com.gammon.pcms.helper.RestTemplateHelper;
import com.gammon.qs.service.PackageService;

@RestController
@RequestMapping(path = "ws")
public class AwardSCPackageController {
	@Autowired
	private PackageService packageService;
	@Autowired
	RestTemplateHelper restTemplateHelper;
	private RestTemplate restTemplate;

	/**
	 * REST mapping
	 * @param requestObj
	 * @param result
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(path = "awardSCPackage")
	public AwardSCPackageResponse getTextAttachment(@Valid @RequestBody AwardSCPackageRequest requestObj, BindingResult result)
			throws Exception {
		if(result.hasErrors()) throw new IllegalArgumentException(result.getAllErrors().toString());
		AwardSCPackageResponse responseObj = new AwardSCPackageResponse();
		responseObj.setCompleted(packageService.toCompleteSCAwardApproval(requestObj.getJobNumber(), requestObj.getPackageNo(), requestObj.getApprovedOrRejected()));
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
	@RequestMapping(path = "awardSCPackage/{jobNumber}/{packageNo}/{approvedOrRejected}")
	public AwardSCPackageResponse getTextAttachmentRequest(HttpServletRequest request, 
			 @PathVariable String jobNumber, @PathVariable String packageNo, @PathVariable String approvedOrRejected) {
		AwardSCPackageRequest requestObj = new AwardSCPackageRequest();
		requestObj.setJobNumber(jobNumber);
		requestObj.setPackageNo(packageNo);
		requestObj.setApprovedOrRejected(approvedOrRejected);
		restTemplate = restTemplateHelper.getLocalRestTemplateForWS(request.getServerName());
		AwardSCPackageResponse responseObj = restTemplate.postForObject(
				"http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() 
				+ "/ws/awardSCPackage", requestObj, AwardSCPackageResponse.class);
		return responseObj;
	}
}
