package com.gammon.qs.webservice.serviceProvider.completeMainCertApproval;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import com.gammon.qs.service.MainContractCertificateService;

public class MarshallingCompleteMainCertApprovalEndpoint  extends AbstractMarshallingPayloadEndpoint{

	private MainContractCertificateService mainContractCertificateRepository;
	
	
	public MarshallingCompleteMainCertApprovalEndpoint(MainContractCertificateService mainContractCertificateRepository, Marshaller marshaller) {
		super(marshaller);
		this.mainContractCertificateRepository = mainContractCertificateRepository;
	}

	/*
	 * Invoked / Called by Web Service such as Approval System
	 */
	protected Object invokeInternal(Object req) throws Exception {
		CompleteMainCertApprovalRequest request = (CompleteMainCertApprovalRequest)req;
		CompleteMainCertApprovalResponse response = new CompleteMainCertApprovalResponse();
		response.setCompleted(mainContractCertificateRepository.toCompleteMainCertApproval(request.getJobNumber(), request.getMainCertNo(), request.getApprovalDecision()));
		return response;
	}

}
