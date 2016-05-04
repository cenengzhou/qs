package com.gammon.qs.service.admin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.gammon.jde.webservice.serviceRequester.userCompanySet.UserCompanySetRequest;
import com.gammon.jde.webservice.serviceRequester.userCompanySet.UserCompanySetResponse;
import com.gammon.qs.application.Authority;
import com.gammon.qs.application.GeneralPreferencesKey;
import com.gammon.qs.application.JobDivisionInfo;
import com.gammon.qs.application.JobSecurity;
import com.gammon.qs.application.User;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.application.AuthorityHBDao;
import com.gammon.qs.dao.application.JobDivisionInfoHBDao;
import com.gammon.qs.dao.application.JobSecurityHBDao;
import com.gammon.qs.dao.application.UserHBDao;
import com.gammon.qs.webservice.WSConfig;
import com.gammon.qs.webservice.WSSEHeaderWebServiceMessageCallback;
@Service
@Transactional(rollbackFor = Exception.class)
public class AdminService {
	
	private java.util.logging.Logger logger = java.util.logging.Logger.getLogger(this.getClass().toString());
	@Autowired
	private AuthorityHBDao authorityDao;
	
	@Autowired
	private UserHBDao userDao;
	
	@Autowired
	private JobSecurityHBDao jobSecurityDao;
	
	@Autowired
	private JobDivisionInfoHBDao jobDivisionInfoDao; // added by brian on 20110127 for add job security to appraisal enquiry
	@Autowired
	@Qualifier("userCompanySetWebserviceTemplate")
	private WebServiceTemplate userCompanySetWebserviceTemplate;
	@Autowired
	@Qualifier("apWebservicePasswordConfig")
	private WSConfig wsConfig;

	public List<User> searchUserByUsername(String username)
			throws DatabaseOperationException {

		return userDao.searchByUsername(username);
	}

	public List<Authority> getAllAuthorities() throws DatabaseOperationException {
		return authorityDao.getAllActive();
	}
	
	public Authority getAuthority(Long id) throws DatabaseOperationException {
		return authorityDao.get(id);
	}
	
	public List<User> getAllActiveUsers() throws DatabaseOperationException {
		return userDao.getAllActive();
	}
	public List<User> getAllUsers() throws DatabaseOperationException {
		return userDao.getAll();
	}

	public User getUserById(Long id) throws DatabaseOperationException {
		return userDao.get(id);
	}
	
	public User getUserByUsername(String username) throws DatabaseOperationException {
		return userDao.getByUsername(username);
	}
	
	public void saveUser(User user, String username)
			throws DatabaseOperationException {
		user.populate(username);
		userDao.saveOrUpdate(user);
	}
	
	public void removeUser(User user) throws DatabaseOperationException {
		userDao.delete(user);
	}

	public void saveUserScreenPreferences(User user) throws DatabaseOperationException {
		User dbUser = null;
		dbUser = getUserById(user.getId());
		Set<String> keySet = user.getScreenPreferences().keySet();
		for(String key:keySet) {
			dbUser.getScreenPreferences().put(key, user.getScreenPreferences().get(key));
		}
		
		saveUser(dbUser, user.getUsername());
		
	}
	
	public void saveUserGeneralPreferences(User user)
		throws DatabaseOperationException {
		User dbUser = null;

		dbUser = getUserById(user.getId());
		
		dbUser.getGeneralPreferences().remove(GeneralPreferencesKey.AMOUNT_DECIMAL_PLACES);
		dbUser.getGeneralPreferences().remove(GeneralPreferencesKey.DEFAULT_JOB);
		dbUser.getGeneralPreferences().remove(GeneralPreferencesKey.QUANTITY_DECIMAL_PLACES);
		dbUser.getGeneralPreferences().remove(GeneralPreferencesKey.RATE_DECIMAL_PLACES);
		userDao.saveOrUpdate(dbUser);
		dbUser = getUserById(user.getId());
		
		Set<String> keySet = user.getGeneralPreferences().keySet();
		for(String key:keySet) 
			dbUser.getGeneralPreferences().put(key, user.getGeneralPreferences().get(key));
		
		saveUser(dbUser, user.getUsername());
	}

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
		// obtain the job information from approval system
		JobDivisionInfo jobDivisionInfo = jobDivisionInfoDao.obtainJobDivisionInfoByJobNumber(jobNumber);

		if(jobDivisionInfo==null){
			logger.info("Job: ["+jobNumber +"] does not exist in Approval System.");
			return true;
		}

		return jobSecurityDao.checkJobSecurity(userName, jobDivisionInfo.getCompany(), jobDivisionInfo.getDivision(), jobDivisionInfo.getDepartment(), jobNumber);
	}

	public List<JobSecurity> obtainCompanyListByUsername(String username) throws DatabaseOperationException {
		return jobSecurityDao.obtainJobSecurityList(username);
	}

}
