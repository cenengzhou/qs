package com.gammon.qs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.client.repository.UserAccessJobsRepositoryRemote;
import com.gammon.qs.service.UserAccessJobsService;

import net.sf.gilead.core.PersistentBeanManager;

@Service
public class UserAccessJobsRepositoryController extends GWTSpringController implements UserAccessJobsRepositoryRemote{

	private static final long serialVersionUID = 4139355819543017818L;
	@Autowired
	private UserAccessJobsService userAccessJobsRepository;
	@Override
	@Autowired
	public void setBeanManager(PersistentBeanManager manager) {
		super.setBeanManager(manager);
	}
	
	public Boolean canAccessJob(String username, String jobNumber)throws Exception {
		return this.userAccessJobsRepository.canAccessJob(username, jobNumber);
	}	

}
