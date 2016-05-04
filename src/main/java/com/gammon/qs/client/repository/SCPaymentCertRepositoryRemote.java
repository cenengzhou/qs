package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.SCPaymentCert;
import com.google.gwt.user.client.rpc.RemoteService;

public interface SCPaymentCertRepositoryRemote extends RemoteService {
	public List<SCPaymentCert> obtainSCPaymentCertListByPackageNo(String jobNumber, Integer packageNo) throws DatabaseOperationException;
}
