package com.gammon.pcms.wrapper;

import com.gammon.pcms.model.AddendumDetail;
import com.gammon.qs.domain.ResourceSummary;



public class ResourceSummaryWrapper extends AddendumDetail {

	private static final long serialVersionUID = 1L;

	private ResourceSummary resourceSummary;
	private AddendumDetail addendumDetail;

	public ResourceSummaryWrapper() {
	}

	public ResourceSummaryWrapper(ResourceSummary resourceSummary, AddendumDetail addendumDetail) {
		this.resourceSummary = resourceSummary;
		this.addendumDetail = addendumDetail;
	}

	public AddendumDetail getAddendumDetail() {
		return addendumDetail;
	}

	public void setAddendumDetail(AddendumDetail addendumDetail) {
		this.addendumDetail = addendumDetail;
	}

	public ResourceSummary getResourceSummary() {
		return resourceSummary;
	}

	public void setResourceSummary(ResourceSummary resourceSummary) {
		this.resourceSummary = resourceSummary;
	}
}
