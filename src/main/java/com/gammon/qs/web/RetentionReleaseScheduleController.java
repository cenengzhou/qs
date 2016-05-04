package com.gammon.qs.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.client.repository.RetentionReleaseScheduleRepositoryRemote;
import com.gammon.qs.domain.MainContractCertificate;
import com.gammon.qs.domain.RetentionRelease;
import com.gammon.qs.service.RetentionReleaseScheduleService;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.RetentionReleaseScheduleEnquiryWindowsWrapper;
import com.gammon.qs.wrapper.RetentionReleaseSchedulePaginationWrapper;

import net.sf.gilead.core.PersistentBeanManager;
@Service
public class RetentionReleaseScheduleController extends GWTSpringController implements RetentionReleaseScheduleRepositoryRemote {
	
	private static final long serialVersionUID = 7023756674762648088L;
	@Autowired
	private RetentionReleaseScheduleService retentionReleaseScheduleRepository;
	@Override
	@Autowired
	public void setBeanManager(PersistentBeanManager manager) {
		super.setBeanManager(manager);
	}

	public String obtainCumulativeRetentionReleaseByJob(MainContractCertificate mainCert) throws DatabaseOperationException{
		return retentionReleaseScheduleRepository.obtainCumulativeRetentionReleaseByJob(mainCert);
	}
	
	public List<RetentionRelease> calculateRetentionReleaseScheduleByJob(MainContractCertificate mainCert) throws DatabaseOperationException{
		return retentionReleaseScheduleRepository.calculateRetentionReleaseScheduleByJob(mainCert);
	}

	public PaginationWrapper<RetentionReleaseScheduleEnquiryWindowsWrapper> obtainRetentionReleaseSchedule(
			String division, String company, String jobNo) throws DatabaseOperationException {		
		return retentionReleaseScheduleRepository.obtainRetentionReleaseScheduleForPagination(division,company,jobNo);
	}
	
	/**
	 * add by paulyiu 20150901
	 */
	@Override
	public RetentionReleaseSchedulePaginationWrapper obtainRetentionReleaseScheduleForPaginationWrapper(
			String division, String company, String jobNo) throws DatabaseOperationException {		
		return retentionReleaseScheduleRepository.obtainRetentionReleaseScheduleForPaginationWrapper(division,company,jobNo);
	}

	/**
	 * add by paulyiu 20150901
	 */
	@Override
	public RetentionReleaseSchedulePaginationWrapper obtainRetentionReleaseScheduleForPaginationWrapperByPage(
			int pageNo) throws DatabaseOperationException {
		return retentionReleaseScheduleRepository.obtainRetentionReleaseScheduleForPaginationWrapperByPage(pageNo);
	}
	
	public Boolean updateRetentionRelease(String jobNo, List<RetentionRelease> saveList) throws DatabaseOperationException{
		return retentionReleaseScheduleRepository.updateForecastRetentionRelease(jobNo, saveList);
	}
	
	public PaginationWrapper<RetentionReleaseScheduleEnquiryWindowsWrapper> obtainRetentionReleaseScheduleForPaginationByPage(
			int pageNo) throws DatabaseOperationException {
		return retentionReleaseScheduleRepository.obtainRetentionReleaseScheduleForPaginationByPage(pageNo);
	}

	/**
	 * add by paulnpyiu
	 * 2015-10-06
	 * add search filter for status and cert status
	 */
	@Override
	public RetentionReleaseSchedulePaginationWrapper obtainRetentionReleaseScheduleForPaginationWrapper(
			String division, String company, String jobNo, String status, String certStatus) throws DatabaseOperationException {
		return retentionReleaseScheduleRepository.obtainRetentionReleaseScheduleForPaginationWrapper(division,company,jobNo,status,certStatus);
	}

}
