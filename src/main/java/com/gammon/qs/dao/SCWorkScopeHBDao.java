package com.gammon.qs.dao;

import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.SCPackage;
import com.gammon.qs.domain.SCWorkScope;
@Repository
public class SCWorkScopeHBDao extends BaseHibernateDao<SCWorkScope> {

	public SCWorkScopeHBDao() {
		super(SCWorkScope.class);
	}

	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(SCWorkScope.class.getName());

	@SuppressWarnings("unchecked")
	public List<SCWorkScope> obtainSCWorkScopeListByPackage(SCPackage scPackage) throws DatabaseOperationException{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("scPackage", scPackage));
			return (List<SCWorkScope>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public void deleteSCWorkScopeBySCPackage(SCPackage scPackage) throws DatabaseOperationException{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("scPackage", scPackage));
		for(SCWorkScope scWorkScope: (List<SCWorkScope>) criteria.list()){
			delete(scWorkScope);
		}
	}
}