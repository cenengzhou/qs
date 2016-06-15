package com.gammon.qs.webservice.serviceProvider.completeMainCertApproval;

import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import com.gammon.qs.service.MainCertService;

public class MarshallingCompleteMainCertApprovalEndpoint  extends AbstractMarshallingPayloadEndpoint{

	private MainCertService mainContractCertificateRepository;
	
	
	public MarshallingCompleteMainCertApprovalEndpoint(MainCertService mainContractCertificateRepository, Marshaller marshaller) {
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
