package com.gammon.qs.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.client.repository.MainCertificateAttachmentRepositoryRemote;
import com.gammon.qs.dao.MainCertificateAttachmentHBDao;
import com.gammon.qs.domain.MainCertificateAttachment;
import com.gammon.qs.domain.MainContractCertificate;
@Service
public class MainCertificateAttachmentRepositoryController extends GWTSpringController implements MainCertificateAttachmentRepositoryRemote {

	private static final long serialVersionUID = 2862364775079201255L;
	@Autowired
	private MainCertificateAttachmentHBDao mainCertificateAttachmentHBDao;
	
	@Override
	public List<MainCertificateAttachment> obtainMainCertificateAttachment(
			MainContractCertificate mainContractCertificate) throws DatabaseOperationException {
		return mainCertificateAttachmentHBDao.obtainMainCertAttachmentList(mainContractCertificate);
	}

}
