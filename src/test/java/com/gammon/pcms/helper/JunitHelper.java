package com.gammon.pcms.helper;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.gammon.qs.util.JasperReportHelper;

import net.sf.jasperreports.engine.JRException;

/**
 * Helper class for Junit test
 * 
 * @author paulnpyiu
 * @since 2015-08-27
 */
public class JunitHelper {
	
	public static final String RESULT_STATUS_PASS = "Passed";
	public static final String RESULT_STATUS_FAIL = "Failed";
	public static final String RESULT_STATUS_VERIFIED = "Verified";
	public static final String RESULT_STATUS_RESULT_NOERROR = "NO Error";
	private static Logger logger = Logger.getLogger(JunitHelper.class.getName());
	private static int reportWidth;
	private static Random random;
	private static boolean debug;
	private static boolean writeReport;

	static {
		reportWidth = 100;
		random = new Random();
		debug = false;
		writeReport = false;
	}

	/**
	 * List all bean loaded by the test case
	 * 
	 * @param applicationContext
	 *            the application context for list bean
	 * @return String the name list
	 */
	@SuppressWarnings("unused")
	public static String listBean(ApplicationContext applicationContext) {
		String[] all = applicationContext.getBeanDefinitionNames();
		String out = "";
		ConfigurableListableBeanFactory clbf = ((AbstractApplicationContext) applicationContext).getBeanFactory();
		for (String name : all) {
			Object s = clbf.getSingleton(name);
			out += name + " | ";
			out += clbf.getBeanDefinition(name).getBeanClassName() + "\n\n";
		}
		return out;
	}

	/**
	 * return the mock session
	 * 
	 * @return MockHttpSession
	 */
	public static MockHttpSession startSession() {
		return new MockHttpSession();
	}

	/**
	 * Stop mock session and clear attributes
	 * 
	 * @param session
	 * @return null
	 */
	public static MockHttpSession endSession(MockHttpSession session) {
		if(session != null) {
			session.clearAttributes();
			session = null;
		}
		return session;
	}

	/**
	 * start mock request
	 * 
	 * @param session
	 *            session for the mock request
	 * @return the mock request
	 */
	public static MockHttpServletRequest startRequest(MockHttpSession session) {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setSession(session);
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		return request;
	}

	/**
	 * stop mock request
	 * 
	 * @param request
	 * @return null
	 */
	public static MockHttpServletRequest endRequest(MockHttpServletRequest request) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attributes != null)
			attributes.requestCompleted();
		RequestContextHolder.resetRequestAttributes();
		request = null;
		return request;
	}

	/**
	 * Search loop base on parameterTitleList
	 * 
	 * @param parameterTitleList
	 *            The properties for search
	 * @param jobNumber
	 *            The job number
	 * @param forTestList
	 *            The testing list for checking result value
	 * @param searchParams
	 *            The parameter for search
	 * @param match
	 *            match criteria (eg:startWith, anywhere)
	 * @param testCase
	 *            The test case instance
	 * @return The search success or not
	 */
	public static <T> boolean searchByPropertiesTest(List<String> parameterTitleList, String jobNumber,
			List<T> forTestList, Map<String, String> searchParams, String match, TestCase testCase) {
		Map<String, String> parameters;
		for (int i = 0; i < parameterTitleList.size(); i++) {
			List<T> resultWrapperList = null;
			String criteria = parameterTitleList.get(i);
			String restriction = (match != null && searchParams != null) ? searchParams.get(criteria)
					: otainValueFromWrapper(forTestList.get(i), parameterTitleList.get(i));
			parameters = new LinkedHashMap<String, String>();
			parameters.put(criteria, restriction);
			// search with all parameters
			if (parameterTitleList.size() > 1 && i == parameterTitleList.size() - 1) {
				for (String key : parameterTitleList) {
					String value = otainValueFromWrapper(parameterTitleList.get(i), key);
					parameters.put(key, value);
				}
				criteria = "^All^";
				restriction = "^All^";
			}
			resultWrapperList = testCase.doSearch(parameters);
			// checking restriction values with result value
			if (resultWrapperList.size() > 0 && !criteria.equals("^All^")) {
				int randomSelect = obtainRandomRecordIndex(resultWrapperList);
				String resultValue = otainValueFromWrapper(resultWrapperList.get(randomSelect), criteria).toString()
						.trim();
				if (match == null) {
					return (criteria == "description") ? StringUtils.containsIgnoreCase(resultValue, restriction)
							: StringUtils.equalsIgnoreCase(restriction, resultValue);
				} else if (match.equals("startWith")) {
					int endIndex = restriction.length();
					if (restriction.endsWith("*")) {
						endIndex--;
					}
					return resultValue.startsWith(restriction.substring(0, endIndex));
				} else if (match.equals("anywhere")) {
					return StringUtils.containsIgnoreCase(resultValue, restriction);
				} else {
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * obtain random index base on resultWrapper list
	 * 
	 * @param resultWrapper
	 * @return the random index
	 */
	public static <T> int obtainRandomRecordIndex(List<T> resultWrapper) {
		int randomSelect = (resultWrapper.size() - 1) > 0 ? random.nextInt(resultWrapper.size() - 1) : 0;
		return randomSelect;
	}

	/**
	 * obtain list of item for testing reference
	 * 
	 * @param wrapperList
	 *            the whole list for random select
	 * @param numOfRecordForTest
	 *            number of record to put into testing list
	 * @param forTestList
	 *            The empty list for generic type
	 * @return forTestList The list for testing reference
	 */
	public static <T> List<T> obtainTestList(List<T> wrapperList, int numOfRecordForTest, List<T> forTestList) {
		numOfRecordForTest = numOfRecordForTest > wrapperList.size() ? wrapperList.size() : numOfRecordForTest;
		for (int i = 0; i < numOfRecordForTest; i++) {
			forTestList.add(wrapperList.get(random.nextInt(wrapperList.size() - 1)));
		}
		return forTestList;
	}

	/**
	 * recursion method for obtain value from object
	 * 
	 * @param wrapper
	 *            the object to invode the getter method
	 * @param property
	 *            the property name(eg:scDetails.scPackage.job.jobNumber)
	 * @return the value of property
	 */
	@SuppressWarnings("unchecked")
	public static <T> String otainValueFromWrapper(T wrapper, String property) {
		try {
			int dot = property.indexOf(".");
			String methodName = dot < 0 ? "get" + StringUtils.capitalize(property)
					: "get" + StringUtils.capitalize(property.substring(0, dot));
			Method method = wrapper.getClass().getDeclaredMethod(methodName, new Class[0]);
			if (dot < 0) {
				return (String) method.invoke(wrapper, new Object[0]).toString().trim();
			} else {
				T bean;
				bean = (T) method.invoke(wrapper, new Object[0]);
				String childBeanProperty = property.substring(dot + 1, property.length());
				return otainValueFromWrapper(bean, childBeanProperty);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * toString method for generic list
	 * 
	 * @param parameters
	 *            parameter for list
	 * @param itemTitle
	 *            the title
	 * @param wrappersForTest
	 *            the list of key and value
	 * @return String of the list content
	 */
	public static <T> String toStringListDetails(Map<String, String> parameters, String itemTitle,
			List<T> wrappersForTest) {
		String out = "";
		for (T w : wrappersForTest) {
			// print item title
			String itemValue = otainValueFromWrapper(w, itemTitle);
			out += StringUtils.center("<[" + itemTitle + " " + itemValue + "]>", reportWidth, "~.");
			out += StringUtils.center("", reportWidth, "=");
			// print properties and values
			for (String k : parameters.keySet()) {
				String v = otainValueFromWrapper(w, k);
				out += toStringProperty(k, v);
			}
			out += StringUtils.rightPad("", reportWidth, "=");
		}
		return out;
	}

	/**
	 * toString method of HTTP response
	 * 
	 * @param response
	 *            the Mock response
	 * @return String of the HTTP response
	 */
	public static String toStringHttpResponse(MockHttpServletResponse response) {
		String out = "";
		String httpHeader = (String) response.getHeader("Content-Disposition");
		int httpStatus = response.getStatus();
		String httpContentType = response.getContentType();
		out += toStringProperty("Http Header", httpHeader);
		out += toStringProperty("Http Content Type", httpContentType);
		out += toStringProperty("Http Status", "" + httpStatus);
		return out;
	}

	/**
	 * toString method for key and value
	 * 
	 * @param left
	 *            the key
	 * @param right
	 *            the value
	 * @return the string contain key at left and value at right
	 */
	public static String toStringProperty(String left, String right) {
		int rLength = reportWidth - left.length();
		return left + StringUtils.leftPad(right, rLength, ".");
	}

	/**
	 * write outputStream to file
	 * 
	 * @param outputStream
	 *            the output stream
	 * @param fileName
	 *            the filename
	 * @return true or false
	 */
	public static boolean writeReport(ByteArrayOutputStream outputStream, String fileName) {
		OutputStream fo = null;
		try {
			if (writeReport) {
				File dir = new File("report");
				if(!dir.exists()){
					dir.mkdir();
				}
				fo = new FileOutputStream(dir.getName() + "\\" + fileName);
				outputStream.writeTo(fo);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (fo != null)
					fo.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * write report to jasper
	 * 
	 * @param jasperParameters
	 *            the parameterMap for jasper
	 * @param resultList
	 *            the data list for jasper
	 * @return true or false
	 */
	public static boolean writeJasperReport(Map<String, Object> jasperParameters, Collection<Result> resultList) {
		try {
			ByteArrayOutputStream jasperOutputStream;
			List<JunitHelper.TestCaseResult> testCaseResultList = new ArrayList<JunitHelper.TestCaseResult>();
			String reportname = (String) jasperParameters.get("TEMPLATE_PATH")
					+ (String) jasperParameters.get("jasperTemplateName");
			testCaseResultList.add(
					new JunitHelper.TestCaseResult((String) jasperParameters.get("REPORT_TEST_CLASS"), resultList));
			String fileName = (String) jasperParameters.get("REPORT_TITLE") + " "
					+ DateHelper.formatDate(new Date(), "yyyy-MM-dd HHmmss") + "."
					+ (String) jasperParameters.get("REPORT_TYPE");
			jasperOutputStream = JasperReportHelper.get()
					.setCurrentReport(testCaseResultList, reportname, jasperParameters).compileAndAddReport()
					.exportAsPDF();
			JunitHelper.turnOnWriteReport();
			JunitHelper.writeReport(jasperOutputStream, fileName);
			return true;
		} catch (JRException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean writeJasperReportResultList(Map<String, Object> jasperParameters,
			Map<String, List<Result>> resultMessageMap) {
		try {
			ByteArrayOutputStream jasperOutputStream;
			List<JunitHelper.TestCaseResult> testCaseResultList = new ArrayList<JunitHelper.TestCaseResult>();
			for (Entry<String, List<Result>> entry : resultMessageMap.entrySet()) {
				String key = entry.getKey();
				List<Result> list = entry.getValue();
				testCaseResultList.add(new JunitHelper.TestCaseResult(key, list));
			}
			String reportname = (String) jasperParameters.get("TEMPLATE_PATH")
					+ (String) jasperParameters.get("jasperTemplateName");
			String fileName = (String) jasperParameters.get("REPORT_TITLE") + " "
					+ DateHelper.formatDate(new Date(), "yyyy-MM-dd HHmmss") + "."
					+ (String) jasperParameters.get("REPORT_TYPE");
			jasperOutputStream = JasperReportHelper.get()
					.setCurrentReport(testCaseResultList, reportname, jasperParameters).compileAndAddReport()
					.exportAsPDF();
			JunitHelper.turnOnWriteReport();
			JunitHelper.writeReport(jasperOutputStream, fileName);
			return true;
		} catch (JRException | IOException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * write resultMessageMap to file
	 * 
	 * @param resultMessageMap
	 *            the map data
	 * @param jasperParameters
	 *            the pass/fail count and timing
	 * @param logger
	 *            the logger to write
	 */
	public static void writeResultLog(Map<String, JunitHelper.Result> resultMessageMap,
			Map<String, Object> jasperParameters, Logger logger) {
		if (resultMessageMap != null) {
			logger.info("result map size:" + resultMessageMap.size());
			for (Entry<String, Result> entry : resultMessageMap.entrySet()) {
				Result result = entry.getValue();
				logger.info("Method name:" + result.getMethodName());
				logger.info("Description:" + result.getDescription());
				logger.info("Status:" + result.getStatus());
				logger.info("Start time:" + result.getStartTime());
				logger.info("End time:" + result.getFinishTime());
				logger.info(".");
			}
		}
		logger.info("PassCount:" + jasperParameters.get("REPORT_TOTALPASSED") + " | FailCount:"
				+ jasperParameters.get("REPORT_TOTALFAILED") + " | TotalCount:"
				+ jasperParameters.get("REPORT_TOTALCOUNT") + " | StartTime:"
				+ jasperParameters.get("REPORT_TESTSTARTTIME") + " | EndTime:"
				+ jasperParameters.get("REPORT_TESTENDTIME") + " | TotalRunTime:"
				+ jasperParameters.get("REPORT_TOTALRUNTIME"));
	}

	/**
	 * subString ByteArrayOutputStream
	 * 
	 * @param outputStream
	 *            the outputStream for subString
	 * @param beginIndex
	 *            beginIndex
	 * @param endIndex
	 *            endIndex
	 * @return the result
	 */
	public static String subStringByteArray(ByteArrayOutputStream outputStream, int beginIndex, int endIndex) {
		String output = null;
		try {
			output = new String(outputStream.toByteArray(), "UTF-8");
			endIndex = output.length() > endIndex ? endIndex : output.length();
			return output.substring(beginIndex, endIndex);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		return output;
	}

	/**
	 * convert million second to time
	 * 
	 * @param time
	 *            the million second
	 * @param d
	 *            time separator
	 * @return the time
	 */
	public static String returnSecondToTime(long time, String d) {
		if (time < 86400) {
			String s = "";
			long x = time / 60;
			long r = time % 60;
			s += x >= 60 ? returnSecondToTime(x, ":") : String.format("%02d", x);
			s += r > 0 ? ":" + String.format("%02d", r) : ":00";
			s = s.length() < 8 ? "00:" + s : s;
			return s.substring(s.length() - 8, s.length());
		} else {
			return "more than a day...;(";
		}
	}

	/**
	 * return random Cap of string
	 * 
	 * @param searchValue
	 *            the string for random Cap
	 * @return random Cap of string
	 */
	public static String randomCap(String searchValue) {
		for (int i = 0; i < searchValue.length(); i++) {
			if (random.nextInt(2) == 1) {
				searchValue = searchValue.substring(0, i) + String.valueOf(StringUtils.swapCase(searchValue).charAt(i))
						+ searchValue.substring(i + 1);
			}
		}
		return searchValue;
	}

	/**
	 * get random integer
	 * 
	 * @param max
	 *            max value
	 * @return the random integer
	 */
	public static int nextInt(int max) {
		return random.nextInt(max);
	}

	/**
	 * obtain customer logger
	 * 
	 * @param reportName
	 *            the reportName for logger
	 * @param logger
	 *            the logger
	 * @return the customer logger
	 */
	public static Logger obtainLogger(String reportName, Logger logger) {
		if (logger.getHandlers().length == 0) {
			Formatter formatter = new loggerFormatter();
			ConsoleHandler ch = new ConsoleHandler();
			ch.setFormatter(formatter);
			logger.addHandler(ch);
			logger.setUseParentHandlers(false);
			LinkedList<Result> loggerResultList = new LinkedList<JunitHelper.Result>();
			logger.addHandler(new JunitHelper.log2ListHandler(loggerResultList));
			try {
				FileHandler fh = new FileHandler("report\\" + reportName + ".%u.%g.txt", 1024 * 1024, 3, false);
				fh.setFormatter(formatter);
				logger.addHandler(fh);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return logger;
	}

	// getter & setter
	public static void debugOn() {
		debug = true;
	}

	public static void debugOff() {
		debug = false;
	}

	public static void turnOnWriteReport() {
		writeReport = true;
	}

	public static void turnOffWriteReport() {
		writeReport = false;
	}

	public static int getReportWidth() {
		return reportWidth;
	}

	public static void setReportWidth(int reportWidth) {
		JunitHelper.reportWidth = reportWidth;
	}

	/**
	 * The result class for jasper report
	 * 
	 * @author paulnpyiu
	 * @since 2015-08-27
	 */
	public static class TestCaseResult {
		String testClassName;
		List<Result> resultList;

		public TestCaseResult(String testClassName, Collection<Result> resultList) {
			super();
			this.testClassName = testClassName;
			this.resultList = new ArrayList<Result>();
			this.resultList.addAll(resultList);
		}

		public String getTestClassName() {
			return testClassName;
		}

		public void setTestClassName(String testClassName) {
			this.testClassName = testClassName;
		}

		public List<Result> getResultList() {
			return resultList;
		}

		public void setResultList(List<Result> resultList) {
			this.resultList = resultList;
		}

	}
	
	/**
	 * read sql from file and return as string
	 * @param sqlFileName
	 * @return
	 */
	public static String readSqlFromFile(String sqlFileName) {

		String ls = System.getProperty("line.separator");
		String line = null;
		try (	
				BufferedReader reader = new BufferedReader(new FileReader(sqlFileName));		
			){
			StringBuilder sb = new StringBuilder();
			while((line = reader.readLine()) != null){
				sb.append(line);
				sb.append(ls);
			}
			return sb.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * the result class for jasper subreport
	 * 
	 * @author paulnpyiu
	 * @since 2015-08-27
	 */
	public static class Result {
		String description = null;
		String className = null;
		String methodName = null;
		String status = null;
		Date startTime = null;
		Date finishTime = null;

		public Result() {
		}

		public Result(String description) {
			this.description = description;
		}

		public Result(String description, String methodName) {
			this(description);
			this.methodName = methodName;
		}

		public Result(String description, String methodName, String status) {
			this(description, methodName);
			this.status = status;
		}

		public String getDescription() {
			return description;
		}

		public String getMethodName() {
			return methodName;
		}

		public void setMethodName(String methodName) {
			this.methodName = methodName;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public Date getStartTime() {
			return startTime;
		}

		public void setStartTime(Date startTime) {
			this.startTime = startTime;
		}

		public Date getFinishTime() {
			return finishTime;
		}

		public void setFinishTime(Date finishTime) {
			this.finishTime = finishTime;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

	}

	/**
	 * the customer handler for logger
	 * 
	 * @author paulnpyiu
	 * @since 2015-08-27
	 */
	public static class log2ListHandler extends Handler {
		private List<Result> messageList;

		public log2ListHandler(List<Result> messageList) {
			this.messageList = messageList;
		}

		@Override
		public void publish(LogRecord record) {
			messageList.add(new Result(record.getMessage()));
		}

		public void print() {
			for (Result message : messageList) {
				logger.info(message.getDescription());
			}
		}

		public List<Result> getMessageList() {
			return messageList;
		}

		public void setMessageList(List<Result> messageList) {
			this.messageList = messageList;
		}

		@Override
		public void close() throws SecurityException {
		}

		@Override
		public void flush() {
		}

	}

	/**
	 * the customer formatter for logger
	 * 
	 * @author paulnpyiu
	 * @since 2015-08-27
	 */
	public static class loggerFormatter extends Formatter {
		@Override
		public String format(LogRecord record) {
			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			StackTraceElement se = stackTraceElements[8];
			if (se.getMethodName().equals("printProperty"))
				se = stackTraceElements[9];
			StringBuffer sb = new StringBuffer();
			sb.append(StringUtils.rightPad(record.getMessage(), reportWidth, "."));
			sb.append(debug ? " | @" + StringUtils.rightPad(se.getMethodName(), 30, "-") : "");
			sb.append(debug ? " (Line: " + se.getLineNumber() + ")" : "");
			sb.append(System.getProperty("line.separator"));
			return sb.toString();
		}
	}

	/**
	 * the interface for test case
	 * 
	 * @author paulnpyiu
	 * @since 2015-08-27
	 */
	public static interface TestCase {
		<T> List<T> doSearch(Map<String, String> parameters);
	}

	public static Class<?>[] createClassArray(Object[] parameters) {
		Class<?>[] clazz = new Class[parameters.length];
		if (parameters.length == 1 && parameters[0] instanceof Class<?>[]) {
			logger.info(parameters[0].toString());
			return new Class[0];
		}
		for (int i = 0; i < parameters.length; i++) {
			clazz[i] = parameters[i].getClass();
		}
		return clazz;
	}

	public static String substringAfterUncapitalize(String methodName, String separator) {
		return StringUtils.uncapitalize(StringUtils.substringAfter(methodName, separator));
	}
}
