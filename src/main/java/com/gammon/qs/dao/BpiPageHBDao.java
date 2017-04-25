package com.gammon.qs.dao;


import java.util.logging.Logger;

import org.springframework.stereotype.Repository;

import com.gammon.qs.domain.BpiPage;
@Repository
public class BpiPageHBDao extends BaseHibernateDao<BpiPage> {

	public BpiPageHBDao() {
		super(BpiPage.class);
	}

	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(BpiPageHBDao.class.getName());

}