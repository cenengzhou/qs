package com.gammon.pcms.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.dao.TenderVarianceHBDao;
import com.gammon.pcms.model.TenderVariance;
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

	public String createTenderVariance(String jobNo, String subcontractNo, String subcontractorNo, List<TenderVariance> tenderVarianceList) throws Exception {
		String error = "";
		try {
			Tender tender = tenderHBDao.obtainTender(jobNo, subcontractNo, Integer.valueOf(subcontractorNo));
			if(tender == null || tender.getId() == null){
				error = "Tender does not exist.";
				return error;
			}
			
			//Remove from DB
			List<TenderVariance> tenderVarianceInDB = tenderVarianceHBDao.obtainTenderVarianceList(jobNo, subcontractNo, subcontractorNo);
			for (TenderVariance variance: tenderVarianceInDB){
				tenderVarianceHBDao.delete(variance);
			}
			
			//Insert into DB
			for (TenderVariance tenderVariance: tenderVarianceList){
				tenderVariance.setNoJob(jobNo);
				tenderVariance.setNoSubcontract(subcontractNo);
				tenderVariance.setNoSubcontractor(subcontractorNo);
				tenderVariance.setNameSubcontractor(tender.getNameSubcontractor());
				tenderVariance.setTender(tender);
				tenderVarianceHBDao.insert(tenderVariance);
			}
			
			
		} catch (Exception e) {
			error= "Tender Variance cannot be updated.";
			e.printStackTrace();
		}
		return error;
	}
	
	public List<TenderVariance> obtainAllTenderVariance() throws Exception{
		return tenderVarianceHBDao.getAll();
	}

	public List<TenderVariance> obtainTenderVarianceList(String jobNo, String subcontractNo, String subcontractorNo) {
		List<TenderVariance> tenderVarianceList = null;
		try {
			tenderVarianceList = tenderVarianceHBDao.obtainTenderVarianceList(jobNo, subcontractNo, subcontractorNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tenderVarianceList;
	}
}
