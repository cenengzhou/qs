package com.gammon.qs.web;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.client.repository.UIErrorMessageLogRemote;
import com.gammon.qs.service.security.SecurityService;

import net.sf.gilead.core.PersistentBeanManager;

@Service("uiErrorMessageLogController")
public class UIErrorMessageLogController extends GWTSpringController implements UIErrorMessageLogRemote {

	private static final long serialVersionUID = -3944164903936937383L;
	@Autowired
	private SecurityService securityService;
	private Logger logger = Logger.getLogger(UIErrorMessageLogController.class.getName());
	@Override
	@Autowired
	public void setBeanManager(PersistentBeanManager manager) {
		super.setBeanManager(manager);
	}
	
	public Boolean logError(String errorMsg,Throwable e){

		logger.log(Level.SEVERE,"User:"+getCurrentUser()+" Message:"+errorMsg);
		e.printStackTrace();
		return Boolean.TRUE;
	}

	public Boolean logError(Throwable e) {
		logger.log(Level.SEVERE,"User:"+getCurrentUser()+" Message:"+e.getMessage());
		return Boolean.TRUE;
	}
	private String getCurrentUser(){
		try {
			return ((securityService==null)?"NoSecurityUser":((securityService.getCurrentUser()==null)?"-NO-USER-":securityService.getCurrentUser().getUsername()));
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
			return "XXXXX-Exception-XXXX";
		}
	}
}
