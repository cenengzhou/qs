package com.gammon.qs.client.repository;

import java.util.List;
import java.util.Map;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.UnitOfMeasurement;
import com.gammon.qs.wrapper.UDC;
import com.google.gwt.user.client.rpc.RemoteService;

public interface UnitRepositoryRemote extends RemoteService {
	public List<UnitOfMeasurement> getUnitOfMeasurementList() throws Exception;
	public Map<String, String> getAppraisalPerformanceGroupMap();
	public Map<String, String> getUDCMap(String productCode, String userDefinedCodes);
	public Map<String, String> getSCStatusCodeMap();
	public List<UDC> getAllWorkScopes() throws DatabaseOperationException; 
}
