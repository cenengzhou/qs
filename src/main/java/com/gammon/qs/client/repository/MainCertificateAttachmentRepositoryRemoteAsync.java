package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.MainCertificateAttachment;
import com.gammon.qs.domain.MainContractCertificate;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MainCertificateAttachmentRepositoryRemoteAsync {
	void obtainMainCertificateAttachment(MainContractCertificate mainContractCertificate, AsyncCallback<List<MainCertificateAttachment>> callback) throws DatabaseOperationException;
}
