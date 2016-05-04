package com.gammon.qs.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.client.repository.EnvironmentConfigRemote;
import com.gammon.qs.service.admin.EnvironmentConfig;

import net.sf.gilead.core.PersistentBeanManager;

@Service
public class EnvironmentConfigController extends GWTSpringController implements EnvironmentConfigRemote {

	private static final long serialVersionUID = 6421301054470670175L;

	@Override
	@Autowired
	public void setBeanManager(PersistentBeanManager manager) {
		super.setBeanManager(manager);
	}
	@Autowired
	private EnvironmentConfig environmentConfig;

	public String getApprovalSystemPath() {
		return environmentConfig.getApprovalSystemPath();
	}
	
}
