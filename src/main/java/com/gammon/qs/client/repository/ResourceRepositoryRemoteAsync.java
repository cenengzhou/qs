package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.BQItem;
import com.gammon.qs.domain.Resource;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ResourceRepositoryRemoteAsync {
	void obtainResourceListByBQItem(BQItem bqItem, AsyncCallback<List<Resource>> callback) throws DatabaseOperationException;
}
