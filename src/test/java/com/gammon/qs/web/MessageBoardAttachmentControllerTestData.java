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
//@PropertySource("file:${MessageBoardAttachmentControllerTestData.properties}")
public class MessageBoardAttachmentControllerTestData extends ControllerTestCase.TestDataBase {
	private Class<?> serviceClass = MessageBoardAttachmentController.class;

	/**
	 * Test data for {@link com.gammon.qs.web.MessageBoardAttachmentController#obtainAttachmentListByMessageID(long)}.
	 */
	public Map<String, Object> testObtainAttachmentListByMessageID() {
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
	 * Test data for {@link com.gammon.qs.web.MessageBoardAttachmentController#obtainAttachmentListByID(long)}.
	 */
	public Map<String, Object> testObtainAttachmentListByID() {
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
	 * Test data for {@link com.gammon.qs.web.MessageBoardAttachmentController#updateAttachments(java.util.List)}.
	 */
	public Map<String, Object> testUpdateAttachments() {
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
	 * Test data for {@link com.gammon.qs.web.MessageBoardAttachmentController#deleteAttachments(java.util.List)}.
	 */
	public Map<String, Object> testDeleteAttachments() {
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
