package com.gammon.qs.dao;

import java.util.List;

import org.apache.commons.validator.GenericValidator;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.TransitCodeMatch;
@Repository
public class TransitCodeMatchHBDao extends BaseHibernateDao<TransitCodeMatch> {
	public TransitCodeMatchHBDao() {
		super(TransitCodeMatch.class);
	}
	
	public String saveAll(List<TransitCodeMatch> codeMatches) throws Exception{
		Session session = getSession();
		int i = 0;
		try{
			for(; i < codeMatches.size(); i++){
				session.save(codeMatches.get(i));
				if(i % 20 == 0){
					session.flush();
					session.clear();
				}
			}
		}catch(HibernateException e){
			TransitCodeMatch codeMatch = codeMatches.get(i);
			return "Failed at resource code " + codeMatch.getResourceCode() + ". Please check if this is a duplicate entry.";
		}
		
		return null;
	}
	
	public TransitCodeMatch getTransitCodeMatchByResourceCode(String matchingType, String resourceCode) throws Exception{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("matchingType", matchingType));
		criteria.add(Restrictions.eq("resourceCode", resourceCode));
		return (TransitCodeMatch)criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getCodeMatchesByType(String matchingType) throws Exception{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("matchingType", matchingType));
		criteria.setProjection(Projections.projectionList().add(Projections.property("resourceCode"))
				.add(Projections.property("objectCode"))
				.add(Projections.property("subsidiaryCode")));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<TransitCodeMatch> searchTransitCodeMatches(String matchingType, String resourceCode, 
			String objectCode, String subsidiaryCode) throws DatabaseOperationException{
		Criteria criteria = getSession().createCriteria(this.getType());
		if(!GenericValidator.isBlankOrNull(matchingType))
			criteria.add(Restrictions.eq("matchingType", matchingType));
		if(!GenericValidator.isBlankOrNull(resourceCode)){
			resourceCode = resourceCode.trim().toUpperCase().replace("*", "%");
			if(resourceCode.contains("%"))
				criteria.add(Restrictions.like("resourceCode", resourceCode));
			else
				criteria.add(Restrictions.eq("resourceCode", resourceCode));
		}
		if(!GenericValidator.isBlankOrNull(objectCode)){
			objectCode = objectCode.trim().replace("*", "%");
			if(objectCode.contains("%"))
				criteria.add(Restrictions.like("objectCode", objectCode));
			else
				criteria.add(Restrictions.eq("objectCode", objectCode));
		}
		if(!GenericValidator.isBlankOrNull(subsidiaryCode)){
			subsidiaryCode = subsidiaryCode.trim().replace("*", "%");
			if(subsidiaryCode.contains("%"))
				criteria.add(Restrictions.like("subsidiaryCode", subsidiaryCode));
			else
				criteria.add(Restrictions.eq("subsidiaryCode", subsidiaryCode));
		}
		return criteria.list();
	}
}
