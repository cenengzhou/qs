package com.gammon.qs.client.repository;

import java.util.Date;

import com.gammon.qs.wrapper.IVHistoryPaginationWrapper;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IVPostingHistoryRepositoryRemoteAsync {
	public void obtainIVPostingHistory(String jobNumber,
			String packageNo, String objectCode, String subsidiaryCode,
			Date fromDate, Date toDate, AsyncCallback<IVHistoryPaginationWrapper> callback);
	public void getIVPostingHistoryByPage(int pageNum, AsyncCallback<IVHistoryPaginationWrapper> callback);
	public void clearCache(AsyncCallback<Boolean> callback);
}
