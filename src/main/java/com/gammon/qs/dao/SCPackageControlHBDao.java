package com.gammon.qs.dao;

import org.springframework.stereotype.Repository;

import com.gammon.qs.domain.poweruserAdmin.SCPackageControl;
@Repository
public class SCPackageControlHBDao extends BaseHibernateDao<SCPackageControl>{

	public SCPackageControlHBDao() {
		super(SCPackageControl.class);
	}

}
