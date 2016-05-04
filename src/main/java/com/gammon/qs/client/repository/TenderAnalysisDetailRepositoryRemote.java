package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.TenderAnalysis;
import com.gammon.qs.domain.TenderAnalysisDetail;
import com.google.gwt.user.client.rpc.RemoteService;

public interface TenderAnalysisDetailRepositoryRemote extends RemoteService {
	public List<TenderAnalysisDetail> obtainTenderAnalysisDetailByTenderAnalysis(TenderAnalysis TenderAnalysis)throws DatabaseOperationException;
}
