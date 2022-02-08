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
import com.gammon.pcms.dto.rs.consumer.gsf.GetJobSecurity;
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

	public List<String> obtainCanAccessJobNoStringList(){
		User user = securityService.getCurrentUser();
		return obtainCanAccessJobNoStringList(user.getUsername());
	}
	
	public List<String> obtainCanAccessJobNoStringList(String username) {
		List<GetJobSecurity.Result> jobSecurityList = obtainJobSecurityListByUsername(username);
		List<String> jobNumberList = new ArrayList<>();
		List<String> jobNumberExcludeList = new ArrayList<>();
		boolean jobAll = false;
		if(jobSecurityList != null)
		for(GetJobSecurity.Result jobSecurity : jobSecurityList){
			if(jobSecurity.getAccessRight().equals(GetJobSecurity.Result.AccessRight.EXCLUDE)) {
				jobNumberExcludeList.add(jobSecurity.getJobNo());
				continue;
			}
			if(jobSecurity.getRoleName() != null && jobSecurity.getRoleName().equals(securityConfig.getRolePcmsJobAll())){
				jobAll = true;
				continue;
			}
			if(!jobSecurity.getJobNo().equals("NA"))
				if(jobSecurity.getAccessRight().equals(GetJobSecurity.Result.AccessRight.INCLUDE) || jobSecurity.getRoleName() != null)
					jobNumberList.add(jobSecurity.getJobNo());
		}
		if(jobAll){
			jobNumberList.clear();
			if(jobNumberExcludeList.isEmpty()){
				jobNumberList.add("JOB_ALL");
			} else {
				try {
					jobNumberList.addAll(jobInfoHBDao.obtainAllJobNoWithExcludeList(jobNumberExcludeList));
					securityService.getCurrentUser().setJobNoExcludeList(jobNumberExcludeList);;
				} catch (DatabaseOperationException e) {
					e.printStackTrace();
				}
			}
		} 
		jobNumberList.removeAll(jobNumberExcludeList);
		return jobNumberList;
	}
	
	public List<JobInfo> obtainCanAccessJobInfoList(Boolean isCompleted) throws DatabaseOperationException{
		return obtainCanAccessJobInfoList(obtainCanAccessJobNoStringList(), isCompleted);
	}
	
	public List<JobInfo> obtainCanAccessJobInfoList(List<String> jobNoStringList, Boolean isCompleted) throws DatabaseOperationException{
		Set<JobInfo> jobInfoSet = new TreeSet<JobInfo>();
		jobInfoSet.addAll(jobInfoHBDao.obtainJobInfoByJobNumberList(jobNoStringList, isCompleted));
		return new ArrayList<JobInfo>(jobInfoSet);
	}
	
	public List<JobInfo> obtainCanAccessJobInfoList(boolean isCompletedJob){
		Set<JobInfo> jobInfoSet = new TreeSet<JobInfo>();
		List<String> canAccessJobList = obtainCanAccessJobNoStringList();
		for(int i=0; i < canAccessJobList.size(); i+=899){
			int from = i;
			int to = i+899 < canAccessJobList.size() ? i+899 : canAccessJobList.size(); 
			logger.info("obtainCanAccessJobInfoList from:" + from + " to:" + to);
			jobInfoSet.addAll(jobInfoHBDao.obtainJobInfoByJobNumberList(canAccessJobList.subList(from, to), isCompletedJob));
		}
		return new ArrayList<JobInfo>(jobInfoSet);
	}
	
	public Boolean canAccessJob(String noJob) {
		User user = securityService.getCurrentUser();
		if(user != null && !Arrays.asList(new String[]{
				webServiceConfig.getPcmsApi("USERNAME")
				}).contains(user.getUsername())){
			return canAccessJob(user, noJob);
		} else {
			//bypass checking for ws provider and quartz
			logger.log(Level.INFO, "bypass canAccessJob checking: Called by web service / quartz scheduler / User is null.");
			return true;
		}
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
		if (user.hasRole(securityConfig.getRolePcmsJobAll()) && user.getJobNoExcludeList().isEmpty()) {
			return true;
		} else {
			if (StringUtils.isNotBlank(noJob)){
				List<String> jobList = obtainCanAccessJobNoStringList(user.getUsername());
				if(jobList.contains(noJob)) return true;
			} 
			throw new AccessDeniedException(user.getFullname() + " cannot access job " + noJob);
		}
	}
	
	public List<String> obtainCompanyCodeListByCurrentUser() throws DatabaseOperationException{
		List<String> canAccessJobList = obtainCanAccessJobNoStringList();
		List<String> companyCodeList = new ArrayList<>();
		for(int i=0; i < canAccessJobList.size(); i+=500){
			int from = i;
			int to = i+499 < canAccessJobList.size() ? i+499 : canAccessJobList.size(); 
			logger.info("obtainCompanyCodeListByCurrentUser from:" + from + " to:" + to);
			companyCodeList.addAll(jobInfoHBDao.obtainCompanyCodeByJobNoList(canAccessJobList.subList(from, to)));
		}
		return companyCodeList;
	}
	
	public List<GetJobSecurity.Result> obtainJobSecurityListByUsername(String username) {
		return gsfService.getJobSecurityList(username);
	}

}
