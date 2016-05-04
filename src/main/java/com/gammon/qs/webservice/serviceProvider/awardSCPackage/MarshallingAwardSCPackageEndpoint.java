package com.gammon.qs.webservice.serviceProvider.awardSCPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import com.gammon.qs.service.PackageService;

public class MarshallingAwardSCPackageEndpoint extends
		AbstractMarshallingPayloadEndpoint {

	private PackageService packageRepository;
	
	public MarshallingAwardSCPackageEndpoint(PackageService packageRepository, Marshaller marshaller){
		super(marshaller);
		this.packageRepository = packageRepository;
	}

	protected Object invokeInternal(Object request) throws Exception {
		AwardSCPackageRequest requestObj = (AwardSCPackageRequest)request;
		AwardSCPackageResponse responseObj = new AwardSCPackageResponse();
		responseObj.setCompleted(packageRepository.toCompleteSCAwardApproval(requestObj.getJobNumber(), requestObj.getPackageNo(), requestObj.getApprovedOrRejected()));
		return responseObj;
	}

}
