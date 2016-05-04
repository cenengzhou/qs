package com.gammon.qs.webservice.serviceProvider.makeHTMLStrForPaymentCertService;

import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import com.gammon.qs.service.HTMLStringForApprovalContentService;

public class MarshallingMakeHTMLStrForPaymentCertEndpoint extends AbstractMarshallingPayloadEndpoint {
	
	private HTMLStringForApprovalContentService htmlStringForApprovalContentHBImpl;
	
	public MarshallingMakeHTMLStrForPaymentCertEndpoint(HTMLStringForApprovalContentService htmlStringForApprovalContentHBImpl, Marshaller marshaller){
		super(marshaller);
		this.htmlStringForApprovalContentHBImpl = htmlStringForApprovalContentHBImpl;
	}

	protected Object invokeInternal(Object request) throws Exception {
		MakeHTMLStrForPaymentCertServiceRequest requestObj = (MakeHTMLStrForPaymentCertServiceRequest)request;
		MakeHTMLStrForPaymentCertServiceResponse responseObj = new MakeHTMLStrForPaymentCertServiceResponse();
		responseObj.setHtmlStr(htmlStringForApprovalContentHBImpl.makeHTMLStringForSCPaymentCert(requestObj.getJobNumber(), requestObj.getPackageNo(), requestObj.getPaymentNo(), requestObj.getHtmlVersion()));
		return responseObj;
	}

}
