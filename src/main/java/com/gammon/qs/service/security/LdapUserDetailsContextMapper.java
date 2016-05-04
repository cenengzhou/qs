package com.gammon.qs.service.security;

import java.util.Collection;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;

import com.gammon.qs.application.User;
import com.gammon.qs.service.admin.AdminService;

public class LdapUserDetailsContextMapper extends LdapUserDetailsMapper {
	private AdminService adminService;
	@Override
	public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
		SpringSecurityUser user = null;
		try {
			User internalUser = adminService.getUserByUsername(username);
			
			if (internalUser == null) {
				User dummyUser = new User();
				dummyUser.setUsername(username);
				user = new SpringSecurityUser(dummyUser);
			} else {
				internalUser.setFullname(ctx.getStringAttribute("name"));
				user = new SpringSecurityUser(internalUser);
				
				adminService.saveUser(internalUser, "SYSTEM");
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return user;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}
}
