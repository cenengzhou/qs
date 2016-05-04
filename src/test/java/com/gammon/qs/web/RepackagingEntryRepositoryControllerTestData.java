/**
 * 
 */
package com.gammon.qs.web;

import java.util.Map;

import com.gammon.junit.testcase.ControllerTestCase;

/**
 * @author paulnpyiu
 *
 */
//@Configuration
//@PropertySource("file:${RepackagingEntryRepositoryControllerTestData.properties}")
public class RepackagingEntryRepositoryControllerTestData extends ControllerTestCase.TestDataBase {
	private Class<?> serviceClass = RepackagingEntryRepositoryController.class;

	/**
	 * Test data for {@link com.gammon.qs.web.RepackagingEntryRepositoryController#getRepackagingEntriesByJob(com.gammon.qs.domain.Job)}.
	 */
	public Map<String, Object> testGetRepackagingEntriesByJob() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {});
		data.put("sql", "");
		post();
		return data;
	}

	/**
	 * Test data for {@link com.gammon.qs.web.RepackagingEntryRepositoryController#getRepackagingEntriesByJobNumber(java.lang.String)}.
	 */
	public Map<String, Object> testGetRepackagingEntriesByJobNumber() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {});
		data.put("sql", "");
		post();
		return data;
	}

	/**
	 * Test data for {@link com.gammon.qs.web.RepackagingEntryRepositoryController#saveRepackagingEntry(com.gammon.qs.domain.RepackagingEntry)}.
	 */
	public Map<String, Object> testSaveRepackagingEntry() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {});
		data.put("sql", "");
		post();
		return data;
	}

	/**
	 * Test data for {@link com.gammon.qs.web.RepackagingEntryRepositoryController#createRepackagingEntry(java.lang.Integer, com.gammon.qs.domain.Job, java.lang.String, java.lang.String)}.
	 */
	public Map<String, Object> testCreateRepackagingEntry() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {});
		data.put("sql", "");
		post();
		return data;
	}

	/**
	 * Test data for {@link com.gammon.qs.web.RepackagingEntryRepositoryController#removeRepackagingEntry(com.gammon.qs.domain.RepackagingEntry)}.
	 */
	public Map<String, Object> testRemoveRepackagingEntry() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {});
		data.put("sql", "");
		post();
		return data;
	}

	/**
	 * Test data for {@link com.gammon.qs.web.RepackagingEntryRepositoryController#getLatestRepackagingEntry(com.gammon.qs.domain.Job)}.
	 */
	public Map<String, Object> testGetLatestRepackagingEntry() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {});
		data.put("sql", "");
		post();
		return data;
	}

	/**
	 * Test data for {@link com.gammon.qs.web.RepackagingEntryRepositoryController#getRepackagingEntry(java.lang.Long)}.
	 */
	public Map<String, Object> testGetRepackagingEntry() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {});
		data.put("sql", "");
		post();
		return data;
	}

}
