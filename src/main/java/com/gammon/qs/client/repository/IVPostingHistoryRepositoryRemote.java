package com.gammon.qs.client.repository;

import java.util.Date;

import com.gammon.qs.application.exception.ValidateBusinessLogicException;
import com.gammon.qs.wrapper.IVHistoryPaginationWrapper;
import com.google.gwt.user.client.rpc.RemoteService;

public interface IVPostingHistoryRepositoryRemote extends RemoteService {
	public IVHistoryPaginationWrapper obtainIVPostingHistory(String jobNumber,
			String packageNo, String objectCode, String subsidiaryCode,
			Date fromDate, Date toDate) throws Exception;
	public IVHistoryPaginationWrapper getIVPostingHistoryByPage(int pageNum) throws ValidateBusinessLogicException;
	public Boolean clearCache();
}
