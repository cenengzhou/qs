package com.gammon.junit.testcase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.hibernate.SessionFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.gammon.junit.testbase.TestBase;
import com.gammon.pcms.helper.JunitHelper;

@Configuration
@PropertySource("file:${BQResourceSummaryRepositoryControllerTestData.properties}")
public class LazyInitTestCase extends TestBase{

//	@Autowired
//	private ApplicationContext applicationContext;
	@Autowired
	private SessionFactory sessionFactory;
	
	String methodName;
	private void init(){
		methodName = JunitHelper.substringAfterUncapitalize(Thread.currentThread().getStackTrace()[2].getMethodName(),"test");
	}

	
	public LazyInitTestCase() {
		super("ControllerTest (LazyInit)", LazyInitTestCase.class.getSimpleName());
	}
	
	@Value("${BQResourceSummaryRepositoryControllerTestData.testSaveResourceSummary_jobNumber}")
	String testSaveResourceSummary_jobNumber;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testSaveResourceSummary_packageNo}")
	String testSaveResourceSummary_packageNo;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testSaveResourceSummary_objectCode}")
	String testSaveResourceSummary_objectCode;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testSaveResourceSummary_subsidiaryCode}")
	String testSaveResourceSummary_subsidiaryCode;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testSaveResourceSummary_resourceDescription}")
	String testSaveResourceSummary_resourceDescription;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testSaveResourceSummary_unit}")
	String testSaveResourceSummary_unit;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testSaveResourceSummary_rate}")
	String testSaveResourceSummary_rate;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testSaveResourceSummary_repackagingEntryId}")
	String testSaveResourceSummary_repackagingEntryId;
	@Value("${BQResourceSummaryRepositoryControllerTestData.testSaveResourceSummary_sql}")
	String testSaveResourceSummary_sql;
	/**
	 * Test method for {@link com.gammon.qs.web.BQResourceSummaryRepositoryController#saveResourceSummary(com.gammon.qs.domain.BQResourceSummary, java.lang.Long)}.
	 */
	@Test
	@Ignore
	public void testSaveResourceSummary() {
		init();
		resultMessage.setDescription("Error");
		//resultMessage.setClassName(BQResourceSummaryRepositoryController.class.getSimpleName());
		resultMessage.setMethodName(methodName);
		int resultSize = 0;
		//BQResourceSummaryRepositoryController bqResourceSummaryRepositoryController = applicationContext.getBean("bqResourceSummaryRepositoryController", BQResourceSummaryRepositoryController.class);
//		BQResourceSummaryService bqResourceSummaryService = applicationContext.getBean(BQResourceSummaryService.class);
//		BQResourceSummary bqResourceSummary = null;
//		BQResourceSummaryWrapper resultObject = null;
		try{
//			bqResourceSummary = bqResourceSummaryService.getResourceSummary(testSaveResourceSummary_jobNumber, testSaveResourceSummary_packageNo, testSaveResourceSummary_objectCode, testSaveResourceSummary_subsidiaryCode, testSaveResourceSummary_resourceDescription, testSaveResourceSummary_unit, new Double(testSaveResourceSummary_rate));
//			 resultObject = bqResourceSummaryRepositoryController.saveResourceSummary(bqResourceSummary, new Long(testSaveResourceSummary_repackagingEntryId));
//			 resultSize = resultObject.getResourceSummaries().size();
		} catch (Exception e){ 
			e.printStackTrace();
			resultMessage.setDescription(e.toString());
			fail(e.toString());
		}	
		resultMessage.setStatus(JunitHelper.RESULT_STATUS_RESULT_NOERROR);
		@SuppressWarnings("rawtypes")
		List sqlList = sessionFactory.getCurrentSession().createSQLQuery(testSaveResourceSummary_sql).list();
		
		resultMessage.setDescription(sqlList.get(0) + " != " + resultSize);
		assertEquals(sqlList.get(0) + " != " + resultSize, Integer.parseInt(sqlList.get(0).toString()), resultSize);
		resultMessage.setDescription("No. of result return:" + resultSize);
		resultMessage.setStatus(JunitHelper.RESULT_STATUS_VERIFIED);
		
	}
	
}
