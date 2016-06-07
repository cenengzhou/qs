package com.gammon.qs.service.user;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Component;
@Component
public class LdapService {
	@Autowired
	private LdapTemplate ldapTemplate;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getObjectAttribute(String username, final String attributeName) {
		List result = new ArrayList();
		try {
			System.err.println(ldapTemplate.getContextSource().toString());			
			ldapTemplate.setIgnorePartialResultException(true);
			result = ldapTemplate.search("", "(sAMAccountName="+username+")", new AttributesMapper() {
				public Object mapFromAttributes(Attributes attrs)  throws NamingException {
					return (String)attrs.get(attributeName).get();
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (result.size() == 1)
			return (String)result.get(0);
		else
			return null;
	}

}
