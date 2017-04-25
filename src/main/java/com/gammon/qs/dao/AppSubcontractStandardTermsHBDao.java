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
	
	public AppSubcontractStandardTerms getSystemConstant(String systemCode, String company)
			throws DatabaseOperationException {
		AppSubcontractStandardTerms result;
		try{
			logger.info(systemCode+"-"+company);
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.eq("systemCode", systemCode));
			criteria.add(Restrictions.eq("company", company));
			result = (AppSubcontractStandardTerms) criteria.uniqueResult();
		}catch (HibernateException he){
			logger.info("Fail: getSystemConstant(String systemCode, String company)");
			throw new DatabaseOperationException(he);
		}
		return result;
	}
	
	/**
	 * @author tikywong
	 * created on 28 May, 2012
	 */
	public AppSubcontractStandardTerms getSystemConstant(String company) throws DatabaseOperationException {
		AppSubcontractStandardTerms systemConstant;
		try{
			logger.info("Company: "+company);
			
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.eq("company", company));
			systemConstant = (AppSubcontractStandardTerms) criteria.uniqueResult();
		}catch (HibernateException he){
			logger.info("Fail: getSystemConstant(String company)");
			throw new DatabaseOperationException(he);
		}
		return systemConstant;
	}
	
	/**
	 * modified my matthewlam, 2015-01-29
	 * Bug fix #77 - Unable to inactivate System Constant records
	 */
	@SuppressWarnings("unchecked")
	public List<AppSubcontractStandardTerms> searchSystemConstants(String systemCode, String company, String scPaymentTerm, Double scMaxRetentionPercent, Double scInterimRetentionPercent, Double scMOSRetentionPercent, String retentionType, String finQS0Review)  throws DatabaseOperationException {
		List<AppSubcontractStandardTerms> resultList;
		try {
			logger.info("Searching system constants with parameters: systemcode = '" + systemCode + "', company = '" + company + "', scPaymentTerm = '" + scPaymentTerm 
					+ "', scMaxRetentionPercent = '" + scMaxRetentionPercent + "', scInterimRetentionPercent = '" + scInterimRetentionPercent
					+ "', scMOSRetentionPercent = '" + scMOSRetentionPercent + "', retentionType = '" + retentionType + "', finQS0Review = '" + finQS0Review + "'");
			
			Criteria criteria = getSession().createCriteria(this.getType());
		
			if(systemCode != null) 					{ criteria.add(Restrictions.eq("systemCode", 				systemCode)); }
			if(company != null) 					{ criteria.add(Restrictions.eq("company", 					company)); }
			if(scPaymentTerm != null) 				{ criteria.add(Restrictions.eq("scPaymentTerm", 			scPaymentTerm)); }
			if(scMaxRetentionPercent != null) 		{ criteria.add(Restrictions.eq("scMaxRetentionPercent", 	scMaxRetentionPercent)); }
			if(scInterimRetentionPercent != null)	{ criteria.add(Restrictions.eq("scInterimRetentionPercent", scInterimRetentionPercent)); }
			if(scMOSRetentionPercent != null) 		{ criteria.add(Restrictions.eq("scMOSRetentionPercent", 	scMOSRetentionPercent)); }
			if(retentionType != null) 				{ criteria.add(Restrictions.eq("retentionType", 			retentionType)); }
			if(finQS0Review != null) 				{ criteria.add(Restrictions.eq("finQS0Review", 				finQS0Review)); }
			criteria.add(Restrictions.eq("systemStatus", "ACTIVE"));

			criteria.addOrder(Order.asc("systemCode"));
			criteria.addOrder(Order.asc("company"));
			
			resultList = criteria.list();
			
		} catch(HibernateException he) {
			logger.info("Failed to search system constants with HibernateException: " + he.getMessage());
			throw new DatabaseOperationException(he);
		}
		return resultList;
	}
	
	public Boolean updateMultipleSystemConstants(List<AppSubcontractStandardTerms> requests, String user) throws DatabaseOperationException {
		try {
			Session session = getSession();
			for(AppSubcontractStandardTerms request : requests) {
				Criteria criteria = session.createCriteria(this.getType());
				criteria.add(Restrictions.eq("systemCode", request.getSystemCode()));
				criteria.add(Restrictions.eq("company", request.getCompany()));
				
				AppSubcontractStandardTerms systemConstantToUpdate = (AppSubcontractStandardTerms)criteria.uniqueResult();
				if( systemConstantToUpdate == null ) {
					throw new DatabaseOperationException("Could not find an existing SystemConstant with systemcode = " + request.getSystemCode() + " and company = " + request.getCompany() + ". Some records were not updated.");
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
			criteria.add(Restrictions.eq("systemCode", request.getSystemCode()));
			criteria.add(Restrictions.eq("company", request.getCompany()));
			AppSubcontractStandardTerms result = (AppSubcontractStandardTerms)criteria.uniqueResult();
			if(result != null) { throw new DatabaseOperationException("A SystemConstant with the same systemCode and company already exists"); }
		
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

	public Boolean inactivateSystemConstant(AppSubcontractStandardTerms request, String user) throws DatabaseOperationException {
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			AppSubcontractStandardTerms record = (AppSubcontractStandardTerms) criteria.add(Restrictions.eq("systemCode", request.getSystemCode()))
					.add(Restrictions.eq("company", request.getCompany()))
					.uniqueResult();
			record.setSystemStatus("INACTIVE");
			getSession().save(record);
		} catch(HibernateException he) {
			logger.info("Failed to create system constant with HibernateException: " + he.getMessage());
			throw new DatabaseOperationException(he);
		}
		return true;
	}


}
