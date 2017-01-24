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
//@PropertySource("file:${UserAccessRightsRepositoryControllerTestData.properties}")
public class UserAccessRightsRepositoryControllerTestData extends ControllerTestCase.TestDataBase {
	private Class<?> serviceClass;// = UserAccessRightsRepositoryController.class;

	/**
	 * Test data for {@link com.gammon.qs.web.UserAccessRightsRepositoryController#getAccessRights(java.lang.String, java.lang.String)}.
	 */
	public Map<String, Object> testGetAccessRights() {
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
	 * Test data for {@link com.gammon.qs.web.UserAccessRightsRepositoryController#obtainAccessRightsByUserLists(java.util.List, java.lang.String)}.
	 */
	public Map<String, Object> testObtainAccessRightsByUserLists() {
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
