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
@PropertySource("file:${PropertiesControllerTestData.properties}")
public class PropertiesRepositoryControllerTestData extends ControllerTestCase.TestDataBase {
	private Class<?> serviceClass = PropertiesRepositoryController.class;

	@Value("${PropertiesControllerTestData.obtainMailReceiverAddress_sql}")
	String obtainMailReceiverAddress_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.PropertiesRepositoryController#obtainMailReceiverAddress()}.
	 */
	public Map<String, Object> testObtainMailReceiverAddress() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[0]);
		data.put("sql", obtainMailReceiverAddress_sql);
		post();
		return data;
	}

	@Value("${PropertiesControllerTestData.updateMailReceiverAddress_mailAddress}")
	String updateMailReceiverAddress_mailAddress;
	@Value("${PropertiesControllerTestData.updateMailReceiverAddress_sql}")
	String updateMailReceiverAddress_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.PropertiesRepositoryController#updateMailReceiverAddress(java.lang.String)}.
	 */
	public Map<String, Object> testUpdateMailReceiverAddress() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[]{updateMailReceiverAddress_mailAddress});
		data.put("sql", updateMailReceiverAddress_sql);
		post();
		return data;
	}

}
