package com.gammon.qs.service.admin;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.gammon.qs.application.ActiveSession;
import com.gammon.qs.application.User;
import com.gammon.qs.dao.AccountCodeWSDao;
import com.gammon.qs.dao.application.ActiveSessionHBDao;
import com.gammon.qs.dao.application.UserHBDao;

@Service
@Transactional(rollbackFor = Exception.class)
public class ActiveSessionService  {
	private Logger logger = Logger.getLogger(AccountCodeWSDao.class.getName());
	//private List<ActiveSession> activeSessions = new LinkedList<ActiveSession>();
	@Autowired
	private ActiveSessionHBDao activeSessionDao;
	@Autowired
	private UserHBDao userDao;
	@Autowired
	private PlatformTransactionManager transactionManager;
	@Autowired
	private EnvironmentConfig environmentConfig;
	
	@PostConstruct
	public void cleanUpLastShutdown() {
		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
            	removeActiveSessionsOfNode(environmentConfig.getNodeName());
            }
        });
	}
	
	private void removeActiveSessionsOfNode(String hostname) {
		try {
			logger.info("cleanUpLastShutdown - hostname: "+hostname);
			List<ActiveSession> existingSessions = activeSessionDao.getByHostName(hostname);
			logger.info("Number of existingSessions: "+existingSessions.size());
			for (ActiveSession existingSession:existingSessions) {logger.info("existingSession to be deleted:"+existingSession.getHostname());
				activeSessionDao.delete(existingSession);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void addOrUpdateActiveSession(ActiveSession activeSession) {
		try {
			//List<ActiveSession> existingSessions = activeSessionDao.getByUsername(activeSession.getUser().getUsername());
			
			if (activeSession.getLoginDateTime() != null ){
				User persistedUser = userDao.getByUsername(activeSession.getUser().getUsername());
				activeSession.setUser(persistedUser);
				activeSession.populate("SYSTEM");
				activeSessionDao.saveOrUpdate(activeSession);
			} else {
				ActiveSession existingSession = activeSessionDao.getByHostnameAndSessionId(activeSession.getHostname(), activeSession.getSessionId());
				if (existingSession != null) {
					existingSession.update(activeSession);
					existingSession.populate("SYSTEM");
					activeSessionDao.saveOrUpdate(existingSession);
				}
			}
			/*
			if (existingSessions.size() == 0) {
				User persistedUser = userDao.getByUsername(activeSession.getUser().getUsername());
				activeSession.setUser(persistedUser);
				activeSession.populate("SYSTEM");
				activeSessionDao.save(activeSession);
			} 
			*/
			/*
			else if (existingSessions.size() == 1) {
				ActiveSession existingSession = existingSessions.get(0);
				existingSession.update(activeSession);
				existingSession.populate("SYSTEM");
				activeSessionDao.save(existingSession);
			} else {
				boolean updated = false;
				for(ActiveSession existingSession:existingSessions) {
					if(!updated) {
						existingSession.update(activeSession);
						existingSession.populate("SYSTEM");
						activeSessionDao.save(existingSession);
						updated = true;
					} else {
						activeSessionDao.delete(existingSession);
					}
				}
			}
			*/
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public void removeActiveSession(String hostname, String sessionId) {
		try {
			ActiveSession existingSession = activeSessionDao.getByHostnameAndSessionId(hostname, sessionId);
			if (existingSession != null)
				activeSessionDao.delete(existingSession);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void removeActiveSession(String username) {
		try {
			List<ActiveSession> existingSessions = activeSessionDao.getByUsername(username);
			for (ActiveSession existingSession:existingSessions) {
				activeSessionDao.delete(existingSession);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public List<ActiveSession> getActiveSessions() {
		List<ActiveSession> result = new LinkedList<ActiveSession>();
		try {
			result = activeSessionDao.getAllActive();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
}
