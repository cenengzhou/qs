/**
 * GammonQS-PH3
 * AccountLedgerRepositoryRemote.java
 * @author koeyyeung
 * Created on May 9, 2013 12:08:47 PM
 */
package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.AccountLedger;
import com.gammon.qs.wrapper.BudgetForecastWrapper;
import com.google.gwt.user.client.rpc.RemoteService;

public interface AccountLedgerRepositoryRemote extends RemoteService {

	public List<BudgetForecastWrapper> obtainAccountLedgersByJobNo(String jobNo, Integer month, Integer year) throws DatabaseOperationException;
	public List<AccountLedger> obtainAccountLedgersByLedgerType(String jobNo, String ledgerType) throws DatabaseOperationException;
}
