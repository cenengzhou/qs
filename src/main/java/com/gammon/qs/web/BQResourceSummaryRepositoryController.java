package com.gammon.qs.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.client.repository.BQResourceSummaryRepositoryRemote;
import com.gammon.qs.domain.BQResourceSummary;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.RepackagingEntry;
import com.gammon.qs.service.BQResourceSummaryService;
import com.gammon.qs.wrapper.BQResourceSummaryWrapper;
import com.gammon.qs.wrapper.IVInputPaginationWrapper;
import com.gammon.qs.wrapper.RepackagingPaginationWrapper;

import net.sf.gilead.core.PersistentBeanManager;
@Service("bqResourceSummaryRepositoryController")
public class BQResourceSummaryRepositoryController extends GWTSpringController
		implements BQResourceSummaryRepositoryRemote {

	private static final long serialVersionUID = -5687804704649329214L;
	@Autowired
	private BQResourceSummaryService bqResourceSummaryRepository;
	@Override
	@Autowired
	public void setBeanManager(PersistentBeanManager manager) {
		super.setBeanManager(manager);
	}

	public BQResourceSummary createResourceSummary(Job job, String username)
			throws Exception {
		
		return null;
	}

	public List<BQResourceSummary> getResourceSummariesByJob(Job job)
			throws Exception {
		return bqResourceSummaryRepository.getResourceSummariesByJob(job);
	}

	public List<BQResourceSummary> getResourceSummariesByJobNumber(
			String jobNumber) throws Exception {
		return bqResourceSummaryRepository.getResourceSummariesByJobNumber(jobNumber);
	}

	public List<BQResourceSummary> getResourceSummariesSearch(Job job,
			String packageNo, String objectCode, String subsidiaryCode)
			throws Exception {
		return bqResourceSummaryRepository.getResourceSummariesSearch(job, packageNo, objectCode, subsidiaryCode);
	}
	
	public RepackagingPaginationWrapper<BQResourceSummary> obtainResourceSummariesSearchByPage(Job job, 
			String packageNo, String objectCode, String subsidiaryCode, String description, String type, String levyExcluded, String defectExcluded, int pageNum) throws Exception{
		return bqResourceSummaryRepository.obtainResourceSummariesSearchByPage(job, packageNo, objectCode, subsidiaryCode, description,type,levyExcluded,defectExcluded, pageNum);	
	}
	
	public IVInputPaginationWrapper obtainResourceSummariesSearchByPageForIVInput(Job job, 
			String packageNo, String objectCode, String subsidiaryCode, String description, int pageNum, boolean finalizedPackage) throws Exception{
		return bqResourceSummaryRepository.obtainResourceSummariesSearchByPageForIVInput(job, packageNo, objectCode, subsidiaryCode, description, pageNum, finalizedPackage);
	}
	
	public BQResourceSummaryWrapper saveResourceSummary(BQResourceSummary resourceSummary, Long repackagingEntryId) throws Exception{
		return bqResourceSummaryRepository.saveResourceSummary(resourceSummary, repackagingEntryId);
	}
	
	public BQResourceSummaryWrapper saveResourceSummaries(List<BQResourceSummary> resourceSummaries, Long repackagingEntryId) throws Exception{
		return bqResourceSummaryRepository.saveResourceSummaries(resourceSummaries, repackagingEntryId);
	}
	
	public Boolean updateResourceSummariesIVAmount(List<BQResourceSummary> resourceSummaries) throws Exception{
		return bqResourceSummaryRepository.updateResourceSummariesIVAmount(resourceSummaries);
	}

	public BQResourceSummaryWrapper splitOrMergeResources(List<BQResourceSummary> oldResources, List<BQResourceSummary> newResources, Long repackagingEntryId) throws Exception{
		return bqResourceSummaryRepository.splitOrMergeResources(oldResources, newResources, repackagingEntryId);
	}
	
	public Boolean groupResourcesIntoSummaries(Job job) throws Exception{
		return bqResourceSummaryRepository.groupResourcesIntoSummaries(job);
	}

	public Boolean postIVAmounts(Job job, String username, boolean finalized) throws Exception {
		return bqResourceSummaryRepository.postIVAmounts(job, username, finalized);
	}
	
	public Boolean generateSnapshotMethodTwo(Job job, RepackagingEntry repackagingEntry) throws Exception{
		return bqResourceSummaryRepository.generateSnapshotMethodTwo(job, repackagingEntry);
	}

	public String deleteVoResources(List<BQResourceSummary> resourceSummaries)
			throws Exception {
		return bqResourceSummaryRepository.deleteVoResources(resourceSummaries);
	}

	public Boolean saveResourceSummariesScAddendum(
			List<BQResourceSummary> resourceSummaries, Long repackagingEntryId)
			throws Exception {
		return bqResourceSummaryRepository.saveResourceSummariesScAddendum(resourceSummaries, repackagingEntryId);
	}

	public String checkForDuplicates(List<BQResourceSummary> resourceSummaries,
			Job job) throws Exception {
		return bqResourceSummaryRepository.checkForDuplicates(resourceSummaries, job);
	}

	/* (non-Javadoc)
	 * @see com.gammon.qs.client.repository.BQResourceSummaryRepositoryRemote#generateSnapshotMethodThree(com.gammon.qs.domain.Job, com.gammon.qs.domain.RepackagingEntry)
	 */
	/**
	 * @author tikywong
	 * April 13, 2011
	 */
	public Boolean generateSnapshotMethodThree(Job job, RepackagingEntry repackagingEntry) throws Exception {
		return bqResourceSummaryRepository.generateSnapshotMethodThree(job, repackagingEntry);
	}

	/**
	 * @author peterchan
	 * May 11, 2011 4:08:30 PM
	 * @see com.gammon.qs.client.repository.BQResourceSummaryRepositoryRemote#getIVMovementOfJobFromResourceSummary(com.gammon.qs.domain.Job)
	 */
	public Double getIVMovementOfJobFromResourceSummary(Job job) {
		return bqResourceSummaryRepository.getIVMovementOfJobFromResourceSummary(job);
	}

	public List<String> obtainUneditableResourceSummaries(Job job) throws Exception {
		return bqResourceSummaryRepository.obtainUneditableResourceSummaries(job);
	}

	public Boolean updateResourceSummariesIVAmountForFinalizedPackage(List<BQResourceSummary> resourceSummaries) throws Exception {
		return bqResourceSummaryRepository.updateResourceSummariesIVAmountForFinalizedPackage(resourceSummaries);
	}

	public List<Double> obtainIVMovementByJob(Job job, boolean finalized) throws DatabaseOperationException {
		return bqResourceSummaryRepository.obtainIVMovementByJob(job, finalized);
	}

	public boolean recalculateResourceSummaryIVForFinalizedPackage(Job job, String packageNo, String objectCode, String subsidiaryCode, String description) throws DatabaseOperationException {
		return bqResourceSummaryRepository.recalculateResourceSummaryIVForFinalizedPackage(job, packageNo, objectCode, subsidiaryCode, description);
	}

}
