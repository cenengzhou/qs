/**
 * 
 */
package com.gammon.qs.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
@PropertySource("file:${IVPostingHistoryControllerTestData.properties}")
public class IVPostingHistoryControllerTestData extends ControllerTestCase.TestDataBase {
	private Class<?> serviceClass;// = IVPostingHistoryController.class;
	
	@Value("${IVPostingHistoryControllerTestData.testObtainIVPostingHistory_jobNumber}")
	String testObtainIVPostingHistory_jobNumber;
	@Value("${IVPostingHistoryControllerTestData.testObtainIVPostingHistory_packageNo}")
	String testObtainIVPostingHistory_packageNo;
	@Value("${IVPostingHistoryControllerTestData.testObtainIVPostingHistory_objectCode}")
	String testObtainIVPostingHistory_objectCode;
	@Value("${IVPostingHistoryControllerTestData.testObtainIVPostingHistory_subsidiaryCode}")
	String testObtainIVPostingHistory_subsidiaryCode;
	@Value("${IVPostingHistoryControllerTestData.testObtainIVPostingHistory_fromDate}")
	String testObtainIVPostingHistory_fromDate;
	@Value("${IVPostingHistoryControllerTestData.testObtainIVPostingHistory_toDate}")
	String testObtainIVPostingHistory_toDate;
	@Value("${IVPostingHistoryControllerTestData.testObtainIVPostingHistory_sql}")
	String testObtainIVPostingHistory_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.IVPostingHistoryController#obtainIVPostingHistory(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Date, java.util.Date)}.
	 */
	public Map<String, Object> testObtainIVPostingHistory() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		Date fromDate = null;
		Date toDate = null;
		try {
			fromDate = new SimpleDateFormat("yyyy/MM/dd").parse(testObtainIVPostingHistory_fromDate);
			toDate = new SimpleDateFormat("yyyy/MM/dd").parse(testObtainIVPostingHistory_toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {testObtainIVPostingHistory_jobNumber, testObtainIVPostingHistory_packageNo, testObtainIVPostingHistory_objectCode, testObtainIVPostingHistory_subsidiaryCode, fromDate, toDate});
		data.put("sql", testObtainIVPostingHistory_sql);
		post();
		return data;
	}

	@Value("${IVPostingHistoryControllerTestData.testGetIVPostingHistoryByPage_pageNum}")
	String testGetIVPostingHistoryByPage_pageNum;
	@Value("${IVPostingHistoryControllerTestData.testGetIVPostingHistoryByPage_jobNumber}")
	String testGetIVPostingHistoryByPage_jobNumber;
	@Value("${IVPostingHistoryControllerTestData.testGetIVPostingHistoryByPage_packageNo}")
	String testGetIVPostingHistoryByPage_packageNo;
	@Value("${IVPostingHistoryControllerTestData.testGetIVPostingHistoryByPage_objectCode}")
	String testGetIVPostingHistoryByPage_objectCode;
	@Value("${IVPostingHistoryControllerTestData.testGetIVPostingHistoryByPage_subsidiaryCode}")
	String testGetIVPostingHistoryByPage_subsidiaryCode;
	@Value("${IVPostingHistoryControllerTestData.testGetIVPostingHistoryByPage_fromDate}")
	String testGetIVPostingHistoryByPage_fromDate;
	@Value("${IVPostingHistoryControllerTestData.testGetIVPostingHistoryByPage_toDate}")
	String testGetIVPostingHistoryByPage_toDate;
	@Value("${IVPostingHistoryControllerTestData.testGetIVPostingHistoryByPage_sql}")
	String testGetIVPostingHistoryByPage_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.IVPostingHistoryController#getIVPostingHistoryByPage(int)}.
	 */
	public void initGetIVPostingHistoryByPage(){
//		IVPostingHistoryController ivPostingHistoryController = applicationContext.getBean("ivPostingHistoryController", IVPostingHistoryController.class);
		try {
//			ivPostingHistoryController.obtainIVPostingHistory(testGetIVPostingHistoryByPage_jobNumber, testGetIVPostingHistoryByPage_packageNo, testGetIVPostingHistoryByPage_objectCode, testGetIVPostingHistoryByPage_subsidiaryCode, new Date(testGetIVPostingHistoryByPage_fromDate), new Date(testGetIVPostingHistoryByPage_toDate));
		} catch (Exception e) {e.printStackTrace();
		}
	}
	public Map<String, Object> testGetIVPostingHistoryByPage() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {new Integer(testGetIVPostingHistoryByPage_pageNum)});
		data.put("clazz", new Class<?>[]{int.class});
		data.put("initMethod", "initGetIVPostingHistoryByPage");
		data.put("sql", testGetIVPostingHistoryByPage_sql);
		post();
		return data;
	}

	@Value("${IVPostingHistoryControllerTestData.testClearCache_sql}")
	String testClearCache_sql;
	/**
	 * Test data for {@link com.gammon.qs.web.IVPostingHistoryController#clearCache()}.
	 */
	public Map<String, Object> testClearCache() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[] {});
		data.put("sql", testClearCache_sql);
		post();
		return data;
	}

}
