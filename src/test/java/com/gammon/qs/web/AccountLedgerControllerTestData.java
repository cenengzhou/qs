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
@PropertySource("file:${AccountLedgerControllerTestData.properties}")
public class AccountLedgerControllerTestData extends ControllerTestCase.TestDataBase {
	private Class<?> serviceClass = AccountLedgerController.class;
	
	@Value("${AccountLedgerControllerTestData.testObtainAccountLedgersByJobNo_jobNo}")
	String testObtainAccountLedgersByJobNo_jobNo;
	@Value("${AccountLedgerControllerTestData.testObtainAccountLedgersByJobNo_month}")
	String testObtainAccountLedgersByJobNo_month;
	@Value("${AccountLedgerControllerTestData.testObtainAccountLedgersByJobNo_year}")
	String testObtainAccountLedgersByJobNo_year;
	@Value("${AccountLedgerControllerTestData.testObtainAccountLedgersByJobNo_sql}")
	String testObtainAccountLedgersByJobNo_sql;
	@Value("${AccountLedgerControllerTestData.testObtainAccountLedgersByJobNo_sqlfilename}")
	String testObtainAccountLedgersByJobNo_sqlfilename;
	/**
	 * Test data for {@link com.gammon.qs.web.AccountLedgerController#obtainAccountLedgersByJobNo(java.lang.String, java.lang.Integer, java.lang.Integer)}.
	 */
	public Map<String, Object> testObtainAccountLedgersByJobNo() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql | sqlFileName}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {testObtainAccountLedgersByJobNo_jobNo, new Integer(testObtainAccountLedgersByJobNo_month), new Integer(testObtainAccountLedgersByJobNo_year)});
		data.put("sql", testObtainAccountLedgersByJobNo_sql);
		//if sqlFileName is not null then it's content will replace the value of 'sql'
		data.put("sqlFileName", testObtainAccountLedgersByJobNo_sqlfilename);
		post();
		return data;
	}

	@Value("${AccountLedgerControllerTestData.testObtainAccountLedgersByLedgerType_jobNo}")
	String testObtainAccountLedgersByLedgerType_jobNo;
	@Value("${AccountLedgerControllerTestData.testObtainAccountLedgersByLedgerType_ledgerType}")
	String testObtainAccountLedgersByLedgerType_ledgerType;
	@Value("${AccountLedgerControllerTestData.testObtainAccountLedgersByLedgerType_sql}")
	String testObtainAccountLedgersByLedgerType_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.AccountLedgerController#obtainAccountLedgersByLedgerType(java.lang.String, java.lang.String)}.
	 */
	public Map<String, Object> testObtainAccountLedgersByLedgerType() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {testObtainAccountLedgersByLedgerType_jobNo, testObtainAccountLedgersByLedgerType_ledgerType});
		data.put("sql", testObtainAccountLedgersByLedgerType_sql);
		post();
		return data;
	}

}
