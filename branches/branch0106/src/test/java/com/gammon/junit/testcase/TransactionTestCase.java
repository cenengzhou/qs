/**
 * 
 */
package com.gammon.junit.testcase;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import java.lang.reflect.Method;
import java.util.Collection;
import org.hibernate.SessionFactory;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;
import com.gammon.junit.testbase.TestBase;
import com.gammon.junit.testsuite.TransactionTestSuite;
import com.gammon.pcms.helper.JunitHelper;

/**
 * @author paulnpyiu
 *
 */
@RunWith(Parameterized.class)
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
@DirtiesContext(classMode=DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) 
public class TransactionTestCase extends TestBase {

	@Autowired
	SessionFactory sessionFactory;
	@ClassRule
	public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();
	@Rule
	public final SpringMethodRule springMethodRule = new SpringMethodRule();
	private static TestContextManager testContextManager = new TestContextManager(TransactionTestCase.class);
	private Class<?> serviceClass;
	private String methodName;
	private Object beforeTestCaseData, afterTestCaseData;
	private TransactionTestCase.TestDataMethod testDataMethod;
	private String assertDescription;
	private Object bean;

	public TransactionTestCase(Class<?> testClass, String testMethod) {
		super("TransactionTest", TransactionTestCase.class.getSimpleName());
		assertDescription = "Error";
		beforeTestCaseData = null;
		testDataMethod = null;
		afterTestCaseData = null;
		serviceClass = testClass;
		methodName = testMethod;
		try {
			testContextManager.prepareTestInstance(this);
			bean = applicationContext.getBean(testClass);
			Method m = bean.getClass().getMethod(testMethod, new Class[0]);
			testDataMethod = (TestDataMethod) m.invoke(bean, new Object[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	@BeforeTransaction
	public void beforeTransaction(){
		super.beforeTransaction();
		beforeTestCaseData = testDataMethod.obtainDataForCompare();
		
		assertDescription = "beforeTestCaseData is null";
		resultMessage.setDescription(assertDescription);
		assertNotNull(assertDescription, beforeTestCaseData);
	}
	
	@Override
	@AfterTransaction
	public void afterTransaction(){
		session = JunitHelper.startSession();
		request = JunitHelper.startRequest(session);
		response = new MockHttpServletResponse();
		
		afterTestCaseData = testDataMethod.obtainDataForCompare();
		
		
		if(beforeTestCaseData.equals(afterTestCaseData)){
			assertDescription = "Rollback successful";
			resultMessage.setStatus(JunitHelper.RESULT_STATUS_VERIFIED);
		}else{
			assertDescription = "beforeTestCaseData not equals with afterTestCaseData";
			if(afterTestCaseData == null) assertDescription = "afterTestCaseData is null";
			resultMessage.setStatus(JunitHelper.RESULT_STATUS_FAIL);
		}
		
		resultMessage.setDescription(assertDescription);
		
		super.afterTransaction();
		assertEquals(assertDescription, JunitHelper.RESULT_STATUS_VERIFIED, resultMessage.getStatus());
	}
	
	@Test
	public void testTransactional() {
		resultMessage.setDescription(assertDescription);
		resultMessage.setClassName(serviceClass.getSimpleName());
		resultMessage.setMethodName(methodName);

		testDataMethod.executeTestMethod();
	}
	
	@SuppressWarnings("rawtypes")
	@Parameterized.Parameters(name = "TestData:{0}.{1}")
	public static Collection getTestData() {
		return TransactionTestSuite.getTestData();
	}
	
	/**
	 * base class for ServiceTestData
	 * @author paulyiu
	 *
	 */
	public static class TestDataBase{
		@Autowired
		protected ApplicationContext applicationContext;
	}
	
	/**
	 * return type of ServiceTestData 
	 * @author paulyiu
	 *
	 */
	public static interface TestDataMethod {
		
		/**
		 * return the object for modify
		 * @return
		 */
		Object obtainObjectFromDB();
		
		/**
		 * Return test data from service
		 * used for @BeforeTransaction and @AfterTransaction
		 * to compare data
		 * @return
		 */
		Object obtainDataForCompare();
		
		/**
		 * call service method to modify data
		 * used for @Test
		 */
		void executeTestMethod();
	}
}
