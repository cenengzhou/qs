package com.gammon.pcms.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gammon.qs.domain.IVPostingHist;
import com.gammon.qs.service.IVPostingHistService;

@RestController
@RequestMapping(value = "service/ivpostinghist/")
public class IVPostingHistController {

	@Autowired
	private IVPostingHistService ivPostingHistService;
	@Autowired
	private ObjectMapper objectMapper;
	
	@PreAuthorize(value = "@GSFService.isFnEnabled('IVPostingHistController','obtainIVPostingHistoryList', @securityConfig.getRolePcmsEnq())")
	@RequestMapping(value = "obtainIVPostingHistoryList", method = RequestMethod.POST)
	public List<IVPostingHist> obtainIVPostingHistoryList(@RequestBody Map<String, Object> searchObject) throws Exception{
		String jobNumber = objectMapper.convertValue(searchObject.get("jobNumber"), String.class);
		Date fromDate = objectMapper.convertValue(searchObject.get("fromDate"), Date.class);
		Date toDate = objectMapper.convertValue(searchObject.get("toDate"), Date.class);
		List<IVPostingHist> resultList = new ArrayList<IVPostingHist>();
		resultList.addAll(ivPostingHistService.obtainIVPostingHistoryList(jobNumber, fromDate, toDate));
		return resultList;
	}

}
