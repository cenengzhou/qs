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
@RequestMapping(value = "service/unit/")
public class UnitController {

	@Autowired
	private UnitService unitService;
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('UnitController','getAllWorkScopes', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getAllWorkScopes", method = RequestMethod.GET)
	public List<UDC> getAllWorkScopes() throws DatabaseOperationException{
		List<UDC> wrapperList = new ArrayList<UDC>();
		wrapperList.addAll(unitService.getAllWorkScopes());
		return wrapperList;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('UnitController','getUnitOfMeasurementList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getUnitOfMeasurementList", method = RequestMethod.GET)
	public List<UnitOfMeasurement> getUnitOfMeasurementList() throws Exception{
		List<UnitOfMeasurement> unitList = new ArrayList<UnitOfMeasurement>();
		unitList = unitService.getUnitOfMeasurementList();
		return unitList;
	}
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('UnitController','getAppraisalPerformanceGroupMap', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getAppraisalPerformanceGroupMap", method = RequestMethod.GET)
	public Map<String, String> getAppraisalPerformanceGroupMap(){
		return unitService.getAppraisalPerformanceGroupMap();
	}

	@PreAuthorize(value = "@GSFService.isFnEnabled('UnitController','getSCStatusCodeMap', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "getSCStatusCodeMap", method = RequestMethod.GET)
	public Map<String, String> getSCStatusCodeMap(){
		return unitService.getSCStatusCodeMap();
	}

}
