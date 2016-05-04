package com.gammon.qs.client;

import com.gammon.qs.application.User;
import com.gammon.qs.client.controller.GlobalSectionController;
import com.gammon.qs.client.repository.UserServiceRemote;
import com.gammon.qs.client.repository.UserServiceRemoteAsync;
import com.gammon.qs.client.ui.GlobalMessageTicket;
import com.gammon.qs.client.ui.util.UIUtil;
import com.gammon.qs.shared.GlobalParameter;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class Main implements EntryPoint{
	
	private UserServiceRemoteAsync userService;
	private GlobalMessageTicket globalMessageTicket;
	
	public void onModuleLoad() {
		globalMessageTicket = new GlobalMessageTicket();
		
		userService = (UserServiceRemoteAsync) GWT.create(UserServiceRemote.class);
		((ServiceDefTarget)userService).setServiceEntryPoint(GlobalParameter.USER_SERVICE_URL);
		
		userService.getUserDetails(new AsyncCallback<User>() {
			public void onSuccess(User user) {
				globalMessageTicket.refresh();
				new GlobalSectionController(user);
			}
			
			public void onFailure(Throwable e) {
				UIUtil.alert("Failed: Unable to obtain User Detail @onModuleLoad()");
				UIUtil.throwException(e);
			}
		});
	}

}
