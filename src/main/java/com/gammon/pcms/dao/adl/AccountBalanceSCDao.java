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

import com.gammon.pcms.model.adl.AccountBalance;
import com.gammon.pcms.model.adl.AccountBalanceSC;

@Repository
public class AccountBalanceSCDao extends BaseAdlHibernateDao<AccountBalanceSC> {

	public AccountBalanceSCDao() {
		super(AccountBalanceSC.class);
	}

	@SuppressWarnings("unchecked")
	public List<AccountBalanceSC> find(	BigDecimal year,
										BigDecimal month,
										String ledgerType,
										String jobNo,
										String subcontractNo,
										String objectCode,
										String subsidiaryCode) throws DataAccessException {
		Criteria criteria = getSession().createCriteria(getType());

		// Data Formatting
		ledgerType = ledgerType.toUpperCase();
		jobNo = StringUtils.leftPad(StringUtils.defaultString(jobNo), 12);
		subcontractNo = StringUtils.isNotBlank(subcontractNo) ? StringUtils.rightPad(StringUtils.defaultString(subcontractNo), 8) : "";

		// Where
		criteria.add(Restrictions.eq("accountTypeLedger", ledgerType))
				.add(Restrictions.eq("entityBusinessUnitKey", jobNo));
		
		// Where (optional)
		if (year.intValue() > 0)
			criteria.add(Restrictions.eq("fiscalYear", year));
		if (month.intValue() > 0)
			criteria.add(Restrictions.eq("accountPeriod", month));
		if (StringUtils.isNotBlank(subcontractNo)) {
			criteria.add(Restrictions.eq("accountSubLedger", subcontractNo));
			criteria.add(Restrictions.eq("accountTypeSubLedger", AccountBalance.TYPE_SUBLEDGER_X));
		}
		if (StringUtils.isNotBlank(objectCode))
			criteria.add(Restrictions.ilike("accountObject", objectCode, MatchMode.START));
		if (StringUtils.isNotEmpty(subsidiaryCode))
			criteria.add(Restrictions.ilike("accountSubsidiary", subsidiaryCode, MatchMode.START));

		// Order By
		criteria.addOrder(Order.asc("fiscalYear"))
				.addOrder(Order.asc("accountPeriod"))
				.addOrder(Order.asc("accountObject"))
				.addOrder(Order.asc("accountSubsidiary"))
				.addOrder(Order.asc("accountSubLedger"));

		return new ArrayList<AccountBalanceSC>(criteria.list());
	}

}
