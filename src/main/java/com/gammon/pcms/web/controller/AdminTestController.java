package com.gammon.pcms.web.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gammon.pcms.config.AdminTestConfig;
import com.gammon.pcms.config.SecurityConfig;
import com.gammon.pcms.config.WebServiceConfig;
import com.gammon.pcms.dto.rs.consumer.gsf.GetFunctionSecurity;
import com.gammon.pcms.helper.RestTemplateHelper;
import com.gammon.qs.webservice.WSConfig;
import com.gammon.qs.webservice.WSSEHeaderWebServiceMessageCallback;

@RestController
@RequestMapping(value = "admintest/")
public class AdminTestController implements InitializingBean{

	public static int RERUN_INTERVAL;
	public static String HOST;
	public static String CONTEXTPATH;
	public static String FNTEST = "FUNCTION|SECURITY";
	private static String GSF_REQUEST_USERNAME;
	
	@Autowired
	private AdminTestConfig adminTestConfig;
	@Autowired
	private RestTemplateHelper restTemplateHelper;
	@Autowired
	private WebServiceConfig webServiceConfig;
	@Autowired
	private SecurityConfig securityConfig;
	@Autowired
	private ObjectMapper objectMapper;	
	@PersistenceContext(unitName = "PersistenceUnit")
	protected EntityManager entityManager;
    @PersistenceContext(unitName = "ADL PersistenceUnit")
    protected EntityManager adlEntityManager;
	@Autowired
	@Qualifier("webservicePasswordConfig")
	private WSConfig wsConfig;
	@Autowired
	private ApplicationContext applicationContext;
	private AdminTestController adminTestController;
	
	private List<TestCase> testCaseList;
	private Map<String, Object> beans = new HashMap<>();
	private Logger logger = Logger.getLogger(getClass());
	
	private final Map<String, String> resultMap = new LinkedHashMap<>();
	private final Map<String, Integer> counterMap = new HashMap<>();
	private boolean restRunning;
	private Calendar restLastRun;
	@Override
	public void afterPropertiesSet() throws Exception {
		adminTestController = (AdminTestController) applicationContext.getBean("adminTestController");
		beans.put("restTemplatehelper", restTemplateHelper);
		beans.put("webServiceConfig", webServiceConfig);
		beans.put("applicationContext", applicationContext);
		beans.put("wsConfig", wsConfig);
		beans.put("objectMapper", objectMapper);
		beans.put("entityManager", entityManager);
		beans.put("adlEntityManager", adlEntityManager);
	}
	
	@RequestMapping(value = "runTestByRest", method = RequestMethod.GET)
	public Map<String, String> runTestByRest(
			@RequestParam(defaultValue = "1") Integer limitSubTaskTo, 
			@RequestParam(defaultValue = "false") boolean overrideAndRunAllSubTask,
			HttpServletRequest request){
		AdminTestController.HOST = request.getServerName() + ":" + request.getServerPort();
		AdminTestController.CONTEXTPATH = request.getContextPath();
		if(restRunning){
			Map<String, String> runningResultMap = new LinkedHashMap<>();
			runningResultMap.put(TestResult.SUCCESS, "0");
			runningResultMap.put(TestResult.FAIL, "1");
			runningResultMap.put("::ERROR", "Test running in other instance, please try later");
			return runningResultMap;
		} else{
			restRunning = true;
			Map<String, String> returnMap = null;
			if(restLastRun != null){
				String  msg = TestMethod.hitRerunInterval(restLastRun);
				if(StringUtils.isNotEmpty(msg)) {
					Map<String, String> rerunIntervalMap = new LinkedHashMap<>();
					rerunIntervalMap.put(TestResult.SUCCESS, "0");
					rerunIntervalMap.put(TestResult.FAIL, "1");
					rerunIntervalMap.put("::ERROR", msg);
					returnMap = rerunIntervalMap;
				} else {
					restLastRun = Calendar.getInstance();
					returnMap = performTestByRest(limitSubTaskTo, overrideAndRunAllSubTask);
				}
			} else {
				restLastRun = Calendar.getInstance();
				returnMap = performTestByRest(limitSubTaskTo, overrideAndRunAllSubTask);
			}
			restRunning = false;
			return returnMap;
		}
	}
	
	private synchronized Map<String, String> performTestByRest(Integer limitSubTaskTo, boolean overrideAndRunAllSubTask){
		resultMap.clear();
		counterMap.clear();
		resultMap.put(TestResult.SUCCESS, "0");
		resultMap.put(TestResult.FAIL, "0");
		List<TestCase> testCaseList = getAllTestCaseList();
		testCaseList.forEach(testCase ->{
			String mapKey = "::" + testCase.getCategory() + "::" + testCase.getMethod();
			Integer count = counterMap.get(mapKey) != null ? counterMap.get(mapKey) : 0;
			if( overrideAndRunAllSubTask || count < limitSubTaskTo) {
				TestResult testResult = runTestMethod(testCase);
				counterMap.put(mapKey, count+1);
				resultMap.put(mapKey + "::" + testCase.getItem(), testResult.getMessage());
				if(testResult.status){
					Integer successCount = new Integer(resultMap.get(TestResult.SUCCESS));
					resultMap.put(TestResult.SUCCESS, "" + (successCount + 1));
				} else {
					Integer failCount = new Integer(resultMap.get(TestResult.FAIL));
					resultMap.put(TestResult.FAIL, "" + (failCount + 1));
				}
			}
		});
		return resultMap;
	}
	
	@JsonView(ExcludeDetailsView.class)
	@RequestMapping(value = "getTestAllTestCase", method = RequestMethod.POST)
	public List<TestCase> getTestItems(HttpServletRequest request){
		AdminTestController.HOST = request.getServerName() + ":" + request.getServerPort();
		AdminTestController.CONTEXTPATH = request.getContextPath();
		return getAllTestCaseList();
	}
	
	private List<TestCase> getAllTestCaseList(){
		if(testCaseList == null) {
			TestCase.count = 0;
			testCaseList = new ArrayList<>();
			adminTestConfig.getCategoryList()
			.forEach(category -> {
				switch(category){
				case "SETTING":
					AdminTestController.RERUN_INTERVAL = new Integer(adminTestConfig.getCategoryMethodItemDetail(category, "ALL", "TASK", "RERUN_INTERVAL"));
					AdminTestController.GSF_REQUEST_USERNAME = adminTestConfig.getCategoryMethodItemDetail(category, "ALL", "TASK", "GSF_REQUEST_USERNAME");
					break;
				default:
					adminTestConfig.getCategoryMethodList(category)
					.forEach(method -> {
						adminTestConfig.getCategoryMethodItemList(category, method)
						.forEach(item -> {
							Map<String, String> details = adminTestConfig.getCategoryMethodItem(category, method, item);
							TestCase testCase = new TestCase(category, method, item, details, beans);
							testCaseList.add(testCase.id, testCase);
						});	
					});
					break;
				}
			});
			securityConfig.getFnMethods()
			.forEach((controller, methods) -> {
				methods.forEach((method, fn) ->{
					Map<String, String> fnMap = new HashMap<String, String>();
					fnMap.put("HOST", webServiceConfig.getWsGsf("URL"));
					fnMap.put("URL", "GetFunctionSecurity");
					fnMap.put("METHODNAME", method);
					fnMap.put("FN", fn);
					TestCase testCase = new TestCase(FNTEST, controller, fn, fnMap, beans);
					testCaseList.add(testCase.id, testCase);
				});
			});
		}
		return testCaseList;
	}
	
	@RequestMapping(value = "runTest", method = RequestMethod.POST)
	public TestResult runTest(@RequestParam int id, @RequestParam String category, @RequestParam String method, @RequestParam String item) throws InterruptedException{
		TestResult testResult = null;
		TestCase testCase = testCaseList.get(id);
		if(category.equals(testCase.getCategory()) && method.equals(testCase.getMethod()) && item.equals(testCase.getItem())){
			testResult = runTestMethod(testCase);
		}
		TimeUnit.MILLISECONDS.sleep(150);
		return testResult;
	}

	private TestResult runTestMethod(TestCase testCase) {
		TestResult testResult = null;
		TestMethod testMethod = testCase.getTestMethod();
		if(testMethod != null) {
			if(testCase.getCategory().equals("DB")){
				if(testCase.getItem().equals("ADL")){
					testResult = adminTestController.adlTest(testMethod);
				} else {
					testResult = adminTestController.pcmsdataTest(testMethod);
				}
			} else {
				testResult = testMethod.process();
			}
		}
		return testResult;
	}
	
	@Transactional(transactionManager="transactionManager", propagation = Propagation.REQUIRES_NEW)
	public TestResult pcmsdataTest(TestMethod testMethod){
		return testMethod.process();
	}
	@Transactional(transactionManager="adlTransactionManager", propagation = Propagation.REQUIRES_NEW)
	public TestResult adlTest(TestMethod testMethod){
		return testMethod.process();
	}
	
	public static class TestCase{
		public static int count = 0;
		private Map<String, Object> beans;
		@JsonView(ExcludeDetailsView.class)
		private int id;
		@JsonView(ExcludeDetailsView.class)
		private String category;
		@JsonView(ExcludeDetailsView.class)
		private String method;
		@JsonView(ExcludeDetailsView.class)
		private String item;
		private Map<String, String> details;
		private TestMethod testMethod;
		@JsonView(ExcludeDetailsView.class)
		private Calendar lastRun;
		@JsonView(ExcludeDetailsView.class)
		private TestResult testResult;
		
		public TestCase(String category, String method, String item, Map<String, String> details, Map<String, Object> beans){
			this.id = count++;
			this.category = category;
			this.method = method;
			this.item = item;
			this.details = details;
			this.beans = beans;
			testResult = new TestResult();
			testResult.setMessage("Testing");
			switch(category){
			case "REST":
			case "FUNCTION|SECURITY":
				testMethod = new RestTestMethod(category, method, details, beans);
				break;
			case "SOAP":
				testMethod = new SoapTestMethod(method, details, beans);
				break;
			case "DB":
				testMethod = new DbTestMethod(method, details, beans);
				break;
			}
		}

		/**
		 * @return the count
		 */
		public static int getCount() {
			return count;
		}

		/**
		 * @return the beans
		 */
		public Map<String, Object> getBeans() {
			return beans;
		}

		/**
		 * @return the id
		 */
		public int getId() {
			return id;
		}

		/**
		 * @return the category
		 */
		public String getCategory() {
			return category;
		}

		/**
		 * @return the method
		 */
		public String getMethod() {
			return method;
		}

		/**
		 * @return the item
		 */
		public String getItem() {
			return item;
		}

		/**
		 * @return the details
		 */
		public Map<String, String> getDetails() {
			return details;
		}

		/**
		 * @return the testMethod
		 */
		public TestMethod getTestMethod() {
			return testMethod;
		}

		/**
		 * @return the lastRun
		 */
		public Calendar getLastRun() {
			return lastRun;
		}

		/**
		 * @return the testResult
		 */
		public TestResult getTestResult() {
			return testResult;
		}

	}
		
	public static interface ExcludeDetailsView{}
	
	public abstract static class TestMethod{
		protected String category;
		protected String method;
		protected TestResult testResult = new TestResult();
		protected Map<String, String> details;
		protected Calendar lastRun;
		protected boolean running;
		public TestResult process(){
			TestResult result = new TestResult();
			if(running){
				result.setStatus(false);
				result.setMessage("Test running in other instance, please try again later.");
			} else {
				running = true;
				if(lastRun != null){
					String  msg = hitRerunInterval(lastRun);
					if(StringUtils.isNotEmpty(msg)) {
						result.setStatus(false);
						result.setMessage(msg);
					} else {
						result = performanceTest();
						lastRun = Calendar.getInstance();
					}
				} else {
					result = performanceTest();
					lastRun = Calendar.getInstance();
				}
				running = false;
			}
			return result;
		}
		
		public static String hitRerunInterval(Calendar lastRun){
			Calendar now = Calendar.getInstance();
			Calendar checkRerun = (Calendar) lastRun.clone();
			checkRerun.add(Calendar.SECOND, RERUN_INTERVAL);
			String msg = "";
			if(checkRerun.after(now)){
				msg = "Hit minimum rerun interval, please try " + ((checkRerun.getTime().getTime() - now.getTime().getTime())/1000 + 1) + " seconds later.";
			}
			return msg;
		}
		
		public TestResult performanceTest(){return testResult;};
	}
	
	public static class RestTestMethod extends TestMethod{
		private RestTemplateHelper restTemplateHelper;
		private WebServiceConfig webServiceConfig;
		private Logger logger = Logger.getLogger(getClass());
		
		public RestTestMethod(String category, String method, Map<String, String> details, Map<String, Object> beans){
			this.category = category;
			this.method = method;
			this.details = details;
			this.restTemplateHelper = (RestTemplateHelper) beans.get("restTemplatehelper");
			this.webServiceConfig = (WebServiceConfig) beans.get("webServiceConfig");
		}

		@Override
		public TestResult performanceTest() {
			try{
				String host, contextpath, url, message;
				if("PROVIDER".equals(method)) {
					host = AdminTestController.HOST;
					contextpath = AdminTestController.CONTEXTPATH != "" ? AdminTestController.CONTEXTPATH + "/" : "/";
					url = "http://" + host + contextpath + details.get("URL");
					message = "REST Service Test response:";
				} else {
					host = details.get("HOST");
					url = details.get("HOST") + "/" + details.get("URL");
					message = "REST Client Test response:";
				}
				RestTemplate restTemplate = restTemplateHelper.getRestTemplate(host, null, null);
				if(FNTEST.equals(category)){
					GetFunctionSecurity.Request functionSecurityRequest = new GetFunctionSecurity.Request(
							WebServiceConfig.GSF_APPLICATION_CODE, AdminTestController.GSF_REQUEST_USERNAME, details.get("FN"));
					ResponseEntity<GetFunctionSecurity.Response> functionSecurityResponse = 
							restTemplate.postForEntity(url, functionSecurityRequest, GetFunctionSecurity.Response.class);
					List<GetFunctionSecurity.Result> functionSecurityList = functionSecurityResponse.getBody().getResultList();
					if(functionSecurityList != null ){
						GetFunctionSecurity.Result result = functionSecurityList.get(0);
						testResult.setMessage(result.getFunctionName() + " is " + result.getAccessRight());
						logger.info("Test " + result.getFunctionName() + " (" + method + "-" + details.get("METHODNAME") + ") " + StringUtils.substring(functionSecurityResponse.toString(), 0, 255));
					} else {
						testResult.setMessage(details.get("FN") + " not found");
						logger.info("Test " + details.get("FN") + " (" + method + "-" + details.get("METHODNAME") + ") " + StringUtils.substring(functionSecurityResponse.toString(), 0, 255));						
					}
				} else {
					ResponseEntity<Object> response;
					HttpHeaders  httpHeaders = new HttpHeaders();
					String headerName = "Authorization";
					String token = webServiceConfig.getPcmsApi("USERNAME") + ":" + webServiceConfig.getPcmsApi("PASSWORD");
					String headerValue = "Basic " + DatatypeConverter.printBase64Binary(token.getBytes());
					httpHeaders.add(headerName, headerValue);
					HttpEntity<String> requestEntity = new HttpEntity<>("Headers", httpHeaders);
					if("GET".equals(details.get("METHOD"))){
						response = restTemplate.exchange(url, HttpMethod.GET, requestEntity , Object.class);
					} else {
						response = restTemplate.postForEntity(url, requestEntity, Object.class);
					}		
					testResult.setMessage("REST Test finished");
					logger.info(message + StringUtils.substring(response.toString(), 0, 255));
				}
				testResult.setStatus(true);
			} catch(Exception e){
				e.printStackTrace();
				testResult.setStatus(false);
				testResult.setMessage("REST Test fail: " + e);
			}
			return testResult;
		}
		
	}

	public static class SoapTestMethod extends TestMethod{
		private ApplicationContext applicationContext;
		private WSConfig wsConfig;
		private ObjectMapper objectMapper;
		private Logger logger = Logger.getLogger(getClass());
		public SoapTestMethod(String method, Map<String, String> details, Map<String, Object> beans) {
			this.details = details;
			this.applicationContext = (ApplicationContext) beans.get("applicationContext");
			this.wsConfig = (WSConfig) beans.get("wsConfig");
			this.objectMapper = (ObjectMapper) beans.get("objectMapper");
		}

		@Override
		public TestResult performanceTest() {
			try{
				WebServiceTemplate webServiceTemplate = (WebServiceTemplate) applicationContext.getBean(details.get("templateBean"));
				String requestString = details.get("requestObject");
				requestString = requestString.replace("'", "\"");
				Object request = objectMapper.readValue(requestString, Class.forName(details.get("requestClass")));
				Object response = webServiceTemplate.marshalSendAndReceive(request, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
				logger.info("SOAP Test response:" + StringUtils.substring(response.toString(), 0, 255));
				testResult.setStatus(true);
				testResult.setMessage("SOAP Test finished");
			} catch(Exception e){
				e.printStackTrace();
				testResult.setStatus(false);
				testResult.setMessage("SOAP Test fail: " + e);
			}
			return testResult;
		}
		
	}
	
	public static class DbTestMethod extends TestMethod{
		private Logger logger = Logger.getLogger(getClass());
		private EntityManager entityManager;
		private ObjectMapper objectMapper;
		public DbTestMethod(String method, Map<String, String> details, Map<String, Object> beans) {
			this.method = method;
			this.details = details;
			this.objectMapper = (ObjectMapper) beans.get("objectMapper");
			if(details.get("SCHEMA").indexOf("ADL") >= 0){
				this.entityManager = (EntityManager) beans.get("adlEntityManager");
				
			} else {
				this.entityManager = (EntityManager) beans.get("entityManager");
			}
		}

		@Override
		public TestResult performanceTest() {
			try{
				String sqlListString = details.get("SQL").replaceAll("`", "\"");
				Map<String, String> sqlMap = objectMapper.readValue(sqlListString, new TypeReference<HashMap<String, String>>(){});
				Query setDefaultSchema = entityManager.createNativeQuery("ALTER SESSION SET CURRENT_SCHEMA=" + details.get("SCHEMA"));
		        setDefaultSchema.executeUpdate();
		        sqlMap.forEach((returnEntity, sql) ->{
		        	try {
		        		Calendar before = Calendar.getInstance();
		        		logger.info("DB Test SQL@" + details.get("SCHEMA") + ":" + sql);
						Query query = entityManager.createNativeQuery(sql, Class.forName(returnEntity));
						query.setMaxResults(1);
						List<?> result = query.getResultList();
						Calendar after = Calendar.getInstance();
						logger.info("DB Test response: " + ((after.getTimeInMillis() - before.getTimeInMillis()) / 1000) + "s " + " => " + StringUtils.substring(result.toString(), 0, 255) + "...");
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
		        });
				testResult.setStatus(true);
				testResult.setMessage("DB Test " + sqlMap.size() + " entity finished");
			} catch(Exception e){
				e.printStackTrace();
				testResult.setStatus(false);
				testResult.setMessage("DB Test fail: " + e);
			}
			return testResult;
		}
	}

	public static class TestResult {
		public static final String SUCCESS = "SUCCESS";
		public static final String FAIL = "FAIL";
		@JsonView(ExcludeDetailsView.class)
		private boolean status = false;
		@JsonView(ExcludeDetailsView.class)
		private String message = "";
		/**
		 * @return the status
		 */
		public boolean isStatus() {
			return status;
		}
		/**
		 * @param status the status to set
		 */
		public void setStatus(boolean status) {
			this.status = status;
		}
		/**
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}
		/**
		 * @param message the message to set
		 */
		public void setMessage(String message) {
			this.message = message;
		}
		
	}
	
}
