/**
 * GammonQS-PH3
 * AccountLedgerRepositoryRemoteAsync.java
 * @author koeyyeung
 * Created on May 9, 2013 12:09:37 PM
 */
package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.domain.AccountLedger;
import com.gammon.qs.wrapper.BudgetForecastWrapper;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AccountLedgerRepositoryRemoteAsync {

	public void obtainAccountLedgersByJobNo(String jobNo, Integer month, Integer year, AsyncCallback<List<BudgetForecastWrapper>> callback);
	public void obtainAccountLedgersByLedgerType(String jobNo, String ledgerType, AsyncCallback<List<AccountLedger>> callback);
	
}
