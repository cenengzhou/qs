package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.MainContractCertificate;
import com.gammon.qs.domain.RetentionRelease;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.RetentionReleaseScheduleEnquiryWindowsWrapper;
import com.gammon.qs.wrapper.RetentionReleaseSchedulePaginationWrapper;
import com.google.gwt.user.client.rpc.RemoteService;

public interface RetentionReleaseScheduleRepositoryRemote extends RemoteService {
	
	public String obtainCumulativeRetentionReleaseByJob(MainContractCertificate mainCert) throws DatabaseOperationException;
	public List<RetentionRelease> calculateRetentionReleaseScheduleByJob(MainContractCertificate mainCert)throws DatabaseOperationException;
	public PaginationWrapper<RetentionReleaseScheduleEnquiryWindowsWrapper> obtainRetentionReleaseSchedule(String division,String company, String jobNo) throws DatabaseOperationException;
	public PaginationWrapper<RetentionReleaseScheduleEnquiryWindowsWrapper> obtainRetentionReleaseScheduleForPaginationByPage(int pageNo) throws DatabaseOperationException;
	public Boolean updateRetentionRelease(String jobNo, List<RetentionRelease> saveList) throws DatabaseOperationException;
	public RetentionReleaseSchedulePaginationWrapper obtainRetentionReleaseScheduleForPaginationWrapper(String division, String company, String jobNo) throws DatabaseOperationException;
	public RetentionReleaseSchedulePaginationWrapper obtainRetentionReleaseScheduleForPaginationWrapperByPage(int pageNo) throws DatabaseOperationException;
	public RetentionReleaseSchedulePaginationWrapper obtainRetentionReleaseScheduleForPaginationWrapper(String division,String company,String jobNo,String status,String certStatus) throws DatabaseOperationException;
}
