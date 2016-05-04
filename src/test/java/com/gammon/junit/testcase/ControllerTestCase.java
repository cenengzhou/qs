package com.gammon.junit.testcase;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.junit.testbase.TestBase;
import com.gammon.junit.testsuite.ControllerTestSuite;
import com.gammon.pcms.helper.JunitHelper;
import com.gammon.qs.wrapper.PaginationWrapper;

/**
 * @author paulnpyiu
 *
 */
@RunWith(Parameterized.class)
@Rollback
@Transactional(rollbackFor = Exception.class)
@DirtiesContext(classMode=DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) 
public class ControllerTestCase extends TestBase {

	@Autowired
	SessionFactory sessionFactory;

	@ClassRule
	public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

	@Rule
	public final SpringMethodRule springMethodRule = new SpringMethodRule();

	private static TestContextManager testContextManager = new TestContextManager(ControllerTestCase.class);
	@SuppressWarnings("unused")
	private Class<?> testClass;
	@SuppressWarnings("unused")
	private String testMethod;

	private Class<?> serviceClass;
	private String methodName;
	private String initMethod;
	private Object[] parameters;
	private Class<?>[] clazz;
	private String sql;
	private String sqlFileName;
	private Object bean;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ControllerTestCase(Class<?> testClass, String testMethod) {
		super("ControllerTest", ControllerTestCase.class.getSimpleName());

		Map<String, Object> data = null;
		try {
			testContextManager.prepareTestInstance(this);
			bean = applicationContext.getBean(testClass);
			Method m = bean.getClass().getMethod(testMethod, new Class[0]);
			data = (Map<String, Object>) m.invoke(bean, new Object[0]);
		} catch (Exception e) {
			e.printStackTrace();
			resultMessage.setDescription(e.getCause().getMessage());
			fail(e.getCause().getMessage());
		}

		this.serviceClass = (Class) data.get("serviceClass");
		this.methodName = (String) data.get("methodName");
		this.parameters = (Object[]) data.get("params");
		this.clazz = (Class<?>[]) data.get("clazz");
		this.initMethod = (String) data.get("initMethod");
		this.sql = (String) data.get("sql");
		this.sqlFileName = (String) data.get("sqlFileName");

	}

	/**
	 * Test method for ControllerTestSuite get test data from
	 * {@link com.gammon.junit.testsuite.ControllerTestSuite#getTestData()} .
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void compareResultWithSqlTestCase() {

		resultMessage.setDescription("Error");
		resultMessage.setClassName(serviceClass.getSimpleName());
		resultMessage.setMethodName(methodName);

		Object service = applicationContext.getBean(serviceClass);
		Method m;
		int resultSize = 0;

		try {
			if (StringUtils.isNotEmpty(initMethod)) {
				Method init = bean.getClass().getMethod(initMethod, new Class[0]);
				init.invoke(bean, new Object[0]);
			}
			if (clazz == null) {
				clazz = JunitHelper.createClassArray(parameters);
			}
			m = service.getClass().getDeclaredMethod(methodName, clazz);
			Object returnObject = m.invoke(service, parameters);
			if (returnObject instanceof Collection) {
				resultSize = ((List<Object>) returnObject).size();
			} else if (returnObject instanceof PaginationWrapper<?>) {
				resultSize = ((PaginationWrapper<Object>) returnObject).getTotalRecords();
			} else if (returnObject != null) {
				resultSize = 1;
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NullPointerException e) {
			e.printStackTrace();
			resultMessage.setDescription(e.getCause().getMessage());
			fail(e.getCause().getMessage());
		}
		if(!StringUtils.isEmpty(sqlFileName)) sql = JunitHelper.readSqlFromFile(sqlFileName);
		resultMessage.setStatus(JunitHelper.RESULT_STATUS_RESULT_NOERROR);
		@SuppressWarnings("rawtypes")
		List sqlList = sessionFactory.getCurrentSession().createSQLQuery(sql).list();

		resultMessage.setDescription(sqlList.get(0) + " != " + resultSize);
		assertEquals(sqlList.get(0) + " != " + resultSize, Integer.parseInt(sqlList.get(0).toString()), resultSize);
		resultMessage.setDescription("No. of result return:" + resultSize);
		resultMessage.setStatus(JunitHelper.RESULT_STATUS_VERIFIED);
	}

	@SuppressWarnings("rawtypes")
	@Parameterized.Parameters(name = "TestData:{0}.{1}")
	public static Collection getTestData() {
		return ControllerTestSuite.getTestData();
	}
	
	/**
	 * base class
	 * @author paulyiu
	 *
	 */
	public static class TestDataBase{
		
		protected String methodName;
		protected Map<String, Object> data;
		@Autowired
		protected ApplicationContext applicationContext;
		protected MockHttpServletResponse response;
		protected MockHttpSession session;
		protected MockHttpServletRequest request;
		protected void init(){
			methodName = JunitHelper.substringAfterUncapitalize(Thread.currentThread().getStackTrace()[2].getMethodName(),"test");
			data = new HashMap<String, Object>();
			session = JunitHelper.startSession();
			request = JunitHelper.startRequest(session);
			response = new MockHttpServletResponse();
		}
		protected void post(){
			request = JunitHelper.endRequest(request);
			session = JunitHelper.endSession(session);
		}
	}
}
