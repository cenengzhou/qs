/**
 * koeyyeung
 * Jul 25, 2013 4:50:54 PM
 */
package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.SubcontractorWrapper;
import com.gammon.qs.wrapper.tenderAnalysis.SubcontractorTenderAnalysisWrapper;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SubcontractorRepositoryRemoteAsync {
	void obtainSubcontractorPaginationWrapper(String workScope, String subcontractor, AsyncCallback<PaginationWrapper<SubcontractorWrapper>> callback);
	void obtainClientPaginationWrapper(String subcontractor, AsyncCallback<PaginationWrapper<SubcontractorWrapper>> callback);

	void obtainSubcontractorWrapperListByPage(int pageNum, AsyncCallback<PaginationWrapper<SubcontractorWrapper>> callback);
	void obtainPackagesByVendorNoPaginationWrapper(String vendorNo, String division, String jobNumber, String packageNumber, String paymentTerm, String paymentType, AsyncCallback<PaginationWrapper<SCPackage>> callback);
	void obtainPackagesByPage(int pageNum, AsyncCallback<PaginationWrapper<SCPackage>> callback);
	void obtainTenderAnalysisPaginationWrapper(String vendorNo, String division, String jobNumber, String packageNumber, String tenderStatus, AsyncCallback<PaginationWrapper<SubcontractorTenderAnalysisWrapper>> callback);
	void obtainTenderAnalysisWrapperByPage(int pageNum, AsyncCallback<PaginationWrapper<SubcontractorTenderAnalysisWrapper>> callback);
	
	void obtainSubconctractorStatistics(String subcontractorNo, AsyncCallback<List<String>> callback);
	void obtainTenderAnalysisWrapperByVendorNo(String vendorNo,String division, String jobNumber, String packageNumber, String tenderStatus, AsyncCallback<List<SubcontractorTenderAnalysisWrapper>> callback);
}
