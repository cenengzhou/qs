package com.gammon.qs.webservice.serviceProvider.makeHTMLStrForSplitTerminateService;

import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import com.gammon.qs.service.HTMLStringForApprovalContentService;

public class MarshallingMakeHTMLStrForSplitTerminateEndpoint extends
		AbstractMarshallingPayloadEndpoint {
	
	private HTMLStringForApprovalContentService htmlStringForApprovalContentHBImpl;
	
	public MarshallingMakeHTMLStrForSplitTerminateEndpoint(HTMLStringForApprovalContentService htmlStringForApprovalContentHBImpl, Marshaller marshaller){
		super(marshaller);
		this.htmlStringForApprovalContentHBImpl = htmlStringForApprovalContentHBImpl;
	}

	protected Object invokeInternal(Object request) throws Exception {
		makeHTMLStrForSplitTerminateServiceRequest requestObj = (makeHTMLStrForSplitTerminateServiceRequest)request;
		makeHTMLStrForSplitTerminateServiceResponse responseObj = new makeHTMLStrForSplitTerminateServiceResponse();
		responseObj.setHtmlStr(htmlStringForApprovalContentHBImpl.makeHTMLStringForSplitTermSC(requestObj.getJobNumber(), requestObj.getPackageNo(), requestObj.getHtmlVersion()));
		return responseObj;
	}

}
