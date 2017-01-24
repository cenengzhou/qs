package com.gammon.pcms.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.model.TenderVariance;
import com.gammon.qs.dao.BaseHibernateDao;

@Repository
public class TenderVarianceHBDao extends BaseHibernateDao<TenderVariance> {

	public TenderVarianceHBDao() {
		super(TenderVariance.class);
	}

	@SuppressWarnings("unchecked")
	public List<TenderVariance> obtainTenderVarianceList(String jobNo, String subcontractNo, String subcontractorNo) {
		try{
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("noJob", jobNo));
			criteria.add(Restrictions.eq("noSubcontract", subcontractNo));
			criteria.add(Restrictions.eq("noSubcontractor", subcontractorNo));
			return criteria.list();
		}catch (HibernateException he){
			he.printStackTrace();
		}
		return null;
	}

}
