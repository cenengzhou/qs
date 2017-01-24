/**
 * 
 */
package com.gammon.qs.wrapper;

import java.util.List;

import com.gammon.qs.domain.ResourceSummary;

/**
 * @author koeyyeung
 *
 */
public class ResourceSummarySplitMergeWrapper{

	
	private List<ResourceSummary> oldResourceSummaryList;
	private List<ResourceSummary> newResourceSummaryList;
	
	public List<ResourceSummary> getOldResourceSummaryList() {
		return oldResourceSummaryList;
	}
	public void setOldResourceSummaryList(List<ResourceSummary> oldResourceSummaryList) {
		this.oldResourceSummaryList = oldResourceSummaryList;
	}
	public List<ResourceSummary> getNewResourceSummaryList() {
		return newResourceSummaryList;
	}
	public void setNewResourceSummaryList(List<ResourceSummary> newResourceSummaryList) {
		this.newResourceSummaryList = newResourceSummaryList;
	}

	
	
}
