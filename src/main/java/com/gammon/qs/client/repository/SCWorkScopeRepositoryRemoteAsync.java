package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.domain.SCWorkScope;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SCWorkScopeRepositoryRemoteAsync {
	void obtainSCWorkScopeListBySCPackage(SCPackage scPackage, AsyncCallback<List<SCWorkScope>> callback) throws DatabaseOperationException;
	void deleteSCWorkScopeBySCPackage(SCPackage scPackage, AsyncCallback<Void> callback) throws DatabaseOperationException;
}
