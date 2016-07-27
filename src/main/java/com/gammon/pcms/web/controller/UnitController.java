package com.gammon.pcms.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.qs.service.UnitService;
import com.gammon.qs.wrapper.UDC;

@RestController
@RequestMapping(value = "service/unit/")
public class UnitController {

	@Autowired
	private UnitService unitService;
	
	@RequestMapping(value = "getAllWorkScopes", method = RequestMethod.POST)
	public List<UDC> getAllWorkScopes(){
		List<UDC> wrapperList = new ArrayList<UDC>();
		try {
			wrapperList.addAll(unitService.getAllWorkScopes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wrapperList;
	}

}