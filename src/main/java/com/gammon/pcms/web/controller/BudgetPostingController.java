/**
 * PCMS-TC
 * com.gammon.pcms.web.controller
 * TransitController.java
 * @since Jun 29, 2016 5:45:47 PM
 * @author tikywong
 */
package com.gammon.pcms.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.qs.service.BudgetPostingService;

@RestController
@RequestMapping(value = "service/budgetposting/")
public class BudgetPostingController {

	@Autowired
	private BudgetPostingService budgetPostingService;
	
	@RequestMapping(value = "postBudget", method = RequestMethod.POST)
	public String postBudget(@RequestParam String jobNumber){
		return budgetPostingService.postBudget(jobNumber, null);
	}

}
