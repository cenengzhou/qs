package com.gammon.qs.client.repository;

import java.util.List;
import java.util.Map;

import com.gammon.qs.domain.UnitOfMeasurement;
import com.gammon.qs.wrapper.UDC;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UnitRepositoryRemoteAsync {
	 void getUnitOfMeasurementList(AsyncCallback<List<UnitOfMeasurement>> callback);
	 void getAppraisalPerformanceGroupMap(AsyncCallback<Map<String, String>> callback);
	 void getUDCMap(String productCode, String userDefinedCodes, AsyncCallback<Map<String, String>> callback);
	 void getSCStatusCodeMap(AsyncCallback<Map<String, String>> callback);
	 void getAllWorkScopes(AsyncCallback<List<UDC>> callback);
}
