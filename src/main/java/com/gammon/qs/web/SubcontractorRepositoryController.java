/**
 * koeyyeung
 * Jul 25, 20134:49:04 PM
 */
package com.gammon.qs.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.client.repository.SubcontractorRepositoryRemote;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.service.SubcontractorService;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.SubcontractorWrapper;
import com.gammon.qs.wrapper.tenderAnalysis.SubcontractorTenderAnalysisWrapper;

import net.sf.gilead.core.PersistentBeanManager;

@Service
public class SubcontractorRepositoryController extends GWTSpringController implements SubcontractorRepositoryRemote{
	private static final long serialVersionUID = 1962564614046429288L;
	@Autowired
	private SubcontractorService subcontractorRepository;
	@Override
	@Autowired
	public void setBeanManager(PersistentBeanManager manager) {
		super.setBeanManager(manager);
	}

	public PaginationWrapper<SubcontractorWrapper> obtainSubcontractorPaginationWrapper(String workScope, String subcontractor) throws DatabaseOperationException {
		return subcontractorRepository.obtainSubcontractorPaginationWrapper(workScope, subcontractor);
	}
	
	public PaginationWrapper<SubcontractorWrapper> obtainClientPaginationWrapper(String subcontractor) throws DatabaseOperationException {
		return subcontractorRepository.obtainClientPaginationWrapper(subcontractor);
	}

	public PaginationWrapper<SubcontractorWrapper> obtainSubcontractorWrapperListByPage(int pageNum) {
		return subcontractorRepository.obtainSubcontractorWrapperListByPage(pageNum);
	}

	public List<String> obtainSubconctractorStatistics(String subcontractorNo) throws DatabaseOperationException {
		return subcontractorRepository.obtainSubconctractorStatistics(subcontractorNo);
	}

	public List<SubcontractorTenderAnalysisWrapper> obtainTenderAnalysisWrapperByVendorNo(String vendorNo, String division, String jobNumber, String packageNumber, String tenderStatus) throws DatabaseOperationException {
		return subcontractorRepository.obtainTenderAnalysisWrapperByVendorNo(vendorNo, division, jobNumber, packageNumber, tenderStatus);
	}

	public PaginationWrapper<SCPackage> obtainPackagesByVendorNoPaginationWrapper(String vendorNo, String division, String jobNumber, String packageNumber, String paymentTerm, String paymentType) throws DatabaseOperationException {
		return subcontractorRepository.obtainPackagesByVendorNoPaginationWrapper(vendorNo, division, jobNumber, packageNumber, paymentTerm, paymentType);
	}
	
	public PaginationWrapper<SCPackage> obtainPackagesByPage(int pageNum) {
		return subcontractorRepository.obtainPackagesByPage(pageNum);
	}

	public PaginationWrapper<SubcontractorTenderAnalysisWrapper> obtainTenderAnalysisPaginationWrapper(String vendorNo,String division, String jobNumber, String packageNumber, String tenderStatus) throws DatabaseOperationException {
		return subcontractorRepository.obtainTenderAnalysisPaginationWrapper(vendorNo, division, jobNumber, packageNumber, tenderStatus);
	}

	public PaginationWrapper<SubcontractorTenderAnalysisWrapper> obtainTenderAnalysisWrapperByPage(int pageNum) {
		return subcontractorRepository.obtainTenderAnalysisWrapperByPage(pageNum);
	}

}
