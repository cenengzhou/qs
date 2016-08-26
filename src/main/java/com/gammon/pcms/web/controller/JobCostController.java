package com.gammon.pcms.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.qs.domain.APRecord;
import com.gammon.qs.domain.ARRecord;
import com.gammon.qs.domain.PORecord;
import com.gammon.qs.service.JobCostService;
import com.gammon.qs.wrapper.PaymentHistoriesWrapper;

@RestController
@RequestMapping(value = "service/jobcost/")
public class JobCostController {

	@Autowired
	private JobCostService jobCostService;
	
	@RequestMapping(value = "getPORecordList", method = RequestMethod.POST)
	public List<PORecord> getPORecordList(
			@RequestParam String jobNumber, 
			@RequestParam(required = false) String orderNumber, 
			@RequestParam(required = false) String orderType, 
			@RequestParam(required = false)  String supplierNumber){
		try {
			return jobCostService.getPORecordList(jobNumber, orderNumber, orderType, supplierNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "getARRecordList", method = RequestMethod.POST)
	public List<ARRecord> getARRecordList(
			@RequestParam String jobNumber, 
			@RequestParam(required = false) String reference, 
			@RequestParam(required = false)  String customerNumber, 
			@RequestParam(required = false)  String documentNumber, 
			@RequestParam(required = false)  String documentType){
		try{
			return jobCostService.getARRecordList(jobNumber, reference, customerNumber, documentNumber, documentType);
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "obtainAPRecordList", method = RequestMethod.POST)
	public List<APRecord> obtainAPRecordList(
			@RequestParam String jobNumber, 
			@RequestParam(required = false) String invoiceNumber, 
			@RequestParam(required = false) String supplierNumber, 
			@RequestParam(required = false) String documentNumber, 
			@RequestParam(required = false) String documentType, 
			@RequestParam(required = false) String subledger, 
			@RequestParam(required = false) String subledgerType){
		try{
			return jobCostService.obtainAPRecordList(jobNumber, invoiceNumber, supplierNumber, documentNumber, documentType, subledger, subledgerType);
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "getAPPaymentHistories", method = RequestMethod.POST)
	public List<PaymentHistoriesWrapper> getAPPaymentHistories(
			@RequestParam String company, 
			@RequestParam String documentType, 
			@RequestParam Integer supplierNumber, 
			@RequestParam Integer documentNumber){
		try{
			return jobCostService.getAPPaymentHistories(company, documentType, supplierNumber, documentNumber);
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
