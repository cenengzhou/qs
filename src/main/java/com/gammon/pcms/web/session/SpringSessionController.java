package com.gammon.pcms.web.session;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gammon.pcms.dao.SpringSessionSPDao;
import com.gammon.pcms.dto.SessionDTO;
import com.gammon.pcms.helper.JsonHelper;
import com.gammon.pcms.model.SpringSession;


@RestController
@Transactional
@Profile(value = "SPRING_SESSION")
@RequestMapping(value = "service", method = RequestMethod.POST)
public class SpringSessionController {

	@Autowired
	private SessionRegistry sessionRegistry;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private SpringSessionSPDao springSessionSPDao;
	
	@RequestMapping(value = "GetSessionList")
	public List<SessionDTO> getSessionList(HttpServletRequest request, HttpServletResponse response){
		List<SessionInformation> sessionInformationList = new ArrayList<SessionInformation>();
		List<SessionDTO> sessionDTOList = new ArrayList<SessionDTO>();
		List<Object> principalList = sessionRegistry.getAllPrincipals();
		for(Object principal : principalList){
			sessionInformationList.addAll(sessionRegistry.getAllSessions(principal, true));
		}
		for(SessionInformation sessionInformation : sessionInformationList){
			SpringSession springSession = springSessionSPDao.findBySessionId(sessionInformation.getSessionId());
			if(springSession != null){
				SessionDTO sessionDTO = new SessionDTO(sessionInformation, springSession);
				sessionDTOList.add(sessionDTO);
			}
		}
		List<SpringSession> nullPrincipalList = springSessionSPDao.findByPrincipalName(null);
		for(SpringSession springSession : nullPrincipalList){
			SessionDTO sessionDTO = new SessionDTO(springSession);
			sessionDTOList.add(sessionDTO);
		}
		return sessionDTOList;
	}
	
	@RequestMapping(value = "InvalidateSessionList")
	public void invalidateSessionList(@RequestParam(required = false)List<String> sessionIdList, HttpServletRequest request, HttpServletResponse response){
		JavaType valueType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
		sessionIdList = JsonHelper.getRequestParam(sessionIdList, valueType, objectMapper, request);
		GeneralSessionController.invalidateSessionList(sessionIdList, sessionRegistry);
		for(String sessionId : sessionIdList){
			springSessionSPDao.deleteBySessionId(sessionId);
		}
	}

}
