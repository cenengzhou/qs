package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.application.User;
import com.gammon.qs.domain.Job;
import com.gammon.qs.domain.RepackagingEntry;
import com.gammon.qs.wrapper.EmailMessage;
import com.gammon.qs.wrapper.RepackagingDetailComparisonWrapper;
import com.gammon.qs.wrapper.RepackagingPaginationWrapper;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RepackagingDetailRepositoryRemoteAsync {
	void prepareRepackagingDetails(RepackagingEntry repackagingEntry, AsyncCallback<Double> callback);
	void getRepackagingDetailsNewSearch(RepackagingEntry repackagingEntry, String packageNo,
			String objectCode, String subsidiaryCode, boolean changesOnly, AsyncCallback<RepackagingPaginationWrapper<RepackagingDetailComparisonWrapper>> callback);
	void getRepackagingDetailsByPage(int pageNum, AsyncCallback<RepackagingPaginationWrapper<RepackagingDetailComparisonWrapper>> callback);
	void postRepackagingDetails(RepackagingEntry repackagingEntry, String username, AsyncCallback<Boolean> callback);
	void clearCache(AsyncCallback<Boolean> callback);
	void sendRepackagingConfirmationEmail (EmailMessage emailMessage, AsyncCallback<String> callback);
	void obtainRepackagingEmailRecipients(AsyncCallback<List<User>> callback);
	void obtainUsernamesByEmails(List<String> emailAddressList, AsyncCallback<List<String>> callback);
	void generateResourceSummaries(Job job, AsyncCallback<RepackagingEntry> callback);
}
