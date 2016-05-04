package com.gammon.qs.web.gwt;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gammon.qs.application.Authority;
import com.gammon.qs.application.User;
import com.gammon.qs.client.repository.UserServiceRemote;
import com.gammon.qs.service.admin.AdminService;
import com.gammon.qs.web.GWTSpringController;

import net.sf.gilead.core.PersistentBeanManager;
@Service
public class UserServiceController extends GWTSpringController implements UserServiceRemote{
	
	private static final long serialVersionUID = -3849918752983660383L;
	@Autowired
	private AdminService adminService;
	@Override
	@Autowired
	public void setBeanManager(PersistentBeanManager manager) {
		super.setBeanManager(manager);
	}
	public User getUserDetails() {
		String username = this.getThreadLocalRequest().getRemoteUser();
		User dbUser =null;
		try {
			dbUser = adminService.getUserByUsername(username);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		//What if dbUser is still null?
		User result = new User();
		result.setId(dbUser.getId());
		result.setUsername(dbUser.getUsername());
		result.setFullname(dbUser.getFullname());
		
		for(Authority dbAuthority:dbUser.getAuthorities()) {
			Authority authority = new Authority();
			authority.setName(dbAuthority.getName());
			result.addAuthority(authority);
		}
		
		Set<String> keySet = dbUser.getScreenPreferences().keySet();
		for(String key:keySet) {
			result.getScreenPreferences().put(key, dbUser.getScreenPreferences().get(key));
		}
		
		keySet = dbUser.getGeneralPreferences().keySet();
		for(String key:keySet) {
			result.getGeneralPreferences().put(key, dbUser.getGeneralPreferences().get(key));
		}
		
		return result; 
	}

	public void saveUserScreenPreferences(User user) {
		try {
			this.adminService.saveUserScreenPreferences(user);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	public void saveUserGeneralPreferences(User user){
		try {
			this.adminService.saveUserGeneralPreferences(user);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}
