package com.gammon.qs.webservice.serviceProvider.makeHTMLStrForAwardService;

import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import com.gammon.pcms.service.HTMLService;

public class MarshallingMakeHTMLStrForAwardEndpoint extends AbstractMarshallingPayloadEndpoint {

	private HTMLService	htmlService;

	public MarshallingMakeHTMLStrForAwardEndpoint(HTMLService htmlService, Marshaller marshaller) {
		super(marshaller);
		this.htmlService = htmlService;
	}

	protected Object invokeInternal(Object request) throws Exception {
		makeHTMLStrForAwardServiceRequest requestObj = (makeHTMLStrForAwardServiceRequest) request;
		makeHTMLStrForAwardServiceResponse responseObj = new makeHTMLStrForAwardServiceResponse();
		responseObj.setHtmlStr(htmlService.makeHTMLStringForTenderAnalysis(requestObj.getJobNumber(), requestObj.getPackageNo(), requestObj.getHtmlVersion()));
		return responseObj;
	}

}
