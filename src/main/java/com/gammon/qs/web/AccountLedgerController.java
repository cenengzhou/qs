/**
 * GammonQS-PH3
 * AccountLedgerController.java
 * @author koeyyeung
 * Created on May 9, 2013 1:27:15 PM
 */
package com.gammon.qs.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.client.repository.AccountLedgerRepositoryRemote;
import com.gammon.qs.domain.AccountLedger;
import com.gammon.qs.service.AccountLedgerService;
import com.gammon.qs.wrapper.BudgetForecastWrapper;

import net.sf.gilead.core.PersistentBeanManager;
@Service
public class AccountLedgerController extends GWTSpringController  implements AccountLedgerRepositoryRemote {

	private static final long serialVersionUID = 1777602028459353521L;
	@Autowired
	private AccountLedgerService accountLedgerRepository;
	@Override
	@Autowired
	public void setBeanManager(PersistentBeanManager manager) {
		super.setBeanManager(manager);
	}
	
	public List<BudgetForecastWrapper> obtainAccountLedgersByJobNo(String jobNo, Integer month, Integer year)throws DatabaseOperationException {
		return accountLedgerRepository.obtainAccountLedgersByJobNo(jobNo, month, year);
	}

	public List<AccountLedger> obtainAccountLedgersByLedgerType(String jobNo, String ledgerType) throws DatabaseOperationException {
		return accountLedgerRepository.obtainAccountLedgersByLedgerType(jobNo, ledgerType);
	}

}
