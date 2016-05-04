package com.gammon.qs.web;

//import java.util.logging.Level;
//import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.client.repository.SystemMessageRepositoryRemote;
import com.gammon.qs.service.SystemMessageService;

import net.sf.gilead.core.PersistentBeanManager;
@Service
public class SystemMessageController extends GWTSpringController implements SystemMessageRepositoryRemote {

	private static final long serialVersionUID = 1540100517396826535L;
	@Autowired
	private SystemMessageService systemMessageRepository;
	@Override
	@Autowired
	public void setBeanManager(PersistentBeanManager manager) {
		super.setBeanManager(manager);
	}
//	private Logger logger = Logger.getLogger(SystemMessageController.class.getName());

	public String getGlobalAlertMessage() throws Exception {
		
		String returnStr = systemMessageRepository.getGlobalSystemMessageStr();			
		return returnStr;
	
	}

	public Boolean logError(Throwable e) {
		return systemMessageRepository.logError(e);
	}

}
