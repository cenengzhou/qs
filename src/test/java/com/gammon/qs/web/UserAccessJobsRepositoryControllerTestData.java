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
//@PropertySource("file:${UserAccessJobsRepositoryControllerTestData.properties}")
public class UserAccessJobsRepositoryControllerTestData extends ControllerTestCase.TestDataBase {
	private Class<?> serviceClass = UserAccessJobsRepositoryController.class;

	/**
	 * Test data for {@link com.gammon.qs.web.UserAccessJobsRepositoryController#canAccessJob(java.lang.String, java.lang.String)}.
	 */
	public Map<String, Object> testCanAccessJob() {
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
