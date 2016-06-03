package com.gammon.qs.dao.application;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.SystemMessage;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.BaseHibernateDao;
import com.gammon.qs.util.DateUtil;
@Repository
public class SystemMessageHBDao extends BaseHibernateDao<SystemMessage> {

	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(SystemMessageHBDao.class.getName());
	
	public SystemMessageHBDao() {
		super(SystemMessage.class);
	}
	
	public SystemMessageHBDao(Class<SystemMessage> type) {
		super(type);		
	}

	@SuppressWarnings("unchecked")
	public List<String> getSystemMessageStrByUsername(String username) throws DatabaseOperationException {
		
		@SuppressWarnings("unused")
		String returnMsg="";
		List<SystemMessage> systemMessageList = null;
		
		try {
			Criteria criteria = getSession().createCriteria(SystemMessage.class);
			
			List<String> searchUserNameList = new LinkedList<String>();
			
			if(username==null )
				searchUserNameList.add("ALL");
			else
				searchUserNameList.add(username);
			
			criteria.add(Restrictions.in("username", searchUserNameList));
			systemMessageList =  criteria.list();
			
			
		} catch (HibernateException ex) {			
			throw new DatabaseOperationException(ex);
		}
		
		
		List<String> systemMessageStrList = new LinkedList<String>();
		
		for(SystemMessage curSystemMessage:   systemMessageList)
		{
			systemMessageStrList.add( curSystemMessage.getMessage());
		}
		
		
		return systemMessageStrList;
	}

	@SuppressWarnings("unchecked")
	public List<SystemMessage> getSystemMessageByUsername(String username)
			throws DatabaseOperationException {
		
		
		List<SystemMessage> systemMessageList = null;
		
		try {
			Criteria criteria = getSession().createCriteria(SystemMessage.class);
			
			String searchUsername = "ALL";
			
			if(username != null)						
				searchUsername = username;
			
			criteria.add(Restrictions.eq("username", searchUsername));
			
			systemMessageList =  criteria.list();
		} catch (HibernateException ex) {			
			throw new DatabaseOperationException(ex);
		}
				
		return systemMessageList;
	}

	@SuppressWarnings("unchecked")
	public List<String> getCurrentScheduledSystemMessageStrByUsername(
			String username) throws DatabaseOperationException {
		
		List<SystemMessage> systemMessageList = null;
		
		Date currentDateTime = new Date();
		currentDateTime =  DateUtil.parseDate(DateUtil.formatDate(currentDateTime, "dd-MM-yyyy HH:mm"), "dd-MM-yyyy HH:mm");
		
			
		try {
			Criteria criteria = getSession().createCriteria(SystemMessage.class);
			
			List<String> searchUserNameList = new LinkedList<String>();
			
			if(username==null )
				searchUserNameList.add("ALL");
			else
				searchUserNameList.add(username);
			
			criteria.add(Restrictions.in("username", searchUserNameList));
//			criteria.add(Restrictions.eq("scheduledDate",new java.sql.Date(currentDateTime.getTime())));  
		
			systemMessageList =  criteria.list();
			
			
		} catch (HibernateException ex) {	
			
			throw new DatabaseOperationException(ex);
		}
		
		
		List<String> systemMessageStrList = new LinkedList<String>();
		if (systemMessageList!=null && systemMessageList.size()>0)
			for(SystemMessage curSystemMessage:   systemMessageList)
			{
				systemMessageStrList.add( curSystemMessage.getMessage());
			}
		else
			return null;
		

		return systemMessageStrList;
		
		
	}

	
	

}
