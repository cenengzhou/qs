package com.gammon.qs.webservice.serviceProvider.makeHTMLStrForMainCertService;

import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import com.gammon.pcms.service.HTMLService;

/**
 * koeyyeung
 * Mar 20, 2015 
 */
public class MarshallingMakeHTMLStrForMainCertEndpoint extends AbstractMarshallingPayloadEndpoint{

	private HTMLService htmlService;

	public MarshallingMakeHTMLStrForMainCertEndpoint(HTMLService htmlService, Marshaller marshaller){
		super(marshaller);
		this.htmlService = htmlService;
	}

 
	protected Object invokeInternal(Object request) throws Exception {
		MakeHTMLStrForMainCertServiceRequest requestObj = (MakeHTMLStrForMainCertServiceRequest)request;
		MakeHTMLStrForMainCertServiceResponse responseObj = new MakeHTMLStrForMainCertServiceResponse();
		responseObj.setHtmlStr(htmlService.makeHTMLStringForMainCert(requestObj.getJobNumber(), requestObj.getMainCertNo(), requestObj.getHtmlVersion()));
		return responseObj;
	}

}
