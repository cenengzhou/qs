package com.gammon.pcms.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsEnq())")
@RequestMapping(value = "service/jobcost/")
public class JobCostController {

	@Autowired
	private JobCostService jobCostService;
	
	@RequestMapping(value = "getPORecordList", method = RequestMethod.POST)
	public List<PORecord> getPORecordList(
			@RequestParam String jobNumber, 
			@RequestParam(required = false) String orderNumber, 
			@RequestParam(required = false) String orderType, 
			@RequestParam(required = false)  String supplierNumber) throws Exception{
			return jobCostService.getPORecordList(jobNumber, orderNumber, orderType, supplierNumber);
	}
	
	@RequestMapping(value = "getARRecordList", method = RequestMethod.POST)
	public List<ARRecord> getARRecordList(
			@RequestParam String jobNumber, 
			@RequestParam(required = false) String reference, 
			@RequestParam(required = false)  String customerNumber, 
			@RequestParam(required = false)  String documentNumber, 
			@RequestParam(required = false)  String documentType) throws Exception{
			return jobCostService.getARRecordList(jobNumber, reference, customerNumber, documentNumber, documentType);
	}

	@RequestMapping(value = "obtainAPRecordList", method = RequestMethod.POST)
	public List<APRecord> obtainAPRecordList(
			@RequestParam String jobNumber, 
			@RequestParam(required = false) String invoiceNumber, 
			@RequestParam(required = false) String supplierNumber, 
			@RequestParam(required = false) String documentNumber, 
			@RequestParam(required = false) String documentType, 
			@RequestParam(required = false) String subledger, 
			@RequestParam(required = false) String subledgerType) {
			return jobCostService.obtainAPRecordList(jobNumber, invoiceNumber, supplierNumber, documentNumber, documentType, subledger, subledgerType);
	}
	
	@RequestMapping(value = "getAPPaymentHistories", method = RequestMethod.POST)
	public List<PaymentHistoriesWrapper> getAPPaymentHistories(
			@RequestParam String company, 
			@RequestParam String documentType, 
			@RequestParam Integer supplierNumber, 
			@RequestParam Integer documentNumber,
			HttpServletResponse response){
			return jobCostService.getAPPaymentHistories(company, documentType, supplierNumber, documentNumber);
	}
	
	
	@RequestMapping(value = "createAccountMasterByGroup", method = RequestMethod.POST)
	public String createAccountMasterByGroup(@RequestParam(required = true) String jobNo,
										@RequestParam(required = false) Boolean resourceCheck, 
										@RequestParam(required = false) Boolean resourceSummaryCheck, 
										@RequestParam(required = false) Boolean scDetailCheck, 
										@RequestParam(required = false) Boolean forecastCheck){
			return jobCostService.createAccountMasterByGroup(resourceCheck, resourceSummaryCheck, scDetailCheck, forecastCheck, jobNo);
	}
	
	
	
}
