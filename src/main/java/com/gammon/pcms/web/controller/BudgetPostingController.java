package com.gammon.pcms.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.qs.service.BudgetPostingService;

@RestController
@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsEnq())")
@RequestMapping(value = "service/budgetposting/")
public class BudgetPostingController {

	@Autowired
	private BudgetPostingService budgetPostingService;
	
	@PreAuthorize(value = "hasRole(@securityConfig.getRolePcmsQs())")
	@RequestMapping(value = "postBudget", method = RequestMethod.POST)
	public String postBudget(@RequestParam String jobNumber){
		return budgetPostingService.postBudget(jobNumber, null);
	}

}
