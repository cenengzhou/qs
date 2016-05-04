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
@PropertySource("file:${AttachmentControllerTestData.properties}")
public class AttachmentControllerTestData extends ControllerTestCase.TestDataBase {
	private Class<?> serviceClass = AttachmentController.class;
	
	@Value("${AttachmentControllerTestData.testAddRepackagingTextAttachment_repackagingEntryID}")
	String testAddRepackagingTextAttachment_repackagingEntryID;
	@Value("${AttachmentControllerTestData.testAddRepackagingTextAttachment_sequenceNo}")
	String testAddRepackagingTextAttachment_sequenceNo;
	@Value("${AttachmentControllerTestData.testAddRepackagingTextAttachment_fileName}")
	String testAddRepackagingTextAttachment_fileName;
	@Value("${AttachmentControllerTestData.testAddRepackagingTextAttachment_sql}")
	String testAddRepackagingTextAttachment_sql;
	/**
	 * Test data for
	 * {@link com.gammon.qs.web.AttachmentController#addRepackagingTextAttachment(java.lang.Long, java.lang.Integer, java.lang.String)}
	 * .
	 */
	public Map<String, Object> testAddRepackagingTextAttachment() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[]{new Long(testAddRepackagingTextAttachment_repackagingEntryID),new Integer(testAddRepackagingTextAttachment_sequenceNo),testAddRepackagingTextAttachment_fileName});
		data.put("sql", testAddRepackagingTextAttachment_sql);
		post();
		return data;
	}

	@Value("${AttachmentControllerTestData.testDeleteRepackagingAttachment_repackagingEntryID}")
	String testDeleteRepackagingAttachment_repackagingEntryID;
	@Value("${AttachmentControllerTestData.testDeleteRepackagingAttachment_sequenceNo}")
	String testDeleteRepackagingAttachment_sequenceNo;
	@Value("${AttachmentControllerTestData.testDeleteRepackagingAttachment_sql}")
	String testDeleteRepackagingAttachment_sql;
	/**
	 * Test data for
	 * {@link com.gammon.qs.web.AttachmentController#deleteRepackagingAttachment(java.lang.Long, java.lang.Integer)}
	 * .
	 */
	public Map<String, Object> testDeleteRepackagingAttachment() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[]{new Long(testDeleteRepackagingAttachment_repackagingEntryID), new Integer(testDeleteRepackagingAttachment_sequenceNo)});
		data.put("sql", testDeleteRepackagingAttachment_sql);
		post();
		return data;
	}

	@Value("${AttachmentControllerTestData.testGetRepackagingTextAttachment_repackagingEntryID}")
	String testGetRepackagingTextAttachment_repackagingEntryID;
	@Value("${AttachmentControllerTestData.testGetRepackagingTextAttachment_sequenceNo}")
	String testGetRepackagingTextAttachment_sequenceNo;
	@Value("${AttachmentControllerTestData.testGetRepackagingTextAttachment_sql}")
	String testGetRepackagingTextAttachment_sql;
	/**
	 * Test data for
	 * {@link com.gammon.qs.web.AttachmentController#getRepackagingTextAttachment(java.lang.Long, java.lang.Integer)}
	 * .
	 */
	public Map<String, Object> testGetRepackagingTextAttachment() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[]{new Long(testGetRepackagingTextAttachment_repackagingEntryID), new Integer(testGetRepackagingTextAttachment_sequenceNo)});
		data.put("sql", testGetRepackagingTextAttachment_sql);
		post();
		return data;
	}

	@Value("${AttachmentControllerTestData.testGetRepackagingAttachments_repackagingEntryID}")
	String testGetRepackagingAttachments_repackagingEntryID;
	@Value("${AttachmentControllerTestData.testGetRepackagingAttachments_sql}")
	String testGetRepackagingAttachments_sql;
	/**
	 * Test data for
	 * {@link com.gammon.qs.web.AttachmentController#getRepackagingAttachments(java.lang.Long)}
	 * .
	 */
	public Map<String, Object> testGetRepackagingAttachments() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);		data.put("params", new Object[]{new Long(testGetRepackagingAttachments_repackagingEntryID)});
		data.put("sql",testGetRepackagingAttachments_sql);
		post();
		return data;
	}

	@Value("${AttachmentControllerTestData.testSaveRepackagingTextAttachment_repackagingEntryID}")
	String testSaveRepackagingTextAttachment_repackagingEntryID;
	@Value("${AttachmentControllerTestData.testSaveRepackagingTextAttachment_sequenceNo}")
	String testSaveRepackagingTextAttachment_sequenceNo;
	@Value("${AttachmentControllerTestData.testSaveRepackagingTextAttachment_content}")
	String testSaveRepackagingTextAttachment_content;
	@Value("${AttachmentControllerTestData.testSaveRepackagingTextAttachment_sql}")
	String testSaveRepackagingTextAttachment_sql;
	/**
	 * Test data for
	 * {@link com.gammon.qs.web.AttachmentController#saveRepackagingTextAttachment(java.lang.Long, java.lang.Integer, java.lang.String)}
	 * .
	 */
	public Map<String, Object> testSaveRepackagingTextAttachment() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[]{new Long(testSaveRepackagingTextAttachment_repackagingEntryID),new Integer(testSaveRepackagingTextAttachment_sequenceNo),testSaveRepackagingTextAttachment_content});
		data.put("sql", testSaveRepackagingTextAttachment_sql);
		post();
		return data;
	}

	@Value("${AttachmentControllerTestData.testGetMainCertTextAttachment_jobNumber}")
	String testGetMainCertTextAttachment_jobNumber;
	@Value("${AttachmentControllerTestData.testGetMainCertTextAttachment_mainCertNumber}")
	String testGetMainCertTextAttachment_mainCertNumber;
	@Value("${AttachmentControllerTestData.testGetMainCertTextAttachment_sequenceNo}")
	String testGetMainCertTextAttachment_sequenceNo;
	@Value("${AttachmentControllerTestData.testGetMainCertTextAttachment_sql}")
	String testGetMainCertTextAttachment_sql;
	/**
	 * Test data for
	 * {@link com.gammon.qs.web.AttachmentController#getMainCertTextAttachment(java.lang.String, java.lang.Integer, java.lang.Integer)}
	 * .
	 */
	public Map<String, Object> testGetMainCertTextAttachment() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[]{testGetMainCertTextAttachment_jobNumber,new Integer(testGetMainCertTextAttachment_mainCertNumber),new Integer(testGetMainCertTextAttachment_sequenceNo)});
		data.put("sql",testGetMainCertTextAttachment_sql);
		post();
		return data;
	}

	@Value("${AttachmentControllerTestData.testAddMainCertTextAttachment_jobNumber}")
	String testAddMainCertTextAttachment_jobNumber;
	@Value("${AttachmentControllerTestData.testAddMainCertTextAttachment_mainCertNumber}")
	String testAddMainCertTextAttachment_mainCertNumber;
	@Value("${AttachmentControllerTestData.testAddMainCertTextAttachment_sequenceNo}")
	String testAddMainCertTextAttachment_sequenceNo;
	@Value("${AttachmentControllerTestData.testAddMainCertTextAttachment_fileName}")
	String testAddMainCertTextAttachment_fileName;
	@Value("${AttachmentControllerTestData.testAddMainCertTextAttachment_sql}")
	String testAddMainCertTextAttachment_sql;
	/**
	 * Test data for
	 * {@link com.gammon.qs.web.AttachmentController#addMainCertTextAttachment(java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String)}
	 * .
	 */
	public Map<String, Object> testAddMainCertTextAttachment() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[]{testAddMainCertTextAttachment_jobNumber, new Integer(testAddMainCertTextAttachment_mainCertNumber), new Integer(testAddMainCertTextAttachment_sequenceNo), testAddMainCertTextAttachment_fileName});
		data.put("sql", testAddMainCertTextAttachment_sql);
		post();
		return data;
	}

	@Value("${AttachmentControllerTestData.testSaveMainCertTextAttachment_jobNumber}")
	String testSaveMainCertTextAttachment_jobNumber;
	@Value("${AttachmentControllerTestData.testSaveMainCertTextAttachment_mainCertNumber}")
	String testSaveMainCertTextAttachment_mainCertNumber;
	@Value("${AttachmentControllerTestData.testSaveMainCertTextAttachment_sequenceNo}")
	String testSaveMainCertTextAttachment_sequenceNo;
	@Value("${AttachmentControllerTestData.testSaveMainCertTextAttachment_text}")
	String testSaveMainCertTextAttachment_text;
	@Value("${AttachmentControllerTestData.testSaveMainCertTextAttachment_sql}")
	String testSaveMainCertTextAttachment_sql;
	/**
	 * Test data for
	 * {@link com.gammon.qs.web.AttachmentController#saveMainCertTextAttachment(java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String)}
	 * .
	 */
	public Map<String, Object> testSaveMainCertTextAttachment() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[]{testSaveMainCertTextAttachment_jobNumber, new Integer(testSaveMainCertTextAttachment_mainCertNumber), new Integer(testSaveMainCertTextAttachment_sequenceNo), testSaveMainCertTextAttachment_text});
		data.put("sql", testSaveMainCertTextAttachment_sql);
		post();
		return data;
	}

	@Value("${AttachmentControllerTestData.testDeleteMainCertAttachment_jobNumber}")
	String testDeleteMainCertAttachment_jobNumber;
	@Value("${AttachmentControllerTestData.testDeleteMainCertAttachment_mainCertNumber}")
	String testDeleteMainCertAttachment_mainCertNumber;
	@Value("${AttachmentControllerTestData.testDeleteMainCertAttachment_sequenceNo}")
	String testDeleteMainCertAttachment_sequenceNo;
	@Value("${AttachmentControllerTestData.testDeleteMainCertAttachment_sql}")
	String testDeleteMainCertAttachment_sql;
	/**
	 * Test data for
	 * {@link com.gammon.qs.web.AttachmentController#deleteMainCertAttachment(java.lang.String, java.lang.Integer, java.lang.Integer)}
	 * .
	 */
	public Map<String, Object> testDeleteMainCertAttachment() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[]{testDeleteMainCertAttachment_jobNumber, new Integer(testDeleteMainCertAttachment_mainCertNumber), new Integer(testDeleteMainCertAttachment_sequenceNo)});
		data.put("sql", testDeleteMainCertAttachment_sql);
		post();
		return data;
	}

	@Value("${AttachmentControllerTestData.testDeleteAttachment_nameObject}")
	String testDeleteAttachment_nameObject;
	@Value("${AttachmentControllerTestData.testDeleteAttachment_textKey}")
	String testDeleteAttachment_textKey;
	@Value("${AttachmentControllerTestData.testDeleteAttachment_sequenceNumber}")
	String testDeleteAttachment_sequenceNumber;
	@Value("${AttachmentControllerTestData.testDeleteAttachment_sql}")
	String testDeleteAttachment_sql;
	/**
	 * Test data for
	 * {@link com.gammon.qs.web.AttachmentController#deleteAttachment(java.lang.String, java.lang.String, java.lang.Integer)}
	 * .
	 */
	public Map<String, Object> testDeleteAttachment() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[]{testDeleteAttachment_nameObject, testDeleteAttachment_textKey, new Integer(testDeleteAttachment_sequenceNumber)});
		data.put("sql", testDeleteAttachment_sql);
		post();
		return data;
	}

	@Value("${AttachmentControllerTestData.testGetAttachmentList_nameObject}")
	String testGetAttachmentList_nameObject;
	@Value("${AttachmentControllerTestData.testGetAttachmentList_textKey}")
	String testGetAttachmentList_textKey;
	@Value("${AttachmentControllerTestData.testGetAttachmentList_sql}")
	String testGetAttachmentList_sql;
	/**
	 * Test data for
	 * {@link com.gammon.qs.web.AttachmentController#getAttachmentList(java.lang.String, java.lang.String)}
	 * .
	 */
	public Map<String, Object> testGetAttachmentList() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[]{testGetAttachmentList_nameObject,testGetAttachmentList_textKey});
		data.put("sql", testGetAttachmentList_sql);
		post();
		return data;
	}

	@Value("${AttachmentControllerTestData.testGetTextAttachmentContent_nameObject}")
	String testGetTextAttachmentContent_nameObject;
	@Value("${AttachmentControllerTestData.testGetTextAttachmentContent_textKey}")
	String testGetTextAttachmentContent_textKey;
	@Value("${AttachmentControllerTestData.testGetTextAttachmentContent_sequenceNumber}")
	String testGetTextAttachmentContent_sequenceNumber;
	@Value("${AttachmentControllerTestData.testGetTextAttachmentContent_sql}")
	String testGetTextAttachmentContent_sql;
	/**
	 * Test data for
	 * {@link com.gammon.qs.web.AttachmentController#getTextAttachmentContent(java.lang.String, java.lang.String, java.lang.Integer)}
	 * .
	 */
	public Map<String, Object> testGetTextAttachmentContent() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[]{testGetTextAttachmentContent_nameObject, testGetTextAttachmentContent_textKey, new Integer(testGetTextAttachmentContent_sequenceNumber)});
		data.put("sql", testGetTextAttachmentContent_sql);
		post();
		return data;
	}

	@Value("${AttachmentControllerTestData.testUploadTextAttachment_nameObject}")
	String testUploadTextAttachment_nameObject;
	@Value("${AttachmentControllerTestData.testUploadTextAttachment_textKey}")
	String testUploadTextAttachment_textKey;
	@Value("${AttachmentControllerTestData.testUploadTextAttachment_sequenceNo}")
	String testUploadTextAttachment_sequenceNo;
	@Value("${AttachmentControllerTestData.testUploadTextAttachment_textContent}")
	String testUploadTextAttachment_textContent;
	@Value("${AttachmentControllerTestData.testUploadTextAttachment_createdUser}")
	String testUploadTextAttachment_createdUser;
	@Value("${AttachmentControllerTestData.testUploadTextAttachment_sql}")
	String testUploadTextAttachment_sql;
	/**
	 * Test data for
	 * {@link com.gammon.qs.web.AttachmentController#uploadTextAttachment(java.lang.String, java.lang.String, java.lang.Integer, java.lang.String, java.lang.String)}
	 * .
	 */
	public Map<String, Object> testUploadTextAttachment() {
		init();
		// {serviceClass, methodName, parameters[], clazz[], initMethod, sql}
		data.put("serviceClass", serviceClass);
		data.put("methodName", methodName);
		data.put("params", new Object[]{testUploadTextAttachment_nameObject, testUploadTextAttachment_textKey, new Integer(testUploadTextAttachment_sequenceNo), testUploadTextAttachment_textContent, testUploadTextAttachment_createdUser});
		data.put("sql", testUploadTextAttachment_sql);
		post();
		return data;
	}

}
