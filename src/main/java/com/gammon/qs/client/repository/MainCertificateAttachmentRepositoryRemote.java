package com.gammon.qs.client.repository;

import java.util.List;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.MainCertificateAttachment;
import com.gammon.qs.domain.MainContractCertificate;
import com.google.gwt.user.client.rpc.RemoteService;

public interface MainCertificateAttachmentRepositoryRemote extends RemoteService {
	public List<MainCertificateAttachment> obtainMainCertificateAttachment(MainContractCertificate mainContractCertificate) throws DatabaseOperationException;
}
