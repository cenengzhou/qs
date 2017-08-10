package com.gammon.qs.dao;


import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.AppSubcontractStandardTerms;
@Repository
public class AppSubcontractStandardTermsHBDao extends BaseHibernateDao<AppSubcontractStandardTerms>{
	public AppSubcontractStandardTermsHBDao() {
		super(AppSubcontractStandardTerms.class);
	}
	
	private Logger logger = Logger.getLogger(AppSubcontractStandardTermsHBDao.class.getName());
	
	public AppSubcontractStandardTerms getSCStandardTerms(String formOfSubcontract, String company)
			throws DatabaseOperationException {
		AppSubcontractStandardTerms result;
		try{
			logger.info(company+"-"+formOfSubcontract);
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.eq("formOfSubcontract", formOfSubcontract));
			criteria.add(Restrictions.eq("company", company));
			result = (AppSubcontractStandardTerms) criteria.uniqueResult();
		}catch (HibernateException he){
			logger.info("Fail: getSCStandardTerms(String formOfSubcontract, String company)");
			throw new DatabaseOperationException(he);
		}
		return result;
	}
	
	
	/**
	 * modified my matthewlam, 2015-01-29
	 * Bug fix #77 - Unable to inactivate System Constant records
	 */
	@SuppressWarnings("unchecked")
	public List<AppSubcontractStandardTerms> getSCStandardTermsList()  throws DatabaseOperationException {
		List<AppSubcontractStandardTerms> resultList;
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));
			
			criteria.addOrder(Order.asc("company"));
			criteria.addOrder(Order.asc("formOfSubcontract"));
			
			resultList = criteria.list();
			
		} catch(HibernateException he) {
			throw new DatabaseOperationException(he);
		}
		return resultList;
	}
	
	public Boolean updateMultipleSystemConstants(List<AppSubcontractStandardTerms> requests, String user) throws DatabaseOperationException {
		try {
			Session session = getSession();
			for(AppSubcontractStandardTerms request : requests) {
				Criteria criteria = session.createCriteria(this.getType());
				criteria.add(Restrictions.eq("formOfSubcontract", request.getFormOfSubcontract()));
				criteria.add(Restrictions.eq("company", request.getCompany()));
				
				AppSubcontractStandardTerms systemConstantToUpdate = (AppSubcontractStandardTerms)criteria.uniqueResult();
				if( systemConstantToUpdate == null ) {
					throw new DatabaseOperationException("Could not find an existing Standard Terms with company = " + request.getCompany() + " and formOfSubcontract = " + request.getFormOfSubcontract() + ". Some records were not updated.");
				}
				
				request.setLastModifiedUser(user);
				
				systemConstantToUpdate.update(request);
			}
			session.flush();
		} catch(HibernateException he) {
			logger.info("Failed to update system constants with HibernateException: " + he.getMessage());
			throw new DatabaseOperationException(he);
		}
		return true;
	}
	
	public Boolean createSystemConstant(AppSubcontractStandardTerms request, String user)  throws DatabaseOperationException {
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("formOfSubcontract", request.getFormOfSubcontract()));
			criteria.add(Restrictions.eq("company", request.getCompany()));
			AppSubcontractStandardTerms result = (AppSubcontractStandardTerms)criteria.uniqueResult();
			if(result != null) { throw new DatabaseOperationException("A SC Standard Tersm with the same formOfSubcontract and company already exists"); }
		
			request.setCreatedDate(new Date());
			request.setCreatedUser(user);
			
			Session session = getSession();
			
		
			session.save(request);
		} catch(HibernateException he) {
			logger.info("Failed to create system constant with HibernateException: " + he.getMessage());
			throw new DatabaseOperationException(he);
		}
		return true;
	}



}
