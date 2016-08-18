package com.gammon.qs.webservice.serviceProvider.makeHTMLStrForPaymentCertService;

import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import com.gammon.pcms.service.HTMLService;

public class MarshallingMakeHTMLStrForPaymentCertEndpoint extends AbstractMarshallingPayloadEndpoint {
	
	private HTMLService htmlService;
	
	public MarshallingMakeHTMLStrForPaymentCertEndpoint(HTMLService htmlService, Marshaller marshaller){
		super(marshaller);
		this.htmlService = htmlService;
	}

	protected Object invokeInternal(Object request) throws Exception {
		MakeHTMLStrForPaymentCertServiceRequest requestObj = (MakeHTMLStrForPaymentCertServiceRequest)request;
		MakeHTMLStrForPaymentCertServiceResponse responseObj = new MakeHTMLStrForPaymentCertServiceResponse();
		responseObj.setHtmlStr(htmlService.makeHTMLStringForSCPaymentCert(requestObj.getJobNumber(), requestObj.getPackageNo(), requestObj.getPaymentNo(), requestObj.getHtmlVersion()));
		return responseObj;
	}

}
