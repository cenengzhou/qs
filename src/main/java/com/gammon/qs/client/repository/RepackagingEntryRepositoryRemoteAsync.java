package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.RepackagingEntry;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RepackagingEntryRepositoryRemoteAsync {
	void getRepackagingEntriesByJobNumber(String jobNumber, AsyncCallback<List<RepackagingEntry>> callback);
	void getRepackagingEntriesByJob(Job job, AsyncCallback<List<RepackagingEntry>> callback);
	void createRepackagingEntry(Integer newVersion, Job job, String username, String remarks, AsyncCallback<RepackagingEntry> callback);
	void saveRepackagingEntry(RepackagingEntry repackagingEntry, AsyncCallback<Boolean> callback);
	void removeRepackagingEntry(RepackagingEntry repackagingEntry, AsyncCallback<Boolean> asyncCallback);
	void getLatestRepackagingEntry(Job job, AsyncCallback<RepackagingEntry> callback);
	void getRepackagingEntry(Long id, AsyncCallback<RepackagingEntry> callback);
}
