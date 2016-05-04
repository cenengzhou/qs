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
import com.gammon.qs.client.repository.SCPaymentCertRepositoryRemote;
import com.gammon.qs.dao.SCPaymentCertHBDao;
import com.gammon.qs.domain.SCPaymentCert;
@Service("scPaymentCertController")
public class SCPaymentCertController extends GWTSpringController  implements SCPaymentCertRepositoryRemote {

	private static final long serialVersionUID = -81554552416435728L;

	@Autowired
	private SCPaymentCertHBDao scPaymentCertHBDao;
	
	@Override
	public List<SCPaymentCert> obtainSCPaymentCertListByPackageNo(String jobNumber, Integer packageNo)
			throws DatabaseOperationException {
		return scPaymentCertHBDao.obtainSCPaymentCertListByPackageNo(jobNumber, packageNo);
	}

}
