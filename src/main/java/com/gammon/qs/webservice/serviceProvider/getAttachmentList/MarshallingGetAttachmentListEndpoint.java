package com.gammon.qs.webservice.serviceProvider.getAttachmentList;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import com.gammon.qs.domain.AbstractAttachment;
import com.gammon.qs.domain.AttachSubcontract;
import com.gammon.qs.domain.AttachSubcontractDetail;
import com.gammon.qs.domain.AttachPayment;
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
		if(AttachSubcontract.SCDetailsNameObject.equals(requestObj.getNameObject())){
			logger.info("Web Service called by AP: SCDetail Attachments");
			attachmentList = attachmentRepository.getAddendumAttachmentList(requestObj.getNameObject(), requestObj.getTextKey());
		}
		else if(AttachSubcontract.SCPaymentNameObject.equals(requestObj.getNameObject())){
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
				if (attachment instanceof AttachSubcontractDetail)
					responseObj.setTextKey(requestObj.getTextKey()+"|"+((AttachSubcontractDetail) attachment).getSubcontractDetail().getSequenceNo());
				else if(attachment instanceof AttachPayment)
					responseObj.setTextKey(requestObj.getTextKey()+"|"+((AttachPayment) attachment).getPaymentCert().getPaymentCertNo().toString());
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
