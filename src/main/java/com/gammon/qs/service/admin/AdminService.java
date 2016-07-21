package com.gammon.qs.service.admin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.gammon.jde.webservice.serviceRequester.userCompanySet.UserCompanySetRequest;
import com.gammon.jde.webservice.serviceRequester.userCompanySet.UserCompanySetResponse;
import com.gammon.pcms.config.SecurityConfig;
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
//			if(jobSecurity.getRoleName().equals(securityConfig.getRolePcmsJobAll())){
//				try {
//					List<String> allJobCompany = jobInfoHBDao.obtainAllJobCompany();
//					for(String company : allJobCompany){
//						jobNumberByCompanyList.addAll(jobInfoHBDao.obtainJobNumberByCompany(company));
//					}
//					jobNumberList.addAll(jobNumberByCompanyList);
//				} catch (DatabaseOperationException e) {
//					e.printStackTrace();
//				}
//				break;
//			}
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
	
	public Boolean canAccessJob(String userName, String jobNumber)throws DataAccessException{
		if(securityService.getCurrentUser().hasRole(securityConfig.getRolePcmsJobAll())){
			return true;
		} else {
			return obtainCanAccessJobNoList(userName).contains(jobNumber);
		}
	}

	public List<JobSecurity> obtainCompanyListByUsername(String username) {
		return gsfService.getJobSecurityList(username);
	}

}
