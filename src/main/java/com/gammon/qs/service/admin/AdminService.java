package com.gammon.qs.service.admin;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.gammon.jde.webservice.serviceRequester.userCompanySet.UserCompanySetRequest;
import com.gammon.jde.webservice.serviceRequester.userCompanySet.UserCompanySetResponse;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.webservice.WSConfig;
import com.gammon.qs.webservice.WSSEHeaderWebServiceMessageCallback;
@Service
@Transactional(rollbackFor = Exception.class)
public class AdminService {

	private java.util.logging.Logger logger = java.util.logging.Logger.getLogger(this.getClass().toString());

	@Autowired
	@Qualifier("userCompanySetWebserviceTemplate")
	private WebServiceTemplate userCompanySetWebserviceTemplate;
	@Autowired
	@Qualifier("apWebservicePasswordConfig")
	private WSConfig wsConfig;

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

	public Boolean canAccessJob(String userName, String jobNumber)throws DatabaseOperationException{
		//TODO: GSF | JobDivisionInfo obtainJobDivisionInfoByJobNumber(jobNumber) | remark AdminService.canAccessJob(String userName, String jobNumber)
		throw new RuntimeException("GSF | JobDivisionInfo obtainJobDivisionInfoByJobNumber(jobNumber) | remark AdminService.canAccessJob(String userName, String jobNumber)");
//		// obtain the job information from approval system
//		JobDivisionInfo jobDivisionInfo = jobDivisionInfoDao.obtainJobDivisionInfoByJobNumber(jobNumber);
//
//		if(jobDivisionInfo==null){
//			logger.info("Job: ["+jobNumber +"] does not exist in Approval System.");
//			return true;
//		}
//
//		return jobSecurityDao.checkJobSecurity(userName, jobDivisionInfo.getCompany(), jobDivisionInfo.getDivision(), jobDivisionInfo.getDepartment(), jobNumber);
	}
//
	//TODO: GSF | List<JobSecurity> obtainJobSecurityList(username) | remark AdminService.obtainCompanyListByUsername(String username)
//	public List<JobSecurity> obtainCompanyListByUsername(String username) throws DatabaseOperationException {
//		return jobSecurityDao.obtainJobSecurityList(username);
//	}

}
