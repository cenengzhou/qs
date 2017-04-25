package com.gammon.qs.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.gammon.jde.webservice.serviceRequester.userAccessRights.UserAccessRightsRequest;
import com.gammon.jde.webservice.serviceRequester.userAccessRights.UserAccessRightsResponse;
import com.gammon.jde.webservice.serviceRequester.userAccessRights.UserAccessRightsResponseList;
import com.gammon.qs.webservice.WSSEHeaderWebServiceMessageCallback;
@Service
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class UserAccessRightsService {
	@Autowired
	@Qualifier("UserAccessRightsTemplate")
	private WebServiceTemplate userAccessRightsWebserviceTemplate;

	public List<String> getAccessRights(String username, String functionName) throws Exception {
		//		ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"spring-config.xml"});
		try {

			UserAccessRightsRequest requestObj = new UserAccessRightsRequest();
			requestObj.setFunctionName(functionName);
			requestObj.setUsername(username);
			Object tmpObj = userAccessRightsWebserviceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback("revampSOA", "revampSOA"));

			UserAccessRightsResponseList responseList = (UserAccessRightsResponseList)tmpObj;
			ArrayList<UserAccessRightsResponse> accessRightsReturned = responseList.getUserAccessRightsList();
			ArrayList<String> accessRights = new ArrayList<String>();
			for (UserAccessRightsResponse userAccessRightsResponse:accessRightsReturned){
				accessRights.add(userAccessRightsResponse.getAccessRight());
			}
			return accessRights;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}
	
}
