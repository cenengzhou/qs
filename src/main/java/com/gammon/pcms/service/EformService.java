package com.gammon.pcms.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.gammon.pcms.config.WebServiceConfig;
import com.gammon.pcms.helper.RestTemplateHelper;
import com.gammon.pcms.helper.RestTemplateHelper.AuthHeader;

@Service
public class EformService {

	public static final String URL_KEY = "URL";
	public static final String USERNAME_KEY = "USERNAME";
	public static final String PASSWORD_KEY = "PASSWORD";
	public static final String PROCESS_START_KEY = "PROCESS_START";
	public static final String APPROVAL_START_KEY = "APPROVAL_START";
	public static final String PROCESS_ABORT_KEY = "PROCESS_ABORT";
	public static final String RETURN_DECISION_MAP_KEY = "RETURN_DECISION_MAP";
	public static final String ACTIVE_TASK_KEY = "ACTIVE_TASK";
	public static final String JOBNO_KEY = "jobNo";
	public static final String APP_CODE_KEY = "applicationCode";
	public static final String FORM_CODE_KEY = "formCode";
	public static final String REQUESTER_KEY = "requester";
	public static final String REFNO_KEY = "refNo";
	public static final String PERSONNEL_CODE_KEY = "PersonnelFormCode";

	@Autowired
	private RestTemplateHelper restTemplateHelper;
	@Autowired
	private WebServiceConfig webServiceConfig;

	private String wfBaseUrl;
	private String username;
	private String password;

	@SuppressWarnings("serial")
	public Long processStart(String formCode, Map<String, String> requestObject) {
		String apiUrl = replaceVariable(
				webServiceConfig.getWsWfApi(PROCESS_START_KEY),
				new HashMap<String, String>(){{
					put(APP_CODE_KEY, webServiceConfig.getWsWfParam(APP_CODE_KEY));
					put(FORM_CODE_KEY, formCode);
				}});
		return restTemplateHelper.restCall(
					wfBaseUrl + apiUrl, 
					username, 
					password, 
					HttpMethod.POST, 
					AuthHeader.Basic,
					requestObject, 
					Long.class
				).getBody();
	}

	@SuppressWarnings({ "unchecked", "serial" })
	public Map<String, Object> approvalStart(Long refNo) {
		String apiUrl = replaceVariable(
				webServiceConfig.getWsWfApi(APPROVAL_START_KEY),
				new HashMap<String, String>(){{
					put(REFNO_KEY, "" + refNo);
				}});
		return restTemplateHelper
				.restCall(
					wfBaseUrl + apiUrl, 
					username, 
					password, 
					HttpMethod.POST, 
					AuthHeader.Basic, 
					null, 
					Map.class
				).getBody();
	}

	@SuppressWarnings("serial")
	public void processAbort(Long refNo) {
		String apiUrl = replaceVariable(
				webServiceConfig.getWsWfApi(PROCESS_ABORT_KEY),
				new HashMap<String, String>(){{
					put(REFNO_KEY, "" + refNo);
				}});
		restTemplateHelper.restCall(
					wfBaseUrl + apiUrl, 
					username, 
					password, 
					HttpMethod.POST, 
					AuthHeader.Basic, 
					null,
					Map.class
				);
	}

	@SuppressWarnings({ "unchecked", "serial" })
	public Map<String, Object> returnDecisionMap(Long refNo) {
		String apiUrl = replaceVariable(
				webServiceConfig.getWsWfApi(RETURN_DECISION_MAP_KEY),
				new HashMap<String, String>(){{
					put(REFNO_KEY, "" + refNo);
				}});
		return restTemplateHelper
				.restCall(
					wfBaseUrl + apiUrl, 
					username, 
					password, 
					HttpMethod.POST, 
					AuthHeader.Basic, 
					null, 
					Map.class
				).getBody();
	}

	@SuppressWarnings({ "unchecked", "serial" })
	public Map<String, Object> activeTask(int refNo) {
		String apiUrl = replaceVariable(
				webServiceConfig.getWsWfApi(ACTIVE_TASK_KEY),
				new HashMap<String, String>(){{
					put(REFNO_KEY, "" + refNo);
				}});
		return restTemplateHelper
				.restCall(
					wfBaseUrl + apiUrl, 
					username, 
					password,
					HttpMethod.POST, 
					AuthHeader.Basic, 
					null, 
					Map.class
				).getBody();
	}
	
	public String replaceVariable(String str, Map<String, String> map) {
		StringBuffer buf = new StringBuffer();
		map.forEach((key, value) -> {
			String strForReplace = buf.length() > 0 ? buf.toString() : str;
			buf.setLength(0);
			buf.append(strForReplace.replaceAll("\\{" + key + "\\}", value));
		});
		return buf.toString();
	}
	
	@PostConstruct
	public void postConstruct() throws Exception {
		wfBaseUrl = webServiceConfig.getWsWf(URL_KEY);
		username = webServiceConfig.getWsWf(USERNAME_KEY);
		password = webServiceConfig.getWsWf(PASSWORD_KEY);
	}

}
