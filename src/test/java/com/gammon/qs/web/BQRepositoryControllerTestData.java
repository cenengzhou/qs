/**
 * 
 */
package com.gammon.qs.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import com.gammon.junit.testcase.ControllerTestCase;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.BpiItem;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.BpiItemResource;
import com.gammon.qs.service.BpiItemService;
import com.gammon.qs.service.JobInfoService;
import com.gammon.qs.wrapper.updateIVByResource.ResourceWrapper;

/**
 * @author paulnpyiu
 *
 */
@Configuration
@PropertySource("file:${BQRepositoryControllerTestData.properties}")
public class BQRepositoryControllerTestData extends ControllerTestCase.TestDataBase {
	private Class<?> serviceClass;// = BQRepositoryController.class;
	
	@Value("${BQRepositoryControllerTestData.testGetBQItemListByBPI_jobNumber}")
	String testGetBQItemListByBPI_jobNumber;
	@Value("${BQRepositoryControllerTestData.testGetBQItemListByBPI_billNo}")
	String testGetBQItemListByBPI_billNo;
	@Value("${BQRepositoryControllerTestData.testGetBQItemListByBPI_subBillNo}")
	String testGetBQItemListByBPI_subBillNo;
	@Value("${BQRepositoryControllerTestData.testGetBQItemListByBPI_sessionNo}")
	String testGetBQItemListByBPI_sessionNo;
	@Value("${BQRepositoryControllerTestData.testGetBQItemListByBPI_pageNo}")
	String testGetBQItemListByBPI_pageNo;
	@Value("${BQRepositoryControllerTestData.testGetBQItemListByBPI_sql}")
	String testGetBQItemListByBPI_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQRepositoryController#getBQItemListByBPI(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public Map<String, Object> testGetBQItemListByBPI() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {testGetBQItemListByBPI_jobNumber, testGetBQItemListByBPI_billNo, testGetBQItemListByBPI_subBillNo, testGetBQItemListByBPI_sessionNo, testGetBQItemListByBPI_pageNo});
		data.put("sql", testGetBQItemListByBPI_sql);
		post();
		return data;
	}

	@Value("${BQRepositoryControllerTestData.testObtainBQItem_jobNumber}")
	String testObtainBQItem_jobNumber;
	@Value("${BQRepositoryControllerTestData.testObtainBQItem_billNo}")
	String testObtainBQItem_billNo;
	@Value("${BQRepositoryControllerTestData.testObtainBQItem_subbillNo}")
	String testObtainBQItem_subbillNo;
	@Value("${BQRepositoryControllerTestData.testObtainBQItem_pageNo}")
	String testObtainBQItem_pageNo;
	@Value("${BQRepositoryControllerTestData.testObtainBQItem_itemNo}")
	String testObtainBQItem_itemNo;
	@Value("${BQRepositoryControllerTestData.testObtainBQItem_bqDesc}")
	String testObtainBQItem_bqDesc;
	@Value("${BQRepositoryControllerTestData.testObtainBQItem_sql}")
	String testObtainBQItem_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQRepositoryController#obtainBpiItem(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public Map<String, Object> testObtainBQItem() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {testObtainBQItem_jobNumber, testObtainBQItem_billNo, testObtainBQItem_subbillNo, testObtainBQItem_pageNo, testObtainBQItem_itemNo, testObtainBQItem_bqDesc});
		data.put("sql", testObtainBQItem_sql);
		post();
		return data;
	}
	
	@Value("${BQRepositoryControllerTestData.testGetBillListWithPagesByJob_jobNumber}")
	String testGetBillListWithPagesByJob_jobNumber;
	@Value("${BQRepositoryControllerTestData.testGetBillListWithPagesByJob_sql}")
	String testGetBillListWithPagesByJob_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQRepositoryController#getBillListWithPagesByJob(com.gammon.qs.domain.JobInfo)}.
	 */
	public Map<String, Object> testGetBillListWithPagesByJob() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		JobInfoService jobService = applicationContext.getBean("jobService",JobInfoService.class);
		JobInfo job = null;
		try {
			job = jobService.obtainJob(testGetBillListWithPagesByJob_jobNumber);
		} catch (DatabaseOperationException e) {e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {job});
		data.put("sql", testGetBillListWithPagesByJob_sql);
		post();
		return data;
	}

	@Value("${BQRepositoryControllerTestData.testSearchBQItemsByPage_jobNumber}")
	String testSearchBQItemsByPage_jobNumber;
	@Value("${BQRepositoryControllerTestData.testSearchBQItemsByPage_billNo}")
	String testSearchBQItemsByPage_billNo;
	@Value("${BQRepositoryControllerTestData.testSearchBQItemsByPage_subBillNo}")
	String testSearchBQItemsByPage_subBillNo;
	@Value("${BQRepositoryControllerTestData.testSearchBQItemsByPage_pageNo}")
	String testSearchBQItemsByPage_pageNo;
	@Value("${BQRepositoryControllerTestData.testSearchBQItemsByPage_itemNo}")
	String testSearchBQItemsByPage_itemNo;
	@Value("${BQRepositoryControllerTestData.testSearchBQItemsByPage_description}")
	String testSearchBQItemsByPage_description;
	@Value("${BQRepositoryControllerTestData.testSearchBQItemsByPage_pageNum}")
	String testSearchBQItemsByPage_pageNum;
	@Value("${BQRepositoryControllerTestData.testSearchBQItemsByPage_sql}")
	String testSearchBQItemsByPage_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQRepositoryController#searchBQItemsByPage(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int)}.
	 */
	public Map<String, Object> testSearchBQItemsByPage() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {testSearchBQItemsByPage_jobNumber, testSearchBQItemsByPage_billNo, testSearchBQItemsByPage_subBillNo, testSearchBQItemsByPage_pageNo, testSearchBQItemsByPage_itemNo, testSearchBQItemsByPage_description, new Integer(testSearchBQItemsByPage_pageNum).intValue()});
		data.put("clazz", new Class<?>[]{String.class, String.class, String.class, String.class, String.class, String.class, int.class});
		data.put("sql", testSearchBQItemsByPage_sql);
		post();
		return data;
	}

	@Value("${BQRepositoryControllerTestData.testUpdateBQItemQuantities_jobNumber}")
	String testUpdateBQItemQuantities_jobNumber;
	@Value("${BQRepositoryControllerTestData.testUpdateBQItemQuantities_billNo}")
	String testUpdateBQItemQuantities_billNo;
	@Value("${BQRepositoryControllerTestData.testUpdateBQItemQuantities_subBillNo}")
	String testUpdateBQItemQuantities_subBillNo;
	@Value("${BQRepositoryControllerTestData.testUpdateBQItemQuantities_pageNo}")
	String testUpdateBQItemQuantities_pageNo;
	@Value("${BQRepositoryControllerTestData.testUpdateBQItemQuantities_itemNo}")
	String testUpdateBQItemQuantities_itemNo;
	@Value("${BQRepositoryControllerTestData.testUpdateBQItemQuantities_bqDesc}")
	String testUpdateBQItemQuantities_bqDesc;
	@Value("${BQRepositoryControllerTestData.testUpdateBQItemQuantities_sql}")
	String testUpdateBQItemQuantities_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQRepositoryController#updateBQItemQuantities(com.gammon.qs.domain.JobInfo, java.util.List)}.
	 */
	public Map<String, Object> testUpdateBQItemQuantities() {
		init();		
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		JobInfoService jobService = applicationContext.getBean("jobService",JobInfoService.class);
		BpiItemService bqService = applicationContext.getBean(BpiItemService.class);
		JobInfo job = null;
		List<BpiItem> bqItems= null;
		try {
			job = jobService.obtainJob(testGetBillListWithPagesByJob_jobNumber);
			bqItems = bqService.obtainBQItemList(testUpdateBQItemQuantities_jobNumber, testUpdateBQItemQuantities_billNo, testUpdateBQItemQuantities_subBillNo, testUpdateBQItemQuantities_pageNo, testUpdateBQItemQuantities_itemNo, testUpdateBQItemQuantities_bqDesc);
		} catch (Exception e) {e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {job, bqItems});
		data.put("clazz", new Class<?>[]{JobInfo.class,List.class});
		data.put("sql", testUpdateBQItemQuantities_sql);
		post();
		return data;
	}

	@Value("${BQRepositoryControllerTestData.testSearchResourcesByPage_jobNumber}")
	String testSearchResourcesByPage_jobNumber;
	@Value("${BQRepositoryControllerTestData.testSearchResourcesByPage_billNo}")
	String testSearchResourcesByPage_billNo;
	@Value("${BQRepositoryControllerTestData.testSearchResourcesByPage_subBillNo}")
	String testSearchResourcesByPage_subBillNo;
	@Value("${BQRepositoryControllerTestData.testSearchResourcesByPage_pageNo}")
	String testSearchResourcesByPage_pageNo;
	@Value("${BQRepositoryControllerTestData.testSearchResourcesByPage_itemNo}")
	String testSearchResourcesByPage_itemNo;
	@Value("${BQRepositoryControllerTestData.testSearchResourcesByPage_packageNo}")
	String testSearchResourcesByPage_packageNo;
	@Value("${BQRepositoryControllerTestData.testSearchResourcesByPage_objectCode}")
	String testSearchResourcesByPage_objectCode;
	@Value("${BQRepositoryControllerTestData.testSearchResourcesByPage_subsidiaryCode}")
	String testSearchResourcesByPage_subsidiaryCode;
	@Value("${BQRepositoryControllerTestData.testSearchResourcesByPage_description}")
	String testSearchResourcesByPage_description;
	@Value("${BQRepositoryControllerTestData.testSearchResourcesByPage_pageNum}")
	String testSearchResourcesByPage_pageNum;
	@Value("${BQRepositoryControllerTestData.testSearchResourcesByPage_sql}")
	String testSearchResourcesByPage_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQRepositoryController#searchResourcesByPage(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int)}.
	 */
	public Map<String, Object> testSearchResourcesByPage() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {testSearchResourcesByPage_jobNumber, testSearchResourcesByPage_billNo, testSearchResourcesByPage_subBillNo, testSearchResourcesByPage_pageNo, testSearchResourcesByPage_itemNo, testSearchResourcesByPage_packageNo, testSearchResourcesByPage_objectCode, testSearchResourcesByPage_subsidiaryCode, testSearchResourcesByPage_description, new Integer(testSearchResourcesByPage_pageNum)});
		data.put("clazz", new Class<?>[]{String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, int.class});
		data.put("sql", testSearchResourcesByPage_sql);
		post();
		return data;
	}

	@Value("${BQRepositoryControllerTestData.testGetResourcesByBqItemId_id}")
	String testGetResourcesByBqItemId_id;
	@Value("${BQRepositoryControllerTestData.testGetResourcesByBqItemId_sql}")
	String testGetResourcesByBqItemId_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQRepositoryController#getResourcesByBqItemId(java.lang.Long)}.
	 */
	public Map<String, Object> testGetResourcesByBqItemId() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {new Long(testGetResourcesByBqItemId_id)});
		data.put("sql", testGetResourcesByBqItemId_sql);
		post();
		return data;
	}

	@Value("${BQRepositoryControllerTestData.testSaveResourceUpdates_id}")
	String testSaveResourceUpdates_id;
	@Value("${BQRepositoryControllerTestData.testSaveResourceUpdates_sql}")
	String testSaveResourceUpdates_sql;	
	/**
	 * Test data for {@link com.gammon.qs.web.BQRepositoryController#saveResourceUpdates(java.util.List)}.
	 */
	public Map<String, Object> testSaveResourceUpdates() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		BpiItemService bqService = applicationContext.getBean( BpiItemService.class);
		List<BpiItemResource> resources = null;
		try {
			resources = bqService.getResourcesByBqItemId(new Long(testSaveResourceUpdates_id));
		} catch (Exception e) {e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {resources});
		data.put("clazz", new Class<?>[]{List.class});
		data.put("sql", testSaveResourceUpdates_sql);
		post();
		return data;
	}

	@Value("${BQRepositoryControllerTestData.testSaveSplitMergeResources_oldId}")
	String testSaveSplitMergeResources_oldId;
	@Value("${BQRepositoryControllerTestData.testSaveSplitMergeResources_newId}")
	String testSaveSplitMergeResources_newId;
	@Value("${BQRepositoryControllerTestData.testSaveSplitMergeResources_sql}")
	String testSaveSplitMergeResources_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQRepositoryController#saveSplitMergeResources(java.util.List, java.util.List)}.
	 */
	public Map<String, Object> testSaveSplitMergeResources() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		BpiItemService bqService = applicationContext.getBean( BpiItemService.class);
		List<BpiItemResource> oldResources = null;
		List<BpiItemResource> newResources = null;
		try {
			oldResources = bqService.getResourcesByBqItemId(new Long(testSaveSplitMergeResources_oldId));
			newResources = bqService.getResourcesByBqItemId(new Long(testSaveSplitMergeResources_newId));
		} catch (Exception e) {e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {oldResources, newResources});
		data.put("clazz", new Class<?>[]{List.class, List.class});
		data.put("sql", testSaveSplitMergeResources_sql);
		post();
		return data;
	}

	@Value("${BQRepositoryControllerTestData.testGetBqItemsByPage_pageNum}")
	String testGetBqItemsByPage_pageNum;
	@Value("${BQRepositoryControllerTestData.testGetBqItemsByPage_jobNumber}")
	String testGetBqItemsByPage_jobNumber;
	@Value("${BQRepositoryControllerTestData.testGetBqItemsByPage_billNo}")
	String testGetBqItemsByPage_billNo;
	@Value("${BQRepositoryControllerTestData.testGetBqItemsByPage_subbillNo}")
	String testGetBqItemsByPage_subbillNo;
	@Value("${BQRepositoryControllerTestData.testGetBqItemsByPage_pageNo}")
	String testGetBqItemsByPage_pageNo;
	@Value("${BQRepositoryControllerTestData.testGetBqItemsByPage_itemNo}")
	String testGetBqItemsByPage_itemNo;
	@Value("${BQRepositoryControllerTestData.testGetBqItemsByPage_bqDesc}")	
	String testGetBqItemsByPage_bqDesc;
	@Value("${BQRepositoryControllerTestData.testGetBqItemsByPage_sql}")
	String testGetBqItemsByPage_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQRepositoryController#getBqItemsByPage(int)}.
	 */
	public void initGetBqItemsByPage(){
		BpiItemService bqService = applicationContext.getBean(BpiItemService.class);
		try {//cachedBQEnquiry first
			bqService.obtainBQItem(testGetBqItemsByPage_jobNumber, testGetBqItemsByPage_billNo, testGetBqItemsByPage_subbillNo, testGetBqItemsByPage_pageNo, testGetBqItemsByPage_itemNo, testGetBqItemsByPage_bqDesc);
		} catch (Exception e) {e.printStackTrace();
		}
	}
	public Map<String, Object> testGetBqItemsByPage() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {new Integer(testGetBqItemsByPage_pageNum)});
		data.put("clazz", new Class<?>[]{int.class});
		data.put("initMethod", "initGetBqItemsByPage");
		data.put("sql", testGetBqItemsByPage_sql);
		post();
		return data;
	}
	
	@Value("${BQRepositoryControllerTestData.testSaveBalancedResources_id}")
	String testSaveBalancedResources_id;
	@Value("${BQRepositoryControllerTestData.testSaveBalancedResources_remeasuredQuantity}")
	String testSaveBalancedResources_remeasuredQuantity;
	@Value("${BQRepositoryControllerTestData.testSaveBalancedResources_sql}")
	String testSaveBalancedResources_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQRepositoryController#saveBalancedResources(java.util.List, java.lang.Double)}.
	 */
	public Map<String, Object> testSaveBalancedResources() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		BpiItemService bqService = applicationContext.getBean(BpiItemService.class);
		List<BpiItemResource> resources = null;
		try {
			resources = bqService.getResourcesByBqItemId(new Long(testSaveBalancedResources_id));
		} catch (Exception e) {e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {resources, new Double(testSaveBalancedResources_remeasuredQuantity)});
		data.put("clazz", new Class<?>[]{List.class,Double.class});
		data.put("sql", testSaveBalancedResources_sql);
		post();
		return data;
	}
	
	@Value("${BQRepositoryControllerTestData.testGetBillItemFieldsForChangeOrder_jobNumber}")
	String testGetBillItemFieldsForChangeOrder_jobNumber;
	@Value("${BQRepositoryControllerTestData.testGetBillItemFieldsForChangeOrder_bqType}")
	String testGetBillItemFieldsForChangeOrder_bqType;
	@Value("${BQRepositoryControllerTestData.testGetBillItemFieldsForChangeOrder_sql}")
	String testGetBillItemFieldsForChangeOrder_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQRepositoryController#getBillItemFieldsForChangeOrder(com.gammon.qs.domain.JobInfo, java.lang.String)}.
	 */
	public Map<String, Object> testGetBillItemFieldsForChangeOrder() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		JobInfoService jobService = applicationContext.getBean("jobService",JobInfoService.class);
		JobInfo job = null;
		try {
			job = jobService.obtainJob(testGetBillItemFieldsForChangeOrder_jobNumber);
		} catch (DatabaseOperationException e) {e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {job, testGetBillItemFieldsForChangeOrder_bqType});
		data.put("sql", testGetBillItemFieldsForChangeOrder_sql);
		post();
		return data;
	}

	@Value("${BQRepositoryControllerTestData.testValidateBqItemChangeOrder_jobNumber}")
	String testValidateBqItemChangeOrder_jobNumber;
	@Value("${BQRepositoryControllerTestData.testValidateBqItemChangeOrder_bqType}")
	String testValidateBqItemChangeOrder_bqType;
	@Value("${BQRepositoryControllerTestData.testValidateBqItemChangeOrder_sql}")
	String testValidateBqItemChangeOrder_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQRepositoryController#validateBqItemChangeOrder(com.gammon.qs.domain.BpiItem)}.
	 */
	public Map<String, Object> testValidateBqItemChangeOrder() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		JobInfoService jobService = applicationContext.getBean("jobService",JobInfoService.class);
		BpiItemService bqService = applicationContext.getBean(BpiItemService.class);
		JobInfo job = null;
		BpiItem bqItem = null;
		try {
			job = jobService.obtainJob(testValidateBqItemChangeOrder_jobNumber);
			bqItem = bqService.getBillItemFieldsForChangeOrder(job, testValidateBqItemChangeOrder_bqType);
		} catch (Exception e) {e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {bqItem});
		data.put("sql", testValidateBqItemChangeOrder_sql);
		post();
		return data;
	}

	@Value("${BQRepositoryControllerTestData.testSaveChangeOrderBqAndResources_id}")
	String testSaveChangeOrderBqAndResources_id;
	@Value("${BQRepositoryControllerTestData.testSaveChangeOrderBqAndResources_sql}")
	String testSaveChangeOrderBqAndResources_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQRepositoryController#saveChangeOrderBqAndResources(com.gammon.qs.domain.BpiItem)}.
	 */
	public Map<String, Object> testSaveChangeOrderBqAndResources() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		BpiItemService bqService = applicationContext.getBean(BpiItemService.class);
		BpiItem bqItem = null;
		try {
			bqItem = bqService.getBqItemWithResources(new Long(testSaveChangeOrderBqAndResources_id));
		} catch (Exception e) {e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {bqItem});
		data.put("sql", testSaveChangeOrderBqAndResources_sql);
		post();
		return data;
	}

	@Value("${BQRepositoryControllerTestData.testGetBqItemWithResources_id}")
	String testGetBqItemWithResources_id;
	@Value("${BQRepositoryControllerTestData.testGetBqItemWithResources_sql}")
	String testGetBqItemWithResources_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQRepositoryController#getBpiItemWithResources(java.lang.Long)}.
	 */
	public Map<String, Object> testGetBqItemWithResources() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {new Long(testGetBqItemWithResources_id)});
		data.put("sql", testGetBqItemWithResources_sql);
		post();
		return data;
	}

	@Value("${BQRepositoryControllerTestData.testSaveResourceSubcontractAddendums_id}")
	String testSaveResourceSubcontractAddendums_id;
	@Value("${BQRepositoryControllerTestData.testSaveResourceSubcontractAddendums_sql}")
	String testSaveResourceSubcontractAddendums_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQRepositoryController#saveResourceSubcontractAddendums(java.util.List)}.
	 */
	public Map<String, Object> testSaveResourceSubcontractAddendums() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		BpiItemService bqService = applicationContext.getBean(BpiItemService.class);
		List<BpiItemResource> resources = null;
		try {
			resources = bqService.getResourcesByBqItemId(new Long(testSaveResourceSubcontractAddendums_id));
		} catch (Exception e) {e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {resources});
		data.put("clazz", new Class<?>[]{List.class});
		data.put("sql", testSaveResourceSubcontractAddendums_sql);
		post();
		return data;
	}

	@Value("${BQRepositoryControllerTestData.testGetResourcesByBpi_jobNumber}")
	String testGetResourcesByBpi_jobNumber;
	@Value("${BQRepositoryControllerTestData.testGetResourcesByBpi_bpi}")
	String testGetResourcesByBpi_bpi;
	@Value("${BQRepositoryControllerTestData.testGetResourcesByBpi_sql}")
	String testGetResourcesByBpi_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQRepositoryController#getResourcesByBpi(java.lang.String, java.lang.String)}.
	 */
	public Map<String, Object> testGetResourcesByBpi() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {testGetResourcesByBpi_jobNumber, testGetResourcesByBpi_bpi});
		data.put("sql", testGetResourcesByBpi_sql);
		post();
		return data;
	}

	@Value("${BQRepositoryControllerTestData.testSearchBQItemsForIVInput_jobNumber}")
	String testSearchBQItemsForIVInput_jobNumber;
	@Value("${BQRepositoryControllerTestData.testSearchBQItemsForIVInput_billNo}")
	String testSearchBQItemsForIVInput_billNo;
	@Value("${BQRepositoryControllerTestData.testSearchBQItemsForIVInput_subBillNo}")
	String testSearchBQItemsForIVInput_subBillNo;
	@Value("${BQRepositoryControllerTestData.testSearchBQItemsForIVInput_pageNo}")
	String testSearchBQItemsForIVInput_pageNo;
	@Value("${BQRepositoryControllerTestData.testSearchBQItemsForIVInput_itemNo}")
	String testSearchBQItemsForIVInput_itemNo;
	@Value("${BQRepositoryControllerTestData.testSearchBQItemsForIVInput_bqDescription}")
	String testSearchBQItemsForIVInput_bqDescription;
	@Value("${BQRepositoryControllerTestData.testSearchBQItemsForIVInput_sql}")
	String testSearchBQItemsForIVInput_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQRepositoryController#searchBQItemsForIVInput(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public Map<String, Object> testSearchBQItemsForIVInput() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {testSearchBQItemsForIVInput_jobNumber, testSearchBQItemsForIVInput_billNo, testSearchBQItemsForIVInput_subBillNo, testSearchBQItemsForIVInput_pageNo, testSearchBQItemsForIVInput_itemNo, testSearchBQItemsForIVInput_bqDescription});
		data.put("sql", testSearchBQItemsForIVInput_sql);
		post();
		return data;
	}

	@Value("${BQRepositoryControllerTestData.testGetBQItemsForIVInputByPage_pageNum}")
	String testGetBQItemsForIVInputByPage_pageNum;
	@Value("${BQRepositoryControllerTestData.testGetBQItemsForIVInputByPage_jobNumber}")
	String testGetBQItemsForIVInputByPage_jobNumber;
	@Value("${BQRepositoryControllerTestData.testGetBQItemsForIVInputByPage_billNo}")
	String testGetBQItemsForIVInputByPage_billNo;
	@Value("${BQRepositoryControllerTestData.testGetBQItemsForIVInputByPage_subBillNo}")
	String testGetBQItemsForIVInputByPage_subBillNo;
	@Value("${BQRepositoryControllerTestData.testGetBQItemsForIVInputByPage_pageNo}")
	String testGetBQItemsForIVInputByPage_pageNo;
	@Value("${BQRepositoryControllerTestData.testGetBQItemsForIVInputByPage_itemNo}")
	String testGetBQItemsForIVInputByPage_itemNo;
	@Value("${BQRepositoryControllerTestData.testGetBQItemsForIVInputByPage_bqDescription}")
	String testGetBQItemsForIVInputByPage_bqDescription;
	@Value("${BQRepositoryControllerTestData.testGetBQItemsForIVInputByPage_sql}")
	String testGetBQItemsForIVInputByPage_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQRepositoryController#getBQItemsForIVInputByPage(int)}.
	 */
	public void initGetBQItemsForIVInputByPage(){
		BpiItemService bqService = applicationContext.getBean(BpiItemService.class);
		try {//create ivInputByBQItemPaginationWrapper
			bqService.searchBQItemsForIVInput(testGetBQItemsForIVInputByPage_jobNumber, testGetBQItemsForIVInputByPage_billNo, testGetBQItemsForIVInputByPage_subBillNo, testGetBQItemsForIVInputByPage_pageNo, testGetBQItemsForIVInputByPage_itemNo, testGetBQItemsForIVInputByPage_bqDescription);
		} catch (Exception e) {e.printStackTrace();
		}
	}
	public Map<String, Object> testGetBQItemsForIVInputByPage() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {new Integer(testGetBQItemsForIVInputByPage_pageNum)});
		data.put("clazz", new Class<?>[]{int.class});
		data.put("initMethod", "initGetBQItemsForIVInputByPage");
		data.put("sql", testGetBQItemsForIVInputByPage_sql);
		post();
		return data;
	}

	@Value("${BQRepositoryControllerTestData.testSearchResourcesForIVInput_jobNumber}")
	String testSearchResourcesForIVInput_jobNumber;
	@Value("${BQRepositoryControllerTestData.testSearchResourcesForIVInput_billNo}")
	String testSearchResourcesForIVInput_billNo;
	@Value("${BQRepositoryControllerTestData.testSearchResourcesForIVInput_subBillNo}")
	String testSearchResourcesForIVInput_subBillNo;
	@Value("${BQRepositoryControllerTestData.testSearchResourcesForIVInput_pageNo}")
	String testSearchResourcesForIVInput_pageNo;
	@Value("${BQRepositoryControllerTestData.testSearchResourcesForIVInput_itemNo}")
	String testSearchResourcesForIVInput_itemNo;
	@Value("${BQRepositoryControllerTestData.testSearchResourcesForIVInput_resourceDescription}")
	String testSearchResourcesForIVInput_resourceDescription;
	@Value("${BQRepositoryControllerTestData.testSearchResourcesForIVInput_objectCode}")
	String testSearchResourcesForIVInput_objectCode;
	@Value("${BQRepositoryControllerTestData.testSearchResourcesForIVInput_subsidiaryCode}")
	String testSearchResourcesForIVInput_subsidiaryCode;
	@Value("${BQRepositoryControllerTestData.testSearchResourcesForIVInput_packageNo}")
	String testSearchResourcesForIVInput_packageNo;
	@Value("${BQRepositoryControllerTestData.testSearchResourcesForIVInput_sql}")
	String testSearchResourcesForIVInput_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQRepositoryController#searchResourcesForIVInput(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public Map<String, Object> testSearchResourcesForIVInput() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {testSearchResourcesForIVInput_jobNumber, testSearchResourcesForIVInput_billNo, testSearchResourcesForIVInput_subBillNo, testSearchResourcesForIVInput_pageNo, testSearchResourcesForIVInput_itemNo, testSearchResourcesForIVInput_resourceDescription, testSearchResourcesForIVInput_objectCode, testSearchResourcesForIVInput_subsidiaryCode, testSearchResourcesForIVInput_packageNo});
		data.put("sql", testSearchResourcesForIVInput_sql);
		post();
		return data;
	}

	@Value("${BQRepositoryControllerTestData.testGetResourcesForIVInputByPage_pageNum}")
	String testGetResourcesForIVInputByPage_pageNum;
	@Value("${BQRepositoryControllerTestData.testGetResourcesForIVInputByPage_jobNumber}")
	String testGetResourcesForIVInputByPage_jobNumber;
	@Value("${BQRepositoryControllerTestData.testGetResourcesForIVInputByPage_billNo}")
	String testGetResourcesForIVInputByPage_billNo;
	@Value("${BQRepositoryControllerTestData.testGetResourcesForIVInputByPage_subBillNo}")
	String testGetResourcesForIVInputByPage_subBillNo;
	@Value("${BQRepositoryControllerTestData.testGetResourcesForIVInputByPage_pageNo}")
	String testGetResourcesForIVInputByPage_pageNo;
	@Value("${BQRepositoryControllerTestData.testGetResourcesForIVInputByPage_itemNo}")
	String testGetResourcesForIVInputByPage_itemNo;
	@Value("${BQRepositoryControllerTestData.testGetResourcesForIVInputByPage_resourceDescription}")
	String testGetResourcesForIVInputByPage_resourceDescription;
	@Value("${BQRepositoryControllerTestData.testGetResourcesForIVInputByPage_objectCode}")
	String testGetResourcesForIVInputByPage_objectCode;
	@Value("${BQRepositoryControllerTestData.testGetResourcesForIVInputByPage_subsidiaryCode}")
	String testGetResourcesForIVInputByPage_subsidiaryCode;
	@Value("${BQRepositoryControllerTestData.testGetResourcesForIVInputByPage_packageNo}")
	String testGetResourcesForIVInputByPage_packageNo;
	@Value("${BQRepositoryControllerTestData.testGetResourcesForIVInputByPage_sql}")
	String testGetResourcesForIVInputByPage_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQRepositoryController#getResourcesForIVInputByPage(int)}.
	 */
	public void initGetResourceForIVInputByPage(){
		BpiItemService bqService = applicationContext.getBean(BpiItemService.class);
		try {//create ivInputByBQItemPaginationWrapper
			bqService.searchResourcesForIVInput(testGetResourcesForIVInputByPage_jobNumber, testGetResourcesForIVInputByPage_billNo, testGetResourcesForIVInputByPage_subBillNo, testGetResourcesForIVInputByPage_pageNo, testGetResourcesForIVInputByPage_itemNo, testGetResourcesForIVInputByPage_resourceDescription, testGetResourcesForIVInputByPage_objectCode, testGetResourcesForIVInputByPage_subsidiaryCode, testGetResourcesForIVInputByPage_packageNo);
		} catch (Exception e) {e.printStackTrace();
		}
	}
	public Map<String, Object> testGetResourcesForIVInputByPage() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {new Integer(testGetResourcesForIVInputByPage_pageNum)});
		data.put("clazz", new Class<?>[]{int.class});
		data.put("initMethod", "initGetResourceForIVInputByPage");
		data.put("sql", testGetResourcesForIVInputByPage_sql);
		post();
		return data;
	}
	
	@Value("${BQRepositoryControllerTestData.testUpdateBQItemsIVAmount_jobNumber}")
	String testUpdateBQItemsIVAmount_jobNumber;
	@Value("${BQRepositoryControllerTestData.testUpdateBQItemsIVAmount_billNo}")
	String testUpdateBQItemsIVAmount_billNo;
	@Value("${BQRepositoryControllerTestData.testUpdateBQItemsIVAmount_subbillNo}")
	String testUpdateBQItemsIVAmount_subbillNo;
	@Value("${BQRepositoryControllerTestData.testUpdateBQItemsIVAmount_pageNo}")
	String testUpdateBQItemsIVAmount_pageNo;
	@Value("${BQRepositoryControllerTestData.testUpdateBQItemsIVAmount_itemNo}")
	String testUpdateBQItemsIVAmount_itemNo;
	@Value("${BQRepositoryControllerTestData.testUpdateBQItemsIVAmount_bqDesc}")
	String testUpdateBQItemsIVAmount_bqDesc;
	@Value("${BQRepositoryControllerTestData.testUpdateBQItemsIVAmount_username}")
	String testUpdateBQItemsIVAmount_username;
	@Value("${BQRepositoryControllerTestData.testUpdateBQItemsIVAmount_sql}")
	String testUpdateBQItemsIVAmount_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQRepositoryController#updateBQItemsIVAmount(java.util.List, java.lang.String)}.
	 */
	public Map<String, Object> testUpdateBQItemsIVAmount() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		BpiItemService bqService = applicationContext.getBean(BpiItemService.class);
		List<BpiItem> bqItems = null;
		try {
			bqItems = bqService.obtainBQItemList(testUpdateBQItemsIVAmount_jobNumber, testUpdateBQItemsIVAmount_billNo, testUpdateBQItemsIVAmount_subbillNo, testUpdateBQItemsIVAmount_pageNo, testUpdateBQItemsIVAmount_itemNo, testUpdateBQItemsIVAmount_bqDesc);
		} catch (Exception e) {e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {bqItems, testUpdateBQItemsIVAmount_username});
		data.put("clazz", new Class<?>[]{List.class, String.class});
		data.put("sql", testUpdateBQItemsIVAmount_sql);
		post();
		return data;
	}

	@Value("${BQRepositoryControllerTestData.testUpdateResourcesIVAmount_id}")
	String testUpdateResourcesIVAmount_id;
	@Value("${BQRepositoryControllerTestData.testUpdateResourcesIVAmount_username}")
	String testUpdateResourcesIVAmount_username;
	@Value("${BQRepositoryControllerTestData.testUpdateResourcesIVAmount_sql}")
	String testUpdateResourcesIVAmount_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQRepositoryController#updateResourcesIVAmount(java.util.List, java.lang.String)}.
	 */
	public Map<String, Object> testUpdateResourcesIVAmount() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		BpiItemService bqService = applicationContext.getBean(BpiItemService.class);
		List<BpiItemResource> resources = null;
		try {
			resources = bqService.getResourcesByBqItemId(new Long(testUpdateResourcesIVAmount_id));
		} catch (Exception e) {e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {resources, testUpdateResourcesIVAmount_username});
		data.put("clazz", new Class<?>[]{List.class, String.class});
		data.put("sql", testUpdateResourcesIVAmount_sql);
		post();
		return data;
	}

	@Value("${BQRepositoryControllerTestData.testRecalculateIVAmountsByResourceForMethodThree_jobNumber}")
	String testRecalculateIVAmountsByResourceForMethodThree_jobNumber;
	@Value("${BQRepositoryControllerTestData.testRecalculateIVAmountsByResourceForMethodThree_sql}")
	String testRecalculateIVAmountsByResourceForMethodThree_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQRepositoryController#recalculateIVAmountsByResourceForMethodThree(java.lang.String)}.
	 */
	public Map<String, Object> testRecalculateIVAmountsByResourceForMethodThree() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {testRecalculateIVAmountsByResourceForMethodThree_jobNumber});
		data.put("sql", testRecalculateIVAmountsByResourceForMethodThree_sql);
		post();
		return data;
	}

	@Value("${BQRepositoryControllerTestData.testObtainResourcesByWrapper_jobNumber}")
	String testObtainResourcesByWrapper_jobNumber;
	@Value("${BQRepositoryControllerTestData.testObtainResourcesByWrapper_billNo}")
	String testObtainResourcesByWrapper_billNo;
	@Value("${BQRepositoryControllerTestData.testObtainResourcesByWrapper_subBillNo}")
	String testObtainResourcesByWrapper_subBillNo;
	@Value("${BQRepositoryControllerTestData.testObtainResourcesByWrapper_pageNo}")
	String testObtainResourcesByWrapper_pageNo;
	@Value("${BQRepositoryControllerTestData.testObtainResourcesByWrapper_itemNo}")
	String testObtainResourcesByWrapper_itemNo;
	@Value("${BQRepositoryControllerTestData.testObtainResourcesByWrapper_subsidiaryCode}")
	String testObtainResourcesByWrapper_subsidiaryCode;
	@Value("${BQRepositoryControllerTestData.testObtainResourcesByWrapper_objectCode}")
	String testObtainResourcesByWrapper_objectCode;
	@Value("${BQRepositoryControllerTestData.testObtainResourcesByWrapper_resDescription}")
	String testObtainResourcesByWrapper_resDescription;
	@Value("${BQRepositoryControllerTestData.testObtainResourcesByWrapper_packageNo}")
	String testObtainResourcesByWrapper_packageNo;
	@Value("${BQRepositoryControllerTestData.testObtainResourcesByWrapper_packageType}")
	String testObtainResourcesByWrapper_packageType;
	@Value("${BQRepositoryControllerTestData.testObtainResourcesByWrapper_sql}")
	String testObtainResourcesByWrapper_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQRepositoryController#obtainResourcesByWrapper(com.gammon.qs.wrapper.updateIVByResource.ResourceWrapper)}.
	 */
	public Map<String, Object> testObtainResourcesByWrapper() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		ResourceWrapper resourceWrapper = new ResourceWrapper();
		resourceWrapper.setJobNumber(testObtainResourcesByWrapper_jobNumber);
		resourceWrapper.setBillNo(testObtainResourcesByWrapper_billNo);
		resourceWrapper.setSubBillNo(testObtainResourcesByWrapper_subBillNo);
		resourceWrapper.setPageNo(testObtainResourcesByWrapper_pageNo);
		resourceWrapper.setItemNo(testObtainResourcesByWrapper_itemNo);
		resourceWrapper.setSubsidiaryCode(testObtainResourcesByWrapper_subsidiaryCode);
		resourceWrapper.setObjectCode(testObtainResourcesByWrapper_objectCode);
		resourceWrapper.setResDescription(testObtainResourcesByWrapper_resDescription);
		resourceWrapper.setPackageNo(testObtainResourcesByWrapper_packageNo);
		resourceWrapper.setPackageType(testObtainResourcesByWrapper_packageType);
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {resourceWrapper});
		data.put("sql", testObtainResourcesByWrapper_sql);
		post();
		return data;
	}

	@Value("${BQRepositoryControllerTestData.testObtainResourcesByPage_pageNum}")
	String testObtainResourcesByPage_pageNum;
	@Value("${BQRepositoryControllerTestData.testObtainResourcesByPage_jobNumber}")
	String testObtainResourcesByPage_jobNumber;
	@Value("${BQRepositoryControllerTestData.testObtainResourcesByPage_billNo}")
	String testObtainResourcesByPage_billNo;
	@Value("${BQRepositoryControllerTestData.testObtainResourcesByPage_subBillNo}")
	String testObtainResourcesByPage_subBillNo;
	@Value("${BQRepositoryControllerTestData.testObtainResourcesByPage_pageNo}")
	String testObtainResourcesByPage_pageNo;
	@Value("${BQRepositoryControllerTestData.testObtainResourcesByPage_itemNo}")
	String testObtainResourcesByPage_itemNo;
	@Value("${BQRepositoryControllerTestData.testObtainResourcesByPage_subsidiaryCode}")
	String testObtainResourcesByPage_subsidiaryCode;
	@Value("${BQRepositoryControllerTestData.testObtainResourcesByPage_objectCode}")
	String testObtainResourcesByPage_objectCode;
	@Value("${BQRepositoryControllerTestData.testObtainResourcesByPage_resDescription}")
	String testObtainResourcesByPage_resDescription;
	@Value("${BQRepositoryControllerTestData.testObtainResourcesByPage_packageNo}")
	String testObtainResourcesByPage_packageNo;
	@Value("${BQRepositoryControllerTestData.testObtainResourcesByPage_packageType}")
	String testObtainResourcesByPage_packageType;
	@Value("${BQRepositoryControllerTestData.testObtainResourcesByPage_sql}")
	String testObtainResourcesByPage_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQRepositoryController#obtainResourcesByPage(int)}.
	 */
	public void initObtainResourcesByPage(){
		ResourceWrapper resourceWrapper = new ResourceWrapper();
		resourceWrapper.setJobNumber(testObtainResourcesByWrapper_jobNumber);
		resourceWrapper.setBillNo(testObtainResourcesByWrapper_billNo);
		resourceWrapper.setSubBillNo(testObtainResourcesByWrapper_subBillNo);
		resourceWrapper.setPageNo(testObtainResourcesByWrapper_pageNo);
		resourceWrapper.setItemNo(testObtainResourcesByWrapper_itemNo);
		resourceWrapper.setSubsidiaryCode(testObtainResourcesByWrapper_subsidiaryCode);
		resourceWrapper.setObjectCode(testObtainResourcesByWrapper_objectCode);
		resourceWrapper.setResDescription(testObtainResourcesByWrapper_resDescription);
		resourceWrapper.setPackageNo(testObtainResourcesByWrapper_packageNo);
		resourceWrapper.setPackageType(testObtainResourcesByWrapper_packageType);
		BpiItemService bqService = applicationContext.getBean(BpiItemService.class);
		try {//create cachedResourceEnquiry
			bqService.obtainResourcesByWrapper(resourceWrapper);
		} catch (Exception e) {e.printStackTrace();
		}
	}
	public Map<String, Object> testObtainResourcesByPage() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {new Integer(testObtainResourcesByPage_pageNum)});
		data.put("clazz", new Class<?>[]{int.class});
		data.put("initMethod", "initObtainResourcesByPage");
		data.put("sql", testObtainResourcesByPage_sql);
		post();
		return data;
	}

	@Value("${BQRepositoryControllerTestData.testObtainUneditableResources_jobNumber}")
	String testObtainUneditableResources_jobNumber;
	@Value("${BQRepositoryControllerTestData.testObtainUneditableResources_sql}")
	String testObtainUneditableResources_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQRepositoryController#obtainUneditableResources(com.gammon.qs.domain.JobInfo)}.
	 */
	public Map<String, Object> testObtainUneditableResources() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		JobInfoService jobService = applicationContext.getBean("jobService",JobInfoService.class);
		JobInfo job = null;
		try {
			job = jobService.obtainJob(testObtainUneditableResources_jobNumber);
		} catch (DatabaseOperationException e) {e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {job});
		data.put("sql", testObtainUneditableResources_sql);
		post();
		return data;
	}
}
