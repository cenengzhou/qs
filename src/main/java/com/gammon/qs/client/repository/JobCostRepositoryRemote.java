package com.gammon.qs.client.repository;

import java.util.Date;
import java.util.List;

import com.gammon.qs.domain.ARRecord;
import com.gammon.qs.domain.AccountLedgerWrapper;
import com.gammon.qs.domain.AccountMaster;
import com.gammon.qs.domain.PORecord;
import com.gammon.qs.wrapper.APRecordPaginationWrapper;
import com.gammon.qs.wrapper.AccountBalanceByDateRangePaginationWrapper;
import com.gammon.qs.wrapper.AccountLedgerPaginationWrapper;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.PaymentHistoriesWrapper;
import com.gammon.qs.wrapper.PurchaseOrderEnquiryWrapper;
import com.google.gwt.user.client.rpc.RemoteService;

public interface JobCostRepositoryRemote extends RemoteService {	
	//AP (Supplier Ledger)
	APRecordPaginationWrapper obtainAPRecordPaginationWrapper(String jobNumber, String invoiceNumber, String supplierNumber, String documentNumber, String documentType, String subledger, String subledgerType) throws Exception;
	APRecordPaginationWrapper getAPRecordPaginationWrapperByPage(Integer pageNum) throws Exception;
	List<PaymentHistoriesWrapper> getAPPaymentHistories(String company, String documentType, Integer supplierNumber, Integer documentNumber);
	
	//AR (Customer Ledger)
	List<ARRecord> getARRecordList(String jobNumber, String reference, String customerNumber, String documentNumber, String documentType) throws Exception;
	PaginationWrapper<ARRecord> getARRecordListByPage(String jobNumber, String reference, String customerNumber, String documentNumber, String documentType, int pageNum, int recordsPerPage) throws Exception;
	
	// Purchase Order
	// Last Modified : Brian Tse @ 20101008 1625
	public List<PORecord> getPORecordList(String jobNumber, String orderNumber, String orderType, String supplierNumber)throws Exception;
	public PurchaseOrderEnquiryWrapper getPurchaseOrderEnquiryWrapperByPage(Integer pageNum) throws Exception;
	public PurchaseOrderEnquiryWrapper getPurchaseOrderEnquiryWrapperByPage(String jobNumber, String orderNumber, String orderType, String supplierNumber, String decription, int pageNum);
	
	//Account Master
	List<AccountMaster> getAccountMasterList(String jobNumber) throws Exception;
	String createAccountMaster(String jobNumber, String objectAcc, String subsidiary);
	String createAccountMasterByGroup(Boolean scResource, Boolean bqResourceSummary, Boolean scDetails, Boolean forecast, String jobNumber);
	
	//Account Balance (Job Cost)
	AccountBalanceByDateRangePaginationWrapper getAccountBalanceByDateRangeList(String jobNumber, String subLedger, String subLedgerType, String totalFlag, String postFlag, Date fromDate, Date thruDate, String year, String period) throws Exception;
	AccountBalanceByDateRangePaginationWrapper getAccountBalanceByDateRangePaginationWrapperByPage(Integer pageNum) throws Exception;
	AccountBalanceByDateRangePaginationWrapper getAccountBalanceByDateRangePaginationWrapperByFilter(String objectCode, String subsidiaryCode, boolean filterZero) throws Exception;
	
	//Account Ledger
	List<AccountLedgerWrapper> getAccountLedger(String accountId, String postedCode, String ledgerType, Date glDate1, Date glDate2, String subledgerType, String subledger) throws Exception;
	AccountLedgerPaginationWrapper getAccountLedgerByAccountCodeList(String accountCode, String postFlag, String ledgerType, Date fromDate, Date thruDate, String subLedgerType, String subLedger) throws Exception;
	AccountLedgerPaginationWrapper getAccountLedgerPaginationWrapperByPage(Integer pageNum) throws Exception;
}

