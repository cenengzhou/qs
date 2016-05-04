package com.gammon.qs.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.client.repository.UserAccessRightsRepositoryRemote;
import com.gammon.qs.service.UserAccessRightsService;

import net.sf.gilead.core.PersistentBeanManager;
@Service
public class UserAccessRightsRepositoryController extends GWTSpringController implements UserAccessRightsRepositoryRemote{

	private static final long serialVersionUID = 6074669952420184919L;
	@Autowired
	private UserAccessRightsService userAccessRightsRepository;
	@Override
	@Autowired
	public void setBeanManager(PersistentBeanManager manager) {
		super.setBeanManager(manager);
	}
	
	public List<String> getAccessRights(String username, String functionName) throws Exception {
		return this.userAccessRightsRepository.getAccessRights(username, functionName);
	}
	
	public List<ArrayList<String>> obtainAccessRightsByUserLists(List<String> usernames, String functionName) throws Exception{
		return this.userAccessRightsRepository.obtainAccessRightsByUserLists(usernames, functionName);
	}
}
