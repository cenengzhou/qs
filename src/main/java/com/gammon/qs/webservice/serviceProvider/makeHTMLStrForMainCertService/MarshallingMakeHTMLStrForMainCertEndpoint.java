package com.gammon.qs.webservice.serviceProvider.makeHTMLStrForMainCertService;

import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import com.gammon.qs.service.HTMLStringForApprovalContentService;

/**
 * koeyyeung
 * Mar 20, 2015 
 */
public class MarshallingMakeHTMLStrForMainCertEndpoint extends AbstractMarshallingPayloadEndpoint{

	private HTMLStringForApprovalContentService htmlStringForApprovalContentHBImpl;

	public MarshallingMakeHTMLStrForMainCertEndpoint(HTMLStringForApprovalContentService htmlStringForApprovalContentHBImpl, Marshaller marshaller){
		super(marshaller);
		this.htmlStringForApprovalContentHBImpl = htmlStringForApprovalContentHBImpl;
	}

 
	protected Object invokeInternal(Object request) throws Exception {
		MakeHTMLStrForMainCertServiceRequest requestObj = (MakeHTMLStrForMainCertServiceRequest)request;
		MakeHTMLStrForMainCertServiceResponse responseObj = new MakeHTMLStrForMainCertServiceResponse();
		responseObj.setHtmlStr(htmlStringForApprovalContentHBImpl.makeHTMLStringForMainCert(requestObj.getJobNumber(), requestObj.getMainCertNo(), requestObj.getHtmlVersion()));
		return responseObj;
	}

}
