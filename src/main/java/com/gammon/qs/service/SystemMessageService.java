package com.gammon.qs.service;


import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.qs.application.SystemMessage;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.application.SystemMessageHBDao;
import com.gammon.qs.service.security.SecurityServiceSpringImpl;
@Service
@Transactional(rollbackFor = Exception.class)
public class SystemMessageService {

	@Autowired
	private SecurityServiceSpringImpl securityService;
	@Autowired
	private SystemMessageHBDao systemMessageDao;
	private transient Logger logger = Logger.getLogger(SystemMessageService.class.getName());
	
	public String getGlobalSystemMessageStr() throws DatabaseOperationException {
			
		List<String> alertMessageList = systemMessageDao.getCurrentScheduledSystemMessageStrByUsername(null);
			
		String alertMessage = " ";
		
		if (alertMessageList!=null && alertMessageList.size()>0)
			for(String curMsg : alertMessageList)
			{
	//			alertMessage += (curMsg +"</br>");
				if (curMsg!=null && curMsg.trim().length()>0)
					alertMessage += (curMsg + "    ");
			}
				
		return alertMessage;
	}


	public void saveGlobalSystemMessage(SystemMessage systemMessage,
			String username) throws DatabaseOperationException {
		
		systemMessage.populate(username);
		systemMessageDao.saveOrUpdate(systemMessage);
		
	}

	public List<SystemMessage> getAllSystemMessage()throws DatabaseOperationException 
	{
		return systemMessageDao.getAll();
	}
	
	
	public void removeSystemMessageById(Long id) throws DatabaseOperationException
	{
		systemMessageDao.deleteById(id);
	}
	
	
	public SystemMessage getSystemMessageById(Long id) throws DatabaseOperationException
	{
		return systemMessageDao.get(id);
	}

	public Boolean logError(Throwable e) {
		String username = ((securityService==null)?"NoSecurityUser":((securityService.getCurrentUser()==null)?"-NO-USER-":securityService.getCurrentUser().getUsername()));
		logger.info("User:"+username+" throws session timeout exception\n***********\n"+e.getMessage()+"\n***********\n");
		e.printStackTrace();
		return Boolean.TRUE;
	}

}
