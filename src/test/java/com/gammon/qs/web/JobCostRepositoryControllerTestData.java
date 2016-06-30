/**
 * 
 */
package com.gammon.qs.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import com.gammon.junit.testcase.ControllerTestCase;

/**
 * @author paulnpyiu
 *
 */
@Configuration
@PropertySource("file:${JobCostRepositoryControllerTestData.properties}")
public class JobCostRepositoryControllerTestData extends ControllerTestCase.TestDataBase {
	private Class<?> serviceClass;// = JobCostRepositoryController.class;
	
	@Value("${JobCostRepositoryControllerTestData.testObtainAPRecordPaginationWrapper_jobNumber}")
	String testObtainAPRecordPaginationWrapper_jobNumber;
	@Value("${JobCostRepositoryControllerTestData.testObtainAPRecordPaginationWrapper_invoiceNumber}")
	String testObtainAPRecordPaginationWrapper_invoiceNumber;
	@Value("${JobCostRepositoryControllerTestData.testObtainAPRecordPaginationWrapper_supplierNumber}")
	String testObtainAPRecordPaginationWrapper_supplierNumber;
	@Value("${JobCostRepositoryControllerTestData.testObtainAPRecordPaginationWrapper_documentNumber}")
	String testObtainAPRecordPaginationWrapper_documentNumber;
	@Value("${JobCostRepositoryControllerTestData.testObtainAPRecordPaginationWrapper_documentType}")
	String testObtainAPRecordPaginationWrapper_documentType;
	@Value("${JobCostRepositoryControllerTestData.testObtainAPRecordPaginationWrapper_subledger}")
	String testObtainAPRecordPaginationWrapper_subledger;
	@Value("${JobCostRepositoryControllerTestData.testObtainAPRecordPaginationWrapper_subledgerType}")
	String testObtainAPRecordPaginationWrapper_subledgerType;
	@Value("${JobCostRepositoryControllerTestData.testObtainAPRecordPaginationWrapper_sql}")
	String testObtainAPRecordPaginationWrapper_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.JobCostRepositoryController#obtainAPRecordPaginationWrapper(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public Map<String, Object> testObtainAPRecordPaginationWrapper() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {testObtainAPRecordPaginationWrapper_jobNumber, testObtainAPRecordPaginationWrapper_invoiceNumber, testObtainAPRecordPaginationWrapper_supplierNumber, testObtainAPRecordPaginationWrapper_documentNumber, testObtainAPRecordPaginationWrapper_documentType, testObtainAPRecordPaginationWrapper_subledger, testObtainAPRecordPaginationWrapper_subledgerType});
		data.put("sql", testObtainAPRecordPaginationWrapper_sql);
		post();
		return data;
	}

	@Value("${JobCostRepositoryControllerTestData.testGetAPRecordPaginationWrapperByPage_pageNum}")
	String testGetAPRecordPaginationWrapperByPage_pageNum;
	@Value("${JobCostRepositoryControllerTestData.testGetAPRecordPaginationWrapperByPage_sql}")
	String testGetAPRecordPaginationWrapperByPage_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.JobCostRepositoryController#getAPRecordPaginationWrapperByPage(java.lang.Integer)}.
	 */
	public void initGetAPRecordPaginationWrapperByPage(){
//		JobCostRepositoryController jobCostRepositoryController = applicationContext.getBean(JobCostRepositoryController.class);
		try {
//			jobCostRepositoryController.obtainAPRecordPaginationWrapper(testObtainAPRecordPaginationWrapper_jobNumber, testObtainAPRecordPaginationWrapper_invoiceNumber, testObtainAPRecordPaginationWrapper_supplierNumber, testObtainAPRecordPaginationWrapper_documentNumber, testObtainAPRecordPaginationWrapper_documentType, testObtainAPRecordPaginationWrapper_subledger, testObtainAPRecordPaginationWrapper_subledgerType);
		} catch (Exception e) {e.printStackTrace();
		}
	}
	public Map<String, Object> testGetAPRecordPaginationWrapperByPage() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {new Integer(testGetAPRecordPaginationWrapperByPage_pageNum)});
		data.put("initMethod", "initGetAPRecordPaginationWrapperByPage");
		data.put("sql", testGetAPRecordPaginationWrapperByPage_sql);
		post();
		return data;
	}

	@Value("${JobCostRepositoryControllerTestData.testGetARRecordList_jobNumber}")
	String testGetARRecordList_jobNumber;
	@Value("${JobCostRepositoryControllerTestData.testGetARRecordList_reference}")
	String testGetARRecordList_reference;
	@Value("${JobCostRepositoryControllerTestData.testGetARRecordList_customerNumber}")
	String testGetARRecordList_customerNumber;
	@Value("${JobCostRepositoryControllerTestData.testGetARRecordList_documentNumber}")
	String testGetARRecordList_documentNumber;
	@Value("${JobCostRepositoryControllerTestData.testGetARRecordList_documentType}")
	String testGetARRecordList_documentType;
	@Value("${JobCostRepositoryControllerTestData.testGetARRecordList_sql}")
	String testGetARRecordList_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.JobCostRepositoryController#getARRecordList(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public Map<String, Object> testGetARRecordList() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {testGetARRecordList_jobNumber, testGetARRecordList_reference, testGetARRecordList_customerNumber, testGetARRecordList_documentNumber, testGetARRecordList_documentType});
		data.put("sql", testGetARRecordList_sql);
		post();
		return data;
	}

	@Value("${JobCostRepositoryControllerTestData.testGetARRecordListByPage_jobNumber}")
	String testGetARRecordListByPage_jobNumber;
	@Value("${JobCostRepositoryControllerTestData.testGetARRecordListByPage_reference}")
	String testGetARRecordListByPage_reference;
	@Value("${JobCostRepositoryControllerTestData.testGetARRecordListByPage_customerNumber}")
	String testGetARRecordListByPage_customerNumber;
	@Value("${JobCostRepositoryControllerTestData.testGetARRecordListByPage_documentNumber}")
	String testGetARRecordListByPage_documentNumber;
	@Value("${JobCostRepositoryControllerTestData.testGetARRecordListByPage_documentType}")
	String testGetARRecordListByPage_documentType;
	@Value("${JobCostRepositoryControllerTestData.testGetARRecordListByPage_pageNum}")
	String testGetARRecordListByPage_pageNum;
	@Value("${JobCostRepositoryControllerTestData.testGetARRecordListByPage_recordsPerPage}")
	String testGetARRecordListByPage_recordsPerPage;
	@Value("${JobCostRepositoryControllerTestData.testGetARRecordListByPage_sql}")
	String testGetARRecordListByPage_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.JobCostRepositoryController#getARRecordListByPage(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int)}.
	 */
	public Map<String, Object> testGetARRecordListByPage() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {testGetARRecordListByPage_jobNumber, testGetARRecordListByPage_reference, testGetARRecordListByPage_customerNumber, testGetARRecordListByPage_documentNumber, testGetARRecordListByPage_documentType, new Integer(testGetARRecordListByPage_pageNum), new Integer(testGetARRecordListByPage_recordsPerPage)});
		data.put("clazz", new Class<?>[]{String.class, String.class, String.class, String.class, String.class, int.class, int.class});
		data.put("sql", testGetARRecordListByPage_sql);
		post();
		return data;
	}

	@Value("${JobCostRepositoryControllerTestData.testGetPORecordList_jobNumber}")
	String testGetPORecordList_jobNumber;
	@Value("${JobCostRepositoryControllerTestData.testGetPORecordList_orderNumber}")
	String testGetPORecordList_orderNumber;
	@Value("${JobCostRepositoryControllerTestData.testGetPORecordList_orderType}")
	String testGetPORecordList_orderType;
	@Value("${JobCostRepositoryControllerTestData.testGetPORecordList_supplierNumber}")
	String testGetPORecordList_supplierNumber;
	@Value("${JobCostRepositoryControllerTestData.testGetPORecordList_sql}")
	String testGetPORecordList_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.JobCostRepositoryController#getPORecordList(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public Map<String, Object> testGetPORecordList() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {testGetPORecordList_jobNumber, testGetPORecordList_orderNumber, testGetPORecordList_orderType, testGetPORecordList_supplierNumber});
		data.put("sql", testGetPORecordList_sql);
		post();
		return data;
	}

	@Value("${JobCostRepositoryControllerTestData.testGetPurchaseOrderEnquiryWrapperByPageInteger_pageNum}")
	Integer testGetPurchaseOrderEnquiryWrapperByPageInteger_pageNum;
	@Value("${JobCostRepositoryControllerTestData.testGetPurchaseOrderEnquiryWrapperByPageInteger_sql}")
	String testGetPurchaseOrderEnquiryWrapperByPageInteger_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.JobCostRepositoryController#getPurchaseOrderEnquiryWrapperByPage(java.lang.Integer)}.
	 */
	public void initGetPurchaseOrderEnquiryWrapperByPageInteger(){
//		JobCostRepositoryController jobCostRepositoryController = applicationContext.getBean(JobCostRepositoryController.class);
//		jobCostRepositoryController.getPurchaseOrderEnquiryWrapperByPage(testGetPurchaseOrderEnquiryWrapperByPageStringStringStringStringStringInt_jobNumber, testGetPurchaseOrderEnquiryWrapperByPageStringStringStringStringStringInt_orderNumber, testGetPurchaseOrderEnquiryWrapperByPageStringStringStringStringStringInt_orderType, testGetPurchaseOrderEnquiryWrapperByPageStringStringStringStringStringInt_supplierNumber, testGetPurchaseOrderEnquiryWrapperByPageStringStringStringStringStringInt_description, new Integer(testGetPurchaseOrderEnquiryWrapperByPageStringStringStringStringStringInt_pageNum));
	}
	public Map<String, Object> testGetPurchaseOrderEnquiryWrapperByPageInteger() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", "getPurchaseOrderEnquiryWrapperByPage");
		data.put("params", new Object[] {new Integer(testGetPurchaseOrderEnquiryWrapperByPageInteger_pageNum)});
		data.put("initMethod", "initGetPurchaseOrderEnquiryWrapperByPageInteger");
		data.put("sql", testGetPurchaseOrderEnquiryWrapperByPageInteger_sql);
		post();
		return data;
	}

	@Value("${JobCostRepositoryControllerTestData.testGetPurchaseOrderEnquiryWrapperByPageStringStringStringStringStringInt_jobNumber}")
	String testGetPurchaseOrderEnquiryWrapperByPageStringStringStringStringStringInt_jobNumber;
	@Value("${JobCostRepositoryControllerTestData.testGetPurchaseOrderEnquiryWrapperByPageStringStringStringStringStringInt_orderNumber}")
	String testGetPurchaseOrderEnquiryWrapperByPageStringStringStringStringStringInt_orderNumber;
	@Value("${JobCostRepositoryControllerTestData.testGetPurchaseOrderEnquiryWrapperByPageStringStringStringStringStringInt_orderType}")
	String testGetPurchaseOrderEnquiryWrapperByPageStringStringStringStringStringInt_orderType;
	@Value("${JobCostRepositoryControllerTestData.testGetPurchaseOrderEnquiryWrapperByPageStringStringStringStringStringInt_supplierNumber}")
	String testGetPurchaseOrderEnquiryWrapperByPageStringStringStringStringStringInt_supplierNumber;
	@Value("${JobCostRepositoryControllerTestData.testGetPurchaseOrderEnquiryWrapperByPageStringStringStringStringStringInt_description}")
	String testGetPurchaseOrderEnquiryWrapperByPageStringStringStringStringStringInt_description;
	@Value("${JobCostRepositoryControllerTestData.testGetPurchaseOrderEnquiryWrapperByPageStringStringStringStringStringInt_pageNum}")
	String testGetPurchaseOrderEnquiryWrapperByPageStringStringStringStringStringInt_pageNum;
	@Value("${JobCostRepositoryControllerTestData.testGetPurchaseOrderEnquiryWrapperByPageStringStringStringStringStringInt_sql}")
	String testGetPurchaseOrderEnquiryWrapperByPageStringStringStringStringStringInt_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.JobCostRepositoryController#getPurchaseOrderEnquiryWrapperByPage(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int)}.
	 */
	public Map<String, Object> testGetPurchaseOrderEnquiryWrapperByPageStringStringStringStringStringInt() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", "getPurchaseOrderEnquiryWrapperByPage");
		data.put("params", new Object[] {testGetPurchaseOrderEnquiryWrapperByPageStringStringStringStringStringInt_jobNumber, testGetPurchaseOrderEnquiryWrapperByPageStringStringStringStringStringInt_orderNumber, testGetPurchaseOrderEnquiryWrapperByPageStringStringStringStringStringInt_orderType, testGetPurchaseOrderEnquiryWrapperByPageStringStringStringStringStringInt_supplierNumber, testGetPurchaseOrderEnquiryWrapperByPageStringStringStringStringStringInt_description, new Integer(testGetPurchaseOrderEnquiryWrapperByPageStringStringStringStringStringInt_pageNum)});
		data.put("clazz", new Class<?>[]{String.class, String.class, String.class, String.class, String.class, int.class});
		data.put("sql", testGetPurchaseOrderEnquiryWrapperByPageStringStringStringStringStringInt_sql);
		post();
		return data;
	}

	@Value("${JobCostRepositoryControllerTestData.testGetAccountBalanceByDateRangeList_jobNumber}")
	String testGetAccountBalanceByDateRangeList_jobNumber;
	@Value("${JobCostRepositoryControllerTestData.testGetAccountBalanceByDateRangeList_subLedger}")
	String testGetAccountBalanceByDateRangeList_subLedger;
	@Value("${JobCostRepositoryControllerTestData.testGetAccountBalanceByDateRangeList_subLedgerType}")
	String testGetAccountBalanceByDateRangeList_subLedgerType;
	@Value("${JobCostRepositoryControllerTestData.testGetAccountBalanceByDateRangeList_totalFlag}")
	String testGetAccountBalanceByDateRangeList_totalFlag;
	@Value("${JobCostRepositoryControllerTestData.testGetAccountBalanceByDateRangeList_postFlag}")
	String testGetAccountBalanceByDateRangeList_postFlag;
	@Value("${JobCostRepositoryControllerTestData.testGetAccountBalanceByDateRangeList_fromDate}")
	String testGetAccountBalanceByDateRangeList_fromDate;
	@Value("${JobCostRepositoryControllerTestData.testGetAccountBalanceByDateRangeList_thruDate}")
	String testGetAccountBalanceByDateRangeList_thruDate;
	@Value("${JobCostRepositoryControllerTestData.testGetAccountBalanceByDateRangeList_year}")
	String testGetAccountBalanceByDateRangeList_year;
	@Value("${JobCostRepositoryControllerTestData.testGetAccountBalanceByDateRangeList_period}")
	String testGetAccountBalanceByDateRangeList_period;
	@Value("${JobCostRepositoryControllerTestData.testGetAccountBalanceByDateRangeList_sql}")
	String testGetAccountBalanceByDateRangeList_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.JobCostRepositoryController#getAccountBalanceByDateRangeList(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Date, java.util.Date, java.lang.String, java.lang.String)}.
	 */
	public Map<String, Object> testGetAccountBalanceByDateRangeList() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		Date fromDate = null;
		Date thruDate = null;
		try {
			fromDate = new SimpleDateFormat("yyyy/MM/dd").parse(testGetAccountBalanceByDateRangeList_fromDate);
			thruDate = new SimpleDateFormat("yyyy/MM/dd").parse(testGetAccountBalanceByDateRangeList_thruDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {testGetAccountBalanceByDateRangeList_jobNumber, testGetAccountBalanceByDateRangeList_subLedger, testGetAccountBalanceByDateRangeList_subLedgerType, testGetAccountBalanceByDateRangeList_postFlag, testGetAccountBalanceByDateRangeList_postFlag, fromDate, thruDate, testGetAccountBalanceByDateRangeList_year, testGetAccountBalanceByDateRangeList_period});
		data.put("sql", testGetAccountBalanceByDateRangeList_sql);
		post();
		return data;
	}

	@Value("${JobCostRepositoryControllerTestData.testGetAccountBalanceByDateRangePaginationWrapperByPage_pageNum}")
	String testGetAccountBalanceByDateRangePaginationWrapperByPage_pageNum;
	@Value("${JobCostRepositoryControllerTestData.testGetAccountBalanceByDateRangePaginationWrapperByPage_sql}")
	String testGetAccountBalanceByDateRangePaginationWrapperByPage_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.JobCostRepositoryController#getAccountBalanceByDateRangePaginationWrapperByPage(java.lang.Integer)}.
	 */
	public void initGetAccountBalanceByDateRangePaginationWrapperByPage(){
//		JobCostRepositoryController jobCostRepositoryController = applicationContext.getBean(JobCostRepositoryController.class);
		try {
//			jobCostRepositoryController.getAccountBalanceByDateRangeList(testGetAccountBalanceByDateRangeList_jobNumber, testGetAccountBalanceByDateRangeList_subLedger, testGetAccountBalanceByDateRangeList_subLedgerType, testGetAccountBalanceByDateRangeList_postFlag, testGetAccountBalanceByDateRangeList_postFlag, new Date(testGetAccountBalanceByDateRangeList_fromDate), new Date(testGetAccountBalanceByDateRangeList_thruDate), testGetAccountBalanceByDateRangeList_year, testGetAccountBalanceByDateRangeList_period);
		} catch (Exception e) {e.printStackTrace();
		}
	}
	public Map<String, Object> testGetAccountBalanceByDateRangePaginationWrapperByPage() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {new Integer(testGetAccountBalanceByDateRangePaginationWrapperByPage_pageNum)});
		data.put("initMethod", "initGetAccountBalanceByDateRangePaginationWrapperByPage");
		data.put("sql", testGetAccountBalanceByDateRangePaginationWrapperByPage_sql);
		post();
		return data;
	}

	@Value("${JobCostRepositoryControllerTestData.testGetAccountBalanceByDateRangePaginationWrapperByFilter_objectCode}")
	String testGetAccountBalanceByDateRangePaginationWrapperByFilter_objectCode;
	@Value("${JobCostRepositoryControllerTestData.testGetAccountBalanceByDateRangePaginationWrapperByFilter_subsidiaryCode}")
	String testGetAccountBalanceByDateRangePaginationWrapperByFilter_subsidiaryCode;
	@Value("${JobCostRepositoryControllerTestData.testGetAccountBalanceByDateRangePaginationWrapperByFilter_filterZero}")
	String testGetAccountBalanceByDateRangePaginationWrapperByFilter_filterZero;
	@Value("${JobCostRepositoryControllerTestData.testGetAccountBalanceByDateRangePaginationWrapperByFilter_sql}")
	String testGetAccountBalanceByDateRangePaginationWrapperByFilter_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.JobCostRepositoryController#getAccountBalanceByDateRangePaginationWrapperByFilter(java.lang.String, java.lang.String, boolean)}.
	 */
	public Map<String, Object> testGetAccountBalanceByDateRangePaginationWrapperByFilter() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {testGetAccountBalanceByDateRangePaginationWrapperByFilter_objectCode, testGetAccountBalanceByDateRangePaginationWrapperByFilter_subsidiaryCode, new Boolean(testGetAccountBalanceByDateRangePaginationWrapperByFilter_filterZero)});
		data.put("clazz", new Class<?>[]{String.class, String.class, boolean.class});
		data.put("sql", testGetAccountBalanceByDateRangePaginationWrapperByFilter_sql);
		post();
		return data;
	}

	@Value("${JobCostRepositoryControllerTestData.testGetAccountLedger_accountId}")
	String testGetAccountLedger_accountId;
	@Value("${JobCostRepositoryControllerTestData.testGetAccountLedger_postedCode}")
	String testGetAccountLedger_postedCode;
	@Value("${JobCostRepositoryControllerTestData.testGetAccountLedger_ledgerType}")
	String testGetAccountLedger_ledgerType;
	@Value("${JobCostRepositoryControllerTestData.testGetAccountLedger_glDate1}")
	String testGetAccountLedger_glDate1;
	@Value("${JobCostRepositoryControllerTestData.testGetAccountLedger_glDate2}")
	String testGetAccountLedger_glDate2;
	@Value("${JobCostRepositoryControllerTestData.testGetAccountLedger_subledgerType}")
	String testGetAccountLedger_subledgerType;
	@Value("${JobCostRepositoryControllerTestData.testGetAccountLedger_subledger}")
	String testGetAccountLedger_subledger;
	@Value("${JobCostRepositoryControllerTestData.testGetAccountLedger_sql}")
	String testGetAccountLedger_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.JobCostRepositoryController#getAccountLedger(java.lang.String, java.lang.String, java.lang.String, java.util.Date, java.util.Date, java.lang.String, java.lang.String)}.
	 */
	public Map<String, Object> testGetAccountLedger() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		Date glDate1 = null;
		Date glDate2 = null;
		try {
			glDate1 = new SimpleDateFormat("yyyy/MM/dd").parse(testGetAccountLedger_glDate1);
			glDate2 = new SimpleDateFormat("yyyy/MM/dd").parse(testGetAccountLedger_glDate2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {testGetAccountLedger_accountId, testGetAccountLedger_postedCode, testGetAccountLedger_ledgerType, glDate1, glDate2, testGetAccountLedger_subledgerType, testGetAccountLedger_subledger});
		data.put("sql", testGetAccountLedger_sql);
		post();
		return data;
	}

	@Value("${JobCostRepositoryControllerTestData.testGetAccountLedgerByAccountCodeList_accountCode}")
	String testGetAccountLedgerByAccountCodeList_accountCode;
	@Value("${JobCostRepositoryControllerTestData.testGetAccountLedgerByAccountCodeList_postFlag}")
	String testGetAccountLedgerByAccountCodeList_postFlag;
	@Value("${JobCostRepositoryControllerTestData.testGetAccountLedgerByAccountCodeList_ledgerType}")
	String testGetAccountLedgerByAccountCodeList_ledgerType;
	@Value("${JobCostRepositoryControllerTestData.testGetAccountLedgerByAccountCodeList_fromDate}")
	String testGetAccountLedgerByAccountCodeList_fromDate;
	@Value("${JobCostRepositoryControllerTestData.testGetAccountLedgerByAccountCodeList_thruDate}")
	String testGetAccountLedgerByAccountCodeList_thruDate;
	@Value("${JobCostRepositoryControllerTestData.testGetAccountLedgerByAccountCodeList_subLedgerType}")
	String testGetAccountLedgerByAccountCodeList_subLedgerType;
	@Value("${JobCostRepositoryControllerTestData.testGetAccountLedgerByAccountCodeList_subLedger}")
	String subLedtestGetAccountLedgerByAccountCodeList_subLedgerger;
	@Value("${JobCostRepositoryControllerTestData.testGetAccountLedgerByAccountCodeList_sql}")
	String testGetAccountLedgerByAccountCodeList_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.JobCostRepositoryController#getAccountLedgerByAccountCodeList(java.lang.String, java.lang.String, java.lang.String, java.util.Date, java.util.Date, java.lang.String, java.lang.String)}.
	 */
	public Map<String, Object> testGetAccountLedgerByAccountCodeList() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		Date fromDate = null;
		Date thruDate = null;
		try {
			fromDate = new SimpleDateFormat("yyyy/MM/dd").parse(testGetAccountLedgerByAccountCodeList_fromDate);
			thruDate = new SimpleDateFormat("yyyy/MM/dd").parse(testGetAccountLedgerByAccountCodeList_thruDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {testGetAccountLedgerByAccountCodeList_accountCode, testGetAccountLedgerByAccountCodeList_postFlag, testGetAccountLedgerByAccountCodeList_ledgerType, fromDate, thruDate, testGetAccountLedgerByAccountCodeList_subLedgerType, subLedtestGetAccountLedgerByAccountCodeList_subLedgerger});
		data.put("sql", testGetAccountLedgerByAccountCodeList_sql);
		post();
		return data;
	}

	@Value("${JobCostRepositoryControllerTestData.testGetAccountLedgerPaginationWrapperByPage_pageNum}")
	Integer testGetAccountLedgerPaginationWrapperByPage_pageNum;
	@Value("${JobCostRepositoryControllerTestData.testGetAccountLedgerPaginationWrapperByPage_sql}")
	String testGetAccountLedgerPaginationWrapperByPage_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.JobCostRepositoryController#getAccountLedgerPaginationWrapperByPage(java.lang.Integer)}.
	 */
	public void initGetAccountLedgerPaginationWrapperByPage(){
//		JobCostRepositoryController jobCostRepositoryController = applicationContext.getBean(JobCostRepositoryController.class);
		try {
//			jobCostRepositoryController.getAccountLedgerByAccountCodeList(testGetAccountLedgerByAccountCodeList_accountCode, testGetAccountLedgerByAccountCodeList_postFlag, testGetAccountLedgerByAccountCodeList_ledgerType, new Date(testGetAccountLedgerByAccountCodeList_fromDate), new Date(testGetAccountLedgerByAccountCodeList_thruDate), testGetAccountLedgerByAccountCodeList_subLedgerType, subLedtestGetAccountLedgerByAccountCodeList_subLedgerger);
		} catch (Exception e) {e.printStackTrace();
		}
	}
	public Map<String, Object> testGetAccountLedgerPaginationWrapperByPage() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {testGetAccountLedgerPaginationWrapperByPage_pageNum});
		data.put("initMethod", "initGetAccountLedgerPaginationWrapperByPage");
		data.put("sql", testGetAccountLedgerPaginationWrapperByPage_sql);
		post();
		return data;
	}

	@Value("${JobCostRepositoryControllerTestData.testGetAccountMasterList_jobNumber}")
	String testGetAccountMasterList_jobNumber;
	@Value("${JobCostRepositoryControllerTestData.testGetAccountMasterList_sql}")
	String testGetAccountMasterList_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.JobCostRepositoryController#getAccountMasterList(java.lang.String)}.
	 */
	public Map<String, Object> testGetAccountMasterList() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {testGetAccountMasterList_jobNumber});
		data.put("sql", testGetAccountMasterList_sql);
		post();
		return data;
	}

	@Value("${JobCostRepositoryControllerTestData.testCreateAccountMaster_jobNumber}")
	String testCreateAccountMaster_jobNumber;
	@Value("${JobCostRepositoryControllerTestData.testCreateAccountMaster_objectCode}")
	String testCreateAccountMaster_objectCode;
	@Value("${JobCostRepositoryControllerTestData.testCreateAccountMaster_subsidiaryCode}")
	String testCreateAccountMaster_subsidiaryCode;
	@Value("${JobCostRepositoryControllerTestData.testCreateAccountMaster_sql}")
	String testCreateAccountMaster_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.JobCostRepositoryController#createAccountMaster(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public Map<String, Object> testCreateAccountMaster() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {testCreateAccountMaster_jobNumber, testCreateAccountMaster_objectCode, testCreateAccountMaster_subsidiaryCode});
		data.put("sql", testCreateAccountMaster_sql);
		post();
		return data;
	}

	@Value("${JobCostRepositoryControllerTestData.testCreateAccountMasterByGroup_scResource}")
	String testCreateAccountMasterByGroup_scResource;
	@Value("${JobCostRepositoryControllerTestData.testCreateAccountMasterByGroup_bqResourceSummary}")
	String testCreateAccountMasterByGroup_bqResourceSummary;
	@Value("${JobCostRepositoryControllerTestData.testCreateAccountMasterByGroup_scDetails}")
	String testCreateAccountMasterByGroup_scDetails;
	@Value("${JobCostRepositoryControllerTestData.testCreateAccountMasterByGroup_forecast}")
	String testCreateAccountMasterByGroup_forecast;
	@Value("${JobCostRepositoryControllerTestData.testCreateAccountMasterByGroup_jobNumber}")
	String testCreateAccountMasterByGroup_jobNumber;
	@Value("${JobCostRepositoryControllerTestData.testCreateAccountMasterByGroup_sql}")
	String testCreateAccountMasterByGroup_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.JobCostRepositoryController#createAccountMasterByGroup(java.lang.Boolean, java.lang.Boolean, java.lang.Boolean, java.lang.Boolean, java.lang.String)}.
	 */
	public Map<String, Object> testCreateAccountMasterByGroup() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {new Boolean(testCreateAccountMasterByGroup_scResource), new Boolean(testCreateAccountMasterByGroup_bqResourceSummary), new Boolean(testCreateAccountMasterByGroup_scDetails), new Boolean(testCreateAccountMasterByGroup_forecast), testCreateAccountMasterByGroup_jobNumber});
		data.put("sql", testCreateAccountMasterByGroup_sql);
		post();
		return data;
	}

	@Value("${JobCostRepositoryControllerTestData.testGetAPPaymentHistories_company}")
	String testGetAPPaymentHistories_company;
	@Value("${JobCostRepositoryControllerTestData.testGetAPPaymentHistories_documentType}")
	String testGetAPPaymentHistories_documentType;
	@Value("${JobCostRepositoryControllerTestData.testGetAPPaymentHistories_supplierNumber}")
	String testGetAPPaymentHistories_supplierNumber;
	@Value("${JobCostRepositoryControllerTestData.testGetAPPaymentHistories_documentNumber}")
	String testGetAPPaymentHistories_documentNumber;
	@Value("${JobCostRepositoryControllerTestData.testGetAPPaymentHistories_sql}")
	String testGetAPPaymentHistories_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.JobCostRepositoryController#getAPPaymentHistories(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)}.
	 */
	public Map<String, Object> testGetAPPaymentHistories() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {testGetAPPaymentHistories_company, testGetAPPaymentHistories_documentType, new Integer(testGetAPPaymentHistories_supplierNumber), new Integer(testGetAPPaymentHistories_documentNumber)});
		data.put("sql", testGetAPPaymentHistories_sql);
		post();
		return data;
	}

}
