package com.gammon.qs.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.client.repository.RepackagingEntryRepositoryRemote;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.RepackagingEntry;
import com.gammon.qs.service.RepackagingEntryService;

import net.sf.gilead.core.PersistentBeanManager;
@Service
public class RepackagingEntryRepositoryController extends GWTSpringController
		implements RepackagingEntryRepositoryRemote {
	
	private static final long serialVersionUID = -3029934305586104996L;

	@Override
	@Autowired
	public void setBeanManager(PersistentBeanManager manager) {
		super.setBeanManager(manager);
	}
	
	@Autowired
	private RepackagingEntryService repackagingEntryRepository;

	public List<RepackagingEntry> getRepackagingEntriesByJob(Job job)
			throws Exception {
		return repackagingEntryRepository.getRepackagingEntriesByJob(job);
	}

	public List<RepackagingEntry> getRepackagingEntriesByJobNumber(
			String jobNumber) throws Exception {
		return repackagingEntryRepository.getRepackagingEntriesByJobNumber(jobNumber);
	}
	
	public Boolean saveRepackagingEntry(RepackagingEntry repackagingEntry)
			throws Exception {
		return repackagingEntryRepository.saveRepackagingEntry(repackagingEntry);
	}
	
	public RepackagingEntry createRepackagingEntry(Integer newVersion, Job job, String username, String remarks) throws Exception{
		return repackagingEntryRepository.createRepackagingEntry(newVersion, job, username, remarks);
	}
	
	public Boolean removeRepackagingEntry(RepackagingEntry repackagingEntry) throws Exception {
		return repackagingEntryRepository.removeRepackagingEntry(repackagingEntry);
	}
	
	public RepackagingEntry getLatestRepackagingEntry(Job job) throws Exception{
		return repackagingEntryRepository.getLatestRepackagingEntry(job);
	}
	
	public RepackagingEntry getRepackagingEntry(Long id) throws Exception {
		return repackagingEntryRepository.getRepackagingEntry(id);
	}

}
