package com.gammon.qs.webservice.serviceProvider.completeAddendumApproval;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import com.gammon.qs.service.PackageService;

public class MarshallingCompleteAddendumApprovalEndpoint extends
		AbstractMarshallingPayloadEndpoint {
	
	private PackageService packageRepository;
	
	public MarshallingCompleteAddendumApprovalEndpoint(PackageService packageRepository, Marshaller marshaller){
		super(marshaller);
		this.packageRepository = packageRepository;
	}

	protected Object invokeInternal(Object request) throws Exception {
		CompleteAddendumApprovalRequest requestObj = (CompleteAddendumApprovalRequest)request;
		CompleteAddendumApprovalResponse responseObj = new CompleteAddendumApprovalResponse();
		responseObj.setCompleted(packageRepository.toCompleteAddendumApproval(requestObj.getJobNumber(), requestObj.getPackageNo(),requestObj.getUser(),requestObj.getApprovalDecision()));
		return responseObj;
	}

}
