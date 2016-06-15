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

import com.gammon.pcms.dto.rs.provider.request.CheckJobIsConvertedRequest;
import com.gammon.pcms.dto.rs.provider.response.CheckJobIsConvertedResponse;
import com.gammon.pcms.helper.RestTemplateHelper;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.service.JobInfoService;

@RestController
@RequestMapping(path = "ws")
public class CheckJobIsConvertedController {
	@Autowired
	private JobInfoService jobService;
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
	@RequestMapping(path = "checkJobIsConverted")
	public CheckJobIsConvertedResponse checkJobIsConverted(@Valid @RequestBody CheckJobIsConvertedRequest requestObj, BindingResult result)
			throws Exception {
		if(result.hasErrors()) throw new IllegalArgumentException(result.getAllErrors().toString());
		CheckJobIsConvertedResponse responseObj = new CheckJobIsConvertedResponse();
		JobInfo job = jobService.obtainJob(requestObj.getJobNumber());
		if (job !=null && job.getConversionStatus() != null)
			responseObj.setConverted(true);
		else
			responseObj.setConverted(false);
		
		return responseObj;	
	}

	/**
	 * REST mapping accept @PathVariable as parameter and
	 * create request object then post via RestTemplate
	 * @param request
	 * @param jobNumber
	 * @return
	 */
	@RequestMapping(path = "checkJobIsConverted/{jobNumber}")
	public CheckJobIsConvertedResponse checkJobIsConverted(HttpServletRequest request, @PathVariable String jobNumber) {
		CheckJobIsConvertedRequest requestObj = new CheckJobIsConvertedRequest();
		requestObj.setJobNumber(jobNumber);
		restTemplate = restTemplateHelper.getRestTemplateForWS(request.getServerName());
		CheckJobIsConvertedResponse responseObj = restTemplate.postForObject(
				"http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/ws/checkJobIsConverted",
				requestObj, CheckJobIsConvertedResponse.class);
		return responseObj;
	}
}
