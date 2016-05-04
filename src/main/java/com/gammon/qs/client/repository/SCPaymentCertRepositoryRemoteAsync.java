package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.SCPaymentCert;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SCPaymentCertRepositoryRemoteAsync {
	void obtainSCPaymentCertListByPackageNo(String jobNumber, Integer packageNo, AsyncCallback<List<SCPaymentCert>> callback) throws DatabaseOperationException;
}
