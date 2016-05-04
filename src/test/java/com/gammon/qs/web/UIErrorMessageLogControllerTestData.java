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
//@PropertySource("file:${UIErrorMessageLogControllerTestData.properties}")
public class UIErrorMessageLogControllerTestData extends ControllerTestCase.TestDataBase {
	private Class<?> serviceClass = UIErrorMessageLogController.class;

	/**
	 * Test data for {@link com.gammon.qs.web.UIErrorMessageLogController#logError(java.lang.String, java.lang.Throwable)}.
	 */
	public Map<String, Object> testLogErrorStringThrowable() {
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
	 * Test data for {@link com.gammon.qs.web.UIErrorMessageLogController#logError(java.lang.Throwable)}.
	 */
	public Map<String, Object> testLogErrorThrowable() {
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
