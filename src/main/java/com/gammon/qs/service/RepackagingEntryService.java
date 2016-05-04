package com.gammon.qs.service;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.qs.application.exception.ValidateBusinessLogicException;
import com.gammon.qs.dao.RepackagingEntryHBDao;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.RepackagingEntry;
@Service
@Transactional(rollbackFor = Exception.class)
public class RepackagingEntryService {
	
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(RepackagingEntryService.class.getName());
	@Autowired
	private RepackagingEntryHBDao repackagingEntryDao;

	public List<RepackagingEntry> getRepackagingEntriesByJob(Job job)
			throws Exception {
		return repackagingEntryDao.getRepackagingEntriesByJob(job);
	}

	public List<RepackagingEntry> getRepackagingEntriesByJobNumber(
			String jobNumber) throws Exception {
		return repackagingEntryDao.getRepackagingEntriesByJobNumber(jobNumber);
	}
	
	public RepackagingEntry getRepackagingEntry(Long id) throws Exception{
		return repackagingEntryDao.get(id);
	}
	
	//Create and save a repackaging entry
	public RepackagingEntry createRepackagingEntry(Integer newVersion, Job job, String username, String remarks) throws Exception{
		RepackagingEntry newEntry = new RepackagingEntry();
		newEntry.setRepackagingVersion(newVersion);
		newEntry.setJob(job);
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
	public Boolean removeRepackagingEntry(RepackagingEntry repackagingEntry) throws Exception {
		Boolean removed = false;
		RepackagingEntry entryInDB;
		
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
	
	public Boolean saveRepackagingEntry(RepackagingEntry repackagingEntry) throws Exception{
		RepackagingEntry entryInDB = repackagingEntryDao.get(repackagingEntry.getId());
		if (entryInDB.getStatus()!=null && "900".equals(entryInDB.getStatus().trim()))
			throw new ValidateBusinessLogicException("Repackaging status is 900. Job "+entryInDB.getJob().getJobNumber()+" is locked already.");
		entryInDB.setStatus(repackagingEntry.getStatus());	
		entryInDB.setRemarks(repackagingEntry.getRemarks());
		repackagingEntryDao.saveOrUpdate(entryInDB);
		return Boolean.TRUE;
	}
	
	public RepackagingEntry getLatestRepackagingEntry(Job job) throws Exception{
		return repackagingEntryDao.getLatestRepackagingEntry(job);
	}

}
