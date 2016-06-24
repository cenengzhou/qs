package com.gammon.pcms.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.dao.TenderVarianceHBDao;
import com.gammon.pcms.model.TenderVariance;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.TenderHBDao;
import com.gammon.qs.domain.Tender;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "request")
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class TenderVarianceService {

	@Autowired
	private TenderVarianceHBDao tenderVarianceHBDao;
	@Autowired
	private TenderHBDao tenderHBDao;

	public void createTenderVariance(TenderVariance tenderVariance) throws DatabaseOperationException {
		Tender tender = tenderVariance.getTender();
		if(tender == null || tender.getId() == null) throw new DatabaseOperationException("Tender ID is null");
		tender = tenderHBDao.get(tender.getId().longValue());
		tenderVariance.setTender(tender);
		tenderVarianceHBDao.insert(tenderVariance);
	}
	
	public List<TenderVariance> obtainAllTenderVariance() throws DatabaseOperationException{
		return tenderVarianceHBDao.getAll();
	}
}
