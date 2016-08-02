package com.gammon.qs.dao;

import java.util.List;

import org.apache.commons.validator.GenericValidator;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import com.gammon.jde.webservice.serviceRequester.InsertRepackagingBudgetManager.getInsertRepackagingBudget.InsertRepackagingBudgetRequestObj;
import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.RepackagingDetail;
import com.gammon.qs.domain.Repackaging;
import com.gammon.qs.wrapper.RepackagingDetailComparisonWrapper;
@Repository
public class RepackagingDetailHBDao extends BaseHibernateDao<RepackagingDetail> {
	
	public RepackagingDetailHBDao() {
		super(RepackagingDetail.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<RepackagingDetailComparisonWrapper> searchRepackagingDetails(	Repackaging repackaging, String packageNo,
																				String objectCode, String subsidiaryCode) throws Exception {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.createAlias("repackaging", "repackaging");
		criteria.add(Restrictions.eq("repackaging.id", repackaging.getId()));
		if(!GenericValidator.isBlankOrNull(packageNo)){
			packageNo = packageNo.replace("*", "%");
			if(packageNo.contains("%")){
				criteria.add(Restrictions.like("this.packageNo", packageNo));
			}
			else
				criteria.add(Restrictions.eq("this.packageNo", packageNo));
		}
		if(!GenericValidator.isBlankOrNull(objectCode)){
			objectCode = objectCode.replace("*", "%");
			if(objectCode.contains("%")){
				criteria.add(Restrictions.like("this.objectCode", objectCode));
			}
			else
				criteria.add(Restrictions.eq("this.objectCode", objectCode));
		}
		if(!GenericValidator.isBlankOrNull(subsidiaryCode)){
			subsidiaryCode = subsidiaryCode.replace("*", "%");
			if(subsidiaryCode.contains("%")){
				criteria.add(Restrictions.like("this.subsidiaryCode", subsidiaryCode));
			}
			else
				criteria.add(Restrictions.eq("this.subsidiaryCode", subsidiaryCode));
		}
		criteria.setProjection(Projections.projectionList().add(Projections.property("packageNo"), "packageNo")
				.add(Projections.property("objectCode"), "objectCode")
				.add(Projections.property("subsidiaryCode"), "subsidiaryCode")
				.add(Projections.property("resourceDescription"), "description")
				.add(Projections.property("unit"), "unit")
				.add(Projections.property("rate"), "rate")
				.add(Projections.property("amount"), "amount")
				.add(Projections.property("resourceType"), "resourceType"));
		criteria.setResultTransformer(new AliasToBeanResultTransformer(RepackagingDetailComparisonWrapper.class));
		return (List<RepackagingDetailComparisonWrapper>)criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<InsertRepackagingBudgetRequestObj> preparePostingOfRepackagingDetails(Repackaging repackagingEntry) throws DatabaseOperationException{
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.createAlias("repackaging", "repackaging");
			criteria.add(Restrictions.eq("repackaging.id", repackagingEntry.getId()));
			criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("objectCode"), "objectAccount")
					.add(Projections.groupProperty("subsidiaryCode"), "subsidiary")
					.add(Projections.groupProperty("packageNo"), "subledger")
					.add(Projections.sum("amount"), "amountField"));
			criteria.setResultTransformer(new AliasToBeanResultTransformer(InsertRepackagingBudgetRequestObj.class));
			return (List<InsertRepackagingBudgetRequestObj>)criteria.list();
		}catch(HibernateException ex){
			throw new DatabaseOperationException(ex);
		}
	}

	@SuppressWarnings("unchecked")
	public List<RepackagingDetail> obtainRepackagingDetail(Repackaging repackagingEntry) {
		Criteria criteria = getSession().createCriteria(this.getType());
		criteria.add(Restrictions.eq("repackaging", repackagingEntry));
		return criteria.list();
	}

}
