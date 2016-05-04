package com.gammon.qs.webservice.serviceProvider.makeHTMLStrForAddendumService;

import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import com.gammon.qs.service.HTMLStringForApprovalContentService;

public class MarshallingMakeHTMLStrForAddendumEndpoint extends
		AbstractMarshallingPayloadEndpoint {
	
	private HTMLStringForApprovalContentService htmlStringForApprovalContentHBImpl;
	
	public MarshallingMakeHTMLStrForAddendumEndpoint(HTMLStringForApprovalContentService htmlStringForApprovalContentHBImpl, Marshaller marshaller){
		super(marshaller);
		this.htmlStringForApprovalContentHBImpl = htmlStringForApprovalContentHBImpl;
	}

	protected Object invokeInternal(Object request) throws Exception {
		makeHTMLStrForAddendumServiceRequest requestObj = (makeHTMLStrForAddendumServiceRequest)request;
		makeHTMLStrForAddendumServiceResponse responseObj = new makeHTMLStrForAddendumServiceResponse();
		responseObj.setHtmlStr(htmlStringForApprovalContentHBImpl.makeHTMLStringForAddendumApproval( requestObj.getJobNumber(), requestObj.getPackageNo(), requestObj.getHtmlVersion()));
		return responseObj;
	}

}
