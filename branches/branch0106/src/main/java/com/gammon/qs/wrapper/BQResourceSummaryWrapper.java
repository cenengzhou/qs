package com.gammon.qs.wrapper;

import java.io.Serializable;
import java.util.List;

import com.gammon.qs.domain.ResourceSummary;

public class BQResourceSummaryWrapper implements Serializable{

	private static final long serialVersionUID = 2268123457364909112L;
	private List<ResourceSummary> resourceSummaries;
	private String error = null;
	
	public void setResourceSummaries(List<ResourceSummary> resourceSummaries) {
		this.resourceSummaries = resourceSummaries;
	}
	public List<ResourceSummary> getResourceSummaries() {
		return resourceSummaries;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}

}
