package com.gammon.qs.service;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.qs.application.exception.ValidateBusinessLogicException;
import com.gammon.qs.dao.RepackagingHBDao;
import com.gammon.qs.domain.JobInfo;
import com.gammon.qs.domain.Repackaging;
@Service
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class RepackagingService {
	
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(RepackagingService.class.getName());
	@Autowired
	private RepackagingHBDao repackagingEntryDao;

	public List<Repackaging> getRepackagingEntriesByJob(JobInfo job)
			throws Exception {
		return repackagingEntryDao.getRepackagingEntriesByJob(job);
	}

	public List<Repackaging> getRepackagingEntriesByJobNumber(
			String jobNumber) throws Exception {
		return repackagingEntryDao.getRepackagingEntriesByJobNumber(jobNumber);
	}
	
	public Repackaging getRepackagingEntry(Long id) throws Exception{
		return repackagingEntryDao.get(id);
	}
	
	//Create and save a repackaging entry
	public Repackaging createRepackagingEntry(Integer newVersion, JobInfo job, String username, String remarks) throws Exception{
		Repackaging newEntry = new Repackaging();
		newEntry.setRepackagingVersion(newVersion);
		newEntry.setJobInfo(job);
		newEntry.setCreateDate(new Date());
		newEntry.setStatus("100");
		newEntry.setRemarks(remarks);
		repackagingEntryDao.saveOrUpdate(newEntry);
		return newEntry;
	}
	
	/**
	 * @author tikywong
	 * created on January 05, 2012
	 */
	public Boolean removeRepackagingEntry(Repackaging repackagingEntry) throws Exception {
		Boolean removed = false;
		Repackaging entryInDB;
		
		if(repackagingEntry==null)
			throw new ValidateBusinessLogicException("Repackaging Entry is null.");
		
		entryInDB = repackagingEntryDao.get(repackagingEntry.getId());
		if(entryInDB==null)
			throw new ValidateBusinessLogicException("Repackaging Entry doesn't exist in the database.");
		
		//Validation
		if(entryInDB.getStatus()==null || !entryInDB.getStatus().equals("100"))
			throw new ValidateBusinessLogicException("Repackaging Status is not 100. Entry cannot be deleted.");
		
		repackagingEntryDao.deleteById(entryInDB.getId());
		removed = true;
		
		return removed;
	}
	
	public Boolean saveRepackagingEntry(Repackaging repackagingEntry) throws Exception{
		Repackaging entryInDB = repackagingEntryDao.get(repackagingEntry.getId());
		if (entryInDB.getStatus()!=null && "900".equals(entryInDB.getStatus().trim()))
			throw new ValidateBusinessLogicException("Repackaging status is 900. Job "+entryInDB.getJobInfo().getJobNumber()+" is locked already.");
		entryInDB.setStatus(repackagingEntry.getStatus());	
		entryInDB.setRemarks(repackagingEntry.getRemarks());
		repackagingEntryDao.saveOrUpdate(entryInDB);
		return Boolean.TRUE;
	}
	
	public Repackaging getLatestRepackagingEntry(JobInfo job) throws Exception{
		return repackagingEntryDao.getLatestRepackagingEntry(job);
	}

}
