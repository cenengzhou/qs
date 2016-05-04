package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.Bill;
import com.gammon.qs.domain.Page;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PageRepositoryRemoteAsync {
	void obtainPageListByBill(Bill bill, AsyncCallback<List<Page>> callback) throws DatabaseOperationException;
}
