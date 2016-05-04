package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.BQResourceSummary;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.RepackagingEntry;
import com.gammon.qs.wrapper.BQResourceSummaryWrapper;
import com.gammon.qs.wrapper.IVInputPaginationWrapper;
import com.gammon.qs.wrapper.RepackagingPaginationWrapper;
import com.google.gwt.user.client.rpc.RemoteService;

public interface BQResourceSummaryRepositoryRemote extends RemoteService {
	List<BQResourceSummary> getResourceSummariesByJobNumber(String jobNumber) throws Exception;
	List<BQResourceSummary> getResourceSummariesByJob(Job job) throws Exception;
	BQResourceSummary createResourceSummary(Job job, String username) throws Exception;
	BQResourceSummaryWrapper saveResourceSummary(BQResourceSummary resourceSummary, Long repackagingEntryId) throws Exception;
	BQResourceSummaryWrapper saveResourceSummaries(List<BQResourceSummary> resourceSummaries, Long repackagingEntryId) throws Exception;
	Boolean updateResourceSummariesIVAmount(List<BQResourceSummary> resourceSummaries) throws Exception;
	Boolean updateResourceSummariesIVAmountForFinalizedPackage(List<BQResourceSummary> resourceSummaries) throws Exception;
	List<BQResourceSummary> getResourceSummariesSearch(Job job, 
			String packageNo, String objectCode, String subsidiaryCode) throws Exception;
	BQResourceSummaryWrapper splitOrMergeResources(List<BQResourceSummary> oldResources, List<BQResourceSummary> newResources, Long repackagingEntryId) throws Exception;
	Boolean groupResourcesIntoSummaries(Job job) throws Exception;
	Boolean postIVAmounts(Job job, String username, boolean finalized) throws Exception;
	RepackagingPaginationWrapper<BQResourceSummary> obtainResourceSummariesSearchByPage(Job job, 
				String packageNo, String objectCode, String subsidiaryCode, String description, String type, String levyExcluded, String defectExcluded, int pageNum) throws Exception;
	IVInputPaginationWrapper obtainResourceSummariesSearchByPageForIVInput(Job job, 
			String packageNo, String objectCode, String subsidiaryCode, String description, int pageNum, boolean finalizedPackage) throws Exception;
	Boolean generateSnapshotMethodTwo(Job job, RepackagingEntry repackagingEntry) throws Exception;
	String deleteVoResources(List<BQResourceSummary> resourceSummaries) throws Exception;
	Boolean saveResourceSummariesScAddendum(List<BQResourceSummary> resourceSummaries, Long repackagingEntryId) throws Exception;
	String checkForDuplicates(List<BQResourceSummary> resourceSummaries, Job job) throws Exception;
	
	/**
	 * @author tikywong
	 * Apr 13, 2011 12:13:25 PM
	 */
	Boolean generateSnapshotMethodThree(Job job, RepackagingEntry repackagingEntry) throws Exception;
	
	/**
	 * 
	 * @author peterchan
	 * May 11, 2011 4:08:14 PM
	 */
	Double getIVMovementOfJobFromResourceSummary(Job job);
	
	public List<String> obtainUneditableResourceSummaries(Job job) throws Exception;
	public List<Double> obtainIVMovementByJob(Job job, boolean finalized) throws DatabaseOperationException;
	public boolean recalculateResourceSummaryIVForFinalizedPackage(Job job, String packageNo, String objectCode, String subsidiaryCode, String description) throws DatabaseOperationException;
}
