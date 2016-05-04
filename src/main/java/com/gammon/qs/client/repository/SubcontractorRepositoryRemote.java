/**
 * koeyyeung
 * Jul 25, 20134:49:49 PM
 */
package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.SubcontractorWrapper;
import com.gammon.qs.wrapper.tenderAnalysis.SubcontractorTenderAnalysisWrapper;
import com.google.gwt.user.client.rpc.RemoteService;

public interface SubcontractorRepositoryRemote extends RemoteService{
	public PaginationWrapper<SubcontractorWrapper> obtainSubcontractorPaginationWrapper(String workScope, String subcontractor) throws DatabaseOperationException;
	public PaginationWrapper<SubcontractorWrapper> obtainClientPaginationWrapper(String subcontractor) throws DatabaseOperationException;

	public PaginationWrapper<SubcontractorWrapper> obtainSubcontractorWrapperListByPage(int pageNum);
	public PaginationWrapper<SCPackage> obtainPackagesByVendorNoPaginationWrapper(String vendorNo, String division, String jobNumber, String packageNumber, String paymentTerm, String paymentType) throws DatabaseOperationException;
	public PaginationWrapper<SCPackage> obtainPackagesByPage(int pageNum);
	public PaginationWrapper<SubcontractorTenderAnalysisWrapper> obtainTenderAnalysisPaginationWrapper(String vendorNo, String division, String jobNumber, String packageNumber, String tenderStatus) throws DatabaseOperationException;
	public PaginationWrapper<SubcontractorTenderAnalysisWrapper> obtainTenderAnalysisWrapperByPage(int pageNum);
	
	public List<String> obtainSubconctractorStatistics(String subcontractorNo) throws DatabaseOperationException;
	public List<SubcontractorTenderAnalysisWrapper> obtainTenderAnalysisWrapperByVendorNo(String vendorNo,String division, String jobNumber, String packageNumber, String tenderStatus) throws DatabaseOperationException;
}
