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

import com.gammon.qs.application.exception.ValidateBusinessLogicException;
import com.gammon.qs.dao.IVPostingHistoryDaoHB;
import com.gammon.qs.domain.IVPostingHistory;
import com.gammon.qs.io.ExcelFile;
import com.gammon.qs.service.jobCost.IvPostingHistoryExcelGenerator;
import com.gammon.qs.wrapper.IVHistoryPaginationWrapper;
@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
@Transactional(rollbackFor = Exception.class)
public class IVPostingHistoryService implements Serializable{
	private transient Logger logger = Logger.getLogger(IVPostingHistoryService.class.getName());
	
	private static final long serialVersionUID = -7266617280054154627L;
	private static final int RECORDS_PER_PAGE = 200;
	@Autowired
	private transient IVPostingHistoryDaoHB ivPostingHistoryDao;
	private List<IVPostingHistory> cachedIVHistoryList = new ArrayList<IVPostingHistory>();
	private Double totalMovement;
	

	public IVHistoryPaginationWrapper obtainIVPostingHistory(String jobNumber, String packageNo, String objectCode, String subsidiaryCode, Date fromDate, Date toDate) throws Exception {
		logger.info("Job: " + jobNumber + " Package: " + packageNo + " Object: " + objectCode + " Subsidiary: " + subsidiaryCode + " From Date: " + fromDate + " To Date: " + toDate);
		
		cachedIVHistoryList = ivPostingHistoryDao.obtainIVPostingHistory(jobNumber, packageNo, objectCode, subsidiaryCode, fromDate, toDate);
		
		totalMovement = new Double(0.0);
		for(IVPostingHistory ivHistory : cachedIVHistoryList)
			totalMovement += ivHistory.getIvMovementAmount();
		
		return obtainIVPostingHistoryByPage(0);
	}
	
	public ExcelFile downloadIvPostingHistoryExcelFile(String jobNumber, String packageNo, String objectCode, String subsidiaryCode, Date fromDate, Date toDate) throws Exception {
		List<IVPostingHistory> ivPostingHistoryList = ivPostingHistoryDao.obtainIVPostingHistory(jobNumber, packageNo, objectCode, subsidiaryCode, fromDate, toDate);
		
		logger.info("DOWNLOAD EXCEL - IV POSTING HISTORY RECORDS SIZE:"+ ivPostingHistoryList.size());

		if(ivPostingHistoryList==null || ivPostingHistoryList.size()==0)
			return null;

		//Create Excel File
		ExcelFile excelFile = new ExcelFile();

		IvPostingHistoryExcelGenerator excelGenerator = new IvPostingHistoryExcelGenerator(ivPostingHistoryList, jobNumber);			
		excelFile = excelGenerator.generate();

		return excelFile;
	}
	
	public IVHistoryPaginationWrapper obtainIVPostingHistoryByPage(int pageNum) throws ValidateBusinessLogicException{
		IVHistoryPaginationWrapper wrapper = new IVHistoryPaginationWrapper();
		
		if (cachedIVHistoryList.size()<1)
			throw new ValidateBusinessLogicException("Caches data lost! Please click the Search button to reload the result.");
		
		wrapper.setTotalMovement(totalMovement);
		wrapper.setCurrentPage(pageNum);
		int size = cachedIVHistoryList.size();
		wrapper.setTotalRecords(size);
		wrapper.setTotalPage((size + RECORDS_PER_PAGE - 1)/RECORDS_PER_PAGE);
		int fromInd = pageNum * RECORDS_PER_PAGE;
		int toInd = (pageNum + 1) * RECORDS_PER_PAGE;
		if(toInd > cachedIVHistoryList.size())
			toInd = cachedIVHistoryList.size();
		wrapper.setCurrentPageContentList(new ArrayList<IVPostingHistory>(cachedIVHistoryList.subList(fromInd, toInd)));
		return wrapper;
	}
	
	public Boolean clearCache(){
		cachedIVHistoryList = new ArrayList<IVPostingHistory>();
		totalMovement = new Double(0.0);
		return Boolean.FALSE;
	}
	
}
