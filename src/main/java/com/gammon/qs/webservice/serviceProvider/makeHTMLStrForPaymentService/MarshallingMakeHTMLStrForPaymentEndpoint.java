package com.gammon.qs.webservice.serviceProvider.makeHTMLStrForPaymentService;

import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import com.gammon.qs.service.HTMLStringForApprovalContentService;

public class MarshallingMakeHTMLStrForPaymentEndpoint extends AbstractMarshallingPayloadEndpoint {
	
	private HTMLStringForApprovalContentService htmlStringForApprovalContentHBImpl;
	
	public MarshallingMakeHTMLStrForPaymentEndpoint(HTMLStringForApprovalContentService htmlStringForApprovalContentHBImpl, Marshaller marshaller){
		super(marshaller);
		this.htmlStringForApprovalContentHBImpl = htmlStringForApprovalContentHBImpl;
	}

	protected Object invokeInternal(Object request) throws Exception {
		makeHTMLStrForPaymentServiceRequest requestObj = (makeHTMLStrForPaymentServiceRequest)request;
		makeHTMLStrForPaymentServiceResponse responseObj = new makeHTMLStrForPaymentServiceResponse();
		responseObj.setHtmlStr(htmlStringForApprovalContentHBImpl.makeHTMLStringForSCPaymentCert(requestObj.getJobNumber(), requestObj.getPackageNo(), null, requestObj.getHtmlVersion()));
		return responseObj;
	}

}
