package com.gammon.pcms.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.gammon.pcms.dto.rs.provider.request.GetAttachmentListRequest;
import com.gammon.pcms.dto.rs.provider.response.GetAttachmentListResponse;
import com.gammon.pcms.dto.rs.provider.response.GetAttachmentListResponseList;
import com.gammon.pcms.helper.RestTemplateHelper;
import com.gammon.qs.domain.AbstractAttachment;
import com.gammon.qs.domain.SCAttachment;
import com.gammon.qs.domain.SCDetailsAttachment;
import com.gammon.qs.domain.SCPaymentAttachment;
import com.gammon.qs.service.AttachmentService;

@RestController
@RequestMapping(path = "ws")
public class GetAttachmentListController {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
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
	@RequestMapping(path = "getAttachmentList")
	public GetAttachmentListResponseList getAttachmentList(@Valid @RequestBody GetAttachmentListRequest requestObj, BindingResult result)
			throws Exception {
		if(result.hasErrors()) throw new IllegalArgumentException(result.getAllErrors().toString());
		GetAttachmentListResponseList responseListObj = new GetAttachmentListResponseList();
		List<? extends AbstractAttachment> attachmentList;
		if(SCAttachment.SCDetailsNameObject.equals(requestObj.getNameObject())){
			logger.info("Web Service called by AP: SCDetail Attachments");
			attachmentList = attachmentService.getAddendumAttachmentList(requestObj.getNameObject(), requestObj.getTextKey());
		}
		else if(SCAttachment.SCPaymentNameObject.equals(requestObj.getNameObject())){
			logger.info("Web Service called by AP: SCPayment Attachments");
			attachmentList = attachmentService.getPaymentAttachmentList(requestObj.getNameObject(), requestObj.getTextKey());
		}
		else{
			logger.info("Web Service called by AP: SCPackage Attachments / Vendor Attachments (@JDE)");
			attachmentList = attachmentService.getAttachmentList(requestObj.getNameObject(), requestObj.getTextKey());
		}
		
		if (attachmentList!=null && attachmentList.size()>0){
			responseListObj.setAttachmentList(new ArrayList<GetAttachmentListResponse>());
			
			for(AbstractAttachment attachment:attachmentList){
				GetAttachmentListResponse responseObj = new GetAttachmentListResponse();
				if (attachment instanceof SCDetailsAttachment)
					responseObj.setTextKey(requestObj.getTextKey()+"|"+((SCDetailsAttachment) attachment).getScDetails().getSequenceNo());
				else if(attachment instanceof SCPaymentAttachment)
					responseObj.setTextKey(requestObj.getTextKey()+"|"+((SCPaymentAttachment) attachment).getScPaymentCert().getPaymentCertNo().toString());
				else
					responseObj.setTextKey(requestObj.getTextKey());
				
				responseObj.setDocumentType(attachment.getDocumentType());
				responseObj.setFileLink(attachment.getFileLink());
				responseObj.setFileName(attachment.getFileName());
				responseObj.setSequenceNo(attachment.getSequenceNo());
				logger.info("Response - Text Key: "+responseObj.getTextKey()+" Document Type: "+responseObj.getDocumentType()+" File Link: "+responseObj.getFileLink());
				
				responseListObj.getAttachmentList().add(responseObj);
			}
		}
		return responseListObj;	}

	/**
	 * REST mapping accept @PathVariable as parameter and
	 * create request object then post via RestTemplate
	 * @param request
	 * @param nameObject
	 * @param textKey
	 * @return
	 */
	@RequestMapping(path = "getAttachmentList/{nameObject}/{textKey}")
	public GetAttachmentListResponseList getAttachmentList(HttpServletRequest request, 
			 @PathVariable String nameObject, @PathVariable String textKey) {
		GetAttachmentListRequest requestObj = new GetAttachmentListRequest();
		requestObj.setNameObject(nameObject);
		requestObj.setTextKey(textKey);
		restTemplate = restTemplateHelper.getLocalRestTemplateForWS(request.getServerName());
		GetAttachmentListResponseList responseObj = restTemplate.postForObject(
				"http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() 
				+ "/ws/getAttachmentList", requestObj, GetAttachmentListResponseList.class);
		return responseObj;
	}
}
