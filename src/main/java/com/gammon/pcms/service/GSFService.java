package com.gammon.pcms.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.gammon.pcms.application.User;
import com.gammon.pcms.config.SecurityConfig;
import com.gammon.pcms.config.WebServiceConfig;
import com.gammon.pcms.dto.rs.consumer.gsf.GetFunctionSecurity;
import com.gammon.pcms.dto.rs.consumer.gsf.GetJobSecurity;
import com.gammon.pcms.dto.rs.consumer.gsf.GetRole;
import com.gammon.pcms.dto.rs.consumer.gsf.GetUserListWithStaffId;
import com.gammon.pcms.helper.RestTemplateHelper;
import com.gammon.qs.service.admin.AdminService;
import com.gammon.qs.service.user.LdapService;

@Service
public class GSFService {

	Logger logger = Logger.getLogger(GSFService.class);
	
	@Autowired
	private WebServiceConfig webServiceConfig;
	@Autowired
	private RestTemplateHelper restTemplateHelper;
	@Autowired
	private LdapService ldapService;
	@Autowired
	private AdminService adminService;
	@Autowired
	private SecurityConfig securityConfig;

	private List<GetUserListWithStaffId.Result> allApproverList;
	private Map<String, Set<GetUserListWithStaffId.Result>> approvalByJobMap = new HashMap<>();
	
	public User getRole(String username) {
		User user = new User();
		String[] login = username.replace("gamska\\", "").split("@");
		String title = ldapService.getObjectAttribute(login[0], "Title");
		String displayname = ldapService.getObjectAttribute(login[0], "displayName");
		GetRole.Request roleRequest = new GetRole.Request(WebServiceConfig.GSF_APPLICATION_CODE,
				"gamska\\" + login[0]);
		String wsUrl = webServiceConfig.getWsGsf("URL") + "/" + WebServiceConfig.GSF_GETROLE;
		logger.debug("GetRole from:" + wsUrl);
		try {
			ResponseEntity<GetRole.Response> roleResponse = getResponseEntity(wsUrl, roleRequest, GetRole.Response.class);
			user = roleResponse.getBody().getUser();
		} catch (RestClientException | HttpMessageNotReadableException e) {
			e.printStackTrace();
			user.getUserRoleList().add(new User.Role(securityConfig.getPcmsRole("MAINTENANCE"), "ROLE_" + securityConfig.getPcmsRole("MAINTENANCE")));
		}
		user.setUsername(login[0]);
		user.setTitle(title);
		user.setFullname(displayname);
		if (login.length == 2) {
			user.setAuthType("Kerberos");
			user.setDomainName(login[1]);
		}		
		logger.debug("User object return:" + user.toString());
		return user;
	}

	public List<GetJobSecurity.Result> getJobSecurityList(String username) {
		String[] login = username.replace("gamska\\", "").split("@");
		GetJobSecurity.Request jobSecurityRequest = new GetJobSecurity.Request(WebServiceConfig.GSF_APPLICATION_CODE,
				"gamska\\" + login[0]);
		ResponseEntity<GetJobSecurity.Response> jobSecurityResponse = getResponseEntity(
				webServiceConfig.getWsGsf("URL") + "/" + WebServiceConfig.GSF_GETJOBSECURITY, jobSecurityRequest, GetJobSecurity.Response.class);
		return jobSecurityResponse.getBody().getResultList();
	}

	public List<GetUserListWithStaffId.Result> getAllApproverList(boolean reload){
		if(reload || allApproverList == null) {
			GetUserListWithStaffId.Request userListRequest = new GetUserListWithStaffId.Request(WebServiceConfig.GSF_APPLICATION_CODE, WebServiceConfig.GSF_REVIEWER_FN);
	//		if(jobNo != null) userListRequest.setJobNo(jobNo);
			ResponseEntity<GetUserListWithStaffId.Response> userListResponse = getResponseEntity(
					webServiceConfig.getWsGsf("URL") + "/" + WebServiceConfig.GSF_GETUSERLISTWITHSTAFFID, userListRequest, GetUserListWithStaffId.Response.class);
			allApproverList = userListResponse.getBody().getResultList();
		}
		return allApproverList;
	}
	
	public List<GetUserListWithStaffId.Result> getApproverListByJob(String jobNo, boolean reload){
		List<GetUserListWithStaffId.Result> approverList = getAllApproverList(reload);
		Set<GetUserListWithStaffId.Result> approverForJobSet = new TreeSet<>();
		if(reload || approvalByJobMap.get(jobNo) == null){
			if(approvalByJobMap.get(jobNo) == null) approvalByJobMap.put(jobNo, new TreeSet<>());
			for(GetUserListWithStaffId.Result approver : approverList){
				List<String> jobNoList = adminService.obtainCanAccessJobNoStringList(approver.getUserAccount());
				if(jobNoList != null && jobNoList.size() > 0 ){
					for(String canAccessJob : jobNoList){
						if(approvalByJobMap.get(canAccessJob) == null) {
							approvalByJobMap.put(canAccessJob, new TreeSet<>());
						}
						approvalByJobMap.get(canAccessJob).add(approver);
					}
				}
			}
		} 
		approverForJobSet = approvalByJobMap.get(jobNo);
		if(approvalByJobMap.get("JOB_ALL") != null) approverForJobSet.addAll(approvalByJobMap.get("JOB_ALL"));
		return new ArrayList<GetUserListWithStaffId.Result>(approverForJobSet);
	}
	
	public boolean allowFunctionWrite(String fn, String roleName){
		boolean allowWrite = false;
		GetFunctionSecurity.Request functionSecurityRequest = new GetFunctionSecurity.Request(
				WebServiceConfig.GSF_APPLICATION_CODE, "gamska\\" + SecurityContextHolder.getContext().getAuthentication().getName(), fn, "ROLE_" + roleName);
		ResponseEntity<GetFunctionSecurity.Response> functionSecurityResponse = getResponseEntity(
				webServiceConfig.getWsGsf("URL") + "/" + WebServiceConfig.GSF_GETFUNCTIONSECURITY, functionSecurityRequest, GetFunctionSecurity.Response.class);
		List<GetFunctionSecurity.Result> functionSecurityList = functionSecurityResponse.getBody().getResultList();
		if(functionSecurityList!= null){
			allowWrite = GetFunctionSecurity.AccessRight.WRITE.equals(functionSecurityList.get(0).getAccessRight());
		}
		return allowWrite;
	}
	
	public <Q, S> ResponseEntity<S> getResponseEntity(String path, Q request, Class<S> response) {
		URI gsf = null;
		try {
			gsf = new URI(path);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		RestTemplate restTemplate = restTemplateHelper.getRestTemplate(gsf.getHost(), null, null);
		return restTemplate.postForEntity(gsf.toString(), request, response);
	}

}
