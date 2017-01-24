package com.gammon.qs.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.gammon.jde.webservice.serviceRequester.UnitDescriptionListQueryManager.getTrimUDC.GetTrimUDCRequestObj;
import com.gammon.jde.webservice.serviceRequester.UnitDescriptionListQueryManager.getTrimUDC.GetTrimUDCResponseListObj;
import com.gammon.jde.webservice.serviceRequester.UnitDescriptionListQueryManager.getUnitDescriptionList_Refactor.GetUnitDescriptionRequestObj;
import com.gammon.jde.webservice.serviceRequester.UnitDescriptionListQueryManager.getUnitDescriptionList_Refactor.GetUnitDescriptionResponseListObj;
import com.gammon.jde.webservice.serviceRequester.UnitDescriptionListQueryManager.getUnitDescriptionList_Refactor.GetUnitDescriptionResponseObj;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.UnitOfMeasurement;
import com.gammon.qs.webservice.WSConfig;
import com.gammon.qs.webservice.WSSEHeaderWebServiceMessageCallback;
import com.gammon.qs.wrapper.UDC;
@Repository
public class UnitWSDao {
	private Logger logger = Logger.getLogger(UnitWSDao.class.getName());
	@Autowired
	@Qualifier("getUnitDescriptionListResponseWebServiceTemplate")
	private WebServiceTemplate getUnitDescriptionListResponseWebServiceTemplate;
	@Autowired
	@Qualifier("getTrimUDCListResponseWebServiceTemplate")
	private WebServiceTemplate getTrimUDCListResponseWebServiceTemplate;
	@Autowired
	@Qualifier("webservicePasswordConfig")
	private WSConfig wsConfig;

	public List<UnitOfMeasurement> getUnitOfMeasurementList() throws Exception {
		GetUnitDescriptionRequestObj requestObj = new GetUnitDescriptionRequestObj();

		requestObj.setProductCode("00");
		requestObj.setUserDefinedCodes("UM");
		long start = System.currentTimeMillis();
		GetUnitDescriptionResponseListObj responseListObj = (GetUnitDescriptionResponseListObj) getUnitDescriptionListResponseWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		long end = System.currentTimeMillis();

		logger.info("Time for call WS(getUnitOfMeasurementList): " + ((end - start) / 1000.00));

		return unitResponseListObjTransformer(responseListObj);
	}

	private List<UnitOfMeasurement> unitResponseListObjTransformer(GetUnitDescriptionResponseListObj responseList) {
		List<UnitOfMeasurement> result = new LinkedList<UnitOfMeasurement>();

		if (responseList == null)
			return result;

		for (GetUnitDescriptionResponseObj curObj : responseList.getGetUnitDescriptionResponseObj()) {
			UnitOfMeasurement uom = new UnitOfMeasurement();
			uom.setUnitCode(curObj.getCode());
			uom.setBalanceType(curObj.getBalanceTyp());
			uom.setUnitDescriptiom(curObj.getDescription());
			result.add(uom);
		}
		return result;
	}

	/**
	 * @author tikywong
	 * modified on April 19, 2013
	 * Get the work scope list from UDC:59|A2 of JDE 
	 */
	public List<UDC> obtainWorkScopeList() throws DatabaseOperationException {
		List<UDC> workScopeList = new ArrayList<UDC>();

		GetTrimUDCRequestObj requestObj = new GetTrimUDCRequestObj();
		requestObj.setProductCode("59");
		requestObj.setUserDefinedCodes("A2");

		logger.info("call WS(getTrimUDCListResponseWebServiceTemplate): Request Object -" +
					" ProductCode: " + requestObj.getProductCode() +
					" UserDefinedCodes: " + requestObj.getUserDefinedCodes());
		GetTrimUDCResponseListObj responseListObj = (GetTrimUDCResponseListObj) getTrimUDCListResponseWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));

		if (responseListObj != null) {
			for (GetUnitDescriptionResponseObj curObj : responseListObj.getGetTrimUDCResponseObj()) {
				UDC udc = new UDC();
				udc.setCode(curObj.getCode().trim());
				udc.setDescription(curObj.getDescription().trim());

				workScopeList.add(udc);
			}
		}

		logger.info("RETURNED WORKSCOPE RECORDS(FULL LIST FROM WS)SIZE: " + workScopeList.size());
		return workScopeList;
	}

	/**
	 * @author tikywong
	 * modified on April 19, 2013
	 * Production Code: 59
	 * User Defined Code: A2
	 */
	public UDC obtainWorkScope(String workScopeCode) throws DatabaseOperationException {
		logger.info("WorkScope Code: " + workScopeCode);
		if (workScopeCode == null)
			return null;

		// 1. Obtain the workScope list
		List<UDC> workScopes = obtainWorkScopeList();

		// 2. Find the specified workScope with the workScope code
		for (UDC udc : workScopes) {
			if (udc.getCode().equalsIgnoreCase(workScopeCode.trim())) {
				logger.info("UDC Code: " + udc.getCode() + " Description: " + udc.getCode());
				return udc;
			}
		}

		return null;
	}

	/**
	 * Get a Map containing the user defined codes and descriptions
	 * of those codes from the WS
	 * @author matthewatc
	 * Date: 03/02/12
	 * @return a Map<String, String> for which the keys are the used defined codes,
	 * and the corresponding value for a key is the description of that code.
	 * NOTE: the key strings are run through trim() before the map is constructed
	 */
	public Map<String, String> getUDCMap(String productCode, String userDefinedCodes) {
		GetTrimUDCRequestObj requestObj = new GetTrimUDCRequestObj();

		requestObj.setProductCode(productCode);
		requestObj.setUserDefinedCodes(userDefinedCodes);
		long start = System.currentTimeMillis();
		GetTrimUDCResponseListObj responseListObj = (GetTrimUDCResponseListObj) getTrimUDCListResponseWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));
		long end = System.currentTimeMillis();
		logger.info("Time for call WS: " + ((end - start) / 1000.00));
		if (responseListObj == null || responseListObj.getGetTrimUDCResponseObj() == null || responseListObj.getGetTrimUDCResponseObj().size() < 1)
			return null;
		Map<String, String> resultMap = new HashMap<String, String>();
		for (GetUnitDescriptionResponseObj respondObj : responseListObj.getGetTrimUDCResponseObj()) {
			resultMap.put(respondObj.getCode().trim(), respondObj.getDescription());
		}
		return resultMap;
	}

	/**
	 * Get a Map containing the performance group codes and descriptions
	 * for subcontractor appraisal from the WS
	 * @author peterchan, matthewatc
	 * Date: 03/02/12
	 * @return a Map<String, String> for which the keys are the performance 
	 * group codes, and the corresponding value for a key is the description
	 * of that code.
	 */
	public Map<String, String> getAppraisalPerformanceGroupMap() {
		return getUDCMap("58", "GC");
	}

	/**
	 * Get a Map containing the SC status codes and descriptions from the WS
	 * @author matthewatc
	 * Date: 03/02/12
	 * @return a Map<String, String> for which the keys are the SC status codes
	 * and the corresponding value for a key is the description of that code.
	 */
	public Map<String, String> getSCStatusCodeMap() {
		return getUDCMap("59", "ST");
	}

	public List<UDC> getTrimedUDCList(String productCode, String userDefinedCodes) {
		GetTrimUDCRequestObj requestObj = new GetTrimUDCRequestObj();

		requestObj.setProductCode(productCode);
		requestObj.setUserDefinedCodes(userDefinedCodes);

		GetTrimUDCResponseListObj responseListObj = (GetTrimUDCResponseListObj) getTrimUDCListResponseWebServiceTemplate.marshalSendAndReceive(requestObj, new WSSEHeaderWebServiceMessageCallback(wsConfig.getUserName(), wsConfig.getPassword()));

		List<UDC> resultList = new ArrayList<UDC>();
		if (responseListObj == null ||
				responseListObj.getGetTrimUDCResponseObj() == null || responseListObj.getGetTrimUDCResponseObj().size() == 0)
			return resultList;

		for (GetUnitDescriptionResponseObj respondObj : responseListObj.getGetTrimUDCResponseObj()) {
			UDC udc = new UDC();

			udc.setCode(respondObj.getCode());
			udc.setDescription(respondObj.getDescription());

			resultList.add(udc);
		}
		return resultList;
	}
}
