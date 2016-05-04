package com.gammon.qs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.client.repository.BudgetPostingRepositoryRemote;
import com.gammon.qs.service.BudgetPostingService;

import net.sf.gilead.core.PersistentBeanManager;
@Service
public class BudgetPostingRepositoryController extends GWTSpringController
		implements BudgetPostingRepositoryRemote {

	private static final long serialVersionUID = 4640817770254406136L;
	@Autowired
	private BudgetPostingService budgetPostingService;
	@Override
	@Autowired
	public void setBeanManager(PersistentBeanManager manager) {
		super.setBeanManager(manager);
	}
	
	public String postBudget(String jobNumber, String username) throws Exception {
		return budgetPostingService.postBudget(jobNumber, username);
	}

}
