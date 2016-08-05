package com.gammon.pcms.dao.adl;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.model.adl.BusinessUnit;

@Repository
public class BusinessUnitDao extends BaseAdlHibernateDao<BusinessUnit> {

	public BusinessUnitDao() {
		super(BusinessUnit.class);
	}

	public BusinessUnit find(String jobNo) throws DataAccessException {
		Criteria criteria = getSession().createCriteria(getType());

		// Formatting
		jobNo = StringUtils.leftPad(StringUtils.defaultString(jobNo), 12);

		// Where
		criteria.add(Restrictions.eq("businessUnitCode", jobNo));

		return (BusinessUnit) criteria.uniqueResult();
	}
}
