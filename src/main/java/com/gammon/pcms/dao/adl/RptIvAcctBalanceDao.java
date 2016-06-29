package com.gammon.pcms.dao.adl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.gammon.pcms.model.adl.DimAccountMaster;
import com.gammon.pcms.model.adl.RptIvAcctBalance;
import com.gammon.qs.application.exception.DatabaseOperationException;

@Repository
public class RptIvAcctBalanceDao extends BaseAdlHibernateDao<RptIvAcctBalance> {

	public RptIvAcctBalanceDao() {
		super(RptIvAcctBalance.class);
	}

	@SuppressWarnings("unchecked")
	public Collection<? extends RptIvAcctBalance> obtainRptIvAcctBalance(DimAccountMaster dimAccountMaster,
			BigDecimal fiscalYear, String accountPeriod) throws DatabaseOperationException {
		Criteria criteria = getSession().createCriteria(getType());
		criteria.add(Restrictions.eq("dimAccountMaster", dimAccountMaster));
		criteria.add(Restrictions.eq("fiscalYear", fiscalYear));
		criteria.add(Restrictions.eq("accountPeriod", accountPeriod));
		List<RptIvAcctBalance> rptIvAcctBalanceList = criteria.list();
		return rptIvAcctBalanceList;
	}
}
