package com.gammon.qs.webservice.serviceProvider.makeHTMLStrForAwardService;

import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import com.gammon.qs.service.HTMLStringForApprovalContentService;

public class MarshallingMakeHTMLStrForAwardEndpoint extends AbstractMarshallingPayloadEndpoint {

	private HTMLStringForApprovalContentService	htmlStringForApprovalContentHBImpl;

	public MarshallingMakeHTMLStrForAwardEndpoint(HTMLStringForApprovalContentService htmlStringForApprovalContentHBImpl, Marshaller marshaller) {
		super(marshaller);
		this.htmlStringForApprovalContentHBImpl = htmlStringForApprovalContentHBImpl;
	}

	protected Object invokeInternal(Object request) throws Exception {
		makeHTMLStrForAwardServiceRequest requestObj = (makeHTMLStrForAwardServiceRequest) request;
		makeHTMLStrForAwardServiceResponse responseObj = new makeHTMLStrForAwardServiceResponse();
		responseObj.setHtmlStr(htmlStringForApprovalContentHBImpl.makeHTMLStringForTenderAnalysis(requestObj.getJobNumber(), requestObj.getPackageNo(), requestObj.getHtmlVersion()));
		return responseObj;
	}

}
