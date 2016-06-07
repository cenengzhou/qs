package com.gammon.pcms.config.initializer;

import java.lang.reflect.Field;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionTrackingMode;

import org.apache.catalina.Manager;
import org.apache.catalina.Session;
import org.apache.catalina.core.ApplicationContext;
import org.apache.catalina.core.ApplicationContextFacade;
import org.apache.catalina.core.StandardContext;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

public class HttpSessionApplicationInitializer extends AbstractHttpSessionApplicationInitializer {
	
	@SuppressWarnings("unchecked")
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		ApplicationContextFacade appContextFacadeObj = (ApplicationContextFacade) servletContext;
        Field applicationContextField;
		try {
			applicationContextField = appContextFacadeObj.getClass().getDeclaredField("context");
	        applicationContextField.setAccessible(true);
	        ApplicationContext appContextObj = (ApplicationContext) applicationContextField.get(appContextFacadeObj);
	        Field standardContextField = appContextObj.getClass().getDeclaredField("sessionTrackingModes");
	        standardContextField.setAccessible(true);
	        Set<SessionTrackingMode> sessionTrackingMode = (Set<SessionTrackingMode>) standardContextField.get(appContextObj);
	        if(sessionTrackingMode == null){
	        	super.onStartup(servletContext);
	        } else {
		        standardContextField = appContextObj.getClass().getDeclaredField("context");
		        standardContextField.setAccessible(true);
		        StandardContext standardContextObj = (StandardContext) standardContextField.get(appContextObj);
		        Manager persistenceManager = standardContextObj.getManager();
		        for(Session session : persistenceManager.findSessions()){
		        	session.expire();
		        }
	        }
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
