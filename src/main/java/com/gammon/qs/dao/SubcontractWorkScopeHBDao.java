package com.gammon.qs.dao;

import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.Subcontract;
import com.gammon.qs.domain.SubcontractWorkScope;
@Repository
public class SubcontractWorkScopeHBDao extends BaseHibernateDao<SubcontractWorkScope> {

	public SubcontractWorkScopeHBDao() {
		super(SubcontractWorkScope.class);
	}

	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(SubcontractWorkScope.class.getName());

	@SuppressWarnings("unchecked")
	public List<SubcontractWorkScope> obtainSCWorkScopeListByPackage(Subcontract subcontract) throws DatabaseOperationException{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("subcontract", subcontract));
			return (List<SubcontractWorkScope>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public void deleteSCWorkScopeBySCPackage(Subcontract subcontract) throws DatabaseOperationException{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("subcontract", subcontract));
		for(SubcontractWorkScope scWorkScope: (List<SubcontractWorkScope>) criteria.list()){
			delete(scWorkScope);
		}
	}
}