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
//@PropertySource("file:${UnitRepositoryControllerTestData.properties}")
public class UnitRepositoryControllerTestData extends ControllerTestCase.TestDataBase {
	private Class<?> serviceClass;// = UnitRepositoryController.class;

	/**
	 * Test data for {@link com.gammon.qs.web.UnitRepositoryController#getUnitOfMeasurementList()}.
	 */
	public Map<String, Object> testGetUnitOfMeasurementList() {
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
	 * Test data for {@link com.gammon.qs.web.UnitRepositoryController#getAppraisalPerformanceGroupMap()}.
	 */
	public Map<String, Object> testGetAppraisalPerformanceGroupMap() {
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
	 * Test data for {@link com.gammon.qs.web.UnitRepositoryController#getUDCMap(java.lang.String, java.lang.String)}.
	 */
	public Map<String, Object> testGetUDCMap() {
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
	 * Test data for {@link com.gammon.qs.web.UnitRepositoryController#getSCStatusCodeMap()}.
	 */
	public Map<String, Object> testGetSCStatusCodeMap() {
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
	 * Test data for {@link com.gammon.qs.web.UnitRepositoryController#getAllWorkScopes()}.
	 */
	public Map<String, Object> testGetAllWorkScopes() {
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
