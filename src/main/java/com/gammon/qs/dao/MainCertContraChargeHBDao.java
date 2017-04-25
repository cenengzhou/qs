package com.gammon.qs.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.qs.application.exception.DatabaseOperationException;
import com.gammon.qs.domain.MainCert;
import com.gammon.qs.domain.MainCertContraCharge;
@Repository
public class MainCertContraChargeHBDao extends BaseHibernateDao<MainCertContraCharge> {

	public MainCertContraChargeHBDao() {
		super(MainCertContraCharge.class);
	}
	
	public MainCertContraCharge obtainMainCertContraCharge(String objectCode, String subsidCode, MainCert mainCert) throws DatabaseOperationException{
		MainCertContraCharge result = null;
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("mainCertificate", mainCert));
			criteria.add(Restrictions.eq("objectCode", objectCode));
			criteria.add(Restrictions.eq("subsidiary", subsidCode));
			result = (MainCertContraCharge)criteria.uniqueResult();
		}catch (HibernateException ex){
			throw new DatabaseOperationException(ex);
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<MainCertContraCharge> obtainMainCertContraChargeList(MainCert mainCert) throws DatabaseOperationException{
		List<MainCertContraCharge> result = null;
		try {
			Criteria criteria = getSession().createCriteria(this.getType());
			criteria.add(Restrictions.eq("mainCertificate", mainCert));
			result = criteria.list();
		}catch (HibernateException ex){
			throw new DatabaseOperationException(ex);
		}
		return result;
	}
	
}
