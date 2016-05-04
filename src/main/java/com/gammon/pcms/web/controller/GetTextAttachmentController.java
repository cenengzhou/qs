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

import com.gammon.pcms.dto.rs.provider.request.GetTextAttachmentRequest;
import com.gammon.pcms.dto.rs.provider.response.GetTextAttachmentResponse;
import com.gammon.pcms.helper.RestTemplateHelper;
import com.gammon.qs.service.AttachmentService;

@RestController
@RequestMapping(path = "ws")
public class GetTextAttachmentController {
	@Autowired
	private AttachmentService attachmentService;
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
	@RequestMapping(path = "getTextAttachment")
	public GetTextAttachmentResponse getTextAttachment(@Valid @RequestBody GetTextAttachmentRequest requestObj, BindingResult result)
			throws Exception {
		if(result.hasErrors()) throw new IllegalArgumentException(result.getAllErrors().toString());
		GetTextAttachmentResponse responseObj = new GetTextAttachmentResponse();
		responseObj.setTextAttachment(attachmentService.obtainTextAttachmentContent(requestObj.getNameObject(),
				requestObj.getTextKey(), requestObj.getSequenceNo()));
		return responseObj;
	}

	/**
	 * REST mapping accept @PathVariable as parameter and
	 * create request object then post via RestTemplate
	 * @param request
	 * @param nameObject
	 * @param textKey
	 * @param sequenceNo
	 * @return
	 */
	@RequestMapping(path = "getTextAttachment/{nameObject}/{textKey}/{sequenceNo}")
	public GetTextAttachmentResponse getTextAttachmentRequest(HttpServletRequest request, 
			@PathVariable String nameObject, @PathVariable String textKey, @PathVariable Integer sequenceNo) {
		GetTextAttachmentRequest requestObj = new GetTextAttachmentRequest();
		requestObj.setNameObject(nameObject);
		requestObj.setTextKey(textKey);
		requestObj.setSequenceNo(sequenceNo);
		restTemplate = restTemplateHelper.getLocalRestTemplateForWS(request.getServerName());
		GetTextAttachmentResponse responseObj = restTemplate.postForObject(
				"http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() 
				+ "/ws/getTextAttachment", requestObj, GetTextAttachmentResponse.class);
		return responseObj;
	}
}
