package com.gammon.qs.web;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.client.repository.JobCostRepositoryRemote;
import com.gammon.qs.domain.ARRecord;
import com.gammon.qs.domain.AccountLedgerWrapper;
import com.gammon.qs.domain.AccountMaster;
import com.gammon.qs.domain.PORecord;
import com.gammon.qs.service.JobCostService;
import com.gammon.qs.service.MasterListService;
import com.gammon.qs.wrapper.APRecordPaginationWrapper;
import com.gammon.qs.wrapper.AccountBalanceByDateRangePaginationWrapper;
import com.gammon.qs.wrapper.AccountLedgerPaginationWrapper;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.PaymentHistoriesWrapper;
import com.gammon.qs.wrapper.PurchaseOrderEnquiryWrapper;

import net.sf.gilead.core.PersistentBeanManager;

@Service
public class JobCostRepositoryController extends GWTSpringController implements JobCostRepositoryRemote {

	private static final long serialVersionUID = -488564409907298698L;
	@Autowired
	private transient JobCostService jobCostRepository;
	@Autowired
	private MasterListService masterListRepository;
	@Override
	@Autowired
	public void setBeanManager(PersistentBeanManager manager) {
		super.setBeanManager(manager);
	}

	//AP (Supplier Ledger)
	public APRecordPaginationWrapper obtainAPRecordPaginationWrapper(String jobNumber, String invoiceNumber, String supplierNumber, String documentNumber, String documentType, String subledger, String subledgerType) throws Exception {
		APRecordPaginationWrapper apRecordPaginationWrapper = jobCostRepository.obtainAPRecordPaginationWrapper(jobNumber, invoiceNumber, supplierNumber, documentNumber, documentType, subledger, subledgerType);
		return apRecordPaginationWrapper;
	}
	
	public APRecordPaginationWrapper getAPRecordPaginationWrapperByPage(Integer pageNum) throws Exception {
		APRecordPaginationWrapper apRecordPaginationWrapper = jobCostRepository.getAPRecordPaginationWrapperByPage(pageNum);
		return apRecordPaginationWrapper;
	}
	
	//AR (Customer Ledger)
	public List<ARRecord> getARRecordList(String jobNumber, String reference, String customerNumber, String documentNumber, String documentType) throws Exception {
		List<ARRecord> arRecordList = jobCostRepository.getARRecordList(jobNumber, reference, customerNumber, documentNumber, documentType);
		return arRecordList;
	}
	
	public PaginationWrapper<ARRecord> getARRecordListByPage(String jobNumber, String reference, String customerNumber, String documentNumber, String documentType, int pageNum, int recordsPerPage) throws Exception {
		return jobCostRepository.getARRecordListByPage(jobNumber, reference, customerNumber, documentNumber, documentType, pageNum, recordsPerPage);
	}
	
	// Last Modified : Brian Tse @ 20101008 1715
	public List<PORecord> getPORecordList(String jobNumber, String orderNumber, String orderType, String supplierNumber) throws Exception {
		return jobCostRepository.getPORecordList(jobNumber, orderNumber, orderType, supplierNumber);
	}
	
	public PurchaseOrderEnquiryWrapper getPurchaseOrderEnquiryWrapperByPage(Integer pageNum) throws Exception{
		PurchaseOrderEnquiryWrapper purchaseOrderEnquiryWrapper = jobCostRepository.getPurchaseOrderEnquiryWrapperByPage(pageNum);
		return purchaseOrderEnquiryWrapper;
		
	}
	
	public PurchaseOrderEnquiryWrapper getPurchaseOrderEnquiryWrapperByPage(String jobNumber, String orderNumber, String orderType, String supplierNumber, String description, int pageNum){
		PurchaseOrderEnquiryWrapper purchaseOrderEnquiryWrapper = jobCostRepository.getPurchaseOrderEnquiryWrapperByPage(jobNumber, orderNumber, orderType, supplierNumber, description, pageNum);
		return purchaseOrderEnquiryWrapper;
	}
	
	public AccountBalanceByDateRangePaginationWrapper getAccountBalanceByDateRangeList(
			String jobNumber, String subLedger, String subLedgerType, String totalFlag, String postFlag, Date fromDate, Date thruDate, String year, String period) throws Exception {
		AccountBalanceByDateRangePaginationWrapper accountBalancePaginationWrapper = jobCostRepository.getAccountBalanceByDateRangeList(jobNumber, subLedger, subLedgerType, totalFlag, postFlag, fromDate, thruDate, year, period);
		return accountBalancePaginationWrapper;
	}
	
	public AccountBalanceByDateRangePaginationWrapper getAccountBalanceByDateRangePaginationWrapperByPage(Integer pageNum) throws Exception {
		AccountBalanceByDateRangePaginationWrapper accountBalancePaginationWrapper = jobCostRepository.getAccountBalanceByDateRangePaginationWrapperByPage(pageNum);
		return accountBalancePaginationWrapper;
	}
	
	public AccountBalanceByDateRangePaginationWrapper getAccountBalanceByDateRangePaginationWrapperByFilter(String objectCode, String subsidiaryCode, boolean filterZero)throws Exception {
		AccountBalanceByDateRangePaginationWrapper accountBalancePaginationWrapper = jobCostRepository.getAccountBalanceByDateRangePaginationWrapperByFilter(objectCode, subsidiaryCode, filterZero);
		return accountBalancePaginationWrapper;
	}

	//Account Ledger
	public List<AccountLedgerWrapper> getAccountLedger(String accountId, String postedCode, String ledgerType, Date glDate1, Date glDate2, String subledgerType, String subledger) throws Exception {
		return this.jobCostRepository.getAccountLedger(accountId, postedCode, ledgerType, glDate1, glDate2, subledgerType, subledger);
	}
	
	public AccountLedgerPaginationWrapper getAccountLedgerByAccountCodeList(String accountCode, String postFlag, String ledgerType, Date fromDate, Date thruDate, String subLedgerType, String subLedger) throws Exception {
		AccountLedgerPaginationWrapper accountLedgerPaginationWrapper = jobCostRepository.getAccountLedgerByAccountCodeList(accountCode, postFlag, ledgerType, fromDate, thruDate, subLedgerType, subLedger);
		return accountLedgerPaginationWrapper;
	}
	
	public AccountLedgerPaginationWrapper getAccountLedgerPaginationWrapperByPage(Integer pageNum) throws Exception {
		AccountLedgerPaginationWrapper accountLedgerPaginationWrapper = jobCostRepository.getAccountLedgerPaginationWrapperByPage(pageNum);
		return accountLedgerPaginationWrapper;
	}
	
	
	//Account Master
	public List<AccountMaster> getAccountMasterList(String jobNumber) throws Exception{		
		return this.jobCostRepository.getAccountMasterList(jobNumber);
	}
	
	public String createAccountMaster(String jobNumber, String objectCode,
			String subsidiaryCode) {
		try {
			return masterListRepository.validateAndCreateAccountCode(jobNumber, objectCode, subsidiaryCode);
		} catch (Exception e) {
			return e.getLocalizedMessage();
		}
	}

	public String createAccountMasterByGroup(Boolean scResource,
			Boolean bqResourceSummary, Boolean scDetails, Boolean forecast, String jobNumber) {
		return jobCostRepository.createAccountMasterByGroup(scResource,bqResourceSummary,scDetails,forecast,jobNumber);
	}

	public List<PaymentHistoriesWrapper> getAPPaymentHistories(String company, String documentType, Integer supplierNumber, Integer documentNumber) {
		return jobCostRepository.getAPPaymentHistories(company, documentType, supplierNumber, documentNumber);
	}
}
