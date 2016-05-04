package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.domain.SCWorkScope;
import com.google.gwt.user.client.rpc.RemoteService;

public interface SCWorkScopeRepositoryRemote extends RemoteService {
	public List<SCWorkScope> obtainSCWorkScopeListBySCPackage(SCPackage scPackage) throws DatabaseOperationException;
	public void deleteSCWorkScopeBySCPackage(SCPackage scPackage) throws DatabaseOperationException;
}
