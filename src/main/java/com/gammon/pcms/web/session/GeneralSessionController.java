package com.gammon.pcms.web.session;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional
@RequestMapping(value = "service", method = RequestMethod.POST)
public class GeneralSessionController {

	@Autowired
	private SessionRegistry sessionRegistry;

	@RequestMapping(value = "GetCurrentSessionId")
	public String getCurrentSessionId(HttpSession session, HttpServletRequest request, HttpServletResponse response){
		return session.getId();	
	}
	
	@RequestMapping(value = "ValidateCurrentSession")
	public boolean ValidateCurrentSession(HttpServletRequest request, HttpServletResponse response){
		SessionInformation sessionInformation = sessionRegistry.getSessionInformation(request.getSession().getId());
		return sessionInformation != null ? !sessionInformation.isExpired() : false;
	}

	public static void invalidateSessionList(List<String> sessionIds, SessionRegistry sessionRegistry){
		for(String sessionId : sessionIds){
			if(sessionId == null) continue;
			SessionInformation session = sessionRegistry.getSessionInformation(sessionId);
			if(session != null){
				session.expireNow();
			}
			sessionRegistry.removeSessionInformation(sessionId);
		}
	}
}
