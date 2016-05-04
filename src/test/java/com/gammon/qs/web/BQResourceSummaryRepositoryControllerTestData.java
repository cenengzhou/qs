/**
 * 
 */
package com.gammon.qs.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.junit.testcase.ControllerTestCase;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.BQResourceSummaryHBDao;
import com.gammon.qs.domain.BQResourceSummary;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.RepackagingEntry;
import com.gammon.qs.service.BQResourceSummaryService;
import com.gammon.qs.service.JobService;
import com.gammon.qs.service.RepackagingEntryService;

/**
 * @author paulnpyiu
 *
 */
@Configuration
@PropertySource("file:${BQResourceSummaryRepositoryControllerTestData.properties}")
@Transactional(rollbackFor = Exception.class)
public class BQResourceSummaryRepositoryControllerTestData extends ControllerTestCase.TestDataBase {
	private Class<?> serviceClass = BQResourceSummaryRepositoryController.class;
	
	@Value("${BQResourceSummaryRepositoryControllerTestData.testCreateResourceSummary_sql}")
	String testCreateResourceSummary_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQResourceSummaryRepositoryController#createResourceSummary(com.gammon.qs.domain.Job, java.lang.String)}.
	 * empty method
	 */
	public Map<String, Object> testCreateResourceSummary() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {new Job(),new String()});
		data.put("sql", testCreateResourceSummary_sql);
		post();
		return data;
	}

	@Value("${BQResourceSummaryRepositoryControllerTestData.testGetResourceSummariesByJob_jobNumber}")
	String testGetResourceSummariesByJob_jobNumber;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testGetResourceSummariesByJob_sql}")
	String testGetResourceSummariesByJob_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQResourceSummaryRepositoryController#getResourceSummariesByJob(com.gammon.qs.domain.Job)}.
	 */
	public Map<String, Object> testGetResourceSummariesByJob() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		JobService jobService = applicationContext.getBean("jobService",JobService.class);
		Job job = null;
		try {
			job = jobService.obtainJob(testGetResourceSummariesByJob_jobNumber);
		} catch (DatabaseOperationException e) {e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {job});
		data.put("sql", testGetResourceSummariesByJob_sql);
		post();
		return data;
	}

	@Value("${BQResourceSummaryRepositoryControllerTestData.testGetResourceSummariesByJobNumber_jobNumber}")
	String testGetResourceSummariesByJobNumber_jobNumber;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testGetResourceSummariesByJobNumber_sql}")
	String testGetResourceSummariesByJobNumber_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQResourceSummaryRepositoryController#getResourceSummariesByJobNumber(java.lang.String)}.
	 */
	public Map<String, Object> testGetResourceSummariesByJobNumber() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {testGetResourceSummariesByJobNumber_jobNumber});
		data.put("sql", testGetResourceSummariesByJobNumber_sql);
		post();
		return data;
	}

	@Value("${BQResourceSummaryRepositoryControllerTestData.testGetResourceSummariesSearch_jobNumber}")
	String testGetResourceSummariesSearch_jobNumber;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testGetResourceSummariesSearch_packageNo}")
	String testGetResourceSummariesSearch_packageNo;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testGetResourceSummariesSearch_objectCode}")
	String testGetResourceSummariesSearch_objectCode;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testGetResourceSummariesSearch_subsidiaryCode}")
	String testGetResourceSummariesSearch_subsidiaryCode;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testGetResourceSummariesSearch_sql}")
	String testGetResourceSummariesSearch_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQResourceSummaryRepositoryController#getResourceSummariesSearch(com.gammon.qs.domain.Job, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public Map<String, Object> testGetResourceSummariesSearch() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		JobService jobService = applicationContext.getBean("jobService",JobService.class);
		Job job = null;
		try {
			job = jobService.obtainJob(testGetResourceSummariesSearch_jobNumber);
		} catch (DatabaseOperationException e) {e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {job, testGetResourceSummariesSearch_packageNo, testGetResourceSummariesSearch_objectCode, testGetResourceSummariesSearch_subsidiaryCode});
		data.put("sql", testGetResourceSummariesSearch_sql);
		post();
		return data;
	}
	
	@Value("${BQResourceSummaryRepositoryControllerTestData.testObtainResourceSummariesSearchByPage_jobNumber}")
	String testObtainResourceSummariesSearchByPage_jobNumber; 
	@Value("${BQResourceSummaryRepositoryControllerTestData.testObtainResourceSummariesSearchByPage_packageNo}")
	String testObtainResourceSummariesSearchByPage_packageNo;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testObtainResourceSummariesSearchByPage_objectCode}")
	String testObtainResourceSummariesSearchByPage_objectCode;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testObtainResourceSummariesSearchByPage_subsidiaryCode}")
	String testObtainResourceSummariesSearchByPage_subsidiaryCode;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testObtainResourceSummariesSearchByPage_description}")
	String testObtainResourceSummariesSearchByPage_description;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testObtainResourceSummariesSearchByPage_type}")
	String testObtainResourceSummariesSearchByPage_type;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testObtainResourceSummariesSearchByPage_levyExcluded}")
	String testObtainResourceSummariesSearchByPage_levyExcluded;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testObtainResourceSummariesSearchByPage_defectExcluded}")
	String testObtainResourceSummariesSearchByPage_defectExcluded;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testObtainResourceSummariesSearchByPage_pageNum}")
	String testObtainResourceSummariesSearchByPage_pageNum;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testObtainResourceSummariesSearchByPage_sql}")
	String testObtainResourceSummariesSearchByPage_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQResourceSummaryRepositoryController#obtainResourceSummariesSearchByPage(com.gammon.qs.domain.Job, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int)}.
	 */
	public Map<String, Object> testObtainResourceSummariesSearchByPage() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		JobService jobService = applicationContext.getBean("jobService",JobService.class);
		Job job = null;
		try {
			job = jobService.obtainJob(testObtainResourceSummariesSearchByPage_jobNumber);
		} catch (DatabaseOperationException e) {e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {job, testObtainResourceSummariesSearchByPage_packageNo, testObtainResourceSummariesSearchByPage_objectCode, testObtainResourceSummariesSearchByPage_subsidiaryCode, testObtainResourceSummariesSearchByPage_description, testObtainResourceSummariesSearchByPage_type, testObtainResourceSummariesSearchByPage_levyExcluded, testObtainResourceSummariesSearchByPage_defectExcluded, new Integer(testObtainResourceSummariesSearchByPage_pageNum)});
		data.put("clazz", new Class<?>[]{Job.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, int.class});
		data.put("sql", testObtainResourceSummariesSearchByPage_sql);
		post();
		return data;
	}

	@Value("${BQResourceSummaryRepositoryControllerTestData.testObtainResourceSummariesSearchByPageForIVInput_jobNumber}")
	String testObtainResourceSummariesSearchByPageForIVInput_jobNumber; 
	@Value("${BQResourceSummaryRepositoryControllerTestData.testObtainResourceSummariesSearchByPageForIVInput_packageNo}")
	String testObtainResourceSummariesSearchByPageForIVInput_packageNo;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testObtainResourceSummariesSearchByPageForIVInput_objectCode}")
	String testObtainResourceSummariesSearchByPageForIVInput_objectCode;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testObtainResourceSummariesSearchByPageForIVInput_subsidiaryCode}")
	String testObtainResourceSummariesSearchByPageForIVInput_subsidiaryCode;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testObtainResourceSummariesSearchByPageForIVInput_description}")
	String testObtainResourceSummariesSearchByPageForIVInput_description;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testObtainResourceSummariesSearchByPageForIVInput_pageNum}")
	int testObtainResourceSummariesSearchByPageForIVInput_pageNum;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testObtainResourceSummariesSearchByPageForIVInput_finalizedPackage}")
	boolean testObtainResourceSummariesSearchByPageForIVInput_finalizedPackage;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testObtainResourceSummariesSearchByPageForIVInput_sql}")
	String testObtainResourceSummariesSearchByPageForIVInput_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQResourceSummaryRepositoryController#obtainResourceSummariesSearchByPageForIVInput(com.gammon.qs.domain.Job, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, boolean)}.
	 */
	public Map<String, Object> testObtainResourceSummariesSearchByPageForIVInput() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		JobService jobService = applicationContext.getBean("jobService",JobService.class);
		Job job = null;
		try {
			job = jobService.obtainJob(testGetResourceSummariesByJob_jobNumber);
		} catch (DatabaseOperationException e) {e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {job, testObtainResourceSummariesSearchByPageForIVInput_packageNo, testObtainResourceSummariesSearchByPageForIVInput_objectCode, testObtainResourceSummariesSearchByPageForIVInput_subsidiaryCode, testObtainResourceSummariesSearchByPageForIVInput_description, new Integer(testObtainResourceSummariesSearchByPageForIVInput_pageNum), new Boolean(testObtainResourceSummariesSearchByPageForIVInput_finalizedPackage)});
		data.put("clazz", new Class<?>[]{Job.class, String.class, String.class, String.class, String.class, int.class, boolean.class});
		data.put("sql", testObtainResourceSummariesSearchByPageForIVInput_sql);
		post();
		return data;
	}

	@Value("${BQResourceSummaryRepositoryControllerTestData.testSaveResourceSummary_jobNumber}")
	String testSaveResourceSummary_jobNumber;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testSaveResourceSummary_packageNo}")
	String testSaveResourceSummary_packageNo;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testSaveResourceSummary_objectCode}")
	String testSaveResourceSummary_objectCode;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testSaveResourceSummary_subsidiaryCode}")
	String testSaveResourceSummary_subsidiaryCode;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testSaveResourceSummary_resourceDescription}")
	String testSaveResourceSummary_resourceDescription;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testSaveResourceSummary_unit}")
	String testSaveResourceSummary_unit;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testSaveResourceSummary_rate}")
	String testSaveResourceSummary_rate;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testSaveResourceSummary_repackagingEntryId}")
	String testSaveResourceSummary_repackagingEntryId;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testSaveResourceSummary_sql}")
	String testSaveResourceSummary_sql;
	public BQResourceSummary bqResourceSummary = null;
	/**
	 * Test data for {@link com.gammon.qs.web.BQResourceSummaryRepositoryController#saveResourceSummary(com.gammon.qs.domain.BQResourceSummary, java.lang.Long)}.
	 */
	public Map<String, Object> testSaveResourceSummary() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		BQResourceSummaryService bqResourceSummaryService = applicationContext.getBean(BQResourceSummaryService.class);
		
		try{
			bqResourceSummary = bqResourceSummaryService.getResourceSummary(testSaveResourceSummary_jobNumber, testSaveResourceSummary_packageNo, testSaveResourceSummary_objectCode, testSaveResourceSummary_subsidiaryCode, testSaveResourceSummary_resourceDescription, testSaveResourceSummary_unit, new Double(testSaveResourceSummary_rate));
		} catch (Exception e){ e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {bqResourceSummary, new Long(testSaveResourceSummary_repackagingEntryId)});
		data.put("sql", testSaveResourceSummary_sql);
		post();
		return data;
	}

	@Value("${BQResourceSummaryRepositoryControllerTestData.testSaveResourceSummaries_jobNumber}")
	String testSaveResourceSummaries_jobNumber;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testSaveResourceSummaries_repackagingEntryId}")
	String testSaveResourceSummaries_repackagingEntryId;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testSaveResourceSummaries_sql}")
	String testSaveResourceSummaries_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQResourceSummaryRepositoryController#saveResourceSummaries(java.util.List, java.lang.Long)}.
	 */
	public Map<String, Object> testSaveResourceSummaries() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		BQResourceSummaryHBDao bqResourceSummaryHBDao = applicationContext.getBean(BQResourceSummaryHBDao.class);
		List<BQResourceSummary> bqResourceSummaryList = null;
		try{
			bqResourceSummaryList = bqResourceSummaryHBDao.obtainSCResourceSummariesByJobNumber(testSaveResourceSummaries_jobNumber);
		} catch (DatabaseOperationException e){ e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {bqResourceSummaryList, new Long(testSaveResourceSummaries_repackagingEntryId)});
		data.put("clazz", new Class<?>[]{List.class, Long.class});
		data.put("sql", testSaveResourceSummaries_sql);
		post();
		return data;
	}

	@Value("${BQResourceSummaryRepositoryControllerTestData.testUpdateResourceSummariesIVAmount_jobNumber}")
	String testUpdateResourceSummariesIVAmount_jobNumber;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testUpdateResourceSummariesIVAmount_sql}")
	String testUpdateResourceSummariesIVAmount_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQResourceSummaryRepositoryController#updateResourceSummariesIVAmount(java.util.List)}.
	 */
	public Map<String, Object> testUpdateResourceSummariesIVAmount() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		BQResourceSummaryHBDao bqResourceSummaryHBDao = applicationContext.getBean(BQResourceSummaryHBDao.class);
		List<BQResourceSummary> bqResourceSummaryList = null;
		try{
			bqResourceSummaryList = bqResourceSummaryHBDao.obtainSCResourceSummariesByJobNumber(testUpdateResourceSummariesIVAmount_jobNumber);
		} catch (DatabaseOperationException e){ e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {bqResourceSummaryList});
		data.put("clazz", new Class<?>[]{List.class});
		data.put("sql", testUpdateResourceSummariesIVAmount_sql);
		post();
		return data;
	}

	@Value("${BQResourceSummaryRepositoryControllerTestData.testSplitOrMergeResources_oldResources}")
	String testSplitOrMergeResources_oldResources;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testSplitOrMergeResources_newResources}")
	String testSplitOrMergeResources_newResources;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testSplitOrMergeResources_repackagingEntryId}")
	String testSplitOrMergeResources_repackagingEntryId;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testSplitOrMergeResources_sql}")
	String testSplitOrMergeResources_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQResourceSummaryRepositoryController#splitOrMergeResources(java.util.List, java.util.List, java.lang.Long)}.
	 */
	public Map<String, Object> testSplitOrMergeResources() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		BQResourceSummaryHBDao bqResourceSummaryHBDao = applicationContext.getBean(BQResourceSummaryHBDao.class);
		List<BQResourceSummary> bqResourceSummaryOldList = null;
		List<BQResourceSummary> bqResourceSummaryNewList = null;
		try{
			bqResourceSummaryOldList = bqResourceSummaryHBDao.obtainSCResourceSummariesByJobNumber(testSplitOrMergeResources_oldResources);
			bqResourceSummaryNewList = bqResourceSummaryHBDao.obtainSCResourceSummariesByJobNumber(testSplitOrMergeResources_newResources);
		} catch (DatabaseOperationException e){ e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {bqResourceSummaryOldList, bqResourceSummaryNewList, new Long(testSplitOrMergeResources_repackagingEntryId)});
		data.put("clazz", new Class<?>[]{List.class, List.class, Long.class});
		data.put("sql", testSplitOrMergeResources_sql);
		post();
		return data;
	}

	@Value("${BQResourceSummaryRepositoryControllerTestData.testGroupResourcesIntoSummaries_jobNumber}")
	String testGroupResourcesIntoSummaries_jobNumber;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testGroupResourcesIntoSummaries_sql}")
	String testGroupResourcesIntoSummaries_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQResourceSummaryRepositoryController#groupResourcesIntoSummaries(com.gammon.qs.domain.Job)}.
	 */
	public Map<String, Object> testGroupResourcesIntoSummaries() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		JobService jobService = applicationContext.getBean("jobService",JobService.class);
		Job job = null;
		try {
			job = jobService.obtainJob(testGroupResourcesIntoSummaries_jobNumber);
		} catch (DatabaseOperationException e) {e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {job});
		data.put("sql", testGroupResourcesIntoSummaries_sql);
		post();
		return data;
	}

	@Value("${BQResourceSummaryRepositoryControllerTestData.testPostIVAmounts_jobNumber}")
	String testPostIVAmounts_jobNumber;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testPostIVAmounts_username}")
	String testPostIVAmounts_username;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testPostIVAmounts_finalized}")
	String testPostIVAmounts_finalized;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testPostIVAmounts_sql}")
	String testPostIVAmounts_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQResourceSummaryRepositoryController#postIVAmounts(com.gammon.qs.domain.Job, java.lang.String, boolean)}.
	 */
	public Map<String, Object> testPostIVAmounts() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		JobService jobService = applicationContext.getBean("jobService",JobService.class);
		Job job = null;
		try {
			job = jobService.obtainJob(testPostIVAmounts_jobNumber);
		} catch (DatabaseOperationException e) {e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {job, testPostIVAmounts_username, new Boolean(testPostIVAmounts_finalized)});
		data.put("clazz", new Class<?>[]{Job.class, String.class, boolean.class});
		data.put("sql", testPostIVAmounts_sql);
		post();
		return data;
	}

	@Value("${BQResourceSummaryRepositoryControllerTestData.testGenerateSnapshotMethodTwo_jobNumber}")
	String testGenerateSnapshotMethodTwo_jobNumber;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testGenerateSnapshotMethodTwo_repackagingEntry}")
	String testGenerateSnapshotMethodTwo_repackagingEntry;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testGenerateSnapshotMethodTwo_sql}")
	String testGenerateSnapshotMethodTwo_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQResourceSummaryRepositoryController#generateSnapshotMethodTwo(com.gammon.qs.domain.Job, com.gammon.qs.domain.RepackagingEntry)}.
	 */
	public Map<String, Object> testGenerateSnapshotMethodTwo() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		JobService jobService = applicationContext.getBean("jobService",JobService.class);
		RepackagingEntryService repackagingEntryService = applicationContext.getBean(RepackagingEntryService.class);
		Job job = null;
		RepackagingEntry repackagingEntry = null;
		try {
			job = jobService.obtainJob(testGenerateSnapshotMethodTwo_jobNumber);
			repackagingEntry = repackagingEntryService.getRepackagingEntry(new Long(testGenerateSnapshotMethodTwo_repackagingEntry));
		} catch (Exception e) {e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {job, repackagingEntry});
		data.put("sql", testGenerateSnapshotMethodTwo_sql);
		post();
		return data;
	}

	@Value("${BQResourceSummaryRepositoryControllerTestData.testDeleteVoResources_jobNumber}")
	String testDeleteVoResources_jobNumber;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testDeleteVoResources_sql}")
	String testDeleteVoResources_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQResourceSummaryRepositoryController#deleteVoResources(java.util.List)}.
	 */
	public Map<String, Object> testDeleteVoResources() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		BQResourceSummaryHBDao bqResourceSummaryHBDao = applicationContext.getBean(BQResourceSummaryHBDao.class);
		List<BQResourceSummary> bqResourceSummaryList = null;
		try{
			bqResourceSummaryList = bqResourceSummaryHBDao.obtainSCResourceSummariesByJobNumber(testDeleteVoResources_jobNumber);
		} catch (DatabaseOperationException e){ e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {bqResourceSummaryList});
		data.put("clazz", new Class<?>[]{List.class});
		data.put("sql", testDeleteVoResources_sql);
		post();
		return data;
	}

	@Value("${BQResourceSummaryRepositoryControllerTestData.testSaveResourceSummariesScAddendum_jobNumber}")
	String testSaveResourceSummariesScAddendum_jobNumber;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testSaveResourceSummariesScAddendum_repackagingEntryId}")
	String testSaveResourceSummariesScAddendum_repackagingEntryId;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testSaveResourceSummariesScAddendum_sql}")
	String testSaveResourceSummariesScAddendum_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQResourceSummaryRepositoryController#saveResourceSummariesScAddendum(java.util.List, java.lang.Long)}.
	 */
	public Map<String, Object> testSaveResourceSummariesScAddendum() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		BQResourceSummaryHBDao bqResourceSummaryHBDao = applicationContext.getBean(BQResourceSummaryHBDao.class);
		List<BQResourceSummary> bqResourceSummaryList = null;
		try{
			bqResourceSummaryList = bqResourceSummaryHBDao.obtainSCResourceSummariesByJobNumber(testSaveResourceSummariesScAddendum_jobNumber);
		} catch (DatabaseOperationException e){ e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {bqResourceSummaryList, new Long(testSaveResourceSummariesScAddendum_repackagingEntryId)});
		data.put("clazz", new Class<?>[]{List.class, Long.class});
		data.put("sql", testSaveResourceSummariesScAddendum_sql);
		post();
		return data;
	}

	@Value("${BQResourceSummaryRepositoryControllerTestData.testCheckForDuplicates_jobNumber}")
	String testCheckForDuplicates_jobNumber;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testCheckForDuplicates_sql}")
	String testCheckForDuplicates_sql;	
	/**
	 * Test data for {@link com.gammon.qs.web.BQResourceSummaryRepositoryController#checkForDuplicates(java.util.List, com.gammon.qs.domain.Job)}.
	 */
	public Map<String, Object> testCheckForDuplicates() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		JobService jobService = applicationContext.getBean("jobService",JobService.class);
		BQResourceSummaryHBDao bqResourceSummaryHBDao = applicationContext.getBean(BQResourceSummaryHBDao.class);
		Job job = null;
		List<BQResourceSummary> bqResourceSummaryList = null;
		try {
			job = jobService.obtainJob(testCheckForDuplicates_jobNumber);
			bqResourceSummaryList = bqResourceSummaryHBDao.obtainSCResourceSummariesByJobNumber(testCheckForDuplicates_jobNumber);
		} catch (DatabaseOperationException e) {e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {bqResourceSummaryList, job});
		data.put("clazz", new Class<?>[]{List.class, Job.class});
		data.put("sql", testCheckForDuplicates_sql);
		post();
		return data;
	}

	@Value("${BQResourceSummaryRepositoryControllerTestData.testGenerateSnapshotMethodThree_jobNumber}")
	String testGenerateSnapshotMethodThree_jobNumber;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testGenerateSnapshotMethodThree_repackagingEntry}")
	String testGenerateSnapshotMethodThree_repackagingEntry;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testGenerateSnapshotMethodThree_sql}")
	String testGenerateSnapshotMethodThree_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQResourceSummaryRepositoryController#generateSnapshotMethodThree(com.gammon.qs.domain.Job, com.gammon.qs.domain.RepackagingEntry)}.
	 */
	public Map<String, Object> testGenerateSnapshotMethodThree() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		JobService jobService = applicationContext.getBean("jobService",JobService.class);
		RepackagingEntryService repackagingEntryService = applicationContext.getBean(RepackagingEntryService.class);
		Job job = null;
		RepackagingEntry repackagingEntry = null;
		try {
			job = jobService.obtainJob(testGenerateSnapshotMethodThree_jobNumber);
			repackagingEntry = repackagingEntryService.getRepackagingEntry(new Long(testGenerateSnapshotMethodThree_repackagingEntry));
		} catch (Exception e) {e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {job, repackagingEntry});
		data.put("sql", testGenerateSnapshotMethodThree_sql);
		post();
		return data;
	}

	@Value("${BQResourceSummaryRepositoryControllerTestData.testGetIVMovementOfJobFromResourceSummary_jobNumber}")
	String testGetIVMovementOfJobFromResourceSummary_jobNumber;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testGetIVMovementOfJobFromResourceSummary_sql}")
	String testGetIVMovementOfJobFromResourceSummary_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQResourceSummaryRepositoryController#getIVMovementOfJobFromResourceSummary(com.gammon.qs.domain.Job)}.
	 */
	public Map<String, Object> testGetIVMovementOfJobFromResourceSummary() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		JobService jobService = applicationContext.getBean("jobService",JobService.class);
		Job job = null;
		try {
			job = jobService.obtainJob(testGetIVMovementOfJobFromResourceSummary_jobNumber);
		} catch (DatabaseOperationException e) {e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {job});
		data.put("sql", testGetIVMovementOfJobFromResourceSummary_sql);
		post();
		return data;
	}

	@Value("${BQResourceSummaryRepositoryControllerTestData.testObtainUneditableResourceSummaries_jobNumber}")
	String testObtainUneditableResourceSummaries_jobNumber;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testObtainUneditableResourceSummaries_sql}")
	String testObtainUneditableResourceSummaries_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQResourceSummaryRepositoryController#obtainUneditableResourceSummaries(com.gammon.qs.domain.Job)}.
	 */
	public Map<String, Object> testObtainUneditableResourceSummaries() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		JobService jobService = applicationContext.getBean("jobService",JobService.class);
		Job job = null;
		try {
			job = jobService.obtainJob(testObtainUneditableResourceSummaries_jobNumber);
		} catch (DatabaseOperationException e) {e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {job});
		data.put("sql", testObtainUneditableResourceSummaries_sql);
		post();
		return data;
	}

	@Value("${BQResourceSummaryRepositoryControllerTestData.testUpdateResourceSummariesIVAmountForFinalizedPackage_jobNumber}")
	String testUpdateResourceSummariesIVAmountForFinalizedPackage_jobNumber;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testUpdateResourceSummariesIVAmountForFinalizedPackage_sql}")
	String testUpdateResourceSummariesIVAmountForFinalizedPackage_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQResourceSummaryRepositoryController#updateResourceSummariesIVAmountForFinalizedPackage(java.util.List)}.
	 */
	public Map<String, Object> testUpdateResourceSummariesIVAmountForFinalizedPackage() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		BQResourceSummaryHBDao bqResourceSummaryHBDao = applicationContext.getBean(BQResourceSummaryHBDao.class);
		List<BQResourceSummary> bqResourceSummaryList = null;
		try{
			bqResourceSummaryList = bqResourceSummaryHBDao.obtainSCResourceSummariesByJobNumber(testUpdateResourceSummariesIVAmountForFinalizedPackage_jobNumber);
		} catch (DatabaseOperationException e){ e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {bqResourceSummaryList});
		data.put("clazz", new Class<?>[]{List.class});
		data.put("sql", testUpdateResourceSummariesIVAmountForFinalizedPackage_sql);
		post();
		return data;
	}

	@Value("${BQResourceSummaryRepositoryControllerTestData.testObtainIVMovementByJob_jobNumber}")
	String testObtainIVMovementByJob_jobNumber;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testObtainIVMovementByJob_finalized}")
	String testObtainIVMovementByJob_finalized;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testObtainIVMovementByJob_sql}")
	String testObtainIVMovementByJob_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQResourceSummaryRepositoryController#obtainIVMovementByJob(com.gammon.qs.domain.Job, boolean)}.
	 */
	public Map<String, Object> testObtainIVMovementByJob() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		JobService jobService = applicationContext.getBean("jobService",JobService.class);
		Job job = null;
		try {
			job = jobService.obtainJob(testObtainIVMovementByJob_jobNumber);
		} catch (DatabaseOperationException e) {e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {job, new Boolean(testObtainIVMovementByJob_finalized)});
		data.put("clazz", new Class<?>[]{Job.class, boolean.class});
		data.put("sql", testObtainIVMovementByJob_sql);
		post();
		return data;
	}

	@Value("${BQResourceSummaryRepositoryControllerTestData.testRecalculateResourceSummaryIVForFinalizedPackage_jobNumber}")
	String testRecalculateResourceSummaryIVForFinalizedPackage_jobNumber;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testRecalculateResourceSummaryIVForFinalizedPackage_packageNo}")
	String testRecalculateResourceSummaryIVForFinalizedPackage_packageNo;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testRecalculateResourceSummaryIVForFinalizedPackage_objectCode}")
	String testRecalculateResourceSummaryIVForFinalizedPackage_objectCode;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testRecalculateResourceSummaryIVForFinalizedPackage_subsidiaryCode}")
	String testRecalculateResourceSummaryIVForFinalizedPackage_subsidiaryCode;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testRecalculateResourceSummaryIVForFinalizedPackage_description}")
	String testRecalculateResourceSummaryIVForFinalizedPackage_description;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testRecalculateResourceSummaryIVForFinalizedPackage_sql}")
	String testRecalculateResourceSummaryIVForFinalizedPackage_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BQResourceSummaryRepositoryController#recalculateResourceSummaryIVForFinalizedPackage(com.gammon.qs.domain.Job, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public Map<String, Object> testRecalculateResourceSummaryIVForFinalizedPackage() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		JobService jobService = applicationContext.getBean("jobService",JobService.class);
		Job job = null;
		try {
			job = jobService.obtainJob(testRecalculateResourceSummaryIVForFinalizedPackage_jobNumber);
		} catch (DatabaseOperationException e) {e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {job, testRecalculateResourceSummaryIVForFinalizedPackage_packageNo, testRecalculateResourceSummaryIVForFinalizedPackage_objectCode, testRecalculateResourceSummaryIVForFinalizedPackage_subsidiaryCode, testRecalculateResourceSummaryIVForFinalizedPackage_description});
		data.put("sql", testRecalculateResourceSummaryIVForFinalizedPackage_sql);
		post();
		return data;
	}

}
