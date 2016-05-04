package com.gammon.qs.webservice.serviceProvider.getAttachmentList;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import com.gammon.qs.domain.AbstractAttachment;
import com.gammon.qs.domain.SCAttachment;
import com.gammon.qs.domain.SCDetailsAttachment;
import com.gammon.qs.domain.SCPaymentAttachment;
import com.gammon.qs.service.AttachmentService;

public class MarshallingGetAttachmentListEndpoint extends AbstractMarshallingPayloadEndpoint {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	private AttachmentService attachmentRepository;
	
	public MarshallingGetAttachmentListEndpoint(AttachmentService attachmentRepository, Marshaller marshaller){
		super(marshaller);
		this.attachmentRepository = attachmentRepository;
	}

	protected Object invokeInternal(Object request) throws Exception {
		getAttachmentListRequest requestObj = (getAttachmentListRequest)request;
		getAttachmentListResponseList responseListObj = new getAttachmentListResponseList();
		
		List<? extends AbstractAttachment> attachmentList;
		if(SCAttachment.SCDetailsNameObject.equals(requestObj.getNameObject())){
			logger.info("Web Service called by AP: SCDetail Attachments");
			attachmentList = attachmentRepository.getAddendumAttachmentList(requestObj.getNameObject(), requestObj.getTextKey());
		}
		else if(SCAttachment.SCPaymentNameObject.equals(requestObj.getNameObject())){
			logger.info("Web Service called by AP: SCPayment Attachments");
			attachmentList = attachmentRepository.getPaymentAttachmentList(requestObj.getNameObject(), requestObj.getTextKey());
		}
		else{
			logger.info("Web Service called by AP: SCPackage Attachments / Vendor Attachments (@JDE)");
			attachmentList = attachmentRepository.getAttachmentList(requestObj.getNameObject(), requestObj.getTextKey());
		}
		
		if (attachmentList!=null && attachmentList.size()>0){
			responseListObj.setAttachmentList(new ArrayList<getAttachmentListResponse>());
			
			for(AbstractAttachment attachment:attachmentList){
				getAttachmentListResponse responseObj = new getAttachmentListResponse();
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
		return responseListObj;
	}
}
