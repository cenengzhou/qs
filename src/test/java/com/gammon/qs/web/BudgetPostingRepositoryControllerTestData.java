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
@PropertySource("file:${BudgetPostingRepositoryControllerTestData.properties}")
public class BudgetPostingRepositoryControllerTestData extends ControllerTestCase.TestDataBase {
	private Class<?> serviceClass = BudgetPostingRepositoryController.class;
	
	@Value("${BQResourceSummaryRepositoryControllerTestData.testPostBudget_jobNumber}")
	String testPostBudget_jobNumber;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testPostBudget_username}")
	String testPostBudget_username;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testPostBudget_sql}")
	String testPostBudget_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.BudgetPostingRepositoryController#postBudget(java.lang.String, java.lang.String)}.
	 */
	public Map<String, Object> testPostBudget() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {testPostBudget_jobNumber, testPostBudget_username});
		data.put("sql", testPostBudget_sql);
		post();
		return data;
	}

}
