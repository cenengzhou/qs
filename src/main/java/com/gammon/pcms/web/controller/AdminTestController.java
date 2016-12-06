package com.gammon.pcms.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonView;
import com.gammon.pcms.config.AdminTestConfig;
import com.gammon.pcms.helper.RestTemplateHelper;

@RestController
@RequestMapping(value = "admintest/")
public class AdminTestController implements InitializingBean{

	public static int RERUN_INTERVAL;
	public static String HOST;
	public static String CONTEXTPATH;
	
	@Autowired
	private AdminTestConfig adminTestConfig;
	@Autowired
	private RestTemplateHelper restTemplateHelper;
	
	private List<TestCase> testCaseList;
	private Map<String, Object> beans = new HashMap<>();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		beans.put("restTemplatehelper", restTemplateHelper);
	}
	
	@JsonView(ExcludeDetailsView.class)
	@RequestMapping(value = "getTestAllTestCase", method = RequestMethod.POST)
	public List<TestCase> getTestItems(HttpServletRequest request){
		AdminTestController.HOST = "http://" + request.getServerName() + ":" + request.getServerPort();
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
		}
		return testCaseList;
	}
	
	@RequestMapping(value = "runTest", method = RequestMethod.POST)
	public TestResult runTest(@RequestParam int id, @RequestParam String category, @RequestParam String method, @RequestParam String item){
		TestResult testResult = null;
		TestCase testCase = testCaseList.get(id);
		if(category.equals(testCase.getCategory()) && method.equals(testCase.getMethod()) && item.equals(testCase.getItem())){
			TestMethod testMethod = testCase.getTestMethod();
			if(testMethod != null) testResult = testMethod.process();
		}
		return testResult;
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
		private Date lastRun;
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
				testMethod = new RestTestMethod(method, details, (RestTemplateHelper) beans.get("restTemplatehelper"));
				break;
			case "SOAP":
				testMethod = new SoapTestMethod(method, details);
				break;
			case "DB":
				testMethod = new DbTestMethod(method, details);
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
		 * @param count the count to set
		 */
		public static void setCount(int count) {
			TestCase.count = count;
		}

		/**
		 * @return the id
		 */
		public int getId() {
			return id;
		}

		/**
		 * @param id the id to set
		 */
		public void setId(int id) {
			this.id = id;
		}

		/**
		 * @return the category
		 */
		public String getCategory() {
			return category;
		}

		/**
		 * @param category the category to set
		 */
		public void setCategory(String category) {
			this.category = category;
		}

		/**
		 * @return the method
		 */
		public String getMethod() {
			return method;
		}

		/**
		 * @param method the method to set
		 */
		public void setMethod(String method) {
			this.method = method;
		}

		/**
		 * @return the item
		 */
		public String getItem() {
			return item;
		}

		/**
		 * @param item the item to set
		 */
		public void setItem(String item) {
			this.item = item;
		}

		/**
		 * @return the details
		 */
		public Map<String, String> getDetails() {
			return details;
		}

		/**
		 * @param details the details to set
		 */
		public void setDetails(Map<String, String> details) {
			this.details = details;
		}

		/**
		 * @return the testMethod
		 */
		public TestMethod getTestMethod() {
			return testMethod;
		}

		/**
		 * @param testMethod the testMethod to set
		 */
		public void setTestMethod(TestMethod testMethod) {
			this.testMethod = testMethod;
		}

		/**
		 * @return the lastRun
		 */
		public Date getLastRun() {
			if(getTestMethod() != null) lastRun = getTestMethod().lastRun;
			return lastRun;
		}

		/**
		 * @param lastRun the lastRun to set
		 */
		public void setLastRun(Date lastRun) {
			this.lastRun = lastRun;
		}

		/**
		 * @return the testResult
		 */
		public TestResult getTestResult() {
			if(getTestMethod() != null) testResult = getTestMethod().testResult;
			return testResult;
		}

		/**
		 * @param testResult the testResult to set
		 */
		public void setTestResult(TestResult testResult) {
			this.testResult = testResult;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "TestCase [id=" + id + ", category=" + category + ", method=" + method + ", item=" + item
					+ ", details=" + details + ", testMethod=" + testMethod + ", lastRun=" + lastRun + ", testResult="
					+ testResult + "]";
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((category == null) ? 0 : category.hashCode());
			result = prime * result + ((details == null) ? 0 : details.hashCode());
			result = prime * result + id;
			result = prime * result + ((item == null) ? 0 : item.hashCode());
			result = prime * result + ((lastRun == null) ? 0 : lastRun.hashCode());
			result = prime * result + ((method == null) ? 0 : method.hashCode());
			result = prime * result + ((testMethod == null) ? 0 : testMethod.hashCode());
			result = prime * result + ((testResult == null) ? 0 : testResult.hashCode());
			return result;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof TestCase)) {
				return false;
			}
			TestCase other = (TestCase) obj;
			if (category == null) {
				if (other.category != null) {
					return false;
				}
			} else if (!category.equals(other.category)) {
				return false;
			}
			if (details == null) {
				if (other.details != null) {
					return false;
				}
			} else if (!details.equals(other.details)) {
				return false;
			}
			if (id != other.id) {
				return false;
			}
			if (item == null) {
				if (other.item != null) {
					return false;
				}
			} else if (!item.equals(other.item)) {
				return false;
			}
			if (lastRun == null) {
				if (other.lastRun != null) {
					return false;
				}
			} else if (!lastRun.equals(other.lastRun)) {
				return false;
			}
			if (method == null) {
				if (other.method != null) {
					return false;
				}
			} else if (!method.equals(other.method)) {
				return false;
			}
			if (testMethod == null) {
				if (other.testMethod != null) {
					return false;
				}
			} else if (!testMethod.equals(other.testMethod)) {
				return false;
			}
			if (testResult == null) {
				if (other.testResult != null) {
					return false;
				}
			} else if (!testResult.equals(other.testResult)) {
				return false;
			}
			return true;
		}

		/**
		 * @return the beans
		 */
		public Map<String, Object> getBeans() {
			return beans;
		}

		/**
		 * @param beans the beans to set
		 */
		public void setBeans(Map<String, Object> beans) {
			this.beans = beans;
		}


	}
		
	public static interface ExcludeDetailsView{}
	
	public abstract static class TestMethod{
		protected String method;
		protected TestResult testResult = new TestResult();
		protected Map<String, String> details;
		protected Date lastRun;
		public TestResult process(){
			lastRun = new Date();
			return performanceTest();
		}
		public TestResult performanceTest(){return testResult;};
	}
	
	public static class RestTestMethod extends TestMethod{
		private RestTemplateHelper restTemplateHelper;
		private Logger logger = Logger.getLogger(getClass());
		
		public RestTestMethod(String method, Map<String, String> details, RestTemplateHelper restTemplateHelper){
			this.method = method;
			this.details = details;
			this.restTemplateHelper = restTemplateHelper;
			if("RESPONSE".equals(this.method)) this.testResult.setMessage("Not implement yet");
		}
		@Override
		public TestResult performanceTest() {
			try{
				String host, contextpath, url;
				if("RESPONSE".equals(method)) {
					host = AdminTestController.HOST;
					contextpath = AdminTestController.CONTEXTPATH != "" ? AdminTestController.CONTEXTPATH + "/" : "/";
					url = host + contextpath + details.get("URL");
				} else {
					host = details.get("HOST");
					url = details.get("HOST") + "/" + details.get("URL");
				}
				RestTemplate restTemplate = restTemplateHelper.getRestTemplateForAPI(host);
				Object response;
				if("GET".equals(details.get("METHOD"))){
					response = restTemplate.getForEntity(url, Object.class);
				} else {
					response = restTemplate.postForEntity(url, null, Object.class);
				}
				logger.info("REST Test response:" + response.toString());
				testResult.setStatus(true);
				testResult.setMessage("REST Test finished");
			} catch(Exception e){
				e.printStackTrace();
				testResult.setStatus(false);
				testResult.setMessage("REST Test fail:" + e);
			}
			return testResult;
		}
		
	}

	public static class SoapTestMethod extends TestMethod{

		public SoapTestMethod(String method, Map<String, String> details) {
			this.details = details;
			this.testResult.setMessage("Not implement yet");
		}

		@Override
		public TestResult performanceTest() {
			testResult.setStatus(true);
			testResult.setMessage("SOAP Test finished");
			return testResult;
		}
		
	}
	
	public static class DbTestMethod extends TestMethod{

		public DbTestMethod(String method, Map<String, String> details) {
			this.method = method;
			this.details = details;
			this.testResult.setMessage("Not implement yet");
		}

		@Override
		public TestResult performanceTest() {
			testResult.setStatus(true);
			testResult.setMessage("DB Test finished");
			return testResult;
		}
		
	}

	public static class TestResult {
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
