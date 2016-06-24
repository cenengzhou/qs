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
		List<String> propertyList = Arrays.asList(propertyNames);
		if (entity instanceof BasePersistedObject) {
			
			modified = onSaveQS(state, username, propertyList);
		} else if (propertyList.indexOf("dateLastModified") >=0){
			modified = onSavePCMS(state, username, propertyList);
		}
		return modified;
	}

	/**For QS Audit
	 * @param state
	 * @param username
	 * @param propertyList
	 * @return
	 */
	private boolean onSaveQS(Object[] state, String username, List<String> propertyList) {
		boolean modified = false;
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
		return modified;
	}
	
	/**For PCMS Audit
	 * @param state
	 * @param username
	 * @param propertyList
	 * @return
	 */
	private boolean onSavePCMS(Object[] state, String username, List<String> propertyList) {
		boolean modified = false;
		int dateCreatedIndex = propertyList.indexOf("dateCreated");
		if (dateCreatedIndex >= 0) {
			state[dateCreatedIndex] = new Timestamp(System.currentTimeMillis());
			modified = true;
		}
		int usernameCreatedIndex = propertyList.indexOf("usernameCreated");
		if (usernameCreatedIndex >= 0) {
			state[usernameCreatedIndex] = username;
			modified = true;
		}
		int usernameLastModifiedIndex = propertyList.indexOf("usernameLastModified");
		if (usernameLastModifiedIndex >= 0) {
			state[usernameLastModifiedIndex] = username;
			modified = true;
		}
		int dateLastModifiedIndex = propertyList.indexOf("dateLastModified");
		if (dateLastModifiedIndex >= 0) {
			state[dateLastModifiedIndex] = new Timestamp(System.currentTimeMillis());
			modified = true;
		}
		return modified;
	}

	public boolean onFlushDirty(Object entity, Serializable id,
			Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		
		String username = securityService==null?"SYSTEM":(securityService.getCurrentUser()==null?"SYSTEM":securityService.getCurrentUser().getUsername());
		
		boolean modified = false;
		List<String> propertyList = Arrays.asList(propertyNames);
		if (entity instanceof BasePersistedObject) {
			modified = onFlushDirtyQS(currentState, username, propertyList);
		} else if (propertyList.indexOf("dateLastModified") >=0){
			modified = onFlushDirtyPCMS(currentState, username, propertyList);
		}
		
		return modified;
	}

	/**Flush dirty for QS
	 * @param currentState
	 * @param username
	 * @param propertyList
	 * @return
	 */
	private boolean onFlushDirtyQS(Object[] currentState, String username, List<String> propertyList) {
		boolean modified = false;
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
		return modified;
	}	
	
	/**Flush dirty for PCMS
	 * @param currentState
	 * @param username
	 * @param propertyList
	 * @return
	 */
	private boolean onFlushDirtyPCMS(Object[] currentState, String username, List<String> propertyList) {
		boolean modified = false;
		int usernameLastModifiedIndex = propertyList.indexOf("usernameLastModified");
		if (usernameLastModifiedIndex >= 0) {
			currentState[usernameLastModifiedIndex] = username;
			modified = true;
		}
		int dateLastModifiedIndex = propertyList.indexOf("dateLastModified");
		if (dateLastModifiedIndex >= 0) {
			currentState[dateLastModifiedIndex] = new Timestamp(System.currentTimeMillis());
			modified = true;
		}
		return modified;
	}	

}
