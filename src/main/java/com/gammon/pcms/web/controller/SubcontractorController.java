
package com.gammon.pcms.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.qs.service.SubcontractorService;
import com.gammon.qs.wrapper.SubcontractorWrapper;

@RestController
@RequestMapping(value = "service/subcontractor/")
public class SubcontractorController {

	@Autowired
	private SubcontractorService subcontractorService;
	
	@RequestMapping(value = "obtainSubcontractorWrappers", method = RequestMethod.POST)
	public List<SubcontractorWrapper> obtainSubcontractorWrappers(
			@RequestParam(defaultValue = "") String workScope, @RequestParam(defaultValue = "") String subcontractor){
		List<SubcontractorWrapper> wrapperList = new ArrayList<SubcontractorWrapper>();
		if(workScope.equals("") && subcontractor.equals("")) {
			throw new IllegalArgumentException("Please input work scope or subcontractor to search");
		}
		try {
			wrapperList.addAll(subcontractorService.obtainSubcontractorWrappers(workScope, subcontractor));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wrapperList;
	}

	@RequestMapping(value = "obtainClientWrappers", method = RequestMethod.POST)
	public List<SubcontractorWrapper> obtainClientWrappers(@RequestParam String client){
		List<SubcontractorWrapper> wrapperList = new ArrayList<SubcontractorWrapper>();
		try {
			wrapperList.addAll(subcontractorService.obtainClientWrappers(client));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wrapperList;
	}

}
