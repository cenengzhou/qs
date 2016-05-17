package com.gammon.qs.dao;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.BasePersistedAuditObject;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.SystemConstant;
@Repository
public class SystemConstantHBDao extends BaseHibernateDao<SystemConstant>{
	public SystemConstantHBDao() {
		super(SystemConstant.class);
	}
	
	private Logger logger = Logger.getLogger(SystemConstantHBDao.class.getName());
	
	public SystemConstant getSystemConstant(String systemCode, String company)
			throws DatabaseOperationException {
		SystemConstant result;
		try{
			logger.info(systemCode+"-"+company);
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.eq("systemCode", systemCode));
			criteria.add(Restrictions.eq("company", company));
			result = (SystemConstant) criteria.uniqueResult();
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
	public SystemConstant getSystemConstant(String company) throws DatabaseOperationException {
		SystemConstant systemConstant;
		try{
			logger.info("Company: "+company);
			
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemStatus", BasePersistedAuditObject.ACTIVE));
			criteria.add(Restrictions.eq("company", company));
			systemConstant = (SystemConstant) criteria.uniqueResult();
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
	public List<SystemConstant> searchSystemConstants(String systemCode, String company, String scPaymentTerm, Double scMaxRetentionPercent, Double scInterimRetentionPercent, Double scMOSRetentionPercent, String retentionType, String finQS0Review)  throws DatabaseOperationException {
		List<SystemConstant> resultList;
		try {
			logger.info("Searching system constants with parameters: systemcode = '" + systemCode + "', company = '" + company + "', scPaymentTerm = '" + scPaymentTerm 
					+ "', scMaxRetentionPercent = '" + scMaxRetentionPercent + "', scInterimRetentionPercent = '" + scInterimRetentionPercent
					+ "', scMOSRetentionPercent = '" + scMOSRetentionPercent + "', retentionType = '" + retentionType + "', finQS0Review = '" + finQS0Review + "'");
			
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		
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
	
	public ArrayList<String> obtainSystemConstantsSelectionOption(String fieldName) throws DatabaseOperationException {
		ArrayList<String> resultList = new ArrayList<String>();
		try {
			logger.info("Generate System Constant Searching field candidates of "+fieldName);
			
			if(isValidField(fieldName)){		
				Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
				@SuppressWarnings("unchecked")
				List<String> data = criteria
										.add(Restrictions.eq("systemStatus", "ACTIVE"))
										.setProjection(Projections.distinct(Projections.property(fieldName)))
										.addOrder( Order.asc(fieldName))
										.list();
				for(String str : data){
					resultList.add(str);
				}
			}
			else{
				logger.info(String.format("Searching for non acceptable field: %s", fieldName));
			}
		} catch(HibernateException he) {
			logger.info("Failed to search system constants with HibernateException: " + he.getMessage());
			throw new DatabaseOperationException(he);
		}
		return resultList;
	}
	
	/*
	 * @author xeth hung
	 * 2015-05-06 13:58
	 * check if the searching field is target field.
	 */
	private boolean isValidField(String fieldName){
		ArrayList<String> tempList = new ArrayList<String>();
		tempList.add(SystemConstant.SEARCHING_FIELD_COMPANY_CODE);
		tempList.add(SystemConstant.SEARCHING_FIELD_SYSTEM_CODE);
		
		return tempList.contains(fieldName);
	}

	
	public Boolean updateMultipleSystemConstants(List<SystemConstant> requests, String user) throws DatabaseOperationException {
		try {
			Session session = getSessionFactory().getCurrentSession();
			for(SystemConstant request : requests) {
				Criteria criteria = session.createCriteria(this.getType());
				criteria.add(Restrictions.eq("systemCode", request.getSystemCode()));
				criteria.add(Restrictions.eq("company", request.getCompany()));
				
				SystemConstant systemConstantToUpdate = (SystemConstant)criteria.uniqueResult();
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
	
	public Boolean createSystemConstant(SystemConstant request, String user)  throws DatabaseOperationException {
		try {
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("systemCode", request.getSystemCode()));
			criteria.add(Restrictions.eq("company", request.getCompany()));
			SystemConstant result = (SystemConstant)criteria.uniqueResult();
			if(result != null) { throw new DatabaseOperationException("A SystemConstant with the same systemCode and company already exists"); }
		
			request.setCreatedDate(new Date());
			request.setCreatedUser(user);
			
			Session session = this.getSessionFactory().getCurrentSession();
			
		
			session.save(request);
		} catch(HibernateException he) {
			logger.info("Failed to create system constant with HibernateException: " + he.getMessage());
			throw new DatabaseOperationException(he);
		}
		return true;
	}

	public Boolean inactivateSystemConstant(SystemConstant request, String user) throws DatabaseOperationException {
		try {
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			SystemConstant record = (SystemConstant) criteria.add(Restrictions.eq("systemCode", request.getSystemCode()))
					.add(Restrictions.eq("company", request.getCompany()))
					.uniqueResult();
			record.setSystemStatus("INACTIVE");
			this.getSessionFactory().getCurrentSession().save(record);
		} catch(HibernateException he) {
			logger.info("Failed to create system constant with HibernateException: " + he.getMessage());
			throw new DatabaseOperationException(he);
		}
		return true;
	}


}