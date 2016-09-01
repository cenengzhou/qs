package com.gammon.pcms.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.UnitOfMeasurement;
import com.gammon.qs.service.UnitService;
import com.gammon.qs.wrapper.UDC;

@RestController
@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsEnq())")
@RequestMapping(value = "service/unit/")
public class UnitController {

	@Autowired
	private UnitService unitService;
	
	@RequestMapping(value = "getAllWorkScopes", method = RequestMethod.GET)
	public List<UDC> getAllWorkScopes() throws DatabaseOperationException{
		List<UDC> wrapperList = new ArrayList<UDC>();
//		try {
			wrapperList.addAll(unitService.getAllWorkScopes());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return wrapperList;
	}
	
	
	@RequestMapping(value = "getUnitOfMeasurementList", method = RequestMethod.GET)
	public List<UnitOfMeasurement> getUnitOfMeasurementList() throws Exception{
		List<UnitOfMeasurement> unitList = new ArrayList<UnitOfMeasurement>();
//		try {
			unitList = unitService.getUnitOfMeasurementList();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return unitList;
	}
	
	@RequestMapping(value = "getAppraisalPerformanceGroupMap", method = RequestMethod.GET)
	public Map<String, String> getAppraisalPerformanceGroupMap(){
//		try{
			return unitService.getAppraisalPerformanceGroupMap();
//		} catch (Exception e){
//			e.printStackTrace();
//		}
//		return null;
	}

	@RequestMapping(value = "getSCStatusCodeMap", method = RequestMethod.GET)
	public Map<String, String> getSCStatusCodeMap(){
//		try{
			return unitService.getSCStatusCodeMap();
//		} catch (Exception e){
//			e.printStackTrace();
//		}
//		return null;
	}

}
