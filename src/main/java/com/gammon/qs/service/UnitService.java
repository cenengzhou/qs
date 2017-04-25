package com.gammon.qs.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.UnitWSDao;
import com.gammon.qs.domain.UnitOfMeasurement;
import com.gammon.qs.wrapper.UDC;
@Service
@Transactional(rollbackFor = Exception.class, value = "transactionManager")
public class UnitService {
	@Autowired
	private UnitWSDao unitDao;

	public List<UnitOfMeasurement> getUnitOfMeasurementList() throws Exception {
		return unitDao.getUnitOfMeasurementList();
	}

	public List<UDC> getAllWorkScopes() throws DatabaseOperationException {
		return unitDao.obtainWorkScopeList();
	}

	public UDC obtainWorkScope(String workScopeCode) throws DatabaseOperationException {
		return unitDao.obtainWorkScope(workScopeCode);
	}

	public Map<String, String> getAppraisalPerformanceGroupMap() {
		return unitDao.getAppraisalPerformanceGroupMap();
	}
	
	public Map<String, String> getSCStatusCodeMap() {
		return unitDao.getSCStatusCodeMap();
	}
}
