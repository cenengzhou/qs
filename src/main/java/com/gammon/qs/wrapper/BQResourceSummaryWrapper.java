package com.gammon.qs.wrapper;

import java.io.Serializable;
import java.util.List;

import com.gammon.qs.domain.BQResourceSummary;

public class BQResourceSummaryWrapper implements Serializable{

	private static final long serialVersionUID = 2268123457364909112L;
	private List<BQResourceSummary> resourceSummaries;
	private String error = null;
	
	public void setResourceSummaries(List<BQResourceSummary> resourceSummaries) {
		this.resourceSummaries = resourceSummaries;
	}
	public List<BQResourceSummary> getResourceSummaries() {
		return resourceSummaries;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}

}
