package com.gammon.qs.webservice.serviceProvider.makeHTMLStrForPaymentService;

import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import com.gammon.pcms.service.HTMLService;

public class MarshallingMakeHTMLStrForPaymentEndpoint extends AbstractMarshallingPayloadEndpoint {
	
	private HTMLService htmlService;
	
	public MarshallingMakeHTMLStrForPaymentEndpoint(HTMLService htmlService, Marshaller marshaller){
		super(marshaller);
		this.htmlService = htmlService;
	}

	protected Object invokeInternal(Object request) throws Exception {
		makeHTMLStrForPaymentServiceRequest requestObj = (makeHTMLStrForPaymentServiceRequest)request;
		makeHTMLStrForPaymentServiceResponse responseObj = new makeHTMLStrForPaymentServiceResponse();
		responseObj.setHtmlStr(htmlService.makeHTMLStringForSCPaymentCert(requestObj.getJobNumber(), requestObj.getPackageNo(), null, requestObj.getHtmlVersion()));
		return responseObj;
	}

}
