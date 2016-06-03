/**
 * 
 */
package com.gammon.qs.web;

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
@PropertySource("file:${EnvironmentConfigControllerTestData.properties}")
public class EnvironmentConfigControllerTestData extends ControllerTestCase.TestDataBase {
	private Class<?> serviceClass;// = EnvironmentConfigController.class;
	
	@Value("${EnvironmentConfigControllerTestData.testGetApprovalSystemPath_sql}")
	String testGetApprovalSystemPath_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.EnvironmentConfigController#getApprovalSystemPath()}.
	 */
	public Map<String, Object> testGetApprovalSystemPath() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {});
		data.put("sql", testGetApprovalSystemPath_sql);
		post();
		return data;
	}

}
