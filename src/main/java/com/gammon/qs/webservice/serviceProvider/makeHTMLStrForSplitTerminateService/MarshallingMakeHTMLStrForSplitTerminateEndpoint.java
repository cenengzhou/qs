package com.gammon.qs.webservice.serviceProvider.makeHTMLStrForSplitTerminateService;

import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import com.gammon.pcms.service.HTMLService;

public class MarshallingMakeHTMLStrForSplitTerminateEndpoint extends
		AbstractMarshallingPayloadEndpoint {
	
	private HTMLService htmlService;
	
	public MarshallingMakeHTMLStrForSplitTerminateEndpoint(HTMLService htmlService, Marshaller marshaller){
		super(marshaller);
		this.htmlService = htmlService;
	}

	protected Object invokeInternal(Object request) throws Exception {
		makeHTMLStrForSplitTerminateServiceRequest requestObj = (makeHTMLStrForSplitTerminateServiceRequest)request;
		makeHTMLStrForSplitTerminateServiceResponse responseObj = new makeHTMLStrForSplitTerminateServiceResponse();
		responseObj.setHtmlStr(htmlService.makeHTMLStringForSplitTermSC(requestObj.getJobNumber(), requestObj.getPackageNo(), requestObj.getHtmlVersion()));
		return responseObj;
	}

}
