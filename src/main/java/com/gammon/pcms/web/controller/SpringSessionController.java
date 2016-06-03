package com.gammon.pcms.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gammon.pcms.dao.SpringSessionSPDao;
import com.gammon.pcms.model.SpringSession;
import com.gammon.qs.dao.SpringSessionHBDao;


@RestController
@RequestMapping(value = "service", method = RequestMethod.POST)
@Transactional
public class SpringSessionController {

	@Autowired
	private SpringSessionSPDao springSessionSPDao;
	@Autowired
	private SpringSessionHBDao springSessionHBDao;
	@Autowired
	private SessionRegistry sessionRegistry;
	@Autowired
	private ObjectMapper objectMapper;
	
	@RequestMapping(value = "GetSpringSessionListSP.json")
	public List<SessionInformation> getSpringSessionListSP(){
//		return (List<SpringSession>) springSessionSPDao.findAll();
		List<SessionInformation> sessionInformationList = new ArrayList<SessionInformation>();
		for(Object principal : sessionRegistry.getAllPrincipals()){
			for(SessionInformation session : sessionRegistry.getAllSessions(principal, true)){
				sessionInformationList.add(session);
			}
		}
		return sessionInformationList;
	}
	
	@RequestMapping(value = "InvalidateSession")
	public void invalidateSession(@RequestParam(required = false)String[] selectedIds, HttpServletRequest request, HttpServletResponse response){
//		 for (Object principal : sessionRegistry.getAllPrincipals()) {
//		        for (SessionInformation session : sessionRegistry.getAllSessions(principal, false)) {
////		        	session.expireNow();
//		        }
//		    }
		if(selectedIds == null){
			try {
				selectedIds = objectMapper.readValue(request.getInputStream(), String[].class);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for(String sessionId : selectedIds){
			SessionInformation session = sessionRegistry.getSessionInformation(sessionId);
			if(session != null){
				session.expireNow();
			}
//			springSessionSPDao.deleteBySessionId(sessionId);
		}
	}
	
	@RequestMapping(value = "GetSpringSessionListHB.json")
	public List<SpringSession> getSpringSessionListHB(){
		return springSessionHBDao.getSpringSessionList();
	}
}
