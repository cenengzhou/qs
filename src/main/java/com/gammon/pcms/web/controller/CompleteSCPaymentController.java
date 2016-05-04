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

import com.gammon.pcms.dto.rs.provider.request.CompleteSCPaymentRequest;
import com.gammon.pcms.dto.rs.provider.response.CompleteSCPaymentResponse;
import com.gammon.pcms.helper.RestTemplateHelper;
import com.gammon.qs.service.PaymentService;

@RestController
@RequestMapping(path = "ws")
public class CompleteSCPaymentController {
	@Autowired
	private PaymentService paymentService;
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
	@RequestMapping(path = "completeSCPayment")
	public CompleteSCPaymentResponse completeSCPayment(@Valid @RequestBody CompleteSCPaymentRequest requestObj, BindingResult result)
			throws Exception {
		if(result.hasErrors()) throw new IllegalArgumentException(result.getAllErrors().toString());
		CompleteSCPaymentResponse responseObj = new CompleteSCPaymentResponse();
		responseObj.setCompleted(paymentService.toCompleteSCPayment(requestObj.getJobNumber(), requestObj.getPackageNo(), requestObj.getApprovalDecision()));
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
	@RequestMapping(path = "completeSCPayment/{jobNumber}/{packageNo}/{approvalDecision}")
	public CompleteSCPaymentResponse completeSCPayment(HttpServletRequest request, 
			 @PathVariable String jobNumber, @PathVariable String packageNo, @PathVariable String approvedOrRejected) {
		CompleteSCPaymentRequest requestObj = new CompleteSCPaymentRequest();
		requestObj.setJobNumber(jobNumber);
		requestObj.setPackageNo(packageNo);
		requestObj.setApprovalDecision(approvedOrRejected);
		restTemplate = restTemplateHelper.getLocalRestTemplateForWS(request.getServerName());
		CompleteSCPaymentResponse responseObj = restTemplate.postForObject(
				"http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() 
				+ "/ws/completeSCPayment", requestObj, CompleteSCPaymentResponse.class);
		return responseObj;
	}
}
