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

import com.gammon.pcms.dto.rs.provider.request.CompleteAddendumApprovalRequest;
import com.gammon.pcms.dto.rs.provider.response.CheckJobIsConvertedResponse;
import com.gammon.pcms.dto.rs.provider.response.CompleteAddendumApprovalResponse;
import com.gammon.pcms.helper.RestTemplateHelper;
import com.gammon.qs.service.SubcontractService;

@RestController
@RequestMapping(path = "ws")
public class CompleteAddendumApprovalController {
	@Autowired
	private SubcontractService packageService;
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
	@RequestMapping(path = "completeAddendumApproval")
	public CompleteAddendumApprovalResponse completeAddendumApproval(@Valid @RequestBody CompleteAddendumApprovalRequest requestObj, BindingResult result)
			throws Exception {
		if(result.hasErrors()) throw new IllegalArgumentException(result.getAllErrors().toString());
		CompleteAddendumApprovalResponse responseObj = new CompleteAddendumApprovalResponse();
		responseObj.setCompleted(packageService.toCompleteAddendumApproval(requestObj.getJobNumber(), requestObj.getPackageNo(),requestObj.getUser(),requestObj.getApprovalDecision()));
		return responseObj;
	}

	/**
	 * REST mapping accept @PathVariable as parameter and
	 * create request object then post via RestTemplate
	 * @param request
	 * @param jobNumber
	 * @param packageNo
	 * @param user
	 * @param approvalDecision
	 * @return
	 */
	@RequestMapping(path = "completeAddendumApproval/{jobNumber}/{packageNo}/{user}/{approvalDecision}")
	public CheckJobIsConvertedResponse completeAddendumApproval(HttpServletRequest request,  
			@PathVariable String jobNumber, @PathVariable String packageNo, @PathVariable String user, @PathVariable String approvalDecision) {
		CompleteAddendumApprovalRequest  requestObj = new CompleteAddendumApprovalRequest ();
		requestObj.setJobNumber(jobNumber);
		requestObj.setPackageNo(packageNo);
		requestObj.setUser(user);
		requestObj.setApprovalDecision(approvalDecision);;
		restTemplate = restTemplateHelper.getRestTemplateForWS(request.getServerName());
		CheckJobIsConvertedResponse responseObj = restTemplate.postForObject(
				"http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() 
				+ "/ws/completeAddendumApproval", requestObj, CheckJobIsConvertedResponse.class);
		return responseObj;
	}
}
