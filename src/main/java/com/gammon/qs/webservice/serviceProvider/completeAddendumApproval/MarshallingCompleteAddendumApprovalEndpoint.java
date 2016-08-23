package com.gammon.qs.webservice.serviceProvider.completeAddendumApproval;

import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import com.gammon.qs.service.AddendumService;

public class MarshallingCompleteAddendumApprovalEndpoint extends
		AbstractMarshallingPayloadEndpoint {
	
	private AddendumService addendumService;
	
	public MarshallingCompleteAddendumApprovalEndpoint(AddendumService addendumService, Marshaller marshaller){
		super(marshaller);
		this.addendumService = addendumService;
	}

	protected Object invokeInternal(Object request) throws Exception {
		CompleteAddendumApprovalRequest requestObj = (CompleteAddendumApprovalRequest)request;
		CompleteAddendumApprovalResponse responseObj = new CompleteAddendumApprovalResponse();
		responseObj.setCompleted(addendumService.toCompleteAddendumApproval(requestObj.getJobNumber(), requestObj.getPackageNo(),requestObj.getUser(),requestObj.getApprovalDecision()));
		return responseObj;
	}

}
