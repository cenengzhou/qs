package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.RepackagingEntry;
import com.google.gwt.user.client.rpc.RemoteService;

public interface RepackagingEntryRepositoryRemote extends RemoteService{
	List<RepackagingEntry> getRepackagingEntriesByJobNumber(String jobNumber) throws Exception;
	List<RepackagingEntry> getRepackagingEntriesByJob(Job job) throws Exception;
	RepackagingEntry createRepackagingEntry(Integer newVersion, Job job, String username, String remarks) throws Exception;
	Boolean saveRepackagingEntry(RepackagingEntry repackagingEntry) throws Exception;
	Boolean removeRepackagingEntry(RepackagingEntry repackagingEntry) throws Exception;
	RepackagingEntry getLatestRepackagingEntry(Job job) throws Exception;
	RepackagingEntry getRepackagingEntry(Long id) throws Exception;
}
