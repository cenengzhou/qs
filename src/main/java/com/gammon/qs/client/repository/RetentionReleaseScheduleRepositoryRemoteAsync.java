package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.domain.MainContractCertificate;
import com.gammon.qs.domain.RetentionRelease;
import com.gammon.qs.wrapper.PaginationWrapper;
import com.gammon.qs.wrapper.RetentionReleaseScheduleEnquiryWindowsWrapper;
import com.gammon.qs.wrapper.RetentionReleaseSchedulePaginationWrapper;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RetentionReleaseScheduleRepositoryRemoteAsync {
	void obtainCumulativeRetentionReleaseByJob(MainContractCertificate mainCert, AsyncCallback<String> callback);
	void calculateRetentionReleaseScheduleByJob(MainContractCertificate mainCert, AsyncCallback<List<RetentionRelease>> callback);
	void obtainRetentionReleaseSchedule(String division,String company,	String jobNo,AsyncCallback<PaginationWrapper<RetentionReleaseScheduleEnquiryWindowsWrapper>> callback);
	void updateRetentionRelease(String jobNo, List<RetentionRelease> saveList, AsyncCallback<Boolean> callback);
	void obtainRetentionReleaseScheduleForPaginationByPage(int pageNo,AsyncCallback<PaginationWrapper<RetentionReleaseScheduleEnquiryWindowsWrapper>> callback);
	void obtainRetentionReleaseScheduleForPaginationWrapper(String division, String company, String jobNo, AsyncCallback<RetentionReleaseSchedulePaginationWrapper> callback);
	void obtainRetentionReleaseScheduleForPaginationWrapperByPage(int pageNo, AsyncCallback<RetentionReleaseSchedulePaginationWrapper> asyncCallback);
	void obtainRetentionReleaseScheduleForPaginationWrapper(String division,String company,String jobNo,String status,String certStatus,AsyncCallback<RetentionReleaseSchedulePaginationWrapper> asyncCallback);
}
