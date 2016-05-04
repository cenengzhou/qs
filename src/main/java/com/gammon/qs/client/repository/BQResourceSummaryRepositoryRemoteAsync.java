package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.domain.BQResourceSummary;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.RepackagingEntry;
import com.gammon.qs.wrapper.BQResourceSummaryWrapper;
import com.gammon.qs.wrapper.IVInputPaginationWrapper;
import com.gammon.qs.wrapper.RepackagingPaginationWrapper;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BQResourceSummaryRepositoryRemoteAsync {
	void getResourceSummariesByJobNumber(String jobNumber, AsyncCallback<List<BQResourceSummary>> callback);
	void getResourceSummariesByJob(Job job, AsyncCallback<List<BQResourceSummary>> callback);
	void createResourceSummary(Job job, String username, AsyncCallback<BQResourceSummary> callback);
	void saveResourceSummary(BQResourceSummary resourceSummary, Long repackagingEntryId, AsyncCallback<BQResourceSummaryWrapper> callback) throws Exception;
	void saveResourceSummaries(List<BQResourceSummary> resourceSummaries, Long repackagingEntryId, AsyncCallback<BQResourceSummaryWrapper> callback) throws Exception;
	void updateResourceSummariesIVAmount(List<BQResourceSummary> resourceSummaries, AsyncCallback<Boolean> callback);
	void updateResourceSummariesIVAmountForFinalizedPackage(List<BQResourceSummary> resourceSummaries, AsyncCallback<Boolean> callback);
	void getResourceSummariesSearch(Job job, String packageNo, String objectCode, String subsidiaryCode, 
			AsyncCallback<List<BQResourceSummary>> callback);
	void obtainResourceSummariesSearchByPage(Job job, String packageNo, String objectCode, String subsidiaryCode, String description, String type, String levyExcluded, String defectExcluded, 
			int pageNum, AsyncCallback<RepackagingPaginationWrapper<BQResourceSummary>> callback);
	void obtainResourceSummariesSearchByPageForIVInput(Job job, String packageNo, String objectCode, String subsidiaryCode, 
			String description, int pageNum, boolean finalizedPackage, AsyncCallback<IVInputPaginationWrapper> callback);
	void splitOrMergeResources(List<BQResourceSummary> oldResources, List<BQResourceSummary> newResources, Long repackagingEntryId, AsyncCallback<BQResourceSummaryWrapper> callback);
	void groupResourcesIntoSummaries(Job job, AsyncCallback<Boolean> callback);
	void postIVAmounts(Job job, String username, boolean finalized, AsyncCallback<Boolean> callback);
	void generateSnapshotMethodTwo(Job job, RepackagingEntry repackagingEntry, AsyncCallback<Boolean> callback);
	void deleteVoResources(List<BQResourceSummary> resourceSummaries, AsyncCallback<String> callback);
	void saveResourceSummariesScAddendum(List<BQResourceSummary> resourceSummaries, Long repackagingEntryId, AsyncCallback<Boolean> callback);
	void checkForDuplicates(List<BQResourceSummary> resourceSummaries, Job job, AsyncCallback<String> callback);
	/**
	 * @author tikywong
	 * Apr 13, 2011 12:12:24 PM
	 */
	void generateSnapshotMethodThree(Job job, RepackagingEntry repackagingEntry, AsyncCallback<Boolean> asyncCallback);
	/**
	 * 
	 * @author peterchan
	 * May 11, 2011 4:10:35 PM
	 */
	void getIVMovementOfJobFromResourceSummary(Job job,AsyncCallback<Double> asyncCallback);
	
	void obtainUneditableResourceSummaries(Job job, AsyncCallback<List<String>> asyncCallback);
	void obtainIVMovementByJob(Job job, boolean finalized, AsyncCallback<List<Double>> callback);
	void recalculateResourceSummaryIVForFinalizedPackage(Job job, String packageNo, String objectCode, String subsidiaryCode, String description, AsyncCallback<Boolean> callback);
}
