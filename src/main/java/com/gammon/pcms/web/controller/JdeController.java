package com.gammon.pcms.web.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.APRecord;
import com.gammon.qs.domain.ARRecord;
import com.gammon.qs.domain.MasterListObject;
import com.gammon.qs.domain.MasterListSubsidiary;
import com.gammon.qs.domain.MasterListVendor;
import com.gammon.qs.domain.PORecord;
import com.gammon.qs.service.BudgetPostingService;
import com.gammon.qs.service.JobCostService;
import com.gammon.qs.service.MasterListService;
import com.gammon.qs.wrapper.PaymentHistoriesWrapper;
import com.gammon.qs.wrapper.WorkScopeWrapper;
import com.gammon.qs.wrapper.monthEndResult.AccountBalanceByDateRangeWrapper;
import com.gammon.qs.wrapper.monthEndResult.AccountLedgerWrapper;

@RestController
@RequestMapping(value = "service/jde/")
public class JdeController {
	
	@Autowired
	private JobCostService jobCostService;
	@Autowired
	private MasterListService masterListService;
	@Autowired
	private BudgetPostingService budgetPostingService;
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('JdeController','getPORecordList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getPORecordList", method = RequestMethod.POST)
	public List<PORecord> getPORecordList(
			@RequestParam String jobNumber, 
			@RequestParam(required = false) String orderNumber, 
			@RequestParam(required = false) String orderType, 
			@RequestParam(required = false)  String supplierNumber) throws Exception{
			return jobCostService.getPORecordList(jobNumber, orderNumber, orderType, supplierNumber);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('JdeController','getARRecordList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getARRecordList", method = RequestMethod.POST)
	public List<ARRecord> getARRecordList(
			@RequestParam String jobNumber, 
			@RequestParam(required = false) String reference, 
			@RequestParam(required = false)  String customerNumber, 
			@RequestParam(required = false)  String documentNumber, 
			@RequestParam(required = false)  String documentType) throws Exception{
			return jobCostService.getARRecordList(jobNumber, reference, customerNumber, documentNumber, documentType);
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('JdeController','obtainAPRecordList', @securityConfig.getRolePcmsEnq())")
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
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('JdeController','getAPPaymentHistories', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getAPPaymentHistories", method = RequestMethod.POST)
	public List<PaymentHistoriesWrapper> getAPPaymentHistories(
			@RequestParam String company, 
			@RequestParam String documentType, 
			@RequestParam Integer supplierNumber, 
			@RequestParam Integer documentNumber,
			HttpServletResponse response){
			return jobCostService.getAPPaymentHistories(company, documentType, supplierNumber, documentNumber);
	}
	
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('JdeController','createAccountMasterByGroup', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "createAccountMasterByGroup", method = RequestMethod.POST)
	public String createAccountMasterByGroup(@RequestParam(required = true) String jobNo,
										@RequestParam(required = false) Boolean resourceCheck, 
										@RequestParam(required = false) Boolean resourceSummaryCheck, 
										@RequestParam(required = false) Boolean scDetailCheck, 
										@RequestParam(required = false) Boolean forecastCheck){
			return jobCostService.createAccountMasterByGroup(resourceCheck, resourceSummaryCheck, scDetailCheck, forecastCheck, jobNo);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('JdeController','getSubcontractor', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getSubcontractor", method = RequestMethod.GET)
	public MasterListVendor getSubcontractor(@RequestParam(name="subcontractorNo") String subcontractorNo) throws DatabaseOperationException{

		MasterListVendor masterListVendor = null;
		masterListVendor = masterListService.obtainVendorByVendorNo(subcontractorNo);
		return masterListVendor;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('JdeController','getSubcontractorList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getSubcontractorList", method = RequestMethod.GET)
	public List<MasterListVendor> getSubcontractorList(@RequestParam(name="searchStr") String searchStr) throws Exception{

		List<MasterListVendor> masterListVendor = null;
		masterListVendor = masterListService.searchVendorList(searchStr);
		return masterListVendor;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('JdeController','searchObjectList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "searchObjectList", method = RequestMethod.POST)
	public List<MasterListObject> searchObjectList(@RequestParam String searchStr) throws Exception{

		List<MasterListObject> masterListObject = null;
		masterListObject = masterListService.searchObjectList(searchStr);
		return masterListObject;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('JdeController','searchSubsidiaryList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "searchSubsidiaryList", method = RequestMethod.POST)
	public List<MasterListSubsidiary> searchSubsidiaryList(@RequestParam String searchStr) throws Exception{

		List<MasterListSubsidiary> masterListSubsidiary = null;
		masterListSubsidiary = masterListService.searchSubsidiaryList(searchStr);
		return masterListSubsidiary;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('JdeController','validateAndCreateAccountCode', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "validateAndCreateAccountCode", method = RequestMethod.POST)
	public String validateAndCreateAccountCode(@RequestParam String jobNo, @RequestParam String objectCode, @RequestParam String subsidiaryCode) throws Exception{
		return masterListService.validateAndCreateAccountCode(jobNo, objectCode, subsidiaryCode);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('JdeController','getSubcontractorWorkScope', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getSubcontractorWorkScope", method = RequestMethod.POST)
	public List<WorkScopeWrapper> getSubcontractorWorkScope(@RequestParam String vendorNo) throws Exception{
		return masterListService.getSubcontractorWorkScope(vendorNo);
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('JdeController','searchVendorAddressDetails', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "searchVendorAddressDetails", method = RequestMethod.POST)
	public MasterListVendor searchVendorAddressDetails(@RequestParam String vendorNo) throws Exception{
		return masterListService.searchVendorAddressDetails(vendorNo);
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('JdeController','postBudget', @securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "postBudget", method = RequestMethod.POST)
	public String postBudget(@RequestParam String jobNumber){
		return budgetPostingService.postBudget(jobNumber, null);
	}
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getAccountBalanceByDateRangeList", method = RequestMethod.GET)
	public List<AccountBalanceByDateRangeWrapper> getAccountBalanceByDateRangeList(
			@RequestParam String jobNumber, 
			@RequestParam String subLedger, 
			@RequestParam String subLedgerType,
			@RequestParam String totalFlag, 
			@RequestParam String postFlag, 
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate, 
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date thruDate, 
			@RequestParam String year, 
			@RequestParam String period
		) throws Exception{
		return jobCostService.getAccountBalanceByDateRangeList(jobNumber, subLedger, subLedgerType, totalFlag, postFlag, fromDate, thruDate, year, period);
	}

	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getAccountLedgerListByAccountCodeList", method = RequestMethod.GET)
	public List<AccountLedgerWrapper> getAccountLedgerListByAccountCodeList(
			@RequestParam String accountCode, 
			@RequestParam String postFlag, 
			@RequestParam String ledgerType, 
			@RequestParam @DateTimeFormat(pattern = "YYYY-MM-DD") Date fromDate, 
			@RequestParam @DateTimeFormat(pattern = "YYYY-MM-DD") Date thruDate, 
			@RequestParam String subLedgerType, 
			@RequestParam String subLedger
		)throws Exception{
		return jobCostService.getAccountLedgerListByAccountCodeList(accountCode, postFlag, ledgerType, fromDate, thruDate, subLedgerType, subLedger);
	}
}
