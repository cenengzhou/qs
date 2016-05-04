package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.TenderAnalysis;
import com.gammon.qs.domain.TenderAnalysisDetail;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TenderAnalysisDetailRepositoryRemoteAsync {
	void obtainTenderAnalysisDetailByTenderAnalysis(TenderAnalysis TenderAnalysis, AsyncCallback<List<TenderAnalysisDetail>> callback)throws DatabaseOperationException;
}
