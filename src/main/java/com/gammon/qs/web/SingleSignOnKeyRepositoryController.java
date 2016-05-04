package com.gammon.qs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.client.repository.SingleSignOnKeyRepositoryRemote;
import com.gammon.qs.service.SingleSignOnKeyService;

import net.sf.gilead.core.PersistentBeanManager;
@Service
public class SingleSignOnKeyRepositoryController extends GWTSpringController implements SingleSignOnKeyRepositoryRemote {

	private static final long serialVersionUID = 4834137160039169019L;
	@Autowired
	private SingleSignOnKeyService singleSignOnKeyRepository;
	@Override
	@Autowired
	public void setBeanManager(PersistentBeanManager manager) {
		super.setBeanManager(manager);
	}

	public String getSingleSignOnKey(String description, String userID) throws Exception {
		return singleSignOnKeyRepository.getSingleSignOnKey(description,userID);
	}

}
