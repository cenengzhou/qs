package com.gammon.pcms.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.qs.domain.IVPostingHist;
import com.gammon.qs.service.IVPostingHistService;

@RestController
@RequestMapping(value = "service/ivpostinghist/")
public class IVPostingHistController {

	@Autowired
	private IVPostingHistService ivPostingHistService;
	
	@RequestMapping(value = "obtainIVPostingHistoryList", method = RequestMethod.POST)
	public List<IVPostingHist> obtainIVPostingHistoryList(@RequestParam String jobNumber, Date fromDate, Date toDate){
		List<IVPostingHist> resultList = new ArrayList<IVPostingHist>();
		try {
			resultList.addAll(ivPostingHistService.obtainIVPostingHistoryList(jobNumber, fromDate, toDate));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

}
