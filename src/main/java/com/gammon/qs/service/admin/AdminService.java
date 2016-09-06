package com.gammon.qs.service.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.gammon.jde.webservice.serviceRequester.userCompanySet.UserCompanySetRequest;
import com.gammon.jde.webservice.serviceRequester.userCompanySet.UserCompanySetResponse;
import com.gammon.pcms.application.User;
import com.gammon.pcms.config.SecurityConfig;
import com.gammon.pcms.config.WebServiceConfig;
import com.gammon.pcms.dto.rs.consumer.gsf.JobSecurity;
import com.gammon.pcms.service.GSFService;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.JobInfoHBDao;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.service.security.SecurityService;
import com.gammon.qs.webservice.WSConfig;
import com.gammon.qs.webservice.WSSEHeaderWebServiceMessageCallback;

@Service
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class AdminService {

	private java.util.logging.Logger logger = java.util.logging.Logger.getLogger(this.getClass().toString());

	@Autowired
	@Qualifier("userCompanySetWebserviceTemplate")
	private WebServiceTemplate userCompanySetWebserviceTemplate;
	@Autowired
	@Qualifier("apWebservicePasswordConfig")
	private WSConfig wsConfig;
	@Autowired
	private GSFService gsfService;
	@Autowired
	private JobInfoHBDao jobInfoHBDao;
	@Autowired
	private SecurityConfig securityConfig;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private WebServiceConfig webServiceConfig;
	
	public Set<String> obtainCompanyListByUsernameViaWS(String username) throws Exception {
		Set<String> companySet = new HashSet<String>();
		try {
			UserCompanySetRequest request = new UserCompanySetRequest();
			request.setUsername(username);
			logger.info("Calling Web Service to JDE(UserCompanySet-unknown()): Request Object - Username: "+username);

			UserCompanySetResponse response = (UserCompanySetResponse) userCompanySetWebserviceTemplate.marshalSendAndReceive(request, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));

			if (response.getCompanySet() != null)
				companySet = response.getCompanySet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return companySet;
	}

	public List<String> obtainCanAccessJobNoList(String username){
		List<JobSecurity> jobSecurityList = obtainCompanyListByUsername(username);
		List<String> jobNumberList = new ArrayList<String>();
		for(JobSecurity jobSecurity : jobSecurityList){
			List<String> jobNumberByCompanyList = new ArrayList<String>();
			try {
				jobNumberByCompanyList = jobInfoHBDao.obtainJobNumberByCompany(jobSecurity.getCompany());
				jobNumberList.addAll(jobNumberByCompanyList);
			} catch (DatabaseOperationException e) {
				e.printStackTrace();
			}
		}
		return jobNumberList;
	}
	
	public List<JobInfo> obtainCanAccessJobInfoList(List<JobSecurity> jobSecurityList){
		Set<JobInfo> jobInfoSet = new TreeSet<JobInfo>();
		for (JobSecurity jobSecurity : jobSecurityList) {
			try {
				if (jobSecurity.getRoleName().equals(securityConfig.getRolePcmsJobAll())) {
					jobInfoSet = new TreeSet<JobInfo>(jobInfoHBDao.obtainAllJobs());
					break;
				}else{
					jobInfoSet.addAll(jobInfoHBDao.obtainAllJobInfoByCompany(jobSecurity.getCompany()));
				}
			} catch (DatabaseOperationException e) {
				e.printStackTrace();
			}
		}
		return new ArrayList<JobInfo>(jobInfoSet);
	}
	
	public Boolean canAccessJob(String noJob) {
		User user = securityService.getCurrentUser();
		if(user != null && !Arrays.asList(new String[]{
				webServiceConfig.getQsWsUsername(), webServiceConfig.getPcmsApiUsername()
				}).contains(user.getUsername())){
			return canAccessJob(user, noJob);
		} else {
			//bypass checking for ws provider and quartz
			logger.log(Level.INFO, "bypass canAccessJob checking for user:" + user.getUsername());
			return true;
		}
	}
	
	public Boolean canAccessJob(String username, String noJob){
		User user = gsfService.getRole(username);
		return canAccessJob(user, noJob);
	}
	
	/**
	 * Return true if user have access right to the jobNo
	 * or throw AccessDeniedException (*never return false)
	 * @param user
	 * @param noJob
	 * @return
	 * @throws AccessDeniedException
	 */
	public Boolean canAccessJob(User user, String noJob) {
		if (user.hasRole(securityConfig.getRolePcmsJobAll())) {
			return true;
		} else {
			if (StringUtils.isNotBlank(noJob)){
				List<String> jobList = obtainCanAccessJobNoList(user.getUsername());
				if(jobList.contains(noJob)) return true;
			} 
			throw new AccessDeniedException(user.getFullname() + " cannot access job " + noJob);
		}
	}

	public List<JobSecurity> obtainCompanyListByUsername(String username) {
		return gsfService.getJobSecurityList(username);
	}

}
