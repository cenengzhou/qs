package com.gammon.qs.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.client.repository.TenderAnalysisDetailRepositoryRemote;
import com.gammon.qs.dao.TenderAnalysisDetailHBDao;
import com.gammon.qs.domain.TenderAnalysis;
import com.gammon.qs.domain.TenderAnalysisDetail;
@Service
public class TenderAnalysisDetailRepositoryController extends GWTSpringController implements TenderAnalysisDetailRepositoryRemote {

	private static final long serialVersionUID = 4272532249555657181L;
	@Autowired
	private TenderAnalysisDetailHBDao tenderAnalysisDetailHBDao;
	
	@Override
	public List<TenderAnalysisDetail> obtainTenderAnalysisDetailByTenderAnalysis(TenderAnalysis TenderAnalysis)
			throws DatabaseOperationException {
		return tenderAnalysisDetailHBDao.obtainTenderAnalysisDetailByTenderAnalysis(TenderAnalysis);
	}

}
