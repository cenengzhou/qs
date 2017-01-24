package com.gammon.qs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.gammon.jde.webservice.serviceRequester.UserAccessJobs.UserAccessJobsRequestObj;
import com.gammon.jde.webservice.serviceRequester.UserAccessJobs.UserAccessJobsResponseObj;
import com.gammon.qs.webservice.WSSEHeaderWebServiceMessageCallback;
@Service
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class UserAccessJobsService {
	@Autowired
	@Qualifier("UserAccessJobsTemplate")
	private WebServiceTemplate userAccessJobsWebserviceTemplate;
	
	public Boolean canAccessJob(String username, String jobNumber) {
		try {
			UserAccessJobsRequestObj requestObj = new UserAccessJobsRequestObj();
			requestObj.setJobNumber(jobNumber);
			requestObj.setUsername(username);
			UserAccessJobsResponseObj responseObj = (UserAccessJobsResponseObj)userAccessJobsWebserviceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback("revampSOA", "revampSOA"));
			Boolean returned = responseObj.getCanAccessJob();
			return returned;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
		
	}

}
