package com.gammon.qs.webservice.serviceProvider.makeHTMLStrForAddendumService;

import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import com.gammon.pcms.service.HTMLService;

public class MarshallingMakeHTMLStrForAddendumEndpoint extends
		AbstractMarshallingPayloadEndpoint {
	
	private HTMLService htmlService;
	
	public MarshallingMakeHTMLStrForAddendumEndpoint(HTMLService htmlService, Marshaller marshaller){
		super(marshaller);
		this.htmlService = htmlService;
	}

	protected Object invokeInternal(Object request) throws Exception {
		makeHTMLStrForAddendumServiceRequest requestObj = (makeHTMLStrForAddendumServiceRequest)request;
		makeHTMLStrForAddendumServiceResponse responseObj = new makeHTMLStrForAddendumServiceResponse();
		responseObj.setHtmlStr(htmlService.makeHTMLStringForAddendumApproval( requestObj.getJobNumber(), requestObj.getPackageNo(), requestObj.getHtmlVersion()));
		return responseObj;
	}

}
