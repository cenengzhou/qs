package com.gammon.pcms.dao.adl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.model.adl.AccountBalanceSC;

@Repository
public class AccountBalanceSCDao extends BaseAdlHibernateDao<AccountBalanceSC> {

	public AccountBalanceSCDao() {
		super(AccountBalanceSC.class);
	}

	@SuppressWarnings("unchecked")
	public List<AccountBalanceSC> find(	BigDecimal yearStart,
										BigDecimal yearEnd,
										String typeLedger,
										String noJob,
										String codeObject,
										String codeSubsidiary) throws DataAccessException {
		Criteria criteria = getSession().createCriteria(getType());

		// Data Formatting
		typeLedger = typeLedger.toUpperCase();
		noJob = StringUtils.leftPad(StringUtils.defaultString(noJob), 12);

		// Where
		criteria.add(Restrictions.eq("accountTypeLedger", typeLedger))
				.add(Restrictions.eq("entityBusinessUnitKey", noJob));
		
		// Where (optional)
		if (yearEnd.intValue() > 0)
			criteria.add(Restrictions.le("fiscalYear", yearEnd));
		if (yearStart.intValue() > 0)
			criteria.add(Restrictions.ge("fiscalYear", yearStart));
		if (StringUtils.isNotBlank(codeObject))
			criteria.add(Restrictions.ilike("accountObject", codeObject, MatchMode.START));
		if (StringUtils.isNotEmpty(codeSubsidiary))
			criteria.add(Restrictions.ilike("accountSubsidiary", codeSubsidiary, MatchMode.START));

		// Order By
		criteria.addOrder(Order.asc("fiscalYear"))
				.addOrder(Order.asc("accountObject"))
				.addOrder(Order.asc("accountSubsidiary"));

		return new ArrayList<AccountBalanceSC>(criteria.list());
	}

}
