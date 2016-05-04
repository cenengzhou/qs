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
//@PropertySource("file:${QrtzTriggerControllerTestData.properties}")
public class QrtzTriggerControllerTestData extends ControllerTestCase.TestDataBase {
	private Class<?> serviceClass = QrtzTriggerController.class;

	/**
	 * Test data for {@link com.gammon.qs.web.QrtzTriggerController#getAllTriggers()}.
	 */
	public Map<String, Object> testGetAllTriggers() {
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
	 * Test data for {@link com.gammon.qs.web.QrtzTriggerController#updateQrtzTriggerList(java.util.List)}.
	 */
	public Map<String, Object> testUpdateQrtzTriggerList() {
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
	 * Test data for {@link com.gammon.qs.web.QrtzTriggerController#getCronTrigger(java.lang.String, java.lang.String)}.
	 */
	public Map<String, Object> testGetCronTrigger() {
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
	 * Test data for {@link com.gammon.qs.web.QrtzTriggerController#updateCronTrigger(com.gammon.qs.application.CronTrigger)}.
	 */
	public Map<String, Object> testUpdateCronTrigger() {
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
