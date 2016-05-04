package com.gammon.qs.webservice.serviceProvider.completeSCPayment;

import org.springframework.oxm.Marshaller;
import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;

import com.gammon.qs.service.PaymentService;

public class MarshallingCompleteSCPaymentEndpoint extends
		AbstractMarshallingPayloadEndpoint {

	private PaymentService paymentRepository;
	
	public MarshallingCompleteSCPaymentEndpoint(PaymentService paymentRepository, Marshaller marshaller){
		super(marshaller);
		this.paymentRepository = paymentRepository;
	}
	
	/*
	 * Invoked / Called by Web Service such as Approval System
	 */
	protected Object invokeInternal(Object req) throws Exception {
		CompleteSCPaymentRequest request = (CompleteSCPaymentRequest)req;
		CompleteSCPaymentResponse response = new CompleteSCPaymentResponse();
		response.setCompleted(paymentRepository.toCompleteSCPayment(request.getJobNumber(), request.getPackageNo(), request.getApprovalDecision()));
		return response;
	}

}
