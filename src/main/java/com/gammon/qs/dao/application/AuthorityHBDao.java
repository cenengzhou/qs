package com.gammon.qs.dao.application;

import org.springframework.stereotype.Repository;

import com.gammon.qs.application.Authority;
import com.gammon.qs.dao.BaseHibernateDao;

@Repository
public class AuthorityHBDao extends BaseHibernateDao<Authority> {
	
	public AuthorityHBDao() {
		super(Authority.class);
	}
	
}
