package com.gammon.junit.testbase;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gammon.junit.JunitConfig;
import com.gammon.pcms.config.ApplicationConfig;
import com.gammon.pcms.config.JasperConfig;
import com.gammon.pcms.config.ServletConfig;
import com.gammon.pcms.config.WebServiceConfig;
import com.gammon.pcms.helper.JunitHelper;
import com.gammon.pcms.helper.JunitHelper.Result;
import com.gammon.qs.service.security.UserDetailsServiceImpl;

@WebAppConfiguration
@ActiveProfiles("junit")
@ContextConfiguration(loader = AnnotationConfigWebContextLoader.class, classes = { ApplicationConfig.class,
		ServletConfig.class, WebServiceConfig.class })
public abstract class TestBase extends AbstractTransactionalJUnit4SpringContextTests {
	protected static java.util.logging.Logger logger;
	protected JunitHelper.Result resultMessage;
	protected static MockHttpServletResponse response;
	protected static MockHttpSession session;
	protected static MockHttpServletRequest request;
	protected String methodName = null;
	protected static Map<String, List<Result>> resultMessageMap;
	protected static Date testStartTime;
	protected static Date testEndTime;
	protected static String totalRunTime;
	protected static AtomicInteger noErrorCount = new AtomicInteger();
	protected static AtomicInteger failCount = new AtomicInteger();
	protected static AtomicInteger verifiedCount = new AtomicInteger();
	protected static AtomicInteger totalCount = new AtomicInteger();
	protected String reportName = "BASE REPORT";
	protected String reportClass = "BASE CLASS";
	protected static Map<String, Object> jasperParameters = new HashMap<String, Object>();
	@Autowired
	protected ApplicationContext applicationContext;
	@Autowired
	private JasperConfig jasperConfig;
	@Autowired
	private JunitConfig junitConfig;
	
	@SuppressWarnings("unused")
	private static boolean bindSession;
	@SuppressWarnings("unused")
	private Session hibSession;
	@SuppressWarnings("unused")
	@Autowired
	private SessionFactory sessionFactory;
	
	public TestBase(String reportName,String reportClass){
		this.reportName = reportName;
		this.reportClass = reportClass;
//		logger = JunitHelper.obtainLogger(reportName, Logger.getLogger(reportClass));
	}
	
	/**
	 * initial and reset variable for test case
	 */
	protected void initVar() {		
		methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
		resultMessage.setMethodName(methodName);
	}

	/**
	 * setup method run once BEFORE the first test case run
	 */
	@BeforeClass
	public static void setUp() {
		//DO NOT CALL JunitUtil in setUP()
		testStartTime = new Date();
		resultMessageMap = new LinkedHashMap<String, List<JunitHelper.Result>>();
//		session = JunitHelper.startSession();
//		request = JunitHelper.startRequest(session);
		response = new MockHttpServletResponse();
	}

	/**
	 * tear down method run once AFTER the last test case finish
	 * generate jasper report for all test case
	 */
	@AfterClass
	public static void tearDown() {
		testEndTime = new Date();
		totalRunTime = JunitHelper.returnSecondToTime((testEndTime.getTime() - testStartTime.getTime()) / 1000, "");
//		totalCount = resultMessageMap.size();

		jasperParameters.put("REPORT_TOTALRUNTIME", totalRunTime);
		jasperParameters.put("REPORT_TESTSTARTTIME", testStartTime);
		jasperParameters.put("REPORT_TESTENDTIME", testEndTime);
		jasperParameters.put("REPORT_TOTALNOERROR", noErrorCount);
		jasperParameters.put("REPORT_TOTALVERIFIED", verifiedCount);
		jasperParameters.put("REPORT_TOTALFAILED", failCount);
		jasperParameters.put("REPORT_TOTALCOUNT", totalCount);
//		request = JunitHelper.endRequest(request);
//		session = JunitHelper.endSession(session);
		JunitHelper.writeJasperReportResultList(jasperParameters, resultMessageMap);
//		JunitHelper.writeResultLog(resultMessageMap, jasperParameters, logger);
	}

	@BeforeTransaction
	public void beforeTransaction(){
		System.setProperty("javax.net.debug", "ssl"); 
		System.setProperty("javax.net.ssl.trustStore", junitConfig.getQsKeyStore());
		System.setProperty("javax.net.ssl.trustStorePassword", junitConfig.getQsKeyStorePassword());

		methodName = null;
		resultMessage = new JunitHelper.Result();
		resultMessage.setStatus(JunitHelper.RESULT_STATUS_FAIL);
		resultMessage.setStartTime(new Date());
	}
	/**
	 * before method run before of each test case.
	 * start mock session and initial default value for resultMessage
	 * @throws Exception 
	 */
	@Before
	public void before() {
		jasperParameters.put("jasperTemplateName", junitConfig.getJasperTemplateName());
		jasperParameters.put("TEMPLATE_PATH", jasperConfig.getTemplatePath());
		jasperParameters.put("IMAGE_PATH", jasperConfig.getTemplatePath());
		jasperParameters.put("REPORT_TITLE", reportName);
		jasperParameters.put("REPORT_TEST_CLASS", reportClass);
		jasperParameters.put("REPORT_TYPE", "pdf");

		session = JunitHelper.startSession();
		request = JunitHelper.startRequest(session);
		response = new MockHttpServletResponse();
		
		UserDetailsService userDetailsService = applicationContext.getBean(UserDetailsServiceImpl.class);
		UserDetails userDetails = userDetailsService.loadUserByUsername(junitConfig.getTestUser());
		UsernamePasswordAuthenticationToken  authToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
		authToken.setDetails(new WebAuthenticationDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authToken);
		
//		WebLogicJtaTransactionManager jtaTransactionManager = (WebLogicJtaTransactionManager) applicationContext.getBean("transactionManager");
//		try {
//			jtaTransactionManager.getTransactionManager().setTransactionTimeout(600);
//		} catch (SystemException e) {
//			e.printStackTrace();
//		}
		assertNotNull("Session mocking fail", session);// logger.info(StringUtils.center("[Mock session created]", reportWidth, "~"));
		assertNotNull("Request mocking fail", request);// logger.info(StringUtils.center("[Mock request created]", reportWidth, "~"));
	}

	/**
	 * after method run after each test case.
	 * incress fail/pass count and close mock session
	 */
	@After
	public void after(){
		
	}
	
	@AfterTransaction
	public void afterTransaction() {
		switch(resultMessage.getStatus()){
		case JunitHelper.RESULT_STATUS_FAIL:
			failCount.incrementAndGet();
			break;
		case JunitHelper.RESULT_STATUS_RESULT_NOERROR:
			noErrorCount.incrementAndGet();
			break;
		case JunitHelper.RESULT_STATUS_VERIFIED:
			verifiedCount.incrementAndGet();
			break;
		}
		totalCount.incrementAndGet();
		String key = resultMessage.getClassName();
		if(resultMessageMap.get(key)==null){
			resultMessageMap.put(key,new ArrayList<JunitHelper.Result>());
		}
		resultMessageMap.get(key).add(resultMessage);

		request = JunitHelper.endRequest(request);
		session = JunitHelper.endSession(session);
		
		resultMessage.setFinishTime(new Date());
		assertNull("request closing fail", request);// logger.info(StringUtils.center("[Mock request closed]", reportWidth, "~"));
		assertNull("session closing fail", session);// logger.info(StringUtils.center("[Mock session closed]", reportWidth, "~"));
		String testCaseRunTime = JunitHelper.returnSecondToTime((resultMessage.getFinishTime().getTime() - resultMessage.getStartTime().getTime() + 5600) / 1000, "");
		resultMessage.setDescription("("+testCaseRunTime+") " + resultMessage.getDescription());
	}
}
