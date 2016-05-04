package com.gammon.qs.aspect;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;

import com.gammon.qs.application.BasePersistedObject;
import com.gammon.qs.service.security.SecurityService;

public class AuditAspectHibernateInterceptor extends EmptyInterceptor {

	private static final long serialVersionUID = -5896075441323564941L;
	@Autowired
	private SecurityService securityService;
	
	public boolean onSave(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {
		
		String username = securityService==null?"SYSTEM":(securityService.getCurrentUser()==null?"SYSTEM":securityService.getCurrentUser().getUsername());
		boolean modified = false;
		
		if (entity instanceof BasePersistedObject) {
			List<String> propertyList = Arrays.asList(propertyNames);
			int createdDateIndex = propertyList.indexOf("createdDate");
			if (createdDateIndex >= 0) {
				state[createdDateIndex] = new Timestamp(System.currentTimeMillis());
				modified = true;
			}
			int createdUserIndex = propertyList.indexOf("createdUser");
			if (createdUserIndex >= 0) {
				state[createdUserIndex] = username;
				modified = true;
			}
			int lastModifiedUserIndex = propertyList.indexOf("lastModifiedUser");
			if (lastModifiedUserIndex >= 0) {
				state[lastModifiedUserIndex] = username;
				modified = true;
			}
			int lastModifiedDateIndex = propertyList.indexOf("lastModifiedDate");
			if (lastModifiedDateIndex >= 0) {
				state[lastModifiedDateIndex] = new Timestamp(System.currentTimeMillis());
				modified = true;
			}
		}
		return modified;
	}
	
	public boolean onFlushDirty(Object entity, Serializable id,
			Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		
		String username = securityService==null?"SYSTEM":(securityService.getCurrentUser()==null?"SYSTEM":securityService.getCurrentUser().getUsername());
		
		boolean modified = false;
		
		if (entity instanceof BasePersistedObject) {
			List<String> propertyList = Arrays.asList(propertyNames);
			int lastModifiedUserIndex = propertyList.indexOf("lastModifiedUser");
			if (lastModifiedUserIndex >= 0) {
				currentState[lastModifiedUserIndex] = username;
				modified = true;
			}
			int lastModifiedDateIndex = propertyList.indexOf("lastModifiedDate");
			if (lastModifiedDateIndex >= 0) {
				currentState[lastModifiedDateIndex] = new Timestamp(System.currentTimeMillis());
				modified = true;
			}
		}
		
		return modified;
	}	
}
