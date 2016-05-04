package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.application.User;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.RepackagingEntry;
import com.gammon.qs.wrapper.EmailMessage;
import com.gammon.qs.wrapper.RepackagingDetailComparisonWrapper;
import com.gammon.qs.wrapper.RepackagingPaginationWrapper;
import com.google.gwt.user.client.rpc.RemoteService;

public interface RepackagingDetailRepositoryRemote extends RemoteService{
	Double prepareRepackagingDetails(RepackagingEntry repackagingEntry) throws Exception;
	RepackagingPaginationWrapper<RepackagingDetailComparisonWrapper> getRepackagingDetailsNewSearch(RepackagingEntry repackagingEntry, String packageNo,
			String objectCode, String subsidiaryCode, boolean changesOnly) throws Exception;
	RepackagingPaginationWrapper<RepackagingDetailComparisonWrapper> getRepackagingDetailsByPage(int pageNum);
	Boolean postRepackagingDetails(RepackagingEntry repackagingEntry, String username) throws Exception;
	Boolean clearCache();
	String sendRepackagingConfirmationEmail (EmailMessage emailMessage);
	List<User> obtainRepackagingEmailRecipients ();
	List<String> obtainUsernamesByEmails(List<String> emailAddressList);
	public RepackagingEntry generateResourceSummaries(Job job) throws Exception;
}
