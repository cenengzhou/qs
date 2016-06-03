package com.gammon.qs.dao;

import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.validator.GenericValidator;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.qs.domain.TransitUomMatch;
@Repository
public class TransitUomMatchHBDao extends BaseHibernateDao<TransitUomMatch> {
	Logger logger = Logger.getLogger(TransitUomMatchHBDao.class.getName());
	
	public TransitUomMatchHBDao() {
		super(TransitUomMatch.class);
	}
	
	public String saveAll(List<TransitUomMatch> codeMatches) throws Exception{
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
			e.printStackTrace();
			TransitUomMatch codeMatch = codeMatches.get(i);
			return "Failed at causeway uom " + codeMatch.getCausewayUom() + ". Please check if this is a duplicate entry.";
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getAllUomMatches() throws Exception{
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.setProjection(Projections.projectionList().add(Projections.property("causewayUom"))
				.add(Projections.property("jdeUom")));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<TransitUomMatch> searchTransitUomMatches(String causewayUom, String jdeUom) throws Exception{
		Criteria criteria = getSession().createCriteria(this.getType());
		if(!GenericValidator.isBlankOrNull(causewayUom)){
			causewayUom = causewayUom.trim().toUpperCase().replace("*", "%");
			if(causewayUom.contains("%"))
				criteria.add(Restrictions.like("causewayUom", causewayUom));
			else
				criteria.add(Restrictions.eq("causewayUom", causewayUom));
		}
		if(!GenericValidator.isBlankOrNull(jdeUom)){
			jdeUom = jdeUom.trim().toUpperCase().replace("*", "%");
			if(jdeUom.contains("%"))
				criteria.add(Restrictions.like("jdeUom", jdeUom));
			else
				criteria.add(Restrictions.eq("jdeUom", jdeUom));
		}
		return criteria.list();
	}
}
