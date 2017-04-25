package com.gammon.qs.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.qs.dao.IVPostingHistDaoHB;
import com.gammon.qs.domain.IVPostingHist;
import com.gammon.qs.service.security.SecurityService;
@Service
//SpringSession workaround: change "session" to "request"
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "request")
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class IVPostingHistService implements Serializable{
	private transient Logger logger = Logger.getLogger(IVPostingHistService.class.getName());
	
	private static final long serialVersionUID = -7266617280054154627L;
	@Autowired
	private transient IVPostingHistDaoHB ivPostingHistoryDao;
	@Autowired
	private SecurityService securityService;
	
	/*************************************** FUNCTIONS FOR PCMS**************************************************************/
	public List<IVPostingHist> obtainIVPostingHistoryList(String jobNumber, Date fromDate, Date toDate) throws Exception {
		List<IVPostingHist> resultList = new ArrayList<IVPostingHist>();
		String username = securityService.getCurrentUser().getUsername();
		logger.info("User:" + username + " Job: " + jobNumber + " Package: " +  " From Date: " + fromDate + " To Date: " + toDate);
		resultList.addAll(ivPostingHistoryDao.obtainIVPostingHistory(jobNumber, null, null, null, fromDate, toDate));

		return resultList;
		
	}
	/*************************************** FUNCTIONS FOR PCMS - END**************************************************************/
	
}
