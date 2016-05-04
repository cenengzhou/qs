package com.gammon.pcms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("file:${message.properties}")
public class MessageConfig {

	@Value("${subcontract.hold.message}")
	private String subcontractHoldMessage;
	@Value("${payment.hold.message}")
	private String paymentHoldMessage;
	
	public String getSubcontractHoldMessage() {
		return subcontractHoldMessage;
	}
	
	public String getPaymentHoldMessage() {
		return paymentHoldMessage;
	}

}
