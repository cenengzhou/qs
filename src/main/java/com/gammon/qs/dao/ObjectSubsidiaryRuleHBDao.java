package com.gammon.qs.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.dao.BaseHibernateDao;
import com.gammon.qs.domain.ObjectSubsidiaryRule;
import com.gammon.qs.domain.ObjectSubsidiaryRule.ObjectSubsidiaryRuleUpdateResponse;
import com.gammon.qs.wrapper.PaginationWrapper;

@Repository
public class ObjectSubsidiaryRuleHBDao extends BaseHibernateDao<ObjectSubsidiaryRule> {
	
	public static final int RECORDS_PER_PAGE = 100;

	public ObjectSubsidiaryRuleHBDao() {
		super(ObjectSubsidiaryRule.class);
	}

	@SuppressWarnings("unchecked")
	public List<ObjectSubsidiaryRule> getObjectSubsidiaryRule(ObjectSubsidiaryRule searchingObj){
		Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
		if (searchingObj.getCostCategory()!=null)
			criteria.add(Restrictions.eq("costCategory", searchingObj.getCostCategory()));
		if (searchingObj.getMainTrade()!=null)
			criteria.add(Restrictions.eq("mainTrade", searchingObj.getMainTrade()));
		if (searchingObj.getResourceType()!=null)
			criteria.add(Restrictions.eq("resourceType", searchingObj.getResourceType()));
		if (searchingObj.getApplicable()!=null)
			criteria.add(Restrictions.eq("applicable", searchingObj.getApplicable()));
		return criteria.list();
	}
	
	public Boolean updateObjectSubsidiaryRule(ObjectSubsidiaryRule oldRule, ObjectSubsidiaryRule newRule) throws DatabaseOperationException {
		try {
			Session session = this.getSessionFactory().getCurrentSession();
			
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			applyRestrictions(criteria, newRule.getResourceType(), newRule.getCostCategory(), newRule.getMainTrade());
			criteria.add(Restrictions.eq("applicable", newRule.getApplicable()));
			ObjectSubsidiaryRule osr = (ObjectSubsidiaryRule)criteria.uniqueResult();
			if(osr != null) { throw new DatabaseOperationException("Updating rule " + newRule.getResourceType() + "/" + newRule.getCostCategory() + "/" + newRule.getMainTrade() + " failed: Update would create duplicate Object Subsidiary Rule"); }
					
			criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			applyRestrictions(criteria, oldRule.getResourceType(), oldRule.getCostCategory(), oldRule.getMainTrade());
			criteria.add(Restrictions.eq("applicable", oldRule.getApplicable()));
			osr = (ObjectSubsidiaryRule)criteria.uniqueResult();
			if(osr == null) { throw new DatabaseOperationException("Updating rule " + newRule.getResourceType() + "/" + newRule.getCostCategory() + "/" + newRule.getMainTrade() + " failed: Object Subsidiary Rule does not exist; cannot update"); }
			
			osr.update(newRule);
			session.flush();
		} catch (HibernateException ex) {
			throw new DatabaseOperationException(ex);
		}
		return true;
	}
	
	/**
	 * @author matthewatc
	 * 13:47:43 13 Jan 2012 (UTC+8)
	 * @param rules - this is a List of arrays the elements of which are of the class ObjectSubsidiaryRule, where each array has 
	 * as its first element the ObjectSubsidiaryRule that is to be updated, and has as its second element an ObjectSubsidiaryRule 
	 * representing the desired updated state of the OSR.
	 * @return an ArrayList of ObjectSubsidiaryRuleUpdateResponse objects, each corresponding to an update request as follows:
	 * 			oldRule - ObjectSubsidiaryRule - the original state of the OSR to be updated
	 * 			newRule - ObjectSubsidiaryRule - the state that it was to be updated to
	 * 			updated - boolean - true iff the update was successful
	 * 			message - String - a descriptive status message regarding the update (or lack thereof)
	 */
	public List<ObjectSubsidiaryRuleUpdateResponse> updateMultipleObjectSubsidiaryRules(List<ObjectSubsidiaryRule[]> rules) {
		ArrayList<ObjectSubsidiaryRuleUpdateResponse> retList = new ArrayList<ObjectSubsidiaryRuleUpdateResponse>();
		for(ObjectSubsidiaryRule[] osrArray : rules) {
			ObjectSubsidiaryRule oldRule = osrArray[0];
			ObjectSubsidiaryRule newRule = osrArray[1];
			ObjectSubsidiaryRuleUpdateResponse response = new ObjectSubsidiaryRuleUpdateResponse();
			response.oldRule = oldRule;
			response.newRule = newRule;
			response.updated = false;
			try {
				boolean skip = false;
				Session session = this.getSessionFactory().getCurrentSession();
				Criteria criteria;
				ObjectSubsidiaryRule osr;
			
				if(oldRule.matches(newRule, false)) {
					if(oldRule.getApplicable().equals(newRule.getApplicable())) {
						response.updated = false;
						response.message = "Rule " + oldRule.getResourceType() + "/" + oldRule.getCostCategory() + "/" + oldRule.getMainTrade() + " is already in the requested state.";
						skip = true;
					}
				} else {
					criteria = session.createCriteria(this.getType());
					applyRestrictions(criteria, newRule.getResourceType(), newRule.getCostCategory(), newRule.getMainTrade());
					osr = (ObjectSubsidiaryRule)criteria.uniqueResult();
					if(osr != null) { 
						response.message = "Updating rule " + oldRule.getResourceType() + "/" + oldRule.getCostCategory() + "/" + oldRule.getMainTrade() + " to " + newRule.getResourceType() + "/" + newRule.getCostCategory() + "/" + newRule.getMainTrade() + " failed: Update would create duplicate Object Subsidiary Rule"; 
						response.updated = false;
						skip = true;
					}
				}
				
				if(!skip) {
					criteria = session.createCriteria(this.getType());
					applyRestrictions(criteria, oldRule.getResourceType(), oldRule.getCostCategory(), oldRule.getMainTrade());
					criteria.add(Restrictions.eq("applicable", oldRule.getApplicable()));
					osr = (ObjectSubsidiaryRule)criteria.uniqueResult();
					if(osr == null) {  
						response.message  = "Updating rule " + oldRule.getResourceType() + "/" + oldRule.getCostCategory() + "/" + oldRule.getMainTrade() + " to " + newRule.getResourceType() + "/" + newRule.getCostCategory() + "/" + newRule.getMainTrade() + " failed: Object Subsidiary Rule does not exist; cannot update";
						response.updated = false;
					} else {
						osr.update(newRule);
						session.flush();
						response.updated = true;
						response.message  =  "Updating rule " + oldRule.getResourceType() + "/" + oldRule.getCostCategory() + "/" + oldRule.getMainTrade() + " to " + newRule.getResourceType() + "/" + newRule.getCostCategory() + "/" + newRule.getMainTrade() + " was successful";
					}
				}

			} catch (HibernateException ex) {
				response.message  =  "Updating rule " + oldRule.getResourceType() + "/" + oldRule.getCostCategory() + "/" + oldRule.getMainTrade() + " to " + newRule.getResourceType() + "/" + newRule.getCostCategory() + "/" + newRule.getMainTrade() + " failed: " + ex.getMessage();
				response.updated = false;
			}
			retList.add(response);
		}
		return retList;
	}
	
	public Boolean createObjectSubsidiaryRule(ObjectSubsidiaryRule osr) throws DatabaseOperationException {
		try {
				Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
				applyRestrictions(criteria, osr.getResourceType(), osr.getCostCategory(), osr.getMainTrade());
				ObjectSubsidiaryRule res = (ObjectSubsidiaryRule)criteria.uniqueResult();
				if(res != null) { throw new DatabaseOperationException("An identical Object Subsidiary Rule already exists"); }
			
				Session session = this.getSessionFactory().getCurrentSession();
				//session.beginTransaction();
				
				session.save(osr);
				
				//session.getTransaction().commit();
		} catch (HibernateException ex) {
			throw new DatabaseOperationException(ex);
		}	
		return true;
	}
	
	/**
	 * @author matthewatc
	 * 11:20:08 30 Dec 2011 (UTC+8)
	 * method to get object subsidiary rule records by page
	 */
	@SuppressWarnings("unchecked")
	public PaginationWrapper<ObjectSubsidiaryRule> findObjectSubsidiaryRuleByPage(Integer pageNum, String resource, String costCategory, String mainTrade) throws DatabaseOperationException {
		PaginationWrapper<ObjectSubsidiaryRule> wrapper = new PaginationWrapper<ObjectSubsidiaryRule>();
		try {
			Criteria criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			applyRestrictions(criteria, resource, costCategory, mainTrade);
			criteria.setFirstResult(pageNum * RECORDS_PER_PAGE);
			criteria.setMaxResults(RECORDS_PER_PAGE); 
			
			criteria.addOrder(Order.asc("resourceType"));
			criteria.addOrder(Order.asc("costCategory"));
			criteria.addOrder(Order.asc("mainTrade"));
			wrapper.setCurrentPageContentList(criteria.list());
			
			criteria = this.getSessionFactory().getCurrentSession().createCriteria(this.getType());
			applyRestrictions(criteria, resource, costCategory, mainTrade);
			criteria.setProjection(Projections.rowCount());
			Integer count = Integer.valueOf(criteria.uniqueResult().toString());
			
			wrapper.setTotalRecords(count);
			wrapper.setCurrentPage(pageNum);
			wrapper.setTotalPage((count + RECORDS_PER_PAGE - 1)/RECORDS_PER_PAGE);

		}  catch (HibernateException ex) {
			throw new DatabaseOperationException(ex);
		}	
		return wrapper;
	}
	
	private void applyRestrictions(Criteria criteria, String resource, String costCategory, String mainTrade) {
		if(resource != null) {
			criteria.add(Restrictions.eq("resourceType", resource));
		}
		if(costCategory != null) {
			criteria.add(Restrictions.eq("costCategory", costCategory));
		}
		if(mainTrade != null) {
			criteria.add(Restrictions.eq("mainTrade", mainTrade));
		}
	}
}
