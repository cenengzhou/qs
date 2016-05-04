package com.gammon.qs.webservice.serviceProvider.getTextAttachment;

import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import com.gammon.qs.service.AttachmentService;

public class MarshallingGetTextAttachmentEndpoint extends
		AbstractMarshallingPayloadEndpoint {
	
	private AttachmentService attachmentRepository;
	
	public MarshallingGetTextAttachmentEndpoint(AttachmentService attachmentRepository, Marshaller marshaller){
		super(marshaller);
		this.attachmentRepository = attachmentRepository;
	}

	protected Object invokeInternal(Object request) throws Exception {
		GetTextAttachmentRequest requestObj = (GetTextAttachmentRequest)request;
		GetTextAttachmentResponse responseObj = new GetTextAttachmentResponse();
		
		responseObj.setTextAttachment(attachmentRepository.obtainTextAttachmentContent(requestObj.getNameObject(), requestObj.getTextKey(), requestObj.getSequenceNo()));
		return responseObj;
	}

}
