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
//@PropertySource("file:${SystemMessageControllerTestData.properties}")
public class SystemMessageControllerTestData extends ControllerTestCase.TestDataBase {
	private Class<?> serviceClass;// = SystemMessageController.class;

	/**
	 * Test data for {@link com.gammon.qs.web.SystemMessageController#getGlobalAlertMessage()}.
	 */
	public Map<String, Object> testGetGlobalAlertMessage() {
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
	 * Test data for {@link com.gammon.qs.web.SystemMessageController#logError(java.lang.Throwable)}.
	 */
	public Map<String, Object> testLogError() {
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
