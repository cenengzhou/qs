package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.Bill;
import com.gammon.qs.domain.Page;
import com.google.gwt.user.client.rpc.RemoteService;

public interface PageRepositoryRemote extends RemoteService {
	public List<Page> obtainPageListByBill(Bill bill) throws DatabaseOperationException;
}
