package com.gammon.pcms.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.pcms.model.TenderVariance;
import com.gammon.pcms.service.TenderVarianceService;
import com.gammon.qs.application.exception.DatabaseOperationException;

@RestController
@RequestMapping(value = "service", method = RequestMethod.POST)
public class TenderVarianceController {

	@Autowired
	private TenderVarianceService tenderVarianceService;
	
	@RequestMapping("createTenderVariance")
	public void createTenderVariance(@RequestBody TenderVariance tenderVariance, HttpServletRequest request, HttpServletResponse response) throws DatabaseOperationException{
		tenderVarianceService.createTenderVariance(tenderVariance);
		response.setStatus(HttpServletResponse.SC_CREATED);
	}
	
	@RequestMapping("obtainAllTenderVariance")
	public List<TenderVariance> obtainAllTenderVariance(HttpServletRequest request, HttpServletResponse response) throws DatabaseOperationException{
		return tenderVarianceService.obtainAllTenderVariance();
	}
}
