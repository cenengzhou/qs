package com.gammon.qs.webservice.serviceProvider.completeSplitTerminate;

import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import com.gammon.qs.service.SubcontractService;

public class MarshallingCompleteSplitTerminateEndpoint extends
		AbstractMarshallingPayloadEndpoint {
	
	private SubcontractService packageRepository;
	
	public MarshallingCompleteSplitTerminateEndpoint(SubcontractService packageRepository, Marshaller marshaller){
		super(marshaller);
		this.packageRepository = packageRepository;
	}

	protected Object invokeInternal(Object request) throws Exception {
		CompleteSplitTerminateRequest requestObj = (CompleteSplitTerminateRequest)request;
		CompleteSplitTerminateResponse responseObj = new CompleteSplitTerminateResponse();
		responseObj.setCompleted(packageRepository.toCompleteSplitTerminate(requestObj.getJobNumber(), requestObj.getPackageNo(), requestObj.getApprovedOrRejected(), requestObj.getSplitOrTerminate()));
		return responseObj;
	}

}
