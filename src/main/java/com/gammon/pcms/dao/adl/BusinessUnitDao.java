package com.gammon.pcms.dao.adl;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.model.adl.BusinessUnit;

@Repository
public class BusinessUnitDao extends BaseAdlHibernateDao<BusinessUnit>{

	public BusinessUnitDao() {
		super(BusinessUnit.class);
	}

	public BusinessUnit getBusinessUnit(String businessUnitCode) {
		Criteria criteria = getSession().createCriteria(getType());
		businessUnitCode = StringUtils.leftPad(StringUtils.defaultString(businessUnitCode), 12);
		criteria.add(Restrictions.eq("businessUnitCode", businessUnitCode));
		BusinessUnit result = (BusinessUnit) criteria.uniqueResult();
		return result;
	}
}
