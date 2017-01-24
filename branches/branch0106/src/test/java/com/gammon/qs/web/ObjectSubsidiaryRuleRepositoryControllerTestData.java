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
//@PropertySource("file:${ObjectSubsidiaryRuleRepositoryControllerTestData.properties}")
public class ObjectSubsidiaryRuleRepositoryControllerTestData extends ControllerTestCase.TestDataBase {
	private Class<?> serviceClass;// = ObjectSubsidiaryRuleRepositoryController.class;

	/**
	 * Test data for {@link com.gammon.qs.web.ObjectSubsidiaryRuleRepositoryController#findObjectSubsidiaryRuleByPage(java.lang.Integer, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	public Map<String, Object> testFindObjectSubsidiaryRuleByPage() {
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
	 * Test data for {@link com.gammon.qs.web.ObjectSubsidiaryRuleRepositoryController#createObjectSubsidiaryRule(com.gammon.qs.domain.ObjectSubsidiaryRule)}.
	 */
	public Map<String, Object> testCreateObjectSubsidiaryRule() {
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
	 * Test data for {@link com.gammon.qs.web.ObjectSubsidiaryRuleRepositoryController#updateObjectSubsidiaryRule(com.gammon.qs.domain.ObjectSubsidiaryRule, com.gammon.qs.domain.ObjectSubsidiaryRule)}.
	 */
	public Map<String, Object> testUpdateObjectSubsidiaryRule() {
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
	 * Test data for {@link com.gammon.qs.web.ObjectSubsidiaryRuleRepositoryController#getObjectSubsidiaryRule(com.gammon.qs.domain.ObjectSubsidiaryRule)}.
	 */
	public Map<String, Object> testGetObjectSubsidiaryRule() {
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
	 * Test data for {@link com.gammon.qs.web.ObjectSubsidiaryRuleRepositoryController#updateMultipleObjectSubsidiaryRules(java.util.List)}.
	 */
	public Map<String, Object> testUpdateMultipleObjectSubsidiaryRules() {
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
