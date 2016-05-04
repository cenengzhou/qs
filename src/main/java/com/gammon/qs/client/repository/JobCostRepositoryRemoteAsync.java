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
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface JobCostRepositoryRemoteAsync {
	//AP (Supplier Ledger)
	void obtainAPRecordPaginationWrapper(String jobNumber, String invoiceNumber, String supplierNumber, String documentNumber, String documentType, String subledger, String subledgerType, AsyncCallback<APRecordPaginationWrapper> asyncCallback);
	void getAPRecordPaginationWrapperByPage(Integer pageNum, AsyncCallback<APRecordPaginationWrapper> asyncCallback);
	void getAPPaymentHistories(String company, String documentType, Integer supplierNumber, Integer documentNumber, AsyncCallback<List<PaymentHistoriesWrapper>> asyncCallback);
	
	//AR (Customer Ledger)
	void getARRecordList(String jobNumber, String reference, String customerNumber, String documentNumber, String documentType, AsyncCallback<List<ARRecord>> asyncCallback);
	void getARRecordListByPage(String jobNumber, String reference, String customerNumber, String documentNumber, String documentType, int pageNum, int recordsPerPage, AsyncCallback<PaginationWrapper<ARRecord>> asyncCallback);

	// Purchase Order
	// Last MOdified : Brian Tse @ 20101008 1625
	void getPORecordList(String jobNumber, String orderNumber, String orderType, String supplierNumber, AsyncCallback<List<PORecord>> asyncCallback);
	void getPurchaseOrderEnquiryWrapperByPage(Integer pageNum, AsyncCallback<PurchaseOrderEnquiryWrapper> asyncCallback);	
	void getPurchaseOrderEnquiryWrapperByPage(String jobNumber, String orderNumber, String orderType, String supplierNumber, String decription, int pageNum, AsyncCallback<PurchaseOrderEnquiryWrapper> asyncCallback);
	
	//Account Master
	void getAccountMasterList(String jobNumber, AsyncCallback<List<AccountMaster>> callback);
	void createAccountMaster(String jobNumber, String objectAcc, String subsidiary, AsyncCallback<String> asyncCallback);
	void createAccountMasterByGroup(Boolean scResource, Boolean bqResourceSummary, Boolean scDetails, Boolean forecast, String jonNumber, AsyncCallback<String> asyncCallback);
	
	//Account Balance (Job Cost)
	void getAccountBalanceByDateRangeList(	String jobNumber, String subLedger, String subLedgerType, String totalFlag, String postFlag, Date fromDate, Date thruDate, String year, String period, AsyncCallback<AccountBalanceByDateRangePaginationWrapper> asyncCallback);
	void getAccountBalanceByDateRangePaginationWrapperByPage(Integer pageNum, AsyncCallback<AccountBalanceByDateRangePaginationWrapper> asyncCallback);
	void getAccountBalanceByDateRangePaginationWrapperByFilter(String objectCode, String subsidiaryCode, boolean filterZero, AsyncCallback<AccountBalanceByDateRangePaginationWrapper> asyncCallback);
	
	//Account Ledger
	void getAccountLedger(String accountId, String postedCode, String ledgerType, Date glDate1, Date glDate2, String subledgerType, String subledger, AsyncCallback<List<AccountLedgerWrapper>> callback);
	void getAccountLedgerByAccountCodeList(String accountCode, String postFlag, String ledgerType, Date fromDate, Date thruDate, String subLedgerType, String subLedger, AsyncCallback<AccountLedgerPaginationWrapper> asyncCallback);
	void getAccountLedgerPaginationWrapperByPage(Integer pageNum, AsyncCallback<AccountLedgerPaginationWrapper> asyncCallback);
}
