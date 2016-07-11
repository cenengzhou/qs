package com.gammon.pcms.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.pcms.scheduler.service.PaymentPostingService;

@RestController
@RequestMapping(value = "service/paymentposting/")
public class PaymentPostingController {
	
	@Autowired
	private PaymentPostingService paymentPostingService;
	
	@RequestMapping(value = "runPaymentPosting", method = RequestMethod.POST)
	public void runPaymentPosting(){
		paymentPostingService.runPaymentPosting();
	}
	
}
